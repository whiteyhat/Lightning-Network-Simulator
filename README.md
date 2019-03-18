# Lightning Network Simulation #

This repository is a majors dissertation project for Aberystwyth University. The aim of this project is educational.

## Current state

* GUI: In development
* Routing: In development

## How it works: ##

There is a list of pre-made networks :
 - Tiny (10 nodes)
 - Small (25 nodes)
 - Medium (50 nodes)
 - Big (500 nodes)

All the defualt networks have nodes well-connected. However, you can create a new Lightning Network and choose a different network topology by selecting:
- Number of Nodes
- Number of channels per node

When a network is loaded. You can start the simulation by clicking Edit>Start and random nodes start to generate transactions along the network.

## How to get started: ##

* Get the source code 
* You can use the main folder as an Eclipse workspace including two projects:
* Import the two Eclipse Maven projects **javafx-d3** and **javafx-d3-demo**
* Build the maven projects
* Run the demo suite **com.github.javafxd3.d3.JavaFxD3DemoSuite**
* If you do not get javafx-d3 up and running please create an issue ticket. 
