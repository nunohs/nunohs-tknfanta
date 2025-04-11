import bagel.*;

import java.io.*;

/**
*  Main Class
 */
public class TKNFanta extends AbstractGame {
    private final static int WINDOW_WIDTH = 1920;
    private final static int WINDOW_HEIGHT = 1080;

    private final static String GAME_TITLE = "TKN FANTA RUNNING";
    private final Image TITLE = new Image("res/asset_Tittle.png");
    private final Image BACKGROUND_IMAGE = new Image("res/asset_background 1.png");
    private final Image BACKGROUND_IMAGE_2 = new Image("res/background_1.png");
    private final Image START_GAME= new Image("res/asset_button 1.png");
    private final Image START_AGAIN = new Image("res/asset_button 3.png");
    private final Image END_GAME = new Image("res/asset_button 5.png");
    private final Image SELECT_CHARACTER = new Image("res/asset_select character text.png");
    private final Image SELECT_PRABOWO = new Image("res/asset_select character 1.png");
    private final Image SELECT_GIBRAN = new Image("res/asset_select character 2.png");
    private final Image GAME_OVER = new Image("res/asset_gameover.png");

    public final static String FONT_FILE = "res/FSO8BITR.TTF";

    private final Font INSTRUCTION_FONT = new Font(FONT_FILE, 24);
    private final Font SCORE_FONT = new Font(FONT_FILE, 30);

    private static final String INSTRUCTIONS2 = "PRESS P";
    private static final String INSTRUCTIONS3 = "PRESS G";

    private final static int TITLE_X = 550;
    private final static int TITLE_Y = 440;
    private final static int INS_X_OFFSET = 190;
    private final static int INS_Y_OFFSET = 110;
    private final static int TITLE_Y_OFFSET = 265;
    private final static int CHARACTER_OFFSET = 160;


    private final Image prabowo;
    private final Image gibran;
    private boolean started = false;
    private boolean select_screen = false;
    private Game currentGame;
    private static int highScore;

    public TKNFanta() {
        super(WINDOW_WIDTH,WINDOW_HEIGHT,GAME_TITLE);
        prabowo = new Image("res/prabowo.png");
        gibran = new Image("res/gibran.png");
        highScore=getHighScore();
    }

    /**
     * The entry point for the program.
     */
    public static void main(String[] args) {
        TKNFanta game = new TKNFanta();
        game.run();
    }

    public static int getHighScore() {
        try (BufferedReader reader = new BufferedReader(new FileReader("highscore.csv"))) {
            String line = reader.readLine();
            if (line != null && !line.isEmpty()) {
                return Integer.parseInt(line);
            }
        } catch (IOException | NumberFormatException e) {
            // Handle exceptions (e.g., file not found, invalid format)
            e.printStackTrace();
        }
        return 0; // Default high score if not found or an error occurs
    }
    public static void setHighScore(int newHighScore) {
        try (PrintWriter writer = new PrintWriter(new FileWriter("highscore.csv"))) {
            writer.println(newHighScore);
        } catch (IOException e) {
            // Handle exceptions (e.g., unable to write to file)
            e.printStackTrace();
        }
    }

    /**
     * Performs a state update.
     */
    @Override
    public void update(Input input){
        BACKGROUND_IMAGE_2.draw((double) Window.getWidth() /2, (double)Window.getHeight()/2);

        if (input.wasPressed(Keys.ESCAPE)) {
            setHighScore(highScore);
            Window.close();
        }
        BACKGROUND_IMAGE.draw((double)Window.getWidth()/2, (double)Window.getHeight()/2);


        if(!started && !select_screen){
            // starting screen
            SCORE_FONT.drawString("SCORE 0000" , (double)Window.getWidth()/2,
                    (double)Window.getHeight()/2-200);
            TITLE.draw((double)Window.getWidth()/2,(double)Window.getHeight()/2-80);


            prabowo.draw(715,(double)WINDOW_HEIGHT/2+92);
            gibran.draw(1230,(double)WINDOW_HEIGHT/2+35);


            START_GAME.draw((double)WINDOW_WIDTH/2,(double)WINDOW_HEIGHT/2+80);
            if(input.wasPressed(MouseButtons.LEFT) || input.wasPressed(Keys.SPACE)){
                select_screen = true;
            }

        }else if (select_screen) {
            SCORE_FONT.drawString("SCORE 0000" , (double)Window.getWidth()/2,
                    (double)Window.getHeight()/2-200);

            SELECT_CHARACTER.draw((double)WINDOW_WIDTH/2,(double)WINDOW_HEIGHT/2-CHARACTER_OFFSET);
            SELECT_PRABOWO.draw((double)WINDOW_WIDTH/2-CHARACTER_OFFSET,(double)WINDOW_HEIGHT/2);
            SELECT_GIBRAN.draw((double)WINDOW_WIDTH/2+CHARACTER_OFFSET,(double)WINDOW_HEIGHT/2);

            INSTRUCTION_FONT.drawString(INSTRUCTIONS2,
                TITLE_X + INS_X_OFFSET, TITLE_Y + TITLE_Y_OFFSET);
            INSTRUCTION_FONT.drawString(INSTRUCTIONS3,
                    (double)WINDOW_WIDTH/2 + 100, TITLE_Y + TITLE_Y_OFFSET);

            if(input.wasPressed(Keys.P) || input.wasPressed(MouseButtons.LEFT) && userClicks(input,"PRABOWO")){
                started=true;
                select_screen = false;
                currentGame = new Game("prabowo");

            } else if (input.wasPressed(Keys.G) || input.wasPressed(MouseButtons.LEFT) &&
                    userClicks(input,"GIBRAN")){
                started=true;
                select_screen = false;
                currentGame = new Game("gibran");
            }

        }else if (currentGame.isFinished()){
            // Finished Screen
            currentGame.drawEnding();
            if(currentGame.getScore() > highScore) {
                highScore = currentGame.getScore() ;
            }

            GAME_OVER.draw((double)WINDOW_WIDTH/2,(double)WINDOW_HEIGHT/2 - INS_Y_OFFSET);
            START_AGAIN.draw(840,(double)WINDOW_HEIGHT/2 );
            END_GAME.draw(1090,(double)WINDOW_HEIGHT/2);

            // Checks whether user wants to restart or end the game.
            if(input.wasPressed(Keys.SPACE) || input.wasPressed(MouseButtons.LEFT)
                    && userClicks(input,"START_AGAIN")){
                started=false;
            } else if (input.wasPressed(MouseButtons.LEFT) && userClicks(input,"END_GAME")) {
                setHighScore(highScore);
                Window.close();
            }
        }
        else{
            // gameplay
            currentGame.update(input);
        }

        // Printing high score across all screens
        if(highScore == 0){
            SCORE_FONT.drawString("HI 0000", (double)Window.getWidth()/2-200,
                    (double)Window.getHeight()/2-200);
        }
        else {
            SCORE_FONT.drawString("HI " + highScore, (double)Window.getWidth() / 2 - 200,
                    (double)Window.getHeight() / 2 - 200);
        }
    }
    /**
     * Checks whether user has clicked a certain button.
     */
    public boolean userClicks(Input input, String title) {
        switch (title) {
            case "START_AGAIN":
                return input.getMouseX() < 954 && input.getMouseX() > 726 &&
                        input.getMouseY() < (double) WINDOW_HEIGHT / 2 + 30
                        && input.getMouseY() > (double) WINDOW_HEIGHT / 2 - 30;
            case "END_GAME":
                return input.getMouseX() < 1194 && input.getMouseX() > 986 &&
                        input.getMouseY() < (double) WINDOW_HEIGHT / 2 + 30
                        && input.getMouseY() > (double) WINDOW_HEIGHT / 2 - 30;
            case "PRABOWO":
                return input.getMouseX() < 920 && input.getMouseX() > 680 &&
                        input.getMouseY() < (double) WINDOW_HEIGHT / 2 + 125
                        && input.getMouseY() > (double) WINDOW_HEIGHT / 2 - 125;
            case "GIBRAN":
                return input.getMouseX() < 1240 && input.getMouseX() > 1000 &&
                        input.getMouseY() < (double) WINDOW_HEIGHT / 2 + 125
                        && input.getMouseY() > (double) WINDOW_HEIGHT / 2 - 125;
        }
        return false;
    }
}
