import bagel.*;

/**
 * Class for obstacles, calculating if it collides with the character
 */
public class Obstacle {
    // Collision forgiveness factor - smaller number means more forgiving (0.8 = 80% of original hitbox)
    private static final double COLLISION_FORGIVENESS = 0.75;
    
    // Standard resource paths with consistent naming
    private static final String RESOURCE_PATH = "res/";
    
    private final Image image;
    private final int y=Window.getHeight()/2+90;
    private int x = Window.getWidth();
    private boolean active;
    private int obstacleWidth;
    private int obstacleHeight;
    private final ResourceManager resourceManager = ResourceManager.getInstance();

    public Obstacle(String dir){
        // Use ResourceManager to load image with error handling
        Image img = null;
        try {
            img = resourceManager.getImage(RESOURCE_PATH + dir + ".png");
            if (img == null) {
                System.err.println("Warning: Failed to load obstacle image: " + dir);
            }
        } catch (Exception e) {
            System.err.println("Error loading obstacle image: " + e.getMessage());
        }
        
        this.image = img;
        this.active = true;

        switch (dir) {
            case "asset_obstacle 1":
                this.obstacleWidth = 65;
                this.obstacleHeight = 95;
                break;
            case "asset_obstacle 2":
                this.obstacleWidth = 40;
                this.obstacleHeight = 60;
                break;
            case "asset_obstacle 3":
                this.obstacleWidth = 45;
                this.obstacleHeight = 40;
                break;
            case "asset_obstacle 4":
                this.obstacleWidth = 50;
                this.obstacleHeight = 90;
                break;
            case "asset_obstacle 5":
                this.obstacleWidth = 32;
                this.obstacleHeight = 55;
                break;
            case "asset_obstacle 6":
                this.obstacleWidth = 90;
                this.obstacleHeight = 60;
                break;
            default:
                this.obstacleWidth = 50;
                this.obstacleHeight = 50;
                System.err.println("Warning: Unknown obstacle type: " + dir);
                break;
        }
    }

    public int getY() {
        return y;
    }

    public int getX() {
        return x;
    }

    public int getObstacleWidth() {
        return obstacleWidth;
    }

    public int getObstacleHeight() {
        return obstacleHeight;
    }

    public boolean isActive() {
        return active;
    }

    public Image getImage() {
        return image;
    }

    public void deactivate() {
        active = false;
    }

    /**
     * Check if the character collides with this obstacle
     * Uses a forgiving hitbox for better gameplay experience
     */
    public boolean checkCollision(int charX, int charY, int charWidth, int charHeight,
                                int obstacleX, int obstacleY, int obstacleWidth, int obstacleHeight) {
        if (!active || image == null) {
            return false;
        }

        // Calculate hitbox with forgiveness factor
        double hitboxWidth = obstacleWidth * COLLISION_FORGIVENESS;
        double hitboxHeight = obstacleHeight * COLLISION_FORGIVENESS;
        
        // Calculate hitbox center
        double hitboxCenterX = obstacleX;
        double hitboxCenterY = obstacleY;
        
        // Calculate character center
        double charCenterX = charX;
        double charCenterY = charY;
        
        // Calculate half-widths and half-heights
        double hitboxHalfWidth = hitboxWidth / 2;
        double hitboxHalfHeight = hitboxHeight / 2;
        double charHalfWidth = charWidth / 2;
        double charHalfHeight = charHeight / 2;
        
        // Check for collision using center points and half-dimensions
        boolean collisionX = Math.abs(charCenterX - hitboxCenterX) < (charHalfWidth + hitboxHalfWidth);
        boolean collisionY = Math.abs(charCenterY - hitboxCenterY) < (charHalfHeight + hitboxHalfHeight);
        
        return collisionX && collisionY;
    }

    public int update(int speed) {
        if (!active || image == null) {
            return 0;
        }
        
        if (x > 0) {
            x -= speed;
            try {
                image.draw(x, y);
            } catch (Exception e) {
                System.err.println("Error drawing obstacle: " + e.getMessage());
                deactivate();
                return 0;
            }
            return 0;
        } else {
            deactivate();
            return 10;
        }
    }
}
