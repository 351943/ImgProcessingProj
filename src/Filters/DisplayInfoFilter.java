package Filters;


import Interfaces.PixelFilter;
import core.DImage;


import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLOutput;
import java.util.ArrayList;


public class DisplayInfoFilter implements PixelFilter {
    public DisplayInfoFilter() {
        System.out.println("Filter running...");
    }


    @Override
    public DImage processImage(DImage img) {
        short[][] orgGrid = img.getBWPixelGrid();
        short[][] grid=downSize(orgGrid);

        System.out.println("Image is " + grid.length + " by "+ grid[0].length);

        getAnswers(grid);

        img.setPixels(grid);
        return img;
    }

    public ArrayList<String> getAnswers(DImage img){
        short[][] orgGrid = img.getBWPixelGrid();
        short[][] grid=downSize(orgGrid);
        ArrayList<String> answers = new ArrayList<>();
        ArrayList<Double> percentList;
        int bubbleSize = (111-51)/5;
        for (int r = 54; r < 54+bubbleSize*2*25; r+=bubbleSize*2) {
            percentList=new ArrayList<>();
            for (int c = 51; c < 51+bubbleSize*5; c+=bubbleSize) {
                percentList.add(getPercentageFilled(grid,r,c,bubbleSize));
                displayBubbleBorder(grid, r, c, bubbleSize);
            }
            answers.add(findMostFilled(percentList));
        }
        return answers;
    }

    public short[][] downSize(short[][] orgGrid) {
        short[][] rescale = new short[orgGrid.length/2][orgGrid[0].length/2];


        for (int newRow = 0; newRow < rescale.length; newRow++) {
            for (int newCol = 0; newCol < rescale[newRow].length; newCol++) {
                rescale[newRow][newCol]=avgVal(orgGrid, newRow*2, newCol*2);
            }
        }
        return rescale;
    }


    private static short avgVal(short[][] grid, int originalRow, int originalCol){
        double top2 = (grid[originalRow][originalCol]+grid[originalRow][originalCol+1]);
        double bottom2 = (grid[originalRow+1][originalCol]+grid[originalRow+1][originalCol+1]);
        return (short) ((top2+bottom2)/4);
    }


    private String findMostFilled(ArrayList<Double> percentList) {
        double max = percentList.get(0);
        int maxIndex=0;
        for (int i = 1; i < percentList.size(); i++) {
            if(percentList.get(i)>max){
                max=percentList.get(i);
                maxIndex=i;
            }
        }
        return findLetter(maxIndex);
    }


    private String findLetter(int maxIndex) {
        if(maxIndex==0){
            return "A";
        }
        if(maxIndex==1){
            return "B";
        }
        if(maxIndex==2){
            return "C";
        }
        if(maxIndex==3) {
            return "D";
        }
        return "E";
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


    private double getPercentageFilled(short[][] grid, int row, int col, int bubbleSize) {
        double blackCount = 0;
        int pixelNum=0;
        for (int r = row; r < row+bubbleSize; r++) {
            for (int c = col; c < col+bubbleSize; c++) {
                if (grid[r][c] < 220) {
                    blackCount++;
                }
                pixelNum++;
            }
        }
        return (blackCount/pixelNum)*100;
    }


}


