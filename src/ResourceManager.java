import bagel.*;
import bagel.util.Colour;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.LinkedHashMap;

/**
 * Centralized resource manager to handle loading and caching of game resources
 */
public class ResourceManager {
    private static ResourceManager instance;
    
    private static final int MAX_CACHE_SIZE = 50;
    private static final String FALLBACK_IMAGE_PATH = "res/asset_button 1.png";
    private static final String FALLBACK_FONT_NAME = "Arial";
    
    private final Map<String, Image> imageCache;
    private final Map<String, Font> fontCache;
    private Image fallbackImage;
    private Font fallbackFont;
    private boolean resourcesInitialized = false;
    
    private ResourceManager() {
        imageCache = new LinkedHashMap<>(MAX_CACHE_SIZE, 0.75f, true) {
            @Override
            protected boolean removeEldestEntry(Map.Entry<String, Image> eldest) {
                return size() > MAX_CACHE_SIZE;
            }
        };
        fontCache = new LinkedHashMap<>(MAX_CACHE_SIZE, 0.75f, true) {
            @Override
            protected boolean removeEldestEntry(Map.Entry<String, Font> eldest) {
                return size() > MAX_CACHE_SIZE;
            }
        };
        initializeResources();
    }
    
    private void initializeResources() {
        if (resourcesInitialized) {
            return;
        }

        try {
            // Load fallback resources
            fallbackImage = new Image(FALLBACK_IMAGE_PATH);
            fallbackFont = new Font(FALLBACK_FONT_NAME, 24);
            
            if (fallbackImage == null || fallbackFont == null) {
                System.err.println("Warning: Failed to load fallback resources");
            }
            
            resourcesInitialized = true;
        } catch (Exception e) {
            System.err.println("Error initializing resources: " + e.getMessage());
        }
    }
    
    /**
     * Get the singleton instance
     */
    public static ResourceManager getInstance() {
        if (instance == null) {
            instance = new ResourceManager();
        }
        return instance;
    }
    
    /**
     * Get an image, loading it if not already cached
     * Returns a fallback image if loading fails
     */
    public Image getImage(String path) {
        if (path == null || path.trim().isEmpty()) {
            System.err.println("Warning: Attempted to load image with null or empty path");
            return fallbackImage;
        }

        return imageCache.computeIfAbsent(path, k -> {
            try {
                Image image = new Image(k);
                return image != null ? image : fallbackImage;
            } catch (Exception e) {
                System.err.println("Failed to load image: " + k + " - " + e.getMessage());
                return fallbackImage;
            }
        });
    }
    
    /**
     * Get a font, loading it if not already cached
     * Returns a fallback font if loading fails
     */
    public Font getFont(String path, int size) {
        if (path == null || path.trim().isEmpty()) {
            System.err.println("Warning: Attempted to load font with null or empty path");
            return fallbackFont;
        }

        String cacheKey = path + "_" + size;
        return fontCache.computeIfAbsent(cacheKey, k -> {
            try {
                Font font = new Font(path, size);
                return font != null ? font : fallbackFont;
            } catch (Exception e) {
                System.err.println("Failed to load font: " + path + " - " + e.getMessage());
                return fallbackFont;
            }
        });
    }
    
    /**
     * Clear all cached resources to free memory
     */
    public void clearResources() {
        clearImageCache();
        clearFontCache();
    }
    
    /**
     * Clear image cache
     */
    public void clearImageCache() {
        imageCache.clear();
    }
    
    /**
     * Clear font cache
     */
    public void clearFontCache() {
        fontCache.clear();
    }
    
    /**
     * Preload commonly used resources
     */
    public void preloadResources(String[] imagePaths, String[] fontPaths, int[] fontSizes) {
        if (!resourcesInitialized) {
            initializeResources();
        }

        if (imagePaths != null) {
            for (String path : imagePaths) {
                getImage(path);
            }
        }

        if (fontPaths != null && fontSizes != null) {
            for (int i = 0; i < Math.min(fontPaths.length, fontSizes.length); i++) {
                getFont(fontPaths[i], fontSizes[i]);
            }
        }
    }
} 