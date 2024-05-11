/*
 *	SWE30001, 2023
 * 
 * 	Hardware interrupt simulator
 * 
 */

package util;

import java.util.Random;

public class Hardware 
{
	private static Random fGenerator = new Random( 12345 );
	
	public static void HWinterrupt()
	{
		if ( fGenerator.nextDouble() < 0.5 )
		{
			Thread.yield();
		}
	}
}
