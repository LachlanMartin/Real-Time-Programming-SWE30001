/*
 *	SWE30001, 2023
 *
 * 	Car simulator controller (GUI & engine thread cycle)
 * 
 *  Code based on CruiseControl, Concurrency: State Models & Java Programs
 * 
 */

package cruise;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Panel;

import javax.swing.JButton;

import util.MakeButton;

public class CarSimulatorController extends Panel implements Runnable, ICarSpeed
{
	private static final long serialVersionUID = 1L;

	private ICruiseControl fCruiseController;
	private SpeedometerCanvas fSpeedometerView;

	private JButton fEngine;
	private JButton fAccelerate;
	private JButton fBrake;

	private double fCurrentSpeed;
    private double fDistanceTraveled;
    private double fThrottleValue;
    private double fBrakeValue;
    private boolean fIgnitionOn;

    private Thread fEngineThread;
    
    /*
     *	Technical considerations: 
     * 
     * 	- air resistance (drag force):
     * 
     * 		FD = cD * A * 0.5 * p * v^2 
     * 
     * 		FD	- drag force (N, kgm/s^2)
     * 		cD 	- drag coefficient (dimensionless)
     * 		A	- cross sectional area (m^2)
     * 		p	- air density (kg/m^3, approx. 1.2 kg/m^3 at 20C sea level)  
     * 		v 	- relative speed (m/s)
     * 
     *  - speed:
     *  
     *   	v = a * t (m/s)
     *   
     *  - typical accelerations:
     *  
     * 		3.4 - 7.0 m/s^2 for midsize cedan 0 to 60 km/h
     * 		10 m/s^2 mean full brake car
     *      15.2 m/s^2 Bugatti Veyron from 0 to 100km/h 2.4s
     * 
     *  - typical velocities
     *  
     *  	5 km/h			 5,000 / 3,600 = 1.39 m/s
     *  	60 km/h			60,000 / 3,600 = 16.67 m/s
     *  	100 km/h	 	100,000 / 3.600 = 27.78 m/s
     *  	200 km/h		200,000 / 3,600 = 55.56 m/s
     *  	
     *  - Audi A4, 2000 model:
     *  	
     *  	cD = 0.31
     *  	A  = 2.12 m^2, based on Audi data
     *  
     *  	cD*A Audi A4 = 0.31 * 2.12 m^2 = 0.6572 m^2
     *  
     *  	FD Audi A4 = 0.6572 m^2 * 0.5 * 1.2 kg/m^3 * v^2 m^2/s^2
     *  
     *  			   = 0.39432 * v^2 kgm/s^2
     *  
     *  	1500 kg * aD = 0.39432 * v^2 kgm/s^2
     *              
     *                aD = 0.00026288 * v^2 m/s^2 
     *  
     *  	aD(v)        = 0.00026288 * v^2 m/s^2
     *  
     *  	m = 1,185-1,760 Kg, use 1,500 kg as mean
     *  
     *  	Bugatti Veyron:
     *  
     *  	cD = 0.36
     *  	A = 2.07 m^2
     *  
     *      cD*A = 0.7452 m^2
	 *
     *      = 0.44712 * v^2 kgm/s^2
     *
     *		m = 1,900 kg
     *
     *      aD(v) = 0.00023533 * v^2 m/s^2
     */

    private double fMaxSpeed;
    private double fMaxThrottle; 
    private double fMaxBrake;

    public static double DefaultMaxSpeed = 240.0;		// 240 km/h
    public static double DefaultMaxThrottle = 5.0;		// 100% => 5 m/s^2 
    public static double DefaultMaxBrake = 10.0;		// 100% => 10 m/s^2
    
    public static double VeyronMaxSpeed = 480.0;		// 480 km/h
    public static double VeyronMaxThrottle = 15.0;		// 100% => 15 m/s^2 
    public static double VeyronMaxBrake = 30.0;			// 100% => 30 m/s^2

    public static double cD = 0.31;
    public static double A = 2.12;

    public static double DefaultWeight = 1500.0;
    public static double VeyronWeight = 1900.0;

    // parameter
    public static double DragMultiplier = cD * A * 0.5 * 1.2 / DefaultWeight;
    
    public final static int TicksPerSecond = 5;

    private synchronized void engineOn_Off()
    {
		if ( fIgnitionOn )
		{
	    	fEngineThread = null;
			fIgnitionOn = false;
			fEngine.setText( "Engine On" );
			fSpeedometerView.clear();
			fAccelerate.setEnabled( false );
			fBrake.setEnabled( false );
			fSpeedometerView.setIgnitionOff();
			if ( fCruiseController != null )
			{
				fCruiseController.engineOff();
			}
		}
		else
		{
			fIgnitionOn = true;
			fEngine.setText( "Engine Off" );
			fAccelerate.setEnabled( true );
			fBrake.setEnabled( true );
			fSpeedometerView.setIgnitionOn();

			if ( fEngineThread == null )
		    {
		    	fEngineThread = new Thread( this );
		       	fEngineThread.start();
		    }
			
			if ( fCruiseController != null )
			{
				fCruiseController.engineOn();
			}
		}
    }

    private synchronized void accelerate()
    {
    	if ( fThrottleValue < fMaxThrottle )
    	{
    		fBrakeValue = 0;
        	fThrottleValue++;
        	
        	if ( fThrottleValue > fMaxThrottle )
        	{
        		fThrottleValue = fMaxThrottle;
        	}
        	        	
        	fSpeedometerView.setThrottle( fThrottleValue );
    	}
    }

    private synchronized void brake()
    {
    	if ( fBrakeValue < fMaxBrake )
    	{
        	fThrottleValue = 0;
        	fBrakeValue++;
        	
        	if ( fCruiseController != null )
        	{
        		fCruiseController.brake();
        	}
        	
        	if ( fBrakeValue > fMaxBrake )
        	{
        		fBrakeValue = fMaxBrake;
        	}
        	
        	fSpeedometerView.setBrake( fBrakeValue );
    	}
    }
    
	private void setupSpeedometerView()
	{
		setLayout( new GridBagLayout() );
		
		GridBagConstraints lConstraints = new GridBagConstraints();

		lConstraints.fill = GridBagConstraints.HORIZONTAL;
		
		fSpeedometerView = new SpeedometerCanvas( fMaxSpeed, fMaxThrottle, fMaxBrake );
		lConstraints.gridx = 0;
		lConstraints.gridy = 0;
		lConstraints.gridheight = 5;
		lConstraints.gridwidth = 3;
		add( fSpeedometerView, lConstraints );
		
		fEngine = MakeButton.createButton( "Engine On", e -> engineOn_Off(), 40 );
		lConstraints.gridx = 0;
		lConstraints.gridy = 6;
		lConstraints.gridheight = 1;
		lConstraints.gridwidth = 1;
		add( fEngine, lConstraints );

		fAccelerate = MakeButton.createButton( "Accelerate", e -> accelerate(), 40 );
		fAccelerate.setEnabled( false );
		lConstraints.gridx = 1;
		add( fAccelerate, lConstraints );

		fBrake = MakeButton.createButton( "Brake", e -> brake(), 40 );
		fBrake.setEnabled( false );
		lConstraints.gridx = 2;
		add( fBrake, lConstraints );
	}

	public CarSimulatorController()
	{
		super();
		
		fCurrentSpeed = 0.0;
	    fDistanceTraveled = 0.0;
	    fThrottleValue = 0.0;
	    fBrakeValue = 0.0;
	    fIgnitionOn = false;

	    // parameters
	    fMaxSpeed = DefaultMaxSpeed;
	    fMaxThrottle = DefaultMaxThrottle; 
	    fMaxBrake = DefaultMaxBrake;

	    setupSpeedometerView();
	}
	
	public int getMaxSpeed()
	{
		return (int)fMaxSpeed;
	}
	
	public double drag()
	{
		return DragMultiplier * ((fCurrentSpeed * fCurrentSpeed) / 3.6);	// in m^2/s^2
	}
	
	public void run() 
	{
		try
		{
			synchronized(this)
			{
				// controller cycle, runs every 0.2 seconds
	            while ( fEngineThread!=null )
	            {
	                wait( 1000 / TicksPerSecond );
	                
	                // update speed
	                double lRelativeAcceleration = fThrottleValue - drag() - fBrakeValue;

	                // add gain in km/h
	                fCurrentSpeed += (lRelativeAcceleration * 3.6) / TicksPerSecond;

	                if ( fCurrentSpeed > fMaxSpeed )
	                {
	                	fCurrentSpeed = fMaxSpeed;
	                }

	                if ( fCurrentSpeed < 0.0 )
	                {
	                	fCurrentSpeed = 0.0;
	                }

	                fEngine.setEnabled( (int)fCurrentSpeed == 0 ); // don't turn engine off while driving
	                
	                fSpeedometerView.setSpeed( fCurrentSpeed );
	                
	                // update distance traveled
	                fDistanceTraveled += fCurrentSpeed / TicksPerSecond;
	                
	                fSpeedometerView.setDistance( fDistanceTraveled );

	                // break decay
	                if ( fBrakeValue > 0.0 )
	                {
	                	double lDecay = fBrakeValue * 0.5;
	                	
	                	fBrakeValue -= lDecay / TicksPerSecond;
	                	
	                	fSpeedometerView.setBrake( fBrakeValue );
	                }

	                // throttle decay
	                if ( fThrottleValue > 0.0 )
	                {
	                	double lDecay = fThrottleValue * 0.05;
	                	
	                	fThrottleValue -= lDecay / TicksPerSecond;

	                	fSpeedometerView.setThrottle( fThrottleValue );
	                }
	            }	
			}
		}
		catch ( InterruptedException e )
		{
			// intentionally empty
		}
		
		fCurrentSpeed = 0.0;
		fDistanceTraveled = 0.0;
    	fThrottleValue = 0.0;
		fBrakeValue = 0.0;
		fSpeedometerView.clear();
	}
	
	public synchronized double getSpeed()
	{
		return fCurrentSpeed;
	}

	public synchronized void setThrottle( double aThrottleValue )
	{
		fThrottleValue = aThrottleValue;
		
		if ( fThrottleValue < 0.0 )
    	{
			// halve maximum brake acceleration
			fBrakeValue = Math.min( fThrottleValue / (fMaxThrottle * fMaxThrottle / -fMaxBrake), fMaxBrake / 2.0 );
	    	fThrottleValue = 0.0;
    	}
		else
		{
			fBrakeValue = 0.0;			
		}

		fSpeedometerView.setBrake( fBrakeValue );
        	
        if ( fThrottleValue > fMaxThrottle )
        {
        	fThrottleValue = fMaxThrottle;
        }
        
        fSpeedometerView.setThrottle( fThrottleValue );
	}
	
	public void setCruiseController( ICruiseControl aController )
	{
		// run at setup
		fCruiseController = aController;
	}
}
