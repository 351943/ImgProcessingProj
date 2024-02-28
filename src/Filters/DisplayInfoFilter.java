package Filters;

import Interfaces.PixelFilter;
import core.DImage;
import java.util.ArrayList;

public class DisplayInfoFilter implements PixelFilter {
    private int page;
    private int bubbleSize;
    private int questionNum;
    private int keyStartRow;
    private int keyEndRow;
    private int keyStartCol;
    private int keyEndCol;

    public DisplayInfoFilter(int pageNum, int questionNum) {
        page=pageNum;
        this.questionNum=questionNum;
        bubbleSize = (111-51)/5;
        keyStartRow=162;
        keyStartCol = 220;
        keyEndRow = 162 + bubbleSize * 10;
        keyEndCol = 220 + bubbleSize * 3;
    }

    @Override
    public DImage processImage(DImage img) {
        short[][] grid=downSize(img.getBWPixelGrid());

        System.out.println("Image is " + grid.length + " by "+ grid[0].length);

        return img;
    }

    private int getNumQuestion(short[][] grid){
        ArrayList<Integer> numQuestionIndex = new ArrayList<>();
        ArrayList<Double> percentList = new ArrayList<>();;
        if (page==1) {
            for (int c = keyStartCol; c < keyEndCol; c += bubbleSize) {
                for (int r = keyStartRow; r < keyEndRow; r += bubbleSize) {
                    percentList.add(getPercentageFilled(grid, r, c, bubbleSize));
                    displayBubbleBorder(grid, r, c, bubbleSize);
                }
                numQuestionIndex.add(getIndexofMostFilledBubble(percentList));
            }
            questionNum=changeIndexToNum(numQuestionIndex);
        }
        return questionNum;
    }

    private int changeIndexToNum(ArrayList<Integer> answers) {
        int tens = answers.get(1)*10;
        return tens+answers.get(2);
    }


    public ArrayList<String> getAnswers(DImage img){
        short[][] orgGrid = img.getBWPixelGrid();
        short[][] grid=downSize(orgGrid);
        ArrayList<String> answers = new ArrayList<>();
        ArrayList<Double> percentList= new ArrayList<>();;
        int startRow=54;
        int endRow = 54 + bubbleSize * 2 * getNumQuestion(grid);
        int startCol = 51;
        int endCol = 51 + bubbleSize * 5;

        for (int r = startRow; r < endRow; r += bubbleSize * 2) {
            for (int c = startCol; c < endCol; c += bubbleSize) {
                percentList.add(getPercentageFilled(grid, r, c, bubbleSize));
                displayBubbleBorder(grid, r, c, bubbleSize);
            }
            answers.add(getLetter(getIndexofMostFilledBubble(percentList)));
        }

        img.setPixels(grid);
        return answers;
    }

    private short[][] downSize(short[][] orgGrid) {
        short[][] rescale = new short[orgGrid.length/2][orgGrid[0].length/2];

        for (int newRow = 0; newRow < rescale.length; newRow++) {
            for (int newCol = 0; newCol < rescale[newRow].length; newCol++) {
                rescale[newRow][newCol]= newVal(orgGrid, newRow*2, newCol*2);
            }
        }
        return rescale;
    }

    private static short newVal(short[][] grid, int originalRow, int originalCol){
        double top2 = (grid[originalRow][originalCol]+grid[originalRow][originalCol+1]);
        double bottom2 = (grid[originalRow+1][originalCol]+grid[originalRow+1][originalCol+1]);
        return (short) ((top2+bottom2)/4);
    }

    private int getIndexofMostFilledBubble(ArrayList<Double> percentList) {
        double max = percentList.get(0);
        int maxIndex=0;
        for (int i = 1; i < percentList.size(); i++) {
            if(percentList.get(i)>max){
                max=percentList.get(i);
                maxIndex=i;
            }
        }
        return maxIndex;
    }

    private String getLetter(int maxIndex) {
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

