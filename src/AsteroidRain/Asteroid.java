package AsteroidRain;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

/**
 * Asteroid class.
 */

public class Asteroid {
    
    /**
     * How much time must pass in order to create a new asteroid?
     */
    public static long timeBetweenAsteroids = Framework.secInNanosec + 60;
    /**
     * Last time when the asteroid was created.
     */
    public static long lastAsteroidTime = 0;

    
    /**
     * Asteroid lines.
     * Where is starting location for the asteroid?
     * Speed of the asteroid?
     * How many points is a asteroid worth?
     */
    public static int[][] asteroidLines = {
                                       {(int)Framework.frameWidth - 1550, (int)(Framework.frameHeight * 0.15), 12, 5},
                                       {Framework.frameWidth, (int)(Framework.frameHeight * 0.25), -5, 3},
                                       {Framework.frameWidth, (int)(Framework.frameHeight * 0.50), -2, 2},
                                       {Framework.frameWidth, (int)(Framework.frameHeight * 0.65), -8, 4},
                                      };
    
    public static int[][] asteroidLinesY = {
                                       {(int)(Framework.frameWidth * 0.40), Framework.frameHeight, -3, 2},
                                       {(int)(Framework.frameWidth * 0.65), Framework.frameHeight, -1, 3},
                                       {(int)(Framework.frameWidth * 0.05), (int)Framework.frameHeight - 900, 11, 5},
                                       {(int)(Framework.frameWidth * 0.19), Framework.frameHeight, -5
                                               , 4},
                                      };
    /**
     * Indicate which is next asteroid line.
     */
    public static int nextAsteroidLines = 0;
    
    
    /**
     * X coordinate of the Asteroid.
     */
    public int x;
    /**
     * Y coordinate of the Asteroid.
     */
    public int y;
    
    /**
     * How fast the Asteroid should move? And to which direction?
     */
    private int speed;
    
    /**
     * How many points this Asteroid is worth?
     */
    public int score;
    
    /**
     * Asteroid image.
     */
    private BufferedImage asteroidImg;
    
    
    /**
     * Creates new Asteroid.
     * 
     * @param x Starting x coordinate.
     * @param y Starting y coordinate.
     * @param speed The speed of this Asteroid.
     * @param score How many points this Asteroid is worth?
     * @param asteroidImg Image of the Asteroid.
     */
    public Asteroid(int x, int y, int speed, int score, BufferedImage asteroidImg)
    {
        this.x = x;
        this.y = y;
        
        this.speed = speed;
        
        this.score = score;
        
        this.asteroidImg = asteroidImg;        
    }
    
    
    /**
     * Move the asteroid x axis.
     */
    public void Update()
    {
        x += speed;
    }
    
    //Move asteroid y axis
    public void UpdateY()
    {
        y += speed;
    }
    
    
    /**
     * Draw the asteroid to the screen.
     * @param g2d Graphics2D
     */
    public void Draw(Graphics2D g2d)
    {
        g2d.drawImage(asteroidImg, x, y, null);
    }
}
