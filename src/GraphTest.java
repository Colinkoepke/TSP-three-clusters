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
	
	//create variable to hold edge
	//create map contain City Name as string and List of edges as value
	List<Edge> e;
	Map<String, List<Edge>> g = new HashMap<>();
	
	//create a list of all 3 clusters/subgraphs
	List<Map<String, List<Edge>>> clusters = new ArrayList<>();
	
	//create counter to get token from file
	int counter = 0;
	
	//create a list to hold total of each cluster - may not be needed
	List<Integer> clustersTotal = new ArrayList<>();
	
	//hold tokens 
	String[] test = new String[3];
		
	/*
	 * Edge class
	 * Each edge contains a weight w, connected city v, & boolean to see if edge is visited
	 * Once a new edge is created, the edge is added to the edge list for each city
	 */
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
		
	//Method to read graph from file and make corresponding clusters
	public void readFromFile(){
		
		try{
			//get input file and create input stream
		    FileInputStream fstream = new FileInputStream("graph.txt");
		    DataInputStream in = new DataInputStream(fstream);
		    BufferedReader br = new BufferedReader(new InputStreamReader(in));
		    String strLine;
		          
		    /*
		     * read each line from the file and place into String array for each token
		     * if the first index of each array is CLUSTER then that means the cluster is over and subgraph should be added to cluster list
		     * if the first index is not CLUSTER, then place odd indices as weights and even indices as city names
		     */
		    while ((strLine = br.readLine()) != null)   {
		    	String[] tokens = strLine.split(" ");  
		        if(!tokens[0].equals("CLUSTER")){
		        	e = new ArrayList<>();
			        for(int i = 1; i < tokens.length; i++){
			        	int weightFromFile = Integer.parseInt(tokens[i]);
			        	Edge e1 = new Edge(weightFromFile, tokens[i+1]);
			        	i++;
			        }
			        g.put(tokens[0], e);
			        //test[counter] = tokens[0];	  
		        }else if(tokens[0].equals("CLUSTER")){
		        	clusters.add(g);
		        	g = new HashMap<>();
		        	
		        	counter++;
		        	if ((strLine = br.readLine())!= null){
		        		tokens = strLine.split(" ");
		        		test[counter] = tokens[0];
		        	e = new ArrayList<>();
			        for(int i = 1; i < tokens.length; i++){
			        	int weightFromFile = Integer.parseInt(tokens[i]);
			        	Edge e1 = new Edge(weightFromFile, tokens[i+1]);
			        	i++;
			        }
			        g.put(tokens[0], e);
		        	}
		        	
		        }
		   }
		    in.close();
			}catch (Exception e){
				System.err.println("Error: " + e.getMessage());
			}
	}
	
	/**
	 * 
	 * @param startingPoint - find starting point for TSP
	 * @param endingPoint - find ending point for TSP
	 * function to read graph from left to right depending on starting and ending cities
	 */
	public void leftToRight(String startingPoint, String endingPoint){
		
		int minWeight = 999999;
		int index = 0;
		String cityToVisit = "";
		String cityToLookup = startingPoint;
		int totalWeight = 0;
		boolean end = false;
		
		List<Edge> edgesList;
		
		/*
		 * first loop is to iterate through each cluster
		 * for each cluster, check if the city has the city, if not check the next cluster
		 * also, if the city is null, then the program should end
		 * then iterate until there are no more cities left in the cluster
		 * 
		 */
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
				//get min weight from each of the edges list to determine where to go next, also make sure edge is not visited
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
			
		
			//same as above method but start from right and go to left
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
		
	//add up cluster total and them all together
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
		g.readFromFile();
		//System.out.println(g.toString());
		
		System.out.print("Enter starting city: ");
		Scanner kb = new Scanner(System.in);
		String startingPoint = kb.nextLine();
		
		System.out.print("\nEnter ending city: ");
		String endingPoint = kb.nextLine();
		
		if(g.determineF(startingPoint, endingPoint))
			g.leftToRight(startingPoint, endingPoint);
		else
			g.rightToLeft(startingPoint, endingPoint);
		System.out.println("Total cost from " + startingPoint + " to " + endingPoint + ": $" + g.findTotal());
	}
}