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

        int bubbleSize = (224-104)/5;
        for (int r = 106; r < 106+bubbleSize*2*12; r+=bubbleSize*2) {
            percentList=new ArrayList<>();
            for (int c = 104; c < 104+bubbleSize*5; c+=bubbleSize) {
                percentList.add(getPercentageFilled(grid,r,c,bubbleSize));
                displayBubbleBorder(grid, r, c, bubbleSize);
            }
            System.out.println(questionNum+": "+findMostFilled(percentList));
            questionNum++;
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

    private int getPercentageFilled(short[][] grid, int row, int col, int bubbleSize) {
        int blackCount = 0;
        int pixelNum=0;
        for (int r = row; r < row+bubbleSize; r++) {
            for (int c = col; c < col+bubbleSize; c++) {
                if (grid[r][c] < 220) {
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

