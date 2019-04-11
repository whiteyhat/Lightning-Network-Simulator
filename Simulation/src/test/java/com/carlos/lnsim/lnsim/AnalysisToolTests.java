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

public class AnalysisToolTests {

	@Test public void ManageInputSimulations() {
		System.out.println("Creating network map using the network data model syntax");
		NetwrokMapGenerator network = new NetwrokMapGenerator(40, 3);
		System.out.println("Network Map created");
		network.createNetwork();
		System.out.println("Loading network map into the simulation");
		Load load = new Load();
		System.out.println("Network loaded into the simulation");

		System.out.println("Displaying simulation");
		GUI gui = new GUI(load);

		System.out.println("Testing balance inputs from simulation");
		gui.setBalances(3,3);
		for (Node n : load.getNodes()) {
			assertEquals(3, n.getBalance());
		}
		System.out.println("Balance inputs tested");

		System.out.println("Testing capacity inputs from simulation");
		gui.setCapacities(5,5);
		Double test = 5.0;
		for (Channel c : load.getChannels()) {
			assertEquals(test, c.getCapacity());
		}
		System.out.println("Capacity inputs tested");

	}

	@Test public void ExportSimulationResults() {
	}
}