import javax.swing.*;
import javax.swing.table.TableColumn;
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
  public static void main(String args[]) {
    System.out.println("Start");
    MainGUI gui = new MainGUI();

    JTable transactionTable = gui.getTransactionTable();
    transactionTable.addColumn(new TableColumn());
    System.out.println("Slutt");
  }

  public void readCSVFile(String args[]) {
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

