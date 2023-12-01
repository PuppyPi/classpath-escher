package gnu.x11;

import java.io.File;

public interface UnixSocketOpener {
  public X11Socketlike open(File path);
}
