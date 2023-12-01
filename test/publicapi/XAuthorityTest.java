package publicapi;

import gnu.x11.XAuthority;
import gnu.x11.XAuthority.Family;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.Optional;

import org.junit.Test;

public class XAuthorityTest {

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
  public void constructor_fails_on_null_family() {
    assertThrows(NullPointerException.class, () -> new XAuthority(null, "hostName".getBytes(), 0, "magic", new byte[1]));
  }

  @Test
  public void constructor_fails_on_null_address() {
    assertThrows(NullPointerException.class, () -> new XAuthority(Family.LOCAL, null, 0, "magic", new byte[1]));
  }

  @Test
  public void constructor_fails_on_negative_displayNumber() {
    assertThrows(IllegalArgumentException.class, () -> new XAuthority(Family.LOCAL, "host".getBytes(), -1, "magic", new byte[1]));
  }

  @Test
  public void constructor_fails_on_null_protocolName() {
    assertThrows(NullPointerException.class, () -> new XAuthority(Family.LOCAL, "host".getBytes(), 0, null, new byte[1]));
  }

  @Test
  public void constructor_fails_on_blank_protocolName() {
    assertThrows(IllegalArgumentException.class, () -> new XAuthority(Family.LOCAL, "host".getBytes(), 0, " ", new byte[1]));
  }

  @Test
  public void constructor_fails_on_null_protocolData() {
    assertThrows(NullPointerException.class, () -> new XAuthority(Family.LOCAL, "host".getBytes(), 0, "magic", null));
  }

  @Test
  public void constructor() {
    XAuthority xAuthority = new XAuthority(Family.LOCAL, "host".getBytes(), 0, "magic", new byte[]{1, 2, 3});
    assert xAuthority.getFamily() == Family.LOCAL;
    assert Arrays.equals(xAuthority.getAddress(), "host".getBytes());
    assert xAuthority.getDisplayNumber().intValue() == 0;
    assert xAuthority.getProtocolName().equals("magic");
    assert Arrays.equals(xAuthority.getProtocolData(), new byte[]{1, 2, 3});
  }

  @Test
  public void read_empty_on_exception() throws IOException {
    Optional<XAuthority> read = XAuthority.read(new DataInputStream(new ByteArrayInputStream(new byte[]{})));
    assert !read.isPresent();
  }

  @Test
  public void read() throws IOException {
    Optional<XAuthority> read = XAuthority.read(new DataInputStream(new ByteArrayInputStream(new byte[]{(byte)0x01, (byte)0x00, (byte)0x00, (byte)0x04, (byte)0x68, (byte)0x6F, (byte)0x73, (byte)0x74, (byte)0x00, (byte)0x01, (byte)0x33, (byte)0x00, (byte)0x05, (byte)0x6D, (byte)0x61, (byte)0x67, (byte)0x69, (byte)0x63, (byte)0x00, (byte)0x03, (byte)0x01, (byte)0x02, (byte)0x03})));
    assert read.isPresent();
    XAuthority xAuthority = read.get();
    assert xAuthority.getFamily() == Family.LOCAL;
    assert Arrays.equals(xAuthority.getAddress(), "host".getBytes());
    assert xAuthority.getDisplayNumber().intValue() == 3;
    assert xAuthority.getProtocolName().equals("magic");
    assert Arrays.equals(xAuthority.getProtocolData(), new byte[]{1, 2, 3});
  }
}
