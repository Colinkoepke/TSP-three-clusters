import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

class MyGraph{
	
	List<Edge> e;
	Map<String, List<Edge>> g = new HashMap<>();
	
	//create a list of all 3 clusters
	List<Map<String, List<Edge>>> clusters = new ArrayList<>();
		
	class Edge{
		int w;
		String v;
		boolean visited = false;
		
		public Edge(int w, String v){
			this.w = w;
			this.v = v;
			e.add(this);
		}

		public int getW() { return w; }

		public void setW(int w) { this.w = w; }

		public String getV() { return v; }

		public void setV(String v) { this.v = v; }
		
		public boolean getVisited() { return visited; }
		
		public void setVisited(boolean visited) { this.visited = visited; }
	}
		
	
	public void run(){
		e = new ArrayList<>();
		Edge e1 = new Edge(100, "Boston");
		Edge e2 = new Edge(200, "Houston");
		g.put("Chicago", e);
		
		e = new ArrayList<>();
		e1 = new Edge(400, "Seattle");
		e2 = new Edge(300, "Los Angeles");
		Edge e3 = new Edge(100, "Houston");
		g.put("Boston", e);
		
		e = new ArrayList<>();
		e1 = new Edge(400, "New York");
		e2 = new Edge(300, "Houston");
		g.put("Los Angeles", e);
		
		e = new ArrayList<>();
		e1 = new Edge(100, "Chicago");
		e2 = new Edge(200, "Los Angeles");
		g.put("Houston", e);
		
		clusters.add(g);
		// -----------------------------------
		//------------------------------------
		g = new HashMap<>();
		
		e = new ArrayList<>();
		e1 = new Edge(100, "C2");
		e2 = new Edge(200, "C3");
		g.put("C1", e);
		
		e = new ArrayList<>();
		e1 = new Edge(400, "C1");
		e2 = new Edge(300, "C3");
		e3 = new Edge(100, "C4");
		g.put("C2", e);
		
		e = new ArrayList<>();
		e1 = new Edge(400, "C5");
		e2 = new Edge(300, "C2");
		g.put("C3", e);
		
		e = new ArrayList<>();
		e1 = new Edge(100, "C1");
		e2 = new Edge(200, "C2");
		g.put("C4", e);
		
		clusters.add(g);
	}
	
	@Override
	public String toString(){
		String result = "";
		
		for(List<Edge> edge : g.values()){
			for(int i = 0; i < edge.size(); i++){
				result += "Connected to " + edge.get(i).getV() + " with a weight of " + edge.get(i).getW() + " ";
			}
			result += "\n";
		}
		return result;
		
	}
	
	public void findFirstGroup(String startingPoint){
		
		int minWeight = 999999;
		int index = 0;
		String cityToVisit = "";
		String cityToLookup = startingPoint;
		int totalWeight = 0;
		//get list of edges for first V then find lowest value
		
		System.out.println("Edge visited: Chicago");
		List<Edge> edgesList;
		
		//need to change condition to number of edges
		for(int k = 0; k < clusters.size(); k++){
			
			
			for(int i = 0; i < g.size() * 10; i++){
				edgesList = clusters.get(k).get(cityToLookup);
				for(int j = 0; j < edgesList.size(); j++){
						if(edgesList.get(j).getW() < minWeight && !edgesList.get(j).visited){
							minWeight = edgesList.get(j).getW();
							index = j;
							cityToVisit = edgesList.get(j).getV();
							
					}
				}		
			//set the index of the visited vertex to true and put the new list into the map
				if(!edgesList.get(index).visited){
					edgesList.get(index).visited = true;
					totalWeight += edgesList.get(index).getW();
					System.out.println("Edge visited: " + edgesList.get(index).v + " with weight of: " + totalWeight);
					g.put(cityToLookup, edgesList);
					cityToLookup = cityToVisit;
					minWeight = 999999;
				}
		}
			System.out.println("Cluster finished");
			totalWeight = 0;
			
			cityToLookup = "C1";
		}		
	}
}

public class GraphTest{
	public static void main(String[] args){
		MyGraph g = new MyGraph();
		g.run();
		System.out.println(g.toString());
		
		Scanner kb = new Scanner(System.in);
		String startingPoint = kb.nextLine();
		g.findFirstGroup(startingPoint);
	}
}