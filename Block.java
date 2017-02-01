//READ TODO under main




/**
 * @(#)Block.java
 *
 *
 * @author 
 * @version 1.00 2016/5/2
 */

import java.awt.*;
import javax.swing.*;

public class Block 
{
	final int HEIGHT = 10;
	
	int width;
	int x;
	Color c;
	int type;		//0 = normal, 1 = red, 2 = blue, 3 = orange, 4 = green, 5 = yellow
					//normal	no chop		double points	avoid	grow	slow
	
    public Block(int width, int x, Color c, int type) 
    {
    	this.width = width;
    	this.x = x;
    	this.c = c;
    	this.type = type;
    }
    
    public int getWidth()
    {
    	return width;
    }
    
    public int getX()
    {
    	return x;
    }
    
    public void setX(int x)
    {
    	this.x = x;
    }
    
    public void shorten(int x)
    {
    	width -= x;
    }
    
    public void widen(int x)
    {
    	width += x;
    }
    
    public Color getColor()
    {
    	return c;
    }
    
    public int getType()
    {
    	return type;
    }
    
    public String toString()
    {
    	return "" + width + " " + x + " " + c + " " + type;
    }
}