import bagel.*;

import java.io.*;

/**
*  Main Class
 */
public class TKNFanta extends AbstractGame {
    // Game states enum for better state management
    private enum GameState {
        START_SCREEN,
        CHARACTER_SELECT,
        GAMEPLAY,
        GAME_OVER
    }
    
    private final static int WINDOW_WIDTH = 1920;
    private final static int WINDOW_HEIGHT = 1080;

    private final static String GAME_TITLE = "TKN FANTA RUNNING";
    public final static String FONT_FILE = "res/FSO8BITR.TTF";
    private final static String HIGH_SCORE_FILE = "highscore.csv";
    
    // Resource paths
    private static final String TITLE_IMAGE_PATH = "res/asset_Tittle.png";
    private static final String BACKGROUND_IMAGE_PATH = "res/asset_background 1.png";
    private static final String BACKGROUND_IMAGE_2_PATH = "res/background_1.png";
    private static final String START_GAME_PATH = "res/asset_button 1.png";
    private static final String START_AGAIN_PATH = "res/asset_button 3.png";
    private static final String END_GAME_PATH = "res/asset_button 5.png";
    private static final String SELECT_CHARACTER_PATH = "res/asset_select character text.png";
    private static final String SELECT_PRABOWO_PATH = "res/asset_select character 1.png";
    private static final String SELECT_GIBRAN_PATH = "res/asset_select character 2.png";
    private static final String GAME_OVER_PATH = "res/asset_gameover.png";
    private static final String PRABOWO_PATH = "res/prabowo.png";
    private static final String GIBRAN_PATH = "res/gibran.png";
    
    // ResourceManager instance
    private final ResourceManager resourceManager = ResourceManager.getInstance();

    private final Font instructionFont;
    private final Font scoreFont;

    private static final String INSTRUCTIONS2 = "PRESS P";
    private static final String INSTRUCTIONS3 = "PRESS G";

    private final static int TITLE_X = 550;
    private final static int TITLE_Y = 440;
    private final static int INS_X_OFFSET = 190;
    private final static int INS_Y_OFFSET = 110;
    private final static int TITLE_Y_OFFSET = 265;
    private final static int CHARACTER_OFFSET = 160;

    private GameState currentState = GameState.START_SCREEN;
    private Game currentGame;
    private static int highScore;
    private boolean isGameInitialized = false;
    private final AudioManager audioManager = AudioManager.getInstance();
    
    // Track resource loading status
    private boolean fontsLoaded = true;

    public TKNFanta() {
        super(WINDOW_WIDTH, WINDOW_HEIGHT, GAME_TITLE);
        
        // Load fonts with error handling
        Font instructFont = resourceManager.getFont(FONT_FILE, 24);
        Font scoreTextFont = resourceManager.getFont(FONT_FILE, 30);
        
        if (instructFont == null || scoreTextFont == null) {
            System.err.println("Warning: Failed to load game fonts");
            fontsLoaded = false;
        }
        
        this.instructionFont = instructFont;
        this.scoreFont = scoreTextFont;
        
        // Initialize highscore
        highScore = getHighScore();
        
        // Start home screen music
        audioManager.playHomeMusic();
    }

    /**
     * The entry point for the program.
     */
    public static void main(String[] args) {
        TKNFanta game = new TKNFanta();
        game.run();
    }

    public static int getHighScore() {
        // Get the absolute file path in the application's directory
        String filePath = System.getProperty("user.dir") + File.separator + HIGH_SCORE_FILE;
        File file = new File(filePath);
        
        // If file doesn't exist, create it
        if (!file.exists()) {
            try {
                file.createNewFile();
                return 0; // New file, so start with 0
            } catch (IOException e) {
                System.err.println("Error creating highscore file: " + e.getMessage());
                return 0; // Default to 0 if file creation failed
            }
        }
        
        // Try to read existing highscore
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line = reader.readLine();
            if (line != null && !line.isEmpty()) {
                try {
                    return Integer.parseInt(line.trim());
                } catch (NumberFormatException e) {
                    System.err.println("Invalid highscore format, resetting to 0");
                    return 0;
                }
            }
        } catch (IOException e) {
            System.err.println("Error reading highscore file: " + e.getMessage());
        }
        return 0; // Default high score if not found or an error occurs
    }
    
    public static void setHighScore(int newHighScore) {
        // Get the absolute file path in the application's directory
        String filePath = System.getProperty("user.dir") + File.separator + HIGH_SCORE_FILE;
        File file = new File(filePath);
        
        try (PrintWriter writer = new PrintWriter(new FileWriter(file))) {
            writer.println(newHighScore);
        } catch (IOException e) {
            System.err.println("Error saving highscore: " + e.getMessage());
        }
    }

    /**
     * Safely draws an image at the specified location, with null checks
     */
    private void safeDrawImage(String path, double x, double y) {
        Image img = resourceManager.getImage(path);
        if (img != null) {
            img.draw(x, y);
        }
    }
    
    /**
     * Initialize game components
     */
    private void initializeGame(String character) {
        if (isGameInitialized) {
            cleanup();
        }
        
        try {
            currentGame = new Game(character);
            isGameInitialized = true;
        } catch (Exception e) {
            System.err.println("Failed to initialize game: " + e.getMessage());
            currentState = GameState.START_SCREEN;
        }
    }
    
    /**
     * Clean up game resources
     */
    private void cleanup() {
        if (currentGame != null) {
            currentGame = null;
        }
        isGameInitialized = false;
    }

    /**
     * Performs a state update.
     */
    @Override
    public void update(Input input) {
        if (input == null) {
            System.err.println("Warning: Input is null in TKNFanta.update()");
            return;
        }
        
        // Draw background
        safeDrawImage(BACKGROUND_IMAGE_2_PATH, (double) Window.getWidth() / 2, (double) Window.getHeight() / 2);

        if (input.wasPressed(Keys.ESCAPE)) {
            setHighScore(highScore);
            Window.close();
        }
        
        safeDrawImage(BACKGROUND_IMAGE_PATH, (double) Window.getWidth() / 2, (double) Window.getHeight() / 2);

        switch (currentState) {
            case START_SCREEN:
                updateStartScreen(input);
                break;
            case CHARACTER_SELECT:
                updateCharacterSelect(input);
                break;
            case GAMEPLAY:
                if (!isGameInitialized) {
                    System.err.println("Error: Game not initialized");
                    currentState = GameState.START_SCREEN;
                    break;
                }
                updateGameplay(input);
                break;
            case GAME_OVER:
                updateGameOver(input);
                break;
        }

        // Printing high score across all screens
        if (scoreFont != null) {
            if(highScore == 0) {
                scoreFont.drawString("HI 0000", (double)Window.getWidth()/2-200,
                        (double)Window.getHeight()/2-200);
            } else {
                scoreFont.drawString("HI " + highScore, (double)Window.getWidth() / 2 - 200,
                        (double)Window.getHeight() / 2 - 200);
            }
        }
    }
    
    /**
     * Updates the start screen
     */
    private void updateStartScreen(Input input) {
            // starting screen
        if (scoreFont != null) {
            scoreFont.drawString("SCORE 0000" , (double)Window.getWidth()/2,
                    (double)Window.getHeight()/2-200);
        }
        
        safeDrawImage(TITLE_IMAGE_PATH, (double)Window.getWidth()/2, (double)Window.getHeight()/2-80);
        safeDrawImage(PRABOWO_PATH, 715, (double)WINDOW_HEIGHT/2+92);
        safeDrawImage(GIBRAN_PATH, 1230, (double)WINDOW_HEIGHT/2+35);
        safeDrawImage(START_GAME_PATH, (double)WINDOW_WIDTH/2, (double)WINDOW_HEIGHT/2+80);
        
        if((input.wasPressed(MouseButtons.LEFT) || input.wasPressed(Keys.SPACE))){
            currentState = GameState.CHARACTER_SELECT;
            }
    }
    
    /**
     * Updates the character selection screen
     */
    private void updateCharacterSelect(Input input) {
        if (scoreFont != null) {
            scoreFont.drawString("SCORE 0000" , (double)Window.getWidth()/2,
                    (double)Window.getHeight()/2-200);
        }

        safeDrawImage(SELECT_CHARACTER_PATH, (double)WINDOW_WIDTH/2, (double)WINDOW_HEIGHT/2-CHARACTER_OFFSET);
        safeDrawImage(SELECT_PRABOWO_PATH, (double)WINDOW_WIDTH/2-CHARACTER_OFFSET, (double)WINDOW_HEIGHT/2);
        safeDrawImage(SELECT_GIBRAN_PATH, (double)WINDOW_WIDTH/2+CHARACTER_OFFSET, (double)WINDOW_HEIGHT/2);

        if (instructionFont != null) {
            instructionFont.drawString(INSTRUCTIONS2,
                TITLE_X + INS_X_OFFSET, TITLE_Y + TITLE_Y_OFFSET);
            instructionFont.drawString(INSTRUCTIONS3,
                    (double)WINDOW_WIDTH/2 + 100, TITLE_Y + TITLE_Y_OFFSET);
        }

        if((input.wasPressed(Keys.P)) || (input.wasPressed(MouseButtons.LEFT) && userClicks(input,"PRABOWO"))) {
            currentState = GameState.GAMEPLAY;
            initializeGame("prabowo");
            audioManager.playGameplayMusic();
        } else if ((input.wasPressed(Keys.G)) || (input.wasPressed(MouseButtons.LEFT) &&
                userClicks(input,"GIBRAN"))) {
            currentState = GameState.GAMEPLAY;
            initializeGame("gibran");
            audioManager.playGameplayMusic();
            }
    }
    
    /**
     * Updates the gameplay screen
     */
    private void updateGameplay(Input input) {
        if (currentGame != null) {
            // gameplay
            currentGame.update(input);
            
            if (currentGame.isFinished()) {
                currentState = GameState.GAME_OVER;
                if(currentGame.getScore() > highScore) {
                    highScore = currentGame.getScore();
                    setHighScore(highScore); // Save high score immediately
                }
                audioManager.playHomeMusic();
            }
        } else {
            // Handle error case if game creation failed
            System.err.println("Error: Current game is null");
            currentState = GameState.START_SCREEN;
            audioManager.playHomeMusic();
        }
    }
    
    /**
     * Updates the game over screen
     */
    private void updateGameOver(Input input) {
        // Finished Screen
        if (currentGame != null) {
            currentGame.drawEnding();
        }

        safeDrawImage(GAME_OVER_PATH, (double)WINDOW_WIDTH/2, (double)WINDOW_HEIGHT/2 - INS_Y_OFFSET);
        safeDrawImage(START_AGAIN_PATH, 840, (double)WINDOW_HEIGHT/2);
        safeDrawImage(END_GAME_PATH, 1090, (double)WINDOW_HEIGHT/2);

        // Checks whether user wants to restart or end the game.
        if((input.wasPressed(Keys.SPACE)) || (input.wasPressed(MouseButtons.LEFT)
                && userClicks(input,"START_AGAIN"))) {
            cleanup();
            currentState = GameState.START_SCREEN;
        } else if (input.wasPressed(MouseButtons.LEFT) && userClicks(input,"END_GAME")) {
            setHighScore(highScore);
            audioManager.cleanup();
            Window.close();
        }
    }

    /**
     * Checks whether user has clicked a certain button.
     */
    public boolean userClicks(Input input, String title) {
        if (input == null || title == null) {
            return false;
        }
        
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
            default:
                return false;
        }
    }
}
