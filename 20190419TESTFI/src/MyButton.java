import java.awt.Color;
import java.awt.Graphics;

import javax.swing.JButton;

@SuppressWarnings("serial")
public class MyButton extends JButton{
	public int state = 10;
	
	public MyButton(String text) {
		super(text);
		decorate();
	}
	
	protected void decorate() {
		setBorderPainted(false);
		setOpaque(false);
	}
	
	public void paint(Graphics g) {
		int width = getWidth();
		int height = getHeight();
		
		if(state!=10)
			g.setColor(Color.green);
		else
			g.setColor(Color.blue);
		
		g.fillRoundRect(0, 0, width, height, 50, 50);
		
		g.setColor(Color.white);
		g.drawString(getText(), (int)getSize().getWidth()/2-4, (int)getSize().getWidth()/2);
	}
}
