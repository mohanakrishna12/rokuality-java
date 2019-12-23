package com.rokuality.core.driver;

import java.awt.Dimension;
import java.awt.Point;

import java.util.Objects;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class Element {

    private int element_y;
    private int element_x;
    private int element_width;
    private int element_height;
    private float element_confidence;
    private String element_text;
    private String session_id;
    private String element_id;
    private String element_json;

    // TODO - serialize with jackson to remove the super ugly castings
    public Element(JSONObject json) {
        element_x = Integer.parseInt(json.get("element_x").toString());
        element_y = Integer.parseInt(json.get("element_y").toString());
        element_width = (int) Double.parseDouble((json.get("element_width").toString()));
        element_height = (int) Double.parseDouble((json.get("element_height").toString()));
        element_confidence = Float.parseFloat(json.get("element_confidence").toString());
        element_text = (String) json.get("element_text");
        session_id = (String) json.get("session_id");
        element_id = (String) json.get("element_id");
        element_json = (String) json.get("element_json");
    }

    /**
	 * Gets the session id that the element belongs to.
	 *
	 * @return String
	 */
    public String getSessionID() {
        return session_id;
    }

    /**
	 * Gets the element id.
	 *
	 * @return String
	 */
    public String getElementID() {
        return element_id;
    }

    /**
	 * Gets the text of the element. If an image based 
     * locator it will be any found text within the matched element.
	 *
	 * @return String
	 */
    public String getText() {
        return element_text;
    }

    /**
	 * Gets the width of the element.
	 *
	 * @return int
	 */
    public int getWidth() {
        return element_width;
    }

    /**
	 * Gets the height of the element.
	 *
	 * @return int
	 */
    public int getHeight() {
        return element_height;
    }

    /**
	 * Gets the size of the element.
	 *
	 * @return Dimension
	 */
    public Dimension getSize() {
        int w = element_width;
        int h = element_height;
        return new Dimension(w, h);
    }

    /**
	 * Gets the starting x of the element.
	 *
	 * @return int
	 */
    public int getX() {
        return element_x;
    }

    /**
	 * Gets the starting y of the element.
	 *
	 * @return int
	 */
    public int getY() {
        return element_y;
    }

    /**
	 * Gets the location x,y of the element.
	 *
	 * @return Point
	 */
    public Point getLocation() {
        return new Point(element_x, element_y);
    }

    /**
	 * Gets the confidence score of the element match.
	 *
	 * @return float
	 */
    public float getConfidence() {
        return element_confidence;
    }

    public JSONObject getElementJSON() {
        if (element_json != null) {
            try {
                return (JSONObject) new JSONParser().parse(element_json);
            } catch (ParseException e) {
                return null;
            }
        }
        return null;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        Element rokuElement = (Element) o;
        return Double.compare(rokuElement.element_width, element_width) == 0
                && Double.compare(rokuElement.element_height, element_height) == 0
                && Objects.equals(element_text, rokuElement.element_text)
                && Objects.equals(element_x, rokuElement.element_x) && Objects.equals(element_y, rokuElement.element_y)
                && Objects.equals(element_confidence, rokuElement.element_confidence);
    }

    @Override
    public int hashCode() {
        return Objects.hash(element_text, element_x, element_y, element_width, element_height, element_confidence);
    }

    @Override
    public String toString() {
        return "{" + "element_id:'" + element_id + "',text:'" + element_text + '\'' + ", x:" + element_x + ", y:"
                + element_y + ", width:" + element_width + ", height:" + element_height + ", confidence:"
                + element_confidence + "}";
    }

}
