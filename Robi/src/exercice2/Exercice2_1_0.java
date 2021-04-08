package exercice2;

import java.awt.Dimension;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;

import graphicLayer.GRect;
import graphicLayer.GSpace;
import stree.parser.SNode;
import stree.parser.SParser;
import tools.Tools;


public class Exercice2_1_0
{
	GSpace space = new GSpace("Exercice 2_1", new Dimension(200, 100));
	GRect robi = new GRect();
	//script sous forme de chaine de caractere
	String script = "(space setColor black) (robi setColor yellow)";

	public Exercice2_1_0()
	{
		//Création de la fenetre
		space.addElement(robi);
		space.open();
		this.runScript();
	}

	private void runScript()
	{
		SParser<SNode> parser = new SParser<>();
		List<SNode> rootNodes = null;
		try {
			//Le script sous forme de chaine de caractère est transformé en
			//arbre de SNode
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
		SNode identifier, action, arg1;
		//affichage du contenu des noeuds
		expr.children().forEach(a -> System.out.print(a.contents() + " "));
		//Selection du premier noeud
		identifier = expr.children().get(0);
		//Si le contenu du premier noeud est "space"
		if(identifier.contents().compareTo("space") == 0)
		{
			//Le contenu du second noeud correspond à l'action
			action = expr.children().get(1);
			//Si l'action est "setColor"
			if(action.contents().compareTo("setColor") == 0)
			{
				arg1 = expr.children().get(2);
				//On utilise le contenu du 3eme noeud avec la méthode setColor
				//pour changer la couleur de "space"
				space.setColor(Tools.getColorByName(arg1.contents()));
			}
			
		}
		//Si le contenu du premier noeud est "space"
		else if(identifier.contents().compareTo("robi") == 0)
		{
			//Le contenu du second noeud correspond à l'action
			action = expr.children().get(1);
			//Si l'action est "setColor"
			if(action.contents().compareTo("setColor") == 0)
			{
				arg1 = expr.children().get(2);
				//On utilise le contenu du 3eme noeud avec la méthode setColor
				//pour changer la couleur de "robi-"
				robi.setColor(Tools.getColorByName(arg1.contents()));
			}
		}
	}

	public static void main(String[] args) {
		new Exercice2_1_0();
	}
}
