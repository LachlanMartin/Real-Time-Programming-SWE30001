/*
 *	SWE30001, 2023
 *
 * 	Car speed interface 
 * 
 *  Code based on CruiseControl, Concurrency: State Models & Java Programs
 * 
 */

package cruise;

public interface ICarSpeed
{
	public int getMaxSpeed();

	public double getSpeed();

	public void setThrottle( double aPercent );
	
	public void setCruiseController( ICruiseControl aController );
	
	public double drag();
}
