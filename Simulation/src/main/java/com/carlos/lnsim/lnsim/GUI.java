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
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;
import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.Timer;
import com.mxgraph.model.mxGraphModel;
import org.jfree.ui.RefineryUtilities;
import org.json.simple.parser.ParseException;
import org.w3c.dom.*;
import com.mxgraph.model.mxCell;
import com.mxgraph.swing.mxGraphComponent;
import com.mxgraph.util.mxDomUtils;
import com.mxgraph.view.mxGraph;
import org.w3c.dom.Node;

public class GUI extends JFrame {
	private static final long serialVersionUID = -708317745824467773L;
	private Load load;
	private ArrayList<Double> width, height;
	private String tx, balance, size, channels;
	private mxGraphComponent graphComponent;
	private boolean New;
	NetworkMapGenerator networkMapGenerator;
	private JLabel transactionLabel, feesLabel, hopsLabel, failedTransactionLabel, congestedChannels;
	private ArrayList<Integer> transactionsBuffer;

	public GUI() {
		super("Lightning Network Simulator");

		load = new Load();
		networkMapGenerator = new NetworkMapGenerator();
		New = true;
		width = new ArrayList<>();
		height = new ArrayList<>();
		init();
	}

	public GUI(Load load) {
		super("Lightning Network Simulator");

		this.load = load;
		load = new Load();
		New = true;
		width = new ArrayList<>();
		height = new ArrayList<>();
		init();
	}

	private void init() {
		JLabel label;
		JMenuBar bar = drawMenuBar(graphComponent);

		File file = new File("src/main/resources/start.png");
		BufferedImage image = null;
		try {
			image = ImageIO.read(file);
		} catch (IOException e) {
			e.printStackTrace();
		}

		label = new JLabel(new ImageIcon(image));
		getContentPane().add(label);
		getContentPane().add(bar, BorderLayout.NORTH);
	}

	private mxGraphComponent drawSimulation(Load load) {
		Document doc = mxDomUtils.createDocument();

		mxGraph graph = createGraph();

		Object parent = graph.getDefaultParent();

		graph.getModel().beginUpdate();
		try {

			int i = -1;
			for (com.carlos.lnsim.lnsim.Node node : load.getNodes()) {
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

			int j = load.getNodes().size();
			for (Channel channel: load.getChannels()) {
				j++;
				Element relation = doc.createElement("Channel");
				relation.setAttribute("capacity", String.valueOf(channel.getCapacity()));
				relation.setAttribute("fee", String.valueOf(channel.getFee()));


				mxCell fromCell = (mxCell) ((mxGraphModel)graph.getModel()).getCell(String.valueOf(channel.getFrom()+1));
				mxCell toCell = (mxCell) ((mxGraphModel)graph.getModel()).getCell(String.valueOf(channel.getTo()+1));

				if (channel.getCapacity() > 80.0){
					graph.insertEdge(parent, String.valueOf(j), relation, fromCell, toCell, "ROUNDED;strokeColor=green;strokeWidth=7");
				} else if ((channel.getCapacity() >= 70.0)&& (channel.getCapacity() <= 80.0)){
					graph.insertEdge(parent, String.valueOf(j), relation, fromCell, toCell, "ROUNDED;strokeColor=green;strokeWidth=6");
				} else if ((channel.getCapacity() >= 60.0)&& (channel.getCapacity() <= 79.9)){
					graph.insertEdge(parent, String.valueOf(j), relation, fromCell, toCell, "ROUNDED;strokeColor=green;strokeWidth=5");
				} else if ((channel.getCapacity() >= 50.0)&& (channel.getCapacity() <= 69.9)){
					graph.insertEdge(parent, String.valueOf(j), relation, fromCell, toCell, "ROUNDED;strokeColor=orange;strokeWidth=4");
				} else if ((channel.getCapacity() >= 40.0)&& (channel.getCapacity() <= 59.9)){
					graph.insertEdge(parent, String.valueOf(j), relation, fromCell, toCell, "ROUNDED;strokeColor=orange;strokeWidth=3");
				} else if ((channel.getCapacity() >= 30.0)&& (channel.getCapacity() <= 49.9)){
					graph.insertEdge(parent, String.valueOf(j), relation, fromCell, toCell, "ROUNDED;strokeColor=orange;strokeWidth=2");
				} else if ((channel.getCapacity() >= 20.0)&& (channel.getCapacity() <= 39.9)){
					graph.insertEdge(parent, String.valueOf(j), relation, fromCell, toCell, "ROUNDED;strokeColor=red;strokeWidth=1");
				}


			}
		}
		finally
		{
			graph.getModel().endUpdate();
		}

		return editValue(graph);
	}

	private mxGraphComponent editValue(mxGraph graph) {
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

	private void updateGraph() {
		mxGraphComponent graphComponent = drawSimulation(load);
		JMenuBar bar = drawMenuBar(graphComponent);

		getContentPane().removeAll();
		getContentPane().add(graphComponent, BorderLayout.CENTER);
		getContentPane().add(bar,BorderLayout.NORTH);
		// refresh the GUI
		SwingUtilities.updateComponentTreeUI(GUI.super.rootPane);
	}

	private JMenuBar drawMenuBar(mxGraphComponent graphComponent) {

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

		JMenuItem shortestPath = new JMenuItem("Shortest Path");
		JMenuItem ANT = new JMenuItem("ANT");
		JMenuItem AMP = new JMenuItem("AMP");

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

		JMenuItem mi7 = new JMenuItem("Channels");
		m3.add(mi7);
		JMenuItem mi8 = new JMenuItem("Nodes");
		m3.add(mi8);
		JMenuItem mi9 = new JMenuItem("Routes");
		m3.add(mi9);

		mi7.addActionListener(new ActionListener() {
			@Override public void actionPerformed(ActionEvent e) {
				PieResults demo = new PieResults( "Lightning Network Simulator", Double.parseDouble(channels), Double.parseDouble(congestedChannels.getText()));
				demo.setSize( 560 , 367 );
				demo.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
				RefineryUtilities.centerFrameOnScreen( demo );
				demo.setVisible( true );
			}
		});

		mi8.addActionListener(new ActionListener() {
			@Override public void actionPerformed(ActionEvent e) {
				LinearResults chart = new LinearResults(
						"Lightning Network Simulator" ,
						"Node Analysis", transactionsBuffer);

				chart.pack( );
				RefineryUtilities.centerFrameOnScreen( chart );
				chart.setVisible( true );
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
				networkMapGenerator.setNodeSize(Integer.parseInt(nodesSize[0].getText()));
				networkMapGenerator.setChannelsPerNode(Integer.parseInt(channelsSize[0].getText()));
				networkMapGenerator.createNetwork();
				restartSim(mi4, "src/main/resources/config/custom.json");
			}

			private void chargeSimulation(mxGraphComponent graphComponent) {
				graphComponent = drawSimulation(load);
				//networkMapGenerator = new NetworkMapGenerator(load.getNodes().size(), load.getChannels().size());
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
//				showWebsite(
//						"mailto:car23@aber.ac.uk?cc=alg25@aber.ac.uk&subject=FEEDBACK - Lightning Network Simulator&");

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

	private void stressTest(JPanel[] panel1, JProgressBar[] pbar, Timer[] timer) {
		boolean routing = true;
		transactionsBuffer = new ArrayList<>();
		transactionLabel = new JLabel();
		feesLabel = new JLabel();
		hopsLabel = new JLabel();
		failedTransactionLabel = new JLabel();
		congestedChannels = new JLabel();
		resultsBar(panel1, pbar, transactionLabel, feesLabel);
		final int[] value = { 0 };
		int recipient = 1;
		if (!routing){
			// Stress test based on 1 hop transaction
			Thread t = new Thread(new Runnable() {
				public void run() {
					shortestPathRoutin1Hop(recipient, timer);
				}
			});

			t.start();
		}else {
			// Stress test based on multi-hop transactions
			Thread t = new Thread(new Runnable() {
				public void run() {
					shortestPathRouting(recipient, timer);
				}
			});

			t.start();
		}

		ActionListener actionListener = new ActionListener() {
			@Override public void actionPerformed(ActionEvent e) {
//								transactionLabel.setText(String.valueOf(trafficGenerator.trafficSize()));
				pbar[0].setValue(value[0]++);
			}
		};
		timer[0] = new Timer(100, actionListener);
		timer[0].start();
	}

	private void shortestPathRoutin1Hop(int recipient, Timer[] timer) {
		int transactions = 0;
		double feeCounter = 0;
		for (com.carlos.lnsim.lnsim.Node node : load.getNodes()) {
			com.carlos.lnsim.lnsim.Node to = new com.carlos.lnsim.lnsim.Node();
			Channel currentChannel = null;
			double fee = 0;
			if (!node.getChannels().isEmpty()){
				for (Channel channel : node.getChannels()) {
					to = to.findNode(to, channel.getTo(), load.getNodes());
					currentChannel = channel;
					fee = channel.getFee();
					feeCounter += channel.getFee();
					feesLabel.setText(String.valueOf(feeCounter));
				}
				if ((node.getBalance() > 0) || (currentChannel.getCapacity() > 0)){
					do {
						node.setBalance((node.getBalance() - recipient) - (int)fee);
						to.setBalance(to.getBalance() + recipient);
						currentChannel.setCapacity(currentChannel.getCapacity() - recipient);

					}while ((node.getBalance() > 0) || (currentChannel.getCapacity() > 0));
				} else {
					timer[0].stop();
				}


				if (node.getBalance() < 0){
					node.setBalance(0);
				}
				transactionLabel.setText(String.valueOf(transactions));
				updateGUI();
			}
		}
	}

	private void shortestPathRouting(int recipient, Timer[] timer) {
		double feeCounter = 0;
		int hops = 0;
		int transactions = 0;
		int congestion = 0;
		Random rand = new Random();

		for (com.carlos.lnsim.lnsim.Node node : load.getNodes()) {
			com.carlos.lnsim.lnsim.Node to = new com.carlos.lnsim.lnsim.Node();
			Channel currentChannel = null;
			double fee = 0;
			if (!node.getChannels().isEmpty()){
				for (Channel channel : node.getChannels()) {
					to = to.findNode(to, rand.nextInt(load.getNodes().size()), load.getNodes());
					currentChannel = channel;
					fee = channel.getFee();
					feeCounter += channel.getFee();
					feesLabel.setText(String.valueOf(feeCounter));
					transactions++;

					if (load.getRoutingTable().get(node).getId() == to.getId()){
						System.out.println("Direct Link: Node " + node.getId() + " - Node " + load.getRoutingTable().get(node).getId());

					}else{
						hops++;
						System.out.println("Link: Node " + node.getId() + " - Node " + load.getRoutingTable().get(node).getId());
						hopsLabel.setText(String.valueOf(hops));
					}

				}


				if ((node.getBalance() > 0) || (currentChannel.getCapacity() > 0)){
					do {
						node.setBalance((node.getBalance() - recipient) - (int)fee);
						to.setBalance(to.getBalance() + recipient);
						currentChannel.setCapacity(currentChannel.getCapacity() - recipient);
						transactions++;
						transactionsBuffer.add(transactions);

					}while ((node.getBalance() > 0) || (currentChannel.getCapacity() > 0));
					if (currentChannel.getCapacity()< 1){
						congestion++;
						congestedChannels.setText(String.valueOf(congestion));
					}
				} else {
					timer[0].stop();
				}


				if (node.getBalance() < 0){
					node.setBalance(0);
				}

				networkMapGenerator.setSimulationCompleted(true);
				networkMapGenerator.createNetwork();
				transactionLabel.setText(String.valueOf(transactions));
				updateGUI();
			}
		}
	}

	public void setCapacities(int i, int j) {
		for (Channel c : load.getChannels()) {
			c.setCapacity((double) ThreadLocalRandom.current().nextInt(i, j + 1));
		}
		New = false;
		updateGraph();
		New = true;
	}

	public void setBalances(int i, int j) {
		for (com.carlos.lnsim.lnsim.Node node : load.getNodes()) {
			node.setBalance(ThreadLocalRandom.current().nextInt(i, j + 1));
		}
		New = false;
		updateGraph();
		New = true;
	}

	private void resultsBar(JPanel[] panel1, JProgressBar[] pbar, JLabel transactionLabel, JLabel feesLabel) {
		SwingUtilities.updateComponentTreeUI(GUI.super.rootPane);
		channels = String.valueOf(load.getChannels().size());
		size = String.valueOf(load.getNodes().size());
		tx = String.valueOf(load.getTransactions().size());
		Double amount = 0.00;
		for (com.carlos.lnsim.lnsim.Node node: load.getNodes()) {
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
		panel1[0].add(congestedChannels);
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

	private void showWebsite(String s) {
		try {
			Desktop.getDesktop().browse(URI.create(s));
		} catch (IOException e1) {
			e1.printStackTrace();
		}
	}

	public void updateGUI() {
		// some code
		New = false;
		mxGraphComponent graphComponent = drawSimulation(load);
		JMenuBar bar = drawMenuBar(graphComponent);

		getContentPane().removeAll();
		getContentPane().add(graphComponent, BorderLayout.CENTER);
		getContentPane().add(bar,BorderLayout.NORTH);
		// refresh the GUI
		SwingUtilities.updateComponentTreeUI(GUI.super.rootPane);
		//SwingUtilities.updateComponentTreeUI(graphComponent);


		final JProgressBar[] pbar = { null };
		final JPanel[] panel1 = new JPanel[1];
		resultsBar(panel1, pbar, transactionLabel, feesLabel);
	}

	public void restartSim(JMenuItem mi4, String s) {
		load.setReadyToLoad(true);
		load.setPath(s);
		try {
			load.start();
		} catch (IOException ex) {
			ex.printStackTrace();
		} catch (ParseException ex) {
			ex.printStackTrace();
		}

		mxGraphComponent graphComponent = drawSimulation(load);
		JMenuBar bar = drawMenuBar(graphComponent);
		startSim(graphComponent, bar);
		mi4.setEnabled(true);
	}

	private void startSim(mxGraphComponent graphComponent, JMenuBar bar) {
		// add graph
		getContentPane().removeAll();
		getContentPane().add(graphComponent, BorderLayout.CENTER);
		getContentPane().add(bar,BorderLayout.NORTH);
		// refresh the GUI
		SwingUtilities.updateComponentTreeUI(GUI.super.rootPane);
	}

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

	public static void main(String[] args){
		GUI frame = new GUI();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
		frame.setUndecorated(true);
		frame.setVisible(true);
	}
}