package uniandes.algobc.metabolites;

import java.util.ArrayList;

public class Graph {
	
	public ArrayList<Node> g;
    
    public Graph(){
    	g = new ArrayList<>();
    }
    
    void addEdge(Metabolite src, Metabolite dest, int weight)
    {
    	for(int i=0;i<g.size();i++) {
    		if(g.get(i).getVertex().getId().equals(src.getId())) {
    			boolean esta = false;
    			for(int j=0;j<g.get(i).getEdges().size();j++) {
    				if(g.get(i).getEdges().get(j).getDest().getId().equals(dest.getId())) {
    					esta = true;
    				}
    			}
    			if(!esta) {
    				g.get(i).getEdges().add(new Edge(dest,weight));
    			}
    		}
    	}
    }
}
