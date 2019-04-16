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

import javax.swing.Timer;
import java.util.*;

public class TrafficGenerator {

	private Queue<Transaction> transactions;
	private HashMap<Node, Node> routingtable;
	private boolean ocurrence, skip;
	private ArrayList<Node> checkedNodes;

	public TrafficGenerator(HashMap routingtable) {
		transactions = new LinkedList<Transaction>() {
		};
		this.routingtable = routingtable;
		checkedNodes = new ArrayList<>();
		ocurrence = false;
	}

	public TrafficGenerator() {
		ocurrence = false;
		checkedNodes = new ArrayList<>();
		transactions = new LinkedList<Transaction>() {};
		routingtable = new HashMap<>();
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

	protected void addLink(Node from, Node to) {
		routingtable.put(from, to);
	}

	protected void addTransaction(Transaction transaction) {
		transactions.add(transaction);
	}

	protected Transaction pushTransaction() {
		return transactions.peek();
	}

	public void start(ArrayList<Channel> channels, Transaction transaction, Node node) {
		for (Channel channel : channels) {
			//check the availability of the channel + balance
			if ((channel.getCapacity() <= transaction.getTokens()) && (node.getBalance() >= transaction.getTokens())) {
				// generate transaction an remove it from the queue
				node.newTransaction(pushTransaction());
			}
		}
	}

	protected int trafficSize() {
		return transactions.size();
	}

	public Queue<Transaction> getTransactions() {
		return transactions;
	}

	@Override public String toString() {
		return "TrafficGenerator{" + "transactions=" + transactions + '}';
	}

	protected void routingMechanism(Node to, Node node, Channel currentChannel, int r, Load l, Timer t[]) {
		// declare variables to store results
		int feeCounter = 0;
		int hops = 0;
		int congestion = 0;
		double fee = 0.0;
		boolean coincidence = false;
		Node destination = new Node();

		// declare randomizer
		Random rand = new Random();

		// for each channel a node has
		for (Channel channel : node.getChannels()) {
			// select the destination node
			to = to.findNode(to, rand.nextInt(l.getNodes().size()), l.getNodes());
			System.err.println("FIRST TRANSACTION");
			System.out.println("sender node" + node.getId());
			System.out.println("destination node: " + to.getId());

			// make the current channel the one used
			currentChannel = channel;

			// select the fee from the channel
			fee = channel.getFee();

			// keep track of the global fee amount
			feeCounter += channel.getFee();


			// if there is direct channel. No need for routinh
			if (l.getRoutingTable().get(node).getId() == to.getId()){
				System.out.println("Direct Link: Node " + node.getId() + " - Node " + l.getRoutingTable().get(node).getId());
				if ((node.getBalance() > 0) || (currentChannel.getCapacity() > 0)){
					do {
						// EMIT TRANSACTION
						sendTransaction(to, node, currentChannel, r, l, fee);

					}while ((node.getBalance() > 0) || (currentChannel.getCapacity() > 0));
				}
			}else{
				// add 1 to the hop counter
				hops++;

				System.out.println("Link: Node " + node.getId() + " - Node " + l.getRoutingTable().get(node).getId());

				destination = searchPath(node, to, l, destination);

				do {

//					System.out.println("Searching paths");
//					System.out.println();
//					for(Map.Entry<Node, Node> entry : l.getRoutingTable().entrySet()) {
//						Node from = entry.getKey();
//						Node destino = entry.getValue();
//						System.out.println("node " + from.getId());
//						System.out.println();
//						if (from.getId() == to.getId()) {
//							coincidence = true;
//							System.out.println("Node " + from.getId() + " has a channel with destionation");
//						}
//					}


				}while (!coincidence);

				// set hop number
				l.setHops(hops);
			}

		}

		// Sanity check. if emitter node has enough balance and channel capacity is not dried out.
		if ((node.getBalance() > 0) || (currentChannel.getCapacity() > 0)){
			do {
				// EMIT TRANSACTION
				sendTransaction(to, node, currentChannel, r, l, fee);

			}while ((node.getBalance() > 0) || (currentChannel.getCapacity() > 0));


			if (currentChannel.getCapacity()< 1){

				// congestion counter +1
				congestion++;
				l.setCongestedChannels(congestion);
			}
		} else {
			t[0].stop();
		}


		if (node.getBalance() < 0){
			node.setBalance(0.0);
		}
	}

	private Node searchPath(Node node, Node to, Load l, Node destination) {
		destination = destination.findNode(destination, l.getRoutingTable().get(node).getId(), l.getNodes());

		if (checkedNodes.contains(destination)){
			skip = true;
		}else {
			checkedNodes.add(destination);
		}

		if (!skip){

			System.out.println("Link: Node " + destination.getId() + " - Node " + l.getRoutingTable().get(destination).getId());

			if (destination.getId() == to.getId()) {
				ocurrence = true;
				return destination;
			}else {
				if (!ocurrence){
					searchPath(destination, to, l, destination);
				}
			}
		}
		return destination;

	}

	private void sendTransaction(Node to, Node node, Channel currentChannel, int r, Load l, double fee) {
		// Set node balance
		node.setBalance((node.getBalance() - r) - fee);
		to.setBalance(to.getBalance() + r);

		// Set channel capacity
		currentChannel.setCapacity(currentChannel.getCapacity() - r);

		// add transaction to the traffic buffer
		l.getTrafficGenerator().addTransaction(new Transaction(to, (double) r));
	}
}