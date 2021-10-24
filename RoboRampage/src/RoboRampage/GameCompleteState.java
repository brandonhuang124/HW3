package RoboRampage;

import jig.ResourceManager;
import org.newdawn.slick.*;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;

/***
 * This state is the game complete state for after the player finishes all the levels. The player can press space to
 * return to the start state, or esc to close the game.
 *
 * Transitions From TestState
 *
 * Transitions To StartState
 */
public class GameCompleteState extends BasicGameState {
  @Override
  public int getID() {
    return 4;
  }

  @Override
  public void enter(GameContainer container, StateBasedGame game) {
    RoboGame rg = (RoboGame)game;
    container.setSoundOn(true);
    Input input = container.getInput();
    input.clearKeyPressedRecord();
    ResourceManager.getSound(RoboGame.SOUND_VICTORY_RSC).play();
  }

  @Override
  public void init(GameContainer gameContainer, StateBasedGame stateBasedGame) throws SlickException {

  }

  @Override
  public void render(GameContainer gameContainer, StateBasedGame stateBasedGame, Graphics graphics) throws SlickException {
    graphics.drawImage(ResourceManager.getImage(RoboGame.MENU_GAMECOMPLETE_RSC), 0, 0);
  }

  @Override
  public void update(GameContainer gameContainer, StateBasedGame game, int i) throws SlickException {

    Input input = gameContainer.getInput();
    RoboGame rg = (RoboGame)game;

    if(input.isKeyPressed(Input.KEY_SPACE)) {
      // Go to the menu
      ((StartState)game.getState(RoboGame.STARTUPSTATE)).restartMusic(true);
      rg.enterState(RoboGame.STARTUPSTATE);
    }
    else if(input.isKeyPressed(Input.KEY_ESCAPE)) {
      // Close the game
      gameContainer.exit();
    }
  }
}
