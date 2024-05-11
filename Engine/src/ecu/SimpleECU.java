/*
 * 	SWE30001 2023
 * 
 * 	Simple ECU emulator
 * 
 */

package ecu;

public class SimpleECU 
{
	private Engine fEngine;
	private SpeedControl fController;
	
	public SimpleECU() 
	{
		fEngine = new Engine();
		fController = new SpeedControl( Engine.TARGETSPEED );
		fController.setEngine( fEngine );
	}
	
	public void execute()
	{
		fEngine.start();
		fController.start();
		
		long lStart = System.currentTimeMillis();
		
		synchronized (this)
		{
			try 
			{
				while ( Math.abs( fEngine.getSpeed() - Engine.TARGETSPEED ) > 0.001 )
				{
					System.out.println( String.format( "Current speed %g km/h",
		                                               fEngine.getSpeed() ) );
					wait( 100 );
				} 
			}
			catch (InterruptedException e) 
		    {}

		    System.out.println( String.format( "Target speed %g km/h reached.",
		                                       Engine.TARGETSPEED ) );
			
			fController.stop();
			fEngine.stop();
		}
		
		long lEnd = System.currentTimeMillis();
		
	    System.out.println( String.format( "Time taken: %2.2f s.", (lEnd - lStart) / 1000.0 ) );		
	}
	
	public static void main( String[] args )
	{
		(new SimpleECU()).execute();
	}
}
