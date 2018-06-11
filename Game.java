import java.awt.Canvas;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.util.Random;

public class Game extends Canvas implements Runnable
{
	private static final long serialVersionUID = 1550691097823471818L;
	
	public static final int WIDTH = 640, HEIGHT = WIDTH / 12 * 9;
	
	private Thread thread;
	private boolean running = false;
	
	public static boolean paused = false;
	public int diff = 0;	// 0 = normal, 1 = hard
	
	private Random r;
	private Handler handler;
	private HUD hud;
	private Spawn spawner;
	private Menu menu;
	private Shop shop;
	
	// States of Game
	public enum STATE
	{
		Menu,
		Select,
		Help,
		Shop,
		Game,
		End
	};
	
	// First State of Game
	public static STATE gameState = STATE.Menu;
	
	public static BufferedImage sprite_sheet;
	
	public Game()
	{
		handler = new Handler();
		hud = new HUD();
		shop = new Shop(handler, hud);
		menu = new Menu(this, handler, hud);
		this.addKeyListener(new KeyInput(handler, this));
		this.addMouseListener(menu);
		this.addMouseListener(shop);
		
		// Get Background Song
		AudioPlayer.load();
		AudioPlayer.getMusic("music").loop();
		
		new Window(WIDTH, HEIGHT, "Game", this);
		
		spawner = new Spawn(handler, hud, this);
		r = new Random();
		
		if (gameState == STATE.Game)
		{
			// Player
			handler.addObject(new Player(WIDTH / 2 - 32, HEIGHT / 2 - 32, ID.Player, handler));
			// Enemy
			handler.addObject(new BasicEnemy(r.nextInt(WIDTH), r.nextInt(HEIGHT), ID.BasicEnemy, handler));
		}
		else
		{
			for (int i = 0; i < 20; i++)
			{
				// Menu Particles
				handler.addObject(new MenuParticle(r.nextInt(WIDTH), r.nextInt(HEIGHT), ID.MenuParticle, handler));
			}
		}
	}
	
	public synchronized void start()
	{
		thread = new Thread(this);
		thread.start();
		running = true;
	}
	
	public synchronized void stop()
	{
		try
		{
			thread.join();
			running = false;
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
	
	public void run()
	{
		this.requestFocus();
		long lastTime = System.nanoTime();
		double amountOfTicks = 60.0;
		double ns = 1e9 / amountOfTicks;
		double delta = 0;
		long timer = System.currentTimeMillis();
		int frames = 0;
		while (running)
		{
			long now = System.nanoTime();
			delta += (now - lastTime) / ns;
			lastTime = now;
			while (delta >= 1)
			{
				tick();
				delta--;
			}
			if (running)
			{
				render();
			}
			frames++;
			
			if (System.currentTimeMillis() - timer > 1000)
			{
				timer += 1000;
				frames = 0;
			}
		}
		
		stop();
	}
	
	private void tick()
	{
		if (gameState == STATE.Game)
		{
			if (!paused)
			{
				hud.tick();
				spawner.tick();
				handler.tick();
				
				if (HUD.HEALTH <= 0)
				{
					// Sets the health to 100
					HUD.HEALTH = 100;
					// End the Game
					gameState = STATE.End;
					// Clear All Enemies
					handler.clearEnemies();
					for (int i = 0; i < 20; i++)
					{
						// Add Particles
						handler.addObject(new MenuParticle(r.nextInt(WIDTH), r.nextInt(HEIGHT), ID.MenuParticle, handler));
					}
				}
			}
		}
		else if (gameState == STATE.Menu || gameState == STATE.End || gameState == STATE.Select)
		{
			menu.tick();
			handler.tick();
		}
	}
	
	private void render()
	{
		BufferStrategy bs = this.getBufferStrategy();
		if (bs == null)
		{
			this.createBufferStrategy(3);
			return;
		}
		
		Graphics g = bs.getDrawGraphics();
		
		g.setColor(Color.black);
		g.fillRect(0, 0, WIDTH, HEIGHT);
		
		if (paused)
		{
			g.setColor(Color.white);
			g.setFont(new Font("arial", 0, 50));
			g.drawString("PAUSED", 220, 200);
		}
		
		if (gameState == STATE.Game)
		{
			hud.render(g);
			handler.render(g);
		}
		else if (gameState == STATE.Shop)
		{
			shop.render(g);
		}
		else if (gameState == STATE.Menu || gameState == STATE.Help || gameState == STATE.End || gameState == STATE.Select)
		{
			menu.render(g);
			handler.render(g);
		}
				
		g.dispose();
		bs.show();
	}
	
	public static float clamp(float var, float min, float max)
	{
		if (var >= max)
		{
			return var = max;
		}
		else if (var <= min)
		{
			return var = min;
		}
		else
		{
			return var;
		}
	}
	
	public static void main(String args[])
	{
		new Game();
	}
}
