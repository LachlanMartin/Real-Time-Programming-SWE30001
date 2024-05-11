package counter;

public class Counter 
{
	private static void createAndShowGUI()
	{
		(new CounterFrame ("Counter")).setVisible(true);
	}
	
	public static void main(String[] args)
	{
		//Schedule a job for the even-dispatching thread:
		//Creating and showing this applications GUI.
		javax.swing.SwingUtilities.invokeLater(new Runnable() {public void run() {createAndShowGUI();}});
	}
}
