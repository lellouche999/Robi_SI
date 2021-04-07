package extra.command;

import extra.Reference;
import graphicLayer.GElement;
import stree.parser.SNode;

public class NewString implements Command {

	@Override
	public Reference run(Reference reference, SNode method) {
		try {
			String name = method.get(2).contents();
			for(int i = 3; i < method.size(); i++)
				name += " " + method.get(i);
			name = name.replace("\"", "");
			
			@SuppressWarnings("unchecked")
			GElement e = ((Class<GElement>) reference.getReceiver()).getDeclaredConstructor(String.class).newInstance(name);
			Reference ref = new Reference(e);
			ref.addCommand("setColor", new SetColor());
			ref.addCommand("setDim", new SetDim());
			ref.addCommand("translate", new Translate());
			return ref;
		} catch(Exception e) {
			e.printStackTrace();
			return null;
		}
	}
}
