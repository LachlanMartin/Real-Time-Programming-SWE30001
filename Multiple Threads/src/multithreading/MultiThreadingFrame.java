/*
 *	SWE30001, 2023
 *
 *	Thread demo
 * 
 */

package multithreading;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import javax.swing.JButton;

import util.ApplicationFrame;
import util.MakeButton;

public class MultiThreadingFrame extends ApplicationFrame
{
	private static final long serialVersionUID = 1L;

	private RotatorPanel fPanelA;
	private RotatorPanel fPanelB;
	private JButton fStop;

	private void stop()
	{
		fPanelA.stop();
		fPanelB.stop();
	}
	
	public MultiThreadingFrame( String aTitle )
	{
    	super( aTitle );

        getContentPane().setLayout( new GridBagLayout() );
		GridBagConstraints lConstraints = new GridBagConstraints();

		lConstraints.fill = GridBagConstraints.HORIZONTAL;

		fPanelA = new RotatorPanel( "ThreadA" );
		getContentPane().add( fPanelA, lConstraints );

		fPanelB = new RotatorPanel( "ThreadB" );      
		getContentPane().add( fPanelB, lConstraints );
        
		fStop = MakeButton.createButton( "Stop", e -> stop(), 40 );
		lConstraints.fill = GridBagConstraints.NONE;	// do not fill
		lConstraints.ipadx = 60;						// add 60 pixels to size 
		lConstraints.gridy = 1;							// row 2
		lConstraints.gridwidth = 2;						// span 2 columns
		getContentPane().add( fStop, lConstraints );

        pack();
 	}
}
