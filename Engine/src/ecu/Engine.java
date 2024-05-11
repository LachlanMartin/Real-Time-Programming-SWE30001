/*
 * 	SWE30001 2023
 * 
 * 	Simple engine emulator
 * 
 */

package ecu;

public class Engine implements Runnable
{
	private Thread fThread;

	private double fSpeed;
	private double fAcceleration; 

	public static final double TARGETSPEED = 120.0;
	public static final double MAXSPEED = 300.0;
	public static final double MAXACCELARATION = 3.0;
	
	// return the speed (3a)
	public synchronized double getSpeed()
	{
		return fSpeed;
	}
	
	// set acceleration (3b)
	public synchronized void setAcceleration(double aAcceleration)
	{
		fAcceleration = aAcceleration;
	}
			
	public Engine() 
	{
		fSpeed = 0.0;
		fAcceleration = 0.0;
		fThread = new Thread( this, "Engine" );
	}

	public void start()
	{
		if ( fThread != null )
		{
			fThread.start();
		}
	}
		
	public void stop()
	{
		if ( fThread != null )
		{
			fThread.interrupt();
			
		}
	}
		
	public void run() 
	{
		try
		{
			synchronized (this)
			{
				while( fThread != null )
				{
	                // update speed, add gain in km/h
	                fSpeed += fAcceleration * 3.6/ 5.0;

	                if ( fSpeed > MAXSPEED )
	                {
	                	fSpeed = MAXSPEED;
	                }

					// decay acceleration by 5%
					fAcceleration -= fAcceleration * 0.05;
					
					if ( fAcceleration < 0.0 )
					{
						fAcceleration = 0.0;
					}
					
					// wait for 200ms
					wait( 200 );
				}
			}
		}
		catch (InterruptedException e)
		{}
		
		fThread = null;
	}
}
