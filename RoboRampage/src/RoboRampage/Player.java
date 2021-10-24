package RoboRampage;

import jig.Entity;
import jig.ResourceManager;
import jig.Vector;
import org.newdawn.slick.Animation;

/***
 * Enitity class for reprsenting the player character. Tracks player statistics such as health and ammo.
 *
 * Useful parameters:
 *  location: Coordinate in the tile map where the player is located.
 *  ammo/maxammo: Ammo tracking
 *  health/maxhealth: Health tracking
 *
 *  Methods:
 *    moveUp()
 *    moveDown()
 *    moveLeft()
 *    moveRight()
 *    faceLeft()
 *    faceRight()
 *    reload()
 *    stop()
 *    modHealth()
 *    death()
 *    gotHit()
 *    shoot()
 */

class Player extends Entity {
  private Vector velocity;
  private float speed;
  private Coordinate location;
  private int ammo, maxammo;
  private int health, maxhealth;
  private Animation moveLeft, moveRight, shootLeft, shootRight, reloadLeft, reloadRight, idleLeft, idleRight,
      defeatRight, defeatLeft, activeAnim, hitIndicator;
  private boolean faceRight, gotHit;

  /***
   * Constructor, prepares stats and animations for the player
   * @param x
   *  x coordinate to spawn the player in
   * @param y
   *  y coordinate to spawn the player in
   * @param xcoord
   * x coordinate in tilemap coordinates to spawn the player in
   * @param ycoord
   * y coordinate in tilemap coodinate to spawn the player in
   */
  public Player(final float x, final float y, int xcoord, int ycoord) {
    super(x + 37,y + 37);
    addImageWithBoundingBox(ResourceManager.getImage(RoboGame.PLAYER_PLAYERIMG_RSC));
    // Initilaize stats and params
    location = new Coordinate(xcoord, ycoord);
    velocity = new Vector(0f,0f);
    speed = 0.25f;
    maxammo = 5;
    maxhealth = 10;
    health = maxhealth;
    ammo = maxammo;
    // get the animations.
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
    hitIndicator = new Animation(ResourceManager.getSpriteSheet(
        RoboGame.PLAYER_HITINDICATOR_RSC, 75, 75), 0, 0, 1, 0,
        true, 300, true);
    // Set animation params and start with a right idle animation
    activeAnim = idleRight;
    addAnimation(idleRight);
    addAnimation(hitIndicator);
    faceRight = true;
    gotHit = false;
    hitIndicator.setCurrentFrame(1);
    hitIndicator.setLooping(false);
    shootLeft.setLooping(false);
    shootRight.setLooping(false);
    reloadRight.setLooping(false);
    reloadLeft.setLooping(false);
    defeatRight.setLooping(false);
    defeatLeft.setLooping(false);
  }

  public Coordinate getLocation() { return location;}

  /***
   * This method resets the players health, ammo, position, and animations to the default values.
   * @param xcoord
   *  x coordinate in tile map coordinates to spawn the player in
   * @param ycoord
   *  y coordinate in tile map coordinates to spawn the player in
   */
  public void resetPlayer(int xcoord, int ycoord) {
    health = maxhealth;
    ammo = maxammo;
    removeAnimation(activeAnim);
    activeAnim = idleRight;
    addAnimation(idleRight);
    faceRight = true;
    gotHit = false;
    setPosition((xcoord * 75) + 37, (ycoord * 75) + 37);
    addImageWithBoundingBox(ResourceManager.getImage(RoboGame.PLAYER_PLAYERIMG_RSC));
    location = new Coordinate(xcoord, ycoord);
    velocity = new Vector(0f,0f);
  }

  /***
   * Function to be called if the player moves a space upwards.
   */
  public void moveUp() {
    // Change positions
    velocity = new Vector(0f, -speed);
    location.y = location.y - 1;
    // Change animations
    removeAnimation(activeAnim);
    if(faceRight) {
      activeAnim = moveRight;
      addAnimation(moveRight);
    }
    else {
      activeAnim = moveLeft;
      addAnimation(moveLeft);
    }
    // Play sound
    ResourceManager.getSound(RoboGame.SOUND_FOOTSTEPS_RSC).play();
  }

  /***
   * Function to be called if the player moves left. Sets animations and plays the walk sound.
   */
  public void moveLeft() {
   velocity = new Vector(-speed, 0f);
   location.x = location.x - 1;
   removeAnimation(activeAnim);
   addAnimation(moveLeft);
   activeAnim = moveLeft;
   faceRight = false;
   ResourceManager.getSound(RoboGame.SOUND_FOOTSTEPS_RSC).play();
  }

  /***
   * Function to be called if the player moves down. Sets animations and plays the walk sound.
   */
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

  /***
   * Function to be called if the player moves right. Sets animations and plays the walk sound.
   */
  public void moveRight() {
    velocity = new Vector(speed, 0);
    location.x = location.x + 1;
    removeAnimation(activeAnim);
    addAnimation(moveRight);
    activeAnim = moveRight;
    faceRight = true;
    ResourceManager.getSound(RoboGame.SOUND_FOOTSTEPS_RSC).play();
  }

  /***
   * Function to be called to stop the players movement. Sets animatons
   */
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

  /***
   * Funtion to be called if the player dies. Only sets sounds and animations.
   */
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

  /***
   * Modifies the players health by the given amount. returns a boolean on the status if the player is dead or not.
   * @param change
   *  Amount to modify health by
   * @return
   *  true if the players health dropped to 0 or below
   *  false otherwise
   */
  public boolean modHealth(int change) {
    health += change;
    gotHit = true;
    hitIndicator.restart();
    if(health <= 0) {
      health = 0;
      return true;
    }
    return false;
  }

  /***
   * Function that simply restores all the players health back to full.
   */
  public void restoreHealth() {
    health = maxhealth;
  }

  /***
   * Function to be called to see if the player was hit previously, must be called after every enemy attack.
   * @return
   */
  public boolean gotHit() {
    boolean status = gotHit;
    gotHit = false;
    return status;
  }

  /***
   * This function refaces the player to the right without moving them.
   */
  public void faceRight() {
    faceRight = true;
    removeAnimation(activeAnim);
    addAnimation(idleRight);
    activeAnim = idleRight;
  }

  /***
   * This function faces the player left without moving them.
   */
  public void faceLeft() {
    faceRight = false;
    removeAnimation(activeAnim);
    addAnimation(idleLeft);
    activeAnim = idleLeft;
  }

  /***
   * This function causes the player to "reload" their weapon. Resets current ammo to the max. Sets animatoins and plays
   * sounds.
   */
  public void reload() {
    // Refresh ammo
    ammo = maxammo;
    // Set the animations
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
    // Play the sound
    ResourceManager.getSound(RoboGame.SOUND_RELOAD_RSC).play();
  }

  /***
   * Function that sets proper animations and sounds after the player shoots their weapon. Doesn't modify ammo
   *
   * @param direction
   *  Direction to fire in, represented as follows:
   *    2: Down, 4: Left, 6: Up, 8: Right
   */
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
