[![Codacy Badge](https://api.codacy.com/project/badge/Grade/7b58c19296bc44c1b714f246228a4930)](https://app.codacy.com/app/carlosroldan26396/Lightning-Network-Simulator?utm_source=github.com&utm_medium=referral&utm_content=whiteyhat/Lightning-Network-Simulator&utm_campaign=Badge_Grade_Settings)
[![License: MIT](https://img.shields.io/badge/License-MIT-yellow.svg)](https://opensource.org/licenses/MIT)
[![Known Vulnerabilities](https://snyk.io/test/github/whiteyhat/Lightning-Network-Simulator/badge.svg?targetFile=Simulation%2Fpom.xml)](https://snyk.io/test/github/whiteyhat/Lightning-Network-Simulator?targetFile=Simulation%2Fpom.xml)
# Lightning Network Simulation #
![Screenshot](https://github.com/whiteyhat/Lightning-Network-Simulation/blob/master/Simulation/src/main/resources/Screenshots/screenshot.gif)

This repository is a majors dissertation project for Aberystwyth University. The aim of this project is educational.
Please, read the [Wiki](https://github.com/whiteyhat/Lightning-Network-Simulation/wiki).

## Current status
* Simulation tool: Stable ✅
* Analysis tool: Stable ✅

## How it works: ##

There is a list of pre-made networks :
 - Tiny (10 nodes)
 - Small (25 nodes)
 - Medium (50 nodes)
 - Big (500 nodes)

All the defualt networks have nodes well-connected. However, you can create a new Lightning Network and choose a different network topology by selecting:
- Number of Nodes
- Number of channels per node

When a network is loaded. You can start the simulation by clicking **Edit>Start** and random nodes start to generate transactions along the network.

## Set up: ##

* Get the source code.
* Make sure you have JDK SE 8.
* You can use the main folder as an Eclipse or IntelliJ IDEA workspace.
* Build the maven projects.
* Run the GUI suite **com.carlos.lnsim.lnsim.GUI**
* If you do not get the simulation up and running please create an issue ticket. 
