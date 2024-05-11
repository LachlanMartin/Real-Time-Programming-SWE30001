/*
 * 
 * SWE300001, 2023
 * 
 * Scheduler super class
 * 
 */

package scheduling;

import java.util.ArrayList;
import java.util.List;

public abstract class AbstractScheduler implements Runnable
{
	protected List<AbstractProcess> fActiveQueue;
	protected List<AbstractProcess> fInactiveQueue;
	
	private List<AbstractProcess> fAddToSchedule;
	private List<AbstractProcess> fRemoveFromSchedule;

	protected Thread fThread;

	public List<AbstractProcess> getProcessQueue()
	{
		return fActiveQueue;
	}
	
	public AbstractScheduler( String aID )
	{
		fActiveQueue = new ArrayList<AbstractProcess>();
		fInactiveQueue = new ArrayList<AbstractProcess>();
		
		fAddToSchedule = new ArrayList<AbstractProcess>();
		fRemoveFromSchedule = new ArrayList<AbstractProcess>();

		fThread = new Thread( this, aID );
		fThread.start();
	}
	
	public void stop()
	{
		if ( fThread != null )
		{
			for ( AbstractProcess p : fActiveQueue )
			{
				p.stop();
			}

			for ( AbstractProcess p : fInactiveQueue )
			{
				p.stop();
			}

			fThread.interrupt();
						
			System.out.println( String.format( "%s terminated.", fThread.getName() ) );

			fThread = null;
		}
	}
	
	public synchronized void addToSchedule( AbstractProcess aProcess )
	{
		aProcess.setNextDeadline( aProcess.getDeadline() );

		fAddToSchedule.add( aProcess );
		
		System.out.println( String.format( "%s scheduled with next deadline %d ms" , 
										   aProcess.getName(), aProcess.getNextDeadline() ) );

		notify();
	}

	public synchronized void removeFromSchedule( AbstractProcess aProcess )
	{
		fRemoveFromSchedule.add( aProcess );
		
		notify();
	}
	
	public void run()
	{
		try
		{
			while ( fThread != null )
			{
				synchronized(this)
				{					
					// wait for next action
					wait();

					// remove process from process queue if present (no ordering)
					while ( !fRemoveFromSchedule.isEmpty() )
					{
						remove( fRemoveFromSchedule.remove( 0 ) );
					}
					
					// add process to process queue if present (orders queue)
					while ( !fAddToSchedule.isEmpty() )
					{
						add( fAddToSchedule.remove( 0 ) );
					}
				}				
			}
		}
		catch (InterruptedException e)
		{}
		
		fThread = null;
	}
		
	protected abstract void add( AbstractProcess aProcess );

	protected abstract void remove( AbstractProcess aProcess );
}
