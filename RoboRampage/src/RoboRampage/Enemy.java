package RoboRampage;

import jig.Entity;
import jig.ResourceManager;
import jig.Vector;
import org.newdawn.slick.Animation;
import org.newdawn.slick.Sound;

import java.util.LinkedList;

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

  public Enemy(final float x, final float y, int xcoord, int ycoord, int newid) {
    super( x + 37, y + 37);
    location = new Coordinate(xcoord, ycoord);
    speed = 0.25f;
    velocity = new Vector(0f, 0f);
    id = newid;
    health = maxhealth = 0;
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
          true, 38, true);
      rightDefeat = new Animation(ResourceManager.getSpriteSheet(
          RoboGame.ENEMY_RANGEDDEFEATRIGHT_RSC, 75, 75), 0, 0, 7, 0,
          true, 38, true);
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

  public void makeMove(Vertex[][] pathMap, Coordinate playerLoc, Player player, Tile[][] tileMap,LinkedList<Projectile> projectileList) {
    /*** Begin Melee Behavior Block ***/
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
        // Choose which direction based on the dijkstras map
        int direction = pathMap[location.x][location.y].getDirection();
        if(direction == 2) moveDown();
        if(direction == 4) moveLeft();
        if(direction == 6) moveRight();
        if(direction == 8) moveUp();
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
          for(int y = this.location.y; y > playerLoc.y; y--) {
            if(tileMap[this.location.x][y].getID() == 1) {
              lineOfSight = false;
              break;
            }
          }
        }
        else {
          attackDirection = 2;
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
          for(int x = this.location.x; x > playerLoc.y; x--) {
            if(tileMap[x][this.location.y].getID() == 1) {
              lineOfSight = false;
              break;
            }
          }
        }
        else {
          attackDirection = 6;
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
        if(direction == 2) moveDown();
        if(direction == 4) moveLeft();
        if(direction == 6) moveRight();
        if(direction == 8) moveUp();
        moveSound.play();
      }
    }
    /*** End Ranged Behavior Block ***/
  }

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

  public void moveUp() {
    velocity = new Vector(0f, -speed);
    location.y = location.y - 1;
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

  public void stop() {
    velocity = new Vector(0f, 0f);
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

  public void dead() {
    removeAnimation(activeAnimation);
    if(faceRight) {
      addAnimation(rightDefeat);
      activeAnimation = rightDefeat;
    }
    else {
      addAnimation(leftDefeat);
      activeAnimation = leftDefeat;
    }
    defeatSound.play();
  }

  public void update(final int delta) {
    translate(velocity.scale(delta));
  }

  public boolean damage(int damage) {
    health -= damage;
    if(this.health <= 0) {
      dead();
      return true;
    }
    hitSound.play();
    return false;
  }

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
    }
  }

  public void resumeAnimation() {
    if(animationPaused) {
      removeAnimation(rightIdle);
      removeAnimation(leftIdle);
      addAnimation(activeAnimation);
      animationPaused = false;
    }
  }

  public Coordinate getCoordinate() { return location;}

  public int getID() { return id;}

  public int getHealth() { return health;}

  public int getMaxHealth() { return maxhealth;}

  public Vector getVelocity() { return velocity;}
}
