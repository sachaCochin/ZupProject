package gui;


import java.awt.BorderLayout;
import java.awt.GraphicsConfiguration;
import java.awt.HeadlessException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;

import compresseur.Compresseur;

import decompresseur.Decompresseur;

public class Gui extends JFrame {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private JProgressBar bar;
	private JButton chooseButton;
	
	public Gui() throws HeadlessException {
		// TODO Auto-generated constructor stub
	}

	public Gui(GraphicsConfiguration gc) {
		super(gc);
		// TODO Auto-generated constructor stub
	}

	public Gui(String title) throws HeadlessException {
		super(title);
		// TODO Auto-generated constructor stub
	}

	public Gui(String title, GraphicsConfiguration gc) {
		super(title, gc);
		// TODO Auto-generated constructor stub
	}
	
	
	public void buildUi() throws IOException	{
		JLabel image = new JLabel(new ImageIcon(this.getClass().getResource("bann.jpg")));	
		ImageIcon logo = new ImageIcon(this.getClass().getResource("logo.jpg"));
		setIconImage(logo.getImage());
		
		chooseButton = new JButton("Choose a file");
		chooseButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e)	{
				JFileChooser fChooser = new JFileChooser();
				int res = fChooser.showOpenDialog(null);
				if(res == JFileChooser.APPROVE_OPTION)	{
					File file = fChooser.getSelectedFile();
					if(file.getName().contains(".zup"))	{
						try {
							Decompresseur.decode(file.getPath().toString());
							FinishPop pop = new FinishPop("Op�ration termin�e avec succes");
							pop.setVisible(true);
						} catch (IOException e1) {
							System.out.println("erreur");
						}
					}
					else{
						try {
							long taille = new File(file.getPath().toString()).length();
							if(taille == 0){
								FinishPop pop = new FinishPop("erreur");
								pop.setVisible(true);								
							}
							else{
							Compresseur.code(file.getPath().toString());
							FinishPop pop = new FinishPop("Op�ration termin�e avec succes");
							pop.setVisible(true);
							}
						} catch (IOException e1) {
							System.out.println("erreur");
						}
					}
				}
			}
			
		});
		bar = new JProgressBar();
		
		JPanel center = new JPanel();
		center.setBorder(BorderFactory.createDashedBorder(null));
		
		center.add(chooseButton);
		
		this.add(image, BorderLayout.PAGE_START);
		this.add(center, BorderLayout.CENTER);
		this.add(bar, BorderLayout.PAGE_END);
	}
	
	public static void main(String[] args) throws IOException {

		Gui ui = new Gui("Zup");
		ui.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		ui.buildUi();
		ui.setSize(400,300);
		ui.setResizable(false);
		ui.setVisible(true);
		ui.setLocationRelativeTo(null);
	}

}
