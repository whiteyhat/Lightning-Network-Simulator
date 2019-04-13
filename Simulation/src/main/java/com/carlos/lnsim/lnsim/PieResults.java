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

import javax.swing.*;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.data.general.DefaultPieDataset;
import org.jfree.data.general.PieDataset;

public class PieResults extends JDialog {


	private double totalChannels, congestedChannels;

	public PieResults( String title, double totalChannels, double congestedChannels ) {
		setTitle(title);
		this.totalChannels = totalChannels;
		this.congestedChannels = congestedChannels;
		setContentPane(createDemoPanel( ));
		setDefaultCloseOperation(HIDE_ON_CLOSE);
	}

	private PieDataset createDataset() {
		DefaultPieDataset dataset = new DefaultPieDataset( );
		dataset.setValue( "Congested Channels" ,  totalChannels-congestedChannels );
		dataset.setValue( "Active Channels" , congestedChannels );
		return dataset;
	}

	private static JFreeChart createChart( PieDataset dataset ) {
		JFreeChart chart = ChartFactory.createPieChart(
				"Channels Analysis",   // chart title
				dataset,          // data
				true,             // include legend
				true,
				false);

		return chart;
	}

	public void setTotalChannels(double totalChannels) {
		this.totalChannels = totalChannels;
	}

	public void setCongestedChannels(double congestedChannels) {
		this.congestedChannels = congestedChannels;
	}

	public JPanel createDemoPanel() {
		JFreeChart chart = createChart(createDataset( ) );
		return new ChartPanel( chart );
	}
}