import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;

public class FastEnemy extends GameObject
{
	private Handler handler;
	
	public FastEnemy(int x, int y, ID id, Handler handler)
	{
		super(x, y, id);
		
		this.handler = handler;
		
		velX = 2;
		velY = 9;
	}
	
	public Rectangle getBounds()
	{
		return new Rectangle((int)x, (int)y, 16, 16);
	}
	
	public void tick()
	{
		// Move Enemy
		x += velX;
		y += velY;
		
		// Bounces when it touches the canvas.
		if (y <= 0 || y >= Game.HEIGHT - 32)
		{
			velY *= -1;
		}
		if (x <= 0 || x >= Game.WIDTH - 16)
		{
			velX *= -1;
		}
	}
	
	public void render(Graphics g)
	{
		g.setColor(Color.cyan);
		g.fillRect((int)x, (int)y, 16, 16);
	}
}
