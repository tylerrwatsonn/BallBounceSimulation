import java.awt.Color;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;

import acm.gui.TableLayout;

public class sliderBox {
	
	JPanel myPanel;
	JLabel nameLabel;
	JLabel minLabel;
	JLabel maxLabel;
	JSlider mySlider;
	JLabel sReadout;
	float value;
	int imin;
	int imax;
	
	
	public sliderBox(String name, int min, int dValue, int max) { // Slider Box Integer values
			myPanel = new JPanel();
			nameLabel = new JLabel(name);
			minLabel = new JLabel(min+"");
			maxLabel = new JLabel(max+"");
			mySlider = new JSlider((int)min,(int)max,(int)dValue);
			sReadout = new JLabel(dValue+"");
			sReadout.setForeground(Color.blue);
			myPanel.setLayout(new TableLayout(1,5));
			myPanel.add(nameLabel,"width=100");
			myPanel.add(minLabel,"width=25");
			myPanel.add(mySlider,"width=100");
			myPanel.add(maxLabel,"width=100");
			myPanel.add(sReadout,"width=80");
			imin=min;
			imax=max;
	}
	
	
	public sliderBox(String name, double min, double dValue, double max) { // Slider Box with double values
			myPanel = new JPanel();
			nameLabel = new JLabel(name);
			minLabel = new JLabel(min+"");
			maxLabel = new JLabel(max+"");
			mySlider = new JSlider((int)min*10,(int)max*10,(int)dValue*10); //values multiplied by 10 to display and handle floats
			sReadout = new JLabel(dValue+"");
			sReadout.setForeground(Color.blue);
			myPanel.setLayout(new TableLayout(1,5));
			myPanel.add(nameLabel,"width=100");
			myPanel.add(minLabel,"width=25");
			myPanel.add(mySlider,"width=100");
			myPanel.add(maxLabel,"width=100");
			myPanel.add(sReadout,"width=80");
			imin=(int) min;
			imax=(int) max;
			value = (float) dValue;
			}
	public sliderBox(String name, String color) { //Slider Box for Color
			myPanel = new JPanel();
			nameLabel = new JLabel(name);
			mySlider = new JSlider(0, 7);
			sReadout = new JLabel(color +"");
			sReadout.setForeground(Color.GREEN);
			myPanel.setLayout(new TableLayout(1,5));
			myPanel.add(nameLabel,"width=125");
			myPanel.add(mySlider,"width=100");
			myPanel.add(sReadout,"width=180");
	}
	
	public Integer getISlider() { //function to get slider value (int)
		return mySlider.getValue();
	}
	public void setISlider (int val) { //function to set slider value (int)
		mySlider.setValue(val);
		sReadout.setText(val+"");
	}
	
	public Float getFSlider() { //function to get slider value (float)
		return (float) (mySlider.getValue());
	}
	public void setFSlider(float val) { //function to set slider value (float)
		mySlider.setValue( (int) val);
		sReadout.setText(val/10.0+"");
	}
}

