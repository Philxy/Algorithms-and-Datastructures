
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.util.*;

// Implementierung des Kruskal Algorithmus zum finden des minimalsten Spannbaumes
public class Kruskal {

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


    //Kantenklasse
    private static class Edge implements Comparable<Edge> {
        int s, t, c;

        Edge(int s, int t, int c) {
            this.s = s;
            this.t = t;
            this.c = c;
        }

        Edge() {
            s = -1;
            t = -1;
            c = -1;
        }

        public String toString() {
            String sedge = "" + s + " " + t + " " + c;
            return sedge;
        }


        public int compareTo(Edge o) {
            //return this.c.compareTo(o.getC());
            return Integer.compare(this.c, o.c);
        }
    }


    //Knotenklasse
    private static class Node {

        int ID;
        float lat, lon;
        ArrayList<Edge> outedges, inedges;


        Node(int ID, float lat, float lon) {
            this.ID = ID;
            this.lat = lat;
            this.lon = lon;
            outedges = new ArrayList<Edge>();
            inedges = new ArrayList<Edge>();
        }

        public String toString() {
            String snode = "" + ID + " " + lat + " " + lon;
            return snode;
        }


    }


    //Graphklasse
    private static class G {


        //Knotenarray für Adjanzenzlistendarstellung
        Node[] nodes;
        Set<Edge> edges = new HashSet<>();

        //Konstruktor
        G(ArrayList<String> data) {

            //Einlesen der Knoten und Kanten
            int n = Integer.parseInt(data.get(0));
            int m = Integer.parseInt(data.get(1));

            nodes = new Node[n];

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
                edges.add(edge);
                nodes[s].outedges.add(edge);
                nodes[t].inedges.add(edge);

                //Füge Kante auch in Rückrichtung ein
                Edge bedge = new Edge(t, s, c);
                nodes[t].outedges.add(bedge);
                nodes[s].inedges.add(bedge);
            }
        }


        boolean[] selfRep;
        int[] rep;
        int[] repSize;

        /**
         * Fuehrt den Kruskal Algorithmus (nach Vorl.) auf einem Startknoten aus und gibt die Integer-Liste zurueck, welche alle
         * Kantenkosten enthaelt, die im minimalen Spannbaum enthalten sind.
         *
         * @param ID
         * @return
         */
        public ArrayList<Integer> runKruskal(int ID) {
            selfRep = new boolean[nodes.length];
            repSize = new int[nodes.length];
            rep = new int[nodes.length];

            ArrayList<Integer> MSTedges = new ArrayList<>();

            // Initialisiere Repreaesentaten
            for (int i = 0; i < nodes.length; i++) {
                rep[i] = i;
                selfRep[i] = true;
                repSize[i] = 1;
            }

            // Erstelle eine ArrayListe mit allen Kanten und sortiere diese
            ArrayList<Edge> sortedEdges = new ArrayList<Edge>();
            /*
            Set<Edge> edges = new HashSet<>();
            for (Node v : nodes) {
                edges.addAll(v.outedges);
                edges.addAll(v.inedges);
            }
             */

            //Kruskal
            sortedEdges.addAll(edges);
            Collections.sort(sortedEdges);


            for (Edge e : sortedEdges) {
                if (findSet(nodes[e.s]).ID != findSet(nodes[e.t]).ID) {
                    MSTedges.add(e.c);
                    union(nodes[e.t], nodes[e.s]);
                }
            }
            return MSTedges;
        }

        /**
         * Diese Methode vereinigt zwei Mengen an Knoten. Dies wird gemacht in dem der Repäsentant der
         * einen Menge nun kein Repräsentat mehr ist, sondern auf dem anderen Repräsentanten verweist.
         * Ueber den repSize Array, wird die kleinere Repreaesentantenmenge stets der groesseren Representatnten-
         * manege angehaengt
         * Diese Veränderung muss auch in selfRep festgehalten werden.
         *
         * @param v Note
         * @param w Note
         */
        public void union(Node v, Node w) {
            Node vRep = findSet(v);
            Node wRep = findSet(w);
            if(repSize[vRep.ID] > repSize[wRep.ID]) {
                selfRep[wRep.ID] = false;
                rep[wRep.ID] = rep[vRep.ID];
                repSize[vRep.ID] += repSize[wRep.ID];
            } else {
                selfRep[vRep.ID] = false;
                rep[vRep.ID] = rep[wRep.ID];
                repSize[wRep.ID] += repSize[vRep.ID];
            }
        }

        /**
         * Diese Methode sucht den Repräsentanten von Node v.
         *
         * @param v Note
         * @return Note
         */
        public Node findSet(Node v) {
            int currID = v.ID;
            while (!selfRep[currID]) {
                currID = rep[currID];
            }
            return nodes[currID];
        }

    }

    public static void main(String args[]) {

        System.out.println("\nSpannender Baum\n");

        if (args.length < 2)
            return;




        ArrayList<String> data = read(args[0]);
        G graph = new G(data);

        //Berechne Spannbaum
        ArrayList<Integer> MSTedges = new ArrayList<>();
        MSTedges = graph.runKruskal(0);


        


        write(args[1], MSTedges);

    }

}
