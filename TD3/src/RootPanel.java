
import java.awt.Color;
import java.awt.Menu;
import java.awt.MenuBar;
import java.awt.MenuItem;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JFrame;
import javax.swing.JSplitPane;


public class RootPanel extends JFrame implements ActionListener {
	private static final long serialVersionUID = 1L;
	
	Polynome polynome;
	Graph graph;
	JSplitPane sp;
	MenuItem close, pol1, pol2, pol3;
	
	public RootPanel() {
		super("Devoir Maison #4");
    	addWindowListener(new Fermeur());
    	
    	graph = new Graph(this);
    	polynome = new Polynome(this);
    	
    	menuMaker();
    	splitPaneMaker();

    	//setSize(1000, 900);
    	//setUndecorated(true);
    	setExtendedState(JFrame.MAXIMIZED_BOTH); 
    	setVisible(true);
	}
	

	//Creer le menu
	private void menuMaker() {
    	Menu Menu = new Menu("Menu");
    	Menu.add(pol1 = new MenuItem("Polynome 1"));
    	pol1.addActionListener(this);
    	Menu.add(pol2 = new MenuItem("Polynome 2"));
    	pol2.addActionListener(this);
    	Menu.add(pol3 = new MenuItem("Polynome 3"));
    	pol3.addActionListener(this);
    	Menu.add(close = new MenuItem("Fermer"));
    	close.addActionListener(this);
    	
    	MenuBar bMenu = new MenuBar();
    	bMenu.add(Menu);
    	setMenuBar(bMenu);
	}


	//Creer les splitpanes
	private void splitPaneMaker() {    	
    	sp = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, polynome, graph);
    	sp.setContinuousLayout(true);
    	sp.setDividerSize(10);
    	sp.setDividerLocation(210 + sp.getInsets().left);
    	add(sp);
	}
	
	//Mise a jour du graph
	public void updateGraph() {
		graph.repaint();
	}

	//Getters pour le sous-panel polynome
	public int[] getBornes() {
		return polynome.getBornes();
	}

	public int[] getTicks() {
		return polynome.getTicks();
	}
	
	public int[] getCoeffs() {
		return polynome.getCoeffs();
	}
	
	public float getY(float x) {
		return polynome.result(x);
	}
	
	public String getStyle() {
		return polynome.getStyle();
	}
	
	public Color getColor() {
		return polynome.getColor();
	}
	
	//Bouttons du menu
	public void actionPerformed(ActionEvent e) {
		if(e.getSource() == close)
			System.exit(0);
		
		else if(e.getSource() == pol1) {
			int[] coeffs = {0, 1};
			polynome.setCoeffs(coeffs);
		}

		else if(e.getSource() == pol2) {
			int[] coeffs = {0, 0, 1};
			polynome.setCoeffs(coeffs);
		}

		else if(e.getSource() == pol3) {
			int[] coeffs = {0, 0, 0, 1};
			polynome.setCoeffs(coeffs);
		}
	}
}


class Fermeur extends WindowAdapter {
    public void windowClosing(WindowEvent e) { 
        System.exit(0);
    }
}