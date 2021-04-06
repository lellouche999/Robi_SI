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
		//analyseur 
		SNode identifier, action, arg1, arg2;
		expr.children().forEach(e -> System.out.print(e.contents() + " "));
		System.out.println();
		identifier = expr.children().get(0);
		if(identifier.contents().compareTo("space") == 0) {
			action = identifier = expr.children().get(1);
			if(action.contents().compareTo("setColor") == 0) {
				arg1 = expr.children().get(2);
				Color color;
				try {
					//pour changer la coulur de space 
					Field field = Class.forName("java.awt.Color").getField(arg1.contents());
					color = (Color)field.get(null);
				} catch (Exception e) {
					color = Color.white;
				}
				return new SpaceChangeColor(color);
			} else if(action.contents().compareTo("sleep") == 0) {
				arg1 = expr.children().get(2);
				int time = Integer.valueOf(arg1.contents());
				return new SpaceSleep(time);
			}
		} else if(identifier.contents().compareTo("robi") == 0) {
			action = identifier = expr.children().get(1);
			if(action.contents().compareTo("setColor") == 0) {
				arg1 = expr.children().get(2);
				Color color;
				try {
					Field field = Class.forName("java.awt.Color").getField(arg1.contents());
					color = (Color)field.get(null);
				} catch (Exception e) {
					color = Color.white;
				}
				return new RobiChangeColor(color);
				
			} else if(action.contents().compareTo("translate") == 0) {
				arg1 = expr.children().get(2);
				arg2 = expr.children().get(3);
				int x = Integer.valueOf(arg1.contents());
				int y = Integer.valueOf(arg2.contents());
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
	
	//cette class permet de changer la couleur de space
	public class SpaceChangeColor implements Command {
		private Color newColor;
		public SpaceChangeColor(Color newColor) {
			this.newColor = newColor;
		}

		@Override
		public void run() {
			space.setColor(newColor);
		}

	}
	
	//cette class permet mettre space en pause pour une durée prédéfini 
	public class SpaceSleep implements Command {
		int timeOut;
		public SpaceSleep(int timeToSleep) {
			this.timeOut = timeToSleep;
		}

		@Override
		public void run() {
			try {
				Thread.sleep(timeOut);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	
	//cette class permet de changer la couleur de Robi
	public class RobiChangeColor implements Command {
		Color newColor;
		public RobiChangeColor(Color newColor) {
			this.newColor = newColor;
		}

		@Override
		public void run() {
			robi.setColor(newColor);
		}
	}
	
	//cette class permet de déplacer le   Robi
	public class RobiTranslate implements Command {
		private Point p;
		public RobiTranslate(int x, int y) {
			this.p = new Point(robi.getX() + x, robi.getY() + y);
		}
		@Override
		public void run() {
			robi.setPosition(p);
		}
		
	}

	
	
}
