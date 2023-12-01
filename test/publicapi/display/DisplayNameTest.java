package publicapi.display;

import static gnu.x11.DisplayName.*;
import gnu.x11.DisplayName;

import java.io.File;

import org.junit.Test;

public class DisplayNameTest {
  protected static void assertThrows(Class<? extends Throwable> c, Runnable r) {
    try {
      r.run();
      throw new AssertionError();
    }
    catch (Throwable t) {
      assert c.isInstance(t);
    }
  }
  
  @Test
  public void parse_null_fails() {
    assertThrows(NullPointerException.class, () -> parse(null));
  }

  @Test
  public void parse_empty_fails() {
    assertThrows(IllegalArgumentException.class, () -> parse(""));
  }

  @Test
  public void parse_negative_displayNumber_fails() {
    assertThrows(IllegalArgumentException.class, () -> parse(":-1"));
  }

  @Test
  public void parse_negative_screenNumber_fails() {
    assertThrows(IllegalArgumentException.class, () -> parse(":1.-1"));
  }

  @Test
  public void parse_hostName() {
    gnu.x11.DisplayName name = parse("hostName");
    assert name.getHostName().equals("hostName");
    assert name.getDisplayNumber() == 0;
    assert name.getScreenNumber() == 0;
    assert name.toString().equals("hostName:0.0");
    assert name.getSocketFile() == null;
  }

  @Test
  public void parse_hostName_displayNumber() {
    gnu.x11.DisplayName name = parse("hostName:2");
    assert name.getHostName().equals("hostName");
    assert name.getDisplayNumber() == 2;
    assert name.getScreenNumber() == 0;
    assert name.toString().equals("hostName:2.0");
    assert name.getSocketFile() == null;
  }

  @Test
  public void parse_hostName_displayNumber_screenNumber() {
    gnu.x11.DisplayName name = parse("hostName:2.1");
    assert name.getHostName().equals("hostName");
    assert name.getDisplayNumber() == 2;
    assert name.getScreenNumber() == 1;
    assert name.toString().equals("hostName:2.1");
    assert name.getSocketFile() == null;
  }

  @Test
  public void parse_displayNumber() {
    gnu.x11.DisplayName name = parse(":2");
    assert name.getHostName() == null;
    assert name.getDisplayNumber() == 2;
    assert name.getScreenNumber()== 0;
    assert name.toString().equals(":2.0");
    assert name.getSocketFile().equals(new File("/tmp/.X11-unix/X2"));
  }

  @Test
  public void parse_displayNumber_screenNumber() {
    DisplayName name = parse(":2.1");
    assert name.getHostName() == null;
    assert name.getDisplayNumber() == 2;
    assert name.getScreenNumber() == 1;
    assert name.toString().equals(":2.1");
    assert name.getSocketFile().equals(new File("/tmp/.X11-unix/X2"));
  }
}
