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
	private ArrayList<Channel> checkedPaths;
	private ArrayList<Transaction> failedTransactions, directTransactions, routedTransactions;
	private Channel channel;
	private int hops, staticHops = 0;
	private double fee, feeCounter = 0;
	private boolean invalidPath = false;
	private TerminalColors terminalColors;

	public TrafficGenerator(HashMap routingtable) {
		terminalColors = new TerminalColors();
		channel = new Channel();
		transactions = new LinkedList<Transaction>() {};
		failedTransactions = new ArrayList<>();
		this.routingtable = routingtable;
		checkedPaths = new ArrayList<>();
		directTransactions = new ArrayList<>();
		routedTransactions = new ArrayList<>();
	}

	public TrafficGenerator() {
		terminalColors = new TerminalColors();
		channel = new Channel();
		checkedPaths = new ArrayList<>();
		failedTransactions = new ArrayList<>();
		transactions = new LinkedList<Transaction>() {};
		routingtable = new HashMap<>();
		directTransactions = new ArrayList<>();
		routedTransactions = new ArrayList<>();
	}

	public double getFeeCounter() {
		return feeCounter;
	}

	public int getStaticHops() {
		return staticHops;
	}

	public void setTransactions(Queue<Transaction> transactions) {
		this.transactions = transactions;
	}

	protected void addLink(Node from, Node to) {
		routingtable.put(from, to);
	}

	protected void addTransaction(Transaction transaction) {
		transactions.add(transaction);
	}

	protected int trafficSize() {
		return transactions.size();
	}

	public Queue<Transaction> getTransactions() {
		return transactions;
	}

	public ArrayList<Transaction> getDirectTransactions() {
		return directTransactions;
	}

	public ArrayList<Transaction> getRoutedTransactions() {
		return routedTransactions;
	}

	@Override public String toString() {
		return "TrafficGenerator{" + "transactions=" + transactions + '}';
	}

	protected void routingMechanism(Node to, Node from, Channel currentChannel, int r, Load l, Timer t[]) {
		// declare variables to store results
		int congestion = 0;
		boolean coincidence = false;
		boolean transactionFailed = false;
		Node destination = new Node();
		boolean valid = false;

		// declare randomizer
		Random rand = new Random();

		// for each channel a from has
		for (Channel channel : from.getChannels()) {

			// Ensure the receiver from is not the same as the sender
			int receiver = rand.nextInt(l.getNodes().size());


			if (receiver == from.getId() || receiver == 0){
				do {
					receiver = rand.nextInt(l.getNodes().size());

					if (receiver != from.getId() && receiver != 0){
						valid = true;
					}
				}while (!valid);

			}

			// select the destination from
			to = to.findNode(to, receiver, l.getNodes());
			System.out.println();
			System.out.println(terminalColors.getBlackBg() + terminalColors.getWhite() + "***********************************" + terminalColors.getStandard());
			System.out.println(terminalColors.getBlackBg() + terminalColors.getGreenBg() + "          TRANSACTION              "+ terminalColors.getStandard() );
			System.out.println(terminalColors.getBlackBg() + terminalColors.getWhite() + "***********************************"+ terminalColors.getStandard());
			System.out.println(terminalColors.getPurple() +"Sender from: " + from.getId()+ terminalColors.getStandard());
			System.out.println(terminalColors.getYellow() +"Receiver from: " + to.getId()+ terminalColors.getStandard());

			// make the current channel the one used
			currentChannel = channel;

			// select the fee from the channel
			fee = channel.getFee();

			// keep track of the global fee amount


			// if there is direct channel. No need for routinh
			if (l.getRoutingTable().get(from).getId() == to.getId()){
				System.out.println(terminalColors.getGreenBg() + terminalColors.getBlack() +"-----------------------------------"+ terminalColors.getStandard());
				System.out.println(terminalColors.getGreenBg() + terminalColors.getBlack() +"       Direct Transaction sent     "+ terminalColors.getStandard());
				System.out.println(terminalColors.getGreenBg() + terminalColors.getBlack() +"-----------------------------------"+ terminalColors.getStandard());
				transactionPayload(to, from, currentChannel, r, l, t, congestion, false);
			}else{
				int track = failedTransactions.size();
				do {
					System.out.println(terminalColors.getBlackBg() + terminalColors.getWhite() +"---------- FINDING PATHS ----------"+ terminalColors.getStandard());
					// Crucial path finding
					destination = searchPath(from, to, l, r);
					invalidPath = false;
					staticHops += hops;
					hops = 0;
					feeCounter += fee;

					// double check
					if (destination.getId() == to.getId()){
						System.out.println(terminalColors.getGreenBg() + terminalColors.getBlack() +"-----------------------------------"+ terminalColors.getStandard());
						System.out.println(terminalColors.getGreenBg() + terminalColors.getBlack() +" $$$$ Routed Transaction sent $$$$ "+ terminalColors.getStandard());
						System.out.println(terminalColors.getGreenBg() + terminalColors.getBlack() +"-----------------------------------"+ terminalColors.getStandard());
						coincidence = true;
						transactionPayload(to, from, currentChannel, r, l, t, congestion, true);
					}

					if (track < failedTransactions.size()){
						coincidence = true;
						System.err.println("Transacion Failed");
					}
					checkedPaths.clear();

				}while (!coincidence);

				// set hop number
				l.setHops(hops);
			}

		}

		// In case from balances are negative correct values to 0
		if (from.getBalance() < 0){
			from.setBalance(0.0);
		}

		if (channel.getCapacity() < 0){
			channel.setCapacity(0.0);
		}
	}

	private void transactionPayload(Node to, Node node, Channel currentChannel, int r, Load l, Timer[] t,
			int congestion, boolean isRouted) {
		if ((node.getBalance() > 0) || (currentChannel.getCapacity() > 0)){
			do {
				// EMIT TRANSACTION
				Transaction tx = sendTransaction(to, node, currentChannel, r, l, fee);

				if (isRouted){
					routedTransactions.add(tx);
				}else {
					directTransactions.add(tx);
				}

			}while ((node.getBalance() > 0) || (currentChannel.getCapacity() > 0));


			if (currentChannel.getCapacity()< 1){

				// congestion counter +1
				congestion++;
				l.setCongestedChannels(congestion);
			}
		} else {
			t[0].stop();
		}
	}

	private Node searchPath(Node node, Node to, Load l, int r) {
		boolean skip = false;
		boolean ocurrence = false;
		Node temp = new Node();

		if (hops>0){
			if (checkedPaths.contains(channel)){
				skip = true;
				invalidPath = true;
				failedTransactions.add(new Transaction(node, Double.valueOf(r)));
			}else {
				// add last channel used in the list to no stop infinite recursion
				checkedPaths.add(channel);
			}
		}

		if (!invalidPath){
			if (!skip){
				// for each channel of selected node
					for (Channel c : node.getChannels()) {

						// receiver node from path route
						temp = node.findNode(node, c.getTo(), l.getNodes());
						channel = l.findChannel(channel, node.getId(), temp.getId());

						// print the path
						System.out.println("PATH: Node " + c.getFrom() + " - Node " + c.getTo());

						// receiver node equal to destination
						if (c.getTo() == to.getId()) {
							node = node.findNode(node, c.getTo(), l.getNodes());
							ocurrence = true;
							fee = c.getFee();
							break;
						}
					}
				}
			hops++;

			//recursive call
			if (!ocurrence)
				node = searchPath(temp, to, l, r);
			}

		// force the node to be the found node
		return node;
	}

	public ArrayList<Transaction> getFailedTransactions() {
		return failedTransactions;
	}

	private Transaction sendTransaction(Node to, Node node, Channel currentChannel, int r, Load l, double fee) {
		// Set node balance
		node.setBalance((node.getBalance() - r) - fee);
		to.setBalance(to.getBalance() + r);
		node = node.findNode(node, currentChannel.getFrom(), l.getNodes());
		node.setBalance(node.getBalance()+fee);

		// Set channel capacity
		currentChannel.setCapacity(currentChannel.getCapacity() - r);

		Transaction t = new Transaction(to, (double) r);
		// add transaction to the traffic buffer
		l.getTrafficGenerator().addTransaction(t);
		return t;
	}
}