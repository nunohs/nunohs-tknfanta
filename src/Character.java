import bagel.*;
import bagel.util.Colour;
import bagel.util.Point;

/**
* Character class, two options: Prabowo or Gibran.
 */
public class Character {
    // Physics constants
    private static final double JUMP_VELOCITY = 13.5;
    private static final double GRAVITY = 0.3;
    private static final double MAX_JUMP_HEIGHT = 250.0;
    
    // File paths (standardized to lowercase for consistency)
    private static final String PRABOWO_PATH = "res/prabowo.png";
    private static final String GIBRAN_PATH = "res/gibran.png";
    
    private final Image charImage;
    private boolean jumping = false;
    private final double xCoordinate = 282;
    private double yCoordinate;
    private double yVelocity = 0;
    private final Point initialPosition;
    private final int charWidth;
    private final int charHeight;
    private final ResourceManager resourceManager = ResourceManager.getInstance();

    public Character(String dir){
        // Standardize character selection
        String imagePath;
        if(dir.equalsIgnoreCase("prabowo")) {
            imagePath = PRABOWO_PATH;
            this.charWidth = 161;
            this.charHeight = 110;
            this.yCoordinate = (double)Window.getHeight()/ 2+92;
            this.initialPosition = new Point(282,(double)Window.getHeight()/2+92);
        } else {
            imagePath = GIBRAN_PATH;
            this.charWidth = 167;
            this.charHeight = 98;
            this.yCoordinate = (double)Window.getHeight()/ 2+98;
            this.initialPosition = new Point(282,(double)Window.getHeight()/2+98);
        }
        
        // Load image with proper error handling
        Image img = resourceManager.getImage(imagePath);
        if (img == null) {
            // If image failed to load, create a colored rectangle as a fallback
            System.err.println("Failed to load character image: " + imagePath);
        }
        this.charImage = img;
    }
    
    public Point getCharCurrentPosition(){
        return new Point(xCoordinate, yCoordinate);
    }

    public Image getCharImage() {
        return charImage;
    }

    public int getCharWidth() {
        return charWidth;
    }

    public int getCharHeight() {
        return charHeight;
    }

    public void update(Input input){
        // Handle jump input
        if(input.wasPressed(Keys.SPACE) && !jumping){
            startJump();
        }
        
        // Update position
        if(jumping){
            updateJump();
        }
        
        // Draw character
        if (charImage != null) {
            charImage.draw(xCoordinate, yCoordinate);
        } else {
            // Fallback rendering if image is null
            Drawing.drawRectangle(xCoordinate - charWidth/2, yCoordinate - charHeight/2, 
                                 charWidth, charHeight, new Colour(1, 0.5, 0));
        }
    }

    /**
     * Start the jump by setting initial velocity
     */
    private void startJump() {
        jumping = true;
        yVelocity = -JUMP_VELOCITY; // Negative velocity means moving up
    }

    /**
     * Updates character position during a jump using physics
     */
    private void updateJump(){
        // Apply velocity to position
        yCoordinate += yVelocity;
        
        // Apply gravity to velocity
        yVelocity += GRAVITY;
        
        // Check if character has returned to the ground
        if (yCoordinate >= initialPosition.y) {
            // Reset to ground position and end jump
            yCoordinate = initialPosition.y;
            yVelocity = 0;
            jumping = false;
        }
        
        // Cap jump height for gameplay consistency
        if (yCoordinate < initialPosition.y - MAX_JUMP_HEIGHT) {
            yCoordinate = initialPosition.y - MAX_JUMP_HEIGHT;
            yVelocity = 0; // Start falling immediately
        }
    }
}
