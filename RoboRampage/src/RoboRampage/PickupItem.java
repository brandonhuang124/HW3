package RoboRampage;

/***
 * This class tracks info for pickup items on the map. Stores the id and location of the pickup for use in the
 * pickup List when playing the game. Current pickup id values:
 *
 * 1: Armor pickup, restores health to full.
 */
public class PickupItem {
  Coordinate location;
  private boolean removeMe;
  private int id;

  /***
   * Constructor
   * @param x
   *  x coordinate of pickup in tilemap coordinates
   * @param y
   *  y coordinate of pickup in tilemap coordinates
   * @param newid
   *  id of the pickup, options:
   *    1: Armor, restores health to full.
   */
  public PickupItem(int x, int y, int newid) {
    location = new Coordinate(x,y);
    id = newid;
    removeMe = false;
  }

  public boolean playerCollision(Player player) {
    Coordinate playerLoc = player.getLocation();
    if(playerLoc.x == location.x && playerLoc.y == location.y) {
      removeMe = true;
      return true;
    }
    return false;
  }

  public int getId() { return id;}

  public Coordinate getLocation() { return location; }

  public boolean getRemoveMe() { return removeMe; }
}
