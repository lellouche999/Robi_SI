package extra.command;

import extra.Reference;
import stree.parser.SNode;

public class Sleep implements Command {

	@Override
	public Reference run(Reference reference, SNode method) {
		int timeToSleep = Integer.valueOf(method.get(2).contents());
		try {
			Thread.sleep(timeToSleep);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		return reference;
	}
}
