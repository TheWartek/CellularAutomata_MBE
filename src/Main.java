import javax.swing.*;
import javax.swing.border.*;
import javax.swing.event.*;
import java.awt.*;
import java.awt.event.*;
import java.util.concurrent.*;

class Task implements Runnable {
	private Drawer d;
	
	public Task(Drawer d) {
		this.d = d;
	}
	
	public void run() {
		try {
			d.anim();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
}

public class Main extends JApplet {
	private static final long serialVersionUID = 3206847208968227199L;
	private static final int size = 600;
	private JPanel opcje = new JPanel(new FlowLayout(FlowLayout.LEFT));
	private JSlider zoom = new JSlider(1, size-1, 10);
	private JSlider speed = new JSlider(0, 1000, 10);
	private Drawer d;
	private Automat a;
	public Wykres w1;
	private JButton b = new JButton("Krok");
	private JButton b2 = new JButton("Animacja");
	private JCheckBox jcb1 = new JCheckBox("Symuluj wszystko");
	private JComboBox list;
	public JLabel jl1 = new JLabel();
	public JLabel jl2 = new JLabel();
	public JLabel jl3 = new JLabel();
	public JLabel jl4 = new JLabel();
	private ExecutorService executor = Executors.newSingleThreadExecutor();
	
	public Main() {
		setLayout(new BorderLayout());
		
		zoom.setValue(99);
		a = new Automat(size);
		d = new Drawer(size-1, a, this);
		d.drawSize(size-1);
		
		a.changeSize(zoom.getValue());
		d.drawSize(zoom.getValue());
		
		jcb1.setToolTipText("Zaznaczenie powoduje, ¿e symulowana jest ca³oœæ. Komórki wielkoœci 1 piksela.");
		jcb1.setSelected(false);
		jcb1.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if(jcb1.isSelected()) {
					a.changeSize(size-1);
				} else {
					int val = zoom.getValue();
					a.changeSize(val);
				}
			}
		});
		
		//zoom.setValue(size-1);
		zoom.setMinorTickSpacing(25);
		zoom.setMajorTickSpacing(100);
		zoom.setPaintTicks(true);
		zoom.setPaintLabels(true);
		zoom.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				int val = ((JSlider)(e.getSource())).getValue();
				if(jcb1.isSelected()) {
					a.changeSize(size-1);
				} else {
					a.changeSize(val);
				}
				d.drawSize(val);
			}
		});
		speed.setValue(50);
		speed.setMajorTickSpacing(200);
		speed.setPaintLabels(true);
		speed.setPaintTicks(true);
		speed.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				int val = ((JSlider)(e.getSource())).getValue();
				d.setSpeed(val);
			}
		});
		
		JPanel border = new JPanel();
		border.setToolTipText("Zmienia rozdzielczoœæ automatu");
		border.setBorder(new TitledBorder("Skalowanie"));
		border.add(zoom);
		
		opcje.add(border);
		
		b.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				d.stopAnimation();
				d.step();
			}
		});
		
		b2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				d.startAnimation();
				Task task = new Task(d);
				executor.execute(task);
			}
		});
		
		JPanel przyciski = new JPanel(new GridLayout(2,1));

		przyciski.add(b);
		przyciski.add(b2);
		opcje.add(przyciski);
		
		border = new JPanel();
		border.setToolTipText("Szybkoœæ symulacji");
		border.setBorder(new TitledBorder("Szybkoœæ"));
		border.add(speed);
		
		opcje.add(border);
		
		String[] models = {"Model 1: MNP", "Model 2: (Family'ego)", "Model 3: (Wolfa-Villaina)", "Model 4: (Das Sarmy-Tamborenea)"};
		list = new JComboBox(models);
		list.setSelectedIndex(0);
		
		list.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				a.changeModel(list.getSelectedIndex());				
			}
		});
		
		JPanel inne = new JPanel(new GridLayout(2,1));
				
		inne.add(list);		
		inne.add(jcb1);
		
		opcje.add(inne);
		
		w1 = new Wykres(110, 50);
		border = new JPanel(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		
		border.setBorder(new TitledBorder("Statystyki"));
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 0;
		c.gridy = 0;
		border.add(jl1, c);
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 0;
		c.gridy = 1;
		border.add(jl2, c);
		c.fill = GridBagConstraints.HORIZONTAL;
		c.ipady = 40;
		c.gridx = 0;
		c.gridy = 2;
		border.add(w1, c);
		c.ipady = 0;
		c.weighty = 0.;
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 0;
		c.gridy = 3;
		border.add(jl3, c);
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 0;
		c.gridy = 4;
		c.ipady=100;
		border.add(jl4, c);
		c.ipady = 0;
		c.weighty = 0;
		c.gridy = 5;
		border.add(new JLabel("Autor:"), c);
		c.weighty = 0;
		c.gridy = 6;
		border.add(new JLabel("Bart³omiej Wojas"), c);

		add(BorderLayout.NORTH, opcje);
		add(BorderLayout.CENTER, d);
		add(BorderLayout.WEST, border);
	}
	
	public void init() {		
		try {
			//UIManager.setLookAndFeel("com.shfarr.ui.plaf.fh.FhLookAndFeel");
			UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
		} catch(Exception e) {
			e.printStackTrace();
		}
		
 		SwingUtilities.updateComponentTreeUI(this);
		SwingConsole.run(new Main(), size, size);
		resize(size+opcje.getWidth(), size+opcje.getHeight());
	}
}