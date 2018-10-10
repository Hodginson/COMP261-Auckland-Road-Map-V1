package Comp261_Assignment1;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.util.ArrayList;
import java.util.List;

public class Create_Polygons {

	private int type;
	private int endLevel;
	private String label;
	private int cityIdx;
	private PolygonColor color;
	private List<Location> polygon = new ArrayList<Location>();
	
	public Create_Polygons(Integer type, Integer endLevel, String label, Integer cityIdx, List<Location> data, PolygonColor color) {
		this.type = type;
		this.endLevel = endLevel;
		this.label = label;
		this.cityIdx = cityIdx;
		this.color = color;
		for (Location loc : data){
			polygon.add(loc);
		}
	}

	public int getType() {//gets the type
		return type;
	}

	public int getEndLevel() {//gets the endLevel
		return endLevel;
	}	
	
	public String getLabel() {//gets the label
		return label;
	}

	public int getCityIdx() {//gets the city index
		return cityIdx;
	}

	public List<Location> getPolygon() {//gets the polygon
		return polygon;
	}
	
	public PolygonColor getColor() {//gets the colour
		return color;
	}
	
	public void draw(Graphics g, Location origin, double scale, int Type) {//draws the polygons
		if (this.type == Type ) {
			int[] PointX = new int[polygon.size()];
			int[] PointY = new int[polygon.size()];
			for (int i=0; i<this.polygon.size(); i++) {
				Point point = polygon.get(i).asPoint(origin, scale);
				Integer X = (int) point.getX();
				Integer Y = (int) point.getY();
				PointX[i] = X;
				PointY[i] = Y;
			}
			int Points = PointX.length;
			g.setColor(new Color(color.Red, color.Green, color.Blue));
			g.fillPolygon(PointX, PointY, Points); 
		}
	}
	

}
