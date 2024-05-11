/*
 *	SWE30001, 2023
 *
 *	Thread demo
 * 
 */

package multithreading;

import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollBar;

import util.MakeButton;

public class RotatorPanel extends JPanel implements Runnable
{
	private static final long serialVersionUID = 1L;

	private ProgressCanvas fView;
	private JButton fRun;
	private JButton fPause;
	private JScrollBar fSpeedSlider;

	private int fWaitTime;
	
	private Thread fThread;
	private boolean fPaused;
	
	private synchronized void setSpeed()
	{
		fWaitTime = fSpeedSlider.getValue();
	}
	
	private void runThread()
	{
		fThread = new Thread( this, fView.getName() );
		fWaitTime = fSpeedSlider.getValue();
		fPaused = false;
		
		fRun.setEnabled( false );
		fPause.setEnabled( true );
		fView.setBackground( Color.GREEN );

		fThread.start();
	}
	
	private synchronized void pause_resume()
	{
		if ( !fPaused )
		{
			fPaused = true;
			fPause.setText( "Resume" );
			fView.setBackground( Color.RED );
		}
		else
		{
			fPaused = false;
			fPause.setText( "Pause" );
			fView.setBackground( Color.GREEN );
		}
	}

	public void stop()
	{
		if ( fThread != null )
		{
			fThread.interrupt();
			fThread = null;
		
			// at this point the associated thread has been interrupted
			fRun.setEnabled( true );
			fPause.setText( "Pause" );
		    fPause.setEnabled( false );
			fPaused = false;
			fView.reset();
			fSpeedSlider.setValue( 100 );
		}
	}
	
	public void run()
	{
		try
		{
			synchronized (this)
			{
				while ( fThread != null )
				{
					if ( !fPaused )
					{
						fView.rotate();						
					}

					wait( fWaitTime );
				}
			}
		}
		catch ( InterruptedException e )
		{
		}
	}
	
	public RotatorPanel( String aTitle )
	{
    	super( new GridBagLayout() );
    	
    	GridBagConstraints lConstraints = new GridBagConstraints();
		lConstraints.fill = GridBagConstraints.HORIZONTAL;

		fView = new ProgressCanvas( aTitle );
		lConstraints.gridwidth = 2;	// occupy 2 rows
      
		add( fView, lConstraints );
      
		fSpeedSlider = new JScrollBar( JScrollBar.HORIZONTAL, 100, 100, 100, 2000 );
		fSpeedSlider.addAdjustmentListener( e -> setSpeed() ); 
		lConstraints.gridy = 1;		// row 2

		add( fSpeedSlider, lConstraints );
        
        fRun = MakeButton.createButton( "Run", e -> runThread(), 40 );
        fRun.setEnabled( true );
		lConstraints.gridy = 2;		// row 3
		lConstraints.gridwidth = 1;	// occupy 1 row
		lConstraints.weightx = 1.0; // distribute extra space 
		
        add( fRun, lConstraints );
        
        fPause = MakeButton.createButton( "Pause", e -> pause_resume(), 40 );
        fPause.setEnabled( false );
		lConstraints.gridx = 1;		// column 2
        
        add( fPause, lConstraints );
 	}
}
