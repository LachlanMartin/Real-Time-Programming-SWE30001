/*
 *	SWE30001, 2023
 *
 *	Concurrent Prime Sieve: PrimePanel
 * 
 */

package sieve;

import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.JPanel;

import util.SimpleNumberCanvas;

public class PrimePanel extends JPanel implements IPrimes
{
	private static final long serialVersionUID = 1L;

	private int fRowCount;
	private int fColumnCount;

	private SimpleNumberCanvas fNumbers[][];
	
	////////////////////////////////////////////
	// some methods may require synchronization
	////////////////////////////////////////////
	
	public PrimePanel( int aRowCount, int aColumnCount )
	{
		// create a panel with GridBagLayout
		// set instance variables
		// use insets of 5, 5, 5, 5
		// initialize array
		// add SimpleNumberCanvas elements to the panel (an array) 
		// initially their background should be CYAN
		// layout numbers by columns
		
		setLayout( new GridBagLayout() );
		
		fRowCount = aRowCount;
		fColumnCount = aColumnCount;
		
		fNumbers = new SimpleNumberCanvas[fRowCount][fColumnCount];
		
		GridBagConstraints lConstraints = new GridBagConstraints();
		lConstraints.fill = GridBagConstraints.VERTICAL;
		lConstraints.insets = new Insets( 5, 5, 5, 5 );
		
		int num = 0;
		for (int j = 0; j < fColumnCount; j++)
		{
			for (int i = 0; i < fRowCount; i++)
			{
				num++;
				fNumbers[i][j] = new SimpleNumberCanvas(num, Eratosthenes.NUMBER_SIZE);
				fNumbers[i][j].setBackground(Color.CYAN);
				lConstraints.gridy = i;
				lConstraints.gridx = j;
				add(fNumbers[i][j], lConstraints);
			}
		}
		
	}
	
	private SimpleNumberCanvas getNumberCanvas( int aNumber )
	{
		// auxiliary function to find and return the object in fNumbers
		// that corresponds to aNumber
		aNumber--;
		return fNumbers[aNumber%fRowCount][aNumber/fRowCount];
	}

	// interface methods
	// highlights number being tested
    public void blink( int aPrime )
    {
    	getNumberCanvas(aPrime).blink();
    }

    // marks number as filtered out
    public void setAsProcessed( int aNumber )
    {
    	getNumberCanvas(aNumber).setBackground(Color.GRAY);
    }
    
    // marks number as ready for testing
    public void setAsAdded( int aNumber )
    {
    	getNumberCanvas(aNumber).setBackground(Color.YELLOW);
    }
    
    // marks number as prime
    public void setAsPrime( int aNumber )
    {
    	getNumberCanvas(aNumber).setBackground(Color.GREEN);
    }
    
    // resets states of all numbers
    public void reset()
    {
    	for (int i = 1; i <= Eratosthenes.TESTS; i++)
    	{
    		getNumberCanvas(i).setBackground(Color.CYAN);
    	}
    }
}
