package Comp261_Assignment1;

 
public class PolygonColor {

	int Red;
	int Green;
	int Blue;

	public PolygonColor(int red, int green, int blue) { //converts the ints to RGB
		this.Red = red % 255;
		this.Green = green % 255;
		this.Blue = blue % 255;
	}

	public int getRed() {//gets the red value
		return Red;
	}

	public int getGreen() { //gets the green value
		return Green;
	}

	public int getBlue() { //gets the blue value
		return Blue;
	}
}
