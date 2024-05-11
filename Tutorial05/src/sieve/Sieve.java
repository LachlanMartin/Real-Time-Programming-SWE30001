/*
 *	SWE30001, 2023
 *
 *	Multi-threaded Sieve of Eratosthenes
 * 
 */

package sieve;

public class Sieve implements ISieve
{
    private int fThreadCount;				// number of thread running
    private int fPrimeCount;				// number of primes found
    private int fFiltered;					// number of integers filtered
	
    private NumberGenerator fGenerator;		// integer generator

    private void println( String aFormat, Object... args )
    {
      String lFormat = String.format( "%s: %s", 
    		                          Thread.currentThread().getName(),
    		                          aFormat );
    			
      System.out.println( String.format( lFormat, args ) );
    }
    
    public Sieve()
    {
    	// constructor for Sieve    	
    	fThreadCount = 0;
		fPrimeCount = 0;
		fFiltered = 0;
	}
  
    public synchronized int getFiltered()
    {
    	return fFiltered;
    }

    public synchronized int getThreads()
    {
    	return fThreadCount;
    }
    
    public void scheduled( int aNumberTested )
    {
    	// prints thread name and number being tested
    	println( "%d scheduled", aNumberTested );
    }

    public void blink( int aPrime, int aNumberTested )
    {
    	// prints thread name and number being tested
    	println( "Testing %d", aNumberTested );

    	synchronized (this)
    	{
        	// waits for 200 ms
        	try 
        	{
    			wait( 200 );
    		} 
        	catch (InterruptedException e) 
        	{}    		
    	}
    }

    public synchronized void incrementThreads( int aPrime )
    {
    	fThreadCount++;
    	// prints Thread name, thread count, and prime if prime != 0
    	// otherwise, just prints thread name and thread count
    	if ( aPrime != 0 )
    	{
        	println( "New prime filter for %d, thread count %d", 
        			 aPrime,
        			 fThreadCount );
    	}
    	else
    	{
        	println( "Thread count %d", fThreadCount );
    	}
    }

    public synchronized void decrementThreads()
    {
    	fThreadCount--;
    	// prints Thread name and thread count
    	println( "Stopped, %d threads remaining", fThreadCount );
    }

    public synchronized void incrementFiltered( int aNumberTested )
    {
    	fFiltered++;
    	// prints Thread name and integer filtered
    	println( "%d eliminated, filter count %d", 
				 aNumberTested,
				 fFiltered );
    }

    public synchronized void incrementPrimes( int aPrime )
    {
    	incrementFiltered( aPrime );
    	
    	fPrimeCount++;
    	// prints Thread name, prime, and prime count
    	println( "%d is a prime number, prime number count %d", 
	   			 aPrime,
	   			 fPrimeCount );
    }

    public void finished()
    {
		// prints "Test completed." and the number of primes found
    	System.out.println( String.format( "Test completed. %d prime numbers found.", 
				   				           fPrimeCount ) );
    }
    
    public void reset()
    {
    	fThreadCount = 0;
		fPrimeCount = 0;
		fFiltered = 0;
    }

    public void execute()
    {
    	// creates generator for 100 integers
    	fGenerator = new NumberGenerator( 100, this );
    	// starts generator
    	fGenerator.start();
    	// waits 1ms (in loop) until thread count is zero, requires synchronization
		synchronized (this)
		{
			try
			{
				while ( fThreadCount != 0 )
				{
					wait( 1 );
				}
			}
			catch (InterruptedException e) 
			{}
		}
    }
	
    public static void main( String[] args )
    {
    	(new Sieve()).execute();
    }
}
