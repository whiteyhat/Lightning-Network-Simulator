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

import java.util.Scanner;

public class Menu {
    boolean exit1level ;
    boolean exit2level ;

    public Menu() {
        this.exit1level = false;
    }

    public void startMenu(){
        System.out.println("===========================================");
        System.out.println("Welcome to the Lightning Network Simulation");
        System.out.println("===========================================");
        System.out.println();
        do {
            System.out.println("0: Exit");
            System.out.println("1: Set Up a Lightning com.carlos.lnsim.lnsim.Node");
            Scanner scanner = new Scanner(System.in);
            int input1level = scanner.nextInt();

            switch (input1level){
                case 0:
                    System.out.println("Bye!");
                    exit1level = true;
                    break;

                case 1:
                    System.out.println("=======================");
                    System.out.println("Set Up a Lightning com.carlos.lnsim.lnsim.Node");
                    System.out.println("=======================");
                    Node node = new Node();
                    System.out.println(node.toString());
                    System.out.println();
                    do {
                        if (node.getBalance() == 0 || node.getAlias() == null) {
                            System.out.println("0: Back");
                            System.err.println("1: Add Alias");
                            System.err.println("2: Add Balance");
                            int input2level = scanner.nextInt();
                            switch (input2level){
                                case 0:
                                    System.out.println("Coming Back..");
                                    exit2level = true;
                                    break;

                                case 1:
                                    System.out.println("Type your Lightning com.carlos.lnsim.lnsim.Node alias");
                                    node.setAlias(scanner.next());
                                    System.out.println(node.toString());
                                    break;
                                case 2:
                                    System.out.println("Type your initial balance");
                                    node.setBalance(scanner.nextInt());
                                    System.out.println(node.toString());
                                    break;
                            }

                        } else if ((node.getBalance() > 0 || node.getAlias() != null) && (node.getChannels() == null)){
                            System.out.println("0: Back");
                            System.out.println("1: Update Alias");
                            System.out.println("2: Add More Balance");
                            System.out.println("3: Open a com.carlos.lnsim.lnsim.Channel");
                            int input2level = scanner.nextInt();
                            switch (input2level){
                                case 0:
                                    System.out.println("Coming Back..");
                                    exit2level = true;
                                    break;

                                case 1:
                                    System.out.println("Type your Lightning com.carlos.lnsim.lnsim.Node alias");
                                    node.setAlias(scanner.next());
                                    System.out.println(node.toString());
                                    break;
                                case 2:
                                    System.out.println("Type your new balance");
                                    node.setBalance(scanner.nextInt());
                                    System.out.println(node.toString());
                                    break;
                                case 3:
                                    Node testNode = new Node();
                                    testNode.setAlias("Testing com.carlos.lnsim.lnsim.Node");
                                    System.out.println("=======================");
                                    System.out.println("Available Nodes in the Lightning network");
                                    System.out.println("=======================");
                                    System.out.println(testNode.toString());
                                    System.out.println();
                                    System.out.println("Type a Lightning com.carlos.lnsim.lnsim.Node id to open a channel");
                                    System.out.println(node.toString());
                                    break;
                            }
                        } else {
                            System.out.println("0: Back");
                            System.out.println("1: Update Alias");
                            System.out.println("2: Add More Balance");
                            System.out.println("3: Request a com.carlos.lnsim.lnsim.Transaction");
                            System.out.println("4: Pay a com.carlos.lnsim.lnsim.Transaction");
                        }


                    }while (!exit2level);
                    break;
            }
        }while (!exit1level);

    }

    public static void main(String[] args) {
       Menu menu = new Menu();
       menu.startMenu();
    }



}


