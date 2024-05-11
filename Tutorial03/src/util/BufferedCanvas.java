/*
 *	SWE30001, 2023
 *
 * 	Double Buffering Support
 * 
 * 	Uses hardware acceleration if possible and avoids "flickering" effect.
 */


package util;

import java.awt.Canvas;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GraphicsConfiguration;
import java.awt.Image;
import java.awt.image.VolatileImage;

public abstract class BufferedCanvas extends Canvas 
{
	private static final long serialVersionUID = 1L;

	private static boolean fUseBuffering = !System.getProperty( "os.name" ).startsWith( "Mac OS" );
	
	public static boolean isBuffering()
	{
		return fUseBuffering;
	}
	
	private static boolean fUseBufferedImage = false;
	
	public static void useBufferedImage( boolean aFlag )
	{
		fUseBufferedImage = aFlag;
	}

	private Image fBufferImage;

	private void createBufferImage()
	{
		fBufferImage = null;

		if ( fUseBuffering )
		{
			ensureBufferImage();
		}
	}

	private void ensureBufferImage()
	{
		if ( fBufferImage == null )
		{
			if ( fUseBufferedImage )
			{
				fBufferImage = createImage( getWidth(), getHeight() );
			}
			else
			{
				fBufferImage = createVolatileImage( getWidth(), getHeight() );
			}
		}
	}
	
	private void ensureCompatibility()
	{
		if ( ((VolatileImage)fBufferImage).validate( getGraphicsConfiguration() ) == VolatileImage.IMAGE_INCOMPATIBLE )
		{
			// recreate image
			createBufferImage();
		}
	}
	
	public BufferedCanvas() 
	{
		super();
		
		createBufferImage();
	}

	public BufferedCanvas( GraphicsConfiguration aConfiguration ) 
	{
		super( aConfiguration );
		
		createBufferImage();
	}

	public final void paint( Graphics g )
	{
		if ( fUseBuffering )
		{
			ensureBufferImage();

			if ( fUseBufferedImage )
			{
				Graphics2D lGraphics = (Graphics2D)fBufferImage.getGraphics();

				render( lGraphics );
				
				lGraphics.dispose();
				g.drawImage( fBufferImage, 0, 0, this );
			}
			else
			{
				do 
				{
					ensureCompatibility();
					
					// draw buffer image
					Graphics2D lGraphics = (Graphics2D)fBufferImage.getGraphics();
						
					render( lGraphics );
						
					lGraphics.dispose();
					g.drawImage( fBufferImage, 0, 0, this );
				} while ( ((VolatileImage)fBufferImage).contentsLost() );
			}
		}
		else
		{
			render( (Graphics2D)g );
		}
	}
	
    public final void update( Graphics g )
	{
    	paint( g );
	}

    // user-defined render method
	public abstract void render( Graphics2D g );
}
