import FileIO.PDFHelper;
import Filters.DisplayInfoFilter;
import Interfaces.PixelFilter;
import core.DImage;
import core.DisplayWindow;
import processing.core.PImage;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

public class FilterTest {

    public static String currentFolder = System.getProperty("user.dir") + "/";


    public static void main(String[] args) throws IOException {
        // ----------------------------------------------------------------
        // >>> Run this to save a pdf page and run filters on the image <<<
        // ----------------------------------------------------------------
       // SaveAndDisplayExample(1);

        // -------------------------------------------------------------------------------
        // >>> Run this to run your filter on a page /without/ displaying anything <<<
        // -------------------------------------------------------------------------------
         RunTheFilter();
    }

    private static void RunTheFilter() throws IOException {
        PImage keyIn = PDFHelper.getPageImage("assets/OfficialOMRSampleDoc.pdf", 1);
        DisplayInfoFilter keyFilter = new DisplayInfoFilter(1,0);
        ArrayList<String> key = keyFilter.getAnswers(new DImage(keyIn));
        int questionAmount = key.size();
        String fileContent = "1. "+questionAmount;
        for (int i = 0; i < questionAmount; i++) {
            fileContent+=", "+key.get(i);
        }
        inputAnswerFile(fileContent);

        for (int pageNum = 1; pageNum < 7; pageNum++) {
            keyIn = PDFHelper.getPageImage("assets/OfficialOMRSampleDoc.pdf", pageNum);
            DImage img = new DImage(keyIn);

            DisplayInfoFilter filter = new DisplayInfoFilter(pageNum,questionAmount);
            ArrayList<String> answers = filter.getAnswers(img);
            inputAnswerFile(compareAnswer(pageNum, key,answers));
        }
    }

    private static String compareAnswer(int pageNum, ArrayList<String> key, ArrayList<String> answers) {
        String compare = "";
        int right = 0;
        for (int i = 0; i < key.size(); i++) {
            String equal = ", false";
            if(key.get(i).equals(answers.get(i))){
                equal=", true";
                right++;
            }
            compare+=equal;
        }
        return (pageNum+". "+right+compare);
    }

    private static void inputAnswerFile(String fileContent) throws IOException {
        writeDataToFile("Answers",fileContent);
    }


    private static void writeDataToFile(String filePath, String data) throws IOException {
        try (FileWriter f = new FileWriter(filePath);
             BufferedWriter b = new BufferedWriter(f);
             PrintWriter writer = new PrintWriter(b);) {
            writer.println(data);
        } catch (IOException error) {
            System.err.println("There was a problem writing to the file: " + filePath);
            error.printStackTrace();
        }
    }

    private static void SaveAndDisplayExample(int page) {
        PImage img = PDFHelper.getPageImage("assets/OfficialOMRSampleDoc.pdf",page);
        img.save(currentFolder + "assets/page" + page + ".png");

        DisplayWindow.showFor("assets/page1.png");
    }
}