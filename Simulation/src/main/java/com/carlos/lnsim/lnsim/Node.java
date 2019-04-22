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


import java.util.ArrayList;

/**
 * Class to create object entities essential for the network component transaction
 */
public class Node {
    private int id;
    private Double balance;
    private ArrayList<Channel> channels;
    private ArrayList<Transaction> transactions;

    /**
     * Constructor to create a Node object entity with all the required parameters and variables
     * @param id Number to identify the node
     * @param balance Balance to send transactions
     * @param channels Group of node channels
     * @param transactions Group of node transactions
     */
    public Node(int id, Double balance, ArrayList<Channel> channels, ArrayList<Transaction> transactions) {
        this.id = id;
        this.balance = balance;
        this.channels = channels;
        this.transactions = transactions;
    }

    /**
     * Constructor to create a Node object entity without parameters
     */
    public Node() {
    }

    /**
     * Method to find a node by using its id
     * @param mynode Node to be found
     * @param id Identification key to find the node
     * @param nodes Array list of nodes to search the node
     * @return The searched node
     */
    protected Node findNode(Node mynode, int id, ArrayList<Node> nodes) {
        for(Node node : nodes) {
            if(node.getId() == id) {
                mynode = node;
            }
        }
        return mynode;
    }

    /**
     * Method to get the node id
     * @return The node id
     */
    public int getId() {
        return id;
    }

    /**
     * Method to set the node id
     * @param id Number to set the new node id
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Method to get the node balance
     * @return The node balance
     */
    public Double getBalance() {
        return balance;
    }

    /**
     * Method to set the node balance
     * @param balance Amount of the balance node id
     */
    public void setBalance(Double balance) {
        this.balance = balance;
    }

    /**
     * Method to get the node channels
     * @return The list of node channels
     */
    public ArrayList<Channel> getChannels() {
        return channels;
    }

    /**
     * Method to get the set the node channels
     * @param channels List of the new node channels
     */
    public void setChannels(ArrayList<Channel> channels) {
        this.channels = channels;
    }

    /**
     * Method to get the node transactions
     * @return The list of the node transactions
     */
    public ArrayList<Transaction> getTransactions() {
        return transactions;
    }

    /**
     * Method to set the node transactions
     * @param transactions list of the new node transactions
     */
    public void setTransactions(ArrayList<Transaction> transactions) {
        this.transactions = transactions;
    }

    /**
     * Method to get a string containing the node object entities
     * @return The variables from the node object
     */
    @Override
    public String toString() {
        return  "Node ID: " + id + "\n" +
                "Balance: " + balance + "\n" +
                "Channels: " + channels + "\n" +
                "Transactions: " + transactions;
    }
}
