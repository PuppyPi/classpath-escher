package gnu.x11;

import gnu.x11.XAuthority.Family;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;

public class XAuthorityFile {
  @Test
  void readFile() throws IOException {
    List<XAuthority> authorities = XAuthority.getAuthorities(new File(getClass().getClassLoader().getResource(".Xauthority").getFile()));
    assert authorities.size() == 2;
    XAuthority first = authorities.get(0);
    assert first.getFamily() == Family.LOCAL;
    assert Arrays.equals(first.getAddress(), new byte[]{(byte)'n', (byte)'1'});
    assert first.getDisplayNumber().intValue() == 0;
    assert first.getProtocolName().equals("MIT-MAGIC-COOKIE-1");
    assert Arrays.equals(first.getProtocolData(), new byte[]{(byte)0xe5, (byte)0x87, (byte)0x17, (byte)0xc9, (byte)0xa5, (byte)0xa6, (byte)0xcb, (byte)0x90, (byte)0x89, (byte)0x54, (byte)0xe3, (byte)0x85, (byte)0x40, (byte)0xf3, (byte)0xea, (byte)0xbf});

    XAuthority second = authorities.get(1);
    assert second.getFamily() == Family.INTERNET;
    assert Arrays.equals(second.getAddress(), new byte[]{127, 0, 1, 1});
    assert second.getDisplayNumber().intValue() == 2;
    assert second.getProtocolName().equals("MIT-MAGIC-COOKIE-1");
    assert Arrays.equals(second.getProtocolData(), new byte[]{(byte)0x75, (byte)0x80, (byte)0xc7, (byte)0x34, (byte)0xc3, (byte)0x7f, (byte)0x7c, (byte)0x7e, (byte)0x0d, (byte)0x20, (byte)0x6b, (byte)0x90, (byte)0x00, (byte)0x8a, (byte)0xd4, (byte)0x7f});
  }
}
