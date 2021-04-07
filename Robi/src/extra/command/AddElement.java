package extra.command;

import graphicLayer.GElement;
import extra.Environment;
import extra.Reference;
import graphicLayer.GContainer;
import stree.parser.SNode;

public class AddElement implements Command {

	@Override
	public Reference run(Reference reference, SNode method) {
		Environment environment = reference.getEnvironment();
		String name = method.get(2).contents();
		String type = method.get(3).get(0).contents();
		Reference ref = (Reference) environment.getReferenceByName(type).run(method.get(3));
		environment.addReference(name, ref);
		ref.addCommand("info", new InfoReference());
		ref.addCommand("add", new AddElement());
		ref.addCommand("del", new DelElement());
		ref.addCommand("sleep", new Sleep());
		ref.addCommand("addScript", new AddScript());
		((GContainer) reference.getReceiver()).addElement((GElement) ref.getReceiver());
		((GContainer) reference.getReceiver()).repaint();
		return ref;
	}

}
