package Comp261_Assignment1;
import java.util.ArrayList;
import java.util.List;
import java.awt.*;


public class Create_Nodes {

	private int id;
	private Location location;
	private List<Create_Segments> segments = new ArrayList<Create_Segments>();

	public Create_Nodes(String line) {
		String[] l = line.split("\t");
		this.id = Integer.parseInt(l[0]);
		this.location = Location.newFromLatLon(Double.parseDouble(l[1]), Double.parseDouble(l[2]));
		}
	
	public Create_Nodes(int id, Location location) {
		this.location = location;
		this.id = id;
	}


	// Gets the ID
	public int getId(){
		return id;
	}

	// Gets the Location
	public Location getLocation(){
		return this.location;
	}

	// Adds the segments
	public void addSegment(Create_Segments segment){
		segments.add(segment);
	}

	// Gets the Segments
	public List<Create_Segments> getSegments(){
		return segments;
	}

	// Draws the nodes
	public void draw(Graphics g, Location origin, double scale, int NodeSize) {
		Point drawNode = this.location.asPoint(origin, scale);
		g.fillRect((int) drawNode.getX()-NodeSize/2, (int) drawNode.getY()-NodeSize/2, NodeSize, NodeSize);
	}
}
