import java.awt.*;
import java.awt.image.BufferedImage;
import javax.swing.JPanel;

public class Wykres extends JPanel {
	private static final long serialVersionUID = 7641921383813523039L;
	private Image img;
	private int width;
	private int height;
	
	public Wykres(int x, int y) {
		width = x;
		height = y;
		img = new BufferedImage(x, y, BufferedImage.TYPE_INT_RGB);
		clear();
	}
	
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		g.drawImage(img, 0, 0, null);
	}
	
	private void przesun() {
		Graphics g = img.getGraphics();
		g.copyArea(1, 1, width, height, -1, 0);
		g.setColor(Color.BLACK);
		g.fillRect(width-1, 0, 1, height);
		repaint();
	}
	
	public int getWidth() {
		return width;
	}
	
	public int getHeight() {
		return height;
	}
	
	public void putNext(int val) {
		Graphics g = img.getGraphics();
		przesun();
		g.setColor(Color.GREEN);
		g.fillRect(width-1, height-(int)(val/2), 1, 1);
		repaint();
	}
	
	private void clear() {
		Graphics g = img.getGraphics();
		g.setColor(Color.BLACK);
		g.fillRect(0, 0, width, height);
	}
}