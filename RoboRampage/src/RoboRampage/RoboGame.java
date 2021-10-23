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
  public static final int HOWTOPLAYSTATE = 2;
  public static final int LEVELSELECTSTATE = 3;
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
  public static final String UTIL_GUNBEAM_RSC = "RoboRampage/Assets/gunBeam.png";
  public static final String UTIL_GUNLAUNCHER_RSC = "RoboRampage/Assets/gunLauncher.png";
  public static final String UTIL_PICKUPARMOR_RSC = "RoboRampage/Assets/pickupArmor.png";
  public static final String UTIL_PICKUPAMMO_RSC = "RoboRampage/Assets/pickupAmmo.png";

  public static final String MENU_SPLASH_RSC = "RoboRampage/Assets/menuSplash.png";
  public static final String MENU_ARROW_RSC = "RoboRampage/Assets/menuArrow.png";
  public static final String MENU_HOWTOPLAY_RSC = "RoboRampage/Assets/menuHowToPlay.png";
  public static final String MENU_START_RSC = "RoboRampage/Assets/menuStart.png";
  public static final String MENU_LEVELSELECT_RSC = "RoboRampage/Assets/menuLevelSelect.png";
  public static final String MENU_BACK_RSC = "RoboRampage/Assets/menuBack.png";
  public static final String MENU_HOWTOPLAYSPLASH_RSC = "RoboRampage/Assets/howToPlaySplash.png";
  public static final String MENU_LEVELSELECTSPLASH_RSC = "RoboRampage/Assets/levelSelectSplash.png";
  public static final String MENU_GAMEOVER_RSC = "RoboRampage/Assets/menuGameover.png";

  public static final String ENEMY_MELEEIMG_RSC = "RoboRampage/Assets/meleeEnemy.png";
  public static final String ENEMY_MELEEIDLERIGHT_RSC = "RoboRampage/Assets/enemyMeleeIdleRight.png";
  public static final String ENEMY_MELEEIDLELEFT_RSC = "RoboRampage/Assets/enemyMeleeIdleLeft.png";
  public static final String ENEMY_MELEEMOVELEFT_RSC = "RoboRampage/Assets/enemyMeleeMoveLeft.png";
  public static final String ENEMY_MELEEMOVERIGHT_RSC = "RoboRampage/Assets/enemyMeleeMoveRight.png";
  public static final String ENEMY_MELEEATTACKLEFT_RSC = "RoboRampage/Assets/enemyMeleeAttackLeft.png";
  public static final String ENEMY_MELEEATTACKRIGHT_RSC = "RoboRampage/Assets/enemyMeleeAttackRight.png";
  public static final String ENEMY_MELEEDEFEATLEFT_RSC = "RoboRampage/Assets/enemyMeleeDefeatLeft.png";
  public static final String ENEMY_MELEEDEFEATRIGHT_RSC = "RoboRampage/Assets/enemyMeleeDefeatRight.png";

  public static final String ENEMY_RANGEDIDLERIGHT_RSC = "RoboRampage/Assets/enemyRangedIdleRight.png";
  public static final String ENEMY_RANGEDIDLELEFT_RSC = "RoboRampage/Assets/enemyRangedIdleLeft.png";
  public static final String ENEMY_RANGEDDEFEATRIGHT_RSC = "RoboRampage/Assets/enemyRangedDefeatRight.png";
  public static final String ENEMY_RANGEDDEFEATLEFT_RSC = "RoboRampage/Assets/enemyRangedDefeatLeft.png";
  public static final String ENEMY_RANGEDMOVERIGHT_RSC = "RoboRampage/Assets/enemyRangedMoveRight.png";
  public static final String ENEMY_RANGEDMOVELEFT_RSC = "RoboRampage/Assets/enemyRangedMoveLeft.png";
  public static final String ENEMY_RANGEDSHOOTRIGHT_RSC = "RoboRampage/Assets/enemyRangedShootRight.png";
  public static final String ENEMY_RANGEDSHOOTLEFT_RSC = "RoboRampage/Assets/enemyRangedShootLeft.png";
  public static final String ENEMY_PROJECTILE_RSC = "RoboRampage/Assets/enemyProjectile.png";

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

  // Sounds
  public static final String SOUND_EXPLOSION_RSC = "RoboRampage/Assets/Sounds/explosion.wav";
  public static final String SOUND_FOOTSTEPS_RSC = "RoboRampage/Assets/Sounds/footstep.wav";
  public static final String SOUND_LASER1_RSC = "RoboRampage/Assets/Sounds/laser.wav";
  public static final String SOUND_LASER2_RSC = "RoboRampage/Assets/Sounds/laser2.wav";
  public static final String SOUND_LASER3_RSC = "RoboRampage/Assets/Sounds/laser3.wav";
  public static final String SOUND_MELEEDEFEAT_RSC = "RoboRampage/Assets/Sounds/meleeDefeat.wav";
  public static final String SOUND_MELEEMOVE_RSC = "RoboRampage/Assets/Sounds/meleeMove.wav";
  public static final String SOUND_MENUSELECT_RSC = "RoboRampage/Assets/Sounds/menuSound1.wav";
  public static final String SOUND_MENUACTIVATE_RSC = "RoboRampage/Assets/Sounds/menuSound2.wav";
  public static final String SOUND_AIM_RSC = "RoboRampage/Assets/Sounds/menuSound3.wav";
  public static final String SOUND_PLAYERDEATH_RSC = "RoboRampage/Assets/Sounds/playerDeath.wav";
  public static final String SOUND_PLAYERHITMELEE_RSC = "RoboRampage/Assets/Sounds/playerHitMelee.wav";
  public static final String SOUND_POWERUP_RSC = "RoboRampage/Assets/Sounds/powerup.wav";
  public static final String SOUND_RANGEDDEFEAT_RSC = "RoboRampage/Assets/Sounds/rangedDefeat.wav";
  public static final String SOUND_RANGEDMOVE_RSC = "RoboRampage/Assets/Sounds/rangedMove.wav";
  public static final String SOUND_RELOAD_RSC = "RoboRampage/Assets/Sounds/reload.wav";
  public static final String SOUND_MELEEHIT_RSC = "RoboRampage/Assets/Sounds/robotHit.wav";
  public static final String SOUND_RANGEDHIT_RSC = "RoboRampage/Assets/Sounds/robotNoise.wav";

  // Music
  public static final String MUSIC_MENU_RSC = "RoboRampage/Assets/Sounds/menuMusic.wav";

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
    addState(new HowState());
    addState(new LevelState());
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
      // Melee
    ResourceManager.loadImage(ENEMY_MELEEIMG_RSC);
    ResourceManager.loadImage(ENEMY_MELEEIDLERIGHT_RSC);
    ResourceManager.loadImage(ENEMY_MELEEIDLELEFT_RSC);
    ResourceManager.loadImage(ENEMY_MELEEMOVELEFT_RSC);
    ResourceManager.loadImage(ENEMY_MELEEMOVERIGHT_RSC);
    ResourceManager.loadImage(ENEMY_MELEEDEFEATLEFT_RSC);
    ResourceManager.loadImage(ENEMY_MELEEDEFEATRIGHT_RSC);
    ResourceManager.loadImage(ENEMY_MELEEATTACKLEFT_RSC);
    ResourceManager.loadImage(ENEMY_MELEEATTACKRIGHT_RSC);
      // Ranged
    ResourceManager.loadImage(ENEMY_RANGEDIDLERIGHT_RSC);
    ResourceManager.loadImage(ENEMY_RANGEDIDLELEFT_RSC);
    ResourceManager.loadImage(ENEMY_RANGEDDEFEATRIGHT_RSC);
    ResourceManager.loadImage(ENEMY_RANGEDDEFEATLEFT_RSC);
    ResourceManager.loadImage(ENEMY_RANGEDMOVERIGHT_RSC);
    ResourceManager.loadImage(ENEMY_RANGEDMOVELEFT_RSC);
    ResourceManager.loadImage(ENEMY_RANGEDSHOOTRIGHT_RSC);
    ResourceManager.loadImage(ENEMY_RANGEDSHOOTLEFT_RSC);
    ResourceManager.loadImage(ENEMY_PROJECTILE_RSC);
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
    ResourceManager.loadImage(UTIL_GUNBEAM_RSC);
    ResourceManager.loadImage(UTIL_GUNLAUNCHER_RSC);
    ResourceManager.loadImage(UTIL_PICKUPARMOR_RSC);
    ResourceManager.loadImage(UTIL_PICKUPAMMO_RSC);
    // Menu Stuff
    ResourceManager.loadImage(MENU_SPLASH_RSC);
    ResourceManager.loadImage(MENU_ARROW_RSC);
    ResourceManager.loadImage(MENU_HOWTOPLAY_RSC);
    ResourceManager.loadImage(MENU_START_RSC);
    ResourceManager.loadImage(MENU_LEVELSELECT_RSC);
    ResourceManager.loadImage(MENU_LEVELSELECTSPLASH_RSC);
    ResourceManager.loadImage(MENU_BACK_RSC);
    ResourceManager.loadImage(MENU_HOWTOPLAYSPLASH_RSC);
    ResourceManager.loadImage(MENU_GAMEOVER_RSC);
    // Music and SFX
    ResourceManager.loadSound(SOUND_EXPLOSION_RSC);
    ResourceManager.loadSound(SOUND_FOOTSTEPS_RSC);
    ResourceManager.loadSound(SOUND_LASER1_RSC);
    ResourceManager.loadSound(SOUND_LASER2_RSC);
    ResourceManager.loadSound(SOUND_LASER3_RSC);
    ResourceManager.loadSound(SOUND_MELEEDEFEAT_RSC);
    ResourceManager.loadSound(SOUND_MELEEMOVE_RSC);
    ResourceManager.loadSound(SOUND_MENUSELECT_RSC);
    ResourceManager.loadSound(SOUND_MENUACTIVATE_RSC);
    ResourceManager.loadSound(SOUND_AIM_RSC);
    ResourceManager.loadSound(SOUND_PLAYERDEATH_RSC);
    ResourceManager.loadSound(SOUND_PLAYERHITMELEE_RSC);
    ResourceManager.loadSound(SOUND_POWERUP_RSC);
    ResourceManager.loadSound(SOUND_RANGEDDEFEAT_RSC);
    ResourceManager.loadSound(SOUND_RANGEDMOVE_RSC);
    ResourceManager.loadSound(SOUND_RELOAD_RSC);
    ResourceManager.loadSound(SOUND_MELEEHIT_RSC);
    ResourceManager.loadSound(SOUND_RANGEDHIT_RSC);
    ResourceManager.loadMusic(MUSIC_MENU_RSC);

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

  public static Vertex[][] getRangedDijkstras(Player player, Tile[][] tilemap) {
    Vertex path[][];
    // Build a copy of the tileMap
    Tile rangedTileMap[][] = new Tile[10][10];
    for(int i = 0; i < 10; i++) {
      for(int j = 0; j < 10; j++) {
        rangedTileMap[i][j] = tilemap[i][j].getCopy();
      }
    }
    Coordinate playerLoc = player.getLocation();
    // Set tiles with line of sight on player to a lower ranged cost
    int x = playerLoc.x;
    int y = playerLoc.y;
    // Update left tiles
    while(rangedTileMap[x-1][y].getID() != 1) {
      rangedTileMap[x-1][y].setCost(1);
      x--;
    }
    // Update right tiles
    x = playerLoc.x;
    while(rangedTileMap[x+1][y].getID() != 1) {
      rangedTileMap[x+1][y].setCost(1);
      x++;
    }
    // Update above tiles
    x = playerLoc.x;
    while(rangedTileMap[x][y-1].getID() != 1) {
      rangedTileMap[x][y-1].setCost(1);
      y--;
    }
    // Update below tiles
    y = playerLoc.y;
    while(rangedTileMap[x][y+1].getID() != 1) {
      rangedTileMap[x][y+1].setCost(1);
      y++;
    }
    // Use dijkstras on the new tile map.
    path = getDijkstras(playerLoc.x, playerLoc.y, rangedTileMap);
    //

    return path;
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
