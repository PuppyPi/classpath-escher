package gnu.x11;

import java.net.Socket;
import jnr.unixsocket.UnixSocket;
import jnr.unixsocket.UnixSocketAddress;
import jnr.unixsocket.UnixSocketChannel;

public enum UnixDomainSocketSupportingDisplayOpenerByJNRUnixSockets implements DisplayOpener {
  I;

  /**
   * @param screenNumber
   *          -1 if not specified, {@link #defaultScreenNumber} will be 0
   * @throws EscherServerConnectionException
   * @see <a href="XOpenDisplay.html">XOpenDisplay</a>
   */
  @Override public Display open (String hostname, int displayNumber,
                                 int screenNumber) {
    System.err.println ("Deisplay::<init>: " + hostname + ":" + displayNumber
                        + "." + screenNumber);
    
    Socket socket;
    
    if (hostname.startsWith ("/")) {
      String displayNmae = hostname + ":" + displayNumber
                           + (screenNumber != -1 ? "." + screenNumber : "");
      UnixSocketAddress address = new UnixSocketAddress (displayNmae);
      UnixSocketChannel channel = UnixSocketChannel.open (address);
      socket = new UnixSocket (channel);
    }
    else {
      socket = new Socket (hostname,
                                                        6000 + displayNumber);
    }
    
    return new Display (X11Socketlike.fromJRESocket (socket), hostname, displayNumber, screenNumber);
  }
}
