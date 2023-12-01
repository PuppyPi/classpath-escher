package gnu.x11;

import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UncheckedIOException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

public interface X11Socketlike extends Closeable {
  public InputStream getInputStream () throws IOException;

  public OutputStream getOutputStream () throws IOException;

  public static X11Socketlike fromJRESocket (Socket socket) {
    return new X11Socketlike () {
      @Override public void close () throws IOException {
        socket.close ();
      }

      @Override public InputStream getInputStream () throws IOException {
        return socket.getInputStream ();
      }

      @Override public OutputStream getOutputStream () throws IOException {
        return socket.getOutputStream ();
      }
    };
  }
  
  public static String translateIfLocalhost (String hostName) {
    if(hostName.equals("localhost")) {
        try {
            return InetAddress.getLocalHost().getHostName();
        } catch (UnknownHostException e) {
            throw new UncheckedIOException(e);
        }
    }
    return hostName;
  }
}
