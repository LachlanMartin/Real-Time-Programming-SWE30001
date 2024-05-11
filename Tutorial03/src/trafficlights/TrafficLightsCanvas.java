/*
 *	SWE30001, 2023
 *
 *	Tutorial 3
 * 
 */

package trafficlights;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;

import util.BufferedCanvas;


public class TrafficLightsCanvas extends BufferedCanvas 
{
	private static final long serialVersionUID = 1L;

	private enum State
	{
		RED,
		GREEN,
		AMBER
	}
	
	private String fDirection;
	private Font fDirectionFont;

	private State fState;

	public TrafficLightsCanvas( String aDirection )
	{ 
		super();
		
		setPreferredSize( new Dimension( 150, 400 ) );

		fDirection = aDirection;
		
		fDirectionFont = new Font( "Arial", Font.BOLD, 36 );
		
		fState = State.RED;		
	}

	public void setRed() 
	{
		fState = State.RED;
		
		repaint();
	}
	
	public void setGreen()
	{
		fState = State.GREEN;
		
		repaint();
	}
	
	public void setAmber()
	{
		fState = State.AMBER;
		
		repaint();
	}

	public void render( Graphics2D g ) 
	{ 
    	// 1. clear background
        g.setColor( getBackground() );
        g.fillRect( 0, 0, getWidth(), getHeight() );
        
        // 2. draw direction
        g.setFont( fDirectionFont );

        FontMetrics lFontMetrics = g.getFontMetrics();
        String lDirectionString = String.valueOf( fDirection );
        
        // calculate the leftmost position for text within canvas
        int lXPos = (getWidth() - lFontMetrics.stringWidth( lDirectionString ))/2;
        // calculate baseline: 20 + ascent
        int lYPos = 20 + lFontMetrics.getAscent();
        
        g.setColor( Color.BLACK );
        g.drawString( lDirectionString, lXPos,  lYPos );

        // 3. draw arcs, per 100 pixels
        
        lXPos = 30;
        lYPos = 85;
        
        g.setColor( fState == State.RED? Color.RED : Color.DARK_GRAY );
        g.fillArc( lXPos, lYPos, 90, 90, 0, 360 );

        lYPos += 100;
        g.setColor( fState == State.AMBER? Color.YELLOW : Color.DARK_GRAY );
        g.fillArc( lXPos, lYPos, 90, 90, 0, 360 );

        lYPos += 100;
        g.setColor( fState == State.GREEN? Color.GREEN : Color.DARK_GRAY );
        g.fillArc( lXPos, lYPos, 90, 90, 0, 360 );
	}
}
