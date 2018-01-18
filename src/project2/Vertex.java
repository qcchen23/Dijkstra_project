package project2;

public class Vertex {

	protected int ID;
	protected int dValue; // Integer.MAX_VALUE by default
	protected int indexInHeap; // -1 if not in heap
	
	// For drawing
	protected int x;
	protected int y;
	
	public Vertex(int ID, int x, int y){
		this(ID, x, y, Integer.MAX_VALUE, -1);
	}
	
	public Vertex(int ID, int x, int y, int dValue, int indexInHeap){
		this.ID=ID;
		this.dValue=dValue;
		this.indexInHeap=indexInHeap;
		this.x=x;
		this.y=y;
	}
	
    public int compareTo(Vertex v){
    	// compare d-values for heap operations
    	if (dValue < v.dValue)
    		return -1;
    	if (dValue==v.dValue)
    		return 0;
    	return 1;
    }
    
    public String toString(){
    	return "("+indexInHeap+","+dValue+")";
    }
	
	
}
