/*
 *	SWE30001, 2023
 *
 * 	Speedometer widget (Java Canvas)
 * 
 *  Code based on CruiseControl, Concurrency: State Models & Java Programs
 * 
 */

package cruise;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Panel;

import javax.swing.JButton;
import javax.swing.JSlider;
import javax.swing.SwingConstants;

import util.MakeButton;

public class CruiseControlController extends Panel implements Runnable, ICruiseControl
{
	private static final long serialVersionUID = 1L;

	private ICarSpeed fCarSpeedController;

	private CruiseControlCanvas fCruiseControlView;

	private JButton fEnable;
	private JButton fSet;
	private JButton fResume;

	private JSlider fSpeed;
	
	private enum States
	{
		OFF,
		ENABLED,
		DISABLED
	}
	
	private double fTargetSpeed;
    private States fState;

    private Thread fCCThread;

    private synchronized void on_off()
    {
    	switch( fState )
    	{
    	case OFF:
    		fState = States.ENABLED;
    		fCruiseControlView.set( true );

			fEnable.setText( "Off" );
			fResume.setEnabled( false );

    		if ( fCCThread == null )
    		{
    			fCCThread = new Thread( this );
    			fCCThread.start();
    		}
    		
    		break;
    	default:
    		fCruiseControlView.set( false );
			fResume.setEnabled( false );
    		fCCThread = null;
			fEnable.setText( "On" );
			fState = States.OFF;
    	}
    }

    private synchronized void setSpeed()
    {
		fTargetSpeed = fCarSpeedController.getSpeed();
		fSpeed.setValue( (int)fTargetSpeed );
		fCruiseControlView.setSpeed( (int)fTargetSpeed );
    }

    private synchronized void resume()
    {
    	fState = States.ENABLED;
		fResume.setEnabled( false );
		fCruiseControlView.set( true );
    }

    private synchronized void change_speed()
    {
		fTargetSpeed = fSpeed.getValue();
		fCruiseControlView.setSpeed( (int)fTargetSpeed );
    }
    
    private void setupCruiseControlView()
	{
		setLayout( new GridBagLayout() );
		GridBagConstraints lConstraints = new GridBagConstraints();

		lConstraints.fill = GridBagConstraints.HORIZONTAL;

		fCruiseControlView = new CruiseControlCanvas();
		lConstraints.gridx = 0;
		lConstraints.gridy = 0;
		lConstraints.gridheight = 2;
		add( fCruiseControlView, lConstraints );
		
		fSpeed = new JSlider( SwingConstants.VERTICAL, 0, fCarSpeedController.getMaxSpeed(), 0 );
		lConstraints.gridx = 1;
		lConstraints.gridy = 0;
		fSpeed.addChangeListener( l -> change_speed() );
		add( fSpeed, lConstraints );

		fEnable = MakeButton.createButton( "On", e -> on_off(), 40 );
		fEnable.setEnabled( false );
		lConstraints.gridx = 0;
		lConstraints.gridy = 2;
		lConstraints.gridheight = 1;
		lConstraints.gridwidth = 2;
		lConstraints.insets = new Insets(10,0,0,0);
		add( fEnable, lConstraints );

		fSet = MakeButton.createButton( "Set", e -> setSpeed(), 40 );
		fSet.setEnabled( false );
		lConstraints.gridy = 3;
		add( fSet, lConstraints );

		fResume = MakeButton.createButton( "Resume", e -> resume(), 40 );
		fResume.setEnabled( false );
		lConstraints.gridy = 4;
		add( fResume, lConstraints );
	}

	public CruiseControlController( ICarSpeed aCarSpeedController )
	{
		super();
		
		fCarSpeedController = aCarSpeedController;
		fTargetSpeed = 0.0;
		fState = States.OFF;
	
		setupCruiseControlView();

		fCarSpeedController.setCruiseController( this );
	}
	
	public synchronized void engineOn()
	{
		fEnable.setEnabled( true );
		fSet.setEnabled( true );
	}

	public synchronized void brake()
	{
		if ( fState == States.ENABLED )
		{
			fState = States.DISABLED;
			fResume.setEnabled( true );
			fCruiseControlView.set( false );
		}
	}
	
	public synchronized void engineOff()
	{
		fCruiseControlView.set( false );
		fCruiseControlView.setSpeed( 0 );
		fEnable.setEnabled( false );
		fSet.setEnabled( false );
		fResume.setEnabled( false );
		fCCThread = null;
		fEnable.setText( "On" );
		fState = States.OFF;
	}
    
	public void run()
	{
		try
		{
			synchronized(this)
			{
				while ( fCCThread != null )
				{
					double lError = 0.0;
					
					if ( fState == States.ENABLED )
					{
						// simplified feed back control, delta speed => acceleration per second
						// lError = d Delta v / d t (1st derivative of velocity over time)
						lError = (fTargetSpeed - fCarSpeedController.getSpeed());
						
						fCarSpeedController.setThrottle( lError + fCarSpeedController.drag() ); 
					}

					wait(500);
				}
			}
		}
		catch ( InterruptedException e )
		{
			// intentionally empty
		}
		
		fCCThread = null;
	}
}
