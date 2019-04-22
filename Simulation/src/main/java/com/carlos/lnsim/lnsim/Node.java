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

public class Node {
    private String alias;
    private int id;
    private Double inboundCapacity;
    private Double outboundCapacity;
    private Double balance;
    private ArrayList<Channel> channels;
    private ArrayList<Transaction> transactions;

    public Node(String alias, int id, Double balance, ArrayList<Channel> channels, ArrayList<Transaction> transactions) {
        this.alias = alias;
        this.id = id;
        this.balance = balance;
        this.channels = channels;
        this.transactions = transactions;
    }

    public Node() {

    }
    protected Node findNode(Node mynode, int id, ArrayList<Node> nodes) {
        for(Node node : nodes) {
            if(node.getId() == id) {
                mynode = node;
            }
        }
        return mynode;
    }

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }


    public Double getBalance() {
        return balance;
    }

    public void setBalance(Double balance) {
        this.balance = balance;
    }

    public ArrayList<Channel> getChannels() {
        return channels;
    }

    public void setChannels(ArrayList<Channel> channels) {
        this.channels = channels;
    }

    public ArrayList<Transaction> getTransactions() {
        return transactions;
    }

    public void setTransactions(ArrayList<Transaction> transactions) {
        this.transactions = transactions;
    }

    @Override
    public String toString() {
        return  "Node ID: " + id + "\n" +
                "Node Alias: " + alias + "\n" +
                "Balance: " + balance + "\n" +
                "Channels: " + channels + "\n" +
                "Transactions: " + transactions;
    }
}
