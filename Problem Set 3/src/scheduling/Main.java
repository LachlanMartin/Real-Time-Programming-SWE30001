/*
 * 
 * SWE300001, 2023
 * 
 * Scheduler Test
 * 
 */

package scheduling;

import java.io.IOException;

public class Main 
{
	private ProcessSet fSet;

	public Main() 
	{
		fSet = new ProcessSet();

		fSet.add( new PeriodicProcess( "T1", 9, 40, 38 ) );
		fSet.add( new PeriodicProcess( "T2", 22, 75, 75 ) );
		fSet.add( new PeriodicProcess( "T3", 16, 66, 64 ) );		
		fSet.add( new PeriodicProcess( "T4", 5, 55, 53 ) );		
		fSet.add( new PeriodicProcess( "T5", 5, 130, 128 ) );		
	}

	public void execute()
	{		
		if ( fSet.meetLiuAndLayland() )
		{
			System.out.println( "RMA by Liu&Layland" );
		}
		else
		{
			if ( fSet.meetCriticalInstant() )
			{
				System.out.println( "RMA by Critical Instant" );
			}
			else
			{
				System.out.println( "RMA not possible" );
			}
		}
		
		double lUtilization = fSet.computeEDFUtilization();
		
		if ( lUtilization <= 1.0 )
		{
			System.out.println( String.format( "Running with EDF: U = %g", lUtilization ) );

			AbstractScheduler lScheduler = new EDFScheduler();
			
			fSet.activate( lScheduler );
						
			try 
			{
				// Wait for any input to stop program

				System.in.read();
			} 
			catch (IOException e) 
			{}
			
			lScheduler.stop();
		}
		else
		{
			System.out.println( String.format( "Process set exceeds permissible utilization = %g.", lUtilization ) );			
		}
	}
	
	public static void main( String[] args )
	{
		(new Main()).execute();
	}
}
