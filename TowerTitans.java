//READ TODO under main




/**
 * @(#)TowerTitans.java
 *
 *
 * @author 
 * @version 1.00 2016/5/2
 */

import java.util.*;

public class TowerTitans
{	
	final int LEFTB = 50;
	final int RIGHTB = 750;

	private ArrayList<Block> tower;
	
	
    public TowerTitans()
    {
    	tower = new ArrayList<>();
    }
    
    private Block cutL(Block b)
    {
    	if (b.getX() < LEFTB)
    	{
    		int wC = Math.abs(b.getX() - LEFTB);
    		return new Block(b.getWidth() - wC, LEFTB, b.getColor(), b.getType());
    	}
    	else
    		return b;
    }
    
    private Block cutR(Block b)
    {
    	if (b.getX() + b.getWidth()> RIGHTB)
    	{
    		int wC = Math.abs(b.getX() + b.getWidth() - RIGHTB);
    		return new Block(b.getWidth() - wC, b.getX(), b.getColor(), b.getType());
    	}
    	else
    		return b;
    }
    
    public int add(Block b)		//maybe use points instead of width
    {
    	b = cutL(b);
    	b = cutR(b);
    	if (testHit(b))			//b hit the tower, give back change in width
    	{
    		//test type of block, what add in is reflective of its width properties
	    	if (b.getType() == 1)	//red, no chop
	    	{
	    		tower.add(b);
	    		return 0;
	    	}
	    	else if (b.getType() == 3)	//orange, must avoid
	    		return Integer.MAX_VALUE;
	    	else if (b.getType() == 4)	//green, grows
	    	{
    			if (getPrev() != null)
    			{
    				int w = Math.abs(b.getX() - getPrev().getX());
    				b.widen(w);
	    			if (b.getX() >= getPrev().getX())
	    			{
	    				b.setX(getPrev().getX());
	    			}
	    			tower.add(b);
	    			return 0 - w;
    			}
	    	}
	    	else	//norm, blue, or yellow are chopped
	    	{
	    		if (getPrev() == null)	//first block always ok
	    		{
	    			tower.add(b);
	    			return 0;
	    		}
	    		//if not perfect on return width difference
	    		else if (!(b.getX() >= getPrev().getX() && b.getX() + b.getWidth() <= getPrev().getX() + getPrev().getWidth()))
	    		{
	    			int w = Math.abs(b.getX() - getPrev().getX());
	    			//if on left shorten and move x coord, if on right shorten
	    			b.shorten(w);
	    			if (b.getX() < getPrev().getX())
	    				b.setX(getPrev().getX());
	    			tower.add(b);
	    			return w;
	    		}
	    		//else is perfect
	    		tower.add(b);
	    		return 0;
	    	}
    	}
    	else		//b missed the tower
    	{
    		if (b.getType() == 3)
    			return 0;
    		return Integer.MAX_VALUE;
    	}
    	return Integer.MAX_VALUE;
    }
    
    public Block get(int index)
    {
    	if (index < 0)
    		return tower.get(0);
    	else if (index >= getTowerHeight())
    		return tower.get(getTowerHeight() - 1);
    	return tower.get(index);
    }
    
    public Block getPrev()
    {
    	if (getTowerHeight() <= 0)
    		return null;
    	return tower.get(getTowerHeight()-1);
    }
    
    public Block get2Prev()
    {
    	if (getTowerHeight() <= 1)
    		return null;
    	return tower.get(getTowerHeight()- 2);
    }
    
    public boolean gameover() //still work in progress
    {
    	Block curr = getPrev();
    	Block past = get2Prev();
    	if (past != null)
    	{
    		if (curr.getX() + curr.getWidth() >= past.getX() && curr.getX() <= past.getX() + past.getWidth())
    		{
    			if (curr.getType() == 3)
    				{
    					return true;
    				}
    			return false;
    		}
    		else
    			return true;
    	}
    	return false;
    }
    
    public boolean testHit(Block b)
    {
    	//test most recent block versus previous
    	Block curr = b;
    	Block past = getPrev();
    	if (curr != null && past != null)
    	{
    		if (curr.getX() + curr.getWidth() >= past.getX() && curr.getX() <= past.getX() + past.getWidth())
    		{
    			return true;
    		}
    		else
    			return false;
    	}
    	return true;		//for first block
    }
    
    public int getTowerHeight()
    {
    	return tower.size();
    }
    
    public String toString()
    {
    	return tower.toString();
    }
    
   	
}