package features;

import java.util.ArrayList;
import org.jetbrains.annotations.NotNull;

/**
 * Writes a canvas object to a PPM formatted String
 */
public class PPMWriter {
    // Define the maximum line length that the PPM format supports
    private static final int MAX_LINE_LENGTH = 70;

    /**
     * Generates a PPM format string that can be output to a file based on the
     * data in the supplied canvas object
     * @param c The canvas object we want to output
     * @return The list of strings that comprise the canvas data
     */
    public static ArrayList<String> canvas_to_ppm(@NotNull Canvas c) {
        ArrayList<String> ppm = new ArrayList<>();

        // Generate the file header first
        ppm.add("P3");                              // Identify this as a PPMv3 file
        ppm.add(c.getWidth() +" "+ c.getHeight());  // Image width and height
        ppm.add("255");                             // Maximum colour value for each pixel

        // Then, iterate the pixels to generate the rest of the data
        StringBuilder pxDataLine = new StringBuilder(MAX_LINE_LENGTH); // Each line of the data will be buffered here.
        // Data lines should not exceed 70 characters.
        // Furthermore, each row of pixels should begin on a new line...
        int xOffset = 0;
        for (Colour colour: c.getAllPixels()) {
            Double[] components = new Double[] { colour.getR(), colour.getG(), colour.getB() };
            for (double component: components) {
                String intString = String.valueOf(Colour.getClampedComponent(component, 0, 255));

                if (pxDataLine.length() > 0) {
                    pxDataLine.append(" ");
                }

                if (pxDataLine.length() + intString.length() >= MAX_LINE_LENGTH) {
                    ppm.add(pxDataLine.toString());
                    pxDataLine.setLength(0); // Clear the String buffer.
                }

                pxDataLine.append(intString);
            }

            xOffset += 1;

            // If we're at the end of a row of pixels, begin a new line (if we've anything to write)
            if (xOffset >= c.getWidth()) {
                xOffset = 0;

                if (pxDataLine.length() > 0) {
                    ppm.add(pxDataLine.toString());
                    pxDataLine.setLength(0);
                }
            }
        }

        // Add the final line if needed
        if (pxDataLine.length() > 0) {
            ppm.add(pxDataLine.toString());
        }

        return ppm;
    }
}
