package RoboRampage;

import jig.ResourceManager;
import org.newdawn.slick.*;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;

public class StartState extends BasicGameState {

  private int select, timer;
  private boolean selected, arrowBlink;
  Animation player, melee, melee2, ranged;
  @Override
  public int getID() {
    return RoboGame.STARTUPSTATE;
  }

  @Override
  public void init(GameContainer container, StateBasedGame game) throws SlickException {

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
  }

  @Override
  public void render(GameContainer container, StateBasedGame game, Graphics g) throws SlickException {
    // Draw Menu splash and buttons
    g.drawImage(ResourceManager.getImage(RoboGame.MENU_SPLASH_RSC), 0, 0);
    g.drawImage(ResourceManager.getImage(RoboGame.MENU_START_RSC), 290, 450);
    g.drawImage(ResourceManager.getImage(RoboGame.MENU_HOWTOPLAY_RSC), 210, 520);
    g.drawImage(ResourceManager.getImage(RoboGame.MENU_LEVELSELECT_RSC), 183, 590);
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

    // Checking if a selection has been made
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
            rg.enterState(RoboGame.TESTSTATE);
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
    else if(input.isKeyPressed(Input.KEY_S) || input.isKeyPressed(Input.KEY_DOWN)) {
      select ++;
    }
    // If moving up
    else if(input.isKeyPressed(Input.KEY_W) || input.isKeyPressed(Input.KEY_UP)) {
      select --;
    }
    // Mod select by 3 to snap back to top, set negatives to 2 to snap to the bottom when going up at the top.
    select = select % 3;
    if(select < 0) select = 2;
  }
}
