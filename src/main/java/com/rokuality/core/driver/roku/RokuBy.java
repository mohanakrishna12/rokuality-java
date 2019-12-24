package com.rokuality.core.driver.roku;

import java.io.Serializable;

import com.rokuality.core.driver.By;

public abstract class RokuBy extends By {

	/**
	 * Constructs a text based locator to search for within the text on the device screen.
	 *
	 * @param text - String The text to search for on the device screen, i.e. "hello world"
	 * 
	 * @return By - The constructed locator
	 */
	public static By Text(String text) {
		return new ByText(text);
	}

	/**
	 * Constructs an attribute/value based locator to search for within the text on the device screen.
	 *
	 * @param attribute - String The attribute to search for on the device screen, i.e. "index"
	 * @param value - String The attributes value to search for on the device screen, i.e. "0"
	 * 
	 * @return By - The constructed locator
	 */
	public static By Attribute(String attribute, String value) {
		return new ByAttribute(attribute, value);
	}

	/**
	 * Constructs a tag based locator to search for within the text on the device screen.
	 *
	 * @param tag - String The tag to search for on the device screen, i.e. "Label"
	 * 
	 * @return By - The constructed locator
	 */
	public static By Tag(String tag) {
		return new ByTag(tag);
	}

	public static class ByText extends By implements Serializable {

		private static final long serialVersionUID = 5341968046120372169L;

		private final String text;

		public ByText(String text) {
			if (text == null) {
				throw new IllegalArgumentException("Element text is null.");
			}

			this.text = text;
		}

		@Override
		public String toString() {
			return "RokuBy.Text: " + text;
		}
	}

	public static class ByAttribute extends By implements Serializable {

		private static final long serialVersionUID = 5341968046120372169L;

		private final String attributeAndValue;

		public ByAttribute(String attribute, String value) {
			if (attribute == null || value == null) {
				throw new IllegalArgumentException("Element attribute is null.");
			}

			this.attributeAndValue = attribute + "::::::::" + value;
		}

		@Override
		public String toString() {
			return "RokuBy.Attribute: " + attributeAndValue;
		}
	}

	public static class ByTag extends By implements Serializable {

		private static final long serialVersionUID = 5341968046120372169L;

		private final String tag;

		public ByTag(String tag) {
			if (tag == null) {
				throw new IllegalArgumentException("Element tag is null.");
			}

			this.tag = tag;
		}

		@Override
		public String toString() {
			return "RokuBy.Tag: " + tag;
		}
	}

}
