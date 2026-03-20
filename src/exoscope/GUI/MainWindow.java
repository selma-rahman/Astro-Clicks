package GUI;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class MainWindow {

	private JFrame window;
	
	public MainWindow() {
		window = new JFrame();
		window.setTitle("Welcome to Exoscope");
		window.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		window.setSize(800,500);
		window.setLocationRelativeTo(null);
		window.setLayout(new BorderLayout());
				
		JPanel panel1 = new JPanel();
		panel1.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 5));
		
		panel1.setBackground(Color.PINK);
				
		panel1.setPreferredSize(new Dimension(200,200));
		
		JLabel label = new JLabel("Begin");
		label.setForeground(Color.WHITE);
		label.setFont(new Font("Serif", Font.BOLD, 36));

		
		window.add(panel1, BorderLayout.SOUTH);
		panel1.add(label, BorderLayout.NORTH);
		
		JButton button2 = new JButton("Update Label Text");
		button2.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				label.setText("End");
			}
		});
		
		panel1.add(button2);
		
	}
	
	public void show() {
		window.setVisible(true);
	}
}
