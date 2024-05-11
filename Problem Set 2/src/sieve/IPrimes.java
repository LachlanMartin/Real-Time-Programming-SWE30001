/*
 *	SWE30001, 2023
 *
 *	Multi-threaded Sieve of Eratosthenes
 * 
 */

package sieve;

public interface IPrimes 
{
    // highlights number being tested
    public void blink( int aPrime );

    // marks number as filtered out
    public void setAsProcessed( int aNumber );
    
    // marks number as ready for testing
    public void setAsAdded( int aNumber );
    
    // marks number as prime
    public void setAsPrime( int aNumber );
    
    // resets states of all numbers
    public void reset();
}
