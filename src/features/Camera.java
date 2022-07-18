package features;

import features.Matrix;
import org.jetbrains.annotations.NotNull;

/**
 * Models a camera within the world, which we'll use to view and render the
 * scene
 *
 * @author Mark Edwards
 * @version July 18th, 2022
 */
public class Camera {
    private int hsize;
    private int vsize;
    private double fov;
    Matrix transform;

    // Private variables used in calculating the aspect ratio and pixel sizes
    // stored within the camera object so they can be reused for ray computations
    private double half_view;
    private double half_width;
    private double half_height;
    private double aspect;
    private double pixel_size;

    /**
     * Construct a new camera with the following canvas and view factors.
     * Also sets a default transform of the identity matrix for the camera
     * @param horiz Horizontal width of the canvas (in pixels)
     * @param vert Vertical height of the canvas (in pixels)
     * @param fov The field of view (In Radians)
     */
    public Camera(int horiz, int vert, double fov) {
        hsize = horiz;
        vsize = vert;
        this.fov = fov;
        transform = Matrix.identity(4);

        computeAspect();
    }

    /**
     * @return Gets the horizontal width of the canvas
     */
    public int getHSize() {
        return hsize;
    }

    /**
     * @return gets the vertical height of the canvas
     */
    public int getVSize() {
        return vsize;
    }

    /**
     * @return gets the Field of View in Radians
     */
    public double getFOV() {
        return fov;
    }

    /**
     * @return Gets the current camera's transformation matrix
     */
    public Matrix getTransform() {
        return new Matrix(transform);
    }

    /**
     * Sets the transformation matrix for the camera
     * @param t The new transformation matrix
     */
    public void setTransform(@NotNull Matrix t) {
        transform = new Matrix(t);
    }

    /**
     * @return The size of a pixel on the canvas in world-space terms
     */
    public double getPixelSize() {
        return pixel_size;
    }

    /**
     * Calculates the aspect ratio along with the half_* member variables and
     * pixel size
     */
    private void computeAspect() {
        half_view = Math.tan(fov / 2);
        aspect = (double)hsize / (double)vsize;

        if (aspect >= 1) {
            half_width = half_view;
            half_height = half_view / aspect;
        } else {
            half_width = half_view * aspect;
            half_height = half_view;
        }

        pixel_size = (half_width * 2) / hsize;
    }

    /**
     * Returns a ray that passes from the camera through the canvas pixel with
     * the given x,y coordinates
     * @param px The x coordinate of the pixel we want to pass through
     * @param py The y coordinate of the pixel we want to pass through
     * @return The computed ray
     */
    public Ray rayForPixel(int px, int py) {
        // Offset from the edge of canvas to the pixel's centre
        double x_offset = (px + 0.5) * pixel_size;
        double y_offset = (py + 0.5) * pixel_size;

        // Untransformed coordinates of the pixel in world space
        double world_x = half_width - x_offset;
        double world_y = half_height - y_offset;

        // Transform the canvas point with the camera transform and the origin
        // Then compute the ray's direction
        Point pixel = transform.inverse().multiply(new Point(world_x, world_y, -1)).toPoint();
        Point origin = transform.inverse().multiply(new Point(0,0,0)).toPoint();
        Vector direction = pixel.subtract(origin).normalize();

        return new Ray(origin, direction);
    }

    /**
     * Renders the scene specified in world
     * @param world The scene we're rendering
     * @return The rendered image canvas
     */
    public Canvas render(@NotNull World world) {
        Canvas image = new Canvas(hsize, vsize);

        for (int row = 0; row < vsize; row++) {
            for (int col = 0; col < hsize; col++) {
                Ray r = rayForPixel(col, row);
                Colour c = world.colourAt(r);
                image.setPixel(col, row, c);
            }
        }

        return image;
    }
}
