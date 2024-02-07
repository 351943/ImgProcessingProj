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
}

