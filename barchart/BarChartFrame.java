import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.Vector;
import java.util.Hashtable;

@SuppressWarnings("serial")
class BarChartFrame extends Frame
{
	Hashtable<String, Color> colorMap = new Hashtable<String, Color>();

	protected Vector<Integer>	data;
	protected Vector<String>	labels;
	protected Vector<Color>		colors;

	Choice 	colorSelect;
	TextField	labelSelect;
	TextField	dataSelect;

	BarChart chart;

	class BarChartFrameControl extends WindowAdapter implements ActionListener 
	{
		// SER515 #2: The line which adds an element to the colors vector
		// assumes the selected item is available in the colorMap. Is this always true?
		public void actionPerformed(ActionEvent e)
		{
			if (e.getSource() instanceof Button)
			{
				labels.addElement(labelSelect.getText());
				data.addElement(new Integer(dataSelect.getText()));
				
				// Get the selected color from colorSelect
				String selectedColor = colorSelect.getSelectedItem();
				Color color = colorMap.get(selectedColor);

				// Check if color exists in the colorMap
					if (color == null) {
						// Handle the case where color is not in the colorMap
						System.out.println("Warning: Selected color not available. Using default color (black).");
						color = Color.BLACK; // Use default color if not found
					}

				colors.addElement(color);


				chart.setData(data);
				chart.setColors(colors);
				chart.setLabels(labels);
				chart.repaint();
			}
			else if (e.getSource() instanceof Menu)
			{
				BarChartFrame.this.dispose();
				System.exit(0);		// only option at this time
			}
		}
	}

	public void initData(String fname) {
		data = new Vector<Integer>();
		labels = new Vector<String>();
		colors = new Vector<Color>();

		colorMap.put("red", Color.red);
		colorMap.put("green", Color.green);
		colorMap.put("blue", Color.blue);
		colorMap.put("magenta", Color.magenta);
		colorMap.put("gray", Color.gray);

		try (FileReader bridge = new FileReader(fname)) {
				StreamTokenizer streamTokens = new StreamTokenizer(bridge);
				streamTokens.eolIsSignificant(false);

				while (streamTokens.nextToken() != StreamTokenizer.TT_EOF) {
						try {
								if (streamTokens.ttype != StreamTokenizer.TT_NUMBER) {
										throw new IllegalArgumentException("Numeric value is expected");
								}
								int number = (int) streamTokens.nval;

								if (streamTokens.nextToken() != StreamTokenizer.TT_WORD || streamTokens.sval == null) {
										throw new IllegalArgumentException("Label is expected after a number");
								}
								String label = streamTokens.sval;

								if (streamTokens.nextToken() != StreamTokenizer.TT_WORD || streamTokens.sval == null) {
										throw new IllegalArgumentException("Color is expected after a label");
								}
								String colorName = streamTokens.sval.toLowerCase();
								Color color = colorMap.get(colorName);

								if (color == null) {
										System.err.println("Warning: Unknown color '" + colorName + "'. Changing to deafualt color blue ");
										color = Color.blue; // Default when color not found
								}

								data.addElement(number);
								labels.addElement(label);
								colors.addElement(color);
						} catch (IllegalArgumentException ex) {
								System.err.println("Skipping invalid entry: " + ex.getMessage());
						}
				}
		} catch (FileNotFoundException ex) {
				System.err.println("File not found: " + ex.getMessage());
		} catch (IOException ex) {
				System.err.println("Error reading file: " + ex.getMessage());
		}
}


	public BarChartFrame(String fname) {
		BarChartFrameControl control = new BarChartFrameControl();

		initData(fname);

		setSize(350,350);
		setLayout(new BorderLayout());

		MenuBar mb = new MenuBar();
		Menu file = new Menu("File");
		file.addActionListener(control);
		file.add("Exit");
		mb.add(file);
		setMenuBar(mb);

		chart = new BarChart();

		chart.setData(data);
		chart.setLabels(labels);
		chart.setColors(colors);

		Panel components = new Panel();
		components.setSize(350,50);
		components.setLayout(new FlowLayout());

		colorSelect = new Choice();
		colorSelect.add ("red");
		colorSelect.add("green");
		colorSelect.add("blue");
		colorSelect.add("magenta");
		colorSelect.add("gray");
		colorSelect.add("orange");
		components.add(colorSelect);
		labelSelect = new TextField("label", 10);
		components.add(labelSelect);
		dataSelect = new TextField("data", 5);
		components.add(dataSelect);

		Button button = new Button("Add Data");
		button.addActionListener(control);
		components.add(button);

		setBackground(Color.lightGray);
		add(components, "South");
		add(chart, "North");
		chart.repaint();
		setVisible(true);
	}
}
