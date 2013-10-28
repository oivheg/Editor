/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Camera;

import com.jme3.input.FlyByCamera;
import com.jme3.input.InputManager;
import com.jme3.input.KeyInput;
import com.jme3.input.MouseInput;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.input.controls.MouseAxisTrigger;
import com.jme3.input.controls.MouseButtonTrigger;
import com.jme3.renderer.Camera;



/**
 *
 * @author oivhe_000
 */


public class EditorCamera extends FlyByCamera implements ActionListener{
    
    
    private static String[] actions = new String [] {

       "Editor_Left", "Editor_Right","Editor_Up","Editor_Down",
       "Editor_Back", "Editor_Forward",
       "Editor_ScrollIn","Editor_ScrollOut",
       //Rotation
       "Editor_RLeft","Editor_RRight","Editor_RUp","Editor_RDown",
       "Editor_Rotate",
       "Editor_Lower","Editor_Rise"
};
    
    public EditorCamera (Camera cam) {
        super(cam);
    }
   private boolean useArrowKeys = true;
      public EditorCamera (Camera cam, boolean useArrowKeys) {
        super(cam);
        this.useArrowKeys =useArrowKeys;
    }
    
    @Override
      public void registerWithInput(InputManager inpuntManager) {
          
        this.inputManager = inpuntManager;
      //Move camera
        inputManager.addMapping("Editor_Left", new KeyTrigger(KeyInput.KEY_A));
        inputManager.addMapping("Editor_Right", new KeyTrigger(KeyInput.KEY_D));
        inputManager.addMapping("Editor_Forward", new KeyTrigger(KeyInput.KEY_W));
        inputManager.addMapping("Editor_Back", new KeyTrigger(KeyInput.KEY_S));

     // mouse only - zoom in/out with wheel, and rotate drag
        inputManager.addMapping("Editor_ScrollIn", new MouseAxisTrigger(MouseInput.AXIS_WHEEL, false));
        inputManager.addMapping("Editor_ScrollOut", new MouseAxisTrigger(MouseInput.AXIS_WHEEL, true));
        //inputManager.addMapping("Editor_RotateDrag", new MouseButtonTrigger(MouseInput.BUTTON_LEFT));
        
     // Raise and lower camera
        inputManager.addMapping("Editor_Rise", new KeyTrigger(KeyInput.KEY_Q));
        inputManager.addMapping("Editor_Lower", new KeyTrigger(KeyInput.KEY_Z));
     // Rotate camere
        inputManager.addMapping("Editor_RLeft", new KeyTrigger(KeyInput.KEY_LEFT));
        inputManager.addMapping("Editor_RRight", new KeyTrigger(KeyInput.KEY_RIGHT));
        inputManager.addMapping("Editor_RUp", new KeyTrigger(KeyInput.KEY_UP));
        inputManager.addMapping("Editor_RDown", new KeyTrigger(KeyInput.KEY_DOWN));
        
        
        inputManager.addListener(this, actions);
        inputManager.setCursorVisible(true);
      }
    
   
   
    @Override
      public void onAnalog(String name, float value, float tpf) {
        if (name.equals("Editor_Left")) {
    moveCamera(value, true);
    } else if (name.equals("Editor_Right")) {
     moveCamera(-value, true);
    } else if (name.equals("Editor_Forward")) {
     moveCamera(value, false);
    } else if (name.equals("Editor_Back")) {
      moveCamera(-value, false);
    }else if (name.equals("Editor_ScrollIn")){
            moveCamera(value, false);
    }else if (name.equals("Editor_ScrollOut")){
            moveCamera(-value, false);
    }else if (name.equals("Editor_StrafeRight")){
            moveCamera(-value, true);
    }else if (name.equals("Editor_Rise")){
            riseCamera(value);
    }else if (name.equals("Editor_RLeft")){
            rotateCamera(value, initialUpVec);
    }else if (name.equals("Editor_RRight")){
            rotateCamera(-value, initialUpVec);
    }else if (name.equals("Editor_RUp")){
            rotateCamera(-value * (invertY ? -1 : 1), cam.getLeft());
    }else if (name.equals("Editor_RDown")){
            rotateCamera(value * (invertY ? -1 : 1), cam.getLeft());
    }
    }

}
