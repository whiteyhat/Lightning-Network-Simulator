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

public class Channel {
    private int id;
    private Double capacity;
    private Double fee;
    private ArrayList<Transaction> transactions;
    private int from, to;

    public Channel(int id, Double capacity, Double fee, ArrayList<Transaction> transactions, int from, int to) {
        this.id = id;
        this.capacity = capacity;
        this.fee = fee;
        this.transactions = transactions;
        this.from = from;
        this.to = to;

    }

	public Channel(int parseInt, double parseDouble, int parseInt1, int parseInt2) {
	}

	public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Double getCapacity() {
        return capacity;
    }

    public void setCapacity(Double capacity) {
        this.capacity = capacity;
    }

    public Double getFee() {
        return fee;
    }

    public int getFrom() {
        return from;
    }

    public Transaction getTransaction(int id){
        for(Transaction transaction : transactions) {
            if(String.valueOf(getId()).equals(String.valueOf(id))) {
                return transaction;
            }
        }
        return null;
    }

    public void setFrom(int from) {
        this.from = from;
    }

    public int getTo() {
        return to;
    }

    public void setTo(int to) {
        this.to = to;
    }

    public void setFee(Double fee) {
        this.fee = fee;
    }

    public ArrayList<Transaction> getTransactions() {
        return transactions;
    }

    public void setTransactions(ArrayList<Transaction> transactions) {
        this.transactions = transactions;
    }

    @Override public String toString() {
        return "Channel{" + "id=" + id + ", capacity=" + capacity + ", fee=" + fee + ", transactions=" + transactions
                + ", from=" + from + ", to=" + to + '}';
    }
}
