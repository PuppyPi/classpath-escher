package gnu.x11;

import gnu.x11.XAuthority.Family;
import java.io.File;
import java.net.UnknownHostException;
import java.nio.charset.StandardCharsets;
import java.util.List;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class XAuthorityFile {
  @Test
  void readFile() throws UnknownHostException {
    List<XAuthority> authorities = XAuthority.getAuthorities(new File(getClass().getClassLoader().getResource(".Xauthority").getFile()));
    assertThat(authorities).hasSize(2);
    XAuthority first = authorities.get(0);
    assertThat(first.getFamily()).isEqualTo(Family.LOCAL);
    assertThat(new String(first.getAddress(), StandardCharsets.UTF_8)).isEqualTo("n1");
    assertThat(first.getDisplayNumber()).isEqualTo(0);
    assertThat(first.getProtocolName()).isEqualTo("MIT-MAGIC-COOKIE-1");
    assertThat(first.getProtocolData()).isEqualTo(new byte[]{(byte)0xe5, (byte)0x87, (byte)0x17, (byte)0xc9, (byte)0xa5, (byte)0xa6, (byte)0xcb, (byte)0x90, (byte)0x89, (byte)0x54, (byte)0xe3, (byte)0x85, (byte)0x40, (byte)0xf3, (byte)0xea, (byte)0xbf});

    XAuthority second = authorities.get(1);
    assertThat(second.getFamily()).isEqualTo(Family.INTERNET);
    assertThat(second.getAddress()).isEqualTo(new byte[]{127, 0, 1, 1});
    assertThat(second.getDisplayNumber()).isEqualTo(2);
    assertThat(second.getProtocolName()).isEqualTo("MIT-MAGIC-COOKIE-1");
    assertThat(second.getProtocolData()).isEqualTo(new byte[]{(byte)0x75, (byte)0x80, (byte)0xc7, (byte)0x34, (byte)0xc3, (byte)0x7f, (byte)0x7c, (byte)0x7e, (byte)0x0d, (byte)0x20, (byte)0x6b, (byte)0x90, (byte)0x00, (byte)0x8a, (byte)0xd4, (byte)0x7f});
  }
}
