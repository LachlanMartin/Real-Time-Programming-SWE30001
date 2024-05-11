/*
 *	SWE30001, 2023
 *
 *	Tutorial 1
 * 
 */

package counter;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;

public class CounterCanvas extends Canvas 
{
	private static final long serialVersionUID = 1L;

	public static final int MAX = 10; 
	public static final int SIZE = 200;
	public static final int CIRCLE_WIDTH = 20;

	private int fCurrentValue;
	private Font fValueFont;
	
    public CounterCanvas()
    {
    	super();
    	
		setPreferredSize( new Dimension( SIZE, SIZE ) );
    	
		fCurrentValue = MAX;
		
		// Arial works on both Windows and MacOS 
		fValueFont = new Font( "Arial", Font.BOLD, 120 );
    }

    public int getValue()
    {
    	return fCurrentValue;
    }

    public void increment()
    {
    	if ( fCurrentValue < MAX )
    	{
    		fCurrentValue++;

    		repaint();
    	}    	
    }

    public void decrement()
    {
    	if ( fCurrentValue > 0 )
    	{
    		fCurrentValue--;
    	
            repaint();
    	}
    }
    
	public void paint( Graphics g )
	{
		update( g );
	}
	
    public void update( Graphics g )
	{
    	// z-order:
    	//
    	// 1. clear background 
    	// 2. draw count arc
    	// 3. draw background circle
    	// 4. draw number
    	//

    	// 1. clear background
        g.setColor( getBackground() );
        g.fillRect( 0, 0, getWidth(), getHeight() );

        // 2. draw count arc
        
        g.setColor( Color.BLUE );

        // increments of 36 degrees (correct for 0 degrees at 3 o'clock)
        // negative angle clockwise rotation
        
        g.fillArc( 5, 5, 
        		   SIZE-10, SIZE-10, 
        		   90, -fCurrentValue*36 );
        
        // 3. draw background arc

        g.setColor( getBackground() );

        // increments of 36 degrees (correct for 0 degrees at 3 o'clock)
        // negative angle clockwise rotation
        // Naive anti aliasing effect: use 35 degrees rather than 36 degrees.
        g.fillArc( 5+CIRCLE_WIDTH, 5+CIRCLE_WIDTH, 
        		   SIZE-2*(5+CIRCLE_WIDTH), SIZE-2*(5+CIRCLE_WIDTH),
        		   90, -fCurrentValue*36-1 );

        // draw center lines
        g.setColor( Color.RED );
        g.drawLine( getWidth()/2, 0, getWidth()/2, getHeight() );
        g.drawLine( 0, getHeight()/2, getWidth(), getHeight()/2 );
  
        // 4. draw number
        // See also:
        //	https://stackoverflow.com/questions/23729944/java-how-to-visually-center-a-specific-string-not-just-a-font-in-a-rectangle
        //	http://faculty.salina.k-state.edu/tmertz/Java/072graphicscolorfont/05fontmetrics.pdf
        g.setFont( fValueFont );

        FontMetrics lFontMetrics = g.getFontMetrics();
        String lValueString = String.valueOf( fCurrentValue );
        
        // calculate the leftmost position for text within canvas
        int lXPos = (getWidth() - lFontMetrics.stringWidth( lValueString ))/2;
        // calculate baseline: 1: canvas height minus font height divided by two
        //					   2: this is top line (centered relative to canvas)
        //					   3: adjust baseline by ascent of font 
        int lYPos = (getHeight() - lFontMetrics.getHeight())/2 + lFontMetrics.getAscent();
        
        g.setColor( Color.BLACK );
        g.drawString( lValueString, lXPos,  lYPos );
	}
}
