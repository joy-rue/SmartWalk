import java.util.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.lang.reflect.Array;

/**
 * @author Ruvimbo Joy Sithole
 * @author Faustina Awuntayami Ama
 *
 * A fully functional graph with alogrithm to compute shortest distance between its vertices
 */

class Graph {
    private boolean undirected;
    public Map<String, Hashtable<String, Edge>> map = new HashMap<>(); // A HashMap used to store vertices with thier edges
    List<String> listVertices = new ArrayList<>();   //ArrayList used to maintain order and position for easy access 
 
    public List<String[]> dijkstraResults = new ArrayList<String[]>(); //a list of the shortest paths to each vertex (i guess an adjacency)
    public List<int[]> dijkstraDistances = new ArrayList<int[]>();  //list of the distances from each vertex to a specific vertex

    /**The default constructor
     @param undirected boolean value is set to default value of true*/
    public Graph(){
        this(true);
    }

    /** constructor with parameter to determine whether the graph is undirected  */
    public Graph(boolean isdirected){
        undirected = isdirected;
    }

    /** Adds a new vertex to the graph*/
    public void addVertex(String v){
        //vertex's Hashtable stores as collection of all the vertices it is directly connected to and the respective edges 
        map.put(v, new Hashtable<String, Edge>());
        listVertices.add(v);
    }

    /** @param src source vertex
     * @param dst destination vertex
     * @param value the weight of the edge*/
    public void addEdge(String src, String dst, int value){ 
        if (!map.containsKey(src))
            addVertex(src); 
        if (!map.containsKey(dst))
            addVertex(dst);
        Edge newEdge = new Edge(src, dst, value);
        map.get(src).put(dst, newEdge);
        if (undirected == true) //both vertices should connect to each other if the graph is undirected
            map.get(dst).put(src, newEdge);
    }

    /** @param u source
     * @param v destination
     * @return the edge between the two vertices*/
    public Edge getEdge(String u, String v){
        return map.get(u).get(v);
    }

    // Returns the number of edges
    public void getEdgesCount(boolean undirected){
        int count = 0;
        for (String v : map.keySet()) {
            count += map.get(v).size();
        }
        // In undirected graphs, the edges connect destination to source and vice versa
        if (undirected == true){
            count = count / 2;
        }
        System.out.println("The graph has " + count + " edges.");
    }

    /**Returns true if the edge exists in the graph */
    public boolean hasEdge(String source, String destination){
        // if the destination vertex is not found in the Hashtable of the source, the edge between them does not exist
        if (map.get(source).get(destination)!= null) {
            return true;
        }
        else {
           return false;
        }
    }

    /** Prints the list of all edge incidents of all the vertices*/
    @Override
    public String toString(){
        StringBuilder builder = new StringBuilder();

        for (String v : map.keySet()) {
            builder.append(v + ": ");
            //obtain an iterator of all the value within the vertex hashtable
            Enumeration<Edge> values = map.get(v).elements(); 

            while(values.hasMoreElements()){
                builder.append(values.nextElement().getElements());
            }
            builder.append("\n");
        }

        return (builder.toString());
    }
    
    
    //------------------------------shortest path Aid methods to the Graph ------------------------------

    /**
     * finds the index of the vertex with the minum value from source, from the vertices that have not been visited
     * @param s_path a list of shortest path distance of all the vertices to the source
     * @param visited a list of boolean values for each vertice
     * @return the index of the vertex with the shortest path*/

    private int minDistance(int s_path[], Boolean visited[]){
        // initialise the min value to infinity
        int min = Integer.MAX_VALUE, min_index = -1;
    
        for (int v = 0; v < s_path.length; v++) //loops through the list fo shortest paths until it finds the minimum
            if (visited[v] == false && s_path[v] <= min ) {
                min = s_path[v];
                min_index = v;
            }
        return min_index;
    }


    /** @param generation the list of all parents that get to the desired destination
     * @param i index of the current vertex in the graph
     * @param source vertex where all vertices point to
     * @return String of shortest path from start to source following the recursion*/

    String getPath(String [] generation, int i, String source){
        if (!generation[i].equals(source)){
            int child = listVertices.indexOf(generation[i]);
            return (" -> " + generation[i] + getPath(generation, child, source));
        }
        return (" to get to the " + source);    
    }
    


    /**
     * The dijkstra's alogrithm finds the shortest path to the source for every vertex
     * @param src the source vertex
     * */

    public void dijkstra( String src){
        int gSize = listVertices.size();
        int s_path[] = new int[gSize]; // will contain the shortest paths
        String [] generations = new String[gSize]; //contains a list of parents that lead to the root/source
        Boolean visited[] = new Boolean[gSize]; // if vertex has been visited, the visited value will be true

        // All distances initalised to INFINITE 
        for (int i = 0; i < gSize; i++) {
            s_path[i] = Integer.MAX_VALUE;
            visited[i] = false;
        }

        //Distance of source vertex from itself is always 0
        s_path[listVertices.indexOf(src)] = 0;
        generations[listVertices.indexOf(src)] = src;
    
        // Find shortest path for all vertices
        for (int count = 0; count < gSize - 1; count++) {
            int u = minDistance(s_path, visited); // u is source in first round
            visited[u] = true; // selected vertex is marked as visited
                
        // Update shortest_path value of all the vertices adjacent to the u found
        Set<String> keys = map.get(listVertices.get(u)).keySet();
            for (String vertex : keys){//vertex is direct neighbor of u
                int v = listVertices.indexOf(vertex);
                int uvDistance = this.getEdge(listVertices.get(u), listVertices.get(v)).distance();
                //update the path if vertex has not been visited && orginal s_path to source > distance (u,v) + distance (u, source) 
                if (!visited[v]  && s_path[u] != Integer.MAX_VALUE && s_path[u] + uvDistance < s_path[v]){
                    s_path[v] = s_path[u] + uvDistance;
                    generations[v] = this.listVertices.get(u);
                }
                
            } 
        }
        // Add the generation of parents for source to the graph field variable
        dijkstraResults.add(listVertices.indexOf(src), generations);
        dijkstraDistances.add(listVertices.indexOf(src), s_path);
      
        
    }

        
// Driver Code

     public static void main(String args[]) throws FileNotFoundException{

        // -----------------------------------------------Data Entry------------------------------------------------------//


        // A new undirected graph is instantiated
        Graph AshesiCampus = new Graph();

        // create new file object and pass the path as a string parameter
        File file = new File("Ashesi_CampusPoints.txt");
        Scanner scannerObject = new Scanner(file);
        scannerObject.nextLine();

        //Adds new vertices and edges using values from the file
        while (scannerObject.hasNextLine()){
            String [] line = scannerObject.nextLine().split("\t");
            AshesiCampus.addEdge(line[0], line[1], Integer.parseInt(line[2])); //line[0] source, line[1] dest, line[2] edge distance
        }
        // Compute shortest path distances from the source to all other vertices
        for (String vertex : AshesiCampus.listVertices){
            AshesiCampus.dijkstra(vertex);
        }

        //-------------------------------------------The program------------------------------------------------------------//


        Scanner scannerObj = new Scanner(System.in);

        System.out.println("\nWELCOME :)\nI'm SmartWalk.\n Tell me where YOU want to go and" 
                            +" I will give you the shortest route to take.\nThese are the main Ashesi Campus points:");

        for (String vertex : AshesiCampus.listVertices){
            if (AshesiCampus.listVertices.indexOf(vertex)%3 == 0)
                System.out.print("\n");
            System.out.print(String.format("%s\t\t\t", vertex)); 
        }
  
        System.out.println("\n Where are you?\t");
        String origin = scannerObj.nextLine().toUpperCase(); //accept input from user 
        System.out.println("Where do you want to go?\t");
        String destination = scannerObj.nextLine().toUpperCase(); //accept input from user
        
        //computing path from source to destination
        String [] allPathstoDestination = AshesiCampus.dijkstraResults.get(AshesiCampus.listVertices.indexOf(destination));
        String path = AshesiCampus.getPath(allPathstoDestination, AshesiCampus.listVertices.indexOf(origin), destination);
        int [] allDistancestoDestination = AshesiCampus.dijkstraDistances.get(AshesiCampus.listVertices.indexOf(destination));
        int distanceToDestination = allDistancestoDestination[AshesiCampus.listVertices.indexOf(origin)]; //get the specific distance for given origin
        
        System.out.println("Start from "+ origin + path + ". You will walk " + distanceToDestination + "m. Bon voyage!\n");
        
        }
        
    }
    
       

       
        

    


