/*
 *	SWE30001, 2023
 *
 *	Thread demo
 * 
 */

package multithreading;

public class MultiThreading
{
    private static void createAndShowGUI() 
    {
    	(new MultiThreadingFrame( "Multithreading" )).setVisible( true );
    }

    public static void main(String[] args) 
    {
        //Schedule a job for the event-dispatching thread:
        //creating and showing this application's GUI.
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                createAndShowGUI();
            }
        });
    }
}
