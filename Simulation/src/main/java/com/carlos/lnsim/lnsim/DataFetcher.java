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

import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

/**
 * Class to create object entities essential for the network simulation.
 * This class is used to create data fetcher objects to load and fetch local data
 */
public class DataFetcher {
    private ArrayList<Transaction> transactions;
    private ArrayList<Channel> channels;
    private ArrayList<Node> nodes;
    private boolean readyToLoad = false;
    private String path = "";
    private TrafficGenerator trafficGenerator;
    private HashMap<Node, Node> routingTable;
    private int hops, congestedChannels, failedTransactions;

    /**
     * Constructor to create a data fetcher object entity with the required methods.
     */
    public DataFetcher() {
        try {
            Load();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Method to fetch and load data from the network map into the system. By default the method searches for a
     * JSON file named "custom.json" to fetch the local data from the network map.
     * @throws IOException If the custom.json file is not found
     * @throws ParseException If there is a JSON syntax issue when parsing data from the network map to the system
     */
    public void Load() throws IOException, ParseException {
        transactions = new ArrayList<>();
        routingTable = new HashMap<>();
        trafficGenerator = new TrafficGenerator(routingTable);
        channels = new ArrayList<>();
        nodes = new ArrayList<>();
        JSONParser parser = new JSONParser();
        JSONArray nodesJson = null;
        if (readyToLoad){
            nodesJson = getJsonFile(parser, path);
        }else {
           nodesJson = getJsonFile(parser, "src/main/resources/config/custom.json");
        }
        // FOR EACH NODE
        for (int i = 0; i <nodesJson.size() ; i++) {
            getNodes(nodesJson, transactions, channels, nodes, i);
        }

        com.carlos.lnsim.lnsim.Node from = new com.carlos.lnsim.lnsim.Node();
        com.carlos.lnsim.lnsim.Node to = new com.carlos.lnsim.lnsim.Node();

        for (Node node : getNodes()) {
            for (Channel channel : node.getChannels()) {
                to = to.findNode(to, channel.getTo(), getNodes());
                from = from.findNode(from, channel.getFrom(), getNodes());

                trafficGenerator.addLink(from, to);
            }
        }
    }

    /**
     * Method to get the routing table containing all direct paths between nodes
     * @return the routing tables
     */
    public HashMap<Node, Node> getRoutingTable() {
        return routingTable;
    }

    /**
     * Method to set the TrafficGenerator entity object
     * @param trafficGenerator New TrafficGenerator entity object to bet set
     */
    public void setTrafficGenerator(TrafficGenerator trafficGenerator) {
        this.trafficGenerator = trafficGenerator;
    }

    /**
     * Method to get the TrafficGenerator entity object
     * @return The TrafficGenerator entity object
     */
    public TrafficGenerator getTrafficGenerator() {
        return trafficGenerator;
    }

    /**
     * Method to set the file path to fetch data from the network map
     * @param path New path of the network map
     */
    public void setPath(String path) {
        this.path = path;
    }

    /**
     * Method to set if the fetching data process is ready to load
     * @param readyToLoad boolean of the loading status
     */
    public void setReadyToLoad(boolean readyToLoad) {
        this.readyToLoad = readyToLoad;
    }

    /**
     * Method to get the transaction list
     * @return The transaction list
     */
    public ArrayList<Transaction> getTransactions() {
        return transactions;
    }

    /**
     * Method to get the channel list
     * @return The channel list
     */
    public ArrayList<Channel> getChannels() {
        return channels;
    }

    /**
     * Method to return the node list
     * @return The node list
     */
    public ArrayList<Node> getNodes() {
        return nodes;
    }

    /**
     * Method to get the network map file path
     * @param parser JSON Parser to parse the network map into the system
     * @param path Network map file path
     * @return The list of nodes to load them into the network simulator
     * @throws IOException If the path is not found
     * @throws ParseException If there is a JSON syntax issue when parsing data from the network map to the system
     */
    private  JSONArray getJsonFile(JSONParser parser, String path) throws IOException, ParseException {
        Object obj = parser.parse(new FileReader(path));

        JSONObject json = (JSONObject) obj;
        return (JSONArray) json.get("Nodes");
    }

    /**
     * Method to create nodes in the lightning network simulator from the network map
     * @param nodesJson List of nodes parsed from the network map
     * @param transactions List of transactions created in the lightning network simulator
     * @param channels List of channels created in the lightning network simulator
     * @param nodes List of nodes created in the lightning network simulator
     * @param i Number to keep track of each selection
     */
    private  void getNodes(JSONArray nodesJson, ArrayList<Transaction> transactions, ArrayList<Channel> channels, ArrayList<Node> nodes, int i) {
        JSONObject node = (JSONObject) nodesJson.get(i);

        String id = (String) node.get("id");
        String balance = (String) node.get("balance");

        JSONArray channelsJson = (JSONArray) node.get("channels");

//                FOR EACH CHANNEL IN A NODE
        for (int j = 0; j < channelsJson.size(); j++) {
            getChannels(transactions, channels, channelsJson, j);
        }

        ArrayList<Channel> nodeChannels = new ArrayList<>();

        for (Channel channel : channels) {
            if (channel.getFrom() == Integer.parseInt(id)){
                nodeChannels.add(channel);
            }
        }

        nodes.add(new Node(Integer.parseInt(id), Double.parseDouble(balance), nodeChannels, transactions));
    }

    /**
     * Method to find a channel by using its id
     * @param selectedChannel Channel to be found
     * @param from Start point from the channel
     * @param to End point from the channel
     * @return The found channel
     */
    protected Channel findChannel(Channel selectedChannel, int from, int to) {
        for(Channel channel : channels) {
            if(String.valueOf(channel.getFrom()).equals(String.valueOf(from)) && String.valueOf(channel.getTo()).equals(String.valueOf(to))) {
                selectedChannel = channel;
            }
        }
        return selectedChannel;
    }

    /**
     * Method to create channels in the lightning network simulator from the network map
     * @param transactions List of transactions created in the lightning network simulator
     * @param channels List of channels created in the lightning network simulator
     * @param channelsJson List of channels parsed from the network map
     * @param j Number to keep track of each selection
     */
    private  void getChannels(ArrayList<Transaction> transactions, ArrayList<Channel> channels, JSONArray channelsJson, int j) {
        JSONObject channel = (JSONObject) channelsJson.get(j);
        String channelId = (String) channel.get("id");
        String channelCapacity = (String) channel.get("capacity");

        String channelFee = (String) channel.get("fee");
        String from = (String) channel.get("from");
        String to = (String) channel.get("to");


        JSONArray transactionsJson = (JSONArray) channel.get("transactions");

//                    FOR EACH TRANSACTION IN A CHANNEL
        for (int k = 0; k < transactionsJson.size(); k++) {
            getTransactions(transactions, transactionsJson, k);

        }

        channels.add(new Channel(
                Integer.parseInt(channelId),
                Double.parseDouble(channelCapacity),
                Double.parseDouble(channelFee), transactions,
                Integer.parseInt(from),
                Integer.parseInt(to)
        ));
    }

    /**
     * Method to create transactions in the lightning network simulator from the network map
     * @param transactions List of transactions created in the lightning network simulator
     * @param transactionsJson List of transactions parsed from the network map
     * @param k Number to keep track of each selection
     */
    private  void getTransactions(ArrayList<Transaction> transactions, JSONArray transactionsJson, int k) {
        JSONObject transaction = (JSONObject) transactionsJson.get(k);

        String transactionTokens = (String) transaction.get("tokens");

    }

    /**
     * Method to get the amount of hops done in a routed transaction
     * @return The number of hops
     */
    public int getHops() {
        return hops;
    }

    /**
     * Method to get the amount of hops done in a routed transaction
     * @param hops the new number of hops
     */
    public void setHops(int hops) {
        this.hops = hops;
    }

    /**
     * Method to the get the number of congested channels
     * @return The number of congested channels
     */
    public int getCongestedChannels() {
        return congestedChannels;
    }

    /**
     * Method to set the number of congested channels
     * @param congestedChannels the new number of congested channels
     */
    public void setCongestedChannels(int congestedChannels) {
        this.congestedChannels = congestedChannels;
    }

    /**
     * Method to get the number of failed transactions
     * @return The number of failed transactions
     */
    public int getFailedTransactions() {
        return failedTransactions;
    }

    /**
     * Method to set the number of failed transactions
     * @param failedTransactions The number of new failed transactions
     */
    public void setFailedTransactions(int failedTransactions) {
        this.failedTransactions = failedTransactions;
    }
}