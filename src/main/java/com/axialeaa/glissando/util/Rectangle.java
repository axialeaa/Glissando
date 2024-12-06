package com.axialeaa.glissando.util;

public record Rectangle(int minX, int minY, int maxX, int maxY) {

    public static Rectangle create(int x, int y, int sizeX, int sizeY) {
        return new Rectangle(x, y, x + sizeX, y + sizeY);
    }

    public boolean isCoordinateIn(double x, double y) {
        boolean withinWidth = x >= this.minX && x < this.maxX;
        boolean withinHeight = y >= this.minY && y < this.maxY;

        return withinWidth && withinHeight;
    }

}