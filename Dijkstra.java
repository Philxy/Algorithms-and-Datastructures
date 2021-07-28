import java.io.BufferedReader;
import java.io.FileReader;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Arrays;
import java.util.Queue;
import java.util.Random;
import java.util.PriorityQueue;
import java.util.LinkedList;


public class Dijkstra {


    //Funktion zum Einlesen eines Files
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

    //Funktion zum Schreiben in einen File
    public static void write(String fileName, ArrayList<?> data) {

        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(fileName));

            for (int i = 0; i < data.size(); i++)
                writer.write(data.get(i).toString() + "\n");

            writer.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //Knotenklasse
    private static class Node {

        int ID;
        float lat, lon;

        //Offsetwerte
        int inoffset, outoffset;

        Node(int ID, float lat, float lon) {
            this.ID = ID;
            this.lat = lat;
            this.lon = lon;

            inoffset = -1;
            outoffset = -1;
        }

        public String toString() {
            String snode = "" + ID + " " + lat + " " + lon;
            return snode;
        }
    }


    //Kantenklasse
    private static class Edge {
        int s, t, c;

        Edge(int s, int t, int c) {
            this.s = s;
            this.t = t;
            this.c = c;
        }

        public String toString() {
            String sedge = "" + s + " " + t + " " + c;
            return sedge;
        }
    }


    //Graphklasse
    private static class G {

        Node[] nodes;

        //Kantenarrays für Offsetdarstellung
        Edge[] inedges;
        Edge[] outedges;

        //Konstruktor
        G(ArrayList<String> data) {

            //Einlesen der Knoten und Kanten
            int n = Integer.parseInt(data.get(0));
            int m = Integer.parseInt(data.get(1));

            nodes = new Node[n];
            inedges = new Edge[m];
            outedges = new Edge[m];

            System.out.println("read graph with " + n + " nodes and " + m + " edges");


            for (int i = 2; i < n + 2; i++) {
                String[] vpr = null;
                vpr = data.get(i).split("\\s+");
                float lat = Float.parseFloat(vpr[0]);
                float lon = Float.parseFloat(vpr[1]);
                Node node = new Node(i - 2, lat, lon);
                nodes[i - 2] = node;
            }


            for (int i = n + 2; i < n + m + 2; i++) {
                String[] vpr = null;
                vpr = data.get(i).split("\\s+");
                int s, t, c;
                s = Integer.parseInt(vpr[0]);
                t = Integer.parseInt(vpr[1]);
                c = Integer.parseInt(vpr[2]);
                Edge edge = new Edge(s, t, c);
                inedges[i - n - 2] = edge;
                outedges[i - n - 2] = edge;
            }

            //Berechnung der Offsetwerte
            computeOffsets();

        }

        private void computeOffsets() {

            //Sortiere ausgehende Kanten nach Startknoten und eingehende Kanten nach Zielknoten
            Arrays.sort(outedges, (e1, e2) -> Integer.compare(e1.s, e2.s));
            Arrays.sort(inedges, (e1, e2) -> Integer.compare(e1.t, e2.t));

            //AUSGEHENDE KANTEN
            //Setze Knotenoffsets für nichtisolierte Knoten
            int olds = -1;
            for (int i = 0; i < outedges.length; i++) {

                int s = outedges[i].s;
                if (s != olds) {
                    nodes[s].outoffset = i;
                }
                olds = s;
            }

            //Setze Knotenoffsets für isolierte Knoten
            int last = outedges.length;
            for (int i = nodes.length - 1; i >= 0; i--) {

                if (nodes[i].outoffset == -1)
                    nodes[i].outoffset = last;

                last = nodes[i].outoffset;
            }

            //EINGEHENDE KANTEN
            //Setze Knotenoffsets für nichtisolierte Knoten
            int oldt = -1;
            for (int i = 0; i < inedges.length; i++) {

                int t = inedges[i].t;
                if (t != oldt) {
                    nodes[t].inoffset = i;
                }
                oldt = t;
            }

            //Setze Knotenoffsets für isolierte Knoten
            last = inedges.length;
            for (int i = nodes.length - 1; i >= 0; i--) {

                if (nodes[i].inoffset == -1)
                    nodes[i].inoffset = last;

                last = nodes[i].inoffset;
            }

        }


        /**
         * Gibt eine ArrayListe zurueck welche alle Kanten enthaelt, welche von einem bestimmten Knoten ausgehen.
         *
         * @param ID die ID zu welcher die Kanten zu den Nachbarn gefunden werden sollen
         * @return Nachbarkanten
         */
        public ArrayList<Edge> getEdgesToNeighbour(int ID) {
            ArrayList<Edge> edges = new ArrayList<>();
            int max;
            if (ID == nodes.length - 1) {
                max = outedges.length - 1;
            } else max = nodes[ID + 1].outoffset;
            for (int i = nodes[ID].outoffset; i < max; i++) {
                edges.add(outedges[i]);
            }
            return edges;
        }

        /**
         * Implementiert den Dijkstra Algorithmus mithilfe des MinHeap als Priority Queue
         *
         * @param s
         * @param t
         * @return
         */
        public int runDijkstra(int s, int t) {
            MinHeap PQ = new MinHeap();

            ArrayList<Integer> dist = new ArrayList<>();

            for (int i = 0; i < nodes.length; i++) {
                dist.add(-1);  // -1 entspricht  unbesuchtem Knoten
            }
            dist.set(s, 0);
            PQ.insert(dist.get(s), s);

            while (!PQ.isEmpty()) {
                int[] v = PQ.extract();
                for (Edge e : getEdgesToNeighbour(v[1])) {
                    int w = e.t;
                    int newCosts = dist.get(v[1]) + e.c;
                    if (dist.get(w) > newCosts || dist.get(w) == -1) { // falls unbesucht oder kuerzere Distanz gefunden
                        dist.set(w, newCosts);
                        PQ.insert(newCosts, w);
                    }
                }
            }
            return dist.get(t);
        }

    }

    /**
     * Implementiert die MinHeap Datenstruktur um sie fuer den Dijkstra Algorithmus als Priority Queue zu verwenden.
     */
    public static class MinHeap {

        ArrayList<HeapNode> A = new ArrayList<HeapNode>();

        /**
         * Überprüft ob die ArrayList A leer ist, wenn nein return false wenn ja return true
         *
         * @return boolean
         */
        public boolean isEmpty() {
            return A.size() <= 0;
        }


        /**
         * Extrahiert die Wurzel des MinHeap. Dann nimmt es das letze Element und setzt es als Wurzel. Danach muss mit sink(0)
         * die MinHeap Eigenschaft wieder hergestellt werden.
         *
         * @return
         */
        public int[] extract() {
            if (A.size() != 0) {
                int[] result = new int[]{A.get(0).getDist(), A.get(0).getID()};
                A.set(0, A.get(A.size() - 1));
                A.remove(A.size() - 1);
                sink(0);
                return result;
            }
            return null;
        }

        /**
         * Das Element welches hinzugefügt werden soll, wir an das Ende der ArrayList A gesetzt. Danach wird mit
         * swim() das Element nach oben verschoben, wenn das Element kleiner als der Elternknoten ist.
         * Die Methode swim() stellt also die MinHeap Eigenschaft wieder her.
         *
         * @param dist
         * @param ID
         */
        public void insert(int dist, int ID) {
            A.add(new HeapNode(dist, ID));
            swim();
        }


        /**
         * Eine HeapNode repraesentiert die Eintraege des MinHeap Arrays bzw. Baumes und speichert lediglich
         * die IDs und die Keys (Distanzen).
         */
        public static class HeapNode {
            int dist;
            int ID;

            public HeapNode(int dist, int ID) {
                this.dist = dist;
                this.ID = ID;
            }

            public int getID() {
                return this.ID;
            }

            public int getDist() {
                return dist;
            }

            public void setDist(int dist) {
                this.dist = dist;
            }

            public void setID(int ID) {
                this.ID = ID;
            }
        }


        /**
         * Korrigiert Fehlstellungen im MinHeap, indem das letze Element des Heap Arrays im Heap Baum aufsteigt bis
         * es an der richtigen Stelle im Baum bzw. Array angelangt ist. Erfolgt mit Laufzeit log2(n) im worst case.
         */
        public void swim() {
            int currIndex = A.size() - 1;
            int parentIndex = ((int) Math.floor((currIndex - 1) / 2));

            if (parentIndex < 0) {
                return;
            }

            while (A.get(currIndex).getDist() < A.get(parentIndex).getDist()) {
                HeapNode temp1 = A.get(parentIndex);
                HeapNode temp2 = A.get(currIndex);
                A.set(currIndex, temp1);
                A.set(parentIndex, temp2);
                currIndex = parentIndex;
                parentIndex = ((int) Math.floor((currIndex - 1) / 2));
                if (parentIndex < 0) {
                    return;
                }
            }
        }


        /**
         * Vertauscht zwei Elemente des Heap Arrays
         *
         * @param i Index Element 1
         * @param j Index Element 2
         */
        private void swap(int i, int j) {
            HeapNode temp1 = A.get(i);
            HeapNode temp2 = A.get(j);
            A.set(i, temp2);
            A.set(j, temp1);
        }


        /**
         * Überpfüft ob der Elternknoten kleiner als beide Kindknoten sind. Wenn ja passiert nichts.
         * Wenn nein versickert der Elternknoten. Dies passiert so lange, bis man nicht mehr tauschen kann.
         * Somit ist dann die MinHeap Eigenschaft wieder hergstellt.
         *
         * @param ID
         */
        private void sink(int ID) {
            if (2 * ID + 1 < A.size()) {
                int minimumChildIndex;
                int leftChildIndex = 2 * ID + 1;
                int rightChildIndex;
                if (2 * ID + 2 < A.size()) {
                    rightChildIndex = 2 * ID + 2;
                } else {
                    rightChildIndex = ID;
                }
                if (A.get(leftChildIndex).getDist() <= A.get(rightChildIndex).getDist()) {
                    minimumChildIndex = leftChildIndex;
                } else {
                    minimumChildIndex = rightChildIndex;
                }
                if (A.get(minimumChildIndex).getDist() < A.get(ID).getDist()) {
                    HeapNode nextParent = A.get(minimumChildIndex);
                    A.set(minimumChildIndex, A.get(ID));
                    A.set(ID, nextParent);
                    sink(minimumChildIndex);
                }
            }
        }


    }


    public static void main(String args[]) {


        if (args.length < 3)
            return;

        //Graph einlesen
        ArrayList<String> data = read(args[0]);
        G graph = new G(data);

        //Start- und Zielknoten einlesen
        ArrayList<String> st = read(args[1]);

        //Kürzeste Wege Berechnung mit BellmanFord
        //(ACHTUNG: zu langsam für große Graphen!)
        ArrayList<Integer> dists = new ArrayList<>();
        for (int i = 0; i < st.size(); i++) {
            String[] vpr = null;
            vpr = st.get(i).split("\\s+");
            int s = Integer.parseInt(vpr[0]);
            int t = Integer.parseInt(vpr[1]);

            int dist = graph.runDijkstra(s, t);

            System.out.println(s + " --> " + t + "   dist = " + dist);
            dists.add(dist);
        }

        //Schreiben der Distanzen in den Ausgabefile
        write(args[2], dists);
    }
}
