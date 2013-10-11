package Editor;

import Game.StartGame;
import com.jme3.app.SimpleApplication;
import com.jme3.math.Vector3f;
import com.jme3.niftygui.NiftyJmeDisplay;
import com.jme3.renderer.RenderManager;
import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.elements.Element;
import de.lessvoid.nifty.elements.render.TextRenderer;

/**
 * test
 * @author normenhansen
 */
public class Main extends SimpleApplication {

    public static void main(String[] args) {
        Main app = new Main();
        app.start();
    }

  private Vector3f walkDirection = new Vector3f();
  private StartGame game;
  private boolean left = false, right = false, up = false, down = false;
 
  //Temporary vectors used on each frame.
  //They here to avoid instanciating new vectors on each frame
  private Vector3f camDir = new Vector3f();
  private Vector3f camLeft = new Vector3f();
  private Vector3f pos = new Vector3f(0,200,300);
  private Nifty nifty;
    @Override
    public void simpleInitApp() {
        
        
//        
//        Box b = new Box(Vector3f.ZERO, 1, 1, 1);
//        Geometry geom = new Geometry("Box", b);
//
//        Material mat = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
//        mat.setColor("Color", ColorRGBA.Blue);
//        geom.setMaterial(mat);
//
//        rootNode.attachChild(geom);
        
        NiftyJmeDisplay niftyDisplay = new NiftyJmeDisplay(
    assetManager, inputManager, audioRenderer, guiViewPort);
/** Create a new NiftyGUI object */
 nifty = niftyDisplay.getNifty();

/** Read your XML and initialize your custom ScreenController */
nifty.fromXml("Interface/screen.xml", "start");
// nifty.fromXml("Interface/helloworld.xml", "start", new MySettingsScreen(data));
// attach the Nifty display to the gui view port as a processor
guiViewPort.addProcessor(niftyDisplay);
// disable the fly cam
flyCam.setDragToRotate(true);


//sets camera start position
cam.setLocation(pos);
 Vector3f left = new Vector3f(-0.9996263f,1.0058284E-7f,-0.027339306f);
 Vector3f up = new Vector3f(0.016700502f, 0.87991315f, -0.47484097f);
cam.setAxes(left, up, walkDirection);


// initializing StartGame()
 game = new StartGame();
 game.init(assetManager, stateManager, viewPort, flyCam, inputManager, cam);
     
rootNode.attachChild(game.getRoot());




       
    }

    @Override
    public void simpleUpdate(float tpf) {
        if (game != null) {
             game.updatePos(tpf);
        }
        
        // this updates the label field in the hud to mach what item is clicked ( will be selectet at a later point)
        if (nifty != null && "hud".equals(nifty.getCurrentScreen().getScreenId()) ) {
      // find old text
Element niftyElement = nifty.getCurrentScreen().findElementByName("name");

// swap old with new text
if (niftyElement != null) {
niftyElement.getRenderer(TextRenderer.class).setText(game.GetName());

}        
}  
    }

    @Override
    public void simpleRender(RenderManager rm) {
        //TODO: add render code
    }

   

    
}
