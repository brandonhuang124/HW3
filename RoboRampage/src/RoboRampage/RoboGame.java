package RoboRampage;

import jig.Entity;
import jig.ResourceManager;

import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;

/**
 * Robo Rampage, a turn based robot shooter.
 *
 * Description:
 * Robo Rampage is a turn based game where the play must defeat all the robots in the level while staying alive. The
 * player can move, shoot, reload, or wait on their turn. After their turn all the enemies on the board can move or
 * attack the player. The player has a limited amount of shots they can take before they must reload, defaulting to 5.
 * The player can pickup powerups to heal or empower their attacks to help complete the level. To beat the level, the
 * player must think a few steps ahead and think how the enemies will move on their turn. If the player defeats all the
 * robots by shooting them, they progress to the next level. If their health drops to 0, a gameover occurs.
 *
 * Controls:
 *  Menu:
 *    wasd: to make a menu selection
 *    space: to select the current selection
 *  Game:
 *    wasd: to move in a direction
 *    r: to reload the weapon
 *    q: to skip turn
 *    space: Start an attack action
 *      After starting attack action:
 *        wasd: aim attack
 *        q: cancel attack
 *        space: confirm attack
 *
 * States:
 * There are 5 states in the game:
 *  Start State: What opens when the game starts. This is the main menu and title screen. The player can select one of
 *  3 options on the menu: Start to enter the main game state, Level Select to enter the level select state, or How To
 *  Play to enter the how to play state.
 *
 *  Level Select State: Another menu state where the player can select which level to start in, rather than starting at
 *  the beginning of the game.
 *
 *  How To Play State: Another menu state where the player can see a brief list of controls and tips on playing the
 *  game. The player can only return to the Start State menu from here.
 *
 *  Test State (Main Game State): This state is the main gameplay state. Here all the logic for how the game is played
 *  is contained and the levels are created for the player to interact with. The player controls a character who can
 *  move around and shoot projectiles which disappear when colliding with walls or enemies. The projectiles damaage
 *  enemies and if the enemies take enough damage, are destroyed. There are two types of enemies, a melee one who can
 *  attack the player if they are in an adjacent space, and a ranged one who can attack the player if they have a clear
 *  line to them. Melee enemies path directly towards the player and ranged path to get line of sight on the player.
 *  Both use Dijkstras Algorithm to find a path to the player. The player can also pickup powerups to help them.
 *  Currently there are two powerups, an armor powerup which restores the player to full health and a beam weapon
 *  powerup which improves the players weapon with increased damage and piercing projectiles for the rest of the level.
 *  The player can only shoot 5 times before they must reload their weapon, costing their turn. The player has 10 hit
 *  points which are shown in the HUD along with their ammo cound and current weapon. If they lose all their hitpoints,
 *  gameover occurs. The player must either defeat all the robots in each level to enter the Game Complete State or
 *  lose all their health in a level where a gameover message is displayed and the player is returned to the Start
 *  State.
 *
 *  Game Complete State: After level 5 of the Test State is complete, the player is taken to this screen, where a
 *  congratulations and thank you message is given. The player can press esc to exit the game, or space to reenter the
 *  Start State and main menu.
 *
 *
 * Graphical Asset Credits:
 *  Enemy, character, and tile assets courtesy of Pupkin and modified by Brandon Huang:
 *   https://trevor-pupkin.itch.io/tech-dungeon-roguelite
 *  Armor/Weapon Icons courtesy of Jere Sikstus and modified by Brandon Huand:
 *    https://jeresikstus.itch.io/cyberpunk-items-16x16?download
 *  All Other graphical assets were made by Brandon Huang.
 *
 * Sound Credits:
 *  Sound effects were obtained on zapsplat.com coutesy of the following uploaders:
 *   zapsplat.com
 *   PMSFX: https://www.zapsplat.com/author/pmsfx/
 *   and Epic Stock Media: https://www.zapsplat.com/author/epic-stock-media/
 *
 * @author Brandon Huang,
 *
 */

public class RoboGame extends StateBasedGame {
  // States
  public static final int STARTUPSTATE = 0;
  public static final int TESTSTATE = 1;
  public static final int HOWTOPLAYSTATE = 2;
  public static final int LEVELSELECTSTATE = 3;
  public static final int GAMECOMPLETESTATE = 4;

  /*** ASSET PATHS ***/

  // Map and Hud
  public static final String TILE_FLOORIMG_RSC = "RoboRampage/Assets/floor.png";
  public static final String TILE_FLOORIMG2_RSC = "RoboRampage/Assets/floor2.png";
  public static final String TILE_FLOORIMG3_RSC = "RoboRampage/Assets/floor3.png";
  public static final String TILE_FLOORIMG4_RSC = "RoboRampage/Assets/floor4.png";
  public static final String TILE_WALLIMG_RSC = "RoboRampage/Assets/wall.png";
  public static final String TILE_FLOORACID_RSC = "RoboRampage/Assets/floorAcid.png";

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
  public static final String UTIL_HUDREADY_RSC = "RoboRampage/Assets/hudReady.png";
  public static final String UTIL_HUDWAIT_RSC = "RoboRampage/Assets/hudWait.png";
  public static final String UTIL_GUNDEFAULT_RSC = "RoboRampage/Assets/gunDefault.png";
  public static final String UTIL_GUNBEAM_RSC = "RoboRampage/Assets/gunBeam.png";
  public static final String UTIL_GUNLAUNCHER_RSC = "RoboRampage/Assets/gunLauncher.png";
  public static final String UTIL_PICKUPARMOR_RSC = "RoboRampage/Assets/pickupArmor.png";
  public static final String UTIL_PICKUPAMMO_RSC = "RoboRampage/Assets/pickupAmmo.png";
  public static final String UTIL_PICKUPBEAMGUN_RSC = "RoboRampage/Assets/pickupBeamGun.png";

  // Menu
  public static final String MENU_SPLASH_RSC = "RoboRampage/Assets/menuSplash.png";
  public static final String MENU_ARROW_RSC = "RoboRampage/Assets/menuArrow.png";
  public static final String MENU_HOWTOPLAY_RSC = "RoboRampage/Assets/menuHowToPlay.png";
  public static final String MENU_START_RSC = "RoboRampage/Assets/menuStart.png";
  public static final String MENU_LEVELSELECT_RSC = "RoboRampage/Assets/menuLevelSelect.png";
  public static final String MENU_BACK_RSC = "RoboRampage/Assets/menuBack.png";
  public static final String MENU_HOWTOPLAYSPLASH_RSC = "RoboRampage/Assets/howToPlaySplash.png";
  public static final String MENU_LEVELSELECTSPLASH_RSC = "RoboRampage/Assets/levelSelectSplash.png";
  public static final String MENU_GAMEOVER_RSC = "RoboRampage/Assets/menuGameover.png";
  public static final String MENU_LEVELCOMPLETE_RSC = "RoboRampage/Assets/menuLevelComplete.png";
  public static final String MENU_GAMECOMPLETE_RSC = "RoboRampage/Assets/menuGameComplete.png";
  public static final String MENU_OUTOFAMMO_RSC = "RoboRampage/Assets/menuOutOfAmmo.png";
  public static final String MENU_LEVEL1_RSC = "RoboRampage/Assets/menuLevel1.png";
  public static final String MENU_LEVEL2_RSC = "RoboRampage/Assets/menuLevel2.png";
  public static final String MENU_LEVEL3_RSC = "RoboRampage/Assets/menuLevel3.png";
  public static final String MENU_LEVEL4_RSC = "RoboRampage/Assets/menuLevel4.png";
  public static final String MENU_LEVEL5_RSC = "RoboRampage/Assets/menuLevel5.png";
  public static final String MENU_ENDLESS_RSC = "RoboRampage/Assets/menuEndless.png";

  // Melee Enemy
  public static final String ENEMY_MELEEIMG_RSC = "RoboRampage/Assets/meleeEnemy.png";
  public static final String ENEMY_MELEEIDLERIGHT_RSC = "RoboRampage/Assets/enemyMeleeIdleRight.png";
  public static final String ENEMY_MELEEIDLELEFT_RSC = "RoboRampage/Assets/enemyMeleeIdleLeft.png";
  public static final String ENEMY_MELEEMOVELEFT_RSC = "RoboRampage/Assets/enemyMeleeMoveLeft.png";
  public static final String ENEMY_MELEEMOVERIGHT_RSC = "RoboRampage/Assets/enemyMeleeMoveRight.png";
  public static final String ENEMY_MELEEATTACKLEFT_RSC = "RoboRampage/Assets/enemyMeleeAttackLeft.png";
  public static final String ENEMY_MELEEATTACKRIGHT_RSC = "RoboRampage/Assets/enemyMeleeAttackRight.png";
  public static final String ENEMY_MELEEDEFEATLEFT_RSC = "RoboRampage/Assets/enemyMeleeDefeatLeft.png";
  public static final String ENEMY_MELEEDEFEATRIGHT_RSC = "RoboRampage/Assets/enemyMeleeDefeatRight.png";
  public static final String ENEMY_SPAWNPOINT_RSC = "RoboRampage/Assets/spawnpoint.png";

  // Ranged Enemy
  public static final String ENEMY_RANGEDIDLERIGHT_RSC = "RoboRampage/Assets/enemyRangedIdleRight.png";
  public static final String ENEMY_RANGEDIDLELEFT_RSC = "RoboRampage/Assets/enemyRangedIdleLeft.png";
  public static final String ENEMY_RANGEDDEFEATRIGHT_RSC = "RoboRampage/Assets/enemyRangedDefeatRight.png";
  public static final String ENEMY_RANGEDDEFEATLEFT_RSC = "RoboRampage/Assets/enemyRangedDefeatLeft.png";
  public static final String ENEMY_RANGEDMOVERIGHT_RSC = "RoboRampage/Assets/enemyRangedMoveRight.png";
  public static final String ENEMY_RANGEDMOVELEFT_RSC = "RoboRampage/Assets/enemyRangedMoveLeft.png";
  public static final String ENEMY_RANGEDSHOOTRIGHT_RSC = "RoboRampage/Assets/enemyRangedShootRight.png";
  public static final String ENEMY_RANGEDSHOOTLEFT_RSC = "RoboRampage/Assets/enemyRangedShootLeft.png";
  public static final String ENEMY_PROJECTILE_RSC = "RoboRampage/Assets/enemyProjectile.png";

  // Player
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
  public static final String PLAYER_HITINDICATOR_RSC = "RoboRampage/Assets/playerHitIndicator.png";
  public static final String PLAYER_BEAMHORIZONTAL_RSC = "RoboRampage/Assets/playerProjectileBeamHorizontal.png";
  public static final String PLAYER_BEAMVERTICAL_RSC = "RoboRampage/Assets/playerProjectileBeamVertical.png";

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
  public static final String SOUND_VICTORY_RSC = "RoboRampage/Assets/Sounds/victory.wav";

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
    addState(new GameCompleteState());

    /*** RESOURCE LOADING ***/
    // Map Stuff
    ResourceManager.loadImage(TILE_FLOORIMG_RSC);
    ResourceManager.loadImage(TILE_WALLIMG_RSC);
    ResourceManager.loadImage(TILE_FLOORIMG2_RSC);
    ResourceManager.loadImage(TILE_FLOORIMG3_RSC);
    ResourceManager.loadImage(TILE_FLOORIMG4_RSC);
    ResourceManager.loadImage(TILE_FLOORACID_RSC);
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
    ResourceManager.loadImage(PLAYER_BEAMHORIZONTAL_RSC);
    ResourceManager.loadImage(PLAYER_BEAMVERTICAL_RSC);
    ResourceManager.loadImage(PLAYER_HITINDICATOR_RSC);
    // Enemy Sheets
    ResourceManager.loadImage(ENEMY_SPAWNPOINT_RSC);
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
    ResourceManager.loadImage(UTIL_HUDREADY_RSC);
    ResourceManager.loadImage(UTIL_HUDWAIT_RSC);
    ResourceManager.loadImage(UTIL_GUNDEFAULT_RSC);
    ResourceManager.loadImage(UTIL_GUNBEAM_RSC);
    ResourceManager.loadImage(UTIL_GUNLAUNCHER_RSC);
    ResourceManager.loadImage(UTIL_PICKUPARMOR_RSC);
    ResourceManager.loadImage(UTIL_PICKUPAMMO_RSC);
    ResourceManager.loadImage(UTIL_PICKUPBEAMGUN_RSC);
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
    ResourceManager.loadImage(MENU_LEVELCOMPLETE_RSC);
    ResourceManager.loadImage(MENU_GAMECOMPLETE_RSC);
    ResourceManager.loadImage(MENU_OUTOFAMMO_RSC);
    ResourceManager.loadImage(MENU_LEVEL1_RSC);
    ResourceManager.loadImage(MENU_LEVEL2_RSC);
    ResourceManager.loadImage(MENU_LEVEL3_RSC);
    ResourceManager.loadImage(MENU_LEVEL4_RSC);
    ResourceManager.loadImage(MENU_LEVEL5_RSC);
    ResourceManager.loadImage(MENU_ENDLESS_RSC);
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
    ResourceManager.loadSound(SOUND_VICTORY_RSC);
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

  /***
   * This function builds a tilemap from a given string which represents the level layout. The string is one long
   * uninterrupted string of 100 characters to build a 10x10 map. The String consists of integers which are IDs for
   * what type each space is. Specifically only works for 100 character strings to build 10x10s. DO NOT GIVE OTHER
   * STINGS.
   * Note: This method needs to be modified if different sized maps are made.
   * @param map
   *  The string for building the level
   * @return
   *  A finished tile map (2D tile array) of constructed tiles
   */
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

  /***
   * Special version of the getDijkstras method (see below). This method builds a new Tile map with new costs to cause
   * different pathing behavior for ranged enemies who just need line of sight on the player. Each floor tile not in
   * line of sight of the player has an increased tile cost so the shortest path is to get in sight of the player.
   * Calls getDijkstras with the new costed graph to get the vertex array.
   *
   * @param player
   *  Location of the player to path to
   * @param tilemap
   *  tile map of the level showing its layout.
   * @return
   *  A 2D vertex array filled with distances and shortest path directions for the corresponding tilemap.
   */
  public static Vertex[][] getRangedDijkstras(Player player, Tile[][] tilemap) {
    Vertex path[][];

    // Build a copy of the tileMap so we don't modify it.
    Tile rangedTileMap[][] = new Tile[10][10];
    for(int i = 0; i < 10; i++) {
      for(int j = 0; j < 10; j++) {
        rangedTileMap[i][j] = tilemap[i][j].getCopy();
      }
    }

    Coordinate playerLoc = player.getLocation();
    int x = playerLoc.x;
    int y = playerLoc.y;

    // Set tiles with line of sight on player to a lower ranged cost by traversing in each direction until we hit a wall
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

    // Use dijkstras on the new tile map to get the path.
    path = getDijkstras(playerLoc.x, playerLoc.y, rangedTileMap);
    return path;
  }

  /**
   * Dijkstras Algorithm for a 10x10 map of Tile objects. Will build a 2D Vertex array to be used for pathfinding for
   * enemies in the game. Only works for 10x10 maps currently
   * Note: If different maps sizes are ever used, this function needs to be modified.
   *
   * @param sourcex
   *  The x coordinate to path to
   * @param sourcey
   *  The y coordinate to path to
   * @param tileMap
   *  The constructed tile map showing the map layout
   * @return
   *  A completed 2D vertex array, filled with costs and directions to move in.
   */
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
      // Get the node with the current shortest distance
      Coordinate current = shortestDistance(path, seen);
      int x = current.x;
      int y = current.y;
      // Mark the current node as seen.
      seen[x][y] = true;
      int compare;
      int currentDist = path[x][y].getDistance();

      // Now update tile distances that are adjacent if the distance is shorter then currently recorded.
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

  /***
   * Sub method for getDijkstras(). Checks the seen array if there are unseen nodes.
   * @param seen
   *  2D array of booleans showing all the currently seen nodes
   * @return
   *  true if there are unseen nodes
   *  false if there aren't
   */
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

  /***
   * Sub method for getDijkstras(). Searches the vertex graph for the unseen node with the lowest distance.
   * @param graph
   *  Vertex graph being searched
   * @param seen
   *  2D boolean array showing which nodes have been marked as seen
   * @return
   *  Coordinate of the node in the vertex which has the lowest distance and is unseen.
   */
  private static Coordinate shortestDistance (Vertex graph[][], boolean seen[][]) {
    Coordinate shortest = new Coordinate(0,0);
    int distance = 100000000;
    // Iterate through the graph and find the right node.
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
