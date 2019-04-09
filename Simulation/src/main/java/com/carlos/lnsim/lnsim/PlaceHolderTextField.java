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
import javax.swing.text.Document;
import java.awt.*;

@SuppressWarnings("serial")
public class PlaceHolderTextField extends JTextField {

	public static void main(final String[] args) {
		final PlaceHolderTextField tf = new PlaceHolderTextField("");
		tf.setColumns(20);
		tf.setPlaceholder("All your base are belong to us!");
		final Font f = tf.getFont();
		tf.setFont(new Font(f.getName(), f.getStyle(), 30));
		JOptionPane.showMessageDialog(null, tf);
	}

	private String placeholder;

	public PlaceHolderTextField() {
	}

	public PlaceHolderTextField(
			final Document pDoc,
			final String pText,
			final int pColumns)
	{
		super(pDoc, pText, pColumns);
	}

	public PlaceHolderTextField(final int pColumns) {
		super(pColumns);
	}

	public PlaceHolderTextField(final String pText) {
		super(pText);
	}

	public PlaceHolderTextField(final String pText, final int pColumns) {
		super(pText, pColumns);
	}

	public String getPlaceholder() {
		return placeholder;
	}

	@Override
	protected void paintComponent(final Graphics pG) {
		super.paintComponent(pG);

		if (placeholder == null || placeholder.length() == 0 || getText().length() > 0) {
			return;
		}

		final Graphics2D g = (Graphics2D) pG;
		g.setRenderingHint(
				RenderingHints.KEY_ANTIALIASING,
				RenderingHints.VALUE_ANTIALIAS_ON);
		g.setColor(getDisabledTextColor());
		g.drawString(placeholder, getInsets().left, pG.getFontMetrics()
				.getMaxAscent() + getInsets().top);
	}

	public void setPlaceholder(final String s) {
		placeholder = s;
	}

}