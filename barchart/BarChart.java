import java.awt.*;
import java.util.Vector;

@SuppressWarnings("serial")
class BarChart extends Panel
{			   
	private int	barWidth = 20;

	private Vector<Integer>	data;
	private Vector<String>	dataLabels;
	private Vector<Color>	dataColors;

       /**
       SER515 #1: Design By Contract - what is the assumed precondition
       of the Vectors in use in the for loop? Add a check to avoid any errors.
       Design an approach to gracefully handle any errors
       */

		 
	public void paint(Graphics g)
	{

			// === SER515 #1 FIX: Defensive checks ===
			if (data == null || dataLabels == null || dataColors == null) {
			g.setColor(Color.RED);
			g.drawString("Error: Data is not initialized.", 20, 100);
			return;
		}

		int size = data.size();
		if (dataLabels.size() != size || dataColors.size() != size) {
			g.setColor(Color.RED);
			g.drawString("Error: Data size doesnt match", 20, 100);
			return;
	   }
	   // ========================================
		setSize(200,250);
		Image duke = Toolkit.getDefaultToolkit().getImage("duke2.gif");
		g.drawImage(duke, 80, 10, this);

		for (int i = 0; i < data.size(); i++)
		{				  
			int yposition = 100+i*barWidth;

			g.setColor(dataColors.elementAt(i));

			int barLength = (data.elementAt(i)).intValue();
			g.fillOval(100, yposition, barLength, barWidth);

			g.setColor(Color.black);
			g.drawString(dataLabels.elementAt(i), 20, yposition+10);
		}
	}

	public void setData(Vector<Integer> dataValues)
	{
		data = dataValues;
	}

	public void setLabels(Vector<String> labels)
	{
		dataLabels = labels;
	}

	public void setColors(Vector<Color> colors)
	{
		dataColors = colors;
	}
}
