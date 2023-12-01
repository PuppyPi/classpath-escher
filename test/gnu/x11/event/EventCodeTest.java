package gnu.x11.event;

import org.junit.Test;

public class EventCodeTest {
  @Test
  public void getEventByID() {
    for(EventCode code : EventCode.values()) {
      assert EventCode.getEventByID(code.getCode()) == code;
    }
  }
}
