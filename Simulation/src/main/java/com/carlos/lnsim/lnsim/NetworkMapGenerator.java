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

/**
 * Class to create an object entity essential for the network interoperability
 */
public class NetworkMapGenerator {
	private int networkSize, channelsPerNode;
	private boolean simulationCompleted;
	private DataFetcher dataFetcher;

	/**
	 * Constructor to create a Network Map Generator object entity with all the required parameters.
	 * @param networkSize Number of nodes to generate the network map
	 * @param channelsPerNode Number of channels per node to generate the network map
	 * @param dataFetcher DataFetcher object to fetch local data from the simulation
	 */
	public NetworkMapGenerator(int networkSize, int channelsPerNode, DataFetcher dataFetcher) {
		this.networkSize = networkSize;
		this.channelsPerNode = channelsPerNode;
		this.dataFetcher = dataFetcher;
		createNetworkMap();
	}

	/**
	 * Constructor to create a Network Map Generator object entity with some required parameters.
	 * @param networkSize Number of nodes to generate the network map
	 * @param channelsPerNode Number of channels per node to generate the network map
	 */
	public NetworkMapGenerator(int networkSize, int channelsPerNode) {
		this.networkSize = networkSize;
		this.channelsPerNode = channelsPerNode;
		createNetworkMap();
	}

	/**
	 * Constructor to create a Network Map Generator object entity without required parameters.
	 */
	public NetworkMapGenerator() {
	}

	/**
	 * Method to set the network size
	 * @param networkSize Number to set the new network size
	 */
	public void setNetworkSize(int networkSize) {
		this.networkSize = networkSize;
	}

	/**
	 * Method to set the number of channels per each node in the network
	 * @param channelsPerNode the number of channels to set the new number of channels per each node in the network
	 */
	public void setChannelsPerNode(int channelsPerNode) {
		this.channelsPerNode = channelsPerNode;
	}

	/**
	 * Method the set if the simulation is completed
	 * @param simulationCompleted boolean to set the simulation completion status
	 */
	public void setSimulationCompleted(boolean simulationCompleted) {
		this.simulationCompleted = simulationCompleted;
	}

	/**
	 * Method to create a network map using the network data model syntax
	 */
	public void createNetworkMap(){
		Random rand = new Random();
		rand.setSeed(System.currentTimeMillis());
//		TODO NetworkMapGenerator seed in config file to replicate scenario
		try {
			init(networkSize, channelsPerNode, 2, dataFetcher);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Method to set the dataFetcher entity object for fetching local data from the simulation
	 * @param dataFetcher Entity object to set the new dataFetcher object
	 */
	public void setDataFetcher(DataFetcher dataFetcher) {
		this.dataFetcher = dataFetcher;
	}

	/**
	 * Method to get the network size
	 * @return The network size
	 */
	public int getNetworkSize() {
		return networkSize;
	}

	/**
	 * Method to get the amount of channel per each node in the network
	 * @return The number of channels per each node
	 */
	public int getChannelsPerNode() {
		return channelsPerNode;
	}

	/**
	 * Method to write Load the Network data model writing into the JSON Network Map.
	 * @param nodeSize Number of nodes to write into the network map
	 * @param channelsPerNode Number of channels per node to write into the network map
	 * @param transactionSize Number of transactions to write into the network map
	 * @param dataFetcher DataFetcher object to fetch local data from the simulation
	 * @throws IOException If the file to write the network map is not found
	 */
	private void init(int nodeSize, int channelsPerNode, int transactionSize, DataFetcher dataFetcher) throws IOException {
		JSONObject json = new JSONObject();
		JSONArray config = new JSONArray();
		int id = 0;
		int channelId = 0;
		for (int i = 1; i < nodeSize+1; i++) {
			id++;
			JSONObject nodes = new JSONObject();
			JSONArray channelsArray = new JSONArray();
			Random rand = new Random();
			nodes.put("id",String.valueOf(id));
			nodes.put("alias",("Node" + (i)));
			nodes.put("balance" , String.valueOf(rand.nextInt(1000)));
			for (int j = 0; j < channelsPerNode; j++) {
				channelId = writeChannels(nodeSize, transactionSize, id, channelId, i, channelsArray);
			}
			nodes.put("channels", channelsArray);
			config.add(nodes);
		}

		json.put("Nodes", config);


		if (simulationCompleted){
			writeSimulationResults(dataFetcher, json);
		}
		// try-with-resources statement based on post comment below :)
		try (FileWriter file = new FileWriter("src/main/resources/config/custom.json")) {
			file.write(json.toJSONString());
		}
	}

	/**
	 * Method to assist the writing process of the simulation results at the end of the simulation.
	 * @param dataFetcher DataFetcher object to fetch local data from the simulation
	 * @param json JSON object to write the data
	 */
	private void writeSimulationResults(DataFetcher dataFetcher, JSONObject json) {
		int networkBalance = 0;
		int fees = 0;
		for (Node n : dataFetcher.getNodes()) {
			networkBalance += n.getBalance();
		}

		for (Channel c : dataFetcher.getChannels()) {
			fees += c.getFee();
		}

		JSONObject results = new JSONObject();
		JSONArray resultValues = new JSONArray();
		results.put("Transactions", String.valueOf(dataFetcher.getTrafficGenerator().trafficSize()));
		results.put("Failed Transactions", "");
		results.put("Hops", String.valueOf(dataFetcher.getHops()));
		results.put("Fees", String.valueOf(fees));
		results.put("Channels", String.valueOf(dataFetcher.getChannels().size()));
		results.put("Congested Channels", dataFetcher.getCongestedChannels());
		results.put("Network Balance", String.valueOf(networkBalance));
		results.put("Network Size", String.valueOf(dataFetcher.getNodes().size()));

		resultValues.add(results);

		json.put("Results", resultValues);
	}

	/**
	 * Method to assist the writing process of the network map generation. This method is focused on writing the
	 * channels into the network map using the network data model syntax.
	 * @param nodeSize Number of nodes to write into the network map
	 * @param transactionSize Number of transactions to write into the network map
	 * @param nodeFrom Id from the current node who owns the channel
	 * @param channelId Channel id of the current channel
	 * @param i Number to ensure the a channel is not opened to 0 or to the same node
	 * @param channelsArray List of channels to write in the network map
	 * @return the channel id of the current channel
	 */
	private int writeChannels(int nodeSize, int transactionSize, int nodeFrom, int channelId, int i, JSONArray channelsArray) {
		channelId++;
		JSONObject channels = new JSONObject();
		JSONArray transactionArray = new JSONArray();
		Random random = new Random();
		channels.put("id",String.valueOf(channelId));
		channels.put("from",String.valueOf(nodeFrom));
		int randomNumber = random.nextInt(nodeSize);
		boolean valid = false;
		do {
			if (randomNumber == i || randomNumber == 0){
				randomNumber = random.nextInt(nodeSize);
			} else {
				valid = true;
			}
		}while (!valid);
		channels.put("to",String.valueOf(randomNumber));
		channels.put("capacity",String.valueOf(random.nextInt(100)));
		channels.put("fee", String.valueOf(random.nextInt(7)));
		for (int k = 0; k < transactionSize ; k++) {
		}

		for (Transaction t : dataFetcher.getTrafficGenerator().getTransactions()) {
			writeTransactions(transactionArray, t);

		}
		channels.put("transactions" ,transactionArray);
		channelsArray.add(channels);
		return channelId;
	}

	/**
	 * Method to assist the writing process of the network map generation. This method is focused on writing the
	 * trannsactions into the network map using the network data model syntax once the simulation has ended.
	 * @param transactionArray List of transactions simulated in the network
	 * @param currentTransaction Transaction which is being currently written into the network map
	 */
	private void writeTransactions(JSONArray transactionArray, Transaction currentTransaction) {
		JSONObject transaction = new JSONObject();
		transaction.put("secret", currentTransaction.getSecret());
		transaction.put("paymentRequest", currentTransaction.getPaymentRequest());
		transaction.put("tokens", String.valueOf(currentTransaction.getTokens()));
		transaction.put("createdAt", String.valueOf(currentTransaction.getCreatedAt()));
		transaction.put("expiredAt", String.valueOf(currentTransaction.getExpiredAt()));
		transactionArray.add(transaction);
	}
}
