/*
 *	SWE30001, 2023
 *
 *	Multi-threaded Sieve of Eratosthenes
 * 
 */

package sieve;

public class NumberGenerator implements Runnable
{
    private int fLimit;			// number of integers to generate
    private ISieve fSieve;		// reference to main object 

    private Thread fThread;		// the NumberGenerator thread
    private PrimeFilter fTwo;	// the first filter for 2
	
    public NumberGenerator( int aLimit, ISieve aSieve )
    {
    	fLimit = aLimit;
    	fSieve = aSieve;
    	fThread = null;
    	fTwo = null;
    }
	
    public void start()
    {
    	// create "NumberGenerator" thread and start it
    	fThread = new Thread( this, "NumberGenerator" );
    	fThread.start();
    	
    	// signal to Sieve that a "NumberGenerator" thread has been started
    	// use 0 as argument
    	fSieve.incrementThreads( 0 );
    }
	
    public void stop()
    {
    	// if fTwo is not null, stop fTwo
    	if ( fTwo != null )
    	{
    		fTwo.stop();
    	}
    }
	
    public void run()
    {
		try 
		{
			synchronized(this)
			{
		    	// mark 1 as filtered (Sieve)
		    	fSieve.incrementFiltered( 1 );

		    	// mark 2 as prime (Sieve)
		    	fSieve.incrementPrimes( 2 );

		    	// create a filter for 2 and start it
		    	fTwo = new PrimeFilter( 2, fSieve );
		    	fTwo.start();
		    	
		    	// while the thread is active, send every 100 ms
		    	// the next integer to the fTwo filter
		    	// stop at fLimit
		    	// wait for 1 ms while the Sieve returns getFiltered() != fLimit
		    	// stop the generator
		    	// finally, decrement the Sieve’s thread count by one

		    	while ( fThread != null )
				{
					for ( int i = 3; i <= fLimit; i++ )
					{
						wait( 100 );
						fSieve.scheduled( i );
						fTwo.filter( i );
					}
					
					while ( fSieve.getFiltered() != fLimit )
					{
						wait( 1 );
					}
					
					stop();

					while ( fSieve.getThreads() > 1 )
					{
						wait( 1 );
					}
		
					fThread.interrupt();
				}
			}
		}
		catch (InterruptedException e) 
		{}
		
		fThread = null;
    	fSieve.decrementThreads();
    	
    	fSieve.finished();
	}
}
