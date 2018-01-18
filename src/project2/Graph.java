package project2;

import java.io.*;
import java.util.*;
import java.util.stream.Stream;


public class Graph {
	// This class is used to pass into the GraphCanvas. 
	// Variables stored in this class can be used for drawing on the screen.

	/*
     * TO DO:
     * testing, a bunch of
     * Kruskal's
     * 
     *
     *
     * Create class variables to store basic graph information:
     *   - All information about each of the vertices
     *   - The adjacency lists (= all information about the edges, including their weight)
     *
     * Create class variables to store results computed by the graph algorithms:
     *   - The predecessor array
     *   - The priority queue
     *   - Any other information that you might want to draw. For example, a string
     *   specifying which of the two algorithms you have run for which you are showing
     *   its results.
     */

	// Class variables
    protected int n; // Number of vertices in the graph
	protected int s;

	//protected ArrayList<AdjListEntry> adjList;
	protected ArrayList<Map<Vertex, ArrayList>> adjList;
	protected Vertex[] vertices;

	//protected int[] pred;
	protected ArrayList<int []> pred;
	protected ArrayListHeap queue;
	protected String algorithm;
	protected HashMap<Vertex, ArrayList> map;

    public Graph() {
		//Initialize class variables.
   		n = -1;
		vertices = null;
		queue = null;
		algorithm = null;
		adjList = null;
		pred = null;
    }

	static int[] parseIntArray(String[] arr) {
		try{
			return Stream.of(arr).mapToInt(Integer::parseInt).toArray();

		} catch(NumberFormatException ex){ // handle your exception
			int[] a = {-1};
			return a;
		}
	}

    public Vertex[] getVertices(){
		return this.vertices;
	}

	public void readGraphFromFile(){
		// Optional; asks for keyboard input.
		String filename;
		System.out.print("Please enter name for graph file: ");	
		Scanner scanner = new Scanner(System.in);
		filename = scanner.nextLine();
        readGraphFromFile(filename);
        scanner.close();
	}
    
    public void readGraphFromFile(String filename) {
		Scanner scanner;

		try {
			scanner = new Scanner(new File(filename));

			// Get first number n, initiate arraylist with n rows
			n = scanner.nextInt();

			// Initialize object list of vertices
			vertices = new Vertex[n];

			// Initialize hashmap for the adjacency list
			map = new HashMap<>();
			adjList = new ArrayList<>();

			// Reading from file into variables
			while (scanner.hasNextLine()){
				String lines = scanner.nextLine();

				// Splits file into strings line by line
				String[] strs = lines.trim().split("\\s+");

				// Transforms the string array into int array
				int[] ints = parseIntArray(strs);

				if (ints.length == 3){// if the row is for a new vertex
					// create the vertex
					Vertex v = new Vertex(ints[0], ints[1], ints[2]);

					// add vertex to vertices
					vertices[ints[0]] = v;

					int outdeg = scanner.nextInt();

					// A single adjacency list
					ArrayList<int[]> adjl = new ArrayList<int[]>(outdeg);

					// Gets the adjacency list of each vertex
					// key: vertex, value: int arrays of [ID, weight]
					int i = outdeg;
					if (i == 0){

					}
					else {
						while (i >= 0) {
							String liness = scanner.nextLine();
							String[] strss = liness.trim().split("\\s+");
							int[] intss = parseIntArray(strss);

							if (!intss.toString().equals(-1)) {
								adjl.add(intss);
							}

							i--;
						}
					}

					// Fill the adjacency list
					map.put(v, adjl);
					adjList.add(map);

					// Print statement for test
					System.out.println("Adjacency list: " + v + ":" + map.get(v));
				}
			}
			scanner.close();

			// Print statement for test
			System.out.println();
			System.out.println("Vertices list length: "+ vertices.length);

			System.out.println();
			for (int i = 0 ; i < vertices.length; i++ ){
				System.out.println("Vertex "+ vertices[i].ID + " " + vertices[i].x + " " + vertices[i].y );
			}

		} catch (FileNotFoundException e) {
			System.err.println("Could not find file " + filename + ". " + e);
			System.exit(0);
		} catch (IOException e) {
			System.err.println("Error reading integer from file " + filename + ". " + e);
			System.exit(0);
		}
	}

	public void bellmanFord(Graph G, int src_index) {

		System.out.println();
		System.out.println("Begin running Bellman Ford algorithm..");

		s = src_index;
		int noprint = 0;

		// check if the graph is empty
		if (G.vertices.length == 0) {
			System.out.println("Empty graph.");
		}

		// check if desired source vertex is in vertices
		if (s >= G.vertices.length) {
			System.out.println("Vertex ID not in graph.");
		}

		else {
			pred = new ArrayList();

			//Step 1: initialize d-values
			for (int i = 0; i < G.vertices.length; i++) {
				G.vertices[i].dValue = Integer.MAX_VALUE;
			}
			G.vertices[src_index].dValue = 0;

			// Step 2: Relax all edges
			for (int k = 0; k < G.vertices.length; k++) {

				for (int i = 0; i < G.vertices.length; i++) {

					ArrayList<int[]> alist;
					alist = G.adjList.get(0).get(G.vertices[i]);

					for (int j = 1; j < alist.size(); j++) {
						int src_ID = i;
						int desc_ID = alist.get(j)[0];
						int weight = alist.get(j)[1];

						Vertex desc_vertex;

						desc_vertex = G.vertices[desc_ID];

						if (G.vertices[src_ID].dValue != Integer.MAX_VALUE && G.vertices[src_ID].dValue + weight < desc_vertex.dValue) {
							desc_vertex.dValue = G.vertices[src_ID].dValue + weight;
							//System.out.println(G.vertices[src_ID].ID + "->" + desc_vertex.ID + ":" + desc_vertex.dValue);

							// updates the predecessor array
							int[] pair = new int[2];
							pair[0] = G.vertices[src_ID].ID;
							pair[1] = desc_vertex.ID;
							// if descendant vertex already in the array, take out previous descendant node because it is not the shortest path!!
							for (int q = 0; q < pred.size(); q++) {
								if (pair[1] == pred.get(q)[1]) {
									pred.remove(q);
								}
							}
							pred.add(pair);
						}
					}
				}
			}
			// Prints out source vertex
			System.out.println("Source vertex ID: " + src_index);


			// Step 3: check for negative-weight cycles.
			for (int i = 0; i < G.vertices.length; i++) {

				ArrayList<int[]> alist;
				alist = G.adjList.get(0).get(G.vertices[i]);

				for (int j = 1; j < alist.size(); j++) {
					// update int src id
					int src_ID = i;
					int desc_ID = alist.get(j)[0];
					int weight = alist.get(j)[1];

					Vertex desc_vertex;

					for (int m = 0; m < G.vertices.length; m++) {
						if (G.vertices[m].ID == desc_ID) {//if ID matches with an vertex in vertices
							desc_vertex = G.vertices[m];

							if (G.vertices[src_ID].dValue != Integer.MAX_VALUE && G.vertices[src_ID].dValue + weight < desc_vertex.dValue) {
								System.out.println("Graph contains negative weight cycle");
								noprint = 1;
							}
						}
					}
				}
			}

			/*see predecessor array(list) printed here!!
			for (int p = 0; p < pred.size(); p++){
				System.out.println(pred.get(p)[0]+ " "+pred.get(p)[1]);
			}*/

			// Prints the shortest path trees from source vertex to all other vertices
			if (noprint  != 1) { //if not negative weight cycle
				for (int i = 0; i < G.vertices.length; i++) {
					System.out.println("Resulting d-value from source to Vertex "+ i + ": " + G.vertices[i].dValue);
				}

				System.out.println("Bellman Ford finished. For Shortest Path Tree, please see window.");
			}
			else {//negative weight cycle detected, clear predecessor array
				pred = new ArrayList();
			}

		}
	}

	public void dijkstra(Graph G, int src_index){
		System.out.println();
		System.out.println("Running Dijkstra..");

		s = src_index;

		if (G.vertices.length == 0) {
			System.out.println("Empty graph.");
		}
		if (s >= G.vertices.length ) {
			System.out.println("Vertex ID not in graph.");
		}

		else {
			System.out.println("Source vertex ID: " + src_index);

			// initialize d-values
			for (int i = 0; i < G.vertices.length; i++) {
				G.vertices[i].dValue = Integer.MAX_VALUE;
			}
			G.vertices[src_index].dValue = 0;

			// build the priority queue
			queue = new ArrayListHeap();
			for (int i = 0; i < G.vertices.length; i++) {
				queue.insert(G.vertices[i]);
			}

			//System.out.println("initial: "+ queue.toString());
			System.out.println("Vertex" + "     " + "Distance (d-value)");

			Vertex u;
			pred = new ArrayList();

			while (!queue.isEmpty() && !queue.unreachable()) {
				u = queue.extractMin();

				// relax all edges leaving u
				ArrayList<int[]> alist;
				alist = G.adjList.get(0).get(u);

				for (int j = 1; j < alist.size(); j++) {

					int desc_ID = alist.get(j)[0];
					int weight = alist.get(j)[1];

					if ( !queue.isEmpty() && G.vertices[desc_ID].dValue > u.dValue + weight ) {
						// updates heap
						G.vertices[desc_ID].dValue = u.dValue + weight;
						queue.decreaseKey(G.vertices[desc_ID].indexInHeap, G.vertices[desc_ID].dValue);

						// updates the predecessor array
						int[] pair = new int [2];
						pair[0] = u.ID;
						pair[1] = desc_ID;
						for (int q =0 ; q < pred.size(); q++){
							if (pair[1] == pred.get(q)[1]) {
								pred.remove(q);
							}
						}
						pred.add(pair);
					}
				}
				System.out.println(src_index + " --> "+ u.ID + "       " +u.dValue);
				//System.out.println("after each adj list" + queue.toString());
			}
			if (queue.unreachable()) {
				System.out.println("There exist unreached vertex in the graph originating from this source vertex.");
			}
			System.out.println("Dijsktra finished. For Shortest Path Tree, please see window.");
		}
	}

	public void kruskal(Graph G) {
		// WORKING PROGRESS
		System.out.println();
		System.out.println("Running Kruskal..");

		if (G.vertices.length == 0) {
			System.out.println("Empty graph.");
		} else {
			// add all edges to a set
			ArrayList edges = new ArrayList();
			for (int k = 0; k < G.vertices.length; k++) {

				for (int i = 0; i < G.vertices.length; i++) {

					ArrayList<int[]> alist;
					alist = G.adjList.get(0).get(G.vertices[i]);

					int[] edge = new int[3];

					for (int j = 1; j < alist.size(); j++) {
						int src_ID = i;
						int desc_ID = alist.get(j)[0];
						int weight = alist.get(j)[1];
						edge[0] = src_ID;
						edge[1] = desc_ID;
						edge[2] = weight;
					}
					//System.out.println(edge[0]);
					edges.add(edge);
				}
			}
			// sort set
			//edges.sort();
			// Create V subsets with single elements
			// pick smallest edge,  if including this edge does't cause cycle, include it in result and increment the index of result for next edge

			/*Pseudo
			for each v in V
				MAKE-SET(v)
			Sort edges of E in non-decreasing order according to w
			For each (u,v) in E taken in this order do
				if FIND-SET(u) != FIND-SET(v) // u,v in different trees
			S = S union {(u,v)}
			UNION(u,v) // Edge (u,v) connects the two trees
			*/
		}
	}
}

