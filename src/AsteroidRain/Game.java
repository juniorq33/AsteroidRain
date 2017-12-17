package AsteroidRain;

import java.applet.AudioClip;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;

/**
 * Putting together some tutorials
 * 
 * author Junior Quintana
 * 
 * 
 */

public class Game {
    
    /**
     * Generating a random number.
     */
    private Random random;
    
    /**
     * Font use to write statistic to the screen.
     */
    private Font font;
    
    /**
     * Array list of the Asteroids in X.
     */
    private ArrayList<Asteroid> asteroidsX;
    
    /**
     * Array list of the Asteroids en Y.
     */
    private ArrayList<Asteroid> asteroidsY;
    
    
    /**
     * Array list of the Animations.
     */
    private ArrayList<Animation> explosionsList;
    
    /**
     * How many Asteroids leave the screen alive?
     */
    private int runawayAsteroids;
    
   /**
     * How many Asteroids the player destroyed?
     */
    private int killedAsteroids;
    
    /**
     * For each destroyed Asteroid, the player gets points.
     */
    private int score;
    
   /**
     * How many times a player is shot?
     */
    private int shoots;
    
    /**
     * Percent of effectivity;
     */
    private double effectivity;
    
    /**
     * Best Score
     */
    private int bestScore;
    
    /**
     * Last time of the shoot.
     */
    private long lastTimeShoot;    
    /**
     * The time which must elapse between shots.
     */
    private long timeBetweenShots;

    /**
     * Game background image.
     */
    private BufferedImage backgroundImg;
    
    /**
     * Explosions image.
     */
      
    private BufferedImage explosionAnimImg;
    
    /**
     * Asteroid image.
     */
    private BufferedImage asteroidImg;
    
    /**
     * Shotgun sight image.
     */
    private BufferedImage sightImg;
    
    /**
     * Sound effect for destroying asteroid
     */
    private AudioClip asteroidDestroyed;
    
    /**
     * Sound effect for background game
     */
    private AudioClip backgroundSound;

    /**
     * Middle width of the sight image.
     */
    private int sightImgMiddleWidth;
    /**
     * Middle height of the sight image.
     */
    private int sightImgMiddleHeight;
    

    public Game()
    {
        Framework.gameState = Framework.GameState.GAME_CONTENT_LOADING;
        
        Thread threadForInitGame = new Thread() {
            @Override
            public void run(){
                // Sets variables and objects for the game.
                Initialize();
                // Load game files (images, sounds, ...)
                LoadContent();
                
                backgroundSound.loop();
                
                Framework.gameState = Framework.GameState.PLAYING;
            }
        };
        threadForInitGame.start();
    }
    
    
   /**
     * Set variables and objects for the game.
     */
    private void Initialize()
    {
        random = new Random();        
        font = new Font("monospaced", Font.BOLD, 19);
        
        asteroidsX = new ArrayList<Asteroid>();
        asteroidsY = new ArrayList<Asteroid>();
        explosionsList = new ArrayList<Animation>();
        
        runawayAsteroids = 0;
        killedAsteroids = 0;
        score = 0;
        shoots = 0;
        effectivity = 0.0;
        bestScore = 0;
        
        lastTimeShoot = 0;
        timeBetweenShots = Framework.secInNanosec / 3;
    }
    
    /**
     * Load game files - images, sounds, ...
     */
    private void LoadContent()
    {
        
        
        
        try
        {
            
            URL backgroundImgUrl = this.getClass().getResource("space.jpg");
            backgroundImg = ImageIO.read(backgroundImgUrl);
            
            URL asteroidImgUrl = this.getClass().getResource("asteroid-icon.png");
            asteroidImg = ImageIO.read(asteroidImgUrl);
            
            URL explosionAnimImgUrl = this.getClass().getResource("explosion_anim.png");
            explosionAnimImg = ImageIO.read(explosionAnimImgUrl);
            
            URL sightImgUrl = this.getClass().getResource("sight.png");
            sightImg = ImageIO.read(sightImgUrl);
            sightImgMiddleWidth = sightImg.getWidth() / 2;
            sightImgMiddleHeight = sightImg.getHeight() / 2;
            
            URL asteroidDestroyedSoundUrl = this.getClass().getResource("BombSound.wav");
            asteroidDestroyed = java.applet.Applet.newAudioClip(asteroidDestroyedSoundUrl);
            
            URL backgroundSoundUrl = this.getClass().getResource("BackgroundSound.wav");
            backgroundSound = java.applet.Applet.newAudioClip(backgroundSoundUrl);
            
        }
        catch (IOException ex) {
            Logger.getLogger(Game.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    
    /**
     * Restart game - reset some variables.
     */
    public void RestartGame()
    {
        // Removes all of the ateroids from this list.
        asteroidsX.clear();
        asteroidsY.clear();
        explosionsList.clear();
        
        // Setting last asteroid time to zero.
        Asteroid.lastAsteroidTime = 0;
        
        if (score >= bestScore)
            bestScore = score;
        runawayAsteroids = 0;
        killedAsteroids = 0;
        score = 0;
        shoots = 0;
        effectivity = 0.0;
        
        lastTimeShoot = 0;
    }
    
    
    /**
     * Update game logic.
     * 
     * @param gameTime gameTime of the game.
     * @param mousePosition current mouse position.
     */
    public void UpdateGame(long gameTime, Point mousePosition)
    {
        // Creates a new Asteroid, if it's the time, and add it to the array list.
        if(System.nanoTime() - Asteroid.lastAsteroidTime >= Asteroid.timeBetweenAsteroids)
        {
            // Here we create new asteroid and add it to the array list.
            asteroidsX.add(new Asteroid(Asteroid.asteroidLines[Asteroid.nextAsteroidLines][0] + random.nextInt(200), Asteroid.asteroidLines[Asteroid.nextAsteroidLines][1], Asteroid.asteroidLines[Asteroid.nextAsteroidLines][2], Asteroid.asteroidLines[Asteroid.nextAsteroidLines][3], asteroidImg));
            asteroidsY.add(new Asteroid(Asteroid.asteroidLinesY[Asteroid.nextAsteroidLines][0] + random.nextInt(200), Asteroid.asteroidLinesY[Asteroid.nextAsteroidLines][1], Asteroid.asteroidLinesY[Asteroid.nextAsteroidLines][2], Asteroid.asteroidLinesY[Asteroid.nextAsteroidLines][3], asteroidImg));
            // Here we increase nextAsteroidLines so that next Asteroid will be created in next line.
            Asteroid.nextAsteroidLines++;
            if(Asteroid.nextAsteroidLines >= Asteroid.asteroidLines.length)
                Asteroid.nextAsteroidLines = 0;
            
            if(Asteroid.nextAsteroidLines >= Asteroid.asteroidLinesY.length)
                Asteroid.nextAsteroidLines = 0;
            
            Asteroid.lastAsteroidTime = System.nanoTime();
        }
        
        // Update all of the Asteroids in X axis.
        for(int i = 0; i < asteroidsX.size(); i++)
        {
            // Move the Asteroid.
            asteroidsX.get(i).Update();
            
            
            // Checks if the Asteroid leaves the screen and remove it if it does.
            if(asteroidsX.get(i).x < 0 - asteroidImg.getWidth() || asteroidsX.get(i).x > 1700)
            {
                asteroidsX.remove(i);
                runawayAsteroids++;
            }
        }
        
        // Update all of the Asteroids in Y axis.
        for(int i = 0; i < asteroidsY.size(); i++)
        {
            // Move the Asteroid.
            asteroidsY.get(i).UpdateY();
            
            
            // Checks if the Asteroid leaves the screen and remove it if it does.
            if(asteroidsY.get(i).y < 0 - asteroidImg.getHeight() || asteroidsY.get(i).y > 1000)
            {
                asteroidsY.remove(i);
                runawayAsteroids++;
            }
        }
        
        
        
        
        
        // Does player shoots?
        if(Canvas.mouseButtonState(MouseEvent.BUTTON1))
        {
            effectivity = ((double)killedAsteroids/shoots) * 100;
            
            // Checks if it can shoot again.
            if(System.nanoTime() - lastTimeShoot >= timeBetweenShots)
            {
                shoots++;
                
                // We go over all the Asteroids and we look if any of them was shoot.
                for(int i = 0; i < asteroidsX.size(); i++)
                {
                    // We check, if the mouse was over Asteroids head or body, when player has shot.
                    if(new Rectangle(asteroidsX.get(i).x + 50, asteroidsX.get(i).y +50    , 150, 150).contains(mousePosition))
                    {
                        killedAsteroids++;
                        score += asteroidsX.get(i).score;
                        effectivity = ((double)killedAsteroids/shoots) * 100;
                        
                        asteroidDestroyed.play();
                        
                        Animation expAnim = new Animation(explosionAnimImg, 134, 134, 12, 45, false, asteroidsX.get(i).x, asteroidsX.get(i).y, 0);
                        explosionsList.add(expAnim);
                        
                        // Remove the Asteroid from the array list.
                        asteroidsX.remove(i);
                        
                        // We found the Asteroid that player shoot so we can leave the for loop.
                        break;
                    }
                }
                
                for(int i = 0; i < asteroidsY.size(); i++)
                {
                    // We check, if the mouse was over Asteroids head or body, when player has shot.
                    if(new Rectangle(asteroidsY.get(i).x + 50, asteroidsY.get(i).y +50    , 150, 150).contains(mousePosition))
                    {
                        killedAsteroids++;
                        score += asteroidsY.get(i).score;
                        effectivity = ((double)killedAsteroids/shoots) * 100;
                        asteroidDestroyed.play();
                        
                        
                        Animation expAnim = new Animation(explosionAnimImg, 134, 134, 12, 45, false, asteroidsY.get(i).x, asteroidsY.get(i).y, 0);
                        explosionsList.add(expAnim);
                        
                        // Remove the Asteroid from the array list.
                        asteroidsY.remove(i);
                        
                        // We found the Asteroid that player shoot so we can leave the for loop.
                        break;
                    }
                }
                
                lastTimeShoot = System.nanoTime();
            }
        }
        
        updateExplosions();
        
        // When 10 Asteroids runaway, the game ends.
        if(runawayAsteroids >= 10)
            Framework.gameState = Framework.GameState.GAMEOVER;
    }
    
    /**
     * Draw the game to the screen.
     * 
     * @param g2d Graphics2D
     * @param mousePosition current mouse position.
     */
    public void Draw(Graphics2D g2d, Point mousePosition)
    {
        g2d.drawImage(backgroundImg, 0, 0, Framework.frameWidth, Framework.frameHeight, null);
        
        // Here we draw all the Asteroids.
        for(int i = 0; i < asteroidsX.size(); i++)
        {
            asteroidsX.get(i).Draw(g2d);
            //This comment code is for placing a rectagular figure on the asteroid so in that way is better to set the shooting range
            //g2d.drawRect(asteroids.get(i).x + 50, asteroids.get(i).y +50    , 150, 150);
	
        }
        
         //Here we draw all the Asteroids.
        for(int i = 0; i < asteroidsY.size(); i++)
        {
            asteroidsY.get(i).Draw(g2d);
            //g2d.drawRect(asteroids.get(i).x + 50, asteroids.get(i).y +50    , 150, 150);
	
        }
        
        // Draw all explosions.
        for(int i = 0; i < explosionsList.size(); i++)
        {
            explosionsList.get(i).Draw(g2d);
        }
        
        
        g2d.drawImage(sightImg, mousePosition.x - sightImgMiddleWidth, mousePosition.y - sightImgMiddleHeight, null);
        
        g2d.setFont(font);
        g2d.setColor(Color.WHITE);
        
        g2d.drawString("RUNAWAY: " + runawayAsteroids, 10, 21);
        g2d.drawString("DESTROYED: " + killedAsteroids, 160, 21);
        g2d.drawString("SHOOTS: " + shoots, 348, 21);
        g2d.drawString("SCORE: " + score, 500, 21);
        g2d.drawString("% K/S: " + Math.round(effectivity), 625, 21);
        g2d.drawString("Best Score: " + bestScore, 750, 21);
       
    }
    
    
    /**
     * Draw the game over screen.
     * 
     * @param g2d Graphics2D
     * @param mousePosition Current mouse position.
     */
    public void DrawGameOver(Graphics2D g2d, Point mousePosition)
    {
        Draw(g2d, mousePosition);
        
        // The first text is used for shade.
        g2d.setColor(Color.black);
        g2d.drawString("GAME OVER", Framework.frameWidth / 2 - 39, (int)(Framework.frameHeight * 0.65) + 1);
        g2d.drawString("Press space or enter to restart, or ESC to exit.", Framework.frameWidth / 2 - 249, (int)(Framework.frameHeight * 0.70) + 1);
        g2d.setColor(Color.blue);
        g2d.drawString("GAME OVER", Framework.frameWidth / 2 - 40, (int)(Framework.frameHeight * 0.65));
        g2d.drawString("Press space or enter to restart, or ESC to exit.", Framework.frameWidth / 2 - 250, (int)(Framework.frameHeight * 0.70));
    }
    
    /**
     * Updates all the animations of an explosion and remove the animation when is over.
     */
    private void updateExplosions()
    {
        for(int i = 0; i < explosionsList.size(); i++)
        {
            // If the animation is over we remove it from the list.
            if(!explosionsList.get(i).active)
                explosionsList.remove(i);
        }
    }
}
