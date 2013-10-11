/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Screens;

import Editor.Main;
import com.jme3.app.state.AbstractAppState;
import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.screen.Screen;
import de.lessvoid.nifty.screen.ScreenController;

/**
 *
 * @author oivhe_000
 */
public class Hud extends AbstractAppState implements ScreenController {
    
    private Nifty nifty;
  private Screen screen;

    
 
    public void bind(Nifty nifty, Screen screen) {
       this.nifty = nifty;
    this.screen = screen;
    
    }

    public void onStartScreen() {
//        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void onEndScreen() {
//        throw new UnsupportedOperationException("Not supported yet.");
    }
    
}
