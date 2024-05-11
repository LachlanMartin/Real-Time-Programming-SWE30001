/*
 *	SWE30001, 2023
 *
 *	Ornamental Garden Problem
 * 
 */

package garden;

public class OrnamentalGarden
{
	public static int MAX = 20;
	
    private static void createAndShowGUI() 
    {
    	(new OrnamentalGardenFrame( "Ornamental Garden" )).setVisible( true );
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
