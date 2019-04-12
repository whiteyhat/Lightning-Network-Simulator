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

public class Load {
    private ArrayList<Transaction> transactions;
    private ArrayList<Channel> channels;
    private ArrayList<Node> nodes;
    private boolean readyToLoad = false;
    private String path = "";
    private TrafficGenerator trafficGenerator;
    private HashMap<Node, Node> routingTable;
    private int hops, congestedChannels, failedTransactions, transactionAmount;

    public Load() {
        try {
            start();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void start() throws IOException, ParseException {
        transactions = new ArrayList<Transaction>();
        routingTable = new HashMap<>();
        trafficGenerator = new TrafficGenerator(routingTable);
        channels = new ArrayList<Channel>();
        nodes = new ArrayList<Node>();
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

    public HashMap<Node, Node> getRoutingTable() {
        return routingTable;
    }

    public TrafficGenerator getTrafficGenerator() {
        return trafficGenerator;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public boolean isReadyToLoad() {
        return readyToLoad;
    }

    public void setReadyToLoad(boolean readyToLoad) {
        this.readyToLoad = readyToLoad;
    }

    public ArrayList<Transaction> getTransactions() {
        return transactions;
    }

    public ArrayList<Channel> getChannels() {
        return channels;
    }

    public ArrayList<Node> getNodes() {
        return nodes;
    }

    private  JSONArray getJsonFile(JSONParser parser, String path) throws IOException, ParseException {
        Object obj = parser.parse(new FileReader(path));

        JSONObject json = (JSONObject) obj;
        return (JSONArray) json.get("Nodes");
    }

    private  void getNodes(JSONArray nodesJson, ArrayList<Transaction> transactions, ArrayList<Channel> channels, ArrayList<Node> nodes, int i) {
        JSONObject node = (JSONObject) nodesJson.get(i);

        String alias = (String) node.get("alias");
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

        nodes.add(new Node(alias, Integer.parseInt(id), Integer.parseInt(balance), nodeChannels, transactions));
    }

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

    private  void getTransactions(ArrayList<Transaction> transactions, JSONArray transactionsJson, int k) {
        JSONObject transaction = (JSONObject) transactionsJson.get(k);

        String transactionTokens = (String) transaction.get("tokens");

    }

    public int getHops() {
        return hops;
    }

    public void setHops(int hops) {
        this.hops = hops;
    }

    public int getCongestedChannels() {
        return congestedChannels;
    }

    public void setCongestedChannels(int congestedChannels) {
        this.congestedChannels = congestedChannels;
    }

    public int getFailedTransactions() {
        return failedTransactions;
    }

    public void setFailedTransactions(int failedTransactions) {
        this.failedTransactions = failedTransactions;
    }

    public int getTransactionAmount() {
        return transactionAmount;
    }

    public void setTransactionAmount(int transactionAmount) {
        this.transactionAmount = transactionAmount;
    }
}