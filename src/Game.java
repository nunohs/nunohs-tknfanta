import bagel.*;
import java.util.List;
import java.util.ArrayList;
import java.util.concurrent.ThreadLocalRandom;

/**
*  Class for each Game played by user
 */
public class Game {
    private int currFrame = 0;
    private final MovingBackground background;
    private final Character character;
    private final List<Obstacle> obstacles = new ArrayList<>();
    private int numObstacles=0;
    private final int currObstacle = 0;
    private int speed = 5;
    private int score = 0;
    private boolean finished = false;

    // TODO: change the font later.
    public final static String FONT_FILE = "res/FSO8BITR.TTF";
    private final Font SCORE_FONT = new Font(FONT_FILE, 30);


    public Game(String character){

        this.character =  new Character(character);
        this.background = new MovingBackground();
    }

    public boolean isFinished() {
        return finished;
    }
    public int getScore() {
        return score;
    }

    public void update(Input input){
        currFrame++;
        background.update(speed);
        character.update(input);

        if(currFrame % 260 == 0){
            createObstacles(obstacles);

        }

        for (int i= currObstacle ; i < numObstacles; i++) {
            if(obstacles.get(i).isActive()) {
                if(obstacles.get(i).checkCollision(
                        (int)character.getCharCurrentPosition().x,
                        (int)character.getCharCurrentPosition().y,
                        character.getCharWidth(),
                        character.getCharHeight(),
                        obstacles.get(i).getX(),
                        obstacles.get(i).getY(),
                        obstacles.get(i).getObstacleWidth(),
                        obstacles.get(i).getObstacleHeight())){
                    finished = true;
                    break;
                }
                else{
                    score += obstacles.get(i).update(speed);

                }
            }
        }
        if(currFrame % 15 == 0) {
            score += 1;
        }
        SCORE_FONT.drawString("Score " + score, (double)Window.getWidth()/2,
                (double)Window.getHeight()/2-200);
    }
    public void getGameSpeed(){
        if(numObstacles % 5 == 0 && numObstacles != 0 && speed<40){
            speed++;
        }
    }

    /**
     * Creates a new obstacle
     */
    public void createObstacles(List<Obstacle> obstacles){
        int randomNum = ThreadLocalRandom.current().nextInt(1,7);
        getGameSpeed();
        Obstacle obstacle = new Obstacle("asset_obstacle "+ randomNum);
        obstacles.add(numObstacles,obstacle);
        numObstacles++;
    }

    /**
     * Draws the ending scene, called when the user has hit an obstacle
     */
    public void drawEnding(){
        background.drawEndingBackground();
        SCORE_FONT.drawString("Score " + score, (double)Window.getWidth()/2,
                (double)Window.getHeight()/2-200);
        character.getCharImage().draw(character.getCharCurrentPosition().x,character.getCharCurrentPosition().y);
        for (int i= currObstacle ; i < numObstacles; i++) {
            if (obstacles.get(i).isActive()) {
                obstacles.get(i).getImage().draw(obstacles.get(i).getX(), obstacles.get(i).getY());
            }
        }
    }

}
