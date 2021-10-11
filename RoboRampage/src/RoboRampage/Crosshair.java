package RoboRampage;

import jig.Entity;
import jig.ResourceManager;
import jig.Vector;

public class Crosshair extends Entity {

  public Crosshair (final float x, final float y) {
    super(x,y);
    addImage(ResourceManager.getImage(RoboGame.UTIL_CROSSHAIRIMG_RSC));
  }

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
