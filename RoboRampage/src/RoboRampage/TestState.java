package RoboRampage;

import java.util.*;

import jig.ResourceManager;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;

public class TestState extends BasicGameState {

  Tile tileMap[][];
  Vertex [][] path;
  LinkedList<Enemy> enemyList;
  LinkedList<Projectile> projectileList;
  boolean inputReady;
  boolean enemyTurn, enemyMoveWait, attackReady;
  int inputWaitTimer, aimDirection;
  Player player;
  Crosshair crosshair;

  @Override
  public int getID() {
    return RoboGame.TESTSTATE;
  }

  @Override
  public void init(GameContainer container, StateBasedGame game) throws SlickException {

  }

  @Override
  public void enter(GameContainer container, StateBasedGame game) {
    path = null;
    inputReady = true;
    enemyTurn = enemyMoveWait = attackReady = false;
    inputWaitTimer = 0;
    RoboGame rg = (RoboGame)game;
    player = rg.player;
    container.setSoundOn(true);
    crosshair = new Crosshair(0,0);
    initLists();
    tileMap = RoboGame.getTileMap
        ("1111111111100000000110111111011000000001100011000110001100011010000101101000010110100001011111111111");
  }

  @Override
  public void render(GameContainer container, StateBasedGame game, Graphics g) throws SlickException {
    RoboGame rg = (RoboGame)game;

    // Draw the tiles
    for(int y = 0; y < 10; y++) {
      for(int x = 0; x < 10; x++) {
        Tile temp = tileMap[x][y];
        if (temp.getID() == 0) {
          g.drawImage(ResourceManager.getImage(RoboGame.TILE_OPENGRIDIMG_RSC),x * 75, y * 75);
        }
        if (temp.getID() == 1) {
          g.drawImage(ResourceManager.getImage(RoboGame.TILE_CLOSEDGRIDIMG_RSC),x * 75, y * 75);
        }
      }
    }
    // Draw the numbers
    if(path != null) {
      for(int x = 0; x < 10; x++) {
        for(int y = 0; y < 10; y++) {
          if(path[x][y].getDistance() < 1000) {
            g.drawString("" + path[x][y].getDistance(), (x * 75) + 10, (y * 75) + 40);
            g.drawString("" + path[x][y].getDirection(), (x * 75) + 10, (y * 75) + 20);
          }
        }
      }
    }
    // Render HUD stuff
    if(attackReady) {
      crosshair.render(g);
    }
    // Render Entities
    player.render(g);
    for(Enemy enemy : enemyList) enemy.render(g);
    for(Projectile projectile : projectileList) projectile.render(g);
  }

  @Override
  public void update(GameContainer container, StateBasedGame game, int delta) throws SlickException {
    Input input = container.getInput();
    RoboGame rg = (RoboGame)game;
    Coordinate playerLoc = player.getLocation();
    path = RoboGame.getDijkstras(playerLoc.x,playerLoc.y,tileMap);

    // Update entity locations
    for(Enemy enemy : enemyList) enemy.update(delta);
    player.update(delta);
    for(Projectile projectile : projectileList) projectile.update(delta);

    // Check for projectile collisions
    for(Projectile projectile : projectileList) {
      // Wall collision
      if (projectile.wallCollision(tileMap)) {

      }
      // Enemy collsion
      Coordinate collisionLocation = projectile.enemyCollision(enemyList);
      if(collisionLocation.x != -1) {

      }
    }
    // Check if were in attack mode
    if(attackReady) {
      // Move Crosshair up
      if (input.isKeyPressed(Input.KEY_W)) {
        crosshair.moveUp(playerLoc);
        aimDirection = 8;
      }
      // Move Crosshair left
      else if (input.isKeyPressed(Input.KEY_A)) {
        crosshair.moveLeft(playerLoc);
        aimDirection = 4;
      }
      // Move Crosshair down
      else if (input.isKeyPressed(Input.KEY_S)) {
        crosshair.moveDown(playerLoc);
        aimDirection = 2;
      }
      // Move Crosshair right
      else if (input.isKeyPressed(Input.KEY_D)) {
        crosshair.moveRight(playerLoc);
        aimDirection = 6;
      }
      // Cancel attack
      else if (input.isKeyPressed(Input.KEY_Q)) {
        attackReady = false;
        inputReady = true;
      }
      // Make attack
      else if (input.isKeyPressed(Input.KEY_SPACE)) {
        attackReady = false;
        projectileList.add(new Projectile(playerLoc, aimDirection));
        waitInput();
        System.out.println("Pew Pew");
      }
    }
    // Check if controls are ready.
    else if (inputReady) {
      // Up
      if (input.isKeyPressed(Input.KEY_W) && tileMap[playerLoc.x][playerLoc.y-1].getID() != 1) {
        // Check if tile above is a wall
        player.moveUp();
        waitInput();
      }
      // Left
      else if (input.isKeyPressed(Input.KEY_A) && tileMap[playerLoc.x-1][playerLoc.y].getID() != 1) {
        // Check if tile left is a wall
        player.moveLeft();
        waitInput();
      }
      // Down
      else if (input.isKeyPressed(Input.KEY_S) && tileMap[playerLoc.x][playerLoc.y+1].getID() != 1) {
        player.moveDown();
        waitInput();
      }
      // Right
      else if (input.isKeyPressed(Input.KEY_D) && tileMap[playerLoc.x+1][playerLoc.y].getID() != 1) {
        player.moveRight();
        waitInput();
      }
      // Wait
      else if (input.isKeyPressed(Input.KEY_Q)) {
        waitInput();
      }
      // Attack

      else if (input.isKeyPressed(Input.KEY_SPACE)) {
        System.out.println("Space pressed");
        attackReady = true;
        inputReady = false;
        crosshair.moveLeft(playerLoc);
        aimDirection = 4;
      }
    }
    else if (enemyTurn) {
      for (Enemy enemy : enemyList) {
        enemy.makeMove(path, player.getLocation());
      }
      enemyTurn = false;
      enemyMoveWait = true;
      waitInput();
    }
    else {
      inputWaitTimer -= delta;
      System.out.println(inputWaitTimer);
      // Offset position back into the grid when done moving
      if(inputWaitTimer <= 0) {
        // If the enemies need to be reset
        if(enemyMoveWait) {
          for (Enemy enemy : enemyList) {
            enemy.update(inputWaitTimer);
            enemy.stop();
          }
          inputReady = true;
          enemyMoveWait = false;
        }
        // If the player needs to be reset
        else {
          player.update(inputWaitTimer);
          enemyTurn = true;
          player.stop();
        }
      }
    }

  }

  public void waitInput() {
    inputReady = false;
    inputWaitTimer = 75;
  }

  private void initLists() {
    enemyList = new LinkedList<Enemy>();
    enemyList.add(new Enemy(375,75,5,1,1));
    projectileList = new LinkedList<Projectile>();
  }
}
