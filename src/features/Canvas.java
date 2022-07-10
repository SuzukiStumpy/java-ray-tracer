package features;

import org.jetbrains.annotations.NotNull;

import java.util.Arrays;

/**
 * A Canvas is simply a rectangular grid of pixels, each of which has an
 * associated colour.
 */
public class Canvas {
    // Simple array of pixels.  When the object is instantiated, then the
    // Array bounds will be set.
    private final Colour[] pixelData;
    private final int width;
    private final int height;

    /**
     * Constructor for a canvas object.  Generates the canvas at the required
     * dimensions and sets all pixels to black.
     * @param width The width of the canvas in pixels
     * @param height The height of the canvas in pixels
     */
    public Canvas(int width, int height) throws RuntimeException {
        if (width <= 0 || height <= 0) {
            throw new RuntimeException("Cannot have a canvas with zero or lower dimension");
        }

        this.width = width;
        this.height = height;
        this.pixelData = new Colour[width * height];

        for (int i = 0; i < pixelData.length; i++) {
            pixelData[i] = new Colour(0,0,0);
        }
    }

    /**
     * @return The width of the canvas
     */
    public int getWidth() {
        return width;
    }

    /**
     * @return The height of the canvas
     */
    public int getHeight() {
        return height;
    }

    /**
     * @return A 1D array of pixels that can be iterated through
     */
    public Colour[] getAllPixels() {
        return this.pixelData;
    }

    /**
     * Set the pixel at the coordinates (x,y) to the colour c
     * @param x The x coordinate of the pixel
     * @param y The y coordinate of the pixel
     * @param c The colour to set
     */
    public void setPixel(int x, int y, @NotNull Colour c) {
        try {
            int index = getPixelIndex(x, y);
            pixelData[index] = new Colour(c.getR(), c.getG(), c.getB());
        }
        catch (IndexOutOfBoundsException e) {
            System.err.println(e.getMessage());
        }
    }

    public void setAllPixels(@NotNull Colour c) {
        Arrays.fill(pixelData, c);
    }

    /**
     * Get the colour at coordinates (x,y).
     * @param x The x coordinate of the pixel
     * @param y The y coordinate of the pixel
     * @return The colour of the pixel at the given coordinates (or null if pixel is off the canvas)
     */
    public Colour getPixel(int x, int y) {
        try {
            int index = getPixelIndex(x, y);
            Colour pixelColour = pixelData[index];
            return new Colour(pixelColour.getR(), pixelColour.getG(), pixelColour.getB());
        }
        catch (IndexOutOfBoundsException e) {
            System.err.println(e.getMessage());
            return null;
        }
    }

    /**
     * Get the index into the pixel array of the given coordinate.
     * @throws IndexOutOfBoundsException if the coordinate falls outside the defined
     * canvas area.
     * @param x The x component
     * @param y The y component
     * @return The appropriate array index
     */
    private int getPixelIndex(int x, int y) throws IndexOutOfBoundsException {
        if (x < 0 || x >= width || y < 0 || y >= height) {
            throw new IndexOutOfBoundsException("Pixel ("+ x +", "+ y +") falls outside of the defined canvas area");
        }
        return (x + (width * y));
    }
}
