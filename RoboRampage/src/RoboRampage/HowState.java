package RoboRampage;

import jig.ResourceManager;
import org.newdawn.slick.*;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;

/***
 * This state is instructions and how to play section of the menu. Controls are similar to StartUp State, using wasd or
 * the arrow keys to navigate and space bar to select. The player can only return to the StartupState from here.
 *
 * Transitions From StartState
 *
 * Transitions To StartState
 */
public class HowState extends BasicGameState {
  private int timer, select;
  boolean arrowBlink, selected;

  @Override
  public int getID() {
    return RoboGame.HOWTOPLAYSTATE;
  }

  @Override
  public void init(GameContainer gameContainer, StateBasedGame stateBasedGame) throws SlickException {

  }

  @Override
  public void enter(GameContainer container, StateBasedGame game) {
    RoboGame rg = (RoboGame)game;
    container.setSoundOn(true);
    select = 0;
    selected = false;
    arrowBlink = true;
  }

  @Override
  public void render(GameContainer gameContainer, StateBasedGame stateBasedGame, Graphics g) throws SlickException {
    g.drawImage(ResourceManager.getImage(RoboGame.MENU_HOWTOPLAYSPLASH_RSC), 0, 0);
    g.drawImage(ResourceManager.getImage(RoboGame.MENU_BACK_RSC), 30, 750);
    if(arrowBlink) {
      switch(select) {
        default:
          g.drawImage(ResourceManager.getImage(RoboGame.MENU_ARROW_RSC), 224, 760);
          break;
      }
    }
  }

  @Override
  public void update(GameContainer container, StateBasedGame game, int i) throws SlickException {
    Input input = container.getInput();
    RoboGame rg = (RoboGame)game;
    // Checking if a selection has been made
    if(selected) {
      if(timer % 15 == 0) {
        arrowBlink = !arrowBlink;
      }
      // Once wait period is over
      if(timer <= 0) {
        switch(select) {
          default:
            ((StartState)game.getState(RoboGame.STARTUPSTATE)).restartMusic(false);
            rg.enterState(RoboGame.STARTUPSTATE);
            break;
        }
      }
      timer --;
    }

    // If Space is pressed to select
    else if(input.isKeyPressed(Input.KEY_SPACE)) {
      timer = 75;
      selected = true;
      ResourceManager.getSound(RoboGame.SOUND_MENUSELECT_RSC).play();
    }
    // If moving down
    else if(input.isKeyPressed(Input.KEY_S) || input.isKeyPressed(Input.KEY_DOWN)) {
      select ++;
      ResourceManager.getSound(RoboGame.SOUND_MENUACTIVATE_RSC).play();
    }
    // If moving up
    else if(input.isKeyPressed(Input.KEY_W) || input.isKeyPressed(Input.KEY_UP)) {
      select --;
      ResourceManager.getSound(RoboGame.SOUND_MENUACTIVATE_RSC).play();
    }
    // Mod select by 3 to snap back to top, set negatives to 2 to snap to the bottom when going up at the top.
    select = select % 1;
    if(select < 0) select = 0;
  }
}
