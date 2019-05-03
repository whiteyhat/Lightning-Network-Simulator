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

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;

/**
 * Class to create toast object entities. Code extracted from
 * https://stackoverflow.com/a/20121567
 */
class Toast extends JDialog {
	int miliseconds;

	/**
	 * Constructor to generate a toast notification to display to the user
	 * @param toastString Message to display in the notification
	 * @param time Time to  display the notification
	 * @param type Type of notification
	 */
	protected Toast(String toastString, int time, int type) {
		Color color = new Color(149, 165, 166);
		if (type == 1){
			color = new Color(52, 152, 219);
		} else if (type == 2){
			color = new Color(203, 67, 53);
		} else if (type == 3){
			color = new Color(35, 155, 86);
		}
		this.miliseconds = time;
		setUndecorated(true);
		getContentPane().setLayout(new BorderLayout(0, 0));

		JPanel panel = new JPanel();
		panel.setBackground(color);
		panel.setBorder(new LineBorder(color, 2));
		getContentPane().add(panel, BorderLayout.SOUTH);

		JLabel toastLabel = new JLabel("");
		toastLabel.setText(toastString);
		toastLabel.setFont(new Font("Dialog", Font.BOLD, 12));
		toastLabel.setForeground(Color.WHITE);

		setBounds(0, 0, 1000, 31);

		setAlwaysOnTop(true);
		Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
		int y = dim.height/2-getSize().height/2;
		int half = y/2;
		setLocation(dim.width/2-getSize().width/2, y+half);
		panel.add(toastLabel);
		setVisible(false);

		new Thread(){
			public void run() {
				try {
					Thread.sleep(miliseconds);
					dispose();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}.start();
	}
}