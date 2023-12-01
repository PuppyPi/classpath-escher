package gnu.x11;

import java.io.File;
import java.net.Socket;

import jnr.unixsocket.UnixSocket;
import jnr.unixsocket.UnixSocketAddress;
import jnr.unixsocket.UnixSocketChannel;

public enum UnixDomainSocketSupportingDisplayOpenerByJNRUnixSockets implements UnixSocketOpener {
  I;

  @Override public X11Socketlike open (File path) {
    return X11Socketlike.fromJRESocket(new UnixSocket(UnixSocketChannel.open(new UnixSocketAddress(path))));
  }
}
