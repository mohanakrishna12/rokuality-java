package com.rokuality.core.driver;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.awt.Dimension;
import java.awt.Point;
import java.util.Objects;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class ScreenText {

    @JsonProperty("text")
    private String text;
    @JsonProperty("location")
    private Point location;
    @JsonProperty("length")
    private int width;
    @JsonProperty("width")
    private int height;
    @JsonProperty("confidence")
    private float confidence;

    /**
	 * Gets the screen text of the word on the device screen.
	 *
	 * @return String
	 */
    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    /**
	 * Gets the screen text location on the device screen.
	 *
	 * @return Point
	 */
    public Point getLocation() {
        return location;
    }

    public void setLocation(Point location) {
        this.location = location;
    }

    /**
	 * Gets the screen text width (x-axis length of text) on the device screen.
	 *
	 * @return int
	 */
    public int getWidth() {
        return width;
    }

    public void setWidth(double width) {
        this.width = (int) width;
    }

    /**
	 * Gets the screen text height (y-axis height of text) on the device screen.
	 *
	 * @return int
	 */
    public int getHeight() {
        return height;
    }

    public void setHeight(double height) {
        this.height = (int) height;
    }

    /**
	 * Gets the screen text confidence as found on the device screen. Only relevant for tesseract based OCR evaluations
     * and not GoogleVision.
	 *
	 * @return float
	 */
    public float getConfidence() {
        return confidence;
    }

    public void setConfidence(float confidence) {
        this.confidence = confidence;
    }

    public ScreenText() {
    }

    /**
	 * Gets the size of the text.
	 *
	 * @return Dimension
	 */
    public Dimension getSize() {
        int w = width;
        int h = height;
        return new Dimension(w, h);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        ScreenText imageText = (ScreenText) o;
        return Integer.compare(imageText.height, height) == 0 && Integer.compare(imageText.width, width) == 0
                && Float.compare(imageText.confidence, confidence) == 0 && Objects.equals(text, imageText.text)
                && Objects.equals(location, imageText.location);
    }

    @Override
    public int hashCode() {
        return Objects.hash(text, location, height, width, confidence);
    }

    @Override
    public String toString() {
        return "ImageText{" + "text='" + text + '\'' + ", location=" + location + ", height=" + height + ", width="
                + width + ", confidence=" + confidence + "}";
    }
}
