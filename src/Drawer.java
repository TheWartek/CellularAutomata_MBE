import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.concurrent.TimeUnit;

public class Drawer extends JPanel {
	private static final long serialVersionUID = -8615142381790367789L;
	private int size;         //rozmiar panelu
	private int drawsize;     //iloœæ komórek do odrysowania
	private Image img;
	private Automat a;
	private Main m;
	private boolean animate = false;
	private int speed = 50;

	public Drawer(int size, Automat atm, Main m) {
		this.m = m;
		this.size = size;
		img = new BufferedImage(size, size, BufferedImage.TYPE_INT_RGB);
		a = atm;
		drawsize = a.getSize();
		repaint();
	}
	
	public void drawSize(int dsize) { //zmiana zakresu rysowania
		drawsize = dsize;
		repaint();
	}
	
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		int rectsize = size/drawsize;
		
		Graphics g2 = img.getGraphics();
		
		g2.setColor(Color.WHITE);
		g2.fillRect(0, 0, size, size);
		g2.setColor(Color.RED);
		
		for(int y = 0; y < drawsize; ++y) {
			for(int x = 0; x < drawsize; ++x) {
				if(a.get(x, y) == 1) {
					g2.fillRect(x*rectsize, y*rectsize, rectsize, rectsize);
					if(rectsize > 2) {
						g2.setColor(Color.BLACK);
						g2.drawRect(x*rectsize, y*rectsize, rectsize, rectsize);
					}
					g2.setColor(Color.RED);
				}
				if(a.get2(x, y) == 1) {
					g2.setColor(Color.GRAY);
					g2.fillRect(x*rectsize, y*rectsize, rectsize, rectsize);
					if(rectsize > 2) {
						g2.setColor(Color.BLACK);
						//g2.drawRect(x*rectsize, y*rectsize, rectsize, rectsize);
						//obramowanie
						if(y > 0 && a.get2(x, y-1) == 0) g2.drawLine(x*rectsize, y*rectsize, x*rectsize+rectsize-1, y*rectsize);
						if(x > 0 && a.get2(x-1, y) == 0) g2.drawLine(x*rectsize, y*rectsize, x*rectsize, y*rectsize+rectsize-1);
						if(x < drawsize && a.get2(x+1, y) == 0) g2.drawLine(x*rectsize+rectsize, y*rectsize, x*rectsize+rectsize, y*rectsize+rectsize-1);
					}
					g2.setColor(Color.RED);
				}
			}
		}
		g.drawImage(img, 0, 0, null);
		showStatistics();
	}
	
	private void showStatistics() {
		m.jl1.setText("l. cz¹stek: " + a.particles());
		int percent = (a.particles()*100)/(a.getSize()*a.getSize());
		m.jl2.setText("Cz¹stek (%): " + percent + "%");
		m.w1.putNext(percent);
		m.jl3.setText("Krok: " + a.steps);
//		percent = (a.spinsDown()*100)/(a.getSize()*a.getSize());
//		m.jl4.setText("Spins DOWN (%): " + percent + "%");
//		m.w2.putNext(percent);
	}
	
	public void step() {
		a.step();
		repaint();
	}
	
	public void anim() {
		while(animate) {
			try {
				TimeUnit.MILLISECONDS.sleep(speed);
				a.step();
				repaint();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	
	public void stopAnimation() {
		animate = false;
	}
	
	public void startAnimation() {
		animate = true;
	}
	
	public void setSpeed(int newSpeed) {
		speed = newSpeed;
	}
}