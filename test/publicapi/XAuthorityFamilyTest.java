package publicapi;

import gnu.x11.XAuthority.Family;

import org.junit.Test;

public class XAuthorityFamilyTest {
  @Test
  public void internetCode() {
    assert Family.INTERNET.getCode() == 0;
  }
  @Test
  public void localCode() {
    assert Family.LOCAL.getCode() == 256;
  }
  @Test
  public void wildCode() {
    assert Family.WILD.getCode() == 65535;
  }
  @Test
  public void krb5principalCode() {
    assert Family.KRB5PRINCIPAL.getCode() == 254;
  }
  @Test
  public void localhostCode() {
    assert Family.LOCALHOST.getCode() == 252;
  }

  @Test
  public void internet_getByCode() {
    assert Family.getByCode(0) == Family.INTERNET;
  }

  @Test
  public void local_getByCode() {
    assert Family.getByCode(256) == Family.LOCAL;
  }

  @Test
  public void wild_getByCode() {
    assert Family.getByCode(65535) == Family.WILD;
  }

  @Test
  public void krb_getByCode() {
    assert Family.getByCode(254) == Family.KRB5PRINCIPAL;
  }

  @Test
  public void localhost_getByCode() {
    assert Family.getByCode(252) == Family.LOCALHOST;
  }

  @Test
  public void fail_getByCode() {
    try {
      Family.getByCode(-1234);
      throw new AssertionError ();
    }
    catch (IllegalArgumentException exc) {
    }
  }
}
