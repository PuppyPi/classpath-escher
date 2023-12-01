package gnu.x11;

import java.io.File;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.net.InetAddress;
import java.net.Socket;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * X display name. Encapsulates display name conventions in unix for creating a Display. If the connection is a unix
 * socket the file is "/tmp/.X11-unix/X" + displayNumber. If the connection is tcp the port is 6000 + displayNumber.
 */
public class DisplayName {
  private final String hostName;
  private final int displayNumber;
  private final int screenNumber;
  private final File socketFile;

  private DisplayName(String hostName, int displayNumber, int screenNumber, File socketFile) {
    if(hostName != null) {
      if (hostName.trim().isEmpty())
        throw new IllegalArgumentException("Blank or empty hostname given!");
      this.hostName = hostName;
    } else {
      this.hostName = null;
    }
    if(displayNumber < 0) {
      throw new IllegalArgumentException("expected displayNumber > 0 but was \"" + displayNumber + "\".");
    }
    this.displayNumber = displayNumber;
    if(screenNumber < 0) {
      throw new IllegalArgumentException("expected screenNumber > 0 but was \"" + screenNumber + "\".");
    }
    this.screenNumber = screenNumber;
    this.socketFile = socketFile;
  }
  
  public String getHostName () {
    return hostName;
  }

  public int getDisplayNumber () {
    return displayNumber;
  }

  public int getScreenNumber () {
    return screenNumber;
  }

  public File getSocketFile () {
    return socketFile;
  }


  /**
   * Parses a DisplayName from the DISPLAY environment variable.
   * @return resulting DisplayName
   */
  public static DisplayName parse() {
    return parse(System.getenv("DISPLAY"));
  }

  /**
   * Parses a DisplayName from convention. The convention for a display name is
   * <code>hostName:displayNumber.screenNumber</code>. hostName and screenNumber are optional.
   * @param convention of <code>hostName:displayNumber.screenNumber</code>
   * @return resulting DisplayName
   */
  public static DisplayName parse(@Nonnull String convention) {
    if (convention.trim().isEmpty())
      throw new IllegalArgumentException("Provided display name was blank or empty");

    String hostName = null;

    int i = convention.indexOf(':');
    // case 1: convention = hostName
    if (i == -1) {
      return new DisplayName(convention, 0, 0, null);
    } else {
      hostName = i == 0 ? null : convention.substring(0, i);
    }

    int displayNumber;
    int screenNumber = 0;
    int j = convention.indexOf('.', i);

    if (j == -1) {
      // case 2: convention = hostName:displayNumber
      displayNumber = Integer.parseInt(convention.substring(i + 1));
    } else {
      // case 3: convention = hostName:displayNumber.screenNumber
      displayNumber = Integer.parseInt(convention.substring(i + 1, j));
      screenNumber = Integer.parseInt(convention.substring(j + 1));
    }

    if(hostName == null || hostName.equals("localhost")) {
      return new DisplayName(hostName, displayNumber, screenNumber, new File("/tmp/.X11-unix/X" + displayNumber));
    }

    return new DisplayName(hostName, displayNumber, screenNumber, null);
  }

  /**
   * Connects to socket and creates new Display. If hostName is null or "localhost" hostName is resolved before
   * connecting. If socketPath is set a unix socket is used otherwise a tcp socket is used.
   * @return
   */
  public Display connect(@Nullable UnixSocketOpener unixDomainSocketOpener) {
    X11Socketlike socket;

    try {
      if (socketFile != null) {
        if (unixDomainSocketOpener == null)
          throw new IllegalArgumentException("We must have the Unix Domain Socket opener for this Display Name!");
        socket = unixDomainSocketOpener.open(socketFile);
      } else {
        InetAddress address;
        if(hostName == null) {
          address = InetAddress.getLocalHost();
        } else {
          address = InetAddress.getByName(hostName);
        }
        socket = X11Socketlike.fromJRESocket(new Socket(address, 6000 + displayNumber));
      }
    } catch(IOException e) {
      throw new UncheckedIOException(String.format("Failed to create connection to \"%s\".", this), e);
    }

    return new Display(socket, hostName, displayNumber, screenNumber);
  }

  /**
   * Returns the string representation of this DisplayName.
   * @return
   */
  public String toString() {
    String h = hostName == null ? "" : hostName;
    return h + ":" + displayNumber + "." + screenNumber;
  }
}
