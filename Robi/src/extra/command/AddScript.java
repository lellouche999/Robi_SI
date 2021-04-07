package extra.command;

import extra.Reference;
import stree.parser.SNode;

public class AddScript implements Command {

	@Override
	public Reference run(Reference reference, SNode method) {
		String name = method.get(2).contents();
		reference.addCommand(name, new RunScript(method.get(3)));
		return reference;
	}

}
