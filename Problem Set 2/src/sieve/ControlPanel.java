/*
 *	SWE30001, 2023
 *
 *	Concurrent Prime Sieve: ControlPanel
 * 
 */

package sieve;

import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.JButton;
import javax.swing.JPanel;

import util.MakeButton;
import util.NumberCanvas;

public class ControlPanel extends JPanel implements IControl
{
	private static final long serialVersionUID = 1L;

	private ISieve fSieve;
	private NumberCanvas fActive;
	private NumberCanvas fThreads;
	private NumberCanvas fPrimesFound;
	private NumberCanvas fNumbersTested;
	private JButton fGo;
	
	private NumberGenerator fGenerator;
	
	////////////////////////////////////////////
	// some methods may require synchronization
	////////////////////////////////////////////
	
	private void go()
	{
		// start Sieve, clear all values, disable Go, start generator
		fSieve.reset();
		fGo.setEnabled(false);
		fGenerator.start();
	}

	public ControlPanel( int aLimit, ISieve aSieve )
	{
		// create a panel with GridBagLayout 
		// initialize instance variables
		// create number generator
		// use insets 20, 5, 20, 5
		// create NumberCanvas control and add them to panel 
		// link ActionListener of Go button to go() method
		
		setLayout( new GridBagLayout() );
		
		fSieve = aSieve;
		fActive = new NumberCanvas("Active", 100);
		fThreads = new NumberCanvas("Threads", 100);
		fPrimesFound = new NumberCanvas("Primes", 100);
		fNumbersTested = new NumberCanvas("Tested", 100);
		fGo = MakeButton.createButton( "Go", e -> go(), 40 );
		
		fGenerator = new NumberGenerator(aLimit, fSieve);
		
		GridBagConstraints lConstraints = new GridBagConstraints();
		lConstraints.fill = GridBagConstraints.HORIZONTAL;
		lConstraints.insets = new Insets( 20, 5, 20, 5 );
		
		lConstraints.gridy = 0;
		fActive.setBackground(Color.GRAY);
		add(fActive, lConstraints);
		
		lConstraints.gridy = 1;
		fThreads.setBackground(Color.GRAY);
		add(fThreads, lConstraints);
		
		lConstraints.gridy = 2;
		fNumbersTested.setBackground(Color.GRAY);
		add(fNumbersTested, lConstraints);
		
		lConstraints.gridy = 3;
		fPrimesFound.setBackground(Color.GRAY);
		add(fPrimesFound, lConstraints);
		
		lConstraints.gridy = 4;
		add(fGo, lConstraints);
	}
	
	// interface methods
	// get number of filtered integers
	public synchronized int getFiltered()
	{
		return fNumbersTested.getValue();
	}
	
	// get number of active threads
    public synchronized int getThreads()
    {
    	return fThreads.getValue();
    }

    // increments active thread count
    public synchronized void incrementActive()
    {
    	fActive.setValue(fActive.getValue() + 1);
    }
    
    // decrements active thread count
    public synchronized void decrementActive()
    {
    	fActive.setValue(fActive.getValue() - 1);
    }

    // increments thread count
    public synchronized void incrementThreads()
    {
    	fThreads.setValue(fThreads.getValue() + 1);
    }
    
    // decrements thread count
    public synchronized void decrementThreads()
    {
    	fThreads.setValue(fThreads.getValue() - 1);
    }
    
	// increments filtered count
    public synchronized void incrementFiltered()
    {
    	fNumbersTested.setValue(fNumbersTested.getValue() + 1);
    }

	// increments prime count
    public synchronized void incrementPrimes()
    {
    	fPrimesFound.setValue(fPrimesFound.getValue() + 1);
    	fNumbersTested.setValue(fNumbersTested.getValue() + 1);
    }
    
    // enable controls
    public void enableControl()
    {
    	fGo.setEnabled(true);
    }

    // reset values
    public void reset()
    {
    	fActive.setValue(0);
		fThreads.setValue(0);
		fPrimesFound.setValue(0);
		fNumbersTested.setValue(0);
    }
}
