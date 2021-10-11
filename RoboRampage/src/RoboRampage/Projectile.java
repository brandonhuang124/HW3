package RoboRampage;

import jig.Entity;
import jig.ResourceManager;
import jig.Vector;

import java.util.LinkedList;

public class Projectile extends Entity {
  private float speed;
  private Vector velocity;

  public Projectile (Coordinate loc, int direction) {
    float x = (loc.x * 75f) + 37;
    float y = (loc.y * 75f) + 37;
    speed = 1f;
    this.setX(x);
    this.setY(y);
    switch(direction) {
      case 2: velocity = new Vector(0f, speed);
              addImageWithBoundingBox(ResourceManager.getImage(RoboGame.PLAYER_PROJECTILEDOWN_RSC));
              break;
      case 4: velocity = new Vector(-speed, 0f);
              addImageWithBoundingBox(ResourceManager.getImage(RoboGame.PLAYER_PROJECTILELEFT_RSC));
              break;
      case 6: velocity = new Vector(speed, 0f);
              addImageWithBoundingBox(ResourceManager.getImage(RoboGame.PLAYER_PROJECTILERIGHT_RSC));
              break;
      default: velocity = new Vector(0f, -speed);
              addImageWithBoundingBox(ResourceManager.getImage(RoboGame.PLAYER_PROJECTILEUP_RSC));
               break;
    }
  }

  public Coordinate getCoord() {
    int x = (int) this.getX() / 75;
    int y = (int) this.getY() / 75;
    return new Coordinate(x,y);
  }

  // Returns (-1,-1) if no collision happened.
  public Coordinate enemyCollision (LinkedList<Enemy> enemyList) {
    Coordinate collisionLoc = new Coordinate(-1,-1);
    Coordinate thisCoord = getCoord();
    for(Enemy enemy : enemyList) {
      if(thisCoord.x == enemy.getLocation().x && thisCoord.y == enemy.getLocation().y) {
        collisionLoc.x = thisCoord.x;
        collisionLoc.y = thisCoord.y;
        break;
      }
    }
    return collisionLoc;
  }

  public boolean wallCollision (Tile[][] tilemap) {
    Coordinate location = getCoord();
    if(tilemap[location.x][location.y].getID() == 1) return true;
    return false;
  }

  public void update(final int delta) {
    translate(velocity.scale(delta));
  }
}
