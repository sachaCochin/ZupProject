package gui;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class FinishPop extends JFrame {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public FinishPop(String etat)	{
		
		super(etat);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		
		JPanel pane = new JPanel();
		pane.setLayout(new BoxLayout(pane, BoxLayout.PAGE_AXIS));
		
		JLabel lab = new JLabel(etat);
		JButton ok = new JButton("Ok");
		ok.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e)	{
				setVisible(false);
			}
		});
		
		pane.add(lab);
		pane.add(Box.createRigidArea(new Dimension(10,30)));
		pane.add(ok);
		
		lab.setAlignmentX(Component.CENTER_ALIGNMENT);
		ok.setAlignmentX(Component.CENTER_ALIGNMENT);
		setContentPane(pane);
		setSize(400, 120);
		setLocationRelativeTo(null);
	}
}
