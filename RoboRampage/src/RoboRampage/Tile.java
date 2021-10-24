package RoboRampage;

import java.util.Random;

/***
 * Tile class for building a tilemap for Robo Game. Each tile type has an id and a associated cost.
 * Current ids:
 *  0: Floor tile
 *  1: Wall tile
 */

public class Tile {
  private int id, subid, cost;

  /***
   * Constructor,
   * @param newID
   *  ID of the tile to be built
   */
  public Tile(int newID) {
    id = newID;
    /*** FLoor tile ***/
    if (id == 0) {
      cost = 10;
      // Each subid represents a different floor tile art design. Use a weighted random assignment to assign a subid.
      if (new Random().nextInt(10) == 0) {
        subid = 1;
      }
      else if (new Random().nextInt(20) == 0) {
        subid = 2;
      }
      else if (new Random().nextInt(75) == 0) {
        subid = 3;
      }
      else {
        subid = 0;
      }
    }
    /*** Wall tile ***/
    if(id == 1) {
      cost = 1000;
      subid = 0;
    }
  }

  public int getID() { return id;}

  public int getSubID() { return subid;}

  public int getCost() { return cost;}

  public void setCost(int newcost) { cost = newcost;}

  /***
   * Method for building copies of a tilemap.
   * @return
   *  Copy of a this tile that is a new object.
   */
  Tile getCopy() {
    Tile tile = new Tile(this.id);
    return tile;
  }
}
