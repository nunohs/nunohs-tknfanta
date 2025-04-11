import bagel.*;

/**
 * Class for obstacles, calculating if it collides with the character
 */
public class Obstacle {
    private final Image image;
    private final int y=Window.getHeight()/2+90;
    private int x = Window.getWidth();
    private boolean active;
    private int obstacleWidth;
    private int obstacleHeight;

    public Obstacle(String dir){
        image = new Image("res/"+dir+".png");
        this.active=true;

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

    public int update( int speed) {
        if (active && x>0) {
            x -= speed;
            image.draw(x,y);
            return 0;
        }
        else {
            deactivate();
            return 10;
        }
    }
    /**
     * Checks whether character collides with the obstacle
     */
    public boolean checkCollision(int charXCoordinate, int charYCoordinate,
                                   int charWidth, int charHeight,
                                   int obstacleXCoordinate, int obstacleYCoordinate,
                                   int obstacleWidth, int obstacleHeight) {
        int charHalfWidth = charWidth / 2;
        int charHalfHeight = charHeight / 2;
        int obstacleHalfWidth = obstacleWidth / 2;
        int obstacleHalfHeight = obstacleHeight / 2;

        // Calculate the overlap along the x-axis
        int overlapX = Math.max(0, Math.min(charXCoordinate + charHalfWidth,obstacleXCoordinate + obstacleHalfWidth)
                - Math.max(charXCoordinate- charHalfWidth, obstacleXCoordinate - obstacleHalfWidth));

        // Calculate the overlap along the y-axis
        int overlapY = Math.max(0, Math.min(charYCoordinate + charHalfHeight, obstacleYCoordinate + obstacleHalfHeight)
                - Math.max(charYCoordinate - charHalfHeight, obstacleYCoordinate - obstacleHalfHeight));

        // Calculate the area of overlap (amount of pixels in collision)
        int collisionPixels = overlapX * overlapY;

        // Check if there is a collision (non-zero overlap)
        return collisionPixels > 0;
    }
}
