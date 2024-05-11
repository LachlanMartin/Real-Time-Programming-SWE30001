/*
 * 
 * SWE300001, 2023
 * 
 * Periodic Process
 * 
 */

package scheduling;

public class PeriodicProcess extends AbstractProcess
{
	private long fPeriod;

	public PeriodicProcess( String aName, long aComputationTime, long aPeriod, long aDeadline ) 
	{
		super( aName, aComputationTime, aDeadline );
		
		fPeriod = aPeriod;
	}	
	
	public long getPeriod()
	{
		return fPeriod;
	}
	
	public void setNextDeadline( long aOffset )
	{
		long lDeadline = getDeadline();
		
		if ( lDeadline < fPeriod )
		{
			aOffset += fPeriod - lDeadline;
		}
		
		super.setNextDeadline( aOffset );
	}
}
