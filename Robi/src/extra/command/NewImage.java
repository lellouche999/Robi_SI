package extra.command;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;

import javax.imageio.ImageIO;

import extra.Reference;
import graphicLayer.GElement;
import stree.parser.SNode;

public class NewImage implements Command {

	@Override
	public Reference run(Reference reference, SNode method) {
		try {
			File path = new File(method.get(2).contents());
			BufferedImage rawImage = null;
			rawImage = ImageIO.read(path);
			
			@SuppressWarnings("unchecked")
			GElement e = ((Class<GElement>) reference.getReceiver()).getDeclaredConstructor(Image.class).newInstance(rawImage);
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
