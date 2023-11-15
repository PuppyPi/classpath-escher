package gnu.x11.event;

import gnu.x11.Atom;
import gnu.x11.Display;
import gnu.x11.RequestOutputStream;
import gnu.x11.ResponseInputStream;
import gnu.x11.Window;


/** X client message event. */
public final class ClientMessage extends Event {
  public static final int CODE = 33;


  private int windowID;
  private int typeAtomID;
  private byte[] data;

  /** Reading. */
  public ClientMessage (Display display, ResponseInputStream in) {
    super(display, in); 
    windowID = in.readInt32();
    typeAtomID = in.readInt32();
    data = new byte[20];
    in.readData(data);
  }


  /** Writing. */
  public ClientMessage (Display display, int numberOfBitsPerLogicalElement,
                        int window_id, int type_atom_id, byte[] data) {
    super (display, EventCode.CLIENT_MESSAGE);

    if (data.length != 20)
      throw new IllegalArgumentException (
                                          "Data was "
                                              + data.length
                                              + " bytes instead of the required 20 bytes!");

    this.detail = numberOfBitsPerLogicalElement;

    this.windowID = window_id;
    this.typeAtomID = type_atom_id;
    this.data = data;
  }

  public ClientMessage (Window window, Atom type,
                        int numberOfBitsPerLogicalElement, byte[] data) {
    this (window.getDisplay (), numberOfBitsPerLogicalElement, window.getID (), type.getID (),
          data);
  }


  //-- reading

  public int format () {
    return detail;
  }

  public int type_id () {
    return typeAtomID;
  }

  public int wm_data () {
      return  (((data [0]) & 0xff) << 24 |
               ((data [1]) & 0xff) << 16 |
               ((data [2]) & 0xff) << 8  |
               ((data [3]) & 0xff));
  }

  public int wm_time () {
    return (((data [4]) & 0xff) << 24 |
            ((data [5]) & 0xff) << 16 |
            ((data [6]) & 0xff) << 8  |
            ((data [7]) & 0xff));
  }


  public boolean deleteWindow () {
    Atom wm_protocols = Atom.intern (display, "WM_PROTOCOLS");
    Atom wm_delete_window = Atom.intern (display,
      "WM_DELETE_WINDOW");

    return format () == 32
      && type () == wm_protocols
      && wm_data () == wm_delete_window.getID();
  }


  public Atom type () { 
    return (Atom) Atom.intern (display, type_id (), true); 
  }
  
  
  public int getWindowID() {
    return windowID;
  }



  // -- writing
  @Override public void write (RequestOutputStream o) {
    super.write (o);
    o.writeInt32 (windowID);
    o.writeInt32 (typeAtomID);
    o.write (data);
  }
}
