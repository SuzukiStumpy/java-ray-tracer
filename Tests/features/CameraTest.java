package features;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CameraTest {
    public static double EPSILON = 0.00001;

    @Test
    void testCameraConstruction() {
        int hSize = 160;
        int vSize = 120;
        double fov = Math.PI / 2;

        Camera c = new Camera(hSize, vSize, fov);

        assertEquals(160, c.getHSize());
        assertEquals(120, c.getVSize());
        assertEquals(Math.PI/2, c.getFOV());
        assertEquals(Matrix.identity(4), c.getTransform());
    }

    @Test
    void testPixelSizeForHorizontalCanvas() {
        Camera c = new Camera(200, 125, Math.PI / 2);
        assertEquals(0.01, c.getPixelSize(), EPSILON);
    }

    @Test
    void testPixelSizeForVerticalCanvas() {
        Camera c = new Camera(125, 200, Math.PI/2);
        assertEquals(0.01, c.getPixelSize(), EPSILON);
    }

    @Test
    void testConstructRayThroughCanvasCentre() {
        Camera c = new Camera(201, 101, Math.PI / 2);
        Ray r = c.rayForPixel(100, 50);
        assertEquals(new Point(0,0,0), r.getOrigin());
        assertEquals(new Vector(0,0,-1), r.getDirection());
    }

    @Test
    void testConstructRayThroughCanvasCorner() {
        Camera c = new Camera(201, 101, Math.PI / 2);
        Ray r = c.rayForPixel(0, 0);
        assertEquals(new Point(0,0,0), r.getOrigin());
        assertEquals(new Vector(0.66519,0.33259,-0.66851), r.getDirection());
    }

    @Test
    void testConstructRayWhenCameraIsTransformed() {
        Camera c = new Camera(201, 101, Math.PI/2);
        c.setTransform(Matrix.rotation_y(Math.PI/4).multiply(Matrix.translation(0,-2, 5)));
        Ray r = c.rayForPixel(100, 50);
        assertEquals(new Point(0, 2,-5), r.getOrigin());
        assertEquals(new Vector(Math.sqrt(2)/2, 0, -Math.sqrt(2)/2), r.getDirection());
    }

    @Test
    void testCrudeRenderWorldWithCameraTest() {
        World w = World.defaultWorld();
        Camera c = new Camera(11, 11, Math.PI / 2);
        Point from = new Point(0, 0, -5);
        Point to = new Point(0,0,0);
        Vector up = new Vector(0, 1, 0);
        c.setTransform(World.view_transform(from, to, up));
        Canvas image = c.render(w);
        assertEquals(new Colour(0.38066, 0.47583, 0.2855), image.getPixel(5, 5));
    }
}
