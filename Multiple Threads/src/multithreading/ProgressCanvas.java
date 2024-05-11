/*
 *	SWE30001, 2023
 *
 *	Thread demo
 * 
 */

package multithreading;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;

import util.BufferedCanvas;

public class ProgressCanvas extends BufferedCanvas 
{
	private static final long serialVersionUID = 1L;

	private String fName;
	private int fAngle;
	
    Font fValueFont;
	
	public static final int STEPSIZE = 6;
	
    public ProgressCanvas( String aName )
    {
    	super();
    	
		setPreferredSize( new Dimension( 200, 200 ) );
		setBackground( Color.CYAN );

		fValueFont = new Font( "Arial", Font.BOLD, 36 );
		
		fName = aName;
		fAngle = 0;
    }

    public String getName()
    {
    	return fName;
    }
    
    public void setBackground( Color aBackground )
    {
		super.setBackground( aBackground );
		
		if ( isVisible() )
		{
			repaint();
		}
    }
    
    public void reset()
    {
    	fAngle = 0;
		
    	setBackground( Color.CYAN );
    }
    
    public void rotate()
    {
    	fAngle = (fAngle + STEPSIZE) % 360;
    	
    	repaint();
    }

    public void render( Graphics2D g )
	{
		g.setColor( getBackground() );
        g.fillRect( 0, 0, getWidth(), getHeight() );

        g.setColor( Color.BLUE );
        // allow 10 pixels on either side
        g.fillArc( 5, 5, getWidth() - 10, getWidth() - 10, 90, -fAngle );

        g.setColor( getBackground() );
        // 30 pixels wide
        g.fillArc( 20, 20, getWidth() - 40, getWidth() - 40, 90, -fAngle );

        g.setColor( Color.BLACK );
        g.setFont( fValueFont );
        
        FontMetrics lFontMetrics = g.getFontMetrics();
 
        // calculate the leftmost position for text within canvas
        int lXPos = (getWidth() - lFontMetrics.stringWidth( fName ))/2;
        // calculate baseline: 1: canvas height minus font height divided by two
        //					   2: this is top line (centered relative to canvas)
        //					   3: adjust baseline by ascent of font 
        int lYPos = (getHeight() - lFontMetrics.getHeight())/2 + lFontMetrics.getAscent();

        g.drawString( fName, lXPos,  lYPos );
 	}
}
