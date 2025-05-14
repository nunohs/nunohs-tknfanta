import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;

/**
 * Manages all game audio including background music and sound effects
 */
public class AudioManager {
    private static AudioManager instance;
    
    // Audio file paths
    private static final String HOME_MUSIC_PATH = "res/609209__kjartan_abel__crossed-path-a-lo-fi-instrumental-hip-hop-track.wav";
    private static final String BOOST_SOUND_PATH = "res/585803__colorscrimsontears__power-boost.wav";
    
    // Audio clips
    private Clip homeMusic;
    private Clip boostSound;
    
    private boolean isHomeMusicPlaying = false;
    
    private AudioManager() {
        initializeAudio();
    }
    
    public static AudioManager getInstance() {
        if (instance == null) {
            instance = new AudioManager();
        }
        return instance;
    }
    
    private void initializeAudio() {
        try {
            // Initialize home music
            AudioInputStream homeStream = AudioSystem.getAudioInputStream(new File(HOME_MUSIC_PATH));
            homeMusic = AudioSystem.getClip();
            homeMusic.open(homeStream);
            homeMusic.loop(Clip.LOOP_CONTINUOUSLY);
            
            // Initialize boost sound
            AudioInputStream boostStream = AudioSystem.getAudioInputStream(new File(BOOST_SOUND_PATH));
            boostSound = AudioSystem.getClip();
            boostSound.open(boostStream);
            
        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            System.err.println("Error initializing audio: " + e.getMessage());
        }
    }
    
    /**
     * Start playing home screen music
     */
    public void playHomeMusic() {
        if (homeMusic != null && !isHomeMusicPlaying) {
            homeMusic.setFramePosition(0);
            homeMusic.start();
            isHomeMusicPlaying = true;
        }
    }
    
    /**
     * Play the boost sound effect
     */
    public void playBoostSound() {
        if (boostSound != null) {
            boostSound.setFramePosition(0);
            boostSound.start();
        }
    }
    
    /**
     * Stop home screen music
     */
    public void stopHomeMusic() {
        if (homeMusic != null && isHomeMusicPlaying) {
            homeMusic.stop();
            isHomeMusicPlaying = false;
        }
    }
    
    /**
     * Stop all music
     */
    public void stopAllMusic() {
        stopHomeMusic();
    }
    
    /**
     * Clean up audio resources
     */
    public void cleanup() {
        stopAllMusic();
        if (homeMusic != null) homeMusic.close();
        if (boostSound != null) boostSound.close();
    }
} 