import java.io.BufferedReader;
import java.io.FileReader;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.util.*;



public class Graph {

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

		}
		catch (Exception e) {
			e.printStackTrace();
		}

		return data;
	}

	//Funktion zum Schreiben in einen File
	public static void write(String fileName, ArrayList<?> data) {

		try {		
			BufferedWriter writer = new BufferedWriter(new FileWriter(fileName));

			for (int i = 0; i < data.size(); i++)
				writer.write(data.get(i).toString() +"\n");

			writer.close();
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Knotenklasse
	 * @param ID, lat, lon
	 */
	private static class Node {

		int ID;
		float lat, lon;

		ArrayList<Edge> inEdges = new ArrayList<Edge>();
		ArrayList<Edge> outEdges = new ArrayList<Edge>();


		public int getID() {
			return this.ID;
		}

		Node(int ID, float lat, float lon) {
			this.ID = ID;
			this.lat = lat;
			this.lon = lon;
		}

		public String toString() {
			String snode = "" + ID + " " + lat + " " + lon;
			return snode;
		}


		private void addInEdge(Edge e) {
			inEdges.add(e);
		}


		private void addOutEdge(Edge e) {
			outEdges.add(e);
		}


		private ArrayList<Edge> getOutEdges() {
			return outEdges;
		}


		private ArrayList<Edge> getInEdges() {
			return inEdges;
		}

	}


	/**
	 * Kantenklasse
	 * @param startID, endID, weight
	 */
	private static class Edge {
		int startID, endID, weight;

		Edge(int start, int end, int weight) {
			this.startID = start;
			this.endID = end;
			this.weight = weight;
		}

		public String toString() {
			String sedge = "" + startID + " " + endID + " " + weight;
			return sedge;
		}


		public int getEndID() {
			return this.endID;
		}
	}

	/**
	 * Graphenklasse
	 */
	private static class G {

		ArrayList<Node> adjacencyList;


		public G(int size) {
			adjacencyList = new ArrayList<Node>(size);
		}


		public void addNode(Node n) {
			adjacencyList.add(n);
		}


		public void addOutEdge(int ID, Edge e) {
			adjacencyList.get(ID).addOutEdge(e);
		}


		public void addInEdge(int ID, Edge e) {
			adjacencyList.get(ID).addInEdge(e);
		}

		public int getDegree(int ID) {
			return adjacencyList.get(ID).getInEdges().size() + adjacencyList.get(ID).getOutEdges().size();
		}


		public ArrayList<Node> getAdjacencyList() {
			return adjacencyList;
		}


		/**
		 * Gibt alle Nachbarknoten einer ID aus.
		 * @param ID
		 * @return
		 */
		private ArrayList<Node> getNeighbours(int ID) {
			ArrayList<Graph.Node> neighbours = new ArrayList<Graph.Node>();
			for(Edge e: adjacencyList.get(ID).getOutEdges()) {
				neighbours.add(adjacencyList.get(e.getEndID()));
			}
			return neighbours;
		}

		/**
		 * Gibt eine Liste aller Nodes aus, welche genau k-Knoten von der ID entfernt ist, auf der aufgerufen wird.
		 * @param ID
		 * @param k
		 * @return
		 */
		public ArrayList<Node> getNeighbourhood(int ID, int k) {
			Set<Node> current = new HashSet<Node>();
			Set<Node> next = new HashSet<Node>();
			Set<Node> visited = new  HashSet<Node>();
			if(k==0) {
				current.add(adjacencyList.get(ID));
				return new ArrayList<Node>(current);
			}else if(k==1){
				return getNeighbours(ID);

			} else {
				current.addAll(getNeighbours(ID));
				for(int i = 0; i<k-1;i++) {
					visited.addAll(current);
					next.clear();
					for(Node n: current) {
						next.addAll(getNeighbours(n.getID()));
					}
					next.removeAll(visited);
					current.clear();
					current.addAll(next);
				}
			}
			current.remove(adjacencyList.get(ID));
			return new ArrayList<Node>(current);
		}
	}


			



	public static void main(String args[]) {

		int ID =  Integer.parseInt(args[1]);
		int k  = Integer.parseInt(args[2]);

		ArrayList<String> data = read(args[0]);

		int nodes = Integer.parseInt(data.get(0));
		int edges = Integer.parseInt((data.get(1)));

		G graph = new G(nodes);

		// Init Nodes in Adjacency List
		for(int i = 2; i<nodes+2; i++) {
			String line = data.get(i);
			String[] xy = line.split("\s");
			Node n = new Node(i-2, Float.parseFloat(xy[0]), Float.parseFloat(xy[1]));
			graph.addNode(n);
		}

		// Init Edges in Adjacency List
		for(int j=nodes+2; j<data.size(); j++)
		{
			String line = data.get(j);
			line.split("\s");
			String[] s = line.split("\s");
			Edge e = new Edge(Integer.parseInt(s[0]), Integer.parseInt(s[1]), Integer.parseInt(s[2]));
			graph.addOutEdge(Integer.parseInt(s[0]), e);
			graph.addInEdge(Integer.parseInt(s[1]), e);
		}


		// Find and write degrees to file
		ArrayList<String> degrees = new ArrayList<String>();
		for(int i = 0; i<nodes;i++) {
			degrees.add(Integer.toString(graph.getDegree(i)));
		}
		write(args[3], degrees);


		// Find and write neighbourhood to file
		ArrayList<String> neighbourhood = new ArrayList<String>();
		for(int i = 0; i<=k;i++) {
			ArrayList<Node> NH = graph.getNeighbourhood(ID, i);
			String s = "";
			for(Node n: NH) {
				s = s + n.getID() + " ";
			}
			neighbourhood.add(s);
		}
		write(args[4], neighbourhood);
	}
}
