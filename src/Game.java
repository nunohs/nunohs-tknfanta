import bagel.*;
import bagel.util.Point;
import java.util.List;
import java.util.ArrayList;
import java.util.concurrent.ThreadLocalRandom;

/**
*  Class for each Game played by user
 */
public class Game {
    // Constants for game mechanics
    private static final int BASE_OBSTACLE_INTERVAL = 260;
    private static final int SCORE_INTERVAL = 15;
    private static final int SCORE_INCREMENT = 1;
    private static final int OBSTACLE_SCORE = 10;
    private static final int MAX_SPEED = 40;
    
    // Resource paths
    private static final String DEFAULT_FONT = "res/FSO8BITR.TTF";
    
    private int currFrame = 0;
    private final MovingBackground background;
    private final Character character;
    private final List<Obstacle> obstacles = new ArrayList<>();
    private int numObstacles = 0;
    private int currObstacle = 0; // Changed from final to allow tracking past obstacles
    private int speed = 6;
    private int score = 0;
    private boolean finished = false;
    private int lastSpeed = 6; // Track previous speed for boost detection

    // Font handling
    private final Font scoreFont;
    private final ResourceManager resourceManager = ResourceManager.getInstance();
    private final SpeedBoost speedBoost;

    public Game(String character){
        this.character = new Character(character);
        this.background = new MovingBackground();
        this.speedBoost = new SpeedBoost();
        
        // Get font with error handling
        Font font = resourceManager.getFont(DEFAULT_FONT, 30);
        if (font == null) {
            System.err.println("Failed to load score font. Using fallback.");
            // Try to load the fallback font
            font = resourceManager.getFont("res/font.ttf", 30);
        }
        this.scoreFont = font;
    }

    public boolean isFinished() {
        return finished;
    }
    
    public int getScore() {
        return score;
    }

    public void update(Input input){
        if (input == null) {
            System.err.println("Warning: Input is null in Game.update()");
            return;
        }
        
        currFrame++;
        background.update(speed);
        character.update(input);

        // Generate obstacles at a rate inversely proportional to speed
        int obstacleInterval = Math.max(BASE_OBSTACLE_INTERVAL - (speed * 3), 120);
        if(currFrame % obstacleInterval == 0){
            createObstacles(obstacles);
        }

        // Check for collisions and update obstacles
        updateObstacles();
        
        // Update score based on frames passed
        if(currFrame % SCORE_INTERVAL == 0) {
            score += SCORE_INCREMENT;
        }
        
        // Update speed boost effect
        if (speed > lastSpeed) {
            speedBoost.activate();
        }
        lastSpeed = speed;
        
        // Draw speed boost effect behind character
        Point charPos = character.getCharCurrentPosition();
        speedBoost.update(charPos.x, charPos.y);
        
        // Draw score with improved visibility
        if (scoreFont != null) {
            // Format score with leading zeros
            String scoreText = String.format("Score %04d", score);
            scoreFont.drawString(scoreText, (double)Window.getWidth()/2,
                    (double)Window.getHeight()/2 - 200);
        } else {
            System.err.println("Warning: Score font is null, cannot display score");
        }
    }
    
    /**
     * Updates obstacles and checks for collisions
     */
    private void updateObstacles() {
        // Ensure we don't go out of bounds
        if (currObstacle < 0) {
            currObstacle = 0;
        }
        
        for (int i = currObstacle; i < numObstacles && i < obstacles.size(); i++) {
            Obstacle obstacle = obstacles.get(i);
            if (obstacle == null) {
                System.err.println("Warning: Null obstacle detected at index " + i);
                continue;
            }
            
            if(obstacle.isActive()) {
                // Check for collision
                if(obstacle.checkCollision(
                        (int)character.getCharCurrentPosition().x,
                        (int)character.getCharCurrentPosition().y,
                        character.getCharWidth(),
                        character.getCharHeight(),
                        obstacle.getX(),
                        obstacle.getY(),
                        obstacle.getObstacleWidth(),
                        obstacle.getObstacleHeight())){
                    finished = true;
                    break;
                } else {
                    // Update obstacle position and get score
                    int obstacleScore = obstacle.update(speed);
                    score += obstacleScore;
                    
                    // If obstacle is now inactive and added score, it was passed
                    if (obstacleScore > 0 && !obstacle.isActive()) {
                        // Advance the current obstacle tracker if this was the first one
                        if (i == currObstacle) {
                            currObstacle++;
                        }
                    }
                }
            } else if (i == currObstacle) {
                // Skip inactive obstacles
                currObstacle++;
    }
        }
    }
    
    /**
     * Updates the game speed based on progress
     */
    private void updateGameSpeed(){
        // Increase speed every 5 obstacles, up to a maximum of MAX_SPEED
        if(numObstacles % 5 == 0 && numObstacles != 0 && speed < MAX_SPEED){
            speed++;
        }
    }

    /**
     * Creates a new obstacle
     */
    public void createObstacles(List<Obstacle> obstacles){
        if (obstacles == null) {
            System.err.println("Warning: Obstacle list is null in createObstacles()");
            return;
        }
        
        int randomNum = ThreadLocalRandom.current().nextInt(1,7);
        updateGameSpeed(); // Renamed from getGameSpeed to reflect its actual function
        
        Obstacle obstacle = new Obstacle("asset_obstacle "+ randomNum);
        if (obstacle == null || obstacle.getImage() == null) {
            System.err.println("Warning: Failed to create valid obstacle");
            return;
        }
        
        obstacles.add(numObstacles, obstacle);
        numObstacles++;
    }

    /**
     * Draws the ending scene, called when the user has hit an obstacle
     */
    public void drawEnding(){
        background.drawEndingBackground();
        
        if (scoreFont != null) {
            scoreFont.drawString("Score " + score, (double)Window.getWidth()/2,
                (double)Window.getHeight()/2-200);
        } else {
            // Just log the issue if font is null
            System.err.println("Warning: Score font is null, cannot display score in ending");
        }
        
        Image charImage = character.getCharImage();
        if (charImage != null) {
            charImage.draw(character.getCharCurrentPosition().x, character.getCharCurrentPosition().y);
        }
        
        for (int i = currObstacle; i < numObstacles && i < obstacles.size(); i++) {
            Obstacle obstacle = obstacles.get(i);
            if (obstacle != null && obstacle.isActive()) {
                Image obstacleImage = obstacle.getImage();
                if (obstacleImage != null) {
                    obstacleImage.draw(obstacle.getX(), obstacle.getY());
            }
        }
    }
    }
}
