package RoboRampage;

import jig.ResourceManager;
import org.newdawn.slick.*;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;

/***
 * This state is the main menu and startup state of the game. The player can use the arrows keys or wasd to move through
 * selections in the menu, and space bar selects the current selection. Depending on what the player selects, they enter
 * the how to play screen, level select screen, or simply begins the game at level 1.
 *
 * Transitions From (Initialization)
 *
 * Transitions To HowState, LevelState and TestState
 */
public class StartState extends BasicGameState {

  private int select, timer;
  private boolean selected, arrowBlink, musicRestart;
  Animation player, melee, melee2, ranged;

  @Override
  public int getID() {
    return RoboGame.STARTUPSTATE;
  }

  @Override
  public void init(GameContainer container, StateBasedGame game) throws SlickException {
    musicRestart = true;
  }

  @Override
  public void enter(GameContainer container, StateBasedGame game) {
    RoboGame rg = (RoboGame)game;
    container.setSoundOn(true);
    select = 0;
    selected = false;
    arrowBlink = true;
    player = new Animation(ResourceManager.getSpriteSheet(
        RoboGame.PLAYER_PLAYERSHOOTRIGHT_RSC, 75, 75), 0, 0, 3, 0,
        true, 120, true);
    melee = new Animation(ResourceManager.getSpriteSheet(
        RoboGame.ENEMY_MELEEMOVELEFT_RSC, 75, 75), 0, 0, 3, 0,
        true, 120, true);
    ranged = new Animation(ResourceManager.getSpriteSheet(
        RoboGame.ENEMY_RANGEDSHOOTLEFT_RSC, 75, 75), 0, 0, 3, 0,
        true, 120, true);
    // Only restart the music if before entering the state, music restart has been requested.
    if(musicRestart) {
      ResourceManager.getMusic(RoboGame.MUSIC_MENU_RSC).loop();
    }
    Input input = container.getInput();
    input.clearKeyPressedRecord();
  }

  @Override
  public void render(GameContainer container, StateBasedGame game, Graphics g) throws SlickException {
    // Draw Menu splash and buttons
    g.drawImage(ResourceManager.getImage(RoboGame.MENU_SPLASH_RSC), 0, 0);
    g.drawImage(ResourceManager.getImage(RoboGame.MENU_START_RSC), 290, 450);
    g.drawImage(ResourceManager.getImage(RoboGame.MENU_HOWTOPLAY_RSC), 210, 520);
    g.drawImage(ResourceManager.getImage(RoboGame.MENU_LEVELSELECT_RSC), 183, 590);
    // Draw the little animations at the bottom of the screen
    g.drawAnimation(player, 150, 750);
    g.drawAnimation(melee, 350, 750);
    g.drawAnimation(melee, 470, 750);
    g.drawAnimation(ranged, 550, 750);
    // Draw Arrow Location
    if (arrowBlink) {
      switch(select) {
        case 1:
          g.drawImage(ResourceManager.getImage(RoboGame.MENU_ARROW_RSC), 590, 530);
          break;
        case 2:
          g.drawImage(ResourceManager.getImage(RoboGame.MENU_ARROW_RSC), 617, 600);
          break;
        default:
          g.drawImage(ResourceManager.getImage(RoboGame.MENU_ARROW_RSC), 510, 460);
          break;
      }
    }
  }

  @Override
  public void update(GameContainer container, StateBasedGame game, int delta) throws SlickException {

    Input input = container.getInput();
    RoboGame rg = (RoboGame)game;

    // Check if a selection has been made, if so, do a countdown before transitioning.
    if(selected) {
      if(timer % 15 == 0) {
        arrowBlink = !arrowBlink;
      }
      // Once wait period is over
      if(timer <= 0) {
        switch(select) {
          case 1:
            System.out.println("To how to play...");
            rg.enterState(RoboGame.HOWTOPLAYSTATE);
            break;
          case 2:
            System.out.println("To Level Select...");
            rg.enterState(RoboGame.LEVELSELECTSTATE);
            break;
          default:
            System.out.println("Game starting...");
            ResourceManager.getMusic(RoboGame.MUSIC_MENU_RSC).stop();
            ((TestState)game.getState(RoboGame.TESTSTATE)).setLevel(1);
            rg.enterState(RoboGame.TESTSTATE);
            break;
        }
      }
      timer --;
    }

    /*** Controls Section ***/
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
    /*** End Controls Section ***/
    // Mod select by 3 to snap back to top, set negatives to 2 to snap to the bottom when going up at the top.
    select = select % 3;
    if(select < 0) select = 2;
  }

  public void restartMusic(boolean state) {musicRestart = state;}
}
