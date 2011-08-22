package no.ol.hoff.csv2qif;

import com.sun.org.apache.bcel.internal.generic.NEW;
import no.ol.hoff.csv2qif.gui.MainGUI;
import org.apache.log4j.Logger;
import org.codehaus.groovy.tools.groovydoc.Main;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
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
    new CSV2QIF();
    logger.debug("Slutt");
  }

  public CSV2QIF() {
    logger.debug("Entering CSV2QIF()");
    // Start up GUI.
    MainGUI gui = new MainGUI();
    // Read CSV file into table.
    JTable transactionTable = gui.getTransactionTable();
    DefaultTableModel dtm = readCSVFile("/Users/pepper/IdeaProjects/brukskonto.csv");
    transactionTable.setModel(dtm);
    transactionTable.repaint();
    gui.pack();

//    transactionTable.addColumn(new TableColumn());
  }

  public DefaultTableModel readCSVFile(String file) {
    logger.debug("Entering readCSVFile(args=" + file + ")");
    DefaultTableModel dtm = new DefaultTableModel();
    try {
      InputStream fileInputStream = new FileInputStream(file);
      BufferedReader reader = new BufferedReader(new InputStreamReader(fileInputStream, "ISO8859_15"));
      while (true) {
        String line = reader.readLine();
        if (line == null) break;
        String[] fields = line.split(",");
        // process fields here
        for (String s : fields) {
          System.out.println(s);
        }
        dtm.addRow(fields);
      }
      reader.close();
    } catch (Exception e) {
      System.out.println(e.toString());
    }
    System.out.println("Return dtm=" + dtm.toString());
    return dtm;
  }
}

