package Comp261_Assignment1;



import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;

public class Quad_Tree {

	private static final int Node_Limit = 4;
	private List<Create_Nodes> nodesList = new ArrayList<Create_Nodes>();
	private Map<String, Quad_Tree> children = new HashMap<String, Quad_Tree>();
	private Location topLeft, bottomRight, centre;

	public Quad_Tree(Location topLeft, Location bottomRight) {
		this.topLeft = topLeft;
		this.bottomRight = bottomRight;
		setCentre();
	}

	public List<Create_Nodes> getNodeList() { //gets the list of nodes
		return nodesList;
	}

	public Map<String, Quad_Tree> getChildren() { //gets the children
		return children;
	}

	public Location getTopLeft() { //gets the top left position
		return topLeft;
	}
	
	public Location getBottomRight() { // gets the bottom right position
		return bottomRight;
	}
	
	public void setNodeList(List<Create_Nodes> nodeList) { //sets the list of nodes
		this.nodesList = nodeList;
	}

	public void setChildren(Map<String, Quad_Tree> children) { //sets the children
		this.children = children;
	}

	public void setTopLeft(Location topLeft) { //sets the top left position
		this.topLeft = topLeft;
	}

	public void setBottomRight(Location bottomRight) { //sets the bottom right position
		this.bottomRight = bottomRight;
	}

	//if the area contains more than the limit on nodes it splits
	public void addNode(Create_Nodes node) {
		if (children.size() > 0) {// Check if the current area already has children
			String nodeQuad = this.getNodeQuadrant(node);
			children.get(nodeQuad).addNode(node);
			return;
		}
		nodesList.add(node);
		if (nodesList.size() >= Node_Limit) {//split if there are more than 4 nodes in a area
			SplitQuad();
		}
	}
	  
	private void setCentre() {//Set the centre location of the quad 
		double centreX = (topLeft.x + bottomRight.x) / 2;
		double centreY = (topLeft.y + bottomRight.y) / 2;
		this.centre = new Location(centreX, centreY);
	}
	
	public Quad_Tree getCoordinates(double x, double y) {
		String Coordinates = getQuadrantFromPosition(x, y);
		if(children.containsKey(Coordinates)) {
			return children.get(Coordinates).getCoordinates(x, y);
		}
		return this;
	}
	
	public String getQuadrantFromPosition(double x, double y){
		String quad = "OUT OF BOUNDS";
		if (x > centre.x) {
			if (y > centre.y) {
				quad = "NorthEast";
			} else {
				quad = "SouthEast";
			}
		} else {
			if (y > centre.y) {
				quad = "NorthWest";
			} else {
				quad = "SouthWest";
			}
		}
		return quad;
	}	
	
	private String getNodeQuadrant(Create_Nodes node) {
		String quad = "OUT OF BOUNDS";
		if (node.getLocation().x > centre.x) {// Checks if it's in the positive X quad
			if (node.getLocation().y > centre.y) {			// Checks if it's in the positive Y quad
				quad = "NorthEast";// in first quad
			} else {
				quad = "SouthEast";// in forth quad
			}
		} else {
			if (node.getLocation().y > centre.y) {
				quad = "NorthWest";//in second quadrant
			} else {
				quad = "SouthWest";	//in third quadrant
			}
		}
		return quad;
	}
	 
	private void SplitQuad() {//splits the quad into 4 and reassigns the children
		Location NorthWestTopLeft = topLeft;
		Location NorthEastTopLeft = new Location(centre.x, topLeft.y);
		Location SouthWestTopLeft = new Location(topLeft.x, centre.y);
		Location SouthEastTopLeft = new Location(centre.x, centre.y);
		
		Location NorthWestBottomRight = centre;
		Location NorthEastBottomRight = new Location(bottomRight.x, centre.y);		
		Location SouthWestBottomRight = new Location(centre.x, bottomRight.y);
		Location SouthEastBottomRight = new Location(bottomRight.x, bottomRight.y);		

		children.put("NorthWest", new Quad_Tree(NorthWestTopLeft, NorthWestBottomRight));
		children.put("NorthEast", new Quad_Tree(NorthEastTopLeft, NorthEastBottomRight));
		children.put("SouthWest", new Quad_Tree(SouthWestTopLeft, SouthWestBottomRight));
		children.put("SouthEast", new Quad_Tree(SouthEastTopLeft, SouthEastBottomRight));

		for (Create_Nodes node : nodesList) {//adds the nodes to the correct children
			String quad = this.getNodeQuadrant(node);
			children.get(quad).addNode(node);
		}
	}

}