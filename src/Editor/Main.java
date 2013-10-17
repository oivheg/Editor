package Editor;

import Game.StartGame;
import com.jme3.app.SimpleApplication;
import com.jme3.collision.CollisionResults;
import com.jme3.input.KeyInput;
import com.jme3.input.MouseInput;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.AnalogListener;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.input.controls.MouseButtonTrigger;
import com.jme3.math.Ray;
import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;
import com.jme3.niftygui.NiftyJmeDisplay;
import com.jme3.renderer.RenderManager;
import com.jme3.scene.Geometry;
import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.elements.Element;
import de.lessvoid.nifty.elements.render.TextRenderer;

/**
 * test
 * @author normenhansen
 */
public class Main extends SimpleApplication implements ActionListener {

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
   private String clickedname = "testing";
    private int counter;
    private boolean view;
    @Override
    public void simpleInitApp() {
        startMenu();
        setCamera();


// initializing StartGame()
 game = new StartGame();
 game.init(assetManager, stateManager, viewPort, flyCam, inputManager, cam);
 rootNode.attachChild(game.getRoot());

 inputManager.addMapping("click", new MouseButtonTrigger(MouseInput.BUTTON_LEFT));
 inputManager.addListener(analogListener, "click");
 inputManager.addMapping("view", new KeyTrigger(KeyInput.KEY_Z));
 inputManager.addListener(this, "view");

 
    }
private AnalogListener analogListener = new AnalogListener() {
    public void onAnalog(String name, float intensity, float tpf) {
      if (name.equals("click")) {
        // Reset results list.
        CollisionResults results = new CollisionResults();
        // Convert screen click to 3d position
        Vector2f click2d = inputManager.getCursorPosition();
        Vector3f click3d = cam.getWorldCoordinates(new Vector2f(click2d.x, click2d.y), 0f).clone();
        Vector3f dir = cam.getWorldCoordinates(new Vector2f(click2d.x, click2d.y), 1f).subtractLocal(click3d).normalizeLocal();
        
        // Aim the ray from the clicked spot forwards.
        Ray ray = new Ray(click3d, dir);
        Vector3f newPos = ray.getDirection();
        // Collect intersections between ray and all nodes in results list.
        rootNode.collideWith(ray, results);
        // (Print the results so we see what is going on:)
        for (int i = 0; i < results.size(); i++) {
          // (For each “hit”, we know distance, impact point, geometry.)
          float dist = results.getCollision(i).getDistance();
          Vector3f pt = results.getCollision(i).getContactPoint();
          String target = results.getCollision(i).getGeometry().getName();
          
          System.out.println("Selection #" + i + ": " + target + " at " + pt + ", " + dist + " WU away.");
        }
        // Use the results -- we rotate the selected geometry.
        if (results.size() > 0) {
          // The closest result is the target that the player picked:
          Geometry target = results.getClosestCollision().getGeometry();
          // Here comes the action:
          if (target.getName().equals("Box")) {
            clickedname = target.getName();
          } else if (target.getName().equals("qube")) {
              clickedname = target.getName();
           }
        }
      } // else if ...
    }
  };

    @Override
    public void simpleUpdate(float tpf) {
        if (game != null) {
          
             updatePos(tpf);
        
        }
        
        
       

        
        // this updates the label field in the hud to mach what item is clicked ( will be selectet at a later point)
        if (nifty != null && "hud".equals(nifty.getCurrentScreen().getScreenId()) ) {
      // find old text
Element niftyElement = nifty.getCurrentScreen().findElementByName("name");

// swap old with new text
if (niftyElement != null) {
niftyElement.getRenderer(TextRenderer.class).setText(GetName());

}        
} 
    }  
    
    
     public void updatePos(float tpf) {
        camDir.set(cam.getDirection()).multLocal(0.6f);
        camLeft.set(cam.getLeft()).multLocal(0.4f);
        walkDirection.set(0, 0, 0);
//        if (left) {
//            walkDirection.addLocal(camLeft);
//        }
//        if (right) {
//            walkDirection.addLocal(camLeft.negate());
//        }
//        if (up) {
//            walkDirection.addLocal(camDir);
//        }
//        if (down) {
//            walkDirection.addLocal(camDir.negate());
//        }
        if (view){
        game.getPlayer().setWalkDirection(walkDirection);
        cam.setLocation(game.getPlayer().getPhysicsLocation());
        flyCam.setDragToRotate(false);
        if (!test) {
//            inputManager.removeListener(this);
        game.setUpKeys();
        test = true;
        view = false;
        }else {
            game.updatePos(tpf);
        }
        }
        }
    boolean test;
     public String GetName() {
        return clickedname;
     }
        
        
    @Override
    public void simpleRender(RenderManager rm) {
        //TODO: add render code
    }

    private void startMenu() {
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
    }

    private void setCamera() {
        //sets camera start position
        cam.setLocation(pos);
         Vector3f left = new Vector3f(-0.9996263f,1.0058284E-7f,-0.027339306f);
         Vector3f up = new Vector3f(0.016700502f, 0.87991315f, -0.47484097f);
        cam.setAxes(left, up, walkDirection);
    }

    public void onAction(String binding, boolean isPressed, float tpf) {
        if (binding.equals("view")) {
//     
       
        view = true;   
      
    
    }

    }

    
}
