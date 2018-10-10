package Comp261_Assignment1;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.awt.*;
import java.awt.geom.Line2D;

public class Create_Segments {

	private Create_Roads road; // Name of the road that the segment belongs to
	private double road_length; // Length of the segment
	private Create_Nodes firstNode; // where the segment starts
	private Create_Nodes secondNode; // where the segment ends
	private List<Location> co_ordinates = new ArrayList<Location>();//creates a list of the segment coordinates

	public Create_Segments(String line, Map<Integer, Create_Roads> roads, Map<Integer, Create_Nodes> nodes) {
		String[]l = line.split("\t");
		road = roads.get(Integer.parseInt(l[0]));
		road_length = Double.parseDouble(l[1]);
		firstNode = nodes.get(Integer.parseInt(l[2]));
		secondNode = nodes.get(Integer.parseInt(l[3]));
		for (int i=4; i<l.length; i=i+2) { //adds the segments coordinates
			co_ordinates.add(Location.newFromLatLon(Double.parseDouble(l[i]), Double.parseDouble(l[i+1])));
		}
	}
	
	public Create_Roads getRoad(){ //gets the road
		return road;
	}
	
	public double getLength(){ //gets the length
		return road_length;
	}
	
	public Create_Nodes getNodeStart(){ // gets the start point
		return firstNode;
	}
	
	public Create_Nodes getNodeEnd(){ //gets the end point
		return secondNode;
	}
	

	public void draw(Graphics g, Location origin, double scale, int road_Thickness) {//draws the segments
		Graphics2D Draw_road = (Graphics2D) g;
        Draw_road.setStroke(new BasicStroke(road_Thickness,BasicStroke.CAP_ROUND, road_Thickness));
		Point point1 = co_ordinates.get(0).asPoint(origin, scale);
		for (int i=1; i<co_ordinates.size(); i++){
			Point point2 = co_ordinates.get(i).asPoint(origin, scale);
			Draw_road.draw(new Line2D.Float((int) point1.getX(), (int) point1.getY(), (int) point2.getX(), (int) point2.getY()));			
			point1=point2;
		}
	}
}
