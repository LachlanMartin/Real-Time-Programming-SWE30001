/*
 *	SWE30001, 2023
 *
 *	Multi-threaded Sieve of Eratosthenes
 * 
 */

package sieve;

public interface IControl 
{
	// get number of filtered integers
	public int getFiltered();
	
	// get number of active threads
    public int getThreads();

    // increments active thread count
    public void incrementActive();
    
    // decrements active thread count
    public void decrementActive();

    // increments thread count
    public void incrementThreads();
    
    // decrements thread count
    public void decrementThreads();
    
	// increments filtered count
    public void incrementFiltered();

	// increments prime count
    public void incrementPrimes(); 
    
    // enable controls
    public void enableControl();

    // reset values
    public void reset();
}
