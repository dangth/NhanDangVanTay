/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import java.io.Serializable;

public class Pixel implements Serializable{
	private float red;		// red value
	private float green;		// green value
	private float blue;		// blue value
	
	public Pixel() {
		red = 0f;
		green = 0f;
		blue = 0f;
	}
	
	public Pixel(float red, float green, float blue) {
		this.red = red;
		this.green = green;
		this.blue = blue;
	}
	
	public float getRed() {
		return red;
	}
	
	public void setRed(float red) {
		this.red = red;
	}
	
	public float getGreen() {
		return green;
	}
	
	public void setGreen(float green) {
		this.green = green;
	}
	
	public float getBlue() {
		return blue;
	}
	
	public void setBlue(float blue) {
		this.blue = blue;
	}
	
	public float getGray() {
		return (red + green+blue)/3;
	}
}

