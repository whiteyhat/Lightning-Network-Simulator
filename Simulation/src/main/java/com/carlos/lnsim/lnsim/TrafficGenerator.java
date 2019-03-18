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

import java.util.*;

public class TrafficGenerator {

	private Queue<Transaction> transactions;

	public TrafficGenerator() {
		transactions =new LinkedList<Transaction>() {
		};
	}

	public void addTransaction(Transaction transaction) {
		transactions.add(transaction);
	}

	public Transaction pushTransaction() {
		return transactions.peek();
	}
	
	public void start(ArrayList<Channel> channels, Transaction transaction, Node node) {
		for (Channel channel : channels) {
			//check the availability of the channel + balance
		if ((channel.getCapacity() <= transaction.getTokens()) && (node.getBalance()>= transaction.getTokens())){
				// generate transaction an remove it from the queue
				node.newTransaction(pushTransaction());
			}
		}
	}

	public int trafficSize() {
		return transactions.size();
	}

	public Queue<Transaction> getTransactions() {
		return transactions;
	}

	@Override public String toString() {
		return "TrafficGenerator{" + "transactions=" + transactions + '}';
	}

	public static void main(String[] args){
		TrafficGenerator trafficGenerator = new TrafficGenerator();

		for (int i = 0; i < 3; i++) {
			Transaction t = new Transaction(new Node(), 1);
			trafficGenerator.addTransaction(t);
		}

		System.out.println(trafficGenerator.trafficSize());

	}
}
