import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Comparator;

public class Search {


    /**
     * Die Klasse "Node" repräsentiert einen einen einzelnen Knoten im binären Suchbaum
     * mit rechtem und linken Kind. Jedem Knoten wird ein Film zugeordnet .
     * Die Schlüssel der Knoten sind die Titel der zugehörigen Filme.
     */
    private static class Node {
        Movie movie;
        Node left, right;

        public Node(Movie movie) {
            this.movie = movie;
        }

        public String getKey() {
            return movie.getTitle();
        }

        public Node getLeft() {
            return left;
        }

        public Node getRight() {
            return right;
        }

        public void setLeft(Node n) {
            this.left = n;
        }

        public Movie getMovie() {
            return movie;
        }

        public void setRight(Node n) {
            this.right = n;
        }
    }


    private static class BinaryTree {
        Node root;

        public BinaryTree() {
            root = null;
        }

        /**
         * Diese Methode schaut, ob der Baum leer ist. Wenn ja dann wird movie als die Wurzel gesetzt, wenn nicht wird mit der
         * insert(Node v, Movie movie) Methode das Element mit der Hilfsmethode insert(Node, Movie) an die richtige Stelle gesetzt.
         *
         * @param movie, Movie
         */
        public void insert(Movie movie) {
            if (root == null) {
                root = new Node(movie);
            }
            insert(root, movie);
        }

        /**
         * Diese Methode fügt das neue Element movie in den binären Scuhbaum (wenn er bereits eine Wurzel hat) ein. Dafür wird mit der compareTo Methode
         * vergliechen, ob dieses Element nach rechts oder links gehört. Die Methode ist rekursive, da man so lange schauen muss
         * bis man kein Kind hat.
         *
         * @param v,     Note
         * @param movie, Movie
         */
        public void insert(Node v, Movie movie) {

            if (v.getKey().compareTo(movie.getTitle()) == 0) {
                return;
            }
            if (v.getKey().compareTo(movie.getTitle()) < 0) {
                if (v.getLeft() != null) {
                    insert(v.getLeft(), movie);
                    return;
                }
            } else {
                if (v.getRight() != null) {
                    insert(v.getRight(), movie);
                    return;
                }
            }
            if (v.getKey().compareTo(movie.getTitle()) < 0) {
                v.setLeft(new Node(movie));
            } else {

                v.setRight(new Node(movie));
            }
        }

        /**
         * Diese Methode sucht anhand eines Schlüssels einen Knoten im
         * binären Suchbaum und gibt diesen zurück sofern er enthalten ist. Ist dis nicht der
         * Fall, so wird null ausgegeben.
         *
         * @param key
         * @return
         */
        public Node find(String key) {
            if (root != null) {
                return find(root, key);
            }
            return null;
        }

        /**
         * Hilfsmethode zu find(key) für den Umgang mit den rekursiven Aufrufen.
         *
         * @param v
         * @param key
         * @return
         */
        public Node find(Node v, String key) {
            if (v.getKey().compareTo(key) == 0) {
                return v;
            }
            if (v.getKey().compareTo(key) < 0) {
                if (v.getLeft() != null) {
                    return find(v.getLeft(), key);
                }
            } else {
                if (v.getRight() != null) {
                    return find(v.getRight(), key);
                }
            }
            return null;
        }

    }


    /**
     * class Movie von gegebenen Search Klasse (von Ilias)
     */
    private static class Movie {

        private String title;
        private String description;

        public Movie(String info) {
            String[] vpr = info.split("\t");
            if (vpr.length == 2) {
                title = vpr[0];
                description = vpr[1];
            }
        }

        public String getTitle() {
            return title;
        }

        public String getDescription() {
            return description;
        }

        public void print() {
            System.out.println("TITLE: " + title + "\n");
            System.out.println("DESCRIPTION: " + description + "\n\n");
        }

    }


    /**
     * Comparator um zwei Filmtiel lexiographisch zu ordnen.
     */
    private static class MovieByTitleComparator implements java.util.Comparator<Movie> {
        public int compare(Movie m1, Movie m2) {
            return m1.getTitle().compareTo(m2.getTitle());
        }
    }


    /**
     * Funktion zum Einlesen eines Files
     *
     * @param fileName
     * @return
     */
    public static ArrayList<String> read(String fileName) {

        ArrayList<String> data = new ArrayList<>();

        try {
            BufferedReader reader = new BufferedReader(new FileReader(fileName));

            String line = "";

            while (line != null) {

                line = reader.readLine();

                if (line != null) {
                    data.add(line);
                }

            }
            reader.close();

        } catch (Exception e) {
            e.printStackTrace();
        }

        return data;
    }


    /**
     * Funktion zum Schreiben in einen File
     *
     * @param fileName
     * @param data
     */
    public static void write(String fileName, ArrayList<?> data) {

        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(fileName));

            for (int i = 0; i < data.size(); i++)
                writer.write(data.get(i) + "\n");

            writer.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String args[]) {

        // Einlesen der File mit Beschreibung
        ArrayList<String> data = read(args[0]);

        // Einlesen der Filmtitel
        ArrayList<String> titles = read(args[1]);

        // Baut eine ArrayListe mit Movies auf
        ArrayList<Movie> movies = new ArrayList<>();
        for (String s : data) {
            movies.add(new Movie(s));
        }

        // Fügt die Filme in einen  binären Suchbaum ein
        BinaryTree movieTree = new BinaryTree();
        for (Movie movie : movies) {
            movieTree.insert(movie);
        }

        // Sucht im Suchbaum nach den Titeln und fügt die zugehörigen Beschreibungen einer ArrayListe hinzu.
        ArrayList<String> descriptions = new ArrayList<>();
        for (String i : titles) {
            descriptions.add(movieTree.find(i).getMovie().getDescription());
        }

        //Schreibt das Ergebnis in eine File
        write(args[2], descriptions);
    }
}
