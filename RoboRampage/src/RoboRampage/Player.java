package RoboRampage;

import jig.Entity;
import jig.ResourceManager;
import jig.Vector;

class Player extends Entity {
  private Vector velocity;
  private float speed;
  private Coordinate location;

  public Player(final float x, final float y, int xcoord, int ycoord) {
    super(x + 37,y + 37);
    addImageWithBoundingBox(ResourceManager.getImage(RoboGame.PLAYER_PLAYERIMG_RSC));
    location = new Coordinate(xcoord, ycoord);
    velocity = new Vector(0f,0f);
    speed = 1f;
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

  public void offset(final int delta) {
    translate(velocity.scale(delta));
  }

  public void update(final int delta) {
    translate(velocity.scale(delta));
  }

  public Vector getVelocity() { return velocity;}
}
