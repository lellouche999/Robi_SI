package extra.command;

import graphicLayer.GSpace;
import graphicLayer.GString;
import extra.Reference;
import graphicLayer.GElement;
import stree.parser.SNode;
import tools.Tools;

public class SetColor implements Command {

	@Override
	public Reference run(Reference reference, SNode method) {
		String color = method.get(2).contents();
		if(reference.getReceiver().getClass() == GSpace.class) {
			((GSpace) reference.getReceiver()).setColor(Tools.getColorByName(color));
		}
		else if(reference.getReceiver().getClass() == GString.class) {
			((GString) reference.getReceiver()).setColor(Tools.getColorByName(color));
		}
		else {
			((GElement) reference.getReceiver()).setColor(Tools.getColorByName(color));
		}
		return reference;
	}

}
