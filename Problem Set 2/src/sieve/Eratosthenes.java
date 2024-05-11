package sieve;


public class Eratosthenes 
{
	public static final int CONTROL_SIZE = 100;
	public static final int NUMBER_SIZE = CONTROL_SIZE/2;
	public static final int TESTS = 100;
	public static final int ROWS = 10;
	public static final int COLUMNS = 10;
	
	private static void createAndShowGUI() 
    {
    	(new EratosthenesFrame( "Eratosthenes" )).setVisible( true );
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
