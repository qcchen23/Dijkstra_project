package project2;

import java.awt.*;
import java.util.ArrayList;

public class GraphCanvas extends Canvas {

	static final int diameter = 30;
	static final int arrowWidth = 6;
	static final int arrowLength = 15;

	protected Graph G;

	public GraphCanvas(Graph G) {
		this.G = G;
	}

	public void drawDValue(Graphics graphics, Vertex v) {
	  /* Draws dValue of vertex in upper left corner */
		graphics.setColor(Color.RED);
		if (v.dValue != 2147483647){
				graphics.drawString(Integer.toString(v.dValue), v.x - diameter / 2, v.y - diameter / 2);
		}
		else {graphics.drawString("∞", v.x - diameter / 2, v.y - diameter / 2);}
	}

	public void drawVertex(Graphics graphics, Vertex v) {
	  /* Draws vertexID in circle centered at (vertex.x,vertex.y) */
		graphics.setColor(Color.BLACK);
		graphics.drawOval(v.x - diameter / 2, v.y - diameter / 2, diameter, diameter);
		graphics.drawString(Integer.toString(v.ID), v.x - diameter / 4, v.y + diameter / 4);
	}

	public void drawEdge(Graphics graphics, Vertex v, Vertex u, int weight) {
	  /* Draws a weighted DIRECTED edge v->u as an arrow from v to u . 
	   * Note that edges v->u and u->v will be side-to-side and not right on top of each other. 
	   */
		double l = Math.sqrt((u.x - v.x) * (u.x - v.x) + (u.y - v.y) * (u.y - v.y));
		graphics.setColor(new Color(110, 60, 240));
		// edge
		graphics.drawLine((int) (u.x + (l - diameter / 2) * (v.x - u.x) / l - arrowWidth * (u.y - v.y) / l),
				(int) (u.y + (l - diameter / 2) * (v.y - u.y) / l + arrowWidth * (u.x - v.x) / l),
				(int) (v.x + (l - diameter / 2) * (u.x - v.x) / l - arrowWidth * (u.y - v.y) / l),
				(int) (v.y + (l - diameter / 2) * (u.y - v.y) / l + arrowWidth * (u.x - v.x) / l));

		// arrow
		Polygon arrowhead = new Polygon();
		arrowhead.addPoint((int) (v.x + (l - diameter / 2) * (u.x - v.x) / l - arrowWidth * (u.y - v.y) / l),
				(int) (v.y + (l - diameter / 2) * (u.y - v.y) / l + arrowWidth * (u.x - v.x) / l));
		arrowhead.addPoint((int) (v.x + (l - diameter / 2 - arrowLength) * (u.x - v.x) / l - 2 * arrowWidth * (u.y - v.y) / l),
				(int) (v.y + (l - diameter / 2 - arrowLength) * (u.y - v.y) / l + 2 * arrowWidth * (u.x - v.x) / l));
		arrowhead.addPoint((int) (v.x + (l - diameter / 2 - arrowLength) * (u.x - v.x) / l + 0 * arrowWidth * (u.y - v.y) / l),
				(int) (v.y + (l - diameter / 2 - arrowLength) * (u.y - v.y) / l - 0 * arrowWidth * (u.x - v.x) / l));
		graphics.fillPolygon(arrowhead);

		// weight
		graphics.drawString(Integer.toString(weight),
				(int) (v.x + (l - diameter / 2 - 2 * arrowLength) * (u.x - v.x) / l - 2 * arrowWidth * (u.y - v.y) / l),
				(int) (v.y + (l - diameter / 2 - 2 * arrowLength) * (u.y - v.y) / l + 2 * arrowWidth * (u.x - v.x) / l));

	}

	public void highlightEdge(Graphics graphics, Vertex v, Vertex u) {
	  /* Highlights the line segment of the DIRECTED edge v->u.  
	   * Note that edges v->u and u->v will be side-to-side and not right on top of each other.
	   */
		double l = Math.sqrt((u.x - v.x) * (u.x - v.x) + (u.y - v.y) * (u.y - v.y));
		graphics.setColor(new Color(240, 20, 110));
		// edge
		graphics.drawLine((int) (u.x + (l - diameter / 2) * (v.x - u.x) / l - arrowWidth * (u.y - v.y) / l),
				(int) (u.y + (l - diameter / 2) * (v.y - u.y) / l + arrowWidth * (u.x - v.x) / l),
				(int) (v.x + (l - diameter / 2) * (u.x - v.x) / l - arrowWidth * (u.y - v.y) / l),
				(int) (v.y + (l - diameter / 2) * (u.y - v.y) / l + arrowWidth * (u.x - v.x) / l));
	}

	public void highlightVertex(Graphics graphics, Vertex v) {
	  /* Highlights circle around the vertex v. 
	   */
		graphics.setColor(Color.GREEN);
		graphics.drawOval(v.x - diameter * 5 / 12, v.y - diameter * 5 / 12, diameter * 10 / 12, diameter * 10 / 12);
	}

	public void drawTitle(Graphics graphics, String s) {
	  /* Draws title in large font on screen.
	   * Could use this method to draw algorithms type on screen (e.g., "Dijkstra") 	  
	   */
		Font oldFont = graphics.getFont();
		graphics.setFont(oldFont.deriveFont((float) 40));
		graphics.setColor(new Color(240, 20, 110));
		graphics.drawString(s, 200, 500);
		graphics.setFont(oldFont);
	}

	public void paint(Graphics graphics) {
		// Don't confuse "Graphics" (which is used to draw on) with "Graph" (which stores your graph)
		// This method has to contain all the drawing/painting code

		if (G.vertices.length != 0) {// if structure is not null
			int i = 0;
			while (i < G.vertices.length) {
				// Draw vertices
				drawVertex(graphics, G.vertices[i]);

				// Creating a temporary list to store adjacencies
				ArrayList<int[]> alist;
				alist = G.adjList.get(0).get(G.vertices[i]);

				// Converting the ID in adjacency list back to vertex for drawing
				for (int n = 0; n < alist.size(); n++) {

					Vertex corresp_vertex;

					for (int m = 0; m < G.vertices.length; m++) {

						if (G.vertices[m].ID == alist.get(n)[0]) {//if ID matches with an vertex in vertices
							corresp_vertex = G.vertices[m];

							// Draw edges between two vertices. The direction is important to distinguish.
							drawEdge(graphics, G.vertices[i], corresp_vertex, alist.get(n)[1]);
						}
					}
				}
				i++;
			}

			// Draw title string in large font on screen.
			drawTitle(graphics, "Graph read from file");

			if (G.s < G.vertices.length) {//if desired starting vertex is one of the vertices
				// Highlight the source vertex
				highlightVertex(graphics, G.vertices[G.s]);

				if (!G.pred.isEmpty()) {//if the algorithm runs

					// Highlight an edge according to information in the priority queue
					for (int q = 0; q < G.pred.size(); q++) {
						highlightEdge(graphics, G.vertices[G.pred.get(q)[0]], G.vertices[G.pred.get(q)[1]]);
					}

					// Draw d-values into the graph
					for (int l = 0; l < G.vertices.length; l++) {
						drawDValue(graphics, G.vertices[l]);
					}
				}
			}
		}
	}
}
