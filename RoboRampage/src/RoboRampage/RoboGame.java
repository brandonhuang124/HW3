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
  public static final String TILE_FLOORIMG_RSC = "RoboRampage/Assets/floor.png";
  public static final String TILE_FLOORIMG2_RSC = "RoboRampage/Assets/floor2.png";
  public static final String TILE_FLOORIMG3_RSC = "RoboRampage/Assets/floor3.png";
  public static final String TILE_FLOORIMG4_RSC = "RoboRampage/Assets/floor4.png";
  public static final String TILE_WALLIMG_RSC = "RoboRampage/Assets/wall.png";
  public static final String UTIL_CROSSHAIRIMG_RSC = "RoboRampage/Assets/crosshair.png";
  public static final String UTIL_GBARCAPRIGHT_RSC = "RoboRampage/Assets/barGreenCapRight.png";
  public static final String UTIL_GBARCAPLEFT_RSC = "RoboRampage/Assets/barGreenCapLeft.png";
  public static final String UTIL_GBAR_RSC = "RoboRampage/Assets/barGreen.png";
  public static final String UTIL_RBARCAPRIGHT_RSC = "RoboRampage/Assets/barRedCapRight.png";
  public static final String UTIL_RBARCAPLEFT_RSC = "RoboRampage/Assets/barRedCapLeft.png";
  public static final String UTIL_RBAR_RSC = "RoboRampage/Assets/barRed.png";
  public static final String UTIL_BULLET_RSC = "RoboRampage/Assets/bullet.png";
  public static final String UTIL_BULLETGONE_RSC = "RoboRampage/Assets/bulletGone.png";
  public static final String UTIL_HUDIMG_RSC = "RoboRampage/Assets/hud.png";
  public static final String UTIL_GUNDEFAULT_RSC = "RoboRampage/Assets/gunDefault.png";

  public static final String ENEMY_MELEEIMG_RSC = "RoboRampage/Assets/meleeEnemy.png";
  public static final String ENEMY_MELEEIDLERIGHT_RSC = "RoboRampage/Assets/enemyMeleeIdleRight.png";
  public static final String ENEMY_MELEEIDLELEFT_RSC = "RoboRampage/Assets/enemyMeleeIdleLeft.png";
  public static final String ENEMY_MELEEMOVELEFT_RSC = "RoboRampage/Assets/enemyMeleeMoveLeft.png";
  public static final String ENEMY_MELEEMOVERIGHT_RSC = "RoboRampage/Assets/enemyMeleeMoveRight.png";
  public static final String ENEMY_MELEEATTACKLEFT_RSC = "RoboRampage/Assets/enemyMeleeAttackLeft.png";
  public static final String ENEMY_MELEEATTACKRIGHT_RSC = "RoboRampage/Assets/enemyMeleeAttackRight.png";
  public static final String ENEMY_MELEEDEFEATLEFT_RSC = "RoboRampage/Assets/enemyMeleeDefeatLeft.png";
  public static final String ENEMY_MELEEDEFEATRIGHT_RSC = "RoboRampage/Assets/enemyMeleeDefeatRight.png";

  public static final String PLAYER_PLAYERIMG_RSC = "RoboRampage/Assets/player.png";
  public static final String PLAYER_PLAYERIDLELEFT_RSC = "RoboRampage/Assets/playerIdleLeft.png";
  public static final String PLAYER_PLAYERIDLERIGHT_RSC = "RoboRampage/Assets/playerIdleRight.png";
  public static final String PLAYER_PLAYERMOVELEFT_RSC = "RoboRampage/Assets/playerMoveLeft.png";
  public static final String PLAYER_PLAYERMOVERIGHT_RSC = "RoboRampage/Assets/playerMoveRight.png";
  public static final String PLAYER_PLAYERDEATHLEFT_RSC = "RoboRampage/Assets/playerDeathLeft.png";
  public static final String PLAYER_PLAYERDEATHRIGHT_RSC = "RoboRampage/Assets/playerDeathRight.png";
  public static final String PLAYER_PLAYERRELOADLEFT_RSC = "RoboRampage/Assets/playerReloadLeft.png";
  public static final String PLAYER_PLAYERRELOADRIGHT_RSC = "RoboRampage/Assets/playerReloadRight.png";
  public static final String PLAYER_PLAYERSHOOTLEFT_RSC = "RoboRampage/Assets/playerShootLeft.png";
  public static final String PLAYER_PLAYERSHOOTRIGHT_RSC = "RoboRampage/Assets/playerShootRight.png";
  public static final String PLAYER_PROJECTILEDEFAULT_RSC = "RoboRampage/Assets/playerProjectile.png";
  public static final String PLAYER_PROJECTILEDOWN_RSC = "RoboRampage/Assets/projectile_down.png";
  public static final String PLAYER_PROJECTILELEFT_RSC = "RoboRampage/Assets/projectile_left.png";
  public static final String PLAYER_PROJECTILERIGHT_RSC = "RoboRampage/Assets/projectile_right.png";
  public static final String PLAYER_PROJECTILEUP_RSC = "RoboRampage/Assets/projectile_up.png";
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
    // Map Stuff
    ResourceManager.loadImage(TILE_FLOORIMG_RSC);
    ResourceManager.loadImage(TILE_WALLIMG_RSC);
    ResourceManager.loadImage(TILE_FLOORIMG2_RSC);
    ResourceManager.loadImage(TILE_FLOORIMG3_RSC);
    ResourceManager.loadImage(TILE_FLOORIMG4_RSC);
    // Player Stuff
    ResourceManager.loadImage(PLAYER_PLAYERIMG_RSC);
    ResourceManager.loadImage(PLAYER_PLAYERDEATHLEFT_RSC);
    ResourceManager.loadImage(PLAYER_PLAYERDEATHRIGHT_RSC);
    ResourceManager.loadImage(PLAYER_PLAYERIDLELEFT_RSC);
    ResourceManager.loadImage(PLAYER_PLAYERIDLERIGHT_RSC);
    ResourceManager.loadImage(PLAYER_PLAYERSHOOTLEFT_RSC);
    ResourceManager.loadImage(PLAYER_PLAYERSHOOTRIGHT_RSC);
    ResourceManager.loadImage(PLAYER_PLAYERRELOADLEFT_RSC);
    ResourceManager.loadImage(PLAYER_PLAYERRELOADRIGHT_RSC);
    ResourceManager.loadImage(PLAYER_PLAYERMOVELEFT_RSC);
    ResourceManager.loadImage(PLAYER_PLAYERMOVERIGHT_RSC);
    ResourceManager.loadImage(PLAYER_PROJECTILEDEFAULT_RSC);
    ResourceManager.loadImage(PLAYER_PROJECTILEDOWN_RSC);
    ResourceManager.loadImage(PLAYER_PROJECTILELEFT_RSC);
    ResourceManager.loadImage(PLAYER_PROJECTILERIGHT_RSC);
    ResourceManager.loadImage(PLAYER_PROJECTILEUP_RSC);
    // Enemy Sheets
    ResourceManager.loadImage(ENEMY_MELEEIMG_RSC);
    ResourceManager.loadImage(ENEMY_MELEEIDLERIGHT_RSC);
    ResourceManager.loadImage(ENEMY_MELEEIDLELEFT_RSC);
    ResourceManager.loadImage(ENEMY_MELEEMOVELEFT_RSC);
    ResourceManager.loadImage(ENEMY_MELEEMOVERIGHT_RSC);
    ResourceManager.loadImage(ENEMY_MELEEDEFEATLEFT_RSC);
    ResourceManager.loadImage(ENEMY_MELEEDEFEATRIGHT_RSC);
    ResourceManager.loadImage(ENEMY_MELEEATTACKLEFT_RSC);
    ResourceManager.loadImage(ENEMY_MELEEATTACKRIGHT_RSC);
    // UI stuff
    ResourceManager.loadImage(UTIL_CROSSHAIRIMG_RSC);
    ResourceManager.loadImage(UTIL_BULLET_RSC);
    ResourceManager.loadImage(UTIL_BULLETGONE_RSC);
    ResourceManager.loadImage(UTIL_GBAR_RSC);
    ResourceManager.loadImage(UTIL_GBARCAPLEFT_RSC);
    ResourceManager.loadImage(UTIL_GBARCAPRIGHT_RSC);
    ResourceManager.loadImage(UTIL_RBAR_RSC);
    ResourceManager.loadImage(UTIL_RBARCAPLEFT_RSC);
    ResourceManager.loadImage(UTIL_RBARCAPRIGHT_RSC);
    ResourceManager.loadImage(UTIL_HUDIMG_RSC);
    ResourceManager.loadImage(UTIL_GUNDEFAULT_RSC);


    player = new Player(75,75,1,1);
  }

  public static void main(String[] args) {
    AppGameContainer app;
    try {
      app = new AppGameContainer(new RoboGame("RoboRampage", 1000,1000));
      app.setDisplayMode(750,850, false);
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
