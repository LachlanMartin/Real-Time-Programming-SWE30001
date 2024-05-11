/*
 *	SWE30001, 2023
 *
 *	Tutorial 1
 * 
 */

package counter;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Point;
import java.awt.Toolkit;

import javax.swing.JButton;
import javax.swing.JFrame;

public class CounterFrame extends JFrame 
{
	private CounterCanvas fView;	// counter canvas
	private JButton fIncrement;		// increment button
	private JButton fDecrement;		// decrement button

	private static final long serialVersionUID = 1L;

	public CounterFrame( String aTitle )
	{
		super( aTitle );
		
		//Make sure we have nice window decorations.
		JFrame.setDefaultLookAndFeelDecorated( true );

		// fixed size
		setResizable( false );
		
		setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );

		getContentPane().setLayout( new GridBagLayout() );
		GridBagConstraints lConstraints = new GridBagConstraints();
		lConstraints.fill = GridBagConstraints.HORIZONTAL;

		fView = new CounterCanvas();
		lConstraints.gridx = 0;
		lConstraints.gridy = 0;
		lConstraints.gridwidth = 2;	// occupy 2 rows
		
		getContentPane().add( fView, lConstraints );
		
		fIncrement = new JButton( "Inc" );
		fIncrement.addActionListener( e -> increment() ); // event handler
		lConstraints.gridx = 0;
		lConstraints.gridy = 1;
		lConstraints.gridwidth = 1;	// occupy 1 row
		lConstraints.weightx = 0.5;	// distribute space evenly
		
		getContentPane().add( fIncrement, lConstraints );
		
		fDecrement = new JButton( "Dec" );
		fDecrement.addActionListener( e -> decrement() ); // event handler
		lConstraints.gridx = 1;
		
		getContentPane().add( fDecrement, lConstraints );
		
		pack();
	        
		// align JFrame to center of screen
        Dimension lScreenSize = Toolkit.getDefaultToolkit().getScreenSize();
        Dimension lWindowSize = getSize();
        Point lTopLeft = new Point( (lScreenSize.width - lWindowSize.width) / 2,
        		                    (lScreenSize.height - lWindowSize.height) / 2 );
        
        setLocation( lTopLeft );

		// make buttons enabled/disabled based on the current value
		fIncrement.setEnabled( fView.getValue() < CounterCanvas.MAX );
		fDecrement.setEnabled( fView.getValue() > 0 );
	}
	
	private void updateButtons()
	{
	    fIncrement.setEnabled( fView.getValue() < CounterCanvas.MAX );
	    fDecrement.setEnabled( fView.getValue() > 0 );
	}
		
	private void increment()
	{
	    fView.increment();
	    updateButtons();
	}

	private void decrement()
	{
	    fView.decrement();
	    updateButtons();
	}
}
