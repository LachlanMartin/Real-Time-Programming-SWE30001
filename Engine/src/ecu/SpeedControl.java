/*
 * 	SWE30001 2023
 * 
 * 	Simple cruise control emulator
 * 
 */

package ecu;

public class SpeedControl implements Runnable
{
	private Engine fEngine;
	private Thread fThread;
	
	private double fTargetSpeed;
	
	public void setEngine( Engine aEngine )
	{
		fEngine = aEngine;
	}
	
	// constructor for SpeedControl (3c)
	public SpeedControl(double targetSpeed) 
	{
        fTargetSpeed = targetSpeed;
    }

	// start thread (3d)
	public void start() 
	{
        if (fThread == null) 
        {
            fThread = new Thread(this, "SpeedControl");
            fThread.start();
        }
    }

	// stop thread (3e)
    public void stop() 
    {
        if (fThread != null) 
        {
            fThread.interrupt();
        }
    }

	private void actuate()
	{
		if ( fEngine != null )
		{
			double lCurrentSpeed  = fEngine.getSpeed();
			// lError = d Delta v / d t (1st derivative of velocity over time)
			double lError = fTargetSpeed - lCurrentSpeed;

			// limit acceleration
			if ( lError < 0.0 )
			{
				lError = Math.max( -Engine.MAXACCELARATION, lError );
			}
			else
			{
				lError = Math.min( Engine.MAXACCELARATION, lError );
			}
			
			fEngine.setAcceleration( lError );
		}		
	}

	public void run() 
	{
		try
		{
			// wait for 100ms and then actuate (3f)
			Thread.sleep(100);
            actuate();
		}
		catch (InterruptedException e)
		{}
		
		fThread = null;
	}
}
