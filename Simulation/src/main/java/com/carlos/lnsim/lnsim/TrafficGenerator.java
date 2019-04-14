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
	private HashMap<Node, Node> routingtable;

	public TrafficGenerator(HashMap routingtable) {
		transactions =new LinkedList<Transaction>() {
		};
		this.routingtable = routingtable;
	}

	public void setRoutingtable(HashMap<Node, Node> routingtable) {
		this.routingtable = routingtable;
	}

	public void setTransactions(Queue<Transaction> transactions) {
		this.transactions = transactions;
	}

	public HashMap<Node, Node> getRoutingtable() {
		return routingtable;
	}

	public void addLink(Node from, Node to) {
	routingtable.put(from, to);
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

//	public void init(Load load, NetworkMapGenerator networkMapGenerator, int recipient, Timer[] timer) {
//		double feeCounter = 0;
//		int hops = 0;
//		int transactions = 0;
//		int congestion = 0;
//		Random rand = new Random();
//
//		for (Node node : load.getNodes()) {
//			Node to = new Node();
//			Channel currentChannel = null;
//			double fee = 0;
//			if (!node.getChannels().isEmpty()){
//				for (Channel channel : node.getChannels()) {
//					to = to.findNode(to, rand.nextInt(load.getNodes().size()), load.getNodes());
//					currentChannel = channel;
//					fee = channel.getFee();
//					feeCounter += channel.getFee();
//					//feesLabel.setText(String.valueOf(feeCounter));
//					transactions++;
//
//					if (load.getRoutingTable().get(node).getId() == to.getId()){
//						System.out.println("Direct Link: Node " + node.getId() + " - Node " + load.getRoutingTable().get(node).getId());
//
//					}else{
//						hops++;
//						System.out.println("Link: Node " + node.getId() + " - Node " + load.getRoutingTable().get(node).getId());
//						//hopsLabel.setText(String.valueOf(hops));
//						load.setHops(hops);
//					}
//
//				}
//
//
//				if ((node.getBalance() > 0) || (currentChannel.getCapacity() > 0)){
//					do {
//						node.setBalance((node.getBalance() - recipient) - (int)fee);
//						to.setBalance(to.getBalance() + recipient);
//						currentChannel.setCapacity(currentChannel.getCapacity() - recipient);
//						transactions++;
//						load.getTrafficGenerator().addTransaction(new Transaction(to, (double) recipient));
//
//					}while ((node.getBalance() > 0) || (currentChannel.getCapacity() > 0));
//					if (currentChannel.getCapacity()< 1){
//						congestion++;
//						//congestedChannels.setText(String.valueOf(congestion));
//						load.setCongestedChannels(congestion);
//					}
//				} else {
//					timer[0].stop();
//				}
//
//
//				if (node.getBalance() < 0){
//					node.setBalance(0.0);
//				}
//
//				networkMapGenerator.setSimulationCompleted(true);
//				networkMapGenerator.createNetwork();
//				//transactionLabel.setText(String.valueOf(transactions));
//				updateGUI();
//			}
//		}
//	}

	private void updateGUI() {

	}
}
