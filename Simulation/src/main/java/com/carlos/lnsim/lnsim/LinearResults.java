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

import org.jfree.chart.ChartPanel;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.DefaultCategoryDataset;

import javax.swing.*;
import java.util.ArrayList;

public class LinearResults extends JDialog {
	ArrayList<Integer> nodes;

	public LinearResults(String title, String chartTitle, ArrayList<Integer> nodes) {
		setTitle(title);
		this.nodes = nodes;
		JFreeChart lineChart = ChartFactory
				.createLineChart(chartTitle, "Seconds", "Transactions", createDataset(), PlotOrientation.VERTICAL, true,
						true, false);

		ChartPanel chartPanel = new ChartPanel(lineChart);
		chartPanel.setPreferredSize(new java.awt.Dimension(560, 367));
		setContentPane(chartPanel);
	}

	private DefaultCategoryDataset createDataset() {
		DefaultCategoryDataset dataset = new DefaultCategoryDataset();

		int time = 0;
		for (Integer value : nodes) {
			time += 10;
			dataset.addValue(value, "Transactions", String.valueOf(time));
		}
		return dataset;
	}
}