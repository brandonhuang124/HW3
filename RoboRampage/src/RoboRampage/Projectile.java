package RoboRampage;

import jig.Entity;
import jig.ResourceManager;
import jig.Vector;
import org.newdawn.slick.Animation;

import java.util.LinkedList;

public class Projectile extends Entity {
  private float speed;
  private Vector velocity;
  private Animation image;
  int id;

  public Projectile (Coordinate loc, int direction, int type) {
    float x = (loc.x * 75f) + 37;
    float y = (loc.y * 75f) + 37;
    speed = .75f;
    this.setX(x);
    this.setY(y);
    id = type;
    // Player Projectile
    if(id == 1) {
      image = new Animation(ResourceManager.getSpriteSheet(
          RoboGame.PLAYER_PROJECTILEDEFAULT_RSC, 25, 25), 0, 0, 3, 0,
          true, 75, true);
    }
    // Enemy Projectile
    else if(id ==2) {
      image = new Animation(ResourceManager.getSpriteSheet(
          RoboGame.ENEMY_PROJECTILE_RSC, 25, 25), 0, 0, 3, 0,
          true, 75, true);
    }
    addAnimation(image);
    image.setLooping(true);
    switch(direction) {
      case 2: velocity = new Vector(0f, speed);
              break;
      case 4: velocity = new Vector(-speed, 0f);
              break;
      case 6: velocity = new Vector(speed, 0f);
              break;
      default: velocity = new Vector(0f, -speed);
               break;
    }
  }

  public Coordinate getCoord() {
    int x = (int) this.getX() / 75;
    int y = (int) this.getY() / 75;
    return new Coordinate(x,y);
  }

  // Returns (-1,-1) if no collision happened.
  public Enemy enemyCollision (LinkedList<Enemy> enemyList) {
    Enemy enemyHit = null;
    Coordinate thisCoord = getCoord();
    for(Enemy enemy : enemyList) {
      if(thisCoord.x == enemy.getLocation().x && thisCoord.y == enemy.getLocation().y) {
        enemyHit = enemy;
        break;
      }
    }
    return enemyHit;
  }

  public boolean wallCollision (Tile[][] tilemap) {
    Coordinate location = getCoord();
    if(tilemap[location.x][location.y].getID() == 1) return true;
    return false;
  }

  public boolean playerCollision(Player player) {
    Coordinate location = getCoord();
    Coordinate playerLoc = player.getLocation();
    if(location.x == playerLoc.x && location.y == playerLoc.y) {
      player.modHealth(-1);
      return true;
    }
    return false;
  }

  public void update(final int delta) {
    translate(velocity.scale(delta));
  }

  public int getID() { return id;}
}
