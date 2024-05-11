/*
 * 
 * SWE300001, 2023
 * 
 * Process super class
 * 
 */

package scheduling;

public abstract class AbstractProcess implements Runnable
{
	protected enum ProcessStates
	{
		INACTIVE,
		RUNNING,
		SUSPENDED
	}

	private String fName;
	private long fComputationTime;
	private long fDeadline;
	private ProcessStates fState;

	private AbstractScheduler fScheduler;
	private long fStartTime;
	private long fNextDeadline;

	public AbstractProcess( String aName, long aComputationTime, long aDeadline ) 
	{
		fName = aName;
		fComputationTime = aComputationTime;
		fDeadline = aDeadline;
		fState = ProcessStates.INACTIVE;
		
		fNextDeadline = 0;
	}
	
	public String getName()
	{
		return fName;
	}
	
	public long getComputationTime()
	{
		return fComputationTime;
	}

	public long getPeriod()
	{
		return Long.MAX_VALUE;
	}

	public long getNextActivation()
	{
		if ( getPeriod() != Long.MAX_VALUE )
		{
			return fStartTime + getPeriod();
		}
		
		return Long.MAX_VALUE;
	}
	
	public long getDeadline()
	{
		return fDeadline;
	}

	public long getNextDeadline()
	{
		return fNextDeadline;
	}
	
	public void setNextDeadline( long aOffset )
	{
		fNextDeadline += aOffset;
	}
	
	protected ProcessStates getState()
	{
		return fState;
	}
	
	protected void setState( ProcessStates aState )
	{
		fState = aState;
	}
	
	private Thread fThread;
	
	public void start( AbstractScheduler aScheduler )
	{
		fScheduler = aScheduler;

		fThread = new Thread( this, fName );
		
		fThread.start();
	}
	
	public void stop()
	{
		if ( fThread != null )
		{
			fThread.interrupt();

			System.out.println( String.format( "%s terminated.", fThread.getName() ) );

			fThread = null;
		}
	}
	
	public void schedule( AbstractScheduler aScheduler )
	{
		aScheduler.addToSchedule( this );
	}
	
	private long emulateComputation() throws InterruptedException
	{
		long lComputeUnits = 0;	
		
		fStartTime = System.currentTimeMillis();	
		
		while ( lComputeUnits < getComputationTime() )
		{
			synchronized(this)
			{
				if ( fState == ProcessStates.RUNNING )
				{
					long lStart = System.currentTimeMillis();

					
					// emulate computation, release lock on process 
					wait( 1 );

					lComputeUnits += (System.currentTimeMillis() - lStart);					
				}
				else
				{
					// process has been preempted

					wait();
				}
			}
		}

		long lElapsed = System.currentTimeMillis();	
		long lWaitTime = getNextActivation() - lElapsed;
		
		System.out.println( String.format( "%s finished in %d ms (%d ms to next activation)", 
				                           getName(), lElapsed - fStartTime,lWaitTime ) );
		
		fScheduler.removeFromSchedule( this );
		
		return lWaitTime;
	}
	
	public void run()
	{
		try
		{
			while ( fThread != null )
			{
				long lWaitTime = emulateComputation();
						
				// next activation

				if ( lWaitTime >= 0 )
				{
					synchronized(this)
					{
						long lStart = System.currentTimeMillis();

						// wait at least n time units before next activation
						
						while (true)
						{
							wait( lWaitTime ); 	

							long lElapsed = System.currentTimeMillis() - lStart;
							long lDelta = lWaitTime - lElapsed;
							
							if ( lDelta <= 0 )
							{
								break;
							}
							
							lWaitTime = lDelta;
						}
					}
				}
				else
				{
					System.out.println( String.format( "OVERRUN: %s missed deadline by %d ms", getName(), -lWaitTime ) );
				}
								
				// schedule again
				fScheduler.addToSchedule( this );
			}
		}
		catch (InterruptedException e)
		{}
		
		fThread = null;
	}
	
	public void setInactive()
	{
		fState = ProcessStates.INACTIVE;
	}
	
	public synchronized void setSuspended()
	{
		fState = ProcessStates.SUSPENDED;

		notify();
	}
	
	public synchronized void setRunning()
	{
		fState = ProcessStates.RUNNING;
		
		notify();
	}

	// Auxiliary methods required for process ordering
	
	public boolean equals( Object aObject )
	{
		if ( aObject instanceof AbstractProcess )
		{
			return fName.equals( ((AbstractProcess)aObject).fName );
		}
		
		return false;
	}
	
	public int hashCode()
	{
		return fName.hashCode();
	}

	public String toString()
	{
		StringBuilder sb = new StringBuilder();
		
		sb.append( fName );
		sb.append( ',' );
		sb.append( fState );
	
		if ( fThread != null )
		{
			sb.append( " - " );
			sb.append( fThread.getState() );
			
		}
		
		return sb.toString();
	}
}
