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
 * Class to create object entities essential for the network component channel
 */
public class Channel {
    private int id;
    private Double capacity;
    private Double fee;
    private ArrayList<Transaction> transactions;
    private int from, to;

    /**
     * Constructor to create a Channel object entity with all the required parameters.
     * @param id Identity number to keep track of unique channels.
     * @param capacity Amount of channel capacity to handle transactions.
     * @param fee Commission for using the channel to route transactions.
     * @param transactions Number of transactions handled by the channel.
     * @param from Start-point from the channel. Node Id who owns the channel.
     * @param to End-poin from the channel. Receiver node id from the channel.
     */
    public Channel(int id, Double capacity, Double fee, ArrayList<Transaction> transactions, int from, int to) {
        this.id = id;
        this.capacity = capacity;
        this.fee = fee;
        this.transactions = transactions;
        this.from = from;
        this.to = to;

    }

    /**
     * Constructor to create a channel object entity without parameters.
     */
    public Channel() {
    }

    /**
     * Method to get the Channel ID
     * @return The channel id number
     */
	public int getId() {
        return id;
    }

    /**
     * Method to set the channel ID
     * @param id Number to set the new channel id
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Method to get the channel capacity
     * @return The channel capacity number
     */
    public Double getCapacity() {
        return capacity;
    }

    /**
     * Method to set the channel capacity
     * @param capacity Number to set the new capacity
     */
    public void setCapacity(Double capacity) {
        this.capacity = capacity;
    }

    /**
     * Method to set the channel fee
     * @return The channel fee number
     */
    public Double getFee() {
        return fee;
    }

    /**
     * Method to get the node id that established the channel
     * @return The node id that owns the channel
     */
    public int getFrom() {
        return from;
    }

    /**
     * Method to set the node id from the channel owner
     * @param from Number to set the new channel owner
     */
    public void setFrom(int from) {
        this.from = from;
    }

    /**
     * Method to get the node id from the channel destination
     * @return The number to set the new channel destination
     */
    public int getTo() {
        return to;
    }

    /**
     Method to set the node id which is the channel destination
     * @param to Number to set the new channel destination
     */
    public void setTo(int to) {
        this.to = to;
    }

    /**
     * Method to set the channel fee
     * @param fee Number to set the new fee
     */
    public void setFee(Double fee) {
        this.fee = fee;
    }

    /**
     * Method to get the list of transaction from this channel
     * @return The list of transactions
     */
    public ArrayList<Transaction> getTransactions() {
        return transactions;
    }

    /**
     * Method to set the list of transactions from this channel
     * @param transactions Array list to set the new transactions
     */
    public void setTransactions(ArrayList<Transaction> transactions) {
        this.transactions = transactions;
    }

    /**
     * Method to get a string containing the channel object entities
     * @return The variables from the channel object
     */
    @Override public String toString() {
        return "Channel{" + "id=" + id + ", capacity=" + capacity + ", fee=" + fee + ", transactions=" + transactions
                + ", from=" + from + ", to=" + to + '}';
    }
}
