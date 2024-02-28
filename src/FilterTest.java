import FileIO.PDFHelper;
import Filters.DisplayInfoFilter;
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
         RunTheFilter();
    }

    private static void RunTheFilter() throws IOException {
        PImage keyIn = PDFHelper.getPageImage("assets/OfficialOMRSampleDoc.pdf", 1);
        DisplayInfoFilter keyFilter = new DisplayInfoFilter(1,0);
        ArrayList<String> key = keyFilter.getAnswers(new DImage(keyIn));
        int numQuestions = key.size();
        String fileContent = addKey(createHeader("", numQuestions),key,numQuestions);

        writeDataToFile("Scores",addScoresContent(fileContent, numQuestions, keyIn, key));
        writeDataToFile("Item Analysis File",addAnalysisContent(numQuestions, keyIn, key));
    }

    private static String addAnalysisContent(int numQ, PImage keyIn, ArrayList<String> key){
        String content = "Question Number, Amt Wrong, Percentage Wrong";
        for (int currQ = 1; currQ <= numQ; currQ++) {
            String add=currQ+", "+timesWrong(currQ,numQ,keyIn,key)+", "+(short)timesWrong(currQ,numQ,keyIn,key)/numQ+"%";
            content+="\n"+add;
        }
        return content;
    }

    private static int timesWrong(int currQ, int numQ, PImage keyIn, ArrayList<String> key) {
        int wrong = 0;
        for (int pageNum = 1; pageNum < 7; pageNum++) {
            keyIn = PDFHelper.getPageImage("assets/OfficialOMRSampleDoc.pdf", pageNum);
            DisplayInfoFilter filter = new DisplayInfoFilter(pageNum,numQ);
            ArrayList<String> answers = filter.getAnswers(new DImage(keyIn));
            if(!answers.get(currQ-1).equals(key.get(currQ-1))) {
                wrong++;
            }
        }
        return wrong;
    }

    private static String addScoresContent(String fileContent, int numQ, PImage keyIn, ArrayList<String> key){
        for (int pageNum = 1; pageNum < 7; pageNum++) {
            keyIn = PDFHelper.getPageImage("assets/OfficialOMRSampleDoc.pdf", pageNum);
            DisplayInfoFilter filter = new DisplayInfoFilter(pageNum,numQ);
            ArrayList<String> answers = filter.getAnswers(new DImage(keyIn));
            fileContent+="\n"+compareToKey(pageNum,key,answers);
        }
        return fileContent;
    }

    private static String addKey(String fileContent, ArrayList<String> key, int numQ) {
        fileContent+= "(key) 1. "+numQ;
        for (int i = 0; i < numQ; i++) {
            fileContent+=", "+key.get(i);
        }
        return fileContent+"\n";
    }

    private static String createHeader(String fileContent, int numQ) {
        fileContent+= "page. # right";
        for (int currQ = 1; currQ <= numQ; currQ++) {
            fileContent+=", q"+(currQ);
        }
        return fileContent + "\n";
    }

    private static String compareToKey(int pageNum, ArrayList<String> key, ArrayList<String> answers) {
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

    private static void writeDataToFile(String filePath, String data) throws IOException {
        try (FileWriter f = new FileWriter(filePath);
            BufferedWriter b = new BufferedWriter(f);
            PrintWriter writer = new PrintWriter(b)) {
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