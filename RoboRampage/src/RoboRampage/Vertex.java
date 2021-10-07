package RoboRampage;

public class Vertex {
  private int cost, distance, direction;

  public Vertex(int newCost) {
    cost = newCost;
    distance = 10000;
    direction = 0;
  }

  public void setCost(int newCost) {cost = newCost;}

  public void setDirection(int newDirection) {direction = newDirection;}

  public void setDistance(int newDistance) {distance = newDistance;}

  public int getCost() {return cost;}

  public int getDistance() {return distance;}

  public int getDirection() {return direction;}
}
