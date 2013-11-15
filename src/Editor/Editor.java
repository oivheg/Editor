package Editor;

import Camera.EditorCamera;
import Game.StartGame;
import com.jme3.app.SimpleApplication;
import com.jme3.bullet.control.RigidBodyControl;
import com.jme3.collision.CollisionResults;
import com.jme3.input.KeyInput;
import com.jme3.input.MouseInput;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.AnalogListener;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.input.controls.MouseAxisTrigger;
import com.jme3.input.controls.MouseButtonTrigger;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Ray;
import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;
import com.jme3.niftygui.NiftyJmeDisplay;
import com.jme3.renderer.RenderManager;
import com.jme3.scene.Geometry;
import com.jme3.terrain.geomipmap.TerrainPatch;
import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.controls.CheckBox;
import de.lessvoid.nifty.controls.TextField;
import de.lessvoid.nifty.elements.Element;
import de.lessvoid.nifty.elements.render.TextRenderer;

public class Editor extends SimpleApplication implements ActionListener {

    public static void main(String[] args) {
        Editor app = new Editor();
        app.start();
    }
    private Vector3f walkDirection = new Vector3f();
    private StartGame game;
    private Vector3f camDir = new Vector3f();
    private Vector3f camLeft = new Vector3f();
    private Vector3f pos = new Vector3f(0, 200, 300);
    private Nifty nifty;
    private EditorCamera ECam;
    private float x, y, z = 0;
    private Geometry pickedGeometry;
    private String nameofPickedItem;
    private Material mat1;

    @Override
    public void simpleInitApp() {

        startMenu();
        setCamera();

        mat1 = new Material(assetManager,
                "Common/MatDefs/Misc/Unshaded.j3md");
        mat1.setColor("Color", ColorRGBA.Red);

// initializing StartGame()
        game = new StartGame();
        game.init(assetManager, stateManager, viewPort, flyCam, inputManager, cam);
        rootNode.attachChild(game.getRoot());

        inputManager.addMapping("click", new MouseButtonTrigger(MouseInput.BUTTON_LEFT));
        inputManager.addMapping("mLeft", new MouseAxisTrigger(MouseInput.AXIS_X, false));
        inputManager.addMapping("mRight", new MouseAxisTrigger(MouseInput.AXIS_X, true));
        inputManager.addMapping("mUp", new MouseAxisTrigger(MouseInput.AXIS_Y, false));
        inputManager.addMapping("mDown", new MouseAxisTrigger(MouseInput.AXIS_Y, true));
        inputManager.addMapping("ctrl", new KeyTrigger(KeyInput.KEY_LCONTROL));

        inputManager.addListener(this, "click");
        inputManager.addListener(this, "ctrl");
        inputManager.addListener(analogListener, "mLeft");
        inputManager.addListener(analogListener, "mRight");
        inputManager.addListener(analogListener, "mUp");
        inputManager.addListener(analogListener, "mDown");

        inputManager.addMapping("view", new KeyTrigger(KeyInput.KEY_C));

        inputManager.addListener(this, "view");

        inputManager.addMapping("light", new KeyTrigger(KeyInput.KEY_L));
        inputManager.addListener(this, "light");




    }
    boolean hasPicked = false;
    ;
    
    
private AnalogListener analogListener = new AnalogListener() {
        public void onAnalog(String name, float intensity, float tpf) {



            if (pickedGeometry != null && clicked) {

                if (name.equals("mLeft")) {

                    mouseDragged("left", tpf);

                }
                if (name.equals("mRight")) {

                    mouseDragged("right", tpf);

                }
                if (name.equals("mUp")) {
                    if (ctrl) {
                        mouseDragged("up", tpf);
                    } else {
                        mouseDragged("back", tpf);
                    }


                }
                if (name.equals("mDown")) {
                    if (ctrl) {
                        mouseDragged("down", tpf);
                    } else {
                        mouseDragged("forward", tpf);
                    }


                }

            }


            hasPicked = false;

        }
    };

    private void pickItem() {

        // Reset results list.
        CollisionResults results = new CollisionResults();
        // Convert screen click to 3d position
        Vector2f click2d = inputManager.getCursorPosition();
        Vector3f click3d = cam.getWorldCoordinates(new Vector2f(click2d.x, click2d.y), 0f).clone();
        Vector3f dir = cam.getWorldCoordinates(new Vector2f(click2d.x, click2d.y), 1f).subtractLocal(click3d).normalizeLocal();

        // Aim the ray from the clicked spot forwards.
        Ray ray = new Ray(click3d, dir);
//              Vector3f newPos = ray.getDirection();
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

            if (!(target instanceof TerrainPatch)) {

                x = target.getLocalScale().x;
                y = target.getLocalScale().y;
                z = target.getLocalScale().z;
//                hasPicked = true;
                pickedGeometry = target;

                nameofPickedItem = pickedGeometry.getName();


            } else {

                nameofPickedItem = "None Selected";
                pickedGeometry = null;

                x = -1;
                y = -1;
                z = -1;
//                   hasPicked = false;
            }

            //target.setLocalScale(x, y, z);
            checkKinematic();

            if (hasKinematic) {

                  
                pickedGeometry.getControl(RigidBodyControl.class).setKinematic(clicked);
            }
        }
    }
   

    @Override
    public void simpleUpdate(float tpf) {
        if (game != null) {

            updatePos(tpf);
        }
        // this updates the label field in the hud to match what item is clicked ( will be selectet at a later point)
        if (nifty != null && "hud".equals(nifty.getCurrentScreen().getScreenId())) {
            // find old text
            Element Label = nifty.getCurrentScreen().findElementByName("name");
            TextField Fieldx = nifty.getCurrentScreen().findNiftyControl("x", TextField.class);
            TextField Fieldy = nifty.getCurrentScreen().findNiftyControl("y", TextField.class);
            TextField Fieldz = nifty.getCurrentScreen().findNiftyControl("z", TextField.class);
            Element Button = nifty.getCurrentScreen().findElementByName("Button_ID");
            CheckBox Gravity = nifty.getCurrentScreen().findNiftyControl("gravity", CheckBox.class);


            // swap old with new text
            if (Label != null) {
                Label.getRenderer(TextRenderer.class).setText(getName());



                if (clicked) {
                    Fieldx.setText(x + "");
                    Fieldy.setText(y + "");
                    Fieldz.setText(z + "");
                    
                    Gravity.setEnabled(hasKinematic);
                    if (isChecked) {

                        Gravity.setChecked(isChecked);
                    } else {
                        Gravity.setChecked(isChecked);
}



                } else {
                    
                    if (Gravity.isChecked()){
                        isChecked = true;
                    }else {
                        isChecked = false;
                    }
//                    
                    setKinematic(isChecked);
                    try {
                        x = Float.parseFloat(Fieldx.getDisplayedText());
                        y = Float.parseFloat(Fieldy.getDisplayedText());
                        z = Float.parseFloat(Fieldz.getDisplayedText());
                        if (!(pickedGeometry instanceof TerrainPatch) && pickedGeometry != null) {
                            pickedGeometry.setLocalScale(x, y, z);

                        }

                    } catch (NumberFormatException nfe) {
                    }


                }
            }
        }
    }
    boolean test;
    boolean view;

    public void updatePos(float tpf) {
        camDir.set(cam.getDirection()).multLocal(0.6f);
        camLeft.set(cam.getLeft()).multLocal(0.4f);
        walkDirection.set(0, 0, 0);

        int tmp = 0;
        if (view) {
            ECam.setEnabled(false);

            game.setVectors(cam.getLocation());
            game.getPlayer().setWalkDirection(walkDirection);
            cam.setLocation(game.getPlayer().getPhysicsLocation());
            flyCam.setDragToRotate(false);

            if (!test) {
//            inputManager.removeListener(this);
                game.setUpKeys();
                test = true;
                view = false;
            } else {
                game.updatePos(tpf);
            }
        }
    }

    public String getName() {

        return nameofPickedItem;
    }

    @Override
    public void simpleRender(RenderManager rm) {
        //TODO: add render code
    }

    private void startMenu() {
        NiftyJmeDisplay niftyDisplay = new NiftyJmeDisplay(
                assetManager, inputManager, audioRenderer, guiViewPort);
        /**
         * Create a new NiftyGUI object
         */
        nifty = niftyDisplay.getNifty();

        /**
         * Read your XML and initialize your custom ScreenController
         */
        nifty.fromXml("Interface/screen.xml", "start");
        // nifty.fromXml("Interface/helloworld.xml", "start", new MySettingsScreen(data));
        // attach the Nifty display to the gui view port as a processor
        guiViewPort.addProcessor(niftyDisplay);
        // disable the fly cam
    }

    private void setCamera() {
        flyCam.setEnabled(false);
        //flyCam.unregisterInput();
        //stateManager.detach(stateManager.getState(FlyCamAppState.class));
        cam.setFrustumFar(5000);
        ECam = new EditorCamera(cam);
        ECam.registerWithInput(inputManager);
        ECam.setMoveSpeed(100);
        ECam.setEnabled(true);
        ECam.setZoomSpeed(100);
        //sets camera start position
        cam.setLocation(pos);
        cam.setLocation(new Vector3f(0, 10, 100));
        cam.lookAt(Vector3f.ZERO, Vector3f.UNIT_Y);
        cam.update();
    }
    boolean clicked = false, ctrl = false;

    public void onAction(String binding, boolean isPressed, float tpf) {
        if (binding.equals("click")) {
            if (isPressed) {
                clicked = true;

                pickItem();



            } else {

                clicked = false;
                if (hasKinematic) { pickedGeometry.getControl(RigidBodyControl.class).setKinematic(clicked);
                }
            }
        }

        if (binding.equals("ctrl")) {
            if (isPressed) {
                ctrl = true;
            } else {
                ctrl = false;
            }
        }
        if (binding.equals("view")) {
            view = true;
        }
        if (binding.equals("light")) {
            game.sunDir();
//            changeDay();
        }
    }

    public void changeDay() {
        game.changeDay();
    }

    public void mouseDragged(String direction, float tpf) {
        float posx = pickedGeometry.getLocalTranslation().x;
        float posy = pickedGeometry.getLocalTranslation().y;
        float posz = pickedGeometry.getLocalTranslation().z;
        if (direction.equals("left")) {
            posx += 1.5;
        } else if (direction.equals("right")) {
            posx -= 1.5;
        } else if (direction.equals("back")) {
            posz -= 2;
        } else if (direction.equals("forward")) {
            posz += 2;
        } else if (direction.equals("up")) {
            posy += 2;
        } else if (direction.equals("down")) {
            posy -= 2;
        }
        pickedGeometry.setLocalTranslation(posx, posy, posz);


    }
    boolean hasKinematic = false;
    boolean isChecked = false;

    private void checkKinematic() {
        if (pickedGeometry != null && pickedGeometry.getControl(RigidBodyControl.class) != null) {
            hasKinematic = true;
            isChecked = !pickedGeometry.getControl(RigidBodyControl.class).isKinematic();
        } else {
            hasKinematic = false;

        }







    }

    private void setKinematic(boolean checked) {
        if (pickedGeometry != null && pickedGeometry.getControl(RigidBodyControl.class) != null) {
            pickedGeometry.getControl(RigidBodyControl.class).setKinematic(!checked);
        }
    }
}
