/*
 *	SWE30001, 2023
 *
 *	Application frame center of screen alignment
 * 	
 */

package util;

import java.awt.Dimension;
import java.awt.HeadlessException;
import java.awt.Point;
import java.awt.Toolkit;

import javax.swing.JFrame;

public class ApplicationFrame extends JFrame {

	private static final long serialVersionUID = 1L;

	public ApplicationFrame(String title) throws HeadlessException 
	{
		super(title);

        //Make sure we have nice window decorations.
        JFrame.setDefaultLookAndFeelDecorated( true );

        setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
        setResizable( false ); // fixed size
	}
	
	public void pack()
	{
		super.pack();
		
		// align JFrame to center of screen
        Dimension lScreenSize = Toolkit.getDefaultToolkit().getScreenSize();
        Dimension lWindowSize = getSize();
        Point lTopLeft = new Point( (lScreenSize.width - lWindowSize.width) / 2,
        		                    (lScreenSize.height - lWindowSize.height) / 2 );
        
        setLocation( lTopLeft );
	}
}
