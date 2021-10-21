package RoboRampage;

import jig.Entity;
import jig.ResourceManager;
import jig.Vector;
import org.newdawn.slick.Animation;

public class Enemy extends Entity {
  private Vector velocity;
  private Coordinate location;
  private float speed;
  private int id; // 1 for melee, 2 for ranged
  private int health, maxhealth;
  private Animation leftIdle, rightIdle, activeAnimation, leftMove, rightMove, leftAttack, rightAttack, leftDefeat,
    rightDefeat;
  private boolean faceRight;

  public Enemy(final float x, final float y, int xcoord, int ycoord, int newid) {
    super( x + 37, y + 37);
    location = new Coordinate(xcoord, ycoord);
    speed = 0.25f;
    velocity = new Vector(0f, 0f);
    id = newid;
    health = maxhealth = 0;
    if(id == 1) {
      health = maxhealth = 5;
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
    }
    faceRight = false;
    activeAnimation = leftIdle;
    addAnimation(leftIdle);
    rightIdle.setLooping(true);
    leftIdle.setLooping(true);
    leftAttack.setLooping(false);
    rightAttack.setLooping(false);
  }

  public Coordinate getLocation() { return location;}

  public void makeMove(Vertex[][] pathMap, Coordinate playerLoc, Player player) {
    // Melee Enemy behavior
    if(id == 1) {
      // Check if the player is next to them
      int xdiff = playerLoc.x - location.x;
      int ydiff = playerLoc.y - location.y;
      // Square above
      if(xdiff == 0 && ydiff == 1) attack(8, player);
      // Square below
      else if(xdiff == 0 && ydiff == -1) attack(2, player);
      // Square left
      else if(xdiff == 1 && ydiff == 0) attack(4, player);
      // Square right
      else if(xdiff == -1 && ydiff == 0) attack(6, player);
      // Otherwise move
      else {
        // Choose which direction based on the dijkstras map
        int direction = pathMap[location.x][location.y].getDirection();
        if(direction == 2) moveDown();
        if(direction == 4) moveLeft();
        if(direction == 6) moveRight();
        if(direction == 8) moveUp();
      }
    }
  }

  public void attack(int direction, Player player) {
    System.out.println("Attacked: Hyah!" + direction);
    player.modHealth(-1);
    removeAnimation(activeAnimation);
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
    return false;
  }

  public Coordinate getCoordinate() { return location;}

  public int getID() { return id;}

  public int getHealth() { return health;}

  public int getMaxHealth() { return maxhealth;}

  public Vector getVelocity() { return velocity;}
}
