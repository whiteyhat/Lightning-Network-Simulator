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

/**
 * Class to create object entities essential for the network component channel
 */
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

	/**
	 * Constructor to create a TrafficGenerator object entity with an alternative parameter and required variables.
	 * @param routingtable
	 */
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

	/**
	 * Constructor to create a TrafficGenerator object entity with all the required variables.
	 */
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

	/**
	 * Method to get the fee counter
	 * @return The number of fees
	 */
	public double getFeeCounter() {
		return feeCounter;
	}

	/**
	 * Method to the get the static number of hops
	 * @return The number of hops
	 */
	public int getStaticHops() {
		return staticHops;
	}

	/**
	 * Method to set the routing table
	 * @param routingtable New routing table
	 */
	public void setRoutingtable(HashMap<Node, Node> routingtable) {
		this.routingtable = routingtable;
	}

	/**
	 * Method to set the transactions
	 * @param transactions New transaction queue
	 */
	public void setTransactions(Queue<Transaction> transactions) {
		this.transactions = transactions;
	}

	/**
	 * Method to get the routing tables
	 * @return The routing tables
	 */
	public HashMap<Node, Node> getRoutingtable() {
		return routingtable;
	}

	/**
	 * Method to add a path in the routing tables
	 * @param from Start point of the route
	 * @param to End point of the route
	 */
	protected void addLink(Node from, Node to) {
		routingtable.put(from, to);
	}

	/**
	 * Method to add a transaction to the transaction queue
	 * @param transaction New transaction to add to the queue
	 */
	protected void addTransaction(Transaction transaction) {
		transactions.add(transaction);
	}

	/**
	 * Method to get the traffic size of the network
	 * @return the number of the network size
	 */
	protected int trafficSize() {
		return transactions.size();
	}

	/**
	 * Method to get the transaction queue
	 * @return the transaction queue
	 */
	public Queue<Transaction> getTransactions() {
		return transactions;
	}

	/**
	 * Method to get a string containing the TrafficGenerator object entities
	 * @return the TrafficGenerator object entities
	 */
	@Override public String toString() {
		return "TrafficGenerator{" + "transactions=" + transactions + '}';
	}

	/**
	 * Fitness function. Method to route transactions
	 * @param to Receiver node
	 * @param from Sender node
	 * @param currentChannel Current channel used
	 * @param type Transaction recipient type
	 * @param l Data fetcher object entity
	 * @param t Timer to update the GUI
	 */
	protected void routingMechanism(Node to, Node from, Channel currentChannel, int type, DataFetcher l, Timer t[]) {
		// declare variables to store results
		int congestion = 0;
		boolean coincidence = false;
		Node destination = new Node();
		boolean valid = false;

		// declare randomizer
		Random rand = new Random();

		// for each channel a from has
		for (Channel channel : from.getChannels()) {

			// Ensure the receiver from is not the same as the sender
			int receiver = rand.nextInt(l.getNodes().size());

			// DESTINATION CALCULATION
			// if receiver node tries to send a transaction to itself or to none(0).
			// Keep searching random destinations
			//This prevents failed transaction configurations
			if (receiver == from.getId() || receiver == 0){
				do {
					receiver = rand.nextInt(l.getNodes().size());

					if (receiver != from.getId() && receiver != 0){
						valid = true;
					}
				}while (!valid);
			}

			// select the destination from the previous calculation
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

			// if there is direct channel. No need for routinh
			if (l.getRoutingTable().get(from).getId() == to.getId()){
				System.out.println(terminalColors.getGreenBg() + terminalColors.getBlack() +"-----------------------------------"+ terminalColors.getStandard());
				System.out.println(terminalColors.getGreenBg() + terminalColors.getBlack() +"       Direct Transaction sent     "+ terminalColors.getStandard());
				System.out.println(terminalColors.getGreenBg() + terminalColors.getBlack() +"-----------------------------------"+ terminalColors.getStandard());
				transactionPayload(to, from, currentChannel, type, l, t, congestion, false);
			}else{
				// Checker to keep track of the failed transaction
				int track = failedTransactions.size();
				do {
					System.out.println(terminalColors.getBlackBg() + terminalColors.getWhite() +"---------- FINDING PATHS ----------"+ terminalColors.getStandard());
					// Crucial path finding
					destination = searchPath(from, to, l);
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
						transactionPayload(to, from, currentChannel, type, l, t, congestion, true);
					}

					// If there are more failed transaction and the checker it means the transaction
					// has failed within the inner path search
					if (track < failedTransactions.size()){
						coincidence = true;
						System.err.println("Transacion Failed");
					}

					// Clear the checped path before iterating over
					checkedPaths.clear();

				}while (!coincidence);

				// set hop number
				l.setHops(hops);
			}

		}
		// SANITY CHECKS

		// In case of node balances are negative correct values to 0
		if (from.getBalance() < 0){
			from.setBalance(0.0);
		}

		// In case of channel capacities are negative correct values to 0

		if (currentChannel.getCapacity() < 0){
			currentChannel.setCapacity(0.0);
		}
	}

	/**
	 * Method to send the transaction Payload. It contains sanity check to node balances an channel capacities.
	 * @param to Receiver node
	 * @param from Sender node
	 * @param currentChannel Current channel used
	 * @param type Transaction recipient type
	 * @param l Data fetcher object entity
	 * @param t Timer to update the GUI
	 * @param congestion Amount of congested channel
	 * @param isRouted Boolean to check the transaction is routed or direct
	 */
	private void transactionPayload(Node to, Node from, Channel currentChannel, int type, DataFetcher l, Timer[] t,
			int congestion, boolean isRouted) {

		// Initialize random object and recipient integer
		Random rand = new Random();
		int r = 1;


		if ((from.getBalance() > 0) || (currentChannel.getCapacity() > 0)){
			do {
				// set up recipient amount based on the type
				if (type == 1){
					r = rand.nextInt(7);
				} else if(type == 2){
					r = rand.nextInt(35);
				} else if (type == 3){
					r = rand.nextInt(100);
				}
				// EMIT TRANSACTION
				Transaction tx = sendTransaction(to, from, currentChannel, r, l, fee);

				if (isRouted){
					routedTransactions.add(tx);
				}else {
					directTransactions.add(tx);
				}

			}while ((from.getBalance() > 0) || (currentChannel.getCapacity() > 0));


			if (currentChannel.getCapacity()< 1){

				// congestion counter +1
				congestion++;
				l.setCongestedChannels(congestion);
			}
		} else {
			t[0].stop();
		}
	}

	/**
	 * Method to get the routed transactions
	 * @return The list of routed transactions
	 */
	protected ArrayList<Transaction> getRoutedTransactions() {
		return routedTransactions;
	}

	/**
	 * Method to get the direct transactions
	 * @return The list of direct transactions
	 */
	protected ArrayList<Transaction> getDirectTransactions() {
		return directTransactions;
	}

	/**
	 * Recursive method to search the path for a destination node. If a path is found it returns the destination node.
	 * @param destinationNode destination node
	 * @param to Receiver node
	 * @param l Data fetcher object entity
	 * @return The destination node
	 */
	private Node searchPath(Node destinationNode, Node to, DataFetcher l) {
		boolean skip = false;
		boolean ocurrence = false;
		// create temporary variable to host a node
		Node temp = new Node();

		// if it is the first time accessing this recursive method
		// add this as a check
		// so when next times checking the same node it reports a failed path
		// to break the recursive loop
		if (hops>0){
			if (checkedPaths.contains(channel)){
				skip = true;
				invalidPath = true;
				failedTransactions.add(new Transaction(destinationNode, 0.0));
			}else {
				// add last channel used in the list to no stop infinite recursion
				checkedPaths.add(channel);
			}
		}

		// if the is the first time checking this node
		if (!invalidPath){

			// double check
			if (!skip){
				// for each channel of selected node
					for (Channel c : destinationNode.getChannels()) {

						// receiver node from path route
						temp = destinationNode.findNode(destinationNode, c.getTo(), l.getNodes());
						channel = l.findChannel(channel, destinationNode.getId(), temp.getId());

						// print the path
						System.out.println("PATH: Node " + c.getFrom() + " - Node " + c.getTo());

						// receiver node equal to destination
						if (c.getTo() == to.getId()) {
							destinationNode = destinationNode.findNode(destinationNode, c.getTo(), l.getNodes());
							ocurrence = true;
							fee = c.getFee();
							break;
						}
					}
				}

			// increase the hops counter
			hops++;

			//recursive call to keep searching the path
			if (!ocurrence)
				destinationNode = searchPath(temp, to, l);
			}

		// force the node to be the found node
		return destinationNode;
	}

	/**
	 * Method to get the list of failed transactions
	 * @return The list of failed transactions
	 */
	public ArrayList<Transaction> getFailedTransactions() {
		return failedTransactions;
	}

	/**
	 * Method to send a transaction and update receiver node balance, sender node balance and channel capacity
	 * @param to Receiver node
	 * @param from Sender node
	 * @param currentChannel Current channel used
	 * @param r Amount of the transaction recipient
	 * @param l Data fetcher object entity
	 * @param fee Chanenl fee
	 */
	private Transaction sendTransaction(Node to, Node from, Channel currentChannel, int r, DataFetcher l, double fee) {
		// Set node balance
		from.setBalance((from.getBalance() - r) - fee);
		to.setBalance(to.getBalance() + r);
		from = from.findNode(from, currentChannel.getFrom(), l.getNodes());
		from.setBalance(from.getBalance()+fee);

		// Set channel capacity
		currentChannel.setCapacity(currentChannel.getCapacity() - r);

		Transaction t = new Transaction(to, (double) r);
		// add transaction to the traffic buffer
		l.getTrafficGenerator().addTransaction(t);
		return t;
	}
}