package Filters;

import Interfaces.PixelFilter;
import core.DImage;

import java.sql.SQLOutput;

public class DisplayInfoFilter implements PixelFilter {
    public DisplayInfoFilter() {
        System.out.println("Filter running...");
    }

    @Override
    public DImage processImage(DImage img) {
        short[][] grid = img.getBWPixelGrid();

        grid = crop(grid, 0, 0, 500, 500);

        System.out.println("Image is " + grid.length + " by "+ grid[0].length);
        img.setPixels(grid);

        int bubbleSize = (221-102)/5;
        int r = 109;
        for (int c = 102; c < 221; c+=bubbleSize) {
            //displayBubbleBorder(grid, r, c, bubbleSize);
            int percent = getPercentageFilled(grid,r,c,bubbleSize);
        }

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

    private int getPercentageFilled(short[][] grid, int row, int col, int bubbleSize) {
        int blackCount = 0;
        int pixelNum=0;
        for (int r = row; r < r+bubbleSize; r++) {
            for (int c = col; c < c+bubbleSize; c++) {
                if (grid[r][c] < 10) {
                    blackCount++;
                }
                pixelNum++;
            }
        }
        int percent = (blackCount/pixelNum)*100;
        return percent;
    }

    private short[][] crop(short[][] grid, int startR, int startC, int endR, int endC) {
        short[][] newGrid = new short[endC-startC][endR-startR];
        for (int r = startR; r < endR; r++) {
            for (int c = startC; c < endC; c++) {
                newGrid[r][c] = grid[r][c];
            }
        }
        return newGrid;
    }
}

