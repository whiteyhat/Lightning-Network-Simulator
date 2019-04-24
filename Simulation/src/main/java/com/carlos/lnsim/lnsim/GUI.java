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

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.URI;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.Timer;
import com.mxgraph.model.mxGraphModel;
import javafx.application.Platform;
import javafx.embed.swing.JFXPanel;
import javafx.scene.Scene;
import javafx.scene.web.WebView;
import org.jfree.ui.RefineryUtilities;
import org.json.simple.parser.ParseException;
import org.w3c.dom.*;
import com.mxgraph.model.mxCell;
import com.mxgraph.swing.mxGraphComponent;
import com.mxgraph.util.mxDomUtils;
import com.mxgraph.view.mxGraph;
import org.w3c.dom.Node;

/**
 * Main Class to create the GUI object to run the Lightning Network Simulator
 */
public class GUI extends JFrame {
	private static final long serialVersionUID = -708317745824467773L;
	private DataFetcher dataFetcher;
	private ArrayList<Double> width, height;
	private String balance, size, channels;
	private boolean New;
	private TrafficGenerator trafficGenerator;
	private NetworkMapGenerator networkMapGenerator;
	private JLabel transactionLabel, feesLabel, hopsLabel, failedTransactionLabel, congestedChannelsLabel;
	private ArrayList<Channel> congestedChannels;
	private JMenuItem mi7, mi8, mi9, mi8i;

	/**
	 * Constructor to create a GUI object entity with all the required variables and initialization methods.
	 */
	public GUI() {
		super("Lightning Network Simulator");
		dataFetcher = new DataFetcher();
		trafficGenerator = new TrafficGenerator();
		networkMapGenerator = new NetworkMapGenerator();
		New = true;
		congestedChannels = new ArrayList<>();
		width = new ArrayList<>();
		height = new ArrayList<>();
		init();
	}

	/**
	 * Constructor to create a GUI object entity with an alternative parameter, all the required variables
	 * abnd initialization methods.
	 * @param dataFetcher Data fetcher object entity
	 */
	public GUI(DataFetcher dataFetcher) {
		super("Lightning Network Simulator");
		this.dataFetcher = dataFetcher;
		dataFetcher = new DataFetcher();
		New = true;
		width = new ArrayList<>();
		height = new ArrayList<>();
		congestedChannels = new ArrayList<>();
		init();
	}

	/**
	 * Method to initialise the GUI, the essential object entities and JavaFX resources.
	 */
	private void init() {
		mxGraphComponent graphComponent = null;
		JMenuBar bar = toolbarOptions(graphComponent);
		JFXPanel jfxPanel = new JFXPanel();
		getContentPane().add(jfxPanel);

		// Creation of scene and future interactions with JFXPanel
		// should take place on the JavaFX Application Thread
		Platform.runLater(() -> {
			WebView webView = new WebView();
			jfxPanel.setScene(new Scene(webView));
			webView.getEngine().load("https://github.com/whiteyhat/Lightning-Network-Simulation/wiki");
		});


		getContentPane().add(bar, BorderLayout.NORTH);
	}

	/**
	 * Method to draw the network in the simulation tool
	 * @param dataFetcher Data fetcher object entity
	 * @return The MxGraphComponent that contains the network
	 */
	private mxGraphComponent drawNetwork(DataFetcher dataFetcher) {
		Document doc = mxDomUtils.createDocument();

		mxGraph graph = createGraph();

		Object parent = graph.getDefaultParent();

		graph.getModel().beginUpdate();
		try {

			int i = -1;
			for (com.carlos.lnsim.lnsim.Node node : dataFetcher.getNodes()) {
				i++;
				if (New){
					width.add(i, Double.valueOf(ThreadLocalRandom.current().nextInt(2, 1500 + 1)));
					height.add(i, Double.valueOf(ThreadLocalRandom.current().nextInt(2, 780 + 1)));
				}

				Element element = doc.createElement("Node");
				element.setAttribute("ID", String.valueOf(node.getId()));
				element.setAttribute("balance", String.valueOf(node.getBalance()));


				if (node.getBalance() > 600) {
					graph.insertVertex(parent, String.valueOf(node.getId()), element, width.get(i), height.get(i), 80, 30,
							"ROUNDED;strokeColor=green;fillColor=green;fontColor=white");
				} else if (node.getBalance() >= 200 && node.getBalance() <= 599){
					graph.insertVertex(parent, String.valueOf(node.getId()), element, width.get(i), height.get(i), 80, 30,
							"ROUNDED;strokeColor=orange;fillColor=orange;fontColor=white");
				} else if (node.getBalance() >= 0 && node.getBalance() <= 199){
					graph.insertVertex(parent, String.valueOf(node.getId()), element, width.get(i), height.get(i), 80, 30,
							"ROUNDED;strokeColor=red;fillColor=red;fontColor=white");
				}

			}

			int j = dataFetcher.getNodes().size();
			for (Channel channel: dataFetcher.getChannels()) {
				j++;
				Element relation = doc.createElement("Channel");
				relation.setAttribute("capacity", String.valueOf(channel.getCapacity()));
				relation.setAttribute("fee", String.valueOf(channel.getFee()));


				mxCell fromCell = (mxCell) ((mxGraphModel)graph.getModel()).getCell(String.valueOf(channel.getFrom()+1));
				mxCell toCell = (mxCell) ((mxGraphModel)graph.getModel()).getCell(String.valueOf(channel.getTo()+1));

				if (channel.getCapacity() > 100.0){
					graph.insertEdge(parent, String.valueOf(j), relation, fromCell, toCell, "ROUNDED;strokeColor=green;strokeWidth=7");
				} else if ((channel.getCapacity() >= 80.0)&& (channel.getCapacity() <= 99.9)){
					graph.insertEdge(parent, String.valueOf(j), relation, fromCell, toCell, "ROUNDED;strokeColor=green;strokeWidth=6");
				} else if ((channel.getCapacity() >= 70.0)&& (channel.getCapacity() <= 79.9)){
					graph.insertEdge(parent, String.valueOf(j), relation, fromCell, toCell, "ROUNDED;strokeColor=green;strokeWidth=5");
				} else if ((channel.getCapacity() >= 60.0)&& (channel.getCapacity() <= 69.9)){
					graph.insertEdge(parent, String.valueOf(j), relation, fromCell, toCell, "ROUNDED;strokeColor=orange;strokeWidth=4");
				} else if ((channel.getCapacity() >= 40.0)&& (channel.getCapacity() <= 59.9)){
					graph.insertEdge(parent, String.valueOf(j), relation, fromCell, toCell, "ROUNDED;strokeColor=orange;strokeWidth=3");
				} else if ((channel.getCapacity() >= 20.0)&& (channel.getCapacity() <= 39.9)){
					graph.insertEdge(parent, String.valueOf(j), relation, fromCell, toCell, "ROUNDED;strokeColor=orange;strokeWidth=2");
				} else if ((channel.getCapacity() >= 5.0)&& (channel.getCapacity() <= 19.9)){
					graph.insertEdge(parent, String.valueOf(j), relation, fromCell, toCell, "ROUNDED;strokeColor=red;strokeWidth=1");
				}
				//System.out.println(channel);

			}
		}
		finally
		{
			graph.getModel().endUpdate();
		}

		return drawNetworkInputs(graph);
	}

	/**
	 * Method to draw the simulation inputs into the graph
	 * @param graph network component where the simulation is
	 * @return The MxGraphComponent that contains the network
	 */
	private mxGraphComponent drawNetworkInputs(mxGraph graph) {
		// Overrides method to create the editing value
		mxGraphComponent graphComponent = new mxGraphComponent(graph)
		{
			private static final long serialVersionUID = 6824440535661529806L;

			public String getEditingValue(Object cell, EventObject trigger)
			{
				if (cell instanceof mxCell)
				{
					Object value = ((mxCell) cell).getValue();

					if (value instanceof Element)
					{
						Element elt = (Element) value;

						if (elt.getTagName().equalsIgnoreCase("Node"))
						{
							String firstName = elt.getAttribute("ID");
							String lastName = elt.getAttribute("balance");

							return firstName + " " + lastName;
						}
					}
				}

				return super.getEditingValue(cell, trigger);
			};

		};

		// Stops editing after enter has been pressed instead
		// of adding a newline to the current editing value
		graphComponent.setEnterStopsCellEditing(true);
		return graphComponent;
	}

	/**
	 * Method to create the graph component which contains the network
	 * @return The network graph
	 */
	private mxGraph createGraph() {
		return new mxGraph()
			{
				// Overrides method to disallow edge label editing
				public boolean isCellEditable(Object cell)
				{
					return !getModel().isEdge(cell);
				}

				// Overrides method to provide a cell label in the display
				public String convertValueToString(Object cell)
				{
					if (cell instanceof mxCell)
					{
						Object value = ((mxCell) cell).getValue();

						if (value instanceof Element)
						{
							Element elt = (Element) value;

							if (elt.getTagName().equalsIgnoreCase("Node"))
							{
								String firstName = elt.getAttribute("ID");
								String lastName = elt.getAttribute("balance");


								return "ID: " + firstName + "\n Balance: " + lastName;
							}
							else if (elt.getTagName().equalsIgnoreCase("Channel"))
							{
								return " Capacity: "
										+ elt.getAttribute("capacity") + " \nFee: " + elt.getAttribute("fee");
							}

						}
					}

					return super.convertValueToString(cell);
				}

				// Overrides method to store a cell label in the model
				public void cellLabelChanged(Object cell, Object newValue,
						boolean autoSize)
				{
					if (cell instanceof mxCell && newValue != null)
					{
						Object value = ((mxCell) cell).getValue();

						if (value instanceof Node)
						{
							String label = newValue.toString();
							Element elt = (Element) value;

							if (elt.getTagName().equalsIgnoreCase("Node"))
							{
								int pos = label.indexOf(' ');

								String firstName = (pos > 0) ? label.substring(0,
										pos).trim() : label;
								String lastName = (pos > 0) ? label.substring(
										pos + 1, label.length()).trim() : "";

								// Clones the value for correct undo/redo
								elt = (Element) elt.cloneNode(true);

								elt.setAttribute("ID", firstName);
								elt.setAttribute("balance", lastName);

								newValue = elt;
							}
						}
					}

					super.cellLabelChanged(cell, newValue, autoSize);
				}
			};
	}

	/**
	 * Method to update the simulation tool
	 */
	private void updateGraph() {
		mxGraphComponent graphComponent = drawNetwork(dataFetcher);
		JMenuBar bar = toolbarOptions(graphComponent);

		getContentPane().removeAll();
		getContentPane().add(graphComponent, BorderLayout.CENTER);
		getContentPane().add(bar,BorderLayout.NORTH);
		// refresh the GUI
		SwingUtilities.updateComponentTreeUI(GUI.super.rootPane);
	}

	/**
	 * Method that draw the toolbar options and create the event listeners/
	 * @param graphComponent MxGraphComponent that contains the network
	 * @return The JMenubar that contains the toolbar options
	 */
	private JMenuBar toolbarOptions(mxGraphComponent graphComponent) {
		setLayout(new BorderLayout());
		JMenuBar bar = new JMenuBar();
		JMenu m1 = new JMenu("File", true);
		JMenuItem mi1 = new JMenuItem("New");

		JMenuItem mi2 = new JMenuItem("Load");
		m1.add(mi1);
		m1.add(mi2);
		// add 4 items to menu 2
		JMenu m2 = new JMenu("Edit", true);
		JMenu editBalances = new JMenu("Set Balances", true);
		JMenu editCapacities = new JMenu("Set Capacities", true);
		JMenu setRouting = new JMenu("Set Routing", true);

		JMenuItem mi4 = new JMenuItem("Start");
		mi4.setEnabled(true);
		m2.add(mi4);
		mi2.addActionListener(new ActionListener() {
			@Override public void actionPerformed(ActionEvent e) {
				chooseFile(mi2, mi4);
				mi4.setEnabled(true);
			}
		});

		JMenuItem lowBalances = new JMenuItem("Low");
		JMenuItem mediumBalances = new JMenuItem("Medium");
		JMenuItem highBalances = new JMenuItem("High");

		editBalances.add(lowBalances);
		editBalances.add(mediumBalances);
		editBalances.add(highBalances);

		JMenuItem lowCapacity = new JMenuItem("Low");
		JMenuItem mediumCapacity = new JMenuItem("Medium");
		JMenuItem highCapacity = new JMenuItem("High");

		editCapacities.add(lowCapacity);
		editCapacities.add(mediumCapacity);
		editCapacities.add(highCapacity);

		JCheckBoxMenuItem shortestPath = new JCheckBoxMenuItem("Shortest Path", true);
		JMenuItem ANT = new JMenuItem("ANT");
		ANT.setEnabled(false);
		JMenuItem AMP = new JMenuItem("AMP");
		AMP.setEnabled(false);
		setRouting.add(shortestPath);
		setRouting.add(ANT);
		setRouting.add(AMP);

		m2.addSeparator();
		m2.add(editBalances);
		m2.add(editCapacities);
		m2.add(setRouting);

		lowBalances.addActionListener(new ActionListener() {
			@Override public void actionPerformed(ActionEvent e) {
				setBalances(0, 199);
			}

		});

		mediumBalances.addActionListener(new ActionListener() {
			@Override public void actionPerformed(ActionEvent e) {
				setBalances(200, 599);

			}
		});

		highBalances.addActionListener(new ActionListener() {
			@Override public void actionPerformed(ActionEvent e) {
				setBalances(600, 1000);

			}
		});

		lowCapacity.addActionListener(new ActionListener() {
			@Override public void actionPerformed(ActionEvent e) {
				setCapacities(0, 29);
			}
		});

		mediumCapacity.addActionListener(new ActionListener() {
			@Override public void actionPerformed(ActionEvent e) {
				setCapacities(30, 69);
			}
		});

		highCapacity.addActionListener(new ActionListener() {
			@Override public void actionPerformed(ActionEvent e) {
				setCapacities(69, 500);
			}
		});

		JMenu m3 = new JMenu("Analyze", true);

		mi7 = new JMenuItem("Channels");
		m3.add(mi7);
		mi8i = new JMenuItem("Transactions");
		m3.add(mi8i);
		mi8 = new JMenuItem("Nodes");
		m3.add(mi8);
		mi9 = new JMenuItem("Routes");
		m3.add(mi9);
		mi7.setEnabled(false);
		mi8.setEnabled(false);
		mi8i.setEnabled(false);
		mi9.setEnabled(false);

		mi7.addActionListener(new ActionListener() {
			@Override public void actionPerformed(ActionEvent e) {
				ArrayList<Channel> low = new ArrayList<>();
				ArrayList<Channel> medium = new ArrayList<>();
				ArrayList<Channel> high = new ArrayList<>();

				for (Channel c : dataFetcher.getChannels()) {
					if ((c.getCapacity() >= 5.0)&& (c.getCapacity() <= 19.9)){
						low.add(c);
					} else if ((c.getCapacity() >= 20.0)&& (c.getCapacity() <= 69.9)){
						medium.add(c);
					} else  if (c.getCapacity() >= 60.0){
						high.add(c);
					}
				}
				PieResults demo1 = new PieResults( 5, high.size(), congestedChannels.size());
				demo1.setMediumValue(medium.size());
				demo1.setLowchannel(low.size());
				demo1.init();
				demo1.setSize( 560 , 367 );
				demo1.setVisible( true );
				demo1.setLocation(400, 500);


				PieResults demo = new PieResults( 1, dataFetcher.getChannels().size(), congestedChannels.size());
				demo.init();
				demo.setSize( 560 , 367 );
				RefineryUtilities.centerFrameOnScreen( demo );
				demo.setVisible( true );
			}
		});

		mi8.addActionListener(new ActionListener() {
			@Override public void actionPerformed(ActionEvent e) {
				ArrayList<com.carlos.lnsim.lnsim.Node> medium = new ArrayList<>();
				ArrayList<com.carlos.lnsim.lnsim.Node> high = new ArrayList<>();
				ArrayList<com.carlos.lnsim.lnsim.Node> low = new ArrayList<>();

				for (com.carlos.lnsim.lnsim.Node n : dataFetcher.getNodes()) {
					if (n.getBalance() >= 200 && n.getBalance() <= 599){
						medium.add(n);
					}else if (n.getBalance() > 600){
						high.add(n);
					}else if (n.getBalance() >= 0 && n.getBalance() <= 199){
						low.add(n);
					}
				}
				PieResults demo = new PieResults( 2, high.size(), low.size());
				demo.setMediumValue(medium.size());
				demo.init();
				demo.setSize( 560 , 367 );
				RefineryUtilities.centerFrameOnScreen( demo );
				demo.setVisible( true );
			}
		});

		mi8i.addActionListener(new ActionListener() {
			@Override public void actionPerformed(ActionEvent e) {
				PieResults demo = new PieResults( 3, dataFetcher.getTrafficGenerator().getTransactions().size(), dataFetcher
						.getFailedTransactions());
				demo.init();
				demo.setSize( 560 , 367 );
				RefineryUtilities.centerFrameOnScreen( demo );
				demo.setVisible( true );

				PieResults demo1 = new PieResults( 4, trafficGenerator.getRoutedTransactions(), trafficGenerator.getDirectTransactions());
				demo1.init();
				demo1.setSize( 560 , 367 );
				demo1.setVisible( true );
				demo1.setLocation(400, 500);
			}
		});

		JMenu m4 = new JMenu("Help", true);

		JMenuItem mi10 = new JMenuItem("Getting Started");
		m4.add(mi10);
		JMenuItem mi11 = new JMenuItem("What's Lightning Network");
		m4.add(mi11);
		JMenuItem mi12 = new JMenuItem("Submit a bug report");
		m4.add(mi12);
		JMenuItem mi13 = new JMenuItem("Submit feedback");
		m4.add(mi13);

		// add two menus to bar
		bar.add(m1);
		bar.add(m2);
		bar.add(m3);
		bar.add(m4);

		final Timer[] timer = { null };
		final JProgressBar[] pbar = { null };
		final JPanel[] panel1 = new JPanel[1];
		final JPanel[] panel = new JPanel[1];

		final PlaceHolderTextField[] nodesSize = new PlaceHolderTextField[1];
		final PlaceHolderTextField[] channelsSize = new PlaceHolderTextField[1];

		JButton start = new JButton("Start");
		start.addActionListener(new ActionListener() {
			@Override public void actionPerformed(ActionEvent e) {
				chargeSimulation(graphComponent);
				networkMapGenerator.setNetworkSize(Integer.parseInt(nodesSize[0].getText()));
				networkMapGenerator.setChannelsPerNode(Integer.parseInt(channelsSize[0].getText()));
				networkMapGenerator.setDataFetcher(dataFetcher);
				networkMapGenerator.createNetworkMap();
				restartSim(mi4, "src/main/resources/config/custom.json");
			}

			private void chargeSimulation(mxGraphComponent graphComponent) {
				graphComponent = drawNetwork(dataFetcher);
				System.out.println();
			}
		});


		mi10.addActionListener(new ActionListener() {
			@Override public void actionPerformed(ActionEvent e) {
				showWebsite("https://github.com/whiteyhat/Lightning-Network-Simulation/wiki");
			}
		});

		mi11.addActionListener(new ActionListener() {
			@Override public void actionPerformed(ActionEvent e) {
				showWebsite("https://lightning.network/lightning-network-summary.pdf");
			}
		});

		mi12.addActionListener(new ActionListener() {
			@Override public void actionPerformed(ActionEvent e) {
				showWebsite("https://github.com/whiteyhat/Lightning-Network-Simulation/issues");
			}
		});

		mi13.addActionListener(new ActionListener() {
			@Override public void actionPerformed(ActionEvent e) {
				showWebsite("mailto:car23@aber.ac.uk?cc=alg25@aber.ac.uk&subject=FEEDBACK-Lightning%20Network%20Simulator&");

			}
		});
		mi1.addActionListener(new ActionListener() {
			@Override public void actionPerformed(ActionEvent e) {
				getContentPane().removeAll();
				getContentPane().add(bar,BorderLayout.NORTH);
				// refresh the GUI

				panel[0] = new JPanel();

				panel[0].add(new JLabel("Amount of Nodes: "));
				nodesSize[0] = new PlaceHolderTextField();
				nodesSize[0].setPlaceholder("40");
				panel[0].add(nodesSize[0]);

				panel[0].add(new JLabel("Amount of Channels per Node: "));
				channelsSize[0] = new PlaceHolderTextField();
				channelsSize[0].setPlaceholder("3");
				panel[0].add(channelsSize[0]);
				panel[0].setLayout(new GridLayout(15,2));
				getContentPane().add(start, BorderLayout.SOUTH);
				getContentPane().add(panel[0], BorderLayout.CENTER);
				SwingUtilities.updateComponentTreeUI(GUI.super.rootPane);

			}
		});

		mi4.addActionListener(new ActionListener() {
			@Override public void actionPerformed(ActionEvent e) {
				stressTest(panel1, pbar, timer);
			}
		});

		return bar;
	}

	/**
	 * Method to generate a simulates transaction stress test
	 * @param panel1 GUI panel where the simulation results are displayed
	 * @param pbar Progress bar that shows the progress of the simulation
	 * @param timer Timer that counts the simulation time
	 */
	private void stressTest(JPanel[] panel1, JProgressBar[] pbar, Timer[] timer) {
		transactionLabel = new JLabel();
		feesLabel = new JLabel();
		hopsLabel = new JLabel();
		failedTransactionLabel = new JLabel();
		congestedChannelsLabel = new JLabel();
		drawSimulationResults(panel1, pbar, transactionLabel, feesLabel);
		final int[] value = { 0 };
		int recipient = 1;

			// Stress test based on multi-hop transactions
			Thread t = new Thread(new Runnable() {
				public void run() {
					shortestPathRouting(recipient, timer);

				}
			});

			t.start();

		ActionListener actionListener = new ActionListener() {
			@Override public void actionPerformed(ActionEvent e) {
//								transactionLabel.setText(String.valueOf(trafficGenerator.trafficSize()));
				pbar[0].setValue(value[0]++);
			}
		};
		timer[0] = new Timer(100, actionListener);
		timer[0].start();
	}

	/**
	 * Method to find the shortest path to create the transactions in the simulation
	 * @param recipient transaction recipient
	 * @param timer Timer that counts the simulation time
	 */
	private void shortestPathRouting(int recipient, Timer[] timer) {
		for (com.carlos.lnsim.lnsim.Node node : dataFetcher.getNodes()) {
			com.carlos.lnsim.lnsim.Node to = new com.carlos.lnsim.lnsim.Node();
			Channel currentChannel = null;
			if (!node.getChannels().isEmpty()){

				trafficGenerator.routingMechanism(to, node, currentChannel, recipient, dataFetcher, timer );

				dataFetcher.setHops(trafficGenerator.getStaticHops());
				dataFetcher.setFailedTransactions(trafficGenerator.getFailedTransactions().size());
				dataFetcher.setTrafficGenerator(trafficGenerator);

				transactionLabel.setText(String.valueOf(trafficGenerator.trafficSize()));
				feesLabel.setText(String.valueOf(trafficGenerator.getFeeCounter()));
				hopsLabel.setText(String.valueOf(dataFetcher.getHops()));
				failedTransactionLabel.setText(String.valueOf(dataFetcher.getFailedTransactions()));

				networkMapGenerator.setSimulationCompleted(true);
				networkMapGenerator.createNetworkMap();
				//transactionLabel.setText(String.valueOf(transactions));
				updateGUI();
			}
		}
		for (Channel c : dataFetcher.getChannels()) {
			if (c.getCapacity() == 0){
				congestedChannels.add(c);
			}
		}
		dataFetcher.setCongestedChannels(congestedChannels.size());
		congestedChannelsLabel.setText(String.valueOf(congestedChannels.size()));

		mi7.setEnabled(true);
		mi8.setEnabled(true);
		mi8i.setEnabled(true);
		mi9.setEnabled(true);
	}

	/**
	 * Method to set the channel capacities
	 * @param i start minimum range
	 * @param j end minimum range
	 */
	public void setCapacities(int i, int j) {
		for (Channel c : dataFetcher.getChannels()) {
			c.setCapacity((double) ThreadLocalRandom.current().nextInt(i, j + 1));
		}
		New = false;
		updateGraph();
		New = true;
	}

	/**
	 * Method to set the channel capacities
	 * @param i start minimum range
	 * @param j end minimum range
	 */
	public void setBalances(int i, int j) {
		for (com.carlos.lnsim.lnsim.Node node : dataFetcher.getNodes()) {
			node.setBalance(ThreadLocalRandom.current().nextDouble(i, j + 1));
		}
		New = false;
		updateGraph();
		New = true;
	}

	/**
	 * Method to draw the simulation results in the GUI
	 * @param panel1 GUI panel where the simulation results are displayed
	 * @param pbar Progress bar that shows the progress of the simulation
	 * @param transactionLabel Transaction label where contains the text of the emitted transactions
	 * @param feesLabel Transaction label where contains the text of the simulation fees
	 */
	private void drawSimulationResults(JPanel[] panel1, JProgressBar[] pbar, JLabel transactionLabel, JLabel feesLabel) {
		SwingUtilities.updateComponentTreeUI(GUI.super.rootPane);
		channels = String.valueOf(dataFetcher.getChannels().size());
		size = String.valueOf(dataFetcher.getNodes().size());
		Double amount = 0.00;
		for (com.carlos.lnsim.lnsim.Node node: dataFetcher.getNodes()) {
			amount += node.getBalance();
		}
		balance = String.valueOf(amount);
		Font font = new Font(" Courier", Font.BOLD, 12);
		panel1[0] = new JPanel();
		panel1[0].add(new JLabel(" Transactions: ")).setFont(font);
		panel1[0].add(transactionLabel);
		panel1[0].add(new JLabel(" Failed Transactions: ")).setFont(font);
		panel1[0].add(failedTransactionLabel);
		panel1[0].add(new JLabel("Hops: ")).setFont(font);
		panel1[0].add(hopsLabel);
		panel1[0].add(new JLabel(" Fees: ")).setFont(font);
		panel1[0].add(feesLabel);
		panel1[0].add(new JLabel(" Channels: ")).setFont(font);
		panel1[0].add(new JLabel(channels));
		panel1[0].add(new JLabel(" Congested Channels: ")).setFont(font);
		panel1[0].add(congestedChannelsLabel);
		panel1[0].add(new JLabel(" Network Balance: ")).setFont(font);
		panel1[0].add(new JLabel(balance));
		panel1[0].add(new JLabel(" Network Size: ")).setFont(font);
		panel1[0].add(new JLabel(size));
		panel1[0].setLayout(new GridLayout(0,2));
		getContentPane().add(panel1[0], BorderLayout.EAST);

		pbar[0] = new JProgressBar();
		pbar[0].setMinimum(0);
		pbar[0].setMaximum(3000);
		getContentPane().add(pbar[0], BorderLayout.SOUTH);
	}

	/**
	 * Method to open the default web browser with a specific URL
	 * @param s String that contains the desired URL to open in the default website
	 */
	private void showWebsite(String s) {
		try {
			Desktop.getDesktop().browse(URI.create(s));
		} catch (IOException e1) {
			e1.printStackTrace();
		}
	}

	/**
	 * Method to update the entire GUI
	 */
	public void updateGUI() {
		// some code
		New = false;
		mxGraphComponent graphComponent = drawNetwork(dataFetcher);
		JMenuBar bar = toolbarOptions(graphComponent);

		getContentPane().removeAll();
		getContentPane().add(graphComponent, BorderLayout.CENTER);
		getContentPane().add(bar,BorderLayout.NORTH);
		// refresh the GUI
		SwingUtilities.updateComponentTreeUI(GUI.super.rootPane);
		//SwingUtilities.updateComponentTreeUI(graphComponent);


		final JProgressBar[] pbar = { null };
		final JPanel[] panel1 = new JPanel[1];
		drawSimulationResults(panel1, pbar, transactionLabel, feesLabel);
	}

	/**
	 * Method to restart the simulation
	 * @param mi4 start button
	 * @param s network map file path
	 */
	public void restartSim(JMenuItem mi4, String s) {
		dataFetcher.setReadyToLoad(true);
		dataFetcher.setPath(s);
		try {
			dataFetcher.Load();
		} catch (IOException ex) {
			ex.printStackTrace();
		} catch (ParseException ex) {
			ex.printStackTrace();
		}

		mxGraphComponent graphComponent = drawNetwork(dataFetcher);
		JMenuBar bar = toolbarOptions(graphComponent);
		startSim(graphComponent, bar);
		mi4.setEnabled(true);
	}

	/**
	 * Method to start the simulation
	 * @param graphComponent MxGraphComponent that contains the network
	 * @param bar Simulation toolbar options
	 */
	private void startSim(mxGraphComponent graphComponent, JMenuBar bar) {
		// add graph
		getContentPane().removeAll();
		getContentPane().add(graphComponent, BorderLayout.CENTER);
		getContentPane().add(bar,BorderLayout.NORTH);
		// refresh the GUI
		SwingUtilities.updateComponentTreeUI(GUI.super.rootPane);
	}

	/**
	 * Method to assist the loading process. It opens a native file chooser to allow the selection of a network map.
	 * @param mi2 Load button
	 * @param mi4 Start button
	 */
	private void chooseFile(JMenuItem mi2, JMenuItem mi4) {
		JFileChooser chooser = new JFileChooser("src/main/resources/config");
		FileNameExtensionFilter filter = new FileNameExtensionFilter(
				".json files", "json");
		chooser.setFileFilter(filter);
		int returnVal = chooser.showOpenDialog(mi2);
		if(returnVal == JFileChooser.APPROVE_OPTION) {
			restartSim(mi4, chooser.getSelectedFile().getPath());

		}
	}

	/**
	 * Execution method that runs the Lightning Network simulator tool
	 * @param args args to run in the execution
	 */
	public static void main(String[] args){
		GUI frame = new GUI();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
		frame.setUndecorated(true);
		frame.setVisible(true);
	}
}