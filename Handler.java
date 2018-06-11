import java.awt.Graphics;
import java.util.LinkedList;

public class Handler
{
	LinkedList<GameObject> object = new LinkedList<GameObject>();
	
	public int spd = 5;
	
	public void tick()
	{
		for(int i = 0; i < object.size(); i++)
		{
			GameObject tempObject = object.get(i);
			
			tempObject.tick();
		}
	}
	
	public void render(Graphics g)
	{
		for (int i = 0; i < object.size(); i++)
		{
			GameObject tempObject = object.get(i);
			
			tempObject.render(g);
		}
	}
	
	public void clearEnemies()
	{
		for (int i = 0; i < object.size(); i++)
		{
			GameObject tempObject = object.get(i);
			
			if (tempObject.getID() != ID.Player)
			{
				object.clear();
				if (Game.gameState != Game.STATE.End)
				{
					
				}
				addObject(new Player((int)tempObject.getX(), (int)tempObject.getY(), ID.Player, this));
			}
		}
	}
	
	// Add Enemies
	public void addObject(GameObject object)
	{
		this.object.add(object);
	}
	
	// Remove Enemies
	public void removedObject(GameObject object)
	{
		this.object.remove(object);
	}
}
