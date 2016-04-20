package org.mixit.cesar.site.model.qrcode;

import java.util.Objects;

/**
 * @author Dev-Mind <guillaume@dev-mind.fr>
 * @since 07/12/15.
 */
public class Point {
    private int x;
    private int y;

    /**
     * A protected constructor. You have to use {@link #create()}
     */
    protected Point(){
    }

    public static Point create(){
        return new Point();
    }

    public static Point create(int x, int y){
        return new Point().set(x,y);
    }
    public Point set(int x, int y) {
        this.x = x;
        this.y = y;
        return this;
    }

    public int x() {
        return x;
    }

    public int y() {
        return y;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Point point = (Point) o;
        return Objects.equals(x, point.x) &&
                Objects.equals(y, point.y);
    }

    @Override
    public String toString() {
        return "Point{" +
                "x=" + x +
                ", y=" + y +
                '}';
    }
}
