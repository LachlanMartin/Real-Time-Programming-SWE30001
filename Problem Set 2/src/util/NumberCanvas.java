/*
 *	SWE30001, 2023
 *
 * 	Count down timer 
 * 
 *  Code based on CruiseControl, Concurrency: State Models & Java Programs
 * 
 */

package util;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;

public class NumberCanvas extends BufferedCanvas 
{
	private static final long serialVersionUID = 1L;

	String fTitle;
	
	int fValue;

    public NumberCanvas( String aTitle , int aWidth, int aHeight )
    {
    	super();
    	
		setPreferredSize( new Dimension( aWidth, aHeight ) );

		fTitle = aTitle;
    	fValue = 0;
    }

    public NumberCanvas( String aTitle )
    {
    	this( aTitle, 200, 200 );
    }

    public NumberCanvas( String aTitle, int aSize )
    {
    	this( aTitle, aSize, aSize );
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
        
        // draw title 
        g.setColor( Color.BLACK );

        int lGap = (getHeight() * 5) / 100;
        
        // adjust font
        for ( int i = getPreferredSize().height; i > 1; i-- )
        {
        	g.setFont( new Font( "Arial", Font.BOLD, i ) );        		

        	FontMetrics lFontMetrics = g.getFontMetrics();

        	// allow 5 pixels between text on either size
        	int lMaxTextWidth = getWidth() - 10;

            if ( lFontMetrics.stringWidth( fTitle ) > lMaxTextWidth )
            {
            	continue;	// continue down scaling
            }

            // title width fine, does it fit in upper quarter of box
        	int lMaxTextHeight = getHeight() / 4 - lGap;
 
           	if ( lFontMetrics.getAscent() <= lMaxTextHeight )
           	{
            	int lXPos = (getWidth() - lFontMetrics.stringWidth( fTitle ))/2;
                int lYPos = lMaxTextHeight/2 + lFontMetrics.getAscent() - lGap;

                g.drawString( fTitle, lXPos,  lYPos );

                break;           		
           	}
        }
        
        // draw value

        String lValueString = String.valueOf( fValue );

        int lGap2 = lGap * 2;
        
        // adjust font
        for ( int i = getPreferredSize().height; i > 1; i-- )
        {
        	g.setFont( new Font( "Arial", Font.BOLD, i ) );        		

        	FontMetrics lFontMetrics = g.getFontMetrics();

        	int lMaxTextHeight = 3 * getHeight() / 4 - lGap2;

        	if ( lFontMetrics.getAscent() > lMaxTextHeight )
        	{
        		continue;
        	}
        	
        	// allow 5 pixels between text on either size
            if ( lFontMetrics.stringWidth( lValueString ) <= getWidth() - lGap )
            {
            	// get leftmost horizontal position based on text width
                int lXPos = (getWidth() - lFontMetrics.stringWidth( lValueString ))/2;
                
                // get text baseline
                int lYPos = lMaxTextHeight / 2 + lFontMetrics.getAscent() - lGap2;

                g.drawString( lValueString, lXPos,  lYPos );

                break;
            }
        }
	}
}
