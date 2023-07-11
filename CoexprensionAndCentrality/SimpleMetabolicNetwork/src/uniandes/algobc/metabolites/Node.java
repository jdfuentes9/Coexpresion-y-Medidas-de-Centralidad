package uniandes.algobc.metabolites;

import java.util.ArrayList;

public class Node {
	
	private Metabolite Vertex;
	
	private ArrayList<Edge> edges;
	
	public Node(Metabolite vertex) {
		Vertex = vertex;
	
		edges = new ArrayList<>();
	}

	public Metabolite getVertex() {
		return Vertex;
	}

	public void setVertex(Metabolite vertex) {
		Vertex = vertex;
	}

	public ArrayList<Edge> getEdges() {
		return edges;
	}

	public void setEdges(ArrayList<Edge> edges) {
		this.edges = edges;
	}
	
	
	
}
