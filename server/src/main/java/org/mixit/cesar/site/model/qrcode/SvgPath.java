    package org.mixit.cesar.site.model.qrcode;

import java.util.Objects;

/**
 * A path element is define by his origin and its width
 *
 * @author Dev-Mind <guillaume@dev-mind.fr>
 * @since 07/12/15.
 */
public class SvgPath {
    private Point origin;
    private int width;

    public static SvgPath create(Point origin, int width) {
        SvgPath rectangle = new SvgPath();
        rectangle.origin = origin;
        rectangle.width = width;
        return rectangle;
    }

    public Point getOrigin() {
        return origin;
    }

    public int getWidth() {
        return width;
    }

    public String generate() {
        //We nned to add an offset of 0.5 (stroke width)
        return String.format("M%s %sh%d", origin.x(), origin.y()+0.5, width);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SvgPath rectangle = (SvgPath) o;
        return Objects.equals(width, rectangle.width) &&
                Objects.equals(origin, rectangle.origin);
    }

    @Override
    public int hashCode() {
        return Objects.hash(origin, width);
    }


    @Override
    public String toString() {
        return "SvgPath{" +
                "origin=" + origin +
                ", width=" + width +
                '}';
    }
}
