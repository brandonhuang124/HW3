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
  Vertex [][] rangedPath;
  LinkedList<Enemy> enemyList;
  LinkedList<Projectile> projectileList;
  boolean inputReady, enemyDead;
  boolean enemyTurn, enemyMoveWait, attackReady, gameover;
  int inputWaitTimer, levelOverTimer, aimDirection, turnDuration;
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
    turnDuration = 300;
    path = null;
    rangedPath = null;
    inputReady = true;
    enemyTurn = enemyMoveWait = attackReady = gameover = enemyDead = false;
    inputWaitTimer = levelOverTimer = 0;
    RoboGame rg = (RoboGame)game;
    player = new Player(75, 75, 1, 1);
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
          if(temp.getSubID() == 0) {
            g.drawImage(ResourceManager.getImage(RoboGame.TILE_FLOORIMG_RSC),x * 75, y * 75);
          }
          else if(temp.getSubID() == 1) {
            g.drawImage(ResourceManager.getImage(RoboGame.TILE_FLOORIMG2_RSC),x * 75, y * 75);
          }
          else if(temp.getSubID() == 2) {
            g.drawImage(ResourceManager.getImage(RoboGame.TILE_FLOORIMG3_RSC),x * 75, y * 75);
          }
          else if(temp.getSubID() == 3) {
            g.drawImage(ResourceManager.getImage(RoboGame.TILE_FLOORIMG4_RSC),x * 75, y * 75);
          }
        }
        if (temp.getID() == 1) {
          g.drawImage(ResourceManager.getImage(RoboGame.TILE_WALLIMG_RSC),x * 75, y * 75);
        }
      }
    }
    // Draw the numbers
    if(rangedPath != null) {
      for(int x = 0; x < 10; x++) {
        for(int y = 0; y < 10; y++) {
          if(rangedPath[x][y].getDistance() < 1000) {
            g.drawString("" + rangedPath[x][y].getDistance(), (x * 75) + 10, (y * 75) + 40);
            g.drawString("" + rangedPath[x][y].getDirection(), (x * 75) + 10, (y * 75) + 20);
          }
        }
      }
    }
    // Render HUD stuff
    g.drawImage(ResourceManager.getImage(RoboGame.UTIL_HUDIMG_RSC), 0, 750);
    if(attackReady) {
      crosshair.render(g);
    }
    // Current Gun x:410 y:765
    g.drawImage(ResourceManager.getImage(RoboGame.UTIL_GUNDEFAULT_RSC), 410, 765);
    // Ammo counter x: 520 y: 782 width:13
    int currentAmmo = player.getAmmo();
    int maxAmmo = player.getMaxAmmo();
    for(int i = 0; i < maxAmmo; i++) {
      if(currentAmmo > 0)
          g.drawImage(ResourceManager.getImage(RoboGame.UTIL_BULLET_RSC), 520 + (13 * i), 782);
      else
          g.drawImage(ResourceManager.getImage(RoboGame.UTIL_BULLETGONE_RSC), 520 + (13 * i), 782);
      currentAmmo--;
    }
    // Health Bar x:94 y:795 width:25
    int currentHealth = player.getHealth();
    int maxHealth = player.getMaxHealth();
    // Render left cap
    if(currentHealth == 0)
        g.drawImage(ResourceManager.getImage(RoboGame.UTIL_RBARCAPLEFT_RSC), 94, 795);
    else
        g.drawImage(ResourceManager.getImage(RoboGame.UTIL_GBARCAPLEFT_RSC), 94, 795);
    currentHealth--;
    // Render middlebar
    for(int i = 1; i < maxHealth - 1; i++) {
      if(currentHealth > 0)
        g.drawImage(ResourceManager.getImage(RoboGame.UTIL_GBAR_RSC), 94 + (25 * i), 795);
      else
        g.drawImage(ResourceManager.getImage(RoboGame.UTIL_RBAR_RSC), 94 + (25 * i), 795);
      currentHealth--;
    }
    // Render right cap
    if(currentHealth > 0)
      g.drawImage(ResourceManager.getImage(RoboGame.UTIL_GBARCAPRIGHT_RSC), 94 + (25 * (maxHealth - 1)), 795);
    else
      g.drawImage(ResourceManager.getImage(RoboGame.UTIL_RBARCAPRIGHT_RSC), 94 + (25 * (maxHealth - 1)), 795);
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
    rangedPath = RoboGame.getRangedDijkstras(player, tileMap);

    // Check if gameover occured
    if(gameover) {
      if(levelOverTimer <= 0) {
        ((StartState)game.getState(RoboGame.STARTUPSTATE)).restartMusic(true);
        rg.enterState(RoboGame.STARTUPSTATE);
      }
      levelOverTimer -= delta;
      return;
    }
    // Check for live projectiles
    if (!projectileList.isEmpty()) {
      // Pause enemy move animations
      for(Enemy enemy : enemyList) {
          enemy.pauseMoveAnimation();
      }
      // Update projectiles
      for(Projectile projectile : projectileList) projectile.update(delta);
      // Check for projectile collisions
      for(Projectile projectile : projectileList) {
        // If a player projectile:
        if(projectile.getID() == 1) {
          // Wall collision
          if (projectile.wallCollision(tileMap)) {
            System.out.println("Projectile hit wall");
            projectileList.remove(projectile);
          }
          // Enemy collision
          Enemy enemyHit = projectile.enemyCollision(enemyList);
          if(enemyHit != null) {
            System.out.println("Projectile hit enemy at: " + enemyHit.getCoordinate().x + ", " + enemyHit.getCoordinate().y);
            projectileList.remove(projectile);
            enemyDead = enemyHit.damage(5);
            if(enemyDead) waitInputDeathAnimation();
          }
        }
        // otherwise is an enemy projectile
        else {
          if(projectile.playerCollision(player)) {
            System.out.println("Projectile hit player.");
            projectileList.remove(projectile);
          }
        }
      }
      return;
    }

    // Update entity locations
    for(Enemy enemy : enemyList) {
      enemy.resumeAnimation();
      enemy.update(delta);
    }
    player.update(delta);

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
        player.faceLeft();
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
        player.faceRight();
      }
      // Cancel attack
      else if (input.isKeyPressed(Input.KEY_Q)) {
        attackReady = false;
        inputReady = true;
      }
      // Make attack
      else if (input.isKeyPressed(Input.KEY_SPACE)) {
        // Check if the player has enough ammo
        if(player.getAmmo() < 1) {
          System.out.println("Not enough ammo!");
          attackReady = false;
          inputReady = true;
        }
        else {
          attackReady = false;
          projectileList.add(new Projectile(playerLoc, aimDirection,1));
          player.modAmmo(-1);
          waitInput();
          System.out.println("Pew Pew");
          player.shoot(aimDirection);
        }
      }
    }
    // Check if controls are ready.
    else if (inputReady) {
      // If so check if the player is dead
      // If the player got hit
      if(player.gotHit()) {
        // Check if theyre dead
        if(player.getHealth() <= 0) {
          gameover = true;
          levelOverTimer = turnDuration * 8;
          player.death();
          System.out.println("Health dropped to 0, gameover...");
        }
      }
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
        attackReady = true;
        inputReady = false;
        crosshair.moveLeft(playerLoc);
        if(player.isFaceRight()) {
          aimDirection = 6;
          crosshair.moveRight(playerLoc);
        }
        else {
          aimDirection = 4;
          crosshair.moveLeft(playerLoc);
        }
      }
      // Reload
      else if (input.isKeyPressed(Input.KEY_R)) {
        System.out.println("Reloading...");
        player.reload();
        waitInput();
      }
    }
    else if (enemyTurn) {
      for (Enemy enemy : enemyList) {
        if(enemy.getID() == 1)
          enemy.makeMove(path, player.getLocation(), player, tileMap, projectileList);
        else if(enemy.getID() == 2) {
          enemy.makeMove(rangedPath, player.getLocation(), player, tileMap, projectileList);
        }
      }
      enemyTurn = false;
      enemyMoveWait = true;
      waitInput();
    }
    else {
      inputWaitTimer -= delta;
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
        // Clear any dead enemies
        enemyList.removeIf( (Enemy enemy) -> enemy.getHealth() <= 0);
        for(Enemy enemy : enemyList) {
          if(enemy.getHealth() <= 0) enemyList.remove(enemy);
        }
      }
    }

  }

  public void waitInput() {
    inputReady = false;
    inputWaitTimer = turnDuration;
  }

  public void waitInputDeathAnimation() {
    inputReady = false;
    inputWaitTimer = turnDuration * 2;
  }

  private void initLists() {
    enemyList = new LinkedList<Enemy>();
    enemyList.add(new Enemy(375,75,5,1,1));
    enemyList.add(new Enemy(525,75,7 ,1,1));
    enemyList.add(new Enemy(600, 75, 8, 1, 2));
    projectileList = new LinkedList<Projectile>();
  }
}
