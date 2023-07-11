package uniandes.algobc.metabolites;

public class Edge {
	
	private Metabolite dest;
	
	private  int weight;

	public Edge(Metabolite dest, int weight) {
		super();
		this.dest = dest;
		this.weight = weight;
	}

	public Metabolite getDest() {
		return dest;
	}

	public void setDest(Metabolite dest) {
		this.dest = dest;
	}

	public int getWeight() {
		return weight;
	}

	public void setWeight(int weight) {
		this.weight = weight;
	}
	
	                                                                            
}
