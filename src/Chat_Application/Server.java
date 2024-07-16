package Chat_Application;
import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.util.*;
import java.text.*;
import java.net.*;

public class Server implements ActionListener{
	
	JTextField text; //TextField, globally declared so that actionPerformed function can access TextField
	static JPanel a1;
	static JFrame f = new JFrame();
	static Box vertical = Box.createVerticalBox();
	static DataOutputStream dout;
	
	Server(){
		
		f.setLayout(null); //needed for custom location of panel (in our case p1)
		
		JPanel p1 = new JPanel();
		p1.setBackground(new Color(0,101,169));
		p1.setBounds(0,0,450, 70); //provides location of panel
		p1.setLayout(null);
		f.add(p1);
				
		ImageIcon i1 = new ImageIcon(ClassLoader.getSystemResource("icons/arrow.png")); //arrow image
		Image i2 = i1.getImage().getScaledInstance(15, 25, Image.SCALE_DEFAULT);
		ImageIcon i3 = new ImageIcon(i2);
		JLabel back = new JLabel(i3); //JLabel cannot accept Image. It only accepts ImageIcon as parameter
		back.setBounds(5, 22, 25, 25);
		p1.add(back);
		back.addMouseListener( //mouse click action on arrow image
			new MouseAdapter(){
				public void mouseClicked(MouseEvent ae) {				
					System.exit(0);
				}
			}
			);
		
		ImageIcon i4 = new ImageIcon(ClassLoader.getSystemResource("icons/server.png")); //profile image
		Image i5 = i4.getImage().getScaledInstance(54, 40, Image.SCALE_DEFAULT);
		ImageIcon i6 = new ImageIcon(i5);
		JLabel profile = new JLabel(i6); //JLabel cannot accept Image. It only accepts ImageIcon as parameter
		profile.setBounds(40, 7, 55, 55);
		p1.add(profile);
		
		JLabel name = new JLabel("Server"); //name
		name.setFont(new Font("Arial", Font.PLAIN, 20));
		name.setBounds(110, 14, 80, 30);
		name.setForeground(Color.WHITE);
		p1.add(name);
		
		JLabel status = new JLabel("Online"); //status
		status.setFont(new Font("Arial", Font.PLAIN, 14));
		status.setBounds(110, 18, 80, 60);
		status.setForeground(Color.WHITE);
		p1.add(status);

		text = new JTextField(); //instantiate Textfield
		text.setBounds(5, 620, 350, 40); //TextField
		text.setBackground(new Color(51,51,51));
		text.setForeground(Color.WHITE);
		text.setFont(new Font("SAN_SERIF", Font.PLAIN, 15));
		f.add(text);
		
		JButton send = new JButton("Send"); //Send Button
		send.setBounds(365, 620, 74, 40);
		send.setBackground(new Color(0,101,169));
		send.setForeground(Color.WHITE);
		send.addActionListener(this);
		f.add(send);
		
		a1 = new JPanel(); //instantiate dark background panel
		a1.setBackground(new Color(37,37,38));
		a1.setBounds(0, 75, 450, 630);
		f.add(a1);

		f.setSize(460, 700); //frame size, Due to some error the screen produced is 450x700 only
		f.setLocation(200, 50); //frame location
		f.getContentPane().setBackground(new Color(37,37,38)); //color class is from awt package
		f.setVisible(true); //by default frame is hidden
	}
	
	public void actionPerformed(ActionEvent ae) { //runs on clicking send button
		
		try {			
			String out = text.getText();
			JPanel p2 = formatLabel(out); //new panel for sent message 
			
			a1.setLayout(new BorderLayout());
			
			JPanel right = new JPanel(new BorderLayout());
			right .setBackground(new Color(37,37,38));
			right.add(p2, BorderLayout.LINE_END); //right side align
			vertical.add(right); //creates a vertical box
			vertical.add(Box.createVerticalStrut(15)); //for multiple vertical boxes
			
			a1.add(vertical, BorderLayout.PAGE_START);
			
			dout.writeUTF(out);
			text.setText("");
			
			f.repaint(); // these three functions needed to refresh the GUI
			f.invalidate();
			f.validate();
		}catch(Exception e){
			e.printStackTrace();
		}
        
	}
	
	public static JPanel formatLabel(String out) { //to format sent message panel before adding the panel to vertical box
		JPanel panel = new JPanel();
		panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));	
		panel.setBackground(new Color(37,37,38));
	
		JLabel output = new JLabel("<html><p style=\"width:150px;\">" + out + "</p></html>"); // \ is used to ignore "
		output.setFont(new Font("Arial", Font.PLAIN, 16));
		output.setBackground(new Color(0,101,169));
		output.setForeground(Color.WHITE);
		output.setOpaque(true);
		output.setBorder(new EmptyBorder(15, 15, 15, 50)); //padding around sent message
		panel.add(output);
		
		Calendar cal = Calendar.getInstance(); //to add time
		SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
		JLabel time = new JLabel();
		time.setText(sdf.format(cal.getTime()));
		time.setForeground(Color.WHITE);
		panel.add(time);
		
		return panel;
	}
	
	public static void main(String[] args) {		
		new Server();
		
		try {
			ServerSocket skt = new ServerSocket(6001);
			while(true) {
				Socket s = skt.accept();
				DataInputStream din = new DataInputStream(s.getInputStream()); 
				dout = new DataOutputStream(s.getOutputStream()); //already declared at start
				
				while(true) {
					a1.setLayout(new BorderLayout());
					String msg = din.readUTF();
					JPanel panel = formatLabel(msg);
					JPanel left = new JPanel(new BorderLayout());
					left.setBackground(new Color(37,37,38));
			        left.add(panel, BorderLayout.LINE_START); //right side align
			        vertical.add(left); //creates a vertical box
			        vertical.add(Box.createVerticalStrut(15)); //for multiple vertical boxes
					a1.add(vertical, BorderLayout.PAGE_START);

			        f.validate(); //refresh page 
			        					
				}
				
			}
			
		}catch(Exception e) {
			e.printStackTrace();
		}
		
		System.out.println("Hello World!");
	}
}
