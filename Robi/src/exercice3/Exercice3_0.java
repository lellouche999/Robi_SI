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
		SNode  Comparator, JustDoIt, Argc, Argv;
		expr.children().forEach(e -> System.out.print(e.contents() + " "));
		System.out.println();
		//ici identifier prends le premier ( indice 0)  argument ,soit robi ou space
		Comparator = expr.children().get(0);
		//le cas de space
		if(Comparator.contents().compareTo("space") == 0) {
			//le variable JustDoIt prends le deuxieme argument 
			JustDoIt = Comparator = expr.children().get(1);
			//si JustDoIt == setColor on lit le 3eme argument ( la couleur)
			if(JustDoIt.contents().compareTo("setColor") == 0) {
				Argc = expr.children().get(2);
				Color color;
				try {
					//pour changer la couleur de space 
					Field field = Class.forName("java.awt.Color").getField(Argc.contents());
					color = (Color)field.get(null);
				} catch (Exception e) {
					color = Color.white;
				}
				return new SpaceChangeColor(color);
				//si JustDoIt == a sleep on lit le 3eme argument ( la durée)
			} else if(JustDoIt.contents().compareTo("sleep") == 0) {
				Argc = expr.children().get(2);
				//time prends la valeur de 3eme argument 
				int time = Integer.valueOf(Argc.contents());
				return new SpaceSleep(time);
			}
			//ici on fait pareil mais cette fois pour robi, le variable action prends le deuxieme argument 
		} else if(Comparator.contents().compareTo("robi") == 0) {
			JustDoIt = Comparator = expr.children().get(1);
			//si JustDoIt == setColor on lit le 3 eme argument ( la couleur )
			if(JustDoIt.contents().compareTo("setColor") == 0) {
				Argc = expr.children().get(2);
				Color color;
				try {
					//pour changer la couleur de robi 
					Field field = Class.forName("java.awt.Color").getField(Argc.contents());
					color = (Color)field.get(null);
				} catch (Exception e) {
					color = Color.white;
				}
				return new RobiChangeColor(color);
				//si le 2eme(JustDoIt) argument est : translate, on lit le 3 et 4 eme argument 
				//le 3 et 4 eme argument sont des coordonnées, d'où quel point vers quel point
			} else if(JustDoIt.contents().compareTo("translate") == 0) {
				Argc = expr.children().get(2);
				Argv = expr.children().get(3);
				int x = Integer.valueOf(Argc.contents());
				int y = Integer.valueOf(Argv.contents());
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
