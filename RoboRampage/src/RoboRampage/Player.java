package RoboRampage;

import jig.Entity;
import jig.ResourceManager;
import jig.Vector;

class Player extends Entity {
  private Vector velocity;
  private float speed;
  private Coordinate location;
  private int ammo, maxammo;
  private int health, maxhealth;
  public Player(final float x, final float y, int xcoord, int ycoord) {
    super(x + 37,y + 37);
    addImageWithBoundingBox(ResourceManager.getImage(RoboGame.PLAYER_PLAYERIMG_RSC));
    location = new Coordinate(xcoord, ycoord);
    velocity = new Vector(0f,0f);
    speed = 1f;
    maxammo = 5;
    maxhealth = 10;
    health = maxhealth;
    ammo = maxammo;
  }

  public Coordinate getLocation() { return location;}

  public void moveUp() {
    velocity = new Vector(0f, -speed);
    location.y = location.y - 1;
  }

  public void moveLeft() {
   velocity = new Vector(-speed, 0f);
   location.x = location.x - 1;
  }

  public void moveDown() {
    velocity = new Vector(0f, speed);
    location.y = location.y + 1;
  }

  public void moveRight() {
    velocity = new Vector(speed, 0);
    location.x = location.x + 1;
  }

  public void stop() {
    velocity = new Vector(0f, 0f);
  }

  public void update(final int delta) {
    translate(velocity.scale(delta));
  }

  public Vector getVelocity() { return velocity;}

  public int getAmmo() { return ammo;}

  public int getMaxAmmo() { return maxammo;}

  public int getHealth() { return health;}

  public int getMaxHealth() { return maxhealth;}

  public void setMaxAmmo(int newMax) { maxammo = newMax;}

  public void setMaxHealth(int newMax) { maxhealth = newMax;}

  public void modAmmo(int change) { ammo += change;}

  public boolean modHealth(int change) {
    health += change;
    if(health <= 0) {
      health = 0;
      return true;
    }
    return false;
  }

  public void reload() { ammo = maxammo;}
}
