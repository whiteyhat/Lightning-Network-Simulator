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
import org.jfree.ui.ApplicationFrame;
import org.jfree.ui.RefineryUtilities;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.DefaultCategoryDataset;

public class LinearResults extends ApplicationFrame {
	public LinearResults(String title, String chartTitle) {
		super(title);
		JFreeChart lineChart = ChartFactory.createLineChart(
				chartTitle,
				"Seconds","Transactions",
				createDataset(),
				PlotOrientation.VERTICAL,
				true,true,false);

		ChartPanel chartPanel = new ChartPanel( lineChart );
		chartPanel.setPreferredSize( new java.awt.Dimension( 560 , 367 ) );
		setContentPane( chartPanel );
	}

	private DefaultCategoryDataset createDataset( ) {
		DefaultCategoryDataset dataset = new DefaultCategoryDataset( );
		dataset.addValue( 15 , "AMP" , "10" );
		dataset.addValue( 30 , "AMP" , "20" );
		dataset.addValue( 60 , "AMP" ,  "30" );
		dataset.addValue( 120 , "AMP" , "40" );
		dataset.addValue( 240 , "AMP" , "50" );
		dataset.addValue( 300 , "AMP" , "60" );

		dataset.addValue( 50 , "Ant Path Routing" , "10" );
		dataset.addValue( 100 , "Ant Path Routing" , "20" );
		dataset.addValue( 124 , "Ant Path Routing" ,  "30" );
		dataset.addValue( 300 , "Ant Path Routing" , "40" );
		dataset.addValue( 540 , "Ant Path Routing" , "50" );
		dataset.addValue( 901 , "Ant Path Routing" , "60" );

		dataset.addValue( 30 , "BGW" , "10" );
		dataset.addValue( 60 , "BGW" , "20" );
		dataset.addValue( 80 , "BGW" ,  "30" );
		dataset.addValue( 111 , "BGW" , "40" );
		dataset.addValue( 150 , "BGW" , "50" );
		dataset.addValue( 204 , "BGW" , "60" );
		return dataset;
	}

	public static void main( String[ ] args ) {
		LinearResults chart = new LinearResults(
				"Lightning Network Simulator" ,
				"Routing Analysis");

		chart.pack( );
		RefineryUtilities.centerFrameOnScreen( chart );
		chart.setVisible( true );
	}
}
