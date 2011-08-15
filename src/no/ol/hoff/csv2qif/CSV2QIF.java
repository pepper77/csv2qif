package no.ol.hoff.csv2qif;

import no.ol.hoff.csv2qif.gui.MainGUI;
import org.apache.log4j.Logger;

import javax.swing.*;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Created Pål Arne Hoff
 * Date: 8/10/11
 * Time: 9:44 PM
 */
public class CSV2QIF {
  private static Logger logger = Logger.getLogger(CSV2QIF.class);

  public static void main(String args[]) {
    logger.debug("Entering main(args=" + args.toString() + ")");
    // Start up GUI.
    MainGUI gui = new MainGUI();
    // Read CSV file into table.
    JTable transactionTable = gui.getTransactionTable();

//    transactionTable.addColumn(new TableColumn());
    logger.debug("Slutt");
  }

  public void readCSVFile(String args[]) {
    logger.debug("Entering readCSVFile(args=" + args.toString() + ")");
    try {
      InputStream fileInputStream = new FileInputStream(args[0]);
      BufferedReader reader = new BufferedReader(new InputStreamReader(fileInputStream, "ISO8859_15"));
      while (true) {
        String line = reader.readLine();
        if (line == null) break;
        String[] fields = line.split(",");
        // process fields here
        for (String s : fields) {
          System.out.println(s);
        }
      }
      reader.close();
    } catch (Exception e) {
      System.out.println(e.toString());
    } finally {
    }
  }
}

