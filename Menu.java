import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Random;

public class Menu extends MouseAdapter
{
	private Game game;
	private Handler handler;
	private HUD hud;
	private Random r = new Random();
	
	public Menu(Game game, Handler handler, HUD hud)
	{
		this.game = game;
		this.hud = hud;
		this.handler = handler;
	}
	
	public void mousePressed(MouseEvent e)
	{
		int mx = e.getX();
		int my = e.getY();
		
		if (game.gameState == Game.STATE.Menu)
		{
			// Play Button
			if (mouseOver(mx, my, 210, 150, 200, 64))
			{
				game.gameState = Game.STATE.Select;
				return;
			}
			
			// Help Button
			if (mouseOver(mx, my, 210, 250, 200, 64))
			{
				game.gameState = Game.STATE.Help;
			}
			
			// Quit Button
			if (mouseOver(mx, my, 210, 350, 200, 64))
			{
				System.exit(1);
			}
		}
		
		if (game.gameState == Game.STATE.Select)
		{
			// Normal Button
			if (mouseOver(mx, my, 210, 150, 200, 64))
			{
				game.gameState = Game.STATE.Game;
				handler.addObject(new Player(Game.WIDTH / 2 - 32, Game.HEIGHT / 2 - 32, ID.Player, handler));
				handler.clearEnemies();
				handler.addObject(new BasicEnemy(r.nextInt(Game.WIDTH), r.nextInt(Game.HEIGHT), ID.BasicEnemy, handler));
				
				game.diff = 0;
			}
			
			// Hard Button
			if (mouseOver(mx, my, 210, 250, 200, 64))
			{
				game.gameState = Game.STATE.Game;
				handler.addObject(new Player(Game.WIDTH / 2 - 32, Game.HEIGHT / 2 - 32, ID.Player, handler));
				handler.clearEnemies();
				handler.addObject(new HardEnemy(r.nextInt(Game.WIDTH), r.nextInt(Game.HEIGHT), ID.BasicEnemy, handler));
				
				game.diff = 1;
			}
			
			// Back Button
			if (mouseOver(mx, my, 210, 350, 200, 64))
			{
				if (mouseOver(mx, my, 210, 350, 200, 64))
				{
					game.gameState = Game.STATE.Menu;
					return;
				}
			}
		}
		
		// Back Button for Help
		if (game.gameState == Game.STATE.Help)
		{
			if (mouseOver(mx, my, 210, 350, 200, 64))
			{
				game.gameState = Game.STATE.Menu;
				return;
			}
		}
		
		// Reset Game
		if (game.gameState == Game.STATE.End)
		{
			if (mouseOver(mx, my, 210, 350, 200, 64))
			{
				game.gameState = Game.STATE.Menu;
				hud.setLevel(1);
				hud.setScore(0);
			}
		}
	}
	
	public void mouseReleased(MouseEvent e) {}
	
	private boolean mouseOver(int mx, int my, int x, int y, int width, int height)
	{
		if (mx > x && mx < x + width)
		{
			if (my > y && my < y + height)
			{
				return true;
			}
			else
			{
				return false;
			}
		}
		else
		{
			return false;
		}
	}
	
	public void tick()
	{
		
	}
	
	public void render(Graphics g)
	{
		if (game.gameState == Game.STATE.Menu)
		{
			Font fnt = new Font("arial", 1, 50);
			Font fnt2 = new Font("arial", 1, 30);
			
			g.setFont(fnt);
			g.setColor(Color.white);
			g.drawString("Wave", 250, 70);
			
			g.setFont(fnt2);
			g.drawRect(210, 150, 200, 64);
			g.drawString("Play", 275, 190);
			
			g.drawRect(210, 250, 200, 64);
			g.drawString("Help", 275, 290);
			
			g.drawRect(210, 350, 200, 64);
			g.drawString("Quit", 275, 390);
		}
		else if (game.gameState == Game.STATE.Help)
		{
			Font fnt = new Font("arial", 1, 50);
			Font fnt2 = new Font("arial", 1, 30);
			Font fnt3 = new Font("arial", 1, 20);
			
			g.setFont(fnt);
			g.setColor(Color.white);
			g.drawString("Help", 240, 70);
			
			g.setFont(fnt2);
			g.drawString("Use WASD keys to move player", 50, 150);
			g.drawString("and dodge enemies.", 50, 200);
			g.drawString("You can buy upgrades with", 50, 270);
			g.drawString("your score.", 50, 320);
			
			g.setFont(fnt3);
			g.drawRect(210, 350, 200, 64);
			g.drawString("Back", 270, 390);
		}
		else if (game.gameState == Game.STATE.End)
		{
			Font fnt = new Font("arial", 1, 50);
			Font fnt2 = new Font("arial", 1, 30);
			Font fnt3 = new Font("arial", 1, 20);
			
			g.setFont(fnt);
			g.setColor(Color.white);
			g.drawString("Game Over", 190, 70);
			
			g.setFont(fnt2);
			g.drawString("You lost with a score of: " + hud.getScore(), 50, 200);
			
			g.setFont(fnt3);
			g.drawRect(210, 350, 200, 64);
			g.drawString("Try Again", 265, 390);
		}
		else if (game.gameState == Game.STATE.Select)
		{
			Font fnt = new Font("arial", 1, 50);
			Font fnt2 = new Font("arial", 1, 30);
			
			g.setFont(fnt);
			g.setColor(Color.white);
			g.drawString("SELECT DIFFICULTY", 70, 70);
			
			g.setFont(fnt2);
			g.drawRect(210, 150, 200, 64);
			g.drawString("Normal", 260, 190);
			
			g.drawRect(210, 250, 200, 64);
			g.drawString("Hard", 275, 290);
			
			g.drawRect(210, 350, 200, 64);
			g.drawString("Back", 275, 390);
		}
	}
}
