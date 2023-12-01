package publicapi;

import gnu.x11.XAuthority.Family;

import org.junit.Test;

public class XAuthorityFamilyTest {
  @Test
  void internetCode() {
    assert Family.INTERNET.getCode() == 0;
  }
  @Test
  void localCode() {
    assert Family.LOCAL.getCode() == 256;
  }
  @Test
  void wildCode() {
    assert Family.WILD.getCode() == 65535;
  }
  @Test
  void krb5principalCode() {
    assert Family.KRB5PRINCIPAL.getCode() == 254;
  }
  @Test
  void localhostCode() {
    assert Family.LOCALHOST.getCode() == 252;
  }

  @Test
  void internet_getByCode() {
    assert Family.getByCode(0) == Family.INTERNET;
  }

  @Test
  void local_getByCode() {
    assert Family.getByCode(256) == Family.LOCAL;
  }

  @Test
  void wild_getByCode() {
    assert Family.getByCode(65535) == Family.WILD;
  }

  @Test
  void krb_getByCode() {
    assert Family.getByCode(254) == Family.KRB5PRINCIPAL;
  }

  @Test
  void localhost_getByCode() {
    assert Family.getByCode(252) == Family.LOCALHOST;
  }

  @Test
  void fail_getByCode() {
    boolean pass = false;
    try {
      Family.getByCode(-1234);
    }
    catch (IllegalArgumentException exc) {
      pass = true;
    }
    assert pass;
  }
}
