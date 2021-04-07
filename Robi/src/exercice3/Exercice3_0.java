package exercice3;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Point;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.Iterator;
import java.util.List;

import graphicLayer.GRect;
import graphicLayer.GSpace;
import stree.parser.SNode;
import stree.parser.SParser;

public class Exercice3_0 {
	GSpace space = new GSpace("Exercice 3", new Dimension(200, 100));
	GRect robi = new GRect();
	String script = "" +
			"   (space setColor black) " +
			"   (robi setColor yellow)" +
			"   (space sleep 1000)" +
			"   (space setColor white)\n" + 
			"   (space sleep 1000)" +
			"	(robi setColor red) \n" + 
			"   (space sleep 1000)" +
			"	(robi translate 100 0)\n" + 
			"	(space sleep 1000)\n" + 
			"	(robi translate 0 50)\n" + 
			"	(space sleep 1000)\n" + 
			"	(robi translate -100 0)\n" + 
			"	(space sleep 1000)\n" + 
			"	(robi translate 0 -40)";

	public Exercice3_0() {
		space.addElement(robi);
		space.open();
		this.runScript();
	}

	private void runScript() {
		SParser<SNode> parser = new SParser<>();
		List<SNode> rootNodes = null;
		try {
			rootNodes = parser.parse(script);
		} catch (IOException e) {
			e.printStackTrace();
		}
		Iterator<SNode> itor = rootNodes.iterator();
		while (itor.hasNext()) {
			this.run(itor.next());
		}
	}

	private void run(SNode expr) {
		Command cmd = getCommandFromExpr(expr);
		if (cmd == null)
			throw new Error("unable to get command for: " + expr);
		cmd.run();
	}

	Command getCommandFromExpr(SNode expr) {
		SNode graphic, action, argument;
		
		for(SNode n : expr.children()) {
			System.out.print(n.contents() + " ");
		}
		System.out.println();
		
		graphic = expr.children().get(0);	//récuperation de l'élement graphique, l'action et l'argument des commandes
		action = expr.children().get(1);
		argument = expr.children().get(2);
		
		//test de l'element graphique a modifier
		if(graphic.contents().compareTo("space") == 0) {
			
			//test de l'action de modification
			if(action.contents().compareTo("setColor") == 0) {
				Color color;
				try {
					Field field = Class.forName("java.awt.Color").getField(argument.contents());
					color = (Color)field.get(null);
				} catch (Exception e) {
					color = Color.white;
				}
				return new SpaceChangeColor(color);
				
			//test de l'action de modification	
			} else if(action.contents().compareTo("sleep") == 0) {
				int time = Integer.valueOf(argument.contents());
				return new SpaceSleep(time);
			}
			
		//test de l'element graphique a modifier
		} else if(graphic.contents().compareTo("robi") == 0) {
			
			//test de l'action de modification				
			if(action.contents().compareTo("setColor") == 0) {
				Color color;
				try {
					Field field = Class.forName("java.awt.Color").getField(argument.contents());
					color = (Color)field.get(null);
				} catch (Exception e) {
					color = Color.white;
				}
				return new RobiChangeColor(color);
				
			//test de l'action de modification					
			} else if(action.contents().compareTo("translate") == 0) {
				int x = Integer.valueOf(expr.children().get(2).contents());
				int y = Integer.valueOf(expr.children().get(3).contents());
				return new RobiTranslate(x, y);
			}
		}
		return null;
	}

	public static void main(String[] args) {
		new Exercice3_0();
	}

	public interface Command {
		abstract public void run();
	}

	public class SpaceChangeColor implements Command {
		Color newColor;

		public SpaceChangeColor(Color newColor) {
			this.newColor = newColor;
		}

		@Override
		public void run() {
			space.setColor(newColor);
		}

	}

	public class SpaceSleep implements Command {
		int timeToSleep;

		public SpaceSleep(int timeToSleep) {
			this.timeToSleep = timeToSleep;
		}

		@Override
		public void run() {
			try {
				Thread.sleep(timeToSleep);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}

	}

	public class RobiChangeColor implements Command {
		Color color;

		public RobiChangeColor(Color newColor) {
			this.color = newColor;
		}

		@Override
		public void run() {
			robi.setColor(color);
		}
	}
	
	public class RobiTranslate implements Command {
		int x, y;

		public RobiTranslate(int x, int y) {
			this.x = x;
			this.y = y;
		}

		@Override
		public void run() {
			robi.translate(new Point(x, y));
		}
	}
}