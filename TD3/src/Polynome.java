import java.awt.*;
import java.awt.event.*;

import javax.swing.*;
import javax.swing.border.*;
import javax.swing.event.*;

public class Polynome extends JPanel implements ChangeListener, ActionListener {
	private static final long serialVersionUID = 1L;
	
	RootPanel parent; 
	PanneauData model;
	
	CheckboxGroup rbGroup;
	Checkbox[] rb = new Checkbox[3];
	JButton valid, reset;
	JScrollPane scrollCoeffs, scrollTable;	
	JSlider degre, colorR, colorG, colorB;
	JTextField xMin, yMin, xMax, yMax;
	JTextField spacingMajorX, spacingMajorY, spacingMinorX, spacingMinorY;
	JTextField[] coeffs = new JTextField[101];
	
	public Polynome(RootPanel parent) {
		this.parent = parent;
		this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS)); 
		
		for(int i=0; i<=100; i++)
			coeffs[i] = new JTextField("0");
		
		this.add(sliderMaker());
		this.add(scrollCoeffs = new JScrollPane(coeffsMaker(degre.getValue())));
		this.add(scrollTable = tableMaker());
		this.add(boundsMaker());
		this.add(spacingMaker());
		this.add(courbeMaker());
		this.add(buttonMaker());
	}

	//Tableau de gestion du degre
	public JPanel sliderMaker() {
		JPanel panel = new JPanel();
		panel.setLayout(new FlowLayout());
		panel.setBorder(new TitledBorder(new EtchedBorder(), "Degre du polynome"));
	
		degre = new JSlider(0, 100, 2);
		degre.setMajorTickSpacing(25);
		degre.setMinorTickSpacing(5);
		degre.setPaintTicks(true);
		degre.setPaintLabels(true);
		degre.setPaintTrack(true);
		degre.setBorder(new EmptyBorder(5, 5, 5, 5));
		degre.addChangeListener(this);
		panel.add(degre);

		panel.setMaximumSize(new Dimension(panel.getMaximumSize().width, panel.getPreferredSize().height));
		
		return panel;
	}


	//Tableau de gestion des bornes
	public JPanel boundsMaker() {
		JPanel panel = new JPanel();
		panel.setLayout(new GridLayout(0, 2));
		panel.setBorder(new TitledBorder(new EtchedBorder(), "Bornes"));

		panel.add(new JLabel("X min"));
		panel.add(xMin = new JTextField("-10"));
		
		panel.add(new JLabel("X max"));
		panel.add(xMax = new JTextField("10"));
		
		panel.add(new JLabel("Y min"));
		panel.add(yMin = new JTextField("-10"));
				
		panel.add(new JLabel("Y max"));
		panel.add(yMax = new JTextField("10"));
		
		panel.setMaximumSize(new Dimension(panel.getMaximumSize().width, panel.getPreferredSize().height));
		
		return panel;
	}
	
	//Tableau de gestion des ticks
	public JPanel spacingMaker() {
		JPanel panel = new JPanel();
		panel.setLayout(new GridLayout(0, 2));
		panel.setBorder(new TitledBorder(new EtchedBorder(), "Espacements"));

		panel.add(new JLabel("X mineur"));
		panel.add(spacingMinorX = new JTextField("1"));
		
		panel.add(new JLabel("X majeur"));
		panel.add(spacingMajorX = new JTextField("5"));

		panel.add(new JLabel("Y mineur"));
		panel.add(spacingMinorY = new JTextField("1"));
		
		panel.add(new JLabel("Y majeur"));
		panel.add(spacingMajorY = new JTextField("5"));
				
		panel.setMaximumSize(new Dimension(panel.getMaximumSize().width, panel.getPreferredSize().height));
		
		return panel;
	}
	
	//Tableau de gestion de la courbe courante
	public JPanel courbeMaker() {
		JPanel panel = new JPanel();
		panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
		panel.setBorder(new TitledBorder(new EtchedBorder(), "Courbe"));


		JPanel rbPanel = new JPanel();
		rbPanel.setLayout(new FlowLayout());
		rbGroup = new CheckboxGroup();
		
		rb[0] = new Checkbox("Lie", rbGroup, true);
		rbPanel.add(rb[0]);
		
		rb[1] = new Checkbox("Points", rbGroup, false);
		rbPanel.add(rb[1]);
		
		rb[2] = new Checkbox("Croix", rbGroup, false);
		rbPanel.add(rb[2]);
		panel.add(rbPanel);

		colorR = new JSlider(0, 255, 0);
		colorR.setMajorTickSpacing(255/5);
		colorR.setPaintTicks(true);
		colorR.setPaintTrack(true);
		colorR.setBorder(new TitledBorder(new EmptyBorder(5, 5, 5, 5), "Rouge"));
		colorR.addChangeListener(this);
		panel.add(colorR);

		colorG = new JSlider(0, 255, 0);
		colorG.setMajorTickSpacing(255/5);
		colorG.setPaintTicks(true);
		colorG.setPaintTrack(true);
		colorG.setBorder(new TitledBorder(new EmptyBorder(5, 5, 5, 5), "Vert"));
		colorG.addChangeListener(this);
		panel.add(colorG);

		colorB = new JSlider(0, 255, 0);
		colorB.setMajorTickSpacing(255/5);
		colorB.setPaintTicks(true);
		colorB.setPaintTrack(true);
		colorB.setBorder(new TitledBorder(new EmptyBorder(5, 5, 5, 5), "Bleu"));
		colorB.addChangeListener(this);
		panel.add(colorB);
		
		panel.setMaximumSize(new Dimension(panel.getMaximumSize().width, panel.getPreferredSize().height));
		
		return panel;
	}
	
	//Tableau de gestion des boutons principaux
	public JPanel buttonMaker() {
		JPanel panel = new JPanel();
		panel.setLayout(new GridLayout(0,1)); 
		panel.setBorder(new TitledBorder(new EtchedBorder(), "Boutons"));

		panel.add(valid = new JButton("Valider"));
		valid.addActionListener(this);
		
		panel.add(reset = new JButton("Reinitialiser"));
		reset.addActionListener(this);
	
		panel.setMaximumSize(new Dimension(panel.getMaximumSize().width, panel.getPreferredSize().height));
		
		return panel;
	}
	
	//Tableau de gestion des coefficients
	public JPanel coeffsMaker(int deg) {
		String exp[] = {"\u2070", "\u00B9", "\u00B2", "\u00B3", "\u2074", "\u2075", "\u2076", "\u2077", "\u2078", "\u2079", "\u00B9\u2070"};
		JPanel panel = new JPanel();
		panel.setLayout(new GridLayout(0, 2));
		panel.setBorder(new TitledBorder(new EtchedBorder(), "Coefficients"));
		    
	    for(int i=0; i<=deg; i++) {
			panel.add(coeffs[i] = new JTextField(coeffs[i].getText()));
			
			coeffs[i].setHorizontalAlignment(SwingConstants.RIGHT);
			
			if(i/10 > 0)
				panel.add(new JLabel("x" + exp[i/10] + exp[i%10]));
				
			else if(i > 1)
				panel.add(new JLabel("x"+exp[i%10]));

			else if(i == 1)
				panel.add(new JLabel("x"));
			
			else
				panel.add(new JLabel(""));
		}
		
		return panel;
	}
	
	//Tableau
	public JScrollPane tableMaker() {
		JTable tableau = new JTable();
		model = new PanneauData(this);
		tableau.setModel(model);

		JScrollPane panel = new JScrollPane(tableau);
		panel.setBorder(new TitledBorder(new EtchedBorder(), "Tableau"));
		panel.setMaximumSize(new Dimension(panel.getMaximumSize().width, panel.getMinimumSize().height));
		
		return panel;
	}
	
	//Retourne le degree max du polynome
	public int getDegreMax() {
		for(int i=degre.getValue(); i>=0; i--)
			if(!(coeffs[i].getText().equals("0")))
				return i;
		
		return 0;
	}
	
	//Retourne un tableau contenant toutes les bornes
	public int[] getBornes() {
		int[] bornes = new int[4];
		
		bornes[0] = Integer.parseInt(xMin.getText());
		bornes[1] = Integer.parseInt(xMax.getText());
		bornes[2] = Integer.parseInt(yMin.getText());
		bornes[3] = Integer.parseInt(yMax.getText());
		
		return bornes;
	}
	
	//Retourne un tableau contenant les valeurs d'espacement
	public int[] getTicks() {
		int[] ticks = new int[4];
		
		ticks[0] = Integer.parseInt(spacingMajorX.getText());
		ticks[1] = Integer.parseInt(spacingMajorY.getText());
		ticks[2] = Integer.parseInt(spacingMinorX.getText());
		ticks[3] = Integer.parseInt(spacingMinorY.getText());
		
		return ticks;
	}
	
	//Retourne un tableau contenant les coefficients
	public int[] getCoeffs() {
		int[] coeffs = new int[getDegreMax()];
		
		for(int i=0; i<getDegreMax(); i++)
			coeffs[i] = Integer.parseInt(this.coeffs[i].getText());
		
		return coeffs;
	}
	
	//Retourne un string identifiant le style choisi
	public String getStyle() {
		String type = null;
		
		if(rb[0].getState())
			type = "lie";
		
		else if(rb[1].getState())
			type = "points";
			
		else if(rb[2].getState())
			type = "croix";
		
		return type;
	}
	
	//Retourne la couleur courante
	public Color getColor() {
		return new Color(colorR.getValue(), colorG.getValue(), colorB.getValue());
	}
	
	//Calcule le polynome pour un x donne
	public float result(float x) {
		float total = 0;

		for(int i=0; i<=getDegreMax(); i++)
			total += Integer.parseInt(coeffs[i].getText()) * pow(x, i);

		return total;
	}

	//Fonction puissance (value = x^exp)
	private float pow(float x, int exp) {
		float value = x;
		
		for(int i=1; i<exp; i++)
			value *= x;
		
		return value;
	}
	
	//Setter pour le polynome
	public void setCoeffs(int[] coeffs) {
		int i;
		
		for(i=0; i<coeffs.length; i++)
			this.coeffs[i].setText(""+coeffs[i]);

		for(; i<=100; i++)
			this.coeffs[i].setText("0");
		
		degre.setValue(coeffs.length-1);
	}
	
	//Evenements sliders
	@Override
	public void stateChanged(ChangeEvent e) {
		scrollCoeffs.setViewportView(coeffsMaker(degre.getValue()));
	}

	//Evenements bouttons
	@Override
	public void actionPerformed(ActionEvent e) {
		JButton tmp = (JButton) e.getSource();
		
		if(tmp == valid) {
			int xMinTemp = 0, xMaxTemp = 0;
			
			int xMin = Integer.parseInt(this.xMin.getText());
			int xMax = Integer.parseInt(this.xMax.getText());
			int yMin = Integer.parseInt(this.yMin.getText());
			int yMax = Integer.parseInt(this.yMax.getText());
			
			for(int i=0; i>=xMin; i--) //On cherche le premier x dans l'affichage
				if(result(i) >= yMin && result(i) <= yMax)
					xMinTemp = i;
				
			for(int i=0; i<=xMax; i++) //On cherche le dernier x dans l'affichage
				if(result(i) >= yMin && result(i) <= yMax)
					xMaxTemp = i;
			
			model.setBounds(xMinTemp, xMaxTemp);
			
			parent.updateGraph();
		}
		
		else if(tmp == reset) {
			degre.setValue(2);
			
			for(int i=0; i<=100; i++)
				coeffs[i].setText("0");
			
			xMin.setText("-10");
			yMin.setText("-10");
			xMax.setText("10");
			yMax.setText("10");
			
			spacingMajorX.setText("5");
			spacingMajorY.setText("5");
			spacingMinorX.setText("1");
			spacingMinorY.setText("1");
			
			rbGroup.setSelectedCheckbox(rb[0]);
		}
	}
	
}
