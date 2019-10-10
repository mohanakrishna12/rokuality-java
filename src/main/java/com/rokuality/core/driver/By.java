package com.rokuality.core.driver;

import java.io.Serializable;

public abstract class By {

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
	 * Constructs an image based locator to search for within the the device screen. The input
	 * can be either an absolute file path to a .png file on your machine, OR can be a url
	 * to a .png
	 *
	 * @param pathOrUrlToImageSnippetPNG - The path or URL to an image snippet to search for within the device screen.
	 * 
	 * @return By - The constructed locator
	 */
	public static By Image(String pathOrUrlToImageSnippetPNG) {
		return new ByImage(pathOrUrlToImageSnippetPNG);
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
			return "By.Text: " + text;
		}
	}

	public static class ByImage extends By implements Serializable {

		private static final long serialVersionUID = 5341968046120372169L;

		private final String image;

		public ByImage(String image) {
			if (image == null) {
				throw new IllegalArgumentException("Element image path is null.");
			}

			this.image = image;
		}

		@Override
		public String toString() {
			return "By.Image: " + image;
		}
	}

}
