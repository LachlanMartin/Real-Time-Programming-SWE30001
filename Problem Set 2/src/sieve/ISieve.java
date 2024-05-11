/*
 *	SWE30001, 2023
 *
 *	Multi-threaded Sieve of Eratosthenes
 * 
 */

package sieve;

public interface ISieve 
{
	// get number of filtered integers
	public int getFiltered();
	
	// get number of active threads
    public int getThreads();

    // marks number to be scheduled for testing
    public void scheduled( int aNumber );

    // highlights number being tested and thread name, if possible
    public void blink( int aPrime, int aNumberTested );

    // increments active thread count
    public void incrementActive();
    
    // decrements active thread count
    public void decrementActive();

    // increments thread count
    public void incrementThreads( int aPrime );
    
    // decrements thread count
    public void decrementThreads();
    
	// increments filtered count
    public void incrementFiltered( int aNumberTested );

	// increments prime count
    public void incrementPrimes( int aPrime );
 
    // filter process complete
    public void finished();
    
    // reset sieve
    public void reset();
}
