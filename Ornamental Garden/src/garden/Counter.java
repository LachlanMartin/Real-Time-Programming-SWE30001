/*
 *	SWE30001, 2023
 *
 *	Ornamental Garden Problem
 * 
 */

package garden;

import util.Hardware;
import util.NumberCanvas;

public class Counter 
{
	private NumberCanvas fView;
	
	public Counter( NumberCanvas aView )
	{
		fView = aView;
		fView.setValue( 0 );
	}
	
	public void increment()
	{
		int temp = fView.getValue();	// read value
		
		Hardware.HWinterrupt();
		
		temp++;							// write value
		
		fView.setValue( temp );
	}
}
