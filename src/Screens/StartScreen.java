/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Screens;


import com.jme3.app.Application;
import com.jme3.app.SimpleApplication;
import com.jme3.app.state.AbstractAppState;
import com.jme3.app.state.AppStateManager;
import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.screen.Screen;
import de.lessvoid.nifty.screen.ScreenController;

/**
 *
 * @author oivhe_000
 */
public class StartScreen  extends AbstractAppState implements ScreenController {

    private Nifty nifty;
  private Screen screen;
  private SimpleApplication app;
  
 
    
    /** Nifty GUI ScreenControl methods */ 
    public void bind(Nifty nifty, Screen screen) {
        this.nifty = nifty;
    this.screen = screen;
    
    
    }

  
    
    
    public void onStartScreen() {
       // throw new UnsupportedOperationException("Not supported yet.");
    }

    public void onEndScreen() {
       // throw new UnsupportedOperationException("Not supported yet.");
    }
     @Override
  public void initialize(AppStateManager stateManager, Application app) {
    super.initialize(stateManager, app);
    this.app=(SimpleApplication)app;
  }
    
     public void startGame(String nextScreen) {
    nifty.gotoScreen(nextScreen);  // switch to another screen
    
   
  }
    
}
