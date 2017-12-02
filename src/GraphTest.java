import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.InputStreamReader;
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
	int counter = 0;
	List<Integer> clustersTotal = new ArrayList<>();
	
	String[] test = new String[3];
		
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
		
		
		 
		try{
		    FileInputStream fstream = new FileInputStream("graph.txt");
		          DataInputStream in = new DataInputStream(fstream);
		          BufferedReader br = new BufferedReader(new InputStreamReader(in));
		          String strLine;
		          while ((strLine = br.readLine()) != null)   {
		        	  
		        	  String[] tokens = strLine.split(" ");
		        	  // Record record = new Record(tokens[0],tokens[1],tokens[2]);//process record , etc
		        	  if(!tokens[0].equals("CLUSTER")){
		        		  e = new ArrayList<>();
			        	  for(int i = 1; i < tokens.length; i++){
			        		  int weightFromFile = Integer.parseInt(tokens[i]);
			        		  Edge e1 = new Edge(weightFromFile, tokens[i+1]);
			        		  i++;
			        	  }
			        	  g.put(tokens[0], e);
			        	  test[counter] = tokens[0];
			        	  
			        	  
		        	  }else if(tokens[0].equals("CLUSTER")){
		        		  clusters.add(g);
		        		  g = new HashMap<>();
		        		  counter++;
		        	  }
		        	  

		   }
		   in.close();
		   }catch (Exception e){
		     System.err.println("Error: " + e.getMessage());
		   }
	}
		
	public void findFirstGroup(String startingPoint, String endingPoint){
		
		int minWeight = 999999;
		int index = 0;
		String cityToVisit = "";
		String cityToLookup = startingPoint;
		int totalWeight = 0;
		boolean end = false;
		//get list of edges for first V then find lowest value
		
		List<Edge> edgesList;
		
		
		
		//need to change condition to number of edges
		for(int k = 0; k < clusters.size(); k++){
			
			if(!clusters.get(k).containsKey(cityToLookup)){
				k++;
			}
			if(clusters.get(k).get(cityToLookup)== null || end)
				break;
			System.out.println("Visited City: " + cityToLookup);
			for(int i = 0; i < 100; i++){
				edgesList = clusters.get(k).get(cityToLookup);
				if(clusters.get(k).get(cityToLookup)== null || end)
					break;
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
					System.out.println("City visited: " + edgesList.get(index).v + " with a cost of: $" + edgesList.get(index).w);
					if(cityToVisit.equals(endingPoint)){
						end = true;
						break;
					}
					g.put(cityToLookup, edgesList);
					cityToLookup = cityToVisit;
					minWeight = 999999;
				}
		}
			System.out.println("Cluster finished");
			clustersTotal.add(totalWeight);
			totalWeight = 0;
			
			//get next city to lookup based on cluster
			try{
				cityToLookup = test[k+1];
				
			}catch(ArrayIndexOutOfBoundsException e){
				break;
			}
		}
		}
		
		public void rightToLeft(String startingPoint, String endingPoint){
			
			int minWeight = 999999;
			int index = 0;
			String cityToVisit = "";
			String cityToLookup = startingPoint;
			int totalWeight = 0;
			boolean end = false;
			//get list of edges for first V then find lowest value
			
			List<Edge> edgesList;
			
			
			
			//need to change condition to number of edges
			for(int k = clusters.size() - 1; k >= 0; k--){
				
				if(!clusters.get(k).containsKey(cityToLookup)){
					k--;
				}
				if(clusters.get(k).get(cityToLookup)== null || end)
					break;
				System.out.println("Visited City: " + cityToLookup);
				for(int i = 0; i < 100; i++){
					edgesList = clusters.get(k).get(cityToLookup);
					if(clusters.get(k).get(cityToLookup)== null || end)
						break;
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
						if(cityToVisit.equals(endingPoint)){
							end = true;
							break;
						}
						g.put(cityToLookup, edgesList);
						cityToLookup = cityToVisit;
						minWeight = 999999;
					}
			}
				System.out.println("Cluster finished");
				clustersTotal.add(totalWeight);
				totalWeight = 0;
				
				//get next city to lookup based on cluster
				try{
					cityToLookup = test[k-1];
				}catch(ArrayIndexOutOfBoundsException e){
					break;
				}
			}
	}
		
	public boolean determineF(String startingPoint, String endingPoint){
		
		//true is left to right
		//false is right to left
		
		if(clusters.get(2).containsKey(startingPoint))
			return false;
		else if(clusters.get(0).containsKey(startingPoint))
			return true;
		else{
			if(clusters.get(0).containsKey(endingPoint))
				return false;
			else if(clusters.get(2).containsKey(endingPoint))
				return true;
		}

		return true;
	}
		
	public int findTotal(){
		int total = 0;
		for (int i = 0; i < clustersTotal.size(); i++){
			total += clustersTotal.get(i);
		}
		return total;
	}
	
}

public class GraphTest{
	public static void main(String[] args){
		MyGraph g = new MyGraph();
		g.run();
		//System.out.println(g.toString());
		
		System.out.print("Enter starting city: ");
		Scanner kb = new Scanner(System.in);
		String startingPoint = kb.nextLine();
		
		System.out.print("\nEnter ending city: ");
		String endingPoint = kb.nextLine();
		
		if(g.determineF(startingPoint, endingPoint))
			g.findFirstGroup(startingPoint, endingPoint);
		else
			g.rightToLeft(startingPoint, endingPoint);
		System.out.println("Total cost from " + startingPoint + " to " + endingPoint + ": $" + g.findTotal());
	}
}