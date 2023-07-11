package uniandes.algobc.metabolites;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;
/**
 * Represents a metabolic network of reactions on metabolites
 * @author Jorge Duitama
 */
public class MetabolicNetwork {

	private Map<String,Enzyme> enzymes = new TreeMap<String,Enzyme>(); 
	private Map<String,Metabolite> metabolites = new TreeMap<String,Metabolite>();
	private Set<String> compartments = new TreeSet<String>();
	private Map<String,Reaction> reactions = new TreeMap<String,Reaction>();
	/**
	 * Adds a new gene product that can catalyze reactions
	 * @param product New gene product
	 */
	public void addEnzyme(Enzyme enzyme) {
		enzymes.put(enzyme.getId(), enzyme);
	}
	/**
	 * Adds a new metabolite. If a metabolite with the given name is already added, it 
	 * @param metabolite New metabolite
	 */
	public void addMetabolite(Metabolite metabolite) {
		metabolites.put(metabolite.getId(), metabolite);
		compartments.add(metabolite.getCompartment());
	}
	/**
	 * Adds a new reaction
	 * @param r New reaction between metabolites
	 */
	public void addReaction(Reaction r) {
		reactions.put(r.getId(),r);
		
	}
	/**
	 * Returns the gene product with the given id
	 * @param id of the product to search
	 * @return GeneProduct with the given id
	 */
	public Enzyme getEnzyme (String id) {
		return enzymes.get(id);
	}
	/**
	 * Returns the metabolite with the given id
	 * @param id of the metabolite to search
	 * @return Metabolite with the given id
	 */
	public Metabolite getMetabolite (String id) {
		return metabolites.get(id);
	}
	/**
	 * @return List of metabolites in the network
	 */
	public List<Metabolite> getMetabolitesList() {
		return new ArrayList<Metabolite>(metabolites.values());
	}
	/**
	 * @return List of reactions in the network
	 */
	public List<Reaction> getReactionsList () {
		return new ArrayList<Reaction>(reactions.values());
	}
	
	/**
	 * @return ArrayList of reactions that are not a product
	 */
	public ArrayList<ReactionComponent> encontrarMetabolitosSuststratos() {
		ArrayList<ReactionComponent> metAsSus = new ArrayList<>();
		for(Reaction r: getReactionsList()) {
			boolean esta = false;
			for(ReactionComponent a:r.getReactants()) {
				for(ReactionComponent b:r.getProducts()) {
					if(a.getMetabolite().getId().equals(b.getMetabolite().getId())) {
						esta = true;
					}
				}
				if(!esta&&!metAsSus.contains(a)) {
				metAsSus.add(a);
				}
			}
		}
		return metAsSus;
	}
	
	/**
	 * @return ArrayList of products that are not a reactants
	 */
	public ArrayList<ReactionComponent> encontrarMetabolitosProductos() {
		ArrayList<ReactionComponent> metAsProd = new ArrayList<>();
		for(Reaction r: getReactionsList()) {
			boolean esta = false;
			for(ReactionComponent a:r.getProducts()) {
				for(ReactionComponent b:r.getReactants()) {
					if(a.getMetabolite().getId().equals(b.getMetabolite().getId())) {
						esta = true;
					}
				}
				if(!esta&&!metAsProd.contains(a)) {
					metAsProd.add(a);
				}
			}
		}
		return metAsProd;
	}
	
	public Graph generateGraph() {
		
		Graph g  = new Graph();
		
		//Inicializar grafos con nodos
		for(Metabolite m: getMetabolitesList()){
			g.g.add(new Node(m));
		}
		
		for(Node a: g.g){
			for(Metabolite m: getMetabolitesList()){
				int weight = calculateWeight(a.getVertex(),m);
				if(weight>0) {
					g.addEdge(a.getVertex(), m, weight);
				}
			}
		}
		return g;

	}
	/**
	 * Calcula el peso entre dos metabolitos
	 * @param a
	 * @param b
	 * @return the weight between the two metabolites
	 */
	public int calculateWeight(Metabolite a ,Metabolite b) {
		int contador = 0;
		for(Reaction r: getReactionsList()) {
			boolean estaA = false;
			boolean estaB = false;
			for(ReactionComponent aR:r.getReactants()) {
				if(a.getId().equals(aR.getMetabolite().getId())) {
					estaA=true;
					break;
				}
			}
			for(ReactionComponent bR:r.getProducts()) {
				if(b.getId().equals(bR.getMetabolite().getId())) {
					estaB=true;
					break;
				}
			}
			if(estaA&&estaB) {
				contador++;
			}
		}
		
		return contador;
	}
	
	public static void main(String[] args) throws IOException {
		MetabolicNetworkXMLLoader loader = new MetabolicNetworkXMLLoader();
		//String[] h = {"./data/e_coli_core.xml"};
		//args = h ;
		MetabolicNetwork network = loader.loadNetwork(args[0]);
		System.out.println("Enzymes");
		for(Enzyme enzyme:network.enzymes.values()) {
			System.out.println(enzyme.getId()+" "+enzyme.getName());
		}
		System.out.println();
		
		List<Metabolite> metabolitesList = network.getMetabolitesList();
		System.out.println("Loaded "+metabolitesList.size()+" metabolites: ");
		for(Metabolite m:metabolitesList) {
			System.out.println(m.getId()+" "+m.getName()+" "+m.getCompartment()+" "+m.getChemicalFormula());
		}
		System.out.println();
		List<Reaction> reactions = network.getReactionsList();
		System.out.println("Loaded "+reactions.size()+" reactions");
		for(Reaction r:reactions) {
			System.out.println(r.getId()+" "+r.getName()+" "+r.getReactants().size()+" "+r.getProducts().size()+" "+r.getEnzymes().size()+" "+r.getLowerBoundFlux()+" "+r.getUpperBoundFlux());
		}
		System.out.println("Metabolitos que solo participen como sustratos:");
		network.printArrayList(network.encontrarMetabolitosSuststratos());
		System.out.println("Metabolitos que solo participen como  productos:");
		network.printArrayList(network.encontrarMetabolitosProductos());
		System.out.println("Generar Grafo");
		Graph  b = network.generateGraph();
		network.printGraph(b);
		
		System.out.println("Generar Archivo del Grafo");
		network.writeGraph(b);
	}
//------------------------------------------------------------------------------------------------------------
//UTILS
//------------------------------------------------------------------------------------------------------------

/**
 * Pintar ArrayList
 */
	public void printArrayList(ArrayList<ReactionComponent> rc) {
		for(ReactionComponent r : rc) {
			System.out.print("Nombre: "+r.getMetabolite().getName()+" ID: "+r.getMetabolite().getId()+", ");
		}
		System.out.println();
	}
/**
 * Pintar Grafo
 */
	public void printGraph(Graph m) {
		for(Node n : m.g) {
			System.out.println("Nombre: "+n.getVertex().getName()+" ID: "+n.getVertex().getId());
			System.out.println("Esta conectado con:");
			for(Edge e : n.getEdges()) {
				System.out.println("--"+e.getWeight()+"--->"+"Nombre: "+e.getDest().getName()+" ID: "+e.getDest().getId());
			}
			System.out.println("---------------------------------------------------------------------------------");
		}
		System.out.println();
	}
/**
 * Write Graph
 */
    public void writeGraph(Graph m){
        try {
            FileWriter myWriter = new FileWriter("./Output/example");
            //myWriter.write("fromNode	toNode	"+"\n");
    		for(Node n : m.g) {
    			for(Edge e : n.getEdges()) {
    				myWriter.write(n.getVertex().getId()+"\t"+e.getDest().getId()+"\t"+e.getWeight()+"\n");
    			}
    		}
            myWriter.close();
            System.out.println("Successfully wrote to the file.");
        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }
}
