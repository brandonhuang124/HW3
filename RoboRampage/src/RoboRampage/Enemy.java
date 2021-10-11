package RoboRampage;

import jig.Entity;
import jig.ResourceManager;
import jig.Vector;

public class Enemy extends Entity {
  private Vector velocity;
  private Coordinate location;
  private float speed;
  private int id; // 1 for melee, 2 for ranged

  public Enemy(final float x, final float y, int xcoord, int ycoord, int newid) {
    super( x + 37, y + 37);
    location = new Coordinate(xcoord, ycoord);
    speed = 1f;
    velocity = new Vector(0f, 0f);
    id = newid;
    addImageWithBoundingBox(ResourceManager.getImage(RoboGame.ENEMY_MELEEIMG_RSC));
  }

  public Coordinate getLocation() { return location;}

  public void makeMove(Vertex[][] pathMap, Coordinate playerLoc) {
    // Melee Enemy behavior
    if(id == 1) {
      // Check if the player is next to them
      int xdiff = playerLoc.x - location.x;
      int ydiff = playerLoc.y - location.y;
      // Square above
      if(xdiff == 0 && ydiff == 1) attack(8);
      // Square below
      else if(xdiff == 0 && ydiff == -1) attack(2);
      // Square left
      else if(xdiff == 1 && ydiff == 0) attack(4);
      // Square right
      else if(xdiff == -1 && ydiff == 0) attack(6);
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

  public void attack(int direction) {
    System.out.println("Attacked: Hyah!" + direction);
  }
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

  public int getID() { return id;}

  public Vector getVelocity() { return velocity;}
}
