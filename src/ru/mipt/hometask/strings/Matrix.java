package ru.mipt.hometask.strings;

public class Matrix {
    private int width;
    private int height;
    private char[][] data;

    public Matrix(int width, int height) {
        this.width = width;
        this.height = height;
        data = new char[width][height];
    }

    public char get(int x, int y) {
        return data[x][y];
    }

    public void get(int x, int y, char value) {
        data[x][y] = value;
    }
}
