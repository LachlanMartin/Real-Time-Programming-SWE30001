/*
 *	SWE30001, 2023
 *
 *	Concurrent Prime Sieve
 * 
 */

package sieve;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import util.ApplicationFrame;

public class EratosthenesFrame extends ApplicationFrame implements ISieve
{
	private static final long serialVersionUID = 1L;

	private PrimePanel fPrimes;
	private ControlPanel fControls;
	
	////////////////////////////////////////////
	// some methods may require synchronization
	////////////////////////////////////////////
	
	public EratosthenesFrame( String aTitle )
	{
    	super( aTitle );

        getContentPane().setLayout( new GridBagLayout() );
		GridBagConstraints lConstraints = new GridBagConstraints();

		lConstraints.fill = GridBagConstraints.HORIZONTAL;
		lConstraints.insets = new Insets( 5, 5, 5, 5 );

		fPrimes = new PrimePanel( Eratosthenes.ROWS, Eratosthenes.COLUMNS );
		getContentPane().add( fPrimes, lConstraints );
		
		fControls = new ControlPanel( Eratosthenes.TESTS, this );
		getContentPane().add( fControls, lConstraints );
        pack();
 	}
	
	// interface methods
	// get number of filtered integers
		public int getFiltered()
		{
			return fControls.getFiltered();
		}
		
		// get number of active threads
	    public int getThreads()
	    {
	    	return fControls.getThreads();
	    }

	    // marks number to be scheduled for testing
	    public void scheduled( int aNumber )
	    {
	    	fPrimes.setAsAdded(aNumber);
	    }

	    // highlights number being tested and thread name, if possible
	    public void blink( int aPrime, int aNumberTested )
	    {
	    	fPrimes.blink(aPrime);
	    }

	    // increments active thread count
	    public void incrementActive()
	    {
	    	fControls.incrementActive();
	    }
	    
	    // decrements active thread count
	    public void decrementActive()
	    {
	    	fControls.decrementActive();
	    }

	    // increments thread count
	    public void incrementThreads( int aPrime )
	    {
	    	fControls.incrementThreads();
	    }
	    
	    // decrements thread count
	    public void decrementThreads()
	    {
	    	fControls.decrementThreads();
	    }
	    
		// increments filtered count
	    public synchronized void incrementFiltered( int aNumberTested )
	    {
	    	fPrimes.setAsProcessed(aNumberTested);
	    	fControls.incrementFiltered();
	    }

		// increments prime count
	    public synchronized void incrementPrimes( int aPrime )
	    {
	    	fPrimes.setAsPrime(aPrime);
	    	fControls.incrementPrimes();
	    }
	 
	    // filter process complete
	    public void finished()
	    {
	    	fControls.enableControl();
	    }
	    
	    // reset sieve
	    public void reset()
	    {
	    	fPrimes.reset();
	    }
}
