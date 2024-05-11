/*
 *	SWE30001, 2023
 * 
 * 	Simple Number Canvas
 * 
 */

package util;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;

public class SimpleNumberCanvas extends BufferedCanvas 
{
	private static final long serialVersionUID = 1L;

	private int fValue;
	private Color fForegroundColor;
	
    public SimpleNumberCanvas( int aValue, int aWidth, int aHeight )
    {
    	super();
    	
		setPreferredSize( new Dimension( aWidth, aHeight ) );

		fForegroundColor = Color.BLACK;
    	fValue = aValue;
    }
    
    public SimpleNumberCanvas( int aValue )
    {
    	this( aValue, 70, 70 );
    }
    
    public SimpleNumberCanvas( int aValue, int aSize )
    {
    	this( aValue, aSize, aSize );
    }
    
    public synchronized void blink()
    {
    	Color lBackground = getBackground();

    	try
    	{
    		for ( int i = 0; i < 2; i++ )
    		{
            	setBackground( Color.RED );
            	Thread.sleep( 100 );
            	setBackground( lBackground );
            	Thread.sleep( 100 );
    		}
    	}
    	catch (InterruptedException e)
    	{}
    }

    public void setForeground( Color aForegroundColor )
    {
    	fForegroundColor = aForegroundColor;
    	
    	repaint();
    }
    
    public void setBackground( Color aBackgroundColor )
    {
    	super.setBackground( aBackgroundColor );
    	
    	repaint();
    }
    
    public void setValue( int aValue )
    {
    	fValue = aValue;
    	
        repaint();
    }
    
    public int getValue()
    {
    	return fValue;
    }

    public void render( Graphics2D g )
	{
    	g.setColor( getBackground() );
        g.fillRect( 0, 0, getWidth(), getHeight() );

        // draw value
        g.setColor( fForegroundColor );
        String lValueString = String.valueOf( 100 );

        // adjust font
        for ( int i = 96; i > 1; i-- )
        {
        	g.setFont( new Font( "Arial", Font.BOLD, i ) );        		

        	FontMetrics lFontMetrics = g.getFontMetrics();

        	// number must fit into canvas
        	if ( (lFontMetrics.getAscent() - 10) > getHeight() )
        	{
        		continue;
        	}
        	        	
        	// allow 5 pixels between text on either size
            if ( lFontMetrics.stringWidth( lValueString ) <= getWidth() - 10 )
            {
            	lValueString = String.valueOf( fValue );

                // calculate the leftmost position for text within canvas
                int lXPos = (getWidth() - lFontMetrics.stringWidth( lValueString ))/2;
                // calculate baseline: 1: canvas height minus font height divided by two
                //					   2: this is top line (centered relative to canvas)
                //					   3: adjust baseline by ascent of font 
                int lYPos = (getHeight() - lFontMetrics.getHeight())/2 + lFontMetrics.getAscent();
                
                g.drawString( lValueString, lXPos,  lYPos );

                break;
            }
        }
	}
}
