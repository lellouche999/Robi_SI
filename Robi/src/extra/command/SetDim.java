package extra.command;

import java.awt.Dimension;

import extra.Reference;
import graphicLayer.GBounded;
import stree.parser.SNode;

public class SetDim implements Command {

	@Override
	public Reference run(Reference reference, SNode method) {
		GBounded e = ((GBounded) reference.getReceiver());
		
		int x = Integer.valueOf(method.get(2).contents());
		int y = Integer.valueOf(method.get(3).contents());
		
		e.setDimension(new Dimension(x, y));
		return reference;
	}

}
