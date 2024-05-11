/*
 *	SWE30001, 2023
 *
 * 	Text utility 
 * 
 */

package util;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;

public class Text 
{
	private String fValue;
	private Color fColor;
	private Font fFont;

	public void setValue( String aValue )
	{
		fValue = aValue;
	}
	
	public void setColor( Color aColor )
	{
		fColor = aColor;
	}
	
	public void setFont( Font aFont )
	{
		fFont = aFont;
	}
	
	public Text( String aValue, Color aColor, Font aFont )
	{
		fValue = aValue;
		fColor = aColor;
		fFont = aFont;
	}

	public int getBaselineOffset( Graphics2D g )
	{
		g.setFont( fFont );
		FontMetrics lFontData = g.getFontMetrics();
	
		return lFontData.getHeight()/2 + lFontData.getAscent();
	}

	public int getRenderWidth( Graphics2D g )
	{
		g.setFont( fFont );
		FontMetrics lFontData = g.getFontMetrics();
	
		return lFontData.stringWidth( fValue );
	}

	public void render( Graphics2D g, int aX, int aY )
	{
		g.setColor( fColor );
		g.setFont( fFont );
		g.drawString( fValue, aX, aY );
	}
	
	public void renderCentered( Graphics2D g, Dimension aSurface )
	{
		g.setFont( fFont );

		FontMetrics lFontData = g.getFontMetrics();
		int lRenderWidth = lFontData.stringWidth( fValue );

        // calculate the leftmost position for text within surface
        int lXPos = (aSurface.width - lRenderWidth)/2;
        // calculate baseline: 1: height minus font height divided by two
        //					   2: this is top line (centered relative to surface)
        //					   3: adjust baseline by ascent of font 
        int lYPos = (aSurface.height - lFontData.getHeight())/2 + lFontData.getAscent();
        
		render( g, lXPos, lYPos );
	}

	public void renderXCentered( Graphics2D g, int aWidth, int aY )
	{
		g.setFont( fFont );

		FontMetrics lFontData = g.getFontMetrics();
		int lRenderWidth = lFontData.stringWidth( fValue );
		
		render( g, (aWidth - lRenderWidth)/2, aY );
	}
}
