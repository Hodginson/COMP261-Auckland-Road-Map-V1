package Comp261_Assignment1;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class Map_Main extends GUI {

	private Location origin;
	private double scale = 30;	
	private double north = Double.NEGATIVE_INFINITY;
	private double south = Double.POSITIVE_INFINITY;
	private double east = Double.NEGATIVE_INFINITY;
	private double west = Double.POSITIVE_INFINITY;
	

	private Map<Integer, Create_Nodes> nodesMap = new HashMap<Integer, Create_Nodes>(); //a map to store the nodes
	private Map<Integer, Create_Roads> roadsMap = new HashMap<Integer, Create_Roads>(); // a map to store the roads
	private Set<Create_Polygons> polygonSet = new HashSet<Create_Polygons>();
	private Map<Integer, PolygonColor> colours = new TreeMap<Integer,PolygonColor>();//the colour set for the polygons
	
	// for the trie tree method
	Trie_tree trieRoot = new Trie_tree();
	List<Create_Roads> SelectedRoads = new ArrayList<Create_Roads>();
	
	//for the quad tree
	private Create_Nodes currentNode; 
	private Quad_Tree quadRoot = null;

	public Map_Main() {	// defines the Color set for polygons
		colours.put(2, new PolygonColor(142, 145, 142)); //city - Light Grey
		colours.put(5, new PolygonColor(174, 40, 58)); //Car park - Reddish purple
		colours.put(7, new PolygonColor(66, 66, 66)); //Airport - Dark grey
		colours.put(8, new PolygonColor(226, 126, 4)); //Shopping Center - Orange
		colours.put(10, new PolygonColor(250, 255, 0)); //University - Yellow
		colours.put(11, new PolygonColor(173, 24, 24)); //Hospital - Red
		colours.put(14, new PolygonColor(206, 206, 202)); //Airport Runway - Grey
		colours.put(19, new PolygonColor(163, 108, 26)); //Man made area - Brown/Orange
		colours.put(22, new PolygonColor(79, 188, 78)); //National park - Green
		colours.put(23, new PolygonColor(9, 232, 42)); //city park - Green
		colours.put(24, new PolygonColor(0, 255, 4)); //golf course - Light Green
		colours.put(25, new PolygonColor(12, 209, 137)); //sport - turquoise 
		colours.put(26, new PolygonColor(125, 61, 165)); //Cemetery - Purple
		colours.put(30, new PolygonColor(0, 178, 38)); //state park - Green
		colours.put(40, new PolygonColor(65, 103, 226)); // OCEAN - Blue
		colours.put(60, new PolygonColor(7, 244, 189)); //lake baby blue
		colours.put(62, new PolygonColor(7, 244, 189)); //lake
		colours.put(64, new PolygonColor(65, 172, 226)); // RIVERS light blue
		colours.put(65, new PolygonColor(65, 172, 226)); // RIVERS
		colours.put(69, new PolygonColor(65, 172, 226)); // RIVERS
		colours.put(71, new PolygonColor(65, 172, 226)); // RIVERS
		colours.put(72, new PolygonColor(65, 172, 226)); // RIVERS
	    colours.put(80, new PolygonColor(0, 68, 1)); // Woods - dark green
	}

	

	protected void setOrigin() { //finds the centre point of the map
		double[] Map_Limits = this.setMap_Limits();
		scale = Math.min(400 / (Map_Limits[1] - Map_Limits[0]),
				400 / (Map_Limits[2] - Map_Limits[3]));
		origin = new Location(Map_Limits[0], Map_Limits[2]);
		double a = Map_Limits[1] ;
		double b = Map_Limits[2] ;
		System.out.printf("Failed to open file: %s, %s, %s",a,b,origin);
	}
	
	@Override
	protected void onLoad(File nodes, File roads, File segments, File polygons) { //called when the user successfully loads the files. polygons may be null depending on the size of the map they are after
		this.loadData(nodes, roads, segments, polygons);
		this.setOrigin();
	}

	
	 //loads the data from the files, checks if polygons is null

	protected void loadData(File nodes, File roads, File segments, File polygons) {
		this.loadRoads(roads);
		this.loadNodes(nodes);
		this.loadSegments(segments);
		if (polygons != null) { 
		this.loadPolygons(polygons);
		}
	}

	 //load the nodes from the Node file, adds them to the Map

		protected void loadNodes(File nodes) {
			try {
				BufferedReader data = new BufferedReader(new FileReader(nodes));
				while (data.ready()) {
					String line = data.readLine(); 	// Read the data and creates a node
					if (line.length() > 0) {
						Create_Nodes node = new Create_Nodes(line); // Add the node to the node map
						nodesMap.put(node.getId(), node);
					}
				}
				// Generates a quad-tree structure for the nodes 
				double[] bounds = this.setMap_Limits();
				quadRoot = new Quad_Tree(new Location(bounds[0], bounds[2]), new Location(bounds[1], bounds[3]));
				// Add all the nodes to the quad-tree 
				for(Create_Nodes node : nodesMap.values()) {
					quadRoot.addNode(node);
				}
				data.close();
			} catch (IOException e) {
				System.out.printf("Failed to open file: %s, %s", e.getMessage(),
						e.getStackTrace());
			}
		}
	
	
	//loads the roads from text file, adds them to the Map

	protected void loadRoads(File roads) {
		try {
			BufferedReader data = new BufferedReader(new FileReader(roads));
			data.readLine();// skips the first line as it contains headers
			while (data.ready()) {
				String line = data.readLine(); // Read the data and creates a road
				Create_Roads road = new Create_Roads(line); //adds the road to the map
				roadsMap.put(road.getId(), road);
				trieRoot.addRoad(road);
			}
			data.close();
		} catch (IOException e) {
			System.out.printf("Failed to load: %s, %s", e.getMessage(),e.getStackTrace());//prints the path if it fails
		}
	}

	
	 //load all segments from text file and adds them to "nodeMap" & "roadMap"

	protected void loadSegments(File segments) {
		try {
			BufferedReader data = new BufferedReader(new FileReader(segments));
			data.readLine(); // skips the first line as it contains headers
			while (data.ready()) { 
				String line = data.readLine();
				Create_Segments segment = new Create_Segments(line, roadsMap, nodesMap);
				Create_Roads road = segment.getRoad();// If a road exists add a new segment
				if (road != null) {
					road.addSegment(segment);
				}
				Create_Nodes nodeStart = segment.getNodeStart(); // If a node exists then add a new segment
				if (nodeStart != null) {
					nodeStart.addSegment(segment);
				}
				Create_Nodes nodeEnd = segment.getNodeEnd();
				if (nodeEnd != null) {
					nodeEnd.addSegment(segment);
				}
			}
			data.close();
		} catch (IOException e) {
			System.out.printf("Failed to load: %s, %s", e.getMessage(),e.getStackTrace());
		}
	}

	
	 //load polygon file into memory
	
	protected void loadPolygons(File polygons){
		Integer polygonType =0;
		String label = "";
		Integer endLevel = 0;
		Integer cityIndex = 0;
		List<Location> coordinates = new ArrayList<Location>();
		Set<Integer> type = new TreeSet<Integer>();

		try {
			BufferedReader data = new BufferedReader(new FileReader(polygons));
			while (data.ready()){
				String line = data.readLine();
					if (line.startsWith("Type=")){
						polygonType = Integer.parseInt(line.substring(7),16);
						// Add polyType to a temp set, to check how many colors to use
						type.add(polygonType);
					} else if (line.startsWith("Label=")) {
						label = line.substring(6);
					} else if (line.startsWith("EndLevel=")) {
						endLevel = Integer.parseInt(line.substring(9));
					} else if (line.startsWith("CityIdx=")) {
						cityIndex = Integer.parseInt(line.substring(8));
					} else if (line.startsWith("Data0=")) {
						String strCoords = line.substring(6);
						coordinates.clear();
			
						String[] coordArray = strCoords.substring(1,strCoords.length()-2).split("\\),\\(",-1);// Splits the coordList String and separates them into X and Y
						for (int i=0;i<coordArray.length;i++){
							Double X = Double.parseDouble(coordArray[i].split(",")[0]);
							Double Y = Double.parseDouble(coordArray[i].split(",")[1]);
							coordinates.add(Location.newFromLatLon(X, Y));
						}
						Create_Polygons polyShape = new Create_Polygons(polygonType, endLevel, label, cityIndex, coordinates, colours.get(polygonType));
						polygonSet.add(polyShape);
					}
				}
			data.close();
		}
		catch (IOException e){
			System.out.printf("Failed to load %s, %s", e.getMessage(), e.getStackTrace());
		}
	}

	


	// set the max and min location

	protected double[] setMap_Limits() {
		for (Create_Nodes nodes : nodesMap.values()) {
			if (nodes.getLocation().y > north)
				north = nodes.getLocation().y;
			if (nodes.getLocation().x > east)
				east = nodes.getLocation().x;
			if (nodes.getLocation().y < south)
				south = nodes.getLocation().y;
			if (nodes.getLocation().x < west)
				west = nodes.getLocation().x;			
		}
		return new double[] { west, east, north, south };
	}

	
	@Override
	protected void onMove(Move m) { //called when a button on the gui is pressed
		double zoomFactor = 1.5;
		double factor = 100/scale;
		switch (m) {
		case NORTH: {
			origin = origin.moveBy(0, factor);
			this.redraw();
			break;
		}
		case SOUTH: {
			origin = origin.moveBy(0, -factor);
			this.redraw();
			break;
		}
		case EAST: {
			origin = origin.moveBy(factor, 0);
			this.redraw();
			break;
		}
		case WEST: {
			origin = origin.moveBy(-factor, 0);
			this.redraw();
			break;
		}
		case ZOOM_IN: {
			double NewOrigin = super.getDrawingAreaDimension().getHeight()
					/ scale * (zoomFactor - 1) / zoomFactor / 2;
			double a = scale * (zoomFactor - 1) / zoomFactor / 2;
			System.out.printf("Failed to load %s, %s", NewOrigin,a);
			origin = new Location(origin.x + NewOrigin, origin.y - NewOrigin);
			scale = scale * zoomFactor;
			this.redraw();
			break;
		}
		case ZOOM_OUT: {
			scale = scale / zoomFactor;
			double newOrigin = super.getDrawingAreaDimension().getHeight()
					/ scale * (zoomFactor - 1) / zoomFactor / 2;
			origin = new Location(origin.x - newOrigin, origin.y + newOrigin);
			this.redraw();
			break;
		}
		}
		return;
	}
	
	@Override
	protected void redraw(Graphics g) {
		this.draw(g, origin, scale,
				super.getDrawingAreaDimension().getHeight(), super
						.getDrawingAreaDimension().getWidth());
	}
		 
	@Override
	protected void onClick(MouseEvent click) {
		// When the user clicks the coordinates are found
		if (click.getButton() == 1) {
			int x = click.getPoint().x;
			int y = click.getPoint().y;
			Location clickedCoordinates = Location.newFromPoint(new Point(x, y), this.origin, this.scale);
			System.out.printf("Failed to load: %s", clickedCoordinates);
			// Find quad tree in area then query the children for intersections
			Quad_Tree childNode = quadRoot.getCoordinates(clickedCoordinates.x, clickedCoordinates.y);
			for (Create_Nodes node : childNode.getNodeList()) {
				if (node.getLocation().isClose(clickedCoordinates, 0.8)) {//the margin of error for a click if the quad node will find it. initially had it too high so selecting nodes was tough as it would always auto select
					int nodeID = node.getId();
					String selectedNode = String.format("Node ID: %d \n Intersection For:\t",nodeID);
					getTextOutputArea().setText(selectedNode);
					List<Create_Segments> segmentList = node.getSegments();
					for (Create_Segments segments : segmentList) {
						getTextOutputArea().append(segments.getRoad().getName()+"\t");
					}
					currentNode = node;					
				}
			}
		}
	}

	@Override
	protected void onSearch() { //called when the search box has something entered or removed from it
		String RequiredRoad = getSearchBox().getText();
		String SearchString = "";
		Set<String> roadNames = new HashSet<String>();
		if (RequiredRoad.length() > 0) {
			Trie_tree node = trieRoot.FindRoad(RequiredRoad);
			if (node != null) {
				SelectedRoads = node.getRoads();
				for (Create_Roads road : SelectedRoads) {
					if (!roadNames.contains(road.getName())) {
						SearchString = SearchString + "\n" + road.getName();
						roadNames.add(road.getName());
					}
				}
				getTextOutputArea().setText(SearchString);
			} else
				SelectedRoads.clear();
		}
	}

	

	
	protected void draw(Graphics g, Location origin, double scale, double screenH, double screenW) {

		//allows the program to know where the user has clicked without recalling draw
		this.origin = origin;
		this.scale = scale;
		if (nodesMap.size() > 0) {

			// Draw the polygons
			for (Create_Polygons polygons : polygonSet) {
				for (Integer entry : colours.keySet()) {
					polygons.draw(g, origin, scale, entry);
				}
			}
			// Draw the segments
			for (Create_Roads roads : roadsMap.values()) {
				if (SelectedRoads.contains(roads)) {
					g.setColor(Color.RED);
					for (Create_Segments segments : roads.getSegments()) {
						segments.draw(g, origin, scale, 1);
					}
				} else {
					g.setColor(Color.BLACK);
					for (Create_Segments segments : roads.getSegments()) {
						segments.draw(g, origin, scale, 1);
					}
				}
			}
			// Draw the nodes
			for (Create_Nodes nodes : nodesMap.values()) {
						if (nodes == currentNode){
							g.setColor(Color.RED);
						}
						else {
							g.setColor(Color.BLUE);
						}
						int nodeSize = (int) Math.min(.5 * (scale),6);
						nodes.draw(g, origin, scale, nodeSize);
					}
				}
			}

	// Main method
	public static void main(String[] args) {
		new Map_Main();
	}
}
