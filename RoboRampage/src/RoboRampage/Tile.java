package RoboRampage;

public class Tile {
  private int id, cost;

  public Tile(int newID) {
    id = newID;
    if(id == 0) {
      cost = 1;
    }
    if(id == 1) {
      cost = 1000;
    }
  }

  public int getID() { return id;}

  public int getCost() { return cost;}
}
