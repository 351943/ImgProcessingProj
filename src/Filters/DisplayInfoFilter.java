package Filters;

import Interfaces.PixelFilter;
import core.DImage;

public class DisplayInfoFilter implements PixelFilter {
    public DisplayInfoFilter() {
        System.out.println("Filter running...");
    }

    @Override
    public DImage processImage(DImage img) {
        short[][] grid = img.getBWPixelGrid();

        grid = crop(grid, 0, 0, 500, 500);

        System.out.println("Image is " + grid.length + " by "+ grid[0].length);

        int bubbleSize = (224-104)/5;
        System.out.println(bubbleSize);
        int r = 106;
        for (int c = 104; c < 104+bubbleSize*5; c+=bubbleSize) {
            //int percent = getPercentageFilled(grid,r,c,bubbleSize);
            displayBubbleBorder(grid, r, c, bubbleSize);
        }

        img.setPixels(grid);
        return img;
    }

    private void displayBubbleBorder(short[][] grid, int r, int c, int bubbleSize) {
        for (int i = r; i < r+bubbleSize; i++) {
            for (int j = c; j < c+bubbleSize; j++) {
                if(i==r||j==c||i==r+bubbleSize-1||j==c+bubbleSize-1){
                    grid[i][j]=0;
                }
            }
        }
    }

    private short[][] crop(short[][] grid, int r, int c, int r1, int c1) {
        short[][] newGrid = new short[r1][c1];
        for (int i = r; i < r1; i++) {
            for (int j = c; j < c1; j++) {
                newGrid[i][j]=grid[i][j];
            }
        }
        return newGrid;
    }

}

