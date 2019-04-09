/*
 * Copyright (c) 2019 Carlos Roldan Torregrosa
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package com.carlos.lnsim.lnsim;

import java.awt.Color;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

public class PlotResults extends JFrame {
	private static final long serialVersionUID = 6294689542092367723L;

	public PlotResults(String title) {
		super(title);

		// Create dataset
		XYDataset dataset = createDataset();

		// Create chart
		JFreeChart chart = ChartFactory.createScatterPlot("Boys VS Girls weight comparison chart", "X-Axis", "Y-Axis", dataset, PlotOrientation.VERTICAL, true, true, false);

		//Changes background color
		XYPlot plot = (XYPlot)chart.getPlot();
		plot.setBackgroundPaint(new Color(255,228,196));


		// Create Panel
		ChartPanel panel = new ChartPanel(chart);
		setContentPane(panel);
	}

	private XYDataset createDataset() {
		XYSeriesCollection dataset = new XYSeriesCollection();

		//Boys (Age,weight) series
		XYSeries series1 = new XYSeries("Boys");
		series1.add(1, 72.9);
		series1.add(2, 81.6);
		series1.add(3, 88.9);
		series1.add(4, 96);
		series1.add(5, 102.1);
		series1.add(6, 108.5);
		series1.add(7, 113.9);
		series1.add(8, 119.3);
		series1.add(9, 123.8);
		series1.add(10, 124.4);

		dataset.addSeries(series1);

		//Girls (Age,weight) series
		XYSeries series2 = new XYSeries("Girls");
		series2.add(1, 72.5);
		series2.add(2, 80.1);
		series2.add(3, 87.2);
		series2.add(4, 94.5);
		series2.add(5, 101.4);
		series2.add(6, 107.4);
		series2.add(7, 112.8);
		series2.add(8, 118.2);
		series2.add(9, 122.9);
		series2.add(10, 123.4);

		dataset.addSeries(series2);

		return dataset;
	}

	public static void main(String[] args) {
		SwingUtilities.invokeLater(() -> {
			PlotResults example = new PlotResults("Scatter Chart Example");
			example.setSize(800, 400);
			example.setLocationRelativeTo(null);
			example.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
			example.setVisible(true);
		});
	}
}
