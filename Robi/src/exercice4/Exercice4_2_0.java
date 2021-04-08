package exercice4;

 /*	
(space add robi (rect new))
(space.robi translate 130 50)
(space.robi setColor yellow)
(space add momo (oval new))
(space.momo setColor red)
(space.momo translate 80 80)
(space add pif (image new alien.gif))
(space.pif translate 100 0)
(space add hello (string new "Hello world"))
(space.hello translate 10 10)
(space.hello setColor black)
*/


import java.awt.Dimension;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.Iterator;
import java.util.List;

import graphicLayer.GElement;
import graphicLayer.GImage;
import graphicLayer.GOval;
import graphicLayer.GRect;
import graphicLayer.GSpace;
import graphicLayer.GString;
import extra.Environment;
import extra.Interpreter;
import extra.Reference;
import extra.command.*;
import stree.parser.SNode;
import stree.parser.SParser;
import tools.Tools;



class NewElement implements Command {
	public Reference run(Reference reference, SNode method) {
		try {
			@SuppressWarnings("unchecked")
			GElement e = ((Class<GElement>) reference.getReceiver()).getDeclaredConstructor().newInstance();
			Reference ref = new Reference(e);
			ref.addCommand("setColor", new SetColor());
			ref.addCommand("translate", new Translate());
			ref.addCommand("setDim", new SetDim());
			return ref;
		} catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException
				| NoSuchMethodException | SecurityException e) {
			e.printStackTrace();
		}
		return null;
	}
}


public class Exercice4_2_0 {
	// Une seule variable d'instance
	Environment environment = new Environment();
	GSpace space;

	public Exercice4_2_0() {
		space = new GSpace("Exercice 4", new Dimension(200, 100));
		space.open();

		Reference spaceRef = new Reference(space);
		
		/*
		Reference rectClassRef = new Reference(GRect.class);
		Reference ovalClassRef = new Reference(GOval.class);
		Reference imageClassRef = new Reference(GImage.class);
		Reference stringClassRef = new Reference(GString.class);
		*/
		spaceRef.addCommand("setColor", new SetColor());
		spaceRef.addCommand("sleep", new Sleep());

		spaceRef.addCommand("add", new AddElement());
		spaceRef.addCommand("del", new DelElement());
		
		spaceRef.addCommand("info", new InfoReference());
		spaceRef.addCommand("addScript", new AddScript());
		
		spaceRef.getEnvironment();
		
		environment.addReference("space", spaceRef);
		
		/*
		rectClassRef.addCommand("new", new NewElement());
		ovalClassRef.addCommand("new", new NewElement());
		imageClassRef.addCommand("new", new NewImage());
		stringClassRef.addCommand("new", new NewString());
		
		environment.addReference("rect.class", rectClassRef);
		environment.addReference("oval.class", ovalClassRef);
		environment.addReference("image.class", imageClassRef);
		environment.addReference("label.class", stringClassRef);
		*/
		
		this.mainLoop();
	}
	
	private void mainLoop() {
		while (true) {
			// prompt
			System.out.print("> ");
			// lecture d'une serie de s-expressions au clavier (return = fin de la serie)
			String input = Tools.readKeyboard();
			// creation du parser
			SParser<SNode> parser = new SParser<>();
			// compilation
			List<SNode> compiled = null;
			try {
				compiled = parser.parse(input);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			// execution des s-expressions compilees
			Iterator<SNode> itor = compiled.iterator();
			SNode exec;
			Reference receiver;
			
			while (itor.hasNext()) {
				exec=itor.next();
				receiver = getReceiver(environment, exec);
				receiver.run(exec);
			}
		}
	}

	private Reference getReceiver(Environment environment, SNode next) {
		String receiverName = next.get(0).contents();
		String[] allRefNames = receiverName.split("\\.");
		Reference receiver = environment.getReferenceByName(receiverName);
		Environment refEnv = environment;
		
		for(String refName : allRefNames) {
			receiver = refEnv.getReferenceByName(refName);
			refEnv = receiver.getEnvironment();
		}
		
		return receiver;
	}

	private void run(SNode expr) {
		// quel est le nom du receiver
		String receiverName = expr.get(0).contents();
		// quel est le receiver
		Reference receiver = environment.getReferenceByName(receiverName);
		// demande au receiver d'executer la s-expression compilee
		receiver.run(expr);
	}
	
	public static void main(String[] args) {
		new Exercice4_2_0();
	}

}