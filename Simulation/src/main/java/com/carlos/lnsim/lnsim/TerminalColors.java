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

public class TerminalColors {

	private final String standard = "\u001B[0m";
	private final String black = "\u001B[30m";
	private final String red = "\u001B[31m";
	private final String green = "\u001B[32m";
	private final String yellow = "\u001B[33m";
	private final String blue = "\u001B[34m";
	private final String purple = "\u001B[35m";
	private final String white = "\u001B[37m";

	private final String blackBg = "\u001B[40m";
	private final String redBg = "\u001B[41m";
	private final String greenBg = "\u001B[42m";
	private final String yellowBg = "\u001B[43m";
	private final String blueBg = "\u001B[44m";
	private final String purpleBg = "\u001B[45m";

	public TerminalColors() {
	}

	public String getStandard() {
		return standard;
	}

	public String getBlack() {
		return black;
	}

	public String getRed() {
		return red;
	}

	public String getGreen() {
		return green;
	}

	public String getYellow() {
		return yellow;
	}

	public String getBlue() {
		return blue;
	}

	public String getPurple() {
		return purple;
	}

	public String getWhite() {
		return white;
	}

	public String getBlackBg() {
		return blackBg;
	}

	public String getRedBg() {
		return redBg;
	}

	public String getGreenBg() {
		return greenBg;
	}

	public String getYellowBg() {
		return yellowBg;
	}

	public String getBlueBg() {
		return blueBg;
	}

	public String getPurpleBg() {
		return purpleBg;
	}
}
