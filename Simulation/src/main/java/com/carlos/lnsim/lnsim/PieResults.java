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

/**
 * Class to create an object entity to assist the creation of interactive charts for the analysis tool
 */
public class PieResults extends JDialog {

	private int positiveValue, negativeValue, mediumValue, type, lowchannel;

	/**
	 * Constructor to create a Pie Results object entity with all the required parameters and methods.
	 * @param type Analysis type
	 * @param positiveValue Main first value to compare
	 * @param negativeValue Main second value to compare
	 */
	public PieResults( int type, int positiveValue, int negativeValue) {
		setTitle("Lightning Network Simulator");
		this.positiveValue = positiveValue;
		this.negativeValue = negativeValue;
		this.type = type;
		setDefaultCloseOperation(HIDE_ON_CLOSE);
	}

	/**
	 * Method to set an alternative value to compare
	 * @param mediumValue Extra value to compare
	 */
	public void setMediumValue(int mediumValue) {
		this.mediumValue = mediumValue;
	}

	/**
	 * Method to set an alternative value to compare
	 * @param lowchannel Extra value to compare
	 */
	public void setLowchannel(int lowchannel) {
		this.lowchannel = lowchannel;
	}

	/**
	 * Method to create an interactive Pie Chart data set
	 * @return the dataset created
	 */
	private PieDataset createDataset() {
		DefaultPieDataset dataset = new DefaultPieDataset( );
		if (type == 1){
			int result = positiveValue - negativeValue;
			dataset.setValue( "Congested Channels" , negativeValue);
			dataset.setValue( "Active Channels" , result);
			return dataset;
		}if (type == 2){
			dataset.setValue( "Low Balance Nodes" , negativeValue);
			dataset.setValue( "Medium Balance Nodes" , mediumValue);
			dataset.setValue( "High Balance nodes" , positiveValue);
			return dataset;
		}if (type == 3){
			dataset.setValue( "Failed Transactions" , negativeValue);
			dataset.setValue( "Successful Transactions" , positiveValue);
			return dataset;
		}if (type == 4){
			dataset.setValue( "Routed Transactions" , negativeValue);
			dataset.setValue( "Direct Transactions" , positiveValue);
			return dataset;
		}if (type == 5){
			dataset.setValue( "Healthy Channels" , positiveValue);

			dataset.setValue( "Medium Balance Channels" , mediumValue);
			dataset.setValue( "Low Balance Channels" , lowchannel);
			dataset.setValue( "Congested Channels" , negativeValue);

		}
		return dataset;
	}

	/**
	 * Method to create the chart using the dataset
	 * @param dataset Dataset containing the information to represent in the chart
	 * @return the chart to be displayed
	 */
	private static JFreeChart createChart( PieDataset dataset ) {
		JFreeChart chart = ChartFactory.createPieChart(
				"Analysis",   // chart title
				dataset,          // data
				true,             // include legend
				true,
				false);

		return chart;
	}

	/**
	 * Method to initialize the chart container
	 */
	protected void init(){
		setContentPane(createDemoPanel( ));
	}

	/**
	 * Method to get the frame container that contains the interactive Pie Chart
	 * @return the frame container
	 */
	private JPanel createDemoPanel() {
		JFreeChart chart = createChart(createDataset( ) );
		return new ChartPanel( chart );
	}
}