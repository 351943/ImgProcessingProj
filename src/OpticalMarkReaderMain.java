import Filters.DisplayInfoFilter;

import javax.swing.*;
import java.io.*;
import java.util.ArrayList;

public class OpticalMarkReaderMain {

    public static void main(String[] args) {
        String pathToPdf = fileChooser();
        String fileContent = "";
        ArrayList<String>answerList = new ArrayList<>();
        System.out.println("Loading pdf at " + pathToPdf);

        /*
        Your code here to...
        (1).  Load the pdf
        (2).  Loop over its pages
        (3).  Create a DImage from each page and process its pixels
        (4).  Output 2 csv files
         */

        //answerList = getAnswers(img);
        //inputAnswerFile(fileContent,answerList);
        
    }

    private void inputAnswerFile(String fileContent, ArrayList<String> list) throws IOException {
        for (int index = 0; index < list.size(); index++) {
            fileContent+=index+": "+ list.get(index);
        }
        writeDataToFile("Answers",fileContent);
    }
    
    
    public static void writeDataToFile(String filePath, String data) throws IOException {
        try (FileWriter f = new FileWriter(filePath);
             BufferedWriter b = new BufferedWriter(f);
             PrintWriter writer = new PrintWriter(b);) {
            writer.println(data);
        } catch (IOException error) {
            System.err.println("There was a problem writing to the file: " + filePath);
            error.printStackTrace();
        }
    }
    
    private static String fileChooser() {
        String userDirLocation = System.getProperty("user.dir");
        File userDir = new File(userDirLocation);
        JFileChooser fc = new JFileChooser(userDir);
        int returnVal = fc.showOpenDialog(null);
        File file = fc.getSelectedFile();
        return file.getAbsolutePath();
    }
}
