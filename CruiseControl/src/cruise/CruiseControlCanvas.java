/*
 *	SWE30001, 2023
 *
 * 	Cruise control controller (GUI & cruise control thread cycle)
 * 
 *  Code based on CruiseControl, Concurrency: State Models & Java Programs
 * 
 */

package cruise;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics2D;

import util.BufferedCanvas;
import util.Text;

public class CruiseControlCanvas extends BufferedCanvas
{
	private static final long serialVersionUID = 1L;

    private Font fBigFont;
    private Font fSmallFont;
 
    private Text fTextCruiseControl;
    private Text fTextCruiseSpeed;
    private Text fTextSpeed;
    private Text fTextEnabled;
    private Text fTextDisabled;
    
    private int fSpeed;
    private boolean fIsEnabled;

    public void setSpeed( int aSpeed )
    {
    	if ( aSpeed != fSpeed )
    	{
    		fSpeed = aSpeed;

    		repaint();
    	}
    }

    public boolean get()
    {
    	return fIsEnabled;
    }

    public void set( boolean aIsEnabled )
    {
    	fIsEnabled = aIsEnabled;
    	
    	repaint();
    }

	public CruiseControlCanvas()
	{
		super();
		
		setPreferredSize( new Dimension( 220, 200 ) );
		
		fBigFont = new Font( "Arial", Font.BOLD, 24 );
		fSmallFont = new Font( "Arial", Font.BOLD, 18 );

		fTextCruiseControl = new Text( "Cruise Control", Color.WHITE, fBigFont );
		fTextCruiseSpeed = new Text( "Cruise Speed", Color.WHITE, fSmallFont );
		fTextSpeed = new Text( "0", Color.WHITE, fBigFont );
	    fTextEnabled = new Text( "Enabled", Color.GREEN, fSmallFont );
	    fTextDisabled = new Text( "Disabled", Color.RED, fSmallFont );

	    fSpeed = 0;
	    fIsEnabled = false;
	}

	public void render( Graphics2D g )
	{
        g.setColor( Color.DARK_GRAY );
        g.fillRect( 0, 0, getWidth(), getHeight() );

        g.setColor( Color.WHITE );
        g.drawRect( 5, 5, getWidth() - 10, getHeight() - 10 );

        fTextCruiseControl.renderXCentered( g, getWidth(), 35 );
        
        fTextCruiseSpeed.renderXCentered( g, getWidth(), 90 );
        
        g.drawRect( 30, 60, 160, 80 );

        fTextSpeed.setValue( String.valueOf( fSpeed ) );
        fTextSpeed.renderXCentered( g, getWidth(), 125 );
        
        if ( fIsEnabled )
        {
            int lRenderWith = fTextEnabled.getRenderWidth( g );
            int lXPos = (getWidth() - lRenderWith)/2 - 20;
            fTextEnabled.render( g, lXPos, 175 );
            
            g.fillArc( lXPos + lRenderWith + 20, 160, 20, 20, 0, 360 );
        }
        else
        {
            int lRenderWith = fTextEnabled.getRenderWidth( g );
            int lXPos = (getWidth() - lRenderWith)/2 - 20;
            fTextDisabled.render( g, lXPos, 175 );
            
            g.fillArc( lXPos + lRenderWith + 20, 160, 20, 20, 0, 360 );
        }
	}
}
