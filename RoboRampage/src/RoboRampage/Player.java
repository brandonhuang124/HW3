package RoboRampage;

import jig.Entity;
import jig.ResourceManager;
import jig.Vector;
import org.newdawn.slick.Animation;

class Player extends Entity {
  private Vector velocity;
  private float speed;
  private Coordinate location;
  private int ammo, maxammo;
  private int health, maxhealth;
  private Animation moveLeft, moveRight, shootLeft, shootRight, reloadLeft, reloadRight, idleLeft, idleRight,
      defeatRight, defeatLeft, activeAnim;
  private boolean faceRight, gotHit;

  public Player(final float x, final float y, int xcoord, int ycoord) {
    super(x + 37,y + 37);
    addImageWithBoundingBox(ResourceManager.getImage(RoboGame.PLAYER_PLAYERIMG_RSC));
    location = new Coordinate(xcoord, ycoord);
    velocity = new Vector(0f,0f);
    speed = 0.25f;
    maxammo = 5;
    maxhealth = 10;
    health = maxhealth;
    ammo = maxammo;
    idleLeft = new Animation(ResourceManager.getSpriteSheet(
        RoboGame.PLAYER_PLAYERIDLELEFT_RSC, 75, 75), 0, 0, 0, 0,
        true, 75, true);
    idleRight = new Animation(ResourceManager.getSpriteSheet(
        RoboGame.PLAYER_PLAYERIDLERIGHT_RSC, 75, 75), 0, 0, 0, 0,
        true, 75, true);
    moveLeft = new Animation(ResourceManager.getSpriteSheet(
        RoboGame.PLAYER_PLAYERMOVELEFT_RSC, 75, 75), 0, 0, 3, 0,
        true, 75, true);
    moveRight = new Animation(ResourceManager.getSpriteSheet(
        RoboGame.PLAYER_PLAYERMOVERIGHT_RSC, 75, 75), 0, 0, 3, 0,
        true, 75, true);
    shootLeft = new Animation(ResourceManager.getSpriteSheet(
        RoboGame.PLAYER_PLAYERSHOOTLEFT_RSC, 75, 75), 0, 0, 3, 0,
        true, 75, true);
    shootRight = new Animation(ResourceManager.getSpriteSheet(
        RoboGame.PLAYER_PLAYERSHOOTRIGHT_RSC, 75, 75), 0, 0, 3, 0,
        true, 75, true);
    reloadLeft = new Animation(ResourceManager.getSpriteSheet(
        RoboGame.PLAYER_PLAYERRELOADLEFT_RSC, 75, 75), 0, 0, 3, 0,
        true, 75, true);
    reloadRight = new Animation(ResourceManager.getSpriteSheet(
        RoboGame.PLAYER_PLAYERRELOADRIGHT_RSC, 75, 75), 0, 0, 3, 0,
        true, 75, true);
    defeatLeft = new Animation(ResourceManager.getSpriteSheet(
        RoboGame.PLAYER_PLAYERDEATHLEFT_RSC, 75, 75), 0, 0, 6, 0,
        true, 100, true);
    defeatRight = new Animation(ResourceManager.getSpriteSheet(
        RoboGame.PLAYER_PLAYERDEATHRIGHT_RSC, 75, 75), 0, 0, 6, 0,
        true, 100, true);
    activeAnim = idleRight;
    addAnimation(idleRight);
    faceRight = true;
    gotHit = false;
    shootLeft.setLooping(false);
    shootRight.setLooping(false);
    reloadRight.setLooping(false);
    reloadLeft.setLooping(false);
    defeatRight.setLooping(false);
    defeatLeft.setLooping(false);
  }

  public Coordinate getLocation() { return location;}

  public void resetPlayer(float x, float y, int xcoord, int ycoord) {
    health = maxhealth;
    ammo = maxammo;
    activeAnim = idleRight;
    addAnimation(idleRight);
    faceRight = true;
    gotHit = false;
    setPosition(x + 37, y = 37);
    addImageWithBoundingBox(ResourceManager.getImage(RoboGame.PLAYER_PLAYERIMG_RSC));
    location = new Coordinate(xcoord, ycoord);
    velocity = new Vector(0f,0f);
  }

  public void moveUp() {
    velocity = new Vector(0f, -speed);
    location.y = location.y - 1;
    removeAnimation(activeAnim);
    if(faceRight) {
      activeAnim = moveRight;
      addAnimation(moveRight);
    }
    else {
      activeAnim = moveLeft;
      addAnimation(moveLeft);
    }
    ResourceManager.getSound(RoboGame.SOUND_FOOTSTEPS_RSC).play();
  }

  public void moveLeft() {
   velocity = new Vector(-speed, 0f);
   location.x = location.x - 1;
   removeAnimation(activeAnim);
   addAnimation(moveLeft);
   activeAnim = moveLeft;
   faceRight = false;
   ResourceManager.getSound(RoboGame.SOUND_FOOTSTEPS_RSC).play();
  }

  public void moveDown() {
    velocity = new Vector(0f, speed);
    location.y = location.y + 1;
    removeAnimation(activeAnim);
    if(faceRight) {
      activeAnim = moveRight;
      addAnimation(moveRight);
    }
    else {
      activeAnim = moveLeft;
      addAnimation(moveLeft);
    }
    ResourceManager.getSound(RoboGame.SOUND_FOOTSTEPS_RSC).play();
  }

  public void moveRight() {
    velocity = new Vector(speed, 0);
    location.x = location.x + 1;
    removeAnimation(activeAnim);
    addAnimation(moveRight);
    activeAnim = moveRight;
    faceRight = true;
    ResourceManager.getSound(RoboGame.SOUND_FOOTSTEPS_RSC).play();
  }

  public void stop() {
    velocity = new Vector(0f, 0f);
    removeAnimation(activeAnim);
    if(faceRight) {
      activeAnim = idleRight;
      addAnimation(idleRight);
    }
    else {
      activeAnim = idleLeft;
      addAnimation(idleLeft);
    }
  }

  public void death() {
    removeAnimation(activeAnim);
    if(faceRight) {
      activeAnim = defeatRight;
      addAnimation(defeatRight);
      defeatRight.restart();
    }
    else {
      activeAnim = defeatLeft;
      addAnimation(defeatLeft);
      defeatLeft.restart();
    }
    ResourceManager.getSound(RoboGame.SOUND_PLAYERDEATH_RSC).play();
  }

  public void update(final int delta) {
    translate(velocity.scale(delta));
  }

  public Vector getVelocity() { return velocity;}

  public int getAmmo() { return ammo;}

  public int getMaxAmmo() { return maxammo;}

  public int getHealth() { return health;}

  public int getMaxHealth() { return maxhealth;}

  public boolean isFaceRight() { return faceRight;}

  public void setMaxAmmo(int newMax) { maxammo = newMax;}

  public void setMaxHealth(int newMax) { maxhealth = newMax;}

  public void modAmmo(int change) { ammo += change;}

  public boolean modHealth(int change) {
    health += change;
    gotHit = true;
    if(health <= 0) {
      health = 0;
      return true;
    }
    return false;
  }

  public boolean gotHit() {
    boolean status = gotHit;
    gotHit = false;
    return status;
  }

  public void faceRight() {
    faceRight = true;
    removeAnimation(activeAnim);
    addAnimation(idleRight);
    activeAnim = idleRight;
  }

  public void faceLeft() {
    faceRight = false;
    removeAnimation(activeAnim);
    addAnimation(idleLeft);
    activeAnim = idleLeft;
  }
  public void reload() {
    ammo = maxammo;
    removeAnimation(activeAnim);
    if(faceRight) {
      activeAnim = reloadRight;
      addAnimation(reloadRight);
      reloadRight.restart();
    }
    else {
      activeAnim = reloadLeft;
      addAnimation(reloadLeft);
      reloadLeft.restart();
    }
    ResourceManager.getSound(RoboGame.SOUND_RELOAD_RSC).play();
  }

  public void shoot(int direction) {
    removeAnimation(activeAnim);
    switch(direction) {
      case 4: removeAnimation(activeAnim);
        addAnimation(shootLeft);
        shootLeft.restart();
        activeAnim = shootLeft;
        break;
      case 6: removeAnimation(activeAnim);
        addAnimation(shootRight);
        shootRight.restart();
        activeAnim = shootRight;
        break;
      default: removeAnimation(activeAnim);
        if(!faceRight) {
          addAnimation(shootLeft);
          shootLeft.restart();
          activeAnim = shootLeft;
        }
        else {
          addAnimation(shootRight);
          shootRight.restart();
          activeAnim = shootRight;
        }
        break;
    }
    ResourceManager.getSound(RoboGame.SOUND_LASER1_RSC).play();
  }
}
