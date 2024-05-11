/*
 *	SWE30001, 2023
 *
 * 	Speedometer widget (Java Canvas)
 * 
 *  Code based on CruiseControl, Concurrency: State Models & Java Programs
 * 
 */

package cruise;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;

import util.BufferedCanvas;
import util.Text;

public class SpeedometerCanvas extends BufferedCanvas
{
	private static final long serialVersionUID = 1L;
	
    private Font fBigFont;
    private Font fSmallFont;

    private double fSpeedMax;
    private double fThrottleMax; 
    private double fBrakeMax;		

    private int fStepSize;		// speedometer steps
    private int fStepDivider;	// speedometer angle increments
    
    private double fSpeedValue;
    private double fDistanceValue;
    private double fThrottleValue;
    private double fBrakeValue;
    private boolean fIgnitionOn;

    private Text fTextSpeed;
    private Text fTextTick;
    private Text fTextIgnition;
    private Text fTextThrottle;
    private Text fTextBrake;
    
    public static int DEFAULT_UNITS_PER_TICK = 20;
    public static int VEYRON_UNITS_PER_TICK = 40;

    public void setSpeed( double aSpeedValue )
    {
    	if ( aSpeedValue != fSpeedValue )
    	{
    		fSpeedValue = aSpeedValue;
    		
    		if ( fSpeedValue > fSpeedMax )
    		{
    			fSpeedValue = fSpeedMax;
    		}
    		
    		repaint();
    	}
    }

    private int fDistanceDigits[];
    
    private void mapDistance()
    {
    	// distance measured in meters, display in meters/100
        int lDistance = (int)fDistanceValue / 10;
        
        // map 5 digits of distance
        for ( int i = 4; i >= 0 ; i-- )
        {            
            lDistance /= 10;
            fDistanceDigits[i] = lDistance % 10;
        }
    }
    
    public void setDistance( double aDistanceValue )
    {
    	if ( aDistanceValue != fDistanceValue )
    	{
    		fDistanceValue = aDistanceValue;
    		
    		mapDistance();
    		
    		repaint();
    	}
    }

    public void setThrottle( double aThrottleValue )
    {
    	if ( aThrottleValue != fThrottleValue )
    	{
    		fThrottleValue = aThrottleValue;
    		fBrakeValue = 0.0;
    		
    		repaint();
    	}
    }

    public void setBrake( double aBrakeValue )
    {
    	if ( aBrakeValue != fBrakeValue )
    	{
    		fBrakeValue = aBrakeValue;
    		fThrottleValue = 0.0;
    		
    		repaint();
    	}
    }
    public void setIgnitionOn()
    {
    	fIgnitionOn = true;
    	
    	repaint();
    }

    public void setIgnitionOff()
    {
    	fIgnitionOn = false;
    	
		repaint();
    }

    public void clear()
    {
    	fIgnitionOn = false;
    	fSpeedValue = 0.0;
    	fDistanceValue = 0.0;
    	fBrakeValue = 0.0;
    	fThrottleValue = 0.0;
    	mapDistance();
    	
		repaint();
    }
    
	public SpeedometerCanvas( double aSpeedMax, double aThrottleMax, double aBrakeMax )
	{
		super();
		
		setPreferredSize( new Dimension( 400, 400 ) );
		
		fBigFont = new Font( "Arial", Font.BOLD, 24 );
		fSmallFont = new Font( "Arial", Font.BOLD, 18 );

		fSpeedMax = aSpeedMax;
		fThrottleMax = aThrottleMax;
		fBrakeMax = aBrakeMax;
		
		// parameters
		fStepSize = DEFAULT_UNITS_PER_TICK;
		fStepDivider = DEFAULT_UNITS_PER_TICK / 20;

		fSpeedValue = 0;
		fDistanceValue = 0;
		fThrottleValue = 0.0;
		fBrakeValue = 0.0;
		fIgnitionOn = false;
		
		fTextSpeed = new Text( "0", Color.GREEN, new Font( "Arial", Font.BOLD, 48 ) );
		fTextTick = new Text( "0", Color.WHITE, fSmallFont );
		fTextIgnition = new Text( "Ignition", Color.WHITE, fSmallFont );
		fTextThrottle = new Text( "Throttle", Color.WHITE, fBigFont );
		fTextBrake = new Text( "Brake", Color.WHITE, fBigFont );
		
		fDistanceDigits = new int[5];
	}
	
	private void drawSpeedometer( Graphics2D g, int aXCenter, int aYCenter, int aRadius )
	{
		g.setStroke( new BasicStroke( 2.0f ) );
        g.setColor( Color.WHITE );

        g.drawArc( aXCenter-aRadius, aYCenter-aRadius, aRadius*2, aRadius*2, 0, 360 );

        for ( int i = 0; i <= 12; i++ )
        {
        	// create markers from 210 to 330 degrees via 90 degrees (top) 
        	int lAngleInDeg = 210 - i * 20;
        	
        	if ( lAngleInDeg < 0 )
        	{
        		lAngleInDeg += 360;
        	}
        	
        	double lAngleInRad = (lAngleInDeg * Math.PI) / 180.0;
        	
        	// compute position of X and Y offsets
            int lXOffset = aXCenter + (int)(aRadius * Math.cos( lAngleInRad ));
            int lYOffset = aYCenter - (int)(aRadius * Math.sin( lAngleInRad ));

            // draw maker line
            g.drawLine( aXCenter, aYCenter, lXOffset, lYOffset );
            
            // place number
            lXOffset = aXCenter + (int)((aRadius+10) * Math.cos( lAngleInRad ));
            lYOffset = aYCenter - (int)((aRadius+10) * Math.sin( lAngleInRad ));

            // a bit of adjustment (optional)
            if ( i <= 5 )
            {
            	lXOffset -=15;
            }

            if ( i == 6  )
            {
            	lXOffset -=10;
            }

            if ( i == 7 )
            {
            	lXOffset -=5;
            }

            fTextTick.setValue( String.valueOf( i * fStepSize ) );
            fTextTick.render( g, lXOffset, lYOffset );
        }

       // draw current speed 
       if ( fSpeedValue > 0.0 )
       {
           g.setColor( Color.GREEN );
           g.fillArc( aXCenter-aRadius+2, 
           		   aYCenter-aRadius+2, 
           		   aRadius*2-2, 
           		   aRadius*2-4,
           		   210, 
           		   (int)fSpeedValue != 0 ? -((int)(fSpeedValue/fStepDivider)) : -1 );    	   
       }

       g.setColor( Color.BLACK );
       // move by (10,10)
       g.fillArc( aXCenter-aRadius+10, 
                  aYCenter-aRadius+10, 
                  aRadius*2-20, 
            	  aRadius*2-20, 
            	  0, 
                  360 );        	 

        // draw ignition status
    	fTextIgnition.render( g, aXCenter-40, aYCenter+50 );
        
        if ( fIgnitionOn )
        {
            g.setColor( Color.GREEN );
        }
        else
        {
            g.setColor( Color.RED );
        }
        
        g.fillArc( aXCenter+30, aYCenter+38, 12, 12, 0, 360 );
        
        fTextSpeed.setValue( String.valueOf( (int)fSpeedValue ) );
        fTextSpeed.renderXCentered( g, getWidth(), 130 );
	}
	
	private void drawOdometer( Graphics2D g )
	{
        // calculate font size
        String lZero = "0";

        g.setFont( fBigFont );
        FontMetrics lFM = g.getFontMetrics();
        
        int lCWidth = lFM.stringWidth( lZero );
        int lCHeight = lFM.getHeight();
        int lXCenter = getWidth()/2 - (lCWidth+4)*5/2;
        int lYCenter = getHeight()/2 - 40;

        for ( int i = 0; i < 5; i++ )
        {
            g.setColor( Color.WHITE );
            g.drawRect( lXCenter + (lCWidth + 4)*i, lYCenter, lCWidth + 4, lCHeight + 2 );
            
            if (i == 4 )
            {
            	g.setColor( Color.RED );
            }
            else
            {
            	g.setColor( Color.YELLOW );
            }
            
            g.drawString( String.valueOf( fDistanceDigits[i] ),
            		      lXCenter+(lCWidth+4)*i+3,
            		      lYCenter+lCHeight-4 );
        }
	}

	private Text fControlText = new Text( "0.0", Color.YELLOW, new Font( "Arial", Font.BOLD, 12 ) );
	
    private void drawControl( Graphics2D g, Text aName, int aPos, double aValue, double aMax, Color aColor ) 
    {
    	int lColumnWidth = getWidth()/2;
        int lStart = 28 + lColumnWidth * aPos;
        int lEnd = (lColumnWidth - 58);
        int lScale = lEnd - 2;
    			
    	int lYPos = getHeight() - 60;
    	
        int lNameWidth = aName.getRenderWidth( g );
        int lNameX = (lColumnWidth - lNameWidth)/2  + lColumnWidth * aPos;
        
        aName.render( g, lNameX, lYPos );        
        
		g.setStroke( new BasicStroke( 2.0f ) );
        g.drawRect( lStart, lYPos + 15, lEnd, 16 );
        g.setColor( aColor );
        g.fillRect( lStart + 2, lYPos + 16, (int)((aValue/aMax) * lScale), 15 );
        
        fControlText.setValue( String.format( "%1.1f", aValue ) );
        int lTextWidth = fControlText.getRenderWidth( g );
        int lTextX = (lColumnWidth - lTextWidth)/2  + lColumnWidth * aPos;
        fControlText.render( g, lTextX, lYPos + 28 );
    }

	public void render( Graphics2D g) 
	{
        g.setColor( Color.BLACK );
        g.fillRect( 0, 0, getWidth(), getHeight() );
        drawSpeedometer( g, getWidth()/2, getHeight()/2 - 20, 130 );
        drawOdometer( g );
        drawControl( g, fTextThrottle, 0, fThrottleValue, fThrottleMax, Color.GREEN );
        drawControl( g, fTextBrake, 1, fBrakeValue, fBrakeMax, Color.RED );
	}
}
