import bagel.*;
import bagel.util.Point;

/**
* Character class, two options: Prabowo or Gibran.
 */
public class Character {
    private final Image charImage;
    private boolean jumping = false;
    private final double xCoordinate = 282;
    private double yCoordinate;
    private final Point initialPosition;
    private boolean maxJumpUp = false;
    private final int charWidth;
    private final int charHeight;

    public Character(String dir){

        this.charImage = new Image("res/"+dir+".PNG");
        if(dir.equals("prabowo")) {
            this.charWidth = 161;
            this.charHeight = 110;
            this.yCoordinate = (double)Window.getHeight()/ 2+92;
            this.initialPosition = new Point(282,(double)Window.getHeight()/2+92);
        } else{
            this.charWidth = 167;
            this.charHeight = 98;
            this.yCoordinate = (double)Window.getHeight()/ 2+98;
            this.initialPosition = new Point(282,(double)Window.getHeight()/2+98);
        }
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
        if(input.wasPressed(Keys.SPACE) && !jumping){
            jumping=true;
        }
        if(jumping){
            jump();
        }
        else {
            charImage.draw(xCoordinate, yCoordinate);
        }
    }

    /**
     * Makes the character jump when called.
     */
    public void jump(){
        if(!maxJumpUp && getCharCurrentPosition().y >= (initialPosition.y-180)){
            if(getCharCurrentPosition().y <= (initialPosition.y-180)){
                maxJumpUp = true;
            }else {
                yCoordinate -= 2.5;
            }
        }
       if(maxJumpUp){
            if (getCharCurrentPosition().y <= initialPosition.y){
                yCoordinate += 2.5;
            }
            else{
                jumping=false;
                maxJumpUp=false;
            }
        }
        charImage.draw(xCoordinate, yCoordinate);

    }

}
