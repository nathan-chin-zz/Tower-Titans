//READ TODO under main




/**
 * @(#)GUI.java
 *
 *
 * @author 
 * @version 1.00 2016/5/2
 */
 import java.awt.*;
 import java.awt.event.*;
 import java.awt.Image;
 import java.awt.image.BufferedImage;
 import java.awt.geom.*;
 import javax.swing.*;
 import javax.imageio.*;
 import java.io.*;

public class GUI extends JComponent
{
    private Graphics2D gr;
//    public static JFrame frame;
    public static Image bgimage = null;
    
    private int xcount;				//xcoord of block
    private int ycount;				//ycoord of block
    private int width;				//width of block
    private Color col;				//color of block
    private int speed;				//speed of block
    
    final public int boundL;
    final public int boundR;
    
    private final static int screenWidth = 800;
    private final static int screenHeight = 600;
    
    private boolean dir;
    private boolean lr;				//false = left
    private boolean mode = false;	//false = move, true = drop (clicked)
    private boolean placed = false;	//false = block is old
    private TowerTitans tower;
    private Block currBlock;
    private int ycount2 = 0;		//for ycoord of tower
    private boolean gameOver;
    
    private int points = 0;
    final private int OK_POINTS = 100;
    final private int GOOD_POINTS = 150;
    final private int GOOD_SPEED = 2;
    final private int OK_SPEED = 1;
    final private int BAD_SPEED = -1;
    final private int SPEED_LIMIT = 9;
    final private int pX = 600;
    final private int pY = 100;
    
   	private static Timer drawTimer;
    private static JButton q1 = new JButton("Quit");
    private static JButton main = new JButton("Main Menu");
    private static JButton replay = new JButton("Replay");
    private static BufferedImage img;
    private static JLayeredPane lpane = new JLayeredPane();
    
    private float colorSeed;
    final private float SAT = 0.8f;
    final private float BRI = 0.75f;
    final private Color ORANGE = new Color(255, 128, 0);
    final private int NUMCOLORS = 25;
    
    final private int MAX_WIDTH = 200;
    final private int START_POS = 20;
    final private int MIN_SPEED = 5;
    final private int HEIGHT = 20;
    final private int DROP_SPEED = 20;
    final private int DROP_MOVE = 2;
    final private int BOUND_WIDTH = 50;
    final private int MAX_HEIGHT = 20;
    final private int POWER_BORDER = 5;
    
    final private int NORM_ID = 0;
    final private int RED_ID = 1;
    final private int BLUE_ID = 2;
    final private int ORANGE_ID = 3;
    final private int GREEN_ID = 4;
    final private int YELLOW_ID = 5;
    
    final private float DASH_WIDTH = 10.0f;
    final static private int FONT_SIZE = 30;
    
    final private int NUMBERS = 101;
    final private int NORMAL_SPAWN = 60;
    final private int YELLOW_SPAWN = 70;
    final private int BLUE_SPAWN = 80;
    final private int ORANGE_SPAWN = 90;
    final private int GREEN_SPAWN = 95;
    
    
    /**
     * Creates a new instance of <code>GUI</code>.
     */
    public GUI()
    {
    	reset();    	
    	boundL = BOUND_WIDTH;
    	boundR = screenWidth - BOUND_WIDTH;
    }
    
    public void reset()
    {
    	width = MAX_WIDTH;
    	ycount = START_POS;
    	ycount2 = 0;
    	colorSeed = (float)Math.random();
    	col = randC();
    	speed = MIN_SPEED;
    	if (!randB())		//start left
    	{
    		xcount = 0;
    		dir = false;
    		lr = false;
    	}
    	else				//start right
    	{
    		xcount = screenWidth - width;
    		dir = true;
    		lr = true;
    	}
    	//build first block
    	currBlock = new Block(width, xcount, col, 0);
    	tower = new TowerTitans();
    	mode = false;
    	placed = false;
    	gameOver = false;
    	points = 0;
    }
    
    public boolean gameStatus()
    {
    	return gameOver;
    }
    
    private void paintTower(Graphics2D g2, int bot)
    {
    	//draw all blocks from towertitans for previous tower
    	for (int a = 0; a < tower.getTowerHeight(); a ++)
    	{
    		Block c = tower.get(a);
    		g2.setColor(c.getColor());
    		Rectangle r = new Rectangle(c.getX(), getHeight() - ((a + 1) * HEIGHT) + bot, c.getWidth(), HEIGHT);
    		g2.draw(r);
    		g2.fill(r);
    	}
    }
    
    private void paintType(Graphics2D g2, int type)
    {
    	if (type == NORM_ID)
    		return;
    	else if (type == RED_ID)
    		g2.setColor(Color.RED);
    	else if (type == BLUE_ID)
    		g2.setColor(Color.BLUE);
    	else if (type == ORANGE_ID)
    		g2.setColor(ORANGE);
    	else if (type == GREEN_ID)
    		g2.setColor(Color.GREEN);
    	else if (type == YELLOW_ID)
    		g2.setColor(Color.YELLOW);
    	Rectangle rec = new Rectangle (0, 0, screenWidth, HEIGHT - POWER_BORDER);
    	g2.draw(rec);
    	g2.fill(rec);
    }
    
    private void gameEnd(Graphics2D g2)
    {
    	g2.setColor(Color.BLACK);
    	Rectangle scr = new Rectangle(0, 0, screenWidth, screenHeight);
    	g2.draw(scr);
    	g2.fill(scr);
    	
    	int h = HEIGHT;
    	boolean ratio = false;
    	if (tower.getTowerHeight() > MAX_HEIGHT)
    	{
    		h = screenHeight * 5 / (6 * tower.getTowerHeight());
    		ratio = true;
    	}
    	
    	if (ratio)
    	{
    		for (int a = 0; a < tower.getTowerHeight(); a ++)
    		{
    			Block c = tower.get(a);
    			g2.setColor(c.getColor());
    			Rectangle r = new Rectangle(c.getX(), getHeight() - (a * h) - h, c.getWidth() * 5 / 6, h);
    			g2.draw(r);
    			g2.fill(r);
    		}
    	}
    	else
    	{
    		for (int a = 0; a < tower.getTowerHeight(); a ++)
    		{
    			Block c = tower.get(a);
    			g2.setColor(c.getColor());
    			Rectangle r = new Rectangle(c.getX(), getHeight() - (a * h) - h, c.getWidth(), h);
    			g2.draw(r);
    			g2.fill(r);
    		}
    	}
    }
    
    public int getPoints()
    {
    	return points;
    }
    
	@Override
    public void paintComponent(Graphics g)
    {
    	super.paintComponent(g);
    	Graphics2D g2 = (Graphics2D)g;
    	if (gameOver)
    	{
    		gameEnd(g2);
    		return;
    	}
    	g.drawImage(img, 0, 0, this);
    	//draw tower
    	if (tower.getTowerHeight() < MAX_HEIGHT)
    		paintTower(g2, 0);
    	else
    		paintTower(g2, ycount2);	
    	
		if (!mode)		//going side to side
		{
			if (placed)	//create block
			{
				currBlock = makeBlock();
				placed = false;
			}
			paintType(g2, currBlock.getType());
			Rectangle rect = new Rectangle (xcount, START_POS, width, HEIGHT);
			g2.setColor(col);
			g2.draw(rect);
			g2.fill(rect);
			if (!dir && !lr)
				xcount += speed;
			else if (dir && !lr)
				xcount -= speed;
			else if (dir && lr)
				xcount -= speed;
			else if (!dir && lr)
				xcount += speed;
			
			if (xcount > screenWidth - width)
				dir = !dir;
			else if (xcount < 0)
				dir = !dir;
		}
		else		//going down
		{
			Rectangle rect = new Rectangle (xcount, ycount, width, HEIGHT);
			g2.setColor(col);
			g2.draw(rect);
			g2.fill(rect);
			//draw block going down
			if ((tower.getTowerHeight() >= MAX_HEIGHT && ycount2 % HEIGHT == 0 && ycount >= screenHeight - (MAX_HEIGHT * HEIGHT) - HEIGHT)
					|| (tower.getTowerHeight() < MAX_HEIGHT && ycount >= screenHeight - tower.getTowerHeight() * HEIGHT - HEIGHT))
			{
				boolean t = fall();
				if (t)
					return;
			}
			else
			{
				ycount += DROP_SPEED;
				if (tower.getTowerHeight() >= MAX_HEIGHT && currBlock.getType() != ORANGE_ID)
					ycount2 += DROP_MOVE;
			}
		}
		//draw boundaries
    	Line2D left = new Line2D.Double(boundL, 0, boundL, screenHeight);
    	Line2D right = new Line2D.Double(boundR, 0, boundR, screenHeight);
    	final float dash1[] = {DASH_WIDTH};
        final  BasicStroke dashed = new BasicStroke(1.0f, BasicStroke.CAP_ROUND,
        		BasicStroke.JOIN_MITER, DASH_WIDTH, dash1, 0.0f);
        g2.setStroke(dashed);
        g2.setColor(Color.RED);
        g2.draw(left);
        g2.draw(right);
        
        //draw points
        g2.setFont(new Font("Serif", Font.PLAIN, FONT_SIZE));
    	g2.drawString(getPoints() + "", pX, pY);
    }
	
	private boolean fall()
	{
		currBlock.setX(xcount);
		setMode(false);
		ycount = START_POS;
		xcount = 0;
		dir = false;
		placed = true;
	
		//add into towertitans arraylist
		int testWidth = tower.add(currBlock);
		if (testWidth == Integer.MAX_VALUE)
		{
			gameOver = true;
			return true;
		}
		else
			width -= testWidth;
		//test gameover here, continue going down if game is over
		if (tower.gameover())
		{
			gameOver = true;
			return true;
		}
		//points
		int numpoints = 0;
		if (margin() == 0)
			numpoints += GOOD_POINTS;
		else
			numpoints += OK_POINTS;
		if (currBlock.getType() == BLUE_ID)
			points += 2 * numpoints;
		else
			points += numpoints;
		
		//modify speed
		getSpeed();
		return false;
	}
	
	private void getSpeed()
	{
		if (currBlock.getType() == YELLOW_ID)
			speed = Math.max(MIN_SPEED, speed - GOOD_SPEED);
		if (margin() == -1)		//no previous blocks to compare speed to
			return;
		if (margin() == 0)
			speed += GOOD_SPEED;
		else if (margin() < SPEED_LIMIT)
			speed += OK_SPEED;
		else
			speed = Math.max(MIN_SPEED, speed + BAD_SPEED);
	}
	
	private int margin()
	{
		if (tower.get2Prev() != null)
			return Math.abs(tower.get2Prev().getWidth() - tower.getPrev().getWidth());
		return -1;
	}
    
    private Block makeBlock()
    {
    	//set direction and start left or right
    	if (randB())
    	{
    		xcount = 0;
    		dir = false;
    		lr = false;
    	}
    	else
    	{
    		xcount = screenWidth - width;
    		dir = true;
    		lr = true;
    	}
    	//set color
    	col = randC();
    	//set type
    	int t = randType();
    	return new Block(width, xcount, col, t);	
    }
    
    public void setMode(boolean m)
    {
    	mode = m;
    }
    
    public boolean getMode()
    {
    	return mode;
    }
    
    private boolean randB()			//use to set initial x coord (left or right of screen), boolean
    {
    	int t = ((int)(Math.random() * 2));
    	if (t == 0)
    		return false;
    	return true;
    }
    
    private int randType()
    {
    	int t = (int)(Math.random() * NUMBERS);
    	if (t < NORMAL_SPAWN)
    		return NORM_ID;		//norm
    	if (t < YELLOW_SPAWN)
    		return YELLOW_ID;	//yellow
    	if (t < BLUE_SPAWN)
    		return BLUE_ID;		//blue
    	if (t < ORANGE_SPAWN)
    		return ORANGE_ID;	//orange
    	if (t < GREEN_SPAWN)
    		return GREEN_ID;	//green
    	return RED_ID;		//red
    }
    
    private Color randC()
    {
    	colorSeed += (1.0f/NUMCOLORS);
    	Color ret = Color.getHSBColor(colorSeed, SAT, BRI);
		return ret;
    }
    
    /**
     * @param args the command line arguments
     */

	public static void main(String[] args) throws IOException
	{
		//build frame
		final JFrame frame = new JFrame();
		frame.setSize(screenWidth, screenHeight);
		frame.setTitle("Tower Titans");
		frame.setResizable(false);
		Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
		frame.setLocation(dim.width/2-frame.getSize().width/2, dim.height/2-frame.getSize().height/2);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.add(lpane, BorderLayout.CENTER);
		lpane.setBounds(0, 0, screenWidth, screenHeight);
		
		img = ImageIO.read(new File("Tower Titans Background Screen-01.png"));	//insert new picture, background pic of gameplay
		
		final JPanel firstPanel = new JPanel();
		firstPanel.setLayout(new GridLayout(3,1,2,2));	//3 rows of buttons, 1 columm, 2 space x and y
		//start button
		JButton start = new JButton("Start");
		firstPanel.add(start);
		//instruction button
		JButton instructions = new JButton("Instructions");
		firstPanel.add(instructions);
		//Quit button
		JButton quit = new JButton("Quit");
		firstPanel.add(quit);
		firstPanel.setBounds(300, 325, 200, 200);	//set in coordination with image
		
		JLabel background = new JLabel(new ImageIcon("Tower Titans Title Screen.jpg"));
		JPanel bg = new JPanel();
		bg.add(background);
		bg.setBounds(0, 0, screenWidth, screenHeight);
		bg.setOpaque(true);
		lpane.add(bg, new Integer(0), 0);
		lpane.add(firstPanel, new Integer(1), 0);
		
		final JPanel secondPanel = new JPanel();
		final GUI guiTest = new GUI();
		
		frame.setVisible(true);
		
		//when start button clicked
		class StartClickListener implements ActionListener
		{
			public void actionPerformed (ActionEvent event)
			{
				frame.getContentPane().removeAll();
				frame.repaint();
				secondPanel.setLayout(new BorderLayout());
				secondPanel.add(guiTest);
				frame.add(secondPanel);
				frame.setVisible(true);
			}
		}
		
		//when instruction button clicked
		class InstructionsClickListener implements ActionListener
		{
			public void actionPerformed (ActionEvent event)
			{
				JOptionPane.showMessageDialog(null, 
				"<html><body width = '" + 475 +
				"'><h1>Instructions:</h1>" + 
				"<h3>Objective:</h3> <p>Stack blocks successively on top of each other to build a tower. " + 
				"The object of the game is to build the tallest tower. Points will be gained " +
				"for each block placed, including double points for the blue blocks (see Blocks section). " + 
				"This means that the tallest tower will ultimately gain the most points.<br></br><br></br></p>" +
				"<h3>How to Play:</h3>" +
				"<p>1. Click the close button on this pop-up, and then click the start button on the main screen.<br></br>" + 
				"2.	A block will move back and forth across the screen. Its speed will increase as you place more blocks on.<br></br>" + 
				"3.	Click the mouse when the block is placed above the stack and the block will fall straight down onto the tower.<br></br>" +
				"4.	If the block isn’t placed perfectly on the tower, the size of the block will shrink to the boundaries of the block within " + 
				"the tower; blocks will not be allowed to extend past the red boundaries.<br></br>" +
				"5.	Another block will move back and forth across the screen, so continue to follow previous steps. " + 
				"By missing the tower, the blocks will continue to shrink making it more difficult to place them on the tower successfully.<br></br>" + 
				"6.	You continue to place blocks on the tower until either the orange block is placed on the tower (see Special Blocks section) " + 
				"or if you miss the tower completely.<br></br><br></br></p>" +
				"<h3>Blocks: (color – ability)</h3>" + 
				"<p> These blocks will be indicated by the color of the status bar at the top of the screen.<br></br><br></br>" +
				"Opaque – normal block; sections extending beyond the bounds of the tower are cut.<br></br>" +
				"Red – does not shrink in size if the user misses the tower. " + 
				"However, game over results if this block is placed completely off the tower.<br></br>" +
				"Blue – double points are received for placing this block on the tower; otherwise it acts as a normal block.<br></br>" +
				"Orange – avoid placing this block on the TOP of the tower. " + 
				"Placing this block on the tower results in an immediate game over.<br></br>" +
				"Green – this block will grow to occupy the entire tower base. " +
				"Helpful tip: place it as close to the edge as possible in order to expand the tower's base.<br></br>" +
				"Yellow – slows down the block speed in order to ease placing more blocks onto the tower; otherwise, it acts as a normal block</p>");
			}
		}
		
		//when quit button clicked
		class QuitClickListener implements ActionListener
		{
			public void actionPerformed (ActionEvent event)
			{
				frame.dispose();
			}
		}
		
		ActionListener listener1 = new StartClickListener();
		start.addActionListener(listener1);
		
		ActionListener listener2 = new InstructionsClickListener();
		instructions.addActionListener(listener2);
		
		ActionListener listener3 = new QuitClickListener();
		quit.addActionListener(listener3);
		
		
		// Listener for timer to re-draw
		class AdvanceTimerListener implements ActionListener
		{
			public void actionPerformed(ActionEvent event)
			{
				drawTimer.stop();
				if (!guiTest.gameStatus())
				{
					guiTest.repaint();
					drawTimer.start();
				}
				else
				{
					frame.remove(secondPanel);
					
					JLayeredPane replayPane = new JLayeredPane();
					replayPane.setBounds(0 , 0, screenWidth, screenHeight);
//					
					JPanel gameOverPanel = new JPanel();
					gameOverPanel.setLayout(new GridLayout(5,1,2,2));
					frame.repaint();
					frame.setVisible(true);
					
					JLabel back = new JLabel();
					back.add(guiTest);
					guiTest.repaint();
					back.setOpaque(true);
					back.setBounds(0, 0, screenWidth, screenHeight);
					
					JPanel p = new JPanel();
					p.setLayout(null);
					p.add(guiTest);
					p.setOpaque(true);
					p.setBounds(0, 0, screenWidth, screenHeight);
					
					JLabel label = new JLabel("Gameover!", SwingConstants.CENTER);
					label.setBackground(Color.BLACK);
					label.setForeground(Color.RED);
					label.setOpaque(false);
					label.setFont(new Font("Arial Black", Font.BOLD, FONT_SIZE));
					
					JLabel label2 = new JLabel("Final score: " + guiTest.getPoints(), SwingConstants.CENTER);
					label2.setBackground(Color.BLACK);
					label2.setForeground(Color.RED);
					label2.setFont(new Font("Arial Black", Font.BOLD, FONT_SIZE));
					
					gameOverPanel.add(label);
					gameOverPanel.add(label2);
					//replay button
					gameOverPanel.add(replay);
					//main button
					gameOverPanel.add(main);
					//quit button
					gameOverPanel.add(q1);				
					gameOverPanel.setBounds(screenWidth / 4, screenHeight / 4, screenWidth / 2, screenHeight / 2);
					gameOverPanel.setOpaque(false);
					
					replayPane.add(p, new Integer(0), 0);
					replayPane.add(gameOverPanel, new Integer(1), 0);
					
					frame.add(replayPane);
					frame.repaint();
					frame.setVisible(true);
				}
								
			}
		}
		ActionListener advanceListener = new AdvanceTimerListener();

		final int DELAY = 30; // Milliseconds between timer ticks
		drawTimer = new Timer(DELAY, advanceListener);
		drawTimer.start();   
			
		//main menu button clicked
		class MenuClickListener implements ActionListener
		{
			public void actionPerformed (ActionEvent event)
			{
				frame.getContentPane().removeAll();
				secondPanel.removeAll();
				frame.repaint();
				frame.add(lpane);
				frame.repaint();
				frame.setVisible(true);
				guiTest.reset();
				drawTimer.start();
			}
		}
		
		ActionListener listener4 = new MenuClickListener();
		main.addActionListener(listener4);
		
		class ReplayClickListener implements ActionListener
		{
			public void actionPerformed (ActionEvent event)
			{
				frame.getContentPane().removeAll();
				frame.repaint();
				frame.add(secondPanel);
				secondPanel.add(guiTest);
				frame.repaint();
				frame.setVisible(true);
				guiTest.reset();
				drawTimer.start();
			}
		}
		
		ActionListener listener5 = new ReplayClickListener();
		replay.addActionListener(listener5);
		
		q1.addActionListener(listener3);
		
		//listener for mouse input
		class mouseClick implements MouseListener
		{
			public void mousePressed (MouseEvent e)
			{
				guiTest.setMode(true);
			}
			public void mouseReleased (MouseEvent e)
			{
			}
			public void mouseEntered (MouseEvent e)
			{
			}
			public void mouseExited (MouseEvent e)
			{
			}
			public void mouseClicked (MouseEvent e)
			{
			}
		}	
		MouseListener mouseIn = new mouseClick();
		secondPanel.addMouseListener(mouseIn);
	}

}