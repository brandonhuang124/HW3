package RoboRampage;

import jig.ResourceManager;
import org.newdawn.slick.*;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;

/***
 * This state is the level selection screen for skipping to certain levels of the game. Controls are similar to startup
 * state, wasd or arrow keys to navigate and space bar to make a selection. Depending on which button the player selects
 * they can return to the main menu or enter the test state at a specific level.
 *
 * Transitions From StartState
 *
 * Transitions To StartState or TestState
 */
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
    g.drawImage(ResourceManager.getImage(RoboGame.MENU_LEVEL1_RSC), 246, 250);
    g.drawImage(ResourceManager.getImage(RoboGame.MENU_LEVEL2_RSC), 246, 320);
    g.drawImage(ResourceManager.getImage(RoboGame.MENU_LEVEL3_RSC), 246, 390);
    g.drawImage(ResourceManager.getImage(RoboGame.MENU_LEVEL4_RSC), 246, 460);
    g.drawImage(ResourceManager.getImage(RoboGame.MENU_LEVEL5_RSC), 246, 530);
    if(arrowBlink) {
      switch(select) {
        case 0:
          g.drawImage(ResourceManager.getImage(RoboGame.MENU_ARROW_RSC), 554, 260);
          break;
        case 1:
          g.drawImage(ResourceManager.getImage(RoboGame.MENU_ARROW_RSC), 554, 330);
          break;
        case 2:
          g.drawImage(ResourceManager.getImage(RoboGame.MENU_ARROW_RSC), 554, 400);
          break;
        case 3:
          g.drawImage(ResourceManager.getImage(RoboGame.MENU_ARROW_RSC), 554, 470);
          break;
        case 4:
          g.drawImage(ResourceManager.getImage(RoboGame.MENU_ARROW_RSC), 554, 540);
          break;
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
          case 0:
            ((TestState)game.getState(RoboGame.TESTSTATE)).setLevel(10);
            ResourceManager.getMusic(RoboGame.MUSIC_MENU_RSC).stop();
            rg.enterState(RoboGame.TESTSTATE);
            break;
          case 1:
            ((TestState)game.getState(RoboGame.TESTSTATE)).setLevel(2);
            ResourceManager.getMusic(RoboGame.MUSIC_MENU_RSC).stop();
            rg.enterState(RoboGame.TESTSTATE);
            break;
          case 2:
            ((TestState)game.getState(RoboGame.TESTSTATE)).setLevel(3);
            ResourceManager.getMusic(RoboGame.MUSIC_MENU_RSC).stop();
            rg.enterState(RoboGame.TESTSTATE);
            break;
          case 3:
            ((TestState)game.getState(RoboGame.TESTSTATE)).setLevel(4);
            ResourceManager.getMusic(RoboGame.MUSIC_MENU_RSC).stop();
            rg.enterState(RoboGame.TESTSTATE);
            break;
          case 4:
            ((TestState)game.getState(RoboGame.TESTSTATE)).setLevel(5);
            ResourceManager.getMusic(RoboGame.MUSIC_MENU_RSC).stop();
            rg.enterState(RoboGame.TESTSTATE);
            break;
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
    select = select % 6;
    if(select < 0) select = 5;

  }
}
