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

import org.junit.Test;
import javax.swing.*;
import java.awt.*;
import java.awt.event.InputEvent;
import static org.junit.Assert.*;

/**
 * Class to create the Unit Testing objects to test some key methods of the simulation tool from the Lightning Network Simulator
 */
public class SimulationToolTests {

	/**
	 * Unit Test to assert the network map creation using the network data model syntax according to the design
	 * spec at: https://github.com/whiteyhat/Lightning-Network-Simulator/wiki/Design
	 */
	@Test
	public void createNetworkMap() {
		NetworkMapGenerator network = new NetworkMapGenerator(40, 3);
		System.out.println("Creating network map using the network data model syntax");
		DataFetcher dataFetcher = new DataFetcher();
		network.createNetworkMap(dataFetcher);
		System.out.println("Network Map created");
		assertEquals(40, network.getNetworkSize());
		assertEquals(3, network.getChannelsPerNode());
	}

	/**
	 * Unit Test to assert the loading process from the network map to the simulation tool by making use of
	 * the data fetcher object according to the design spec at: https://github.com/whiteyhat/Lightning-Network-Simulator/wiki/Design
	 */
	@Test
	public void loadNetworkSimulation() {
		System.out.println("Creating network map using the network data model syntax");
		NetworkMapGenerator network = new NetworkMapGenerator(40, 3);
		System.out.println("Network Map created");
		DataFetcher dataFetcher = new DataFetcher();
		System.out.println("Network loaded into the simulation");
		network.createNetworkMap(dataFetcher);
		System.out.println("Loading network map into the simulation");
		assertEquals(40, dataFetcher.getNodes().size());
		assertEquals(3*40, dataFetcher.getChannels().size());
	}

	/**
	 * Unit Test to assert the network is displayed in the simulation by making use of loading a default network map
	 * according to the design spec at: https://github.com/whiteyhat/Lightning-Network-Simulator/wiki/Design
	 */
	@Test
	public void displayNetwork() {
		DataFetcher dataFetcher = null;
		System.out.println("Creating network map using the network data model syntax");
		NetworkMapGenerator network = new NetworkMapGenerator(40, 3, dataFetcher);
		System.out.println("Network Map created");
		dataFetcher = new DataFetcher();
		network.createNetworkMap(dataFetcher);
		System.out.println("Loading network map into the simulation");
		System.out.println("Network loaded into the simulation");

		System.out.println("Displaying simulation");

		GUI frame = new GUI();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
		frame.setUndecorated(true);
		frame.setVisible(true);

		Robot bot = null;
		try {
			bot = new Robot();
			bot.mouseMove(14,16);
			bot.mousePress(InputEvent.BUTTON1_MASK);
			//add time between press and release or the input event system may
			//not think it is a click
			try{Thread.sleep(250);}catch(InterruptedException e){}
			bot.mouseRelease(InputEvent.BUTTON1_MASK);

			bot.mouseMove(22,56);
			bot.mousePress(InputEvent.BUTTON1_MASK);
			//add time between press and release or the input event system may
			//not think it is a click
			try{Thread.sleep(250);}catch(InterruptedException e){}
			bot.mouseRelease(InputEvent.BUTTON1_MASK);
			Rectangle rectangle = new Rectangle();
			rectangle.setSize(700, 1000);
			bot.createScreenCapture(rectangle);

			bot.mouseMove(871,446);
			bot.mousePress(InputEvent.BUTTON1_MASK);
			//add time between press and release or the input event system may
			//not think it is a click
			try{Thread.sleep(250);}catch(InterruptedException e){}
			bot.mouseRelease(InputEvent.BUTTON1_MASK);

			bot.mouseMove(1061,657);
			bot.mousePress(InputEvent.BUTTON1_MASK);
			//add time between press and release or the input event system may
			//not think it is a click
			try{Thread.sleep(250);}catch(InterruptedException e){}
			bot.mouseRelease(InputEvent.BUTTON1_MASK);
		} catch (AWTException e) {
			e.printStackTrace();
		}

	}
}