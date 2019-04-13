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

public class RoutingTable {

	public List<Node> getDirections(Node sourceNode, Node destinationNode) {
		//Initialization.
		Map<Node, Node> nextNodeMap = new HashMap<Node, Node>();
		Node currentNode = sourceNode;

		//Queue
		Queue<Node> queue = new LinkedList<Node>();
		queue.add(currentNode);

		/*
		 * The set of visited nodes doesn't have to be a Map, and, since order
		 * is not important, an ordered collection is not needed. HashSet is
		 * fast for add and lookup, if configured properly.
		 */
		Set<Node> visitedNodes = new HashSet<Node>();
		visitedNodes.add(currentNode);

		//Search.
		while (!queue.isEmpty()) {
			currentNode = queue.remove();
			if (currentNode.equals(destinationNode)) {
				break;
//			} else {
//				for (Node nextNode : getChildNodes(currentNode)) {
//					if (!visitedNodes.contains(nextNode)) {
//						queue.add(nextNode);
//						visitedNodes.add(nextNode);
//
//						//Look up of next node instead of previous.
//						nextNodeMap.put(currentNode, nextNode);
//					}
//				}
			}
		}

		//If all nodes are explored and the destination node hasn't been found.
		if (!currentNode.equals(destinationNode)) {
			throw new RuntimeException("No feasible path.");
		}

		//Reconstruct path. No need to reverse.
		List<Node> directions = new LinkedList<Node>();
		for (Node node = sourceNode; node != null; node = nextNodeMap.get(node)) {
			directions.add(node);
		}

		return directions;
	}
}
