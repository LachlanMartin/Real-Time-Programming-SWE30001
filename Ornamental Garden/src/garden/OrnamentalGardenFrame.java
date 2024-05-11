/*
 *	SWE30001, 2023
 *
 *	Ornamental Garden Problem
 * 
 */

package garden;

import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.JButton;
import javax.swing.JCheckBox;

import util.ApplicationFrame;
import util.MakeButton;
import util.NumberCanvas;

public class OrnamentalGardenFrame extends ApplicationFrame
{
	private static final long serialVersionUID = 1L;

	private NumberCanvas fWest;
	private NumberCanvas fPatrons;
	private NumberCanvas fEast;
	private JButton fGo;
	private JCheckBox fFixIt;
	
	private Counter fCPatrons;
	private Turnstile fGateWest;
	private Turnstile fGateEast;
	
	private int fFinishedCount;

	private void go()
	{
		fCPatrons = new Counter( fPatrons );
		fGateWest = new Turnstile( this, fWest, fCPatrons, fFixIt.isSelected() );
		fGateEast = new Turnstile( this, fEast, fCPatrons, fFixIt.isSelected() );
		
		fGateWest.go();
		fGateEast.go();
		
		fGo.setEnabled( false );
		fFixIt.setEnabled( false );
		
		fFinishedCount = 0;
	}
	
	public synchronized void end()
	{
		if ( ++fFinishedCount == 2 )
		{
			fGo.setEnabled( true );
			fFixIt.setEnabled( true );
			fFixIt.setSelected( false );
		}
	}
	
	public OrnamentalGardenFrame( String aTitle )
	{
    	super( aTitle );
    	
        getContentPane().setLayout( new GridBagLayout() );
		GridBagConstraints lConstraints = new GridBagConstraints();

		lConstraints.fill = GridBagConstraints.HORIZONTAL;
		lConstraints.insets = new Insets( 5, 5, 5, 5 );

		fWest = new NumberCanvas( "West" );
		fWest.setBackground( Color.CYAN );
		getContentPane().add( fWest, lConstraints );

		fPatrons = new NumberCanvas( "Patrons" );
		fPatrons.setBackground( Color.GREEN );
		getContentPane().add( fPatrons, lConstraints );
		
		fEast = new NumberCanvas( "East" );
		fEast.setBackground( Color.CYAN );
		getContentPane().add( fEast, lConstraints );

		fFixIt = new JCheckBox( "Fix it" );
		lConstraints.gridy = 1;							// row 2
		lConstraints.gridx = 0;							// column 1
		getContentPane().add( fFixIt, lConstraints );

		fGo = MakeButton.createButton( "Go", e -> go(), 40 );
		lConstraints.fill = GridBagConstraints.NONE;	// do not fill
		lConstraints.ipadx = 40;						// add 40 pixels to width
		lConstraints.ipady = 10;						// add 10 pixels to height 
		lConstraints.gridx = 1;							// column 2
		getContentPane().add( fGo, lConstraints );
		
        pack();
 	}
}
