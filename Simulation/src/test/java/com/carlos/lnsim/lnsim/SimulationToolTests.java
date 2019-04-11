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

import static org.junit.Assert.*;

public class SimulationToolTests {

	@Test
	public void createNetworkMap() {
		NetwrokMapGenerator network = new NetwrokMapGenerator(40, 3);
		System.out.println("Creating network map using the network data model syntax");
		network.createNetwork();
		System.out.println("Network Map created");
		assertEquals(40, network.getNodeSize());
		assertEquals(3, network.getChannelsPerNode());
	}

	@Test
	public void LoadNetworkSimulation() {
		System.out.println("Creating network map using the network data model syntax");
		NetwrokMapGenerator network = new NetwrokMapGenerator(40, 3);
		System.out.println("Network Map created");
		network.createNetwork();
		System.out.println("Loading network map into the simulation");
		Load load = new Load();
		System.out.println("Network loaded into the simulation");
		assertEquals(40, load.getNodes().size());
		assertEquals(3*40, load.getChannels().size());
	}

	@Test
	public void DisplayNetwork() {
		//TODO
	}

	@Test
	public void routeTransactions() {
		//TODO
	}
}