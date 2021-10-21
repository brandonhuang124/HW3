package RoboRampage;

import java.util.Random;

public class Tile {
  private int id, subid, cost;

  public Tile(int newID) {
    id = newID;
    if (id == 0) {
      cost = 1;
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
    if(id == 1) {
      cost = 1000;
      subid = 0;
    }
  }

  public int getID() { return id;}

  public int getSubID() { return subid;}

  public int getCost() { return cost;}
}
