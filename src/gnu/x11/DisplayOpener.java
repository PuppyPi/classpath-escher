package gnu.x11;

import java.io.IOException;

public interface DisplayOpener {
  public Display open (String hostname, int displayNumber, int screenNumber) throws IOException;
}
