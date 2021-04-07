package exercice1;

import java.awt.Color;
import java.awt.Dimension;
import java.util.Random;

import graphicLayer.GRect;
import graphicLayer.GSpace;
import tools.Tools;

public class Exercice1_0 {
	GSpace space = new GSpace("Exercice 1", new Dimension(200, 150));
	GRect robi = new GRect();

	public Exercice1_0() {
		space.addElement(robi);
		space.open();
		int width = space.getWidth();;
		int height = space.getHeight();;
		
		while(true) {
			//width = space.getWidth();
			// Passage du coin haut gauche à droit 
			while (robi.getX() < width-robi.getWidth() ) {
				Tools.sleep(5);
				width = space.getWidth();
				height = space.getHeight();
				//Réglage de la position du robi
				robi.setX(robi.getX()+1);
				robi.setY(0);
				/* Changement de couleur avec une couleur aléatoire */
				robi.setColor(new Color((int) (Math.random() * 0x1000000)));
			}
			
			
			//height = space.getHeight();
			
			// Passage du coin haut droit à bas droit
			
			while(robi.getY() < height-robi.getHeight()) {
				Tools.sleep(5);
				width = space.getWidth();
				height = space.getHeight();
				robi.setY(robi.getY()+1);
				robi.setX(width-robi.getWidth());
				/* Changement de couleur avec une couleur aléatoire */
				robi.setColor(new Color((int) (Math.random() * 0x1000000)));
			}
			
			//Passage du bas droit à bas gauche
			
			while(robi.getX() > 0) {
				Tools.sleep(5);
				width = space.getWidth();
				height = space.getHeight();width = space.getWidth();
				robi.setX(robi.getX()-1);
				robi.setY(height-robi.getHeight());
				robi.setColor(new Color((int) (Math.random() * 0x1000000)));
			}
			
			//Passage du bas gauche à haut droit
			
			while(robi.getY() > 0) {
				Tools.sleep(5);
				width = space.getWidth();
				height = space.getHeight();
				robi.setY(robi.getY()-1);
				robi.setX(0);
				robi.setColor(new Color((int) (Math.random() * 0x1000000)));
			}
			
		}
		
	}

	public static void main(String[] args) {
		new Exercice1_0();
	}

}
