/*
 *	SWE30001, 2023
 *
 * 	Button utility 
 * 
 */

package util;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionListener;

import javax.swing.JButton;

public class MakeButton 
{
	public static Font DefaultFont = new Font( "Arial", Font.PLAIN, 18 );
	
	public static JButton createButton( String aTitle, ActionListener aListener )
	{
		return createButton( aTitle, aListener, MakeButton.DefaultFont );
	}

	public static JButton createButton( String aTitle, ActionListener aListener, Font aFont )
	{
		JButton Result = new JButton( aTitle );

        Result.setFont( aFont );
		Result.addActionListener( aListener );
		
		return Result;
	}

	public static JButton createButton( String aTitle, ActionListener aListener, Dimension aSize )
	{
		return createButton( aTitle, aListener, aSize, MakeButton.DefaultFont );
	}

	public static JButton createButton( String aTitle, ActionListener aListener, Dimension aSize, Font aFont )
	{
		JButton Result = createButton( aTitle, aListener, aFont );
		
		Result.setPreferredSize( aSize );
		
		return Result;		
	}

	public static JButton createButton( String aTitle, ActionListener aListener, int aHeight )
	{
		return createButton( aTitle, aListener, aHeight, MakeButton.DefaultFont );
	}

	public static JButton createButton( String aTitle, ActionListener aListener, int aHeight, Font aFont )
	{
		JButton Result = createButton( aTitle, aListener, aFont );
		Dimension lSize = Result.getPreferredSize();
		
		lSize.height = aHeight;
		
		Result.setPreferredSize( lSize );
		
		return Result;		
	}
}
