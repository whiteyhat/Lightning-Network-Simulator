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
	private SwingWorker<Boolean, Void> backgroundProcess;
	private mxGraphComponent graphComponent;
	private boolean New;

	public GUI() {
		super("Lightning Network Simulation");

		load = new Load();
		New = true;
		width = new ArrayList<>();
		height = new ArrayList<>();
		graphComponent = drawSimulation(load);

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
		getContentPane().add(bar,BorderLayout.NORTH);

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
				graph.insertVertex(parent, String.valueOf(node.getId()), element, width.get(i), height.get(i), 80, 30);

			}

			for (Channel channel: load.getChannels()) {
				Element relation = doc.createElement("Channel");
				relation.setAttribute("capacity", String.valueOf(channel.getCapacity()));


				mxCell fromCell = (mxCell) ((mxGraphModel)graph.getModel()).getCell(String.valueOf(channel.getFrom()+1));
				mxCell toCell = (mxCell) ((mxGraphModel)graph.getModel()).getCell(String.valueOf(channel.getTo()+1));

				if (channel.getCapacity() > 80.0){
					graph.insertEdge(parent, String.valueOf(channel.getId()), relation, fromCell, toCell, "ROUNDED;strokeColor=green;strokeWidth=7");
				} else if ((channel.getCapacity() >= 70.0)&& (channel.getCapacity() <= 80.0)){
					graph.insertEdge(parent, String.valueOf(channel.getId()), relation, fromCell, toCell, "ROUNDED;strokeColor=green;strokeWidth=6");
				} else if ((channel.getCapacity() >= 60.0)&& (channel.getCapacity() <= 79.9)){
					graph.insertEdge(parent, String.valueOf(channel.getId()), relation, fromCell, toCell, "ROUNDED;strokeColor=green;strokeWidth=5");
				} else if ((channel.getCapacity() >= 50.0)&& (channel.getCapacity() <= 69.9)){
					graph.insertEdge(parent, String.valueOf(channel.getId()), relation, fromCell, toCell, "ROUNDED;strokeColor=blue;strokeWidth=4");
				} else if ((channel.getCapacity() >= 40.0)&& (channel.getCapacity() <= 59.9)){
					graph.insertEdge(parent, String.valueOf(channel.getId()), relation, fromCell, toCell, "ROUNDED;strokeColor=blue;strokeWidth=3");
				} else if ((channel.getCapacity() >= 30.0)&& (channel.getCapacity() <= 49.9)){
					graph.insertEdge(parent, String.valueOf(channel.getId()), relation, fromCell, toCell, "ROUNDED;strokeColor=orange;strokeWidth=2");
				} else if ((channel.getCapacity() >= 20.0)&& (channel.getCapacity() <= 39.9)){
					graph.insertEdge(parent, String.valueOf(channel.getId()), relation, fromCell, toCell, "ROUNDED;strokeColor=red;strokeWidth=1");
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
								return elt.getTagName() + " (Capacity: "
										+ elt.getAttribute("capacity") + ")";
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

		JMenuItem mi4 = new JMenuItem("Start");
		mi4.setEnabled(true);
		m2.add(mi4);
		mi2.addActionListener(new ActionListener() {
			@Override public void actionPerformed(ActionEvent e) {
				chooseFile(mi2, mi4);
				mi4.setEnabled(true);
			}
		});
		JMenuItem mi5 = new JMenuItem("Pause");
		mi5.setEnabled(false);
		m2.add(mi5);
		JMenuItem mi6 = new JMenuItem("Stop");
		mi6.setEnabled(false);
		m2.add(mi6);

		JMenu m3 = new JMenu("Analyze", true);

		JMenuItem mi7 = new JMenuItem("Channels");
		m3.add(mi7);
		JMenuItem mi8 = new JMenuItem("Balances");
		m3.add(mi8);
		JMenuItem mi9 = new JMenuItem("Routes");
		m3.add(mi9);

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
				Save save = new Save();
				save.createNetwork(Integer.parseInt(nodesSize[0].getText()), Integer.parseInt(channelsSize[0].getText()));
				restartSim(mi4, "src/main/resources/config/custom.json");
			}
		});

		mi11.addActionListener(new ActionListener() {
			@Override public void actionPerformed(ActionEvent e) {
				try {
					Desktop.getDesktop().browse(
							URI.create("https://lightning.network/lightning-network-summary.pdf"));
				} catch (IOException e1) {
					e1.printStackTrace();
				}

			}
		});

		mi12.addActionListener(new ActionListener() {
			@Override public void actionPerformed(ActionEvent e) {
				try {
					Desktop.getDesktop().browse(
							URI.create("mailto:car23@aber.ac.uk?cc=alg25@aber.ac.uk&subject=BUG - Lightning Network Simulator&"));
				} catch (IOException e1) {
					e1.printStackTrace();
				}

			}
		});

		mi13.addActionListener(new ActionListener() {
			@Override public void actionPerformed(ActionEvent e) {
				try {
					Desktop.getDesktop().browse(
							URI.create("mailto:car23@aber.ac.uk?cc=alg25@aber.ac.uk&subject=FEEDBACK - Lightning Network Simulator&"));
				} catch (IOException e1) {
					e1.printStackTrace();
				}

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
				SwingUtilities.updateComponentTreeUI(GUI.super.rootPane);
				channels = String.valueOf(load.getChannels().size());
				size = String.valueOf(load.getNodes().size());
				tx = String.valueOf(load.getTransactions().size());
				Double amount = 0.00;
				for (com.carlos.lnsim.lnsim.Node node: load.getNodes()) {
					amount += node.getBalance();
				}
				balance = String.valueOf(amount);
				Font font = new Font("Courier", Font.BOLD, 12);
				panel1[0] = new JPanel();
				panel1[0].add(new JLabel("Transactions: ")).setFont(font);
				JLabel transactionLabel = new JLabel(tx);
				panel1[0].add(transactionLabel);
				panel1[0].add(new JLabel("Channels: ")).setFont(font);
				panel1[0].add(new JLabel(channels));
				panel1[0].add(new JLabel("Network Balance: ")).setFont(font);
				panel1[0].add(new JLabel(balance));
				panel1[0].add(new JLabel("Network Size: ")).setFont(font);
				panel1[0].add(new JLabel(size));
				panel1[0].setLayout(new GridLayout(0,2));
				getContentPane().add(panel1[0], BorderLayout.EAST);

				pbar[0] = new JProgressBar();
				pbar[0].setMinimum(0);
				pbar[0].setMaximum(3000);
				getContentPane().add(pbar[0], BorderLayout.SOUTH);
				final int[] value = { 0 };

//				new Thread(new Runnable() {
//					@Override public void run() {
//						TrafficGenerator trafficGenerator = new TrafficGenerator();
//
//							for (Channel channel : load.getChannels()) {
//
//								for(com.carlos.lnsim.lnsim.Node selectedNode : load.getNodes()) {
//									if(String.valueOf(selectedNode.getId()).equals(String.valueOf(channel.getTo()))) {
//										boolean valid = false;
//										do {
//											trafficGenerator.getTransactions().add(new Transaction(selectedNode, 2));
//											selectedNode.setBalance(selectedNode.getBalance()-2);
//											channel.setCapacity(channel.getCapacity()-2.0);
//											if ((selectedNode.getBalance() < 1) || (channel.getCapacity() < 1.0)){
//												valid = true;
//											}
//										}while (!valid);
//									}
//								}
//							}
//
//							transactionLabel.setText(String.valueOf(trafficGenerator.trafficSize()));
//
//					}
//				}).start();


							ActionListener actionListener = new ActionListener() {
								@Override public void actionPerformed(ActionEvent e) {
									pbar[0].setValue(value[0]++);
									for (Channel ch : load.getChannels()) {
										ch.setCapacity(ch.getCapacity()- 1.0);
									}

									for (com.carlos.lnsim.lnsim.Node node : load.getNodes()) {
										node.setBalance(node.getBalance() -1);
									}
									updateGUI();
									mi6.setEnabled(true);
									mi5.setEnabled(true);
									mi4.setEnabled(false);

								}
							};



				timer[0] = new Timer(300, actionListener);
				timer[0].start();
			}

		});

		mi5.addActionListener(new ActionListener() {
			@Override public void actionPerformed(ActionEvent e) {
				timer[0].stop();
				mi5.setEnabled(false);
				mi6.setEnabled(true);
				mi4.setEnabled(true);
			}
		});

		mi6.addActionListener(new ActionListener() {
			@Override public void actionPerformed(ActionEvent e) {
				SwingUtilities.updateComponentTreeUI(GUI.super.rootPane);
				mi6.setEnabled(false);
				timer[0].stop();
				getContentPane().remove(pbar[0]);
				getContentPane().remove(panel1[0]);

			}
		});
		return bar;
	}

	public void updateGUI() {
		// some code
		New = false;
		mxGraphComponent graphComponent = drawSimulation(load);
		JMenuBar bar = drawMenuBar(graphComponent);
		startSim(graphComponent, bar);

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

		if (!New){

		}
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