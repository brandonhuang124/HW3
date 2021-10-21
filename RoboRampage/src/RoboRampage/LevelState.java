package RoboRampage;

import jig.ResourceManager;
import org.newdawn.slick.*;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;

public class LevelState extends BasicGameState {
  private int select, timer;
  private boolean selected, arrowBlink;

  @Override
  public int getID() {
    return RoboGame.LEVELSELECTSTATE;
  }

  @Override
  public void init(GameContainer container, StateBasedGame stateBasedGame) throws SlickException {

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
  public void render(GameContainer container, StateBasedGame stateBasedGame, Graphics g) throws SlickException {
    g.drawImage(ResourceManager.getImage(RoboGame.MENU_LEVELSELECTSPLASH_RSC), 0, 0);
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

    if(selected) {
      if(timer % 15 == 0) {
        arrowBlink = !arrowBlink;
      }
      // Once wait period is over
      if(timer <= 0) {
        switch(select) {
          default:
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
    }
    // If moving down
    else if(input.isKeyPressed(Input.KEY_D) || input.isKeyPressed(Input.KEY_DOWN)) {
      select ++;
    }
    // If moving up
    else if(input.isKeyPressed(Input.KEY_W) || input.isKeyPressed(Input.KEY_UP)) {
      select --;
    }
    // Mod select by 3 to snap back to top, set negatives to 2 to snap to the bottom when going up at the top.
    select = select % 1;
    if(select < 0) select = 0;

  }
}
