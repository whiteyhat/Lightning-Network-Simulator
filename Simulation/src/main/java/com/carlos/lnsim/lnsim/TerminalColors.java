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

/**
 * Class to create an object entity to provide colorful messages in the terminal.
 */
public class TerminalColors {

	private final String standard = "\u001B[0m";
	private final String black = "\u001B[30m";
	private final String yellow = "\u001B[33m";
	private final String purple = "\u001B[35m";
	private final String white = "\u001B[37m";

	private final String blackBg = "\u001B[40m";
	private final String greenBg = "\u001B[42m";

	/**
	 *Constructor to create a TerminalColors object entity.
	 */
	public TerminalColors() {
	}

	/**
	 * Method to get the standard color
	 * @return The standard color
	 */
	public String getStandard() {
		return standard;
	}

	/**
	 * Method to get the black color
	 * @return The black color
	 */
	public String getBlack() {
		return black;
	}

	/**
	 * Method to get the yellow color
	 * @return The yellow color
	 */
	public String getYellow() {
		return yellow;
	}

	/**
	 * Method to get the purple color
	 * @return The purple color
	 */
	public String getPurple() {
		return purple;
	}

	/**
	 * Method to get the white color
	 * @return The white color
	 */
	public String getWhite() {
		return white;
	}

	/**
	 * Method to get the black color background
	 * @return The black color background
	 */
	public String getBlackBg() {
		return blackBg;
	}

	/**
	 * Method to get the black green background
	 * @return The black color background
	 */
	public String getGreenBg() {
		return greenBg;
	}
}
