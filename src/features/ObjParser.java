package features;

import objects.Group;
import objects.Triangle;
import org.jetbrains.annotations.NotNull;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;

/**
 * Parses and loads a WavefrontOBJ format file into the renderer and passes
 * back the parsed file as a group of triangles so we can render it.
 *
 * TODO: Make more efficient by converting to a triangle mesh rather than a
 *       simple group of triangle objects.
 *
 * @author Mark Edwards
 * @version August 3rd, 2022
 */
public class ObjParser {
    private final ArrayList<Point> vertices;
    private final HashMap<String, Group> groups;
    private String lastGroup;
    private String fileData;


    public ObjParser() {
        vertices = new ArrayList<>();
        groups = new HashMap<>();
        Group g = new Group();
        lastGroup = "Default Group";
        groups.put(lastGroup, g);
        fileData = "";
    }

    /**
     * Resets the data state if we try to re-parse a file.  Note that we still
     * retain the fileData string so that we don't necessarily need to re-load
     * the entire file.
     */
    private void reset() {
        vertices.clear();
        groups.clear();
        Group g = new Group();
        lastGroup = "Default Group";
        groups.put(lastGroup, g);
    }

    /**
     * Read the OBJ file from disk and store it in the parser fileData element for
     * subsequent parsing.
     * @param filename The fully qualified filename we wish to load
     * @throws IOException If the file cannot be read for any reason.
     */
    public void load(@NotNull String filename) throws IOException {
        Path path = Paths.get(filename);
        List<String> input = Files.readAllLines(path);

        // Do the assignment separate to the read in case we throw an exception
        // during file load.
        fileData = String.join("\n", input);
    }

    /**
     * Alternative method for parsing data.  If no input string is passed, then
     * instead, we'll attempt to read from the fileData string instead.
     * @return The number of lines ignored from the parsed data.
     * @throws RuntimeException if no file has been previously loaded.
     */
    public int parse() throws RuntimeException {
        if (Objects.equals(fileData, "")) {
            throw new RuntimeException("No OBJ file data has been loaded for the parser to parse!");
        }

        return parse(fileData);
    }

    /**
     * The main file parser.  Accepts the OBJ file data as a string.  Also called
     * from the parse() method, which passes in the loaded file data from within the parser
     * object.
     * @param data The OBJ file data
     * @return The number of ignored lines.
     */
    public int parse(@NotNull String data) {
        reset();    // Reset state prior to reading in a new file

        int faces = 0;
        int verts = 0;
        int ignored = 0;    // Counter for invalid lines that will be ignored.
        Group currentGroup = groups.get(lastGroup); // Reference to the group to which we'll add faces

        Stream<String> lines = data.lines();

        // Figure out how many faces are in the object so we can build sensible group boundaries to
        // hopefully improve performance (aim for ten groups minimum for an object)
        long totalFaceCount = lines.filter(w -> w.matches("^f .*")).count();
        long groupSize = totalFaceCount / 10;
        long facesInGroup = 0;

        // Since we've used the data stream to compute totalFaceCount, we need to regenerate it
        // so we can iterate properly
        lines = data.lines();
        Iterable<String> iter = lines::iterator;

        for (String line : iter) {
            // Ignore empty lines...
            if (line.length() == 0) {
                continue;
            }

            char command = line.charAt(0);

            switch (command) {
                case 'v' -> { // Generate a new vertex
                    String[] pointData = line.substring(2).split(" ");
                    if (pointData.length != 3) {
                        ignored++;
                    } else {
                        Point vert = new Point(Double.parseDouble(pointData[0]),
                            Double.parseDouble(pointData[1]), Double.parseDouble(pointData[2]));
                        vertices.add(vert);
                        verts++;
                    }
                }
                case 'f' -> { // Generate a new (triangle) face
                    String[] points = line.substring(2).split(" ");
                    if (points.length < 3) {
                        ignored++;
                    } else {
                        Point p1 = vertices.get(Integer.parseInt(points[0]) - 1);

                        for (int i = 1; i < points.length - 1; i++) {
                            Point p2 = vertices.get(Integer.parseInt(points[i]) - 1);
                            Point p3 = vertices.get(Integer.parseInt(points[i + 1]) - 1);

                            Triangle t = new Triangle(p1, p2, p3);
                            currentGroup.addObject(t);
                            faces++;
                            facesInGroup++;
                            if (facesInGroup >= groupSize) {
                                Group g = new Group();
                                Group lg = groups.get(lastGroup);
                                lg.addObject(g);
                                currentGroup = g;
                                facesInGroup = 0;
                            }
                        }
                    }
                }
                case 'g' -> { // Update current group
                    lastGroup = line.substring(2);
                    if (!groups.containsKey(lastGroup)) {
                        Group g = new Group();
                        groups.put(lastGroup, g);
                    }
                    currentGroup = groups.get(lastGroup);
                    facesInGroup = 0;
                }
                default -> ignored++;
            }
        }

        // Remove any empty group from the model
        if (currentGroup.contents().size() == 0) {
            Group parentGroup = (Group) currentGroup.parent();
            parentGroup.removeObject(currentGroup);
        }

        System.out.println("ObjParse: Model has: "+ verts +" vertices, and "+ faces +" polygons.");
        return ignored;
    }

    /**
     * Get the vertex at the given index value.  Index is clamped to the valid range
     * of points stored in the vertices structure.  Also note that index is 1-based
     * and not zero-based.
     * @param index The index of the vertex to retrieve
     * @return The vertex at the given index position
     */
    public Point verts(int index) throws RuntimeException {
        // If we have no data, throw an exception
        if (vertices.size() == 0) {
            throw new RuntimeException("No vertex data in parser yet.");
        }

        // Clamp index
        index = Math.min(vertices.size(), index);
        index = Math.max(1, index);
        return vertices.get(index-1);
    }

    /**
     * Get a reference to the default group within the parser.
     * @return Reference to the default group
     */
    public Group defaultGroup() {
        return groups.get("Default Group");
    }

    /**
     * Returns a reference to the named object group.  If an empty string (or null)
     * is passed, then the default group is returned instead.  If the group cannot
     * be found, method throws a runtime exception
     * @param name The name of the group we want to return
     * @return Reference to the group
     * @throws RuntimeException if the group cannot be found.
     */
    public Group group(String name) throws RuntimeException {
        if (name == null || name.isEmpty()) {
            return defaultGroup();
        } else {
            if (!groups.containsKey(name)) {
                throw new RuntimeException("Group " + name + " not found!");
            }
            return groups.get(name);
        }
    }

    /**
     * Gets the complete model data as a single group containing all the other groups
     * that have been loaded (Essentially, all the elements of the default group with
     * all other groups as children.
     * @return The complete set of model data as a single group.
     */
    public Group getModel() {
        Group g = groups.get("Default Group");

        for (String key: groups.keySet()) {
            if (!key.equals("Default Group")) {
                g.addObject(groups.get(key));
            }
        }

        return g;
    }
}
