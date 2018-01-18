package project2;

import java.awt.*;
import javax.swing.JFrame;
import java.io.*;
import java.util.*;

public class Project2 {
	
	
	public static void main(String[] args) {
	    // Set up application frame
	    JFrame window = new JFrame("Project 2");
	    window.setBounds(50,50,800,800); // Dimensions of frame
	    window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	    System.out.println("\nWritten by Chloe Chen\n");

	    /*
	     * TO DO:
	     * Create Graph and pass into GraphCanvas (done)
	     * Run Bellman-Ford or Dijkstra.
	     * You probably want to just run one of the algorithms at a time.
	     */

	    // Creates new graph
	    Graph G = new Graph();

		String filename = "graph.dat";
		G.readGraphFromFile(filename);

		System.out.println();
		System.out.println("Graph read from file "+ filename);

		GraphCanvas canvas = new GraphCanvas(G);

	    // Display frame
	    canvas.setBackground(new Color(245,241,222));
	    window.getContentPane().add(canvas); // component added to content pane
	    window.setVisible(true); // displays the frame

		// Uncomment one of the three!
		G.bellmanFord(G, 0);
		//G.dijkstra(G, 0);
		//G.kruskal(G);
	}
}
