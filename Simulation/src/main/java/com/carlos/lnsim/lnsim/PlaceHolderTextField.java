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

/**
 * Class to create an object entity to assist the creation of interactive charts for the analysis tool
 */
@SuppressWarnings("serial")
public class PlaceHolderTextField extends JTextField {

	private String placeholder;

	/**
	 * Constructor to create a placeholder object entity without parameters.
	 */
	public PlaceHolderTextField() {
	}

	/**
	 * Constructor to create a placeholder object entity with all the required parameters.
	 * @param pDoc Document from the JTextfield
	 * @param pText Text from the JTextfield
	 * @param pColumns Number of columns for the JTextfield
	 */
	public PlaceHolderTextField(
			final Document pDoc,
			final String pText,
			final int pColumns)
	{
		super(pDoc, pText, pColumns);
	}

	/**
	 * Method to set the number of columns
	 * @param pColumns Number of new columns
	 */
	public PlaceHolderTextField(final int pColumns) {
		super(pColumns);
	}

	/**
	 * Method to set the JTextfield text
	 * @param pText New JTextfield text
	 */
	public PlaceHolderTextField(final String pText) {
		super(pText);
	}

	/**
	 * Method to create the place holder text field
	 * @param pText Placeholder text to create the object entity
	 * @param pColumns Number of columns to create the object entity
	 */
	public PlaceHolderTextField(final String pText, final int pColumns) {
		super(pText, pColumns);
	}

	/**
	 * Method to draw and initialize the UI component. It renders the text within the text field as a hint.
	 * @param pG Graphics object entity to set up to the JTextfield
	 */
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

	/**
	 * Method to set the placeholder text
	 * @param s New string to replace the placeholder text
	 */
	public void setPlaceholder(final String s) {
		placeholder = s;
	}

}