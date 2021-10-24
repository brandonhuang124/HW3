package RoboRampage;

import jig.Entity;
import jig.ResourceManager;
import jig.Vector;
import org.newdawn.slick.Animation;
import org.newdawn.slick.Sound;

import java.util.LinkedList;

/***
 * Entity class for representing the enemies of RoboGame. Behavior differs depending on which id the enemy is
 * constructed with. Tracks enemy's health.
 *
 * Methods:
 *  makeMove()
 *  attack()
 *  spaceClear()
 *  moveUp()
 *  moveDown()
 *  moveRight()
 *  moveLeft()
 *  stop()
 *  dead()
 *  damage()
 *  pauseMoveAnimation()
 *  resumeMoveAnimation()
 */
public class Enemy extends Entity {
  private Vector velocity;
  private Coordinate location;
  private float speed;
  private int id; // 1 for melee, 2 for ranged
  private int health, maxhealth;
  private Animation leftIdle, rightIdle, activeAnimation, leftMove, rightMove, leftAttack, rightAttack, leftDefeat,
    rightDefeat;
  private Sound attackSound, moveSound, hitSound, defeatSound;
  private boolean faceRight, animationPaused;

  /***
   * Constructor.
   * @param x
   *  x coordinate in absolute coordinates to spawn the enemy in
   * @param y
   *  y coodrinate in absolute coordinate to spawn the enemy in
   * @param xcoord
   *  x coordinate in tilemap coordinates to spawn the enemy in
   * @param ycoord
   *  y coodrinate in tilemap coordinates to spawn the enemy in
   * @param newid
   *  The id of the enemy. Current available ids:
   *    1: Melee robot enemy type
   *    2: Ranged robot enemy type
   */
  public Enemy(final float x, final float y, int xcoord, int ycoord, int newid) {
    // Set paramaters
    super( x + 37, y + 37);
    location = new Coordinate(xcoord, ycoord);
    speed = 0.25f;
    velocity = new Vector(0f, 0f);
    id = newid;
    health = maxhealth = 0;
    /*** MELEE ENEMY: SET ANIMATIONS AND HEALTH ***/
    if(id == 1) {
      health = maxhealth = 10;
      addImageWithBoundingBox(ResourceManager.getImage(RoboGame.ENEMY_MELEEIMG_RSC));
      rightIdle = new Animation(ResourceManager.getSpriteSheet(
          RoboGame.ENEMY_MELEEIDLERIGHT_RSC, 75, 75), 0, 0, 3, 0,
          true, 75, true);
      leftIdle = new Animation(ResourceManager.getSpriteSheet(
          RoboGame.ENEMY_MELEEIDLELEFT_RSC, 75, 75), 0, 0, 3, 0,
          true, 75, true);
      rightMove = new Animation(ResourceManager.getSpriteSheet(
          RoboGame.ENEMY_MELEEMOVERIGHT_RSC, 75, 75), 0, 0, 3, 0,
          true, 75, true);
      leftMove = new Animation(ResourceManager.getSpriteSheet(
          RoboGame.ENEMY_MELEEMOVELEFT_RSC, 75, 75), 0, 0, 3, 0,
          true, 75, true);
      leftAttack = new Animation(ResourceManager.getSpriteSheet(
          RoboGame.ENEMY_MELEEATTACKLEFT_RSC, 75, 75), 0, 0, 3, 0,
          true, 150, true);
      rightAttack = new Animation(ResourceManager.getSpriteSheet(
          RoboGame.ENEMY_MELEEATTACKRIGHT_RSC, 75, 75), 0, 0, 3, 0,
          true, 150, true);
      leftDefeat = new Animation(ResourceManager.getSpriteSheet(
          RoboGame.ENEMY_MELEEDEFEATLEFT_RSC, 75, 75), 0, 0, 7, 0,
          true, 38, true);
      rightDefeat = new Animation(ResourceManager.getSpriteSheet(
          RoboGame.ENEMY_MELEEDEFEATRIGHT_RSC, 75, 75), 0, 0, 7, 0,
          true, 38, true);
      attackSound = ResourceManager.getSound(RoboGame.SOUND_PLAYERHITMELEE_RSC);
      moveSound = ResourceManager.getSound(RoboGame.SOUND_MELEEMOVE_RSC);
      hitSound = ResourceManager.getSound(RoboGame.SOUND_MELEEHIT_RSC);
      defeatSound = ResourceManager.getSound(RoboGame.SOUND_MELEEDEFEAT_RSC);
    }
    /*** RANGED ENEMY: SET ANIMATIONS AND HEALTH ***/
    else if (id == 2) {
      health = maxhealth = 5;
      addImageWithBoundingBox(ResourceManager.getImage(RoboGame.ENEMY_MELEEIMG_RSC));
      rightIdle = new Animation(ResourceManager.getSpriteSheet(
          RoboGame.ENEMY_RANGEDIDLERIGHT_RSC, 75, 75), 0, 0, 3, 0,
          true, 75, true);
      leftIdle = new Animation(ResourceManager.getSpriteSheet(
          RoboGame.ENEMY_RANGEDIDLELEFT_RSC, 75, 75), 0, 0, 3, 0,
          true, 75, true);
      rightMove = new Animation(ResourceManager.getSpriteSheet(
          RoboGame.ENEMY_RANGEDMOVERIGHT_RSC, 75, 75), 0, 0, 3, 0,
          true, 75, true);
      leftMove = new Animation(ResourceManager.getSpriteSheet(
          RoboGame.ENEMY_RANGEDMOVELEFT_RSC, 75, 75), 0, 0, 3, 0,
          true, 75, true);
      leftAttack = new Animation(ResourceManager.getSpriteSheet(
          RoboGame.ENEMY_RANGEDSHOOTLEFT_RSC, 75, 75), 0, 0, 3, 0,
          true, 150, true);
      rightAttack = new Animation(ResourceManager.getSpriteSheet(
          RoboGame.ENEMY_RANGEDSHOOTRIGHT_RSC, 75, 75), 0, 0, 3, 0,
          true, 150, true);
      leftDefeat = new Animation(ResourceManager.getSpriteSheet(
          RoboGame.ENEMY_RANGEDDEFEATLEFT_RSC, 75, 75), 0, 0, 7, 0,
          true, 75, true);
      rightDefeat = new Animation(ResourceManager.getSpriteSheet(
          RoboGame.ENEMY_RANGEDDEFEATRIGHT_RSC, 75, 75), 0, 0, 7, 0,
          true, 75, true);
      attackSound = ResourceManager.getSound(RoboGame.SOUND_LASER2_RSC);
      moveSound = ResourceManager.getSound(RoboGame.SOUND_RANGEDMOVE_RSC);
      hitSound = ResourceManager.getSound(RoboGame.SOUND_RANGEDHIT_RSC);
      defeatSound = ResourceManager.getSound(RoboGame.SOUND_RANGEDDEFEAT_RSC);
    }
    faceRight = false;
    activeAnimation = leftIdle;
    animationPaused = false;
    addAnimation(leftIdle);
    rightIdle.setLooping(true);
    leftIdle.setLooping(true);
    leftAttack.setLooping(false);
    rightAttack.setLooping(false);
  }

  public Coordinate getLocation() { return location;}

  /***
   * Method which is called when the enemy is ready to take a turn. Enemy will execute the appropriate move based on
   * their situation. Enemy logic is in this function.
   * @param pathMap
   *  The 2d Vertex array obtained by calling getDijktras() or getRangedDijkstras().
   * @param player
   *  The Player object which is the player character in the level.
   * @param tileMap
   *  The tilemap object representing the level the enemy is in
   * @param projectileList
   *  The projectile list of the level where new projectiles can be added to.
   * @param enemyList
   *  The linked list of enemies of the level, for checking locations of other enemies.
   */
  public void makeMove(Vertex[][] pathMap, Player player, Tile[][] tileMap, LinkedList<Projectile> projectileList, LinkedList<Enemy> enemyList) {
    /*** Begin Melee Behavior Block ***/
    Coordinate playerLoc = player.getLocation();
    if(id == 1) {
      // Check if the player is next to them
      int xdiff = playerLoc.x - location.x;
      int ydiff = playerLoc.y - location.y;
      // Square above
      if(xdiff == 0 && ydiff == 1) attack(8, player, projectileList);
      // Square below
      else if(xdiff == 0 && ydiff == -1) attack(2, player, projectileList);
      // Square left
      else if(xdiff == -1 && ydiff == 0) attack(4, player, projectileList);
      // Square right
      else if(xdiff == 1 && ydiff == 0) attack(6, player, projectileList);
      // Otherwise move
      else {
        int direction = pathMap[location.x][location.y].getDirection();
        if(direction == 2 && spaceClear(this.location.x, this.location.y + 1, enemyList))
          moveDown();
        if(direction == 4 && spaceClear(this.location.x - 1, this.location.y, enemyList))
          moveLeft();
        if(direction == 6 && spaceClear(this.location.x, this.location.y - 1, enemyList))
          moveRight();
        if(direction == 8 && spaceClear(this.location.x + 1, this.location.y, enemyList))
          moveUp();
        moveSound.play();
      }
    }
    /*** End Melee Behavior Block ***/

    /*** Begin Ranged Behavior Block ***/
    else if(id == 2) {
      // Check if player is in line with the enemy
      boolean lineOfSight = false;
      int attackDirection = 2;
      // If the player is on the same column
      if(playerLoc.x == this.location.x) {
        lineOfSight = true;
        boolean playerAbove = false;
        // Check if the player is above or below
        if (playerLoc.y < this.location.y)
            playerAbove = true;
        if(playerAbove) {
          attackDirection = 8;
          // Travel through the tile map until we get to the player checking if there is a wall on the way.
          for(int y = this.location.y; y > playerLoc.y; y--) {
            if(tileMap[this.location.x][y].getID() == 1) {
              lineOfSight = false;
              break;
            }
          }
        }
        else {
          attackDirection = 2;
          // Travel through the tile map until we get to the player, checking if there is a wall on the way
          for(int y = this.location.y; y < playerLoc.y; y++) {
            if (tileMap[this.location.x][y].getID() == 1) {
              lineOfSight = false;
              break;
            }
          }
        }
      }

      // If the player is on the same row
      else if(playerLoc.y == this.location.y) {
        lineOfSight = true;
        boolean playerToTheLeft = false;
        // Check if the player is right or left
        if (playerLoc.x < this.location.x)
          playerToTheLeft = true;
        if(playerToTheLeft) {
          attackDirection = 4;
          // Travel through the tile map until we get to the player, checking if there is a wall on the way
          for(int x = this.location.x; x > playerLoc.y; x--) {
            if(tileMap[x][this.location.y].getID() == 1) {
              lineOfSight = false;
              break;
            }
          }
        }
        else {
          attackDirection = 6;
          // Travel through the tile map until we get to the player, checking if there is a wall on the way
          for(int x = this.location.x; x < playerLoc.x; x++) {
            if (tileMap[x][this.location.y].getID() == 1) {
              lineOfSight = false;
              break;
            }
          }
        }
      }
      // Now if we have found no obstruction between the player and the enemy, and they were on the same row or column,
      if(lineOfSight) {
        attack(attackDirection, player, projectileList);
      }
      // Otherwise just make a move.
      else {
        int direction = pathMap[location.x][location.y].getDirection();
        if(direction == 2 && spaceClear(this.location.x, this.location.y + 1, enemyList))
          moveDown();
        if(direction == 4 && spaceClear(this.location.x - 1, this.location.y, enemyList))
          moveLeft();
        if(direction == 6 && spaceClear(this.location.x, this.location.y - 1, enemyList))
          moveRight();
        if(direction == 8 && spaceClear(this.location.x + 1, this.location.y, enemyList))
          moveUp();
        moveSound.play();
      }
    }
    /*** End Ranged Behavior Block ***/
  }

  /***
   * Method to be called if the enemy is going to make an attack on the player.
   * @param direction
   *  Direction of the attack:
   *    2: Down, 4: Left, 6: Up, 8: Right
   * @param player
   *  Player object of the player character in the level.
   * @param projectileList
   *  Linked list of projectiles of the level for adding more projectiles when ranged attacks occur.
   */
  public void attack(int direction, Player player, LinkedList<Projectile> projectileList) {
    System.out.println("Attacked: Hyah!" + direction);
    removeAnimation(activeAnimation);
    // Reface the enemy if the player is on a certain side
    if(direction == 4)
      faceRight = false;
    if(direction == 6)
      faceRight = true;
    // Attack animation for facing right
    if(faceRight) {
      addAnimation(rightAttack);
      rightAttack.restart();
      activeAnimation = rightAttack;
    }
    // Attack animation for facing left
    else {
      addAnimation(leftAttack);
      leftAttack.restart();
      activeAnimation = leftAttack;
    }
    attackSound.play();
    // If this is a melee attack
    if(id == 1) {
      player.modHealth(-1);
      System.out.println("Melee Attack");
    }
    // If this is a ranged attack
    else if(id == 2) {
      System.out.println("Ranged Attack");
      projectileList.add(new Projectile(this.location, direction, 2));
    }
  }

  /***
   * Internal function for checking if a certain space isn't occupied by another enemy.
   * @param x
   *  tilemap x coordinate to check
   * @param y
   *  tilemap y coordinate to check
   * @param enemyList
   *  Linked list of enemies to check.
   * @return
   *  true: if the space doesn't have an enemy in it.
   *  false: if the space does have an enemy in it.
   */
  private boolean spaceClear(int x, int y, LinkedList<Enemy> enemyList) {
    // Check the list to see if the space isn't already occupied.
    for(Enemy enemy : enemyList) {
      Coordinate enemyLoc = enemy.getCoordinate();
      if(enemy != this && enemyLoc.x == x && enemyLoc.y == y)
        return false;
    }
    return true;
  }

  /***
   * Functions to be called when the enemy is to move in a direction. One for each direction.
   */
  public void moveUp() {
    // Begin moving
    velocity = new Vector(0f, -speed);
    // Update location
    location.y = location.y - 1;
    // Set animations
    removeAnimation(activeAnimation);
    if(faceRight) {
      addAnimation(rightMove);
      activeAnimation = rightMove;
    }
    else {
      addAnimation(leftMove);
      activeAnimation = leftMove;
    }
  }

  public void moveLeft() {
    velocity = new Vector(-speed, 0f);
    location.x = location.x - 1;
    removeAnimation(activeAnimation);
    activeAnimation = leftMove;
    addAnimation(leftMove);
    faceRight = false;
  }

  public void moveDown() {
    velocity = new Vector(0f, speed);
    location.y = location.y + 1;
    removeAnimation(activeAnimation);
    if(faceRight) {
      addAnimation(rightMove);
      activeAnimation = rightMove;
    }
    else {
      addAnimation(leftMove);
      activeAnimation = leftMove;
    }
  }

  public void moveRight() {
    velocity = new Vector(speed, 0);
    location.x = location.x + 1;
    removeAnimation(activeAnimation);
    activeAnimation = rightMove;
    addAnimation(rightMove);
    faceRight = true;
  }
  /*** End of move functions ***/

  /***
   * Function to be called once the enemy must stop moving.
   */
  public void stop() {
    // Stop the movement
    velocity = new Vector(0f, 0f);
    // Update animations
    removeAnimation(activeAnimation);
    if(faceRight) {
      addAnimation(rightIdle);
      activeAnimation = rightIdle;
    }
    else {
      addAnimation(leftIdle);
      activeAnimation = leftIdle;
    }
  }

  /***
   * Function to be called once the enemies health drops to 0 or below. Starts death animations and sounds.
   */
  public void dead() {
    // Set animations
    removeAnimation(activeAnimation);
    if(faceRight) {
      addAnimation(rightDefeat);
      activeAnimation = rightDefeat;
    }
    else {
      addAnimation(leftDefeat);
      activeAnimation = leftDefeat;
    }
    // Play defeat sound
    defeatSound.play();
  }

  public void update(final int delta) {
    translate(velocity.scale(delta));
  }

  /***
   * This function is for modifying the enemies health when they are attacked by the player. Returns the death status of
   * the enemy.
   * @param damage
   *  The amount of damage taken.
   * @return
   *  true: if the enemy's health drops to 0 or below
   *  false: if the enemy still has health
   */
  public boolean damage(int damage) {
    health -= damage;
    if(this.health <= 0) {
      dead();
      return true;
    }
    hitSound.play();
    return false;
  }

  /***
   * Method to be called to stop enemy animations if they need to be paused, such as when a projectile is in the air
   * and melee enemies are moving.  Only works if the animations aren't already paused.
   */
  public void pauseMoveAnimation() {
    if(!animationPaused) {
      if(activeAnimation == leftMove){
        removeAnimation(activeAnimation);
        addAnimation(leftIdle);
        animationPaused = true;
      }
      if(activeAnimation == rightMove) {
        removeAnimation(activeAnimation);
        addAnimation(rightIdle);
        animationPaused = true;
      }
      // Stop sound effect as well
      ResourceManager.getSound(RoboGame.SOUND_MELEEMOVE_RSC).stop();
    }
  }

  /***
   * Method to be called to resume animations after calling pauseMoveAnimations(). Won't do anything if
   * pauseMoveAnimation() wasn't called on this object previously.
   */
  public void resumeAnimation() {
    if(animationPaused) {
      removeAnimation(rightIdle);
      removeAnimation(leftIdle);
      addAnimation(activeAnimation);
      animationPaused = false;
      ResourceManager.getSound(RoboGame.SOUND_MELEEMOVE_RSC).play();
    }
  }

  public Coordinate getCoordinate() { return location;}

  public int getID() { return id;}

  public int getHealth() { return health;}

  public int getMaxHealth() { return maxhealth;}

  public Vector getVelocity() { return velocity;}
}
