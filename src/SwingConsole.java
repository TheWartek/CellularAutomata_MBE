import javax.swing.*;
import java.awt.*;

public class SwingConsole {
	public static void run(final JApplet a, final int width, final int height) {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				a.setLayout(new BorderLayout());
				a.setSize(width, height);
				a.setVisible(true);
			}
		});
	}
}