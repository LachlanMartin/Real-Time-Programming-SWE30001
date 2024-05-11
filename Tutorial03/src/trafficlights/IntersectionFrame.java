/*
  *	SWE30001, 2023
 *
 *	Tutorial 3
 * 
 */

package trafficlights;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Point;
import java.awt.Toolkit;

import javax.swing.JButton;
import javax.swing.JFrame;

public class IntersectionFrame extends JFrame implements Runnable
{
	private static final long serialVersionUID = 1L;

	private TrafficLightsCanvas fEast;
	private TrafficLightsCanvas fNorth;
	private JButton fStart;
	private Thread fController;
	
	private void animate()
	  {
		if ( fController == null)
		{
		    fController = new Thread( this );
		    fController.start();
		    fStart.setText( "Stop" );
		}
		else
		{
		    fController.interrupt();
		    fController = null;
		    fEast.setRed();
		    fNorth.setRed();
		    fStart.setText( "Start" );
		}
	  }
		
	  public void run() // animation thread function
	  {
	    try
	    {
	      synchronized (this)
	      {
	        while ( fController != null )
	        {
	          // cycle twice (per direction) through traffic lights stages
	          // transition from red to green delayed by 0.5s
	          // green for 4s
	          // amber for 1s
	          // use Thread.sleep( millis ) to pause the animation thread
	          Thread.sleep( 500 );
	          fNorth.setGreen();
	          Thread.sleep( 4000 );
	          fNorth.setAmber();
	          Thread.sleep( 1000 );
	          fNorth.setRed();
	          Thread.sleep( 500 );
	          fEast.setGreen();
	          Thread.sleep( 4000 );
	          fEast.setAmber();
	          Thread.sleep( 1000 );
	          fEast.setRed();
	        }
	      }
	    }
	    catch ( InterruptedException e )
	    {}
	    
	    fController = null;
	  }

	public IntersectionFrame( String aTitle )
	{
    	super( aTitle );
    	
        //Make sure we have nice window decorations.
        JFrame.setDefaultLookAndFeelDecorated( true );

		// fixed size
		setResizable( false );

        setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );

        // GridBagLayout: most flexible layout manager
        getContentPane().setLayout( new GridBagLayout() );
		GridBagConstraints lConstraints = new GridBagConstraints();
		lConstraints.fill = GridBagConstraints.HORIZONTAL;

        fEast = new TrafficLightsCanvas( "East" );
		lConstraints.gridx = 0;		// 1st column
		lConstraints.gridy = 0;		// 1st row
		lConstraints.gridwidth = 1;	// occupy 1 column
        
        getContentPane().add( fEast, lConstraints );

        fNorth = new TrafficLightsCanvas( "North" );
		lConstraints.gridx = 1;		// 2nd column
		lConstraints.gridy = 0;		// 1st row
        
        getContentPane().add( fNorth, lConstraints );
        
        fStart = new JButton( "Start" );
        fStart.setFont( new Font( "Arial", Font.BOLD, 36 ) );
        fStart.addActionListener( e -> animate() ); // use lambda expression
		lConstraints.gridx = 0;		// 1st column
		lConstraints.gridy = 1;		// 1st row
		lConstraints.gridwidth = 2;	// occupy 2 columns
        
        getContentPane().add( fStart, lConstraints );
        
        pack();				// force layout manager to place GUI elements

        // align JFrame to center of screen
        Dimension lScreenSize = Toolkit.getDefaultToolkit().getScreenSize();
        Dimension lWindowSize = getSize();
        Point lTopLeft = new Point( (lScreenSize.width - lWindowSize.width) / 2,
        		                    (lScreenSize.height - lWindowSize.height) / 2 );
        
        setLocation( lTopLeft );
 	}
}
