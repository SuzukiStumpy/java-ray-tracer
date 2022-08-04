package features;

import objects.Group;
import objects.Shape;
import objects.Triangle;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.Test;
import org.junit.platform.engine.support.hierarchical.OpenTest4JAwareThrowableCollector;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class ObjParserTest {
    @Test
    void testParserIgnoresUnrecognisedLines() {
        String junk = "There was a young lady named Bright,\n" +
            "who traveled much faster than light.\n" +
            "She set out one day\n" +
            "In a relative way\n" +
            "And came back the previous night";

        ObjParser parser = new ObjParser();

        assertEquals(5, parser.parse(junk));
    }

    @Test
    void testProcessingVertexRecords() {
        String file = "v -1 1 0\n" +
            "v -1.0000 0.5000 0.0000\n" +
            "v 1 0 0\n" +
            "v 1 1 0";

        ObjParser parser = new ObjParser();
        parser.parse(file);

        assertEquals(new Point(-1, 1, 0), parser.verts(1));
        assertEquals(new Point(-1, 0.5, 0), parser.verts(2));
        assertEquals(new Point(1,0,0), parser.verts(3));
        assertEquals(new Point(1,1,0), parser.verts(4));
    }

    @Test
    void testParserWithTriangleData() {
        String file = "v -1 1 0\n"+
            "v -1 0 0\n"+
            "v 1 0 0\n"+
            "v 1 1 0\n"+
            "\n"+
            "f 1 2 3\n"+
            "f 1 3 4";

        ObjParser parser = new ObjParser();
        parser.parse(file);

        Group g = parser.defaultGroup();
        Triangle t1 = (Triangle) g.contents().get(0);
        Triangle t2 = (Triangle) g.contents().get(1);

        assertEquals(parser.verts(1), t1.point(0));
        assertEquals(parser.verts(2), t1.point(1));
        assertEquals(parser.verts(3), t1.point(2));
        assertEquals(parser.verts(1), t2.point(0));
        assertEquals(parser.verts(3), t2.point(1));
        assertEquals(parser.verts(4), t2.point(2));
    }

    @Test
    void testParserTriangulatesPolygonData() {
        String file = "v -1 1 0\n" +
            "v -1 0 0\n"+
            "v 1 0 0\n"+
            "v 1 1 0\n"+
            "v 0 2 0\n"+
            "\n"+
            "f 1 2 3 4 5";

        ObjParser parser = new ObjParser();
        parser.parse(file);

        Group g = parser.defaultGroup();
        Triangle t1 = (Triangle) g.contents().get(0);
        Triangle t2 = (Triangle) g.contents().get(1);
        Triangle t3 = (Triangle) g.contents().get(2);

        assertEquals(parser.verts(1), t1.point(0));
        assertEquals(parser.verts(2), t1.point(1));
        assertEquals(parser.verts(3), t1.point(2));
        assertEquals(parser.verts(1), t2.point(0));
        assertEquals(parser.verts(3), t2.point(1));
        assertEquals(parser.verts(4), t2.point(2));
        assertEquals(parser.verts(1), t3.point(0));
        assertEquals(parser.verts(4), t3.point(1));
        assertEquals(parser.verts(5), t3.point(2));
    }

    @Test
    void testSupportForNamedGroupsInFile() {
        String file = trianglesObj();

        ObjParser parser = new ObjParser();
        parser.parse(file);

        Group g1 = parser.group("FirstGroup");
        Group g2 = parser.group("SecondGroup");
        Triangle t1 = (Triangle) g1.contents().get(0);
        Triangle t2 = (Triangle) g2.contents().get(0);

        assertEquals(parser.verts(1), t1.point(0));
        assertEquals(parser.verts(2), t1.point(1));
        assertEquals(parser.verts(3), t1.point(2));
        assertEquals(parser.verts(1), t2.point(0));
        assertEquals(parser.verts(3), t2.point(1));
        assertEquals(parser.verts(4), t2.point(2));
    }

    @Test
    void testFinalConversionOfObjModelToGroup() {
        String file = trianglesObj();

        ObjParser parser = new ObjParser();
        parser.parse(file);

        Group g = parser.getModel();
        Group g1 = parser.group("FirstGroup");
        Group g2 = parser.group("SecondGroup");

        ArrayList<Shape> al = g.contents();

        assertTrue(al.contains(g1));
        assertTrue(al.contains(g2));
    }

    @Contract(pure = true)
    private @NotNull String trianglesObj() {
        return "v -1 1 0\n"+
            "v -1 0 0\n"+
            "v 1 0 0\n"+
            "v 1 1 0\n"+
            "\n"+
            "g FirstGroup\n"+
            "f 1 2 3\n"+
            "g SecondGroup\n"+
            "f 1 3 4";
    }
}
