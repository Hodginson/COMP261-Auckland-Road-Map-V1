package Comp261_Assignment1;
import java.util.*;


public class Create_Roads {

	// Field variables
	private int id;
	private String name;
	private String city;
	private boolean oneway;
	private int speed;
	private int roadType;
	private boolean NoCars;
	private boolean noPedestrians;
	private boolean NoBikes;
	private List<Create_Segments> segments = new ArrayList<Create_Segments>();//creates a list of the segments
	
	
	public Create_Roads(String line) {
		String l[] = line.split("\t");
		id = Integer.parseInt(l[0]);
		name = l[2];
		city = l[3];
		oneway = l[4].equals("1");
		speed = Integer.parseInt(l[5]);
		roadType = Integer.parseInt(l[6]);
		NoCars = l[7].equals("1");
		noPedestrians = l[8].equals("1");
		NoBikes = l[9].equals("1");
	}
	
	
	public void addSegment(Create_Segments seg) {// Adds the segments
		segments.add(seg);
	}
	
	
	public int getId() {// Gets the id
		return id;
	}
	
	
	public List<Create_Segments> getSegments () {// Gets the segments
		return segments;
	}
	
	
	public String getName() {// Gets the street name
		return name;
	}
	
	
	public String getCity() {// Gets the city name
		return city;
	}

	
	public boolean isOneway() {//checks if it is one way
		return oneway;
	}
	

	public int getSpeed() {	// Gets the speed limit
		return speed;
	}
	

	public int getRoadType() {	// Gets the road class
		return roadType;
	}
	
	
	public boolean getNoPedestrians() { // checks if pedestrians are allowed
		return noPedestrians;
	} 

	public boolean getNoBikes() { // checks if bikes are allowed
		return NoBikes;
	}

	public boolean getNoCars() {	// checks if cars are allowed
		return NoCars;
	}

}
