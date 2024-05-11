/*
 *	SWE30001, 2023
 *
 *	Ornamental Garden Problem
 * 
 */

package garden;

import util.NumberCanvas;

public class Turnstile implements Runnable 
{
	private NumberCanvas fView;
	private Counter fPatrons;
	private Thread fThread;
	private boolean fSynchronize;
	private OrnamentalGardenFrame fContainer;
	
	public Turnstile( OrnamentalGardenFrame aContainer, NumberCanvas aView, Counter aPatrons, boolean aSynchronize )
	{
		fContainer = aContainer;
		fView = aView;			// turnstile display
		fPatrons = aPatrons;	// counter for all patrons
		fSynchronize = aSynchronize;
		fThread = null;
	}
	
	public void go()
	{
		fThread = new Thread( this );
		fThread.start();
	}
	
	public void run()
	{
		try
		{
			fView.setValue( 0 );
			
			for ( int i = 1; i <= OrnamentalGarden.MAX; i++ )
			{
				Thread.sleep( 500 ); 	// 0.5s between arrivals
				fView.setValue( i );	// count arrival at gate

				if ( fSynchronize )
				{
					synchronized (fPatrons)
					{
						fPatrons.increment(); 	// count total patrons
					}
				}
				else
				{
					fPatrons.increment(); 	// count total patrons					
				}
			}
		}
		catch (InterruptedException e)
		{}
		
		fThread = null;
		fContainer.end(); 	// notify container
	}
}
