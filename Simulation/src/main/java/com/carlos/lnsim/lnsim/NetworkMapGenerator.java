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

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import java.util.Random;
import java.io.FileWriter;
import java.io.IOException;

public class NetworkMapGenerator {
	private int nodeSize, channelsPerNode;
	private boolean simulationCompleted;
	private Load load;

	public NetworkMapGenerator(int nodeSize, int channelsPerNode, Load load) {
		this.nodeSize = nodeSize;
		this.channelsPerNode = channelsPerNode;
		this.load = load;
		createNetwork();
	}

	public NetworkMapGenerator(int nodeSize, int channelsPerNode) {
		this.nodeSize = nodeSize;
		this.channelsPerNode = channelsPerNode;
		createNetwork();
	}

	public NetworkMapGenerator() {
	}

	public void setNodeSize(int nodeSize) {
		this.nodeSize = nodeSize;
	}

	public void setChannelsPerNode(int channelsPerNode) {
		this.channelsPerNode = channelsPerNode;
	}

	public boolean isSimulationCompleted() {
		return simulationCompleted;
	}

	public void setSimulationCompleted(boolean simulationCompleted) {
		this.simulationCompleted = simulationCompleted;
	}

	public void createNetwork(){
		Random rand = new Random();
		rand.setSeed(System.currentTimeMillis());
//		TODO NetworkMapGenerator seed in config file to replicate scenario
		try {
			init(nodeSize, channelsPerNode, 2, load);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void setLoad(Load load) {
		this.load = load;
	}

	public int getNodeSize() {
		return nodeSize;
	}

	public int getChannelsPerNode() {
		return channelsPerNode;
	}

	private void init(int nodeSize, int channelsPerNode, int tx, Load load) throws IOException {
		JSONObject json = new JSONObject();
		JSONArray config = new JSONArray();
		for (int i = 0; i < nodeSize; i++) {
			JSONObject nodes = new JSONObject();
			JSONArray channelsArray = new JSONArray();
			Random rand = new Random();
			nodes.put("id",String.valueOf(i));
			nodes.put("alias",("Node" + (i)));
			nodes.put("balance" , String.valueOf(rand.nextInt(1000)));
			for (int j = 0; j < channelsPerNode; j++) {
				JSONObject channels = new JSONObject();
				JSONArray transactionArray = new JSONArray();
				Random random = new Random();
				channels.put("id",String.valueOf(j));
				channels.put("from",String.valueOf(i));
				int randomNumber = random.nextInt(nodeSize);
				boolean valid = false;
				do {
					if (randomNumber == i){
						randomNumber = random.nextInt(nodeSize);
					} else {
						valid = true;
					}
				}while (!valid);
				channels.put("to",String.valueOf(randomNumber));
				channels.put("capacity",String.valueOf(random.nextInt(100)));
				channels.put("fee", String.valueOf(random.nextInt(7)));
				for (int k = 0; k < tx ; k++) {
					JSONObject transaction = new JSONObject();
					transaction.put("secret", "");
					transaction.put("paymentRequest", "");
					transaction.put("tokens", String.valueOf(random.nextInt(10000)));
					transaction.put("createdAt", "");
					transaction.put("expiredAt", "");
					transactionArray.add(transaction);
				}
				channels.put("transactions" ,transactionArray);
				channelsArray.add(channels);
			}
			nodes.put("channels", channelsArray);
			config.add(nodes);
		}

		json.put("Nodes", config);


		if (simulationCompleted){
			int networkBalance = 0;
			int fees = 0;
			for (Node n : load.getNodes()) {
				networkBalance += n.getBalance();
			}

			for (Channel c : load.getChannels()) {
				fees += c.getFee();
			}

			JSONObject results = new JSONObject();
			JSONArray resultValues = new JSONArray();
			results.put("Transactions", String.valueOf(load.getTransactionAmount()));
			results.put("Failed Transactions", "");
			results.put("Hops", String.valueOf(load.getHops()));
			results.put("Fees", String.valueOf(fees));
			results.put("Channels", String.valueOf(load.getChannels().size()));
			results.put("Congested Channels", load.getCongestedChannels());
			results.put("Network Balance", String.valueOf(networkBalance));
			results.put("Network Size", String.valueOf(load.getNodes().size()));

			resultValues.add(results);

			json.put("Results", resultValues);
		}
		// try-with-resources statement based on post comment below :)
		try (FileWriter file = new FileWriter("src/main/resources/config/custom.json")) {
			file.write(json.toJSONString());
		}
	}
}
