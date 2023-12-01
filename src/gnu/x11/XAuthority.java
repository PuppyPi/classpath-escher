
package gnu.x11;

import static java.util.Objects.*;

import java.io.DataInput;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * An XAuthority.
 * https://gitlab.freedesktop.org/xorg/lib/libxau/-/blob/master/include/X11/Xauth.h
 * https://gitlab.freedesktop.org/xorg/lib/libxau/-/blob/master/AuGetBest.c
 * https://gitlab.freedesktop.org/xorg/lib/libxau
 */
public class XAuthority {

  @Nonnull Family family;
  @Nonnull byte[] address;
  @Nullable Integer displayNumber;
  @Nonnull String protocolName;
  @Nonnull byte[] protocolData;

  public XAuthority(@Nonnull Family family, @Nonnull byte[] address, int displayNumber, @Nonnull String protocolName, @Nonnull byte[] protocolData) {
    this.family = requireNonNull(family);
    this.address = requireNonNull(address);
    if(displayNumber < 0) {
      throw new IllegalArgumentException("displayNumber was \"" + displayNumber + "\" expected >= 0.");
    }
    this.displayNumber = displayNumber;
    if(protocolName.isEmpty ())
      throw new IllegalArgumentException ("protocolName was empty");
    this.protocolName = requireNonNull(protocolName);
    this.protocolData = requireNonNull(protocolData);
  }

  public Family getFamily () {
    return family;
  }

  public byte[] getAddress () {
    return address;
  }

  public Integer getDisplayNumber () {
    return displayNumber;
  }

  public String getProtocolName () {
    return protocolName;
  }

  public byte[] getProtocolData () {
    return protocolData;
  }

  /**
   * Fetches the current Xauthority entries from $HOME/.Xauthority or
   * whatever is specified in the environment variable $XAUTHORITY.
   *
   * @return the current Xauthority entries
   */
  public static List<XAuthority> getAuthorities() {
    try {
      return getAuthorities(getXAuthorityFile());
    }
    catch (IOException exc) {
      throw new UncheckedIOException (exc);
    }
  }

  public static File getXAuthorityFile() {
    String authFilename = System.getenv("XAUTHORITY");
    if (authFilename == null || authFilename.equals("")) {
      authFilename = System.getProperty("user.home") + File.separatorChar + ".Xauthority";
    }
    return new File(authFilename);
  }
  
  public static List<XAuthority> getAuthorities(File file) throws IOException {
    try (InputStream in = new FileInputStream(file)) {
      return getAuthorities(new DataInputStream(in));
    }
  }

  public static List<XAuthority> getAuthorities(DataInput in) throws IOException {
    List<XAuthority> authorities = new ArrayList<>();
    Optional<XAuthority> read = read(in);
    while(read.isPresent()) {
      XAuthority current = read.get();
      authorities.add(current);
      read = read(in);
    }
    return authorities;
  }

  public static Optional<XAuthority> read(DataInput in) {
    try {
      Family family = Family.getByCode(in.readUnsignedShort());
      int dataLength = in.readUnsignedShort();
      byte[] address = readBytes(in, dataLength);
      String numberS = in.readUTF();  //TODO I don't think X11 actually uses Java's very specific Modified UTF-8 character encoding >>'
      Integer number = numberS.isEmpty() ? null : Integer.parseInt(numberS);
      String name = in.readUTF();  //TODO I don't think X11 actually uses Java's very specific Modified UTF-8 character encoding >>'
      dataLength = in.readUnsignedShort();
      byte[] data = readBytes(in, dataLength);
      return Optional.of(new XAuthority(family, address, number, name, data));
    } catch(IOException ex) {
      return Optional.empty();
    }
  }
  
  static boolean hostNameMatch(byte[] a, String b) {
    try {
      InetAddress authAddress = InetAddress.getByName(new String(a, StandardCharsets.UTF_8));
      InetAddress hostNameAddress = InetAddress.getByName(b);
      return authAddress.equals(hostNameAddress);
    }catch(UnknownHostException e) {
      return false;  //do nothing
    }
  }
  
  public static Optional<XAuthority> getAuthority(@Nonnull String hostName) {
    return getAuthority(Family.WILD, hostName, null);
  }

  public static Optional<XAuthority> getAuthority(@Nonnull String hostName, @Nullable Integer displayNumber) {
    return getAuthority(Family.WILD, hostName, displayNumber);
  }
  
  public static Optional<XAuthority> getAuthority(@Nonnull Family family, @Nonnull String hostName, @Nullable Integer displayNumber) {
    List<XAuthority> auths = getAuthorities();
    for (int i = 0; i < auths.size(); i++) {
      XAuthority auth = auths.get(i);
      if (isMatching(auth, family, hostName, displayNumber)) {
        return Optional.of(auth);
      }
    }
    return Optional.empty();
  }
  
  public static boolean isMatching(XAuthority auth, @Nonnull Family family, @Nonnull String hostName, @Nullable Integer displayNumber) {
    if (family != Family.WILD && auth.getFamily() != Family.WILD)
      if (family != auth.getFamily())
        return false;
    
    if (hostName.isEmpty() && auth.getAddress().length > 0)
      if (!hostNameMatch(auth.getAddress(), hostName))
        return false;
    
    if (displayNumber != null && auth.getDisplayNumber() != null)
      if (displayNumber.intValue() != auth.getDisplayNumber().intValue())
        return false;
    
    return true;
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
