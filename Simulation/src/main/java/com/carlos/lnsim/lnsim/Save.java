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

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import java.util.Random;
import java.io.FileWriter;
import java.io.IOException;

public class Save {

	public static void main(String[] args) throws IOException {
		Random rand = new Random();
		init(10, rand.nextInt(10), 2);
	}

	public void createNetwork(int nodeSize, int channelsPerNode){
		Random rand = new Random();
		rand.setSeed(System.currentTimeMillis());
//		TODO Save seed in config file to replicate scenario
		try {
			init(nodeSize, channelsPerNode, 2);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private static void init(int nodeSize, int channelsPerNode, int tx) throws IOException {
		JSONObject json = new JSONObject();
		JSONArray config = new JSONArray();
		for (int i = 0; i < nodeSize; i++) {
			JSONObject nodes = new JSONObject();
			JSONArray channelsArray = new JSONArray();
			Random rand = new Random();
			nodes.put("id",String.valueOf(i));
			nodes.put("alias",("Node" + (i)));
			nodes.put("balance" , String.valueOf(rand.nextInt(1000)));
			for (int j = 0; j < channelsPerNode; j++) {
				JSONObject channels = new JSONObject();
				JSONArray transactionArray = new JSONArray();
				Random random = new Random();
				channels.put("id",String.valueOf(j));
				channels.put("from",String.valueOf(i));
				int randomNumber = random.nextInt(nodeSize);
				boolean valid = false;
				do {
					if (randomNumber == i){
						randomNumber = random.nextInt(nodeSize);
					} else {
						valid = true;
					}
				}while (!valid);
				channels.put("to",String.valueOf(randomNumber));
				channels.put("capacity",String.valueOf(random.nextInt(100)));
				channels.put("fee", String.valueOf(random.nextInt(7)));
				for (int k = 0; k < tx ; k++) {
					JSONObject transaction = new JSONObject();
					transaction.put("secret", "");
					transaction.put("paymentRequest", "");
					transaction.put("tokens", String.valueOf(random.nextInt(10000)));
					transaction.put("createdAt", "");
					transaction.put("expiredAt", "");
					transactionArray.add(transaction);
				}
				channels.put("transactions" ,transactionArray);
				channelsArray.add(channels);
			}
			nodes.put("channels", channelsArray);
			config.add(nodes);
		}

		json.put("Nodes", config);
		System.out.println(config);
		// try-with-resources statement based on post comment below :)
		try (FileWriter file = new FileWriter("src/main/resources/config/custom.json")) {
			file.write(json.toJSONString());
		}
	}
}
