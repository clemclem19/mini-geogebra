import java.awt.*;
import java.awt.image.*;

import javax.swing.JPanel;

public class Graph extends JPanel {
	private static final long serialVersionUID = 1L;

	RootPanel parent;

	BufferedImage image;
	WritableRaster raster;
	ColorModel model;	
	Color c;
	
	Point axis;
	int width, height;
	float pointsParX, pointsParY;
	
	int xMin, xMax, yMin, yMax;
	int majorX, majorY, minorX, minorY;


	//Initialisation
	public Graph(RootPanel parent) {
		this.parent = parent;
		axis = new Point();
	}

	//Met a jour les parametres et lances les differentes fonctions graphiques
	public void paintComponent(Graphics g) {
		int[] bornes = parent.getBornes();
		xMin = bornes[0];
		xMax = bornes[1];
		yMin = bornes[2];
		yMax = bornes[3];

		int[] spacing = parent.getTicks();
		majorX = spacing[0];
		majorY = spacing[1];
		minorX = spacing[2];
		minorY = spacing[3];

		c = parent.getColor();
		
		width = getWidth();
		height = getHeight();

		pointsParX = width/(abs(xMin-xMax));
		pointsParY = height/(abs(yMin-yMax));

		axis.setLocation(
				width*abs(xMin)/(float)(xMax+abs(xMin)), 
				height*abs(yMax)/(float)(yMax+abs(yMin)));

		image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		raster = image.getRaster();
		model = image.getColorModel();		
		
		String style = parent.getStyle();

		paintBackground();
		paintAxis();
		paintTicks();
				
		if(style.equals("lie"))
			paintCourbeLie();
		
		else if(style.equals("points"))
			paintCourbePoints();
		
		else if(style.equals("croix"))
			paintCourbeCroix();

		g.drawImage(image, 0, 0, null);
	}

	//Dessine l'arriere-plan
	private void paintBackground() {
		for (int i = 0; i < width; i++)
			for (int j = 0; j < height; j++)
				paintPoint(i, j, Color.WHITE);
	}
	
	//Dessine une courbe par croix
	private void paintCourbeCroix() {
		float x, y;
		int i, j;
		int k, l;
		
		
		for(x=xMin; x<=xMax; x++) { //Une croix pour chaque x entier dans le repere
			y = parent.getY(x);
			
			if(y>yMin && y<yMax) { //Si le point est dans le repere
				i = convertX(x);
				j = convertY(y);
				
				for(k=i-2, l=j-2; l<=j+2; k++, l++)	// *       *
					paintPointInColor(k, l);		//   *   *
													//     *
				for(k=i+2, l=j-2; l<=j+2; k--, l++)	//   *   *
					paintPointInColor(k, l);		// *       *
			}
		}	
	}

	//Dessine une courbe par "points"
	private void paintCourbePoints() {
		float x, y;
		int i, j;
		
		for(x=xMin; x<=xMax; x++) {
			y = parent.getY(x);
			
			if(y>yMin && y<yMax) {
				i = convertX(x);
				j = convertY(y);
				
				//Utilisation d'un carre/losange pour simuler un point 
				//de taille superieur a un pixel car celui-ci est peu visible.
				paintPointInColor(i-2, j);
				paintPointInColor(i-1, j-1);				
				paintPointInColor(i-1, j);
				paintPointInColor(i-1, j+1);
				paintPointInColor(i, j-2);	//	    *
				paintPointInColor(i, j-1);	//	  * * *
				paintPointInColor(i, j);	//	* * * * *
				paintPointInColor(i, j+1);	//	  * * *
				paintPointInColor(i, j+2);	//	    *
				paintPointInColor(i+1, j-1);
				paintPointInColor(i+1, j);
				paintPointInColor(i+1, j+1);
				paintPointInColor(i+2, j);
			}
		}		
	}
	
	//Dessine une courbe liee
	private void paintCourbeLie() {
		float x, y, yPrec = parent.getY(0);
		int j=0, jPrec = (int) axis.getY();
		
		for(int i=(int) axis.getX()-1; i>0; i--) { //Dessine la courbe precedant l'origine
			x = (float) ((i-axis.getX())/(pointsParX));
			y = parent.getY(x);

			if(y>yMin && y<yMax) {
				j = convertY(y);
			
				paintPointInColor(i, j);
				
				if(j != jPrec-1 && j != jPrec+1) //Si il y a un espacement entre ce point et le dernier point dessine
					traceLink(i, j, i-1, jPrec); //on trace une ligne entre ces deux points
				
				jPrec = j;
			}
			
			else if(y > yMax && yPrec < yMax) //Trace la courbe jusqu'en haut si le point sort de l'affichage
				traceLink(i, 1, i-1, jPrec);
				
			else if(y < yMin && yPrec > yMin) //Trace la courbe jusqu'en bas si le point sort de l'affichage
				traceLink(i, height-1, i-1, jPrec);

			yPrec = y;
		}
		
		yPrec = parent.getY(0);
		jPrec = (int) axis.getY();
		
		for(int i=(int) axis.getX(); i<width; i++) { //Dessine la courbe suivant l'origine
			x = (float) ((i-axis.getX())/(pointsParX));
			y = parent.getY(x);

			if(y>yMin && y<yMax) {
				j = convertY(y);
			
				paintPointInColor(i, j);
				
				if(j != jPrec-1 && j != jPrec+1)
					traceLink(i, j, i-1, jPrec);
				
				jPrec = j;
			}
			
			else if(y > yMax && yPrec < yMax)
				traceLink(i, 1, i-1, jPrec);

			else if(y < yMin && yPrec > yMin)
				traceLink(i, height-1, i-1, jPrec);
				
			yPrec = y;
		}
	}

	private void paintTicks() {
		int i, j;
		
		//Trace les ticks MINEURS sur la partie INFERIEUR de l'axe des ABSCISSES
		for(int x=min(0, xMax); x>xMin; x-=minorX) {
			i = convertX(x);
			
			for(j=(int) axis.getY()-3; j<=axis.getY()+3; j++)
				paintPoint(i, j);
		}

		//Trace les ticks MAJEURS sur la partie INFERIEUR de l'axe des ABSCISSES
		for(int x=min(0, xMax); x>xMin; x-=majorX) {
			i = convertX(x);
			
			for(j=(int) axis.getY()-7; j<=axis.getY()+7; j++)
				paintPoint(i, j);
		}

		//Trace les ticks MINEURS sur la partie SUPERIEUR de l'axe des ABSCISSES
		for(int x=max(0, xMin); x<xMax; x+=minorX) {
			i = convertX(x);
			
			for(j=(int) axis.getY()-3; j<=axis.getY()+3; j++)
				paintPoint(i, j);
		}

		//Trace les ticks MAJEURS sur la partie SUPERIEUR de l'axe des ABSCISSES
		for(int x=max(0, xMin); x<xMax; x+=majorX) {
			i = convertX(x);
			
			for(j=(int) axis.getY()-7; j<=axis.getY()+7; j++)
				paintPoint(i, j);
		}
		
		
		
		//Trace les ticks MINEURS sur la partie INFERIEUR de l'axe des ORDONNEES
		for(int y=min(0, yMax); y>yMin; y-=minorY) {
			j = convertY(y);
			
			for(i=(int) axis.getX()-3; i<=axis.getX()+3; i++)
				paintPoint(i, j);
		}

		//Trace les ticks MAJEURS sur la partie INFERIEUR de l'axe des ORDONNEES
		for(int y=min(0, yMax); y>yMin; y-=majorY) {
			j = convertY(y);
			
			for(i=(int) axis.getX()-7; i<=axis.getX()+7; i++)
				paintPoint(i, j);
		}

		//Trace les ticks MINEURS sur la partie SUPERIEUR de l'axe des ORDONNEES
		for(int y=max(0, yMin); y<yMax; y+=minorY) {
			j = convertY(y);
			
			for(i=(int) axis.getX()-3; i<=axis.getX()+3; i++)
				paintPoint(i, j);
		}

		//Trace les ticks MAJEURS sur la partie SUPERIEUR de l'axe des ORDONNEES
		for(int y=max(0, yMin); y<yMax; y+=majorY) {
			j = convertY(y);
			
			for(i=(int) axis.getX()-7; i<=axis.getX()+7; i++)
				paintPoint(i, j);
		}
	}

	//Trace les axes
	private void paintAxis() {
		for(int i=1; i<width; i++)
			paintPoint(i, (int) axis.getY());

		for(int i=1; i<height; i++)
			paintPoint((int) axis.getX(), i);

	}
	
	//Trace une ligne entre deux points passes en parametres
	private void traceLink(int i1, int j1, int i0, int j0) {
		if(j1 < axis.getY()) {
			if(j1 > j0)
				for(int k=j0+1; k<j1; k++)
					paintPointInColor(i0, k);
	
			else
				for(int k=j1+1; k<j0; k++)
					paintPointInColor(i1, k);
		}
		
		else {
			if(j1 > j0)
				for(int k=j0+1; k<j1; k++)
					paintPointInColor(i1, k);
	
			else
				for(int k=j1+1; k<j0; k++)
					paintPointInColor(i0, k);
		}
	}

	//Dessine un point aux coordonnees passees en argument en noir
	private void paintPoint(int x, int y) {
		raster.setDataElements(x, y, model.getDataElements(Color.BLACK.getRGB(), null));		
	}

	//Dessine un point aux coordonnes passees en argument dans la couleur desiree
	private void paintPoint(int x, int y, Color c) {
		raster.setDataElements(x, y, model.getDataElements(c.getRGB(), null));		
	}

	//Dessine un point aux coordonnes passees en argument dans la couleur sauvegardee dans la classe
	private void paintPointInColor(int x, int y) {
		raster.setDataElements(x, y, model.getDataElements(c.getRGB(), null));		
	}

	//Converti un x d'un affichage artificiel a l'affichage du JPanel
	private int convertX(float x) {
		return (int) (axis.getX() + x*pointsParX);
	}

	//Converti un y d'un affichage artificiel a l'affichage du JPanel
	private int convertY(float y) {
		return (int) (axis.getY() - y*pointsParY);
	}
	
	//Renvoie la valeur absolue de l'argument
	private int abs(int i) {
		if(i<0)
			i *= -1;

		return i;
	}
	
	//Renvoie la valeur maximum de deux arguments
	private int max(int a, int b) {
		return a>b?a:b;
	}
	
	//Renvoie la valeur minimum de deux arguments
	private int min(int a, int b) {
		return a<b?a:b;
	}
}
