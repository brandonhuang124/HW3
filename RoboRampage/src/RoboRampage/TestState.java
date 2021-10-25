package RoboRampage;

import java.util.*;

import jig.ResourceManager;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;

/***
 * This state is the gameplay state of RoboGame. Here is where the game is actually played. A map will render which the
 * player, enemies, and obstacles in the way. The objective is to defeat all the enemies on the the screen, after which
 * the next level will start after a short pause. The game is played in discrete turns, where the player takes an action
 * then all the enemies take an action. The player can Move, shoot, or reload on their turn, and the enemies can move or
 * attack on theirs. If the players health drops to 0, the game transitions back to startup state after a short delay.
 * The play can use wasd to move around the map, Q to skip their turn, R to reload, or Spacebar to begin aiming an
 * attack. After beginning aim, the player can use wasd to choose a direction, and space bar to confirm the attack in
 * that direction.
 *
 * Transitions From StartState
 *
 * Transitions To StartState
 */
public class TestState extends BasicGameState {

  Tile tileMap[][];
  Vertex [][] path;
  Vertex [][] rangedPath;
  LinkedList<Enemy> enemyList;
  LinkedList<Projectile> projectileList;
  LinkedList<PickupItem> pickupList;
  boolean inputReady, enemyDead;
  boolean rangedDijkstrasDisplay, dijkstrasDisplay;
  boolean enemyTurn, enemyMoveWait, attackReady, gameover, levelComplete;
  int inputWaitTimer, levelOverTimer, aimDirection, turnDuration, level;
  Player player;
  Crosshair crosshair;

  @Override
  public int getID() {
    return RoboGame.TESTSTATE;
  }

  @Override
  public void init(GameContainer container, StateBasedGame game) throws SlickException {
    level = 1;
  }

  @Override
  public void enter(GameContainer container, StateBasedGame game) {
    turnDuration = 300;
    container.setSoundOn(true);
    RoboGame rg = (RoboGame)game;
    player = new Player(75, 75, 1, 1);
    crosshair = new Crosshair(0,0);
    nextLevel();
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
        if (temp.getID() == 2) {
          g.drawImage(ResourceManager.getImage(RoboGame.TILE_FLOORACID_RSC), x * 75, y * 75);
        }
      }
    }

    // Render HUD stuff
    g.drawImage(ResourceManager.getImage(RoboGame.UTIL_HUDIMG_RSC), 0, 750);
    if(attackReady) {
      crosshair.render(g);
    }

    // Current Gun x:410 y:765
    if(player.getWeapon() == 1) {
      g.drawImage(ResourceManager.getImage(RoboGame.UTIL_GUNDEFAULT_RSC), 410, 765);
    }
    else if (player.getWeapon() == 2) {
      g.drawImage(ResourceManager.getImage(RoboGame.UTIL_GUNBEAM_RSC), 410, 775);
    }

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

    // Hud ready indicator
    if(inputReady) {
      g.drawImage(ResourceManager.getImage(RoboGame.UTIL_HUDREADY_RSC), 600, 770);
    }
    else {
      g.drawImage(ResourceManager.getImage(RoboGame.UTIL_HUDWAIT_RSC), 600, 770);
    }

    // Render item pickups
    for(PickupItem pickup : pickupList) {
      // If its a armor pickup
      if(pickup.getId() == 1) {
        Coordinate loc = pickup.getLocation();
        g.drawImage(ResourceManager.getImage(RoboGame.UTIL_PICKUPARMOR_RSC), loc.x * 75, loc.y * 75);
      }
      else if(pickup.getId() ==2) {
        Coordinate loc = pickup.getLocation();
        g.drawImage(ResourceManager.getImage(RoboGame.UTIL_PICKUPBEAMGUN_RSC), loc.x * 75, loc.y * 75);
      }
    }
    // Render Entities
    player.render(g);
    for(Enemy enemy : enemyList) enemy.render(g);
    for(Projectile projectile : projectileList) projectile.render(g);

    // Draw the Dijkstras overlay if enabled.
    if(dijkstrasDisplay) {
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
    }

    else if(rangedDijkstrasDisplay) {
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
    }

    // Render the Gameover message if needed.
    if(gameover) {
      g.drawImage(ResourceManager.getImage(RoboGame.MENU_GAMEOVER_RSC), 119, 300);
    }

    // Render the level complete message if needed.
    if(levelComplete) {
      g.drawImage(ResourceManager.getImage(RoboGame.MENU_LEVELCOMPLETE_RSC), 113,300);
    }
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
      // If so, countdown until transitioning.
      if(levelOverTimer <= 0) {
        ((StartState)game.getState(RoboGame.STARTUPSTATE)).restartMusic(true);
        rg.enterState(RoboGame.STARTUPSTATE);
      }
      levelOverTimer -= delta;
      return;
    }

    // Check if all the enemies have been defeated
    if(enemyList.isEmpty() && !levelComplete) {
      levelComplete = true;
      levelOverTimer = turnDuration * 8;
      ResourceManager.getSound(RoboGame.SOUND_EXPLOSION_RSC).play();
    }

    // If all the enemies are dead, the level is complete.
    if(levelComplete) {
      System.out.println(levelOverTimer);
      if(levelOverTimer <= 0) {
        // Check if the last level was completed.
        level++;
        if(level == 6) {
          rg.enterState(RoboGame.GAMECOMPLETESTATE);
          return;
        }
        nextLevel();
      }
      levelOverTimer -= delta;
      return;
    }

    // Check for projectiles, noone can take actions if projectiles are in the air.
    if (!projectileList.isEmpty()) {
      // Pause enemy move animations
      for(Enemy enemy : enemyList) {
          enemy.pauseMoveAnimation();
      }
      // Update projectile location
      for(Projectile projectile : projectileList) projectile.update(delta);
      // Check for projectile collisions
      for(Projectile projectile : projectileList) {
        // If a player projectile, check for wall and enemy collisions
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
        // If a beam projectile collision,
        else if(projectile.getID() == 3) {
          if (projectile.wallCollision(tileMap)) {
            System.out.println("Projectile hit wall");
            projectileList.remove(projectile);
          }
          // Enemy collision
          Enemy enemyHit = projectile.enemyCollision(enemyList);
          if(enemyHit != null) {
            System.out.println("Projectile hit enemy at: " + enemyHit.getCoordinate().x + ", " + enemyHit.getCoordinate().y);
            enemyDead = enemyHit.damage(10);
            if(enemyDead) waitInputDeathAnimation();
          }
        }
        // If an enemy projectile, check for player collisions. (Should never shoot at a wall due to behavior)
        else {
          if(projectile.playerCollision(player)) {
            System.out.println("Projectile hit player.");
            projectileList.remove(projectile);
          }
        }
      }
      return;
    }

    // Update entity locations as long as no projectiles are in the air.
    for(Enemy enemy : enemyList) {
      enemy.resumeAnimation();
      enemy.update(delta);
    }
    player.update(delta);

    /*** ATTACK MODE SECTIONS ***/
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
          if(player.getWeapon() == 1) {
            projectileList.add(new Projectile(playerLoc, aimDirection,1));
            player.modAmmo(-1);
          }
          else if(player.getWeapon() == 2) {
            projectileList.add(new Projectile(playerLoc, aimDirection, 3));
            player.modAmmo(-1);
          }
          waitInput();
          System.out.println("Pew Pew");
          player.shoot(aimDirection);
        }
      }
    }

    /*** USUAL CONTROLS SECTION ***/
    // Check if controls are ready.
    else if (inputReady) {
      // If the player got hit
      if(player.gotHit()) {
        // Check if theyre dead
        if(player.getHealth() <= 0) {
          gameover = true;
          levelOverTimer = turnDuration * 8;
          player.death();
          System.out.println("Health dropped to 0, gameover...");
          return;
        }
      }

      // Up
      if (input.isKeyPressed(Input.KEY_W) && isSpaceClear(playerLoc.x, playerLoc.y-1)) {
        // Check if tile above is a wall
        player.moveUp();
        waitInput();
      }

      // Left
      else if (input.isKeyPressed(Input.KEY_A) && isSpaceClear(playerLoc.x-1, playerLoc.y)) {
        // Check if tile left is a wall
        player.moveLeft();
        waitInput();
      }

      // Down
      else if (input.isKeyPressed(Input.KEY_S) && isSpaceClear(playerLoc.x, playerLoc.y+1)) {
        player.moveDown();
        waitInput();
      }

      // Right
      else if (input.isKeyPressed(Input.KEY_D) && isSpaceClear(playerLoc.x+1, playerLoc.y)) {
        player.moveRight();
        waitInput();
      }

      // Wait
      else if (input.isKeyPressed(Input.KEY_Q)) {
        waitInput();
      }

      // Attack
      else if (input.isKeyPressed(Input.KEY_SPACE)) {
        ResourceManager.getSound(RoboGame.SOUND_AIM_RSC).play();
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

      // Enable Dijkstras overlay
      else if(input.isKeyPressed(Input.KEY_9)) {
        dijkstrasDisplay = !dijkstrasDisplay;
        if(rangedDijkstrasDisplay)
          rangedDijkstrasDisplay = false;
      }

      // Enable Ranged Dijkstras overlay
      else if(input.isKeyPressed(Input.KEY_0)) {
        rangedDijkstrasDisplay = !rangedDijkstrasDisplay;
        if(dijkstrasDisplay)
          dijkstrasDisplay = false;
      }
    }

    /*** ENEMY TURN SECTION ***/
    else if (enemyTurn) {
      // Check if the player is on a pickup:
      for (PickupItem item : pickupList) {
        if(item.playerCollision(player)) {
          // If its a armor pickup
          if(item.getId() == 1) {
            player.restoreHealth();
          }
          else if(item.getId() == 2) {
            player.changeWeapon(2);
          }
          // Play the sound
          ResourceManager.getSound(RoboGame.SOUND_POWERUP_RSC).play();
        }
      }
      // Clear any items that need clearing.
      pickupList.removeIf( (PickupItem pickup) -> pickup.getRemoveMe());
      for (Enemy enemy : enemyList) {
        if(enemy.getID() == 1)
          enemy.makeMove(path, player, tileMap, projectileList, enemyList);
        else if(enemy.getID() == 2) {
          enemy.makeMove(rangedPath, player, tileMap, projectileList, enemyList);
        }
      }
      // Check if the player is standing in acid
      if(tileMap[playerLoc.x][playerLoc.y].getID() == 2) {
        player.modHealth(-1);
      }
      enemyTurn = false;
      enemyMoveWait = true;
      waitInput();
    }
    /*** IN BETWEEN TURNS SECTION ***/
    // Here the wait timer is countdown and entitys are snapped back into the grid if they strayed.
    else {
      input.clearKeyPressedRecord();
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

  /***
   * Internal method to be called when moves are being executed.
   */
  private void waitInput() {
    inputReady = false;
    inputWaitTimer = turnDuration;
  }

  /***
   * Internal method to be called when the player is dead.
   */
  private void waitInputDeathAnimation() {
    inputReady = false;
    inputWaitTimer = turnDuration * 2;
  }

  /***
   * Internal method which initializes Lists which are needed for level function
   */
  private void initLists() {
    enemyList = new LinkedList<Enemy>();
    projectileList = new LinkedList<Projectile>();
    pickupList = new LinkedList<PickupItem>();
  }

  /***
   * Internal method that checks if the node in the tilemap is open for the player to move into.
   * @param x
   *  The x coordinate in tile coordinates
   * @param y
   *  The y coordinate in tile coordinates
   * @return
   *  true: if the space is unoccupied and isnt a wall
   *  false: if the space is occupied or a wall.
   */
  private boolean isSpaceClear(int x, int y) {
    // Check if moving would take us out of the map
    // First check if the space is a wall
    if (tileMap[x][y].getID() == 1)
      return false;
    // Then check if the space is occupied by an enemy
    for (Enemy enemy : enemyList) {
      Coordinate enemyLoc = enemy.getLocation();
      if (enemyLoc.x == x && enemyLoc.y == y)
        return false;
    }
    return true;
  }

  public void setLevel(int newLevel) {
    level = newLevel;
  }

  /***
   * Internal function that builds the level based on the current level.
   */
  private void nextLevel() {
    // Reset various fields to restart the level.
    path = null;
    rangedPath = null;
    inputReady = true;
    enemyTurn = enemyMoveWait = attackReady = gameover = enemyDead = levelComplete = dijkstrasDisplay = rangedDijkstrasDisplay = false;
    inputWaitTimer = levelOverTimer = 0;
    player.changeWeapon(1);
    initLists();
    tileMap = RoboGame.getTileMap
        ("1111111111102000000110111111011000000001100011000110001100011010000101101000010110100001011111111111");
    // Builds for different levels.
    if(level == 1) {
      tileMap = RoboGame.getTileMap
          ("1111111111100100011110010001111101000111100010111110000000011000110011111110000111111000011111111111");
      player.resetPlayer(1,1);
      enemyList.add(new Enemy(6,1,1));
      enemyList.add(new Enemy(2,6,1));
      enemyList.add(new Enemy(8,5,1));
      enemyList.add(new Enemy(6,8,1));
    }
    else if (level == 2) {
      tileMap = RoboGame.getTileMap
          ("1111111111102000000110111111011000000001111100111110000000011011001101101100110110000000011111111111");
      player.resetPlayer(1,1);
      pickupList.add(new PickupItem(4,4,1));
      enemyList.add(new Enemy(7,1,1));
      enemyList.add(new Enemy(3,5,1));
      enemyList.add(new Enemy(8,3,2));
      enemyList.add(new Enemy(8,5,2));
      enemyList.add(new Enemy(1,8,2));
    }
    else if (level == 3) {
      tileMap = RoboGame.getTileMap
          ("1111111111100000001110001000111111121111111110111110001011111000101001100010100110000000011111111111");
      player.resetPlayer(1,1);
      pickupList.add(new PickupItem(3,2,2));
      pickupList.add(new PickupItem(7,1,1));
      enemyList.add(new Enemy(2,8,1));
      enemyList.add(new Enemy(3,8,1));
      enemyList.add(new Enemy(7,8,1));
      enemyList.add(new Enemy(8,8,1));
      enemyList.add(new Enemy(1,6,2));
      enemyList.add(new Enemy(2,7,2));
      enemyList.add(new Enemy(6,2,2));
    }
    else if (level == 4) {
      tileMap = RoboGame.getTileMap
          ("1111111111100111000110010000011001010001110001000111011111111000110001100000000110001100011111111111");
      player.resetPlayer(1,1);
      pickupList.add(new PickupItem(6,2,2));
      pickupList.add(new PickupItem(3,6,1));
      enemyList.add(new Enemy(4,4,1));
      enemyList.add(new Enemy(7,4,1));
      enemyList.add(new Enemy(8,3,1));
      enemyList.add(new Enemy(8,7,1));
      enemyList.add(new Enemy(8,4,2));
      enemyList.add(new Enemy(3,7,2));
      enemyList.add(new Enemy(8,6,2));
      enemyList.add(new Enemy(8,8,2));
    }
    else if (level == 5) {
      tileMap = RoboGame.getTileMap
          ("1111111111111000011111100000111011010001101000100110000020011010001001100101000111000000111111111111");
      player.resetPlayer(4,5);
      pickupList.add(new PickupItem(3,4,1));
      pickupList.add(new PickupItem(2,8,2));
      enemyList.add(new Enemy(4,7,1));
      enemyList.add(new Enemy(2,7,1));
      enemyList.add(new Enemy(4,2,1));
      enemyList.add(new Enemy(8,5,1));
      enemyList.add(new Enemy(1,4,2));
      enemyList.add(new Enemy(7,8,2));
      enemyList.add(new Enemy(4,1,2));
      enemyList.add(new Enemy(7,3,2));
    }
    else {
      tileMap = RoboGame.getTileMap
          ("1111111111102000000110111111011000000001100011000110001100011010000101101000010110100001011111111111");
      player.resetPlayer(1,1);
      pickupList.add(new PickupItem(2,4,1));
      pickupList.add(new PickupItem(2,5,2));
      enemyList.add(new Enemy(5,1,1));
      enemyList.add(new Enemy(7 ,1,2));
      enemyList.add(new Enemy(8, 1, 2));
    }
    // block the screen while new level is being built
  }
}
