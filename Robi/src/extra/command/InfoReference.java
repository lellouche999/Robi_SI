package extra.command;

import extra.Environment;
import extra.Reference;
import stree.parser.SNode;

public class InfoReference implements Command {

	@Override
	public Reference run(Reference reference, SNode method) {
		Environment e = reference.getEnvironment();
		e.show();
		return reference;
	}

}
