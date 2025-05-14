import bagel.*;

/**
 * Class to handle the speed boost effect animation
 */
public class SpeedBoost {
    private static final String BOOST_IMAGE_PATH = "res/boost.png";
    private static final int BOOST_DURATION = 100; // Increased duration for better visibility
    private static final double BOOST_SCALE = 0.45; // Adjusted scale to match the image size
    
    private final Image boostImage;
    private int boostTimer = 0;
    private boolean isActive = false;
    private final ResourceManager resourceManager = ResourceManager.getInstance();
    private final AudioManager audioManager = AudioManager.getInstance();
    
    public SpeedBoost() {
        // Load the boost image
        Image img = resourceManager.getImage(BOOST_IMAGE_PATH);
        if (img == null) {
            System.err.println("Warning: Failed to load speed boost image");
        }
        this.boostImage = img;
    }
    
    /**
     * Activate the speed boost effect
     */
    public void activate() {
        if (!isActive) {
            isActive = true;
            boostTimer = BOOST_DURATION;
            audioManager.playBoostSound();
        }
    }
    
    /**
     * Update and draw the speed boost effect
     */
    public void update(double x, double y) {
        if (!isActive || boostImage == null) {
            return;
        }
        
        // Draw the boost effect behind the character
        if (boostTimer > 0) {
            // Calculate opacity based on remaining time
            double opacity = (double) boostTimer / BOOST_DURATION;
            
            // Draw the boost effect at the back of the car
            // Position it behind the car by offsetting it to the left
            boostImage.draw(x - 100, y + 20, new DrawOptions()
                .setScale(BOOST_SCALE, BOOST_SCALE)
                .setBlendColour(1, 1, 1, opacity));
            
            boostTimer--;
        } else {
            isActive = false;
        }
    }
    
    /**
     * Check if the boost effect is currently active
     */
    public boolean isActive() {
        return isActive;
    }
} 