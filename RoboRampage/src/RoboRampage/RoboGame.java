package RoboRampage;

import jig.Entity;
import jig.ResourceManager;

import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;

public class RoboGame extends StateBasedGame {
  // States
  public static final int STARTUPSTATE = 0;
  public static final int TESTSTATE = 1;
  // Resources
  public static final String TILE_OPENGRIDIMG_RSC = "RoboRampage/Assets/gridOpen.png";
  public static final String TILE_CLOSEDGRIDIMG_RSC = "RoboRampage/Assets/gridClosed.png";
  public static final String UTIL_CROSSHAIRIMG_RSC = "RoboRampage/Assets/crosshair.png";

  public static final String ENEMY_MELEEIMG_RSC = "RoboRampage/Assets/meleeEnemy.png";

  public static final String PLAYER_PLAYERIMG_RSC = "RoboRampage/Assets/player.png";
  // Parameters
  public final int ScreenWidth;
  public final int ScreenHeight;
  /**
   * Create a new state based game
   *
   * @param title The name of the game
   */
  public RoboGame(String title, int width, int height) {
    super(title);
    ScreenHeight = height;
    ScreenWidth = width;

    Entity.setCoarseGrainedCollisionBoundary(Entity.AABB);
  }

  Player player;
  @Override
  public void initStatesList(GameContainer container) throws SlickException {
    // Load states
    addState(new StartState());
    addState(new TestState());
    // Load resources
    ResourceManager.loadImage(TILE_OPENGRIDIMG_RSC);
    ResourceManager.loadImage(TILE_CLOSEDGRIDIMG_RSC);
    ResourceManager.loadImage(PLAYER_PLAYERIMG_RSC);
    ResourceManager.loadImage(ENEMY_MELEEIMG_RSC);
    ResourceManager.loadImage(UTIL_CROSSHAIRIMG_RSC);

    player = new Player(75,75,1,1);
  }

  public static void main(String[] args) {
    AppGameContainer app;
    try {
      app = new AppGameContainer(new RoboGame("RoboRampage", 1000,1000));
      app.setDisplayMode(750,750, false);
      app.setVSync(true);
      app.setTargetFrameRate(60);
      app.start();
    } catch(SlickException e) {
      e.printStackTrace();
    }
  }

  public static Tile[][] getTileMap(String map) {
    Tile tileMap[][] = new Tile[10][10];
    char tempMap[] = map.toCharArray();
    int x = 0, y = 0, i = 0;
    for(char current : tempMap) {
      if(x == 10) {
        x = 0;
        y++;
      }
      tileMap[x][y] = new Tile(Character.getNumericValue(tempMap[i]));
      i++;
      x++;
    }
    return tileMap;
  }

  public static Vertex[][] getDijkstras(int sourcex, int sourcey, Tile[][] tileMap) {
    Vertex path[][] = new Vertex[10][10];
    boolean seen[][] = new boolean[10][10];
    // Intialize the path and seen arrays
    for(int x = 0; x < 10; x++) {
      for(int y = 0; y < 10; y++) {
        path[x][y] = new Vertex(tileMap[x][y].getCost());
        seen[x][y] = false;
      }
    }
    // Set the source distance to 0
    path[sourcex][sourcey].setDistance(0);

    // Keep going until all nodes are seen
    while(hasUnseenNodes(seen)) {
      Coordinate current = shortestDistance(path, seen);
      int x = current.x;
      int y = current.y;
      seen[x][y] = true;
      int compare;
      int currentDist = path[x][y].getDistance();

      // Tile above
      if(y > 0) {
        compare = currentDist + path[x][y-1].getCost();
        if(path[x][y-1].getDistance() > compare) {
          path[x][y-1].setDistance(compare);
          path[x][y-1].setDirection(2);
        }
      }
      // Tile below
      if(y < 9) {
        compare = currentDist + path[x][y+1].getCost();
        if(path[x][y+1].getDistance() > compare) {
          path[x][y+1].setDistance(compare);
          path[x][y+1].setDirection(8);
        }
      }
      // Tile left
      if(x > 0) {
        compare = currentDist + path[x-1][y].getCost();
        if(path[x-1][y].getDistance() > compare) {
          path[x-1][y].setDistance(compare);
          path[x-1][y].setDirection(6);
        }
      }
      // Tile right
      if(x < 9) {
        compare = currentDist + path[x+1][y].getCost();
        if(path[x+1][y].getDistance() > compare) {
          path[x+1][y].setDistance(compare);
          path[x+1][y].setDirection(4);
        }
      }
    }
    return path;
  }

  private static boolean hasUnseenNodes(boolean seen[][]) {
    for (int x = 0; x < 10; x++) {
      for (int y = 0; y < 10; y++) {
        if (!seen[x][y]) {
          // System.out.println("found a node x:" + x + " y:" + y);
          return true;
        }
      }
    }
    return false;
  }

  private static Coordinate shortestDistance (Vertex graph[][], boolean seen[][]) {
    Coordinate shortest = new Coordinate(0,0);
    int distance = 100000000;
    for(int x = 0; x < 10; x++) {
      for(int y = 0; y < 10; y++) {
        int newDistance = graph[x][y].getDistance();
        if(newDistance < distance && !seen[x][y]) {
          distance = newDistance;
          shortest.x = x;
          shortest.y = y;
        }
      }
    }
    return shortest;
  }
}
