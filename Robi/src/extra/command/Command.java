package extra.command;

import extra.Reference;
import stree.parser.SNode;

public interface Command {
	abstract public Reference run(Reference reference, SNode method);
}
