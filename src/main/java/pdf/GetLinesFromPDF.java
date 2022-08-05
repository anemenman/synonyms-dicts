package pdf;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.apache.pdfbox.text.TextPosition;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class GetLinesFromPDF extends PDFTextStripper {
    static List<String> lines = new ArrayList<>();

    private boolean hyphen = false;

    public GetLinesFromPDF() throws IOException {
    }

    /**
     * @throws IOException If there is an error parsing the document.
     */
    public static void main(String[] args) throws IOException {
        PDDocument document = null;
        String fileName = "synonim.pdf";
        try {
            document = PDDocument.load(new File(fileName));
            PDFTextStripper stripper = new GetLinesFromPDF();
            stripper.setSortByPosition(true);
//            stripper.setStartPage(0);
//            stripper.setEndPage(document.getNumberOfPages());

            stripper.setStartPage(22);
            stripper.setEndPage(100);

            Writer dummy = new OutputStreamWriter(new ByteArrayOutputStream());
            stripper.writeText(document, dummy);

            // print lines
            System.out.println("!!!!!!!!!");
            for (String line : lines) {
                System.out.println("line--->" + line);
            }
        } finally {
            if (document != null) {
                document.close();
            }
        }
    }

    /**
     * Override the default functionality of PDFTextStripper.writeString()
     */
    @Override
    protected void writeString(String str, List<TextPosition> textPositions) throws IOException {
        if (textPositions.size() > 0) {
            float fontSize = textPositions.get(0).getFontSize();
//            System.out.println("fontSize--->" + fontSize);
//            System.out.println("str--->" + str);
//            if (fontSize == 10.328f || fontSize == 11.3f) {
            if (fontSize >= 10.0f) {


                if (hyphen) {
                    String prevStr = lines.get(lines.size() - 1);
                    System.out.println("prevStr-->" + prevStr);
                    System.out.println("str-->" + str);
                    str = prevStr.substring(0, prevStr.length() - 1).concat(str);
                    System.out.println("newStr--->" + str);
                    lines.remove(lines.size() - 1);
//                    lines.add(lines.size() - 1, prevStr);
                    hyphen = false;
                }
//                else {
//                    lines.add(str);
//                }

                lines.add(str);
//
                if (str.endsWith("-")) {
                    hyphen = true;
                }
////
//                if (str.endsWith("-")) {
//                    hyphen = true;
//                    str = str.substring(0, str.length() - 1);
//                } else {
//                    hyphen = false;
//                }
//                lines.add(str);
            }
            // you may process the line here itself, as and when it is obtained
        }
    }
}
