import bagel.*;

/**
*  Moves the distinct backgrounds to the left creating an effect that character is moving
 */
public class MovingBackground {
    // Number of background layers
    private static final int NUM_BACKGROUNDS = 3;
    
    // Resource paths
    private static final String RESOURCE_PATH = "res/asset_background ";
    private static final double SCROLL_THRESHOLD = 0.5; // Threshold for background scrolling
    
    private final Image[] backgrounds = new Image[NUM_BACKGROUNDS];
    private final int[] backgroundX = new int[NUM_BACKGROUNDS];
    private final ResourceManager resourceManager = ResourceManager.getInstance();
    private boolean resourceLoadSuccessful = true;
    private int lastWindowWidth = 0;
    private int lastWindowHeight = 0;

    public MovingBackground() {
        initializeBackgrounds();
    }

    private void initializeBackgrounds() {
        int successfulLoads = 0;
        lastWindowWidth = Window.getWidth();
        lastWindowHeight = Window.getHeight();
        
        for (int i = 0; i < NUM_BACKGROUNDS; i++) {
            try {
                String path = RESOURCE_PATH + (i + 1) + ".png";
                Image img = resourceManager.getImage(path);
                if (img == null) {
                    System.err.println("Warning: Failed to load background image: " + path);
                } else {
                    successfulLoads++;
                }
                this.backgrounds[i] = img;
                this.backgroundX[i] = lastWindowWidth/2 + (i*lastWindowWidth);
            } catch (Exception e) {
                System.err.println("Error loading background image " + (i+1) + ": " + e.getMessage());
                this.backgrounds[i] = null;
            }
        }
        
        resourceLoadSuccessful = (successfulLoads > 0);
        if (!resourceLoadSuccessful) {
            System.err.println("Critical: No background images could be loaded");
        }
    }

    public void update(int speed) {
        if (!resourceLoadSuccessful) {
            return;
        }
        
        // Check if window size changed
        if (lastWindowWidth != Window.getWidth() || lastWindowHeight != Window.getHeight()) {
            initializeBackgrounds();
            return;
        }
        
        for (int i = 0; i < NUM_BACKGROUNDS; i++) {
            if (backgrounds[i] == null) continue;
            
            // Only update position if speed is significant
            if (Math.abs(speed) > SCROLL_THRESHOLD) {
                backgroundX[i] -= speed;
            }

            // Check if the background is completely off-screen to the left
            if (backgroundX[i] + backgrounds[i].getWidth()/2 < 0) {
                // Move the background to the right of the screen
                backgroundX[i] += NUM_BACKGROUNDS * lastWindowWidth;
            }

            try {
                backgrounds[i].draw(backgroundX[i], (double) lastWindowHeight / 2);
            } catch (Exception e) {
                System.err.println("Error drawing background " + (i+1) + ": " + e.getMessage());
            }
        }
    }

    /**
     * Draws the background at the end of the game
     */
    public void drawEndingBackground() {
        if (!resourceLoadSuccessful) {
            return;
        }
        
        for (int i = 0; i < NUM_BACKGROUNDS; i++) {
            if (backgrounds[i] != null) {
                try {
                    backgrounds[i].draw(backgroundX[i], (double) lastWindowHeight / 2);
                } catch (Exception e) {
                    System.err.println("Error drawing ending background " + (i+1) + ": " + e.getMessage());
                }
            }
        }
    }
}