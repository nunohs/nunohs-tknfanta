import bagel.*;

/**
*  Moves the distinct backgrounds to the left creating an effect that character is moving
 */
public class MovingBackground {
    private final Image[] backgrounds =  new Image[3];
    private final int[] backgroundX = new int[3];

   public MovingBackground() {
       for (int i = 0; i < 3; i++) {
           this.backgrounds[i] = new Image("res/asset_background " + (i + 1) + ".png");
           this.backgroundX[i] = Window.getWidth()/2 + (i*Window.getWidth());
       }

   }

    public void update(int speed) {
        for (int i = 0; i < 3; i++) {
            backgroundX[i] -= speed;

            // Check if the background is completely off-screen to the left
            if (backgroundX[i] + backgrounds[i].getWidth()/2  < 0) {
                // Move the background to the right of the screen
                backgroundX[i] += 3 * Window.getWidth();
            }

            backgrounds[i].draw(backgroundX[i], (double) Window.getHeight() / 2);

        }
    }

    /**
     * Draws the background at the end of the game
     */
    public void drawEndingBackground(){
        for (int i = 0; i < 3; i++) {
            backgrounds[i].draw(backgroundX[i], (double) Window.getHeight() / 2);
        }
    }
}