/*
 *	SWE30001, 2023
 *
 * 	Car simulator Main window (Java JFrame)
 * 
 *  Code based on CruiseControl, Concurrency: State Models & Java Programs
 * 
 */

package cruise;

import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Point;
import java.awt.Toolkit;

import javax.swing.JFrame;

public class SimulatorFrame extends JFrame 
{
	private static final long serialVersionUID = 1L;
	
	CarSimulatorController fCarSimulator;
	CruiseControlController fCruiseControlSimulator;

    public SimulatorFrame( String aTitle )
    {
    	super( aTitle );
    	
        //Make sure we have nice window decorations.
        JFrame.setDefaultLookAndFeelDecorated( true );
        setResizable( false ); // fixed size
                
        setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );

        getContentPane().setLayout( new GridLayout( 1, 2, 1, 1 ) );

        fCarSimulator = new CarSimulatorController();
        fCruiseControlSimulator = new CruiseControlController( fCarSimulator );
        
        getContentPane().add( fCarSimulator );
        getContentPane().add( fCruiseControlSimulator );
        pack();
        
		// align JFrame to center of screen
        Dimension lScreenSize = Toolkit.getDefaultToolkit().getScreenSize();
        Dimension lWindowSize = getSize();
        Point lTopLeft = new Point( (lScreenSize.width - lWindowSize.width) / 2,
        		                    (lScreenSize.height - lWindowSize.height) / 2 );
        
        setLocation( lTopLeft );
    }
}
