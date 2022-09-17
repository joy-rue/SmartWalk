/**
 @author Ruvimbo Joy Sithole
 @author Faustina Awuntayami Ama
 * Creates an edge that stores values for the sources and destination to which it is incident
 */
public class Edge {

    String source;
    String destination; 
    int weight;

    /**
     * The constructor
     * @param src the source vertex
     * @param dst the destination vertex
     * @param value the weight of the edge 
     */
    public Edge(String src, String dst, int value){
        source = src;
        destination = dst; 
        weight = value;
    }

    /**
     * gets the vertices associated with the edge
     * @return String of the vertices (source, destination)
     */
    String getElements(){

        // Could this just have been set(this.source, this.destination)
        return "(" + this.source + ", " + this.destination + ")";
    }

    /**
     * Retrieves the destination vertex from the given source u
     * @param v source vertex
     * @return vertex that shares the same edge as u
     */
    String opposite(String v){
        if (v == source){
        return destination;
        }
        else 
        return source;
    }

    /**
     * @return the weight of the edge
     */
    int distance(){
        return weight;
    }
}
