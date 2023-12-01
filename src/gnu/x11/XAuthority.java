
package gnu.x11;

import java.io.*;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.nio.charset.StandardCharsets;
import java.util.*;

import javax.annotation.Nonnull;

import static gnu.util.Strings.requiresNonBlank;

/**
 * An XAuthority.
 * https://gitlab.freedesktop.org/xorg/lib/libxau/-/blob/master/include/X11/Xauth.h
 * https://gitlab.freedesktop.org/xorg/lib/libxau
 */
public class XAuthority {

  @Nonnull Family family;
  @Nonnull byte[] address;
  int displayNumber;
  @Nonnull String protocolName;
  @Nonnull byte[] protocolData;

  public XAuthority(@Nonnull Family family, @Nonnull byte[] address, int displayNumber, @Nonnull String protocolName, @Nonnull byte[] protocolData) {
    this.family = family;
    this.address = address;
    if(displayNumber < 0) {
      throw new IllegalArgumentException("displayNumber was \"" + displayNumber + "\" expected >= 0.");
    }
    this.displayNumber = displayNumber;
    this.protocolName = requiresNonBlank("protocolName", protocolName);
    this.protocolData = protocolData;
  }

  /**
   * Fetches the current Xauthority entries from $HOME/.Xauthority or
   * whatever is specified in the environment variable $XAUTHORITY.
   *
   * @return the current Xauthority entries
   */
  public static List<XAuthority> getAuthorities() {
    return getAuthorities(getXAuthorityFile());
  }

  public static File getXAuthorityFile() {
    String authFilename = System.getenv("XAUTHORITY");
    if (authFilename == null || authFilename.equals("")) {
      authFilename = System.getProperty("user.home") + File.separatorChar + ".Xauthority";
    }
    return new File(authFilename);
  }

  public static List<XAuthority> getAuthorities(File file) {
    List<XAuthority> authorities = new ArrayList<>();
    try (DataInputStream in = new DataInputStream(new FileInputStream(file))) {
      Optional<XAuthority> read = read(in);
      while(read.isPresent()) {
        XAuthority current = read.get();
        authorities.add(current);
        read = read(in);
      }
    } catch(IOException ex) {
      throw new X11ClientException(ex);
    }
    return authorities;
  }

  public static Optional<XAuthority> read(DataInput in) {
    try {
      Family family = Family.getByCode(in.readUnsignedShort());
      int dataLength = in.readUnsignedShort();
      byte[] address = readBytes(in, dataLength);
      int number = Integer.parseInt(in.readUTF());
      String name = in.readUTF();
      dataLength = in.readUnsignedShort();
      byte[] data = readBytes(in, dataLength);
      return Optional.of(new XAuthority(family, address, number, name, data));
    } catch(IOException ex) {
      return Optional.empty();
    }
  }

  public static Optional<XAuthority> getAuthority(String hostName) {
    List<XAuthority> auths = getAuthorities();
    for (int i = 0; i < auths.size(); i++) {
      XAuthority auth = auths.get(i);
        switch(auth.getFamily()) {
          case WILD:
            return Optional.of(auth);
          default:
            try {
              InetAddress authAddress = InetAddress.getByName(new String(auth.getAddress(), StandardCharsets.UTF_8));
              InetAddress hostNameAddress = InetAddress.getByName(hostName);
              if (authAddress.equals(hostNameAddress)) {
                return Optional.of(auth);
              }
            }catch(UnknownHostException e) {
              //do nothing
            }
            break;
        }
    }
    return Optional.empty();
  }

  private static byte[] readBytes(DataInput in, int length) throws IOException {
    byte[] bytes = new byte[length];
    in.readFully(bytes);
    return bytes;
  }

  public enum Family {
    INTERNET(0),
    LOCAL(256),
    WILD(65535),
    KRB5PRINCIPAL(254),
    LOCALHOST(252);

    private int code;

    Family(int code) {
      this.code = code;
    }

    public int getCode() {
      return this.code;
    }

    public static Family getByCode(int code) {
      for(Family family : Family.values()) {
        if(family.code == code) {
          return family;
        }
      }
      throw new IllegalArgumentException("Unsupported code \"" + code + "\"");
    }
  }
}
