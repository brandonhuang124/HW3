package RoboRampage;

import jig.Entity;
import jig.ResourceManager;
import jig.Vector;

/***
 * Crosshair entity which shows which direction the player is aiming in. Does not interact with the environment in any
 * way and stays in the tiles adjacent to the player.
 *
 * Methods:
 *  moveLeft()
 *  moveUp()
 *  moveDown()
 *  moveRight()
 */

public class Crosshair extends Entity {

  /**
   * Constructor,
   * @param x
   *  x coordinate to spawn in (absolute, not tile coordinates.)
   * @param y
   *  y coordinate to spawn in (absolute, not tile coordinates.)
   */
  public Crosshair (final float x, final float y) {
    super(x,y);
    addImage(ResourceManager.getImage(RoboGame.UTIL_CROSSHAIRIMG_RSC));
  }

  /**
   * The four methods below move the crosshair to the corresponding direction in their titles relative to the player.
   *
   * @param playerloc
   *  Location of the player the crosshair needs to be placed by
   */
  public void moveLeft(Coordinate playerloc) {
    this.setPosition(((playerloc.x - 1) * 75) + 37, (playerloc.y * 75) + 37);
  }

  public void moveUp(Coordinate playerloc) {
    this.setPosition((playerloc.x * 75) + 37, ((playerloc.y - 1) * 75) + 37);
  }

  public void moveDown(Coordinate playerloc) {
    this.setPosition((playerloc.x * 75) + 37, ((playerloc.y + 1) * 75) + 37);
  }

  public void moveRight(Coordinate playerloc) {
    this.setPosition(((playerloc.x + 1) * 75) + 37, (playerloc.y * 75) + 37);
  }
}
