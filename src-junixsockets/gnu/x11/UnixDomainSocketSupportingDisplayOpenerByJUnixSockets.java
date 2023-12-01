package gnu.x11;

import java.io.File;
import java.net.Socket;

import org.newsclub.net.unix.*;

public enum UnixDomainSocketSupportingDisplayOpenerByJUnixSockets implements UnixSocketOpener {
  I;

  @Override public X11Socketlike open (File path) {
    return X11Socketlike.fromJRESocket(AFUNIXSocket.connectTo(new AFUNIXSocketAddress(path)));
  }
}
