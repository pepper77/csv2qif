package no.ol.hoff.csv2qif.gui;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.List;

import no.ol.hoff.csv2qif.ResultProvider;
import no.ol.hoff.csv2qif.data.CopySaveProperties;
import no.ol.hoff.csv2qif.data.GlobalSettings;
import org.apache.log4j.Logger;

/**
 * This class is used to display a select file dialog and the actual loading of
 * the selected file. It extends the Thread-class and starts loading of a file in
 * a new thread to make the prosess run in the background.
 * <p/>
 * The class sends two kinds of {@link java.beans.PropertyChangeEvent}s:
 * <ul>
 * <li>{@link FileLoader#TEXT} if a textfile is loaded.
 * <li>{@link ResultProvider#RESULT} when a DefaultTableModel is loaded.
 * <li>{@link ResultProvider#ERROR} when an error occurs while loading the file.
 * </ul>
 *
 * @author Pål Arne Hoff
 * @version 1.0
 * @serial 14.jan.2004
 */
public class FileLoader extends SwingWorker implements ResultProvider {
  /**
   * This field is used to describe a {@link java.beans.PropertyChangeEvent} that signals
   * succesful loading of a textfile. The actual {@link java.beans.PropertyChangeEvent}
   * contains null as old value and a pointer to the resulting {@link String}
   * as new value.
   */
  public final static String TEXT = "Text Property";
  private static Logger logger = Logger.getLogger(FileLoader.class);
  private List textFileEndings = new ArrayList();
  private List commaSeparatedEndings = new ArrayList();
  private PropertyChangeSupport propertySupport;
  private Component parent;
  private File selectedFile;
  private CopySaveProperties loadProperties;
  private String[] columnSizes;
  private DefaultTableModel resultTable;
  private Exception errorMessage;
  private String resultTextFile;


  /**
   * Creates a new FileLoader-object.
   *
   * @param parent The parent Component of this object.
   */
  public FileLoader(Component parent) {
    logger.debug("Entering FileLoader(parent=" + parent + ")");
    this.parent = parent;
    propertySupport = new PropertyChangeSupport(this);
    textFileEndings.add("txt");
    textFileEndings.add("sql");
    commaSeparatedEndings.add("csv");
  }


  /**
   * This method displays a file-chooser-dialog, and then starts a new Thread loading
   * the selected file.
   *
   * @param fc A path to the directory where the file-dialog will open.
   * @return A File-object representing the selected file.
   */
  public File load(JFileChooser fc) {
    logger.debug("Entering load(fc=" + fc + ")");
    // Show open file dialog.
    int returnVal = fc.showOpenDialog(parent);

    if (returnVal == JFileChooser.APPROVE_OPTION) {
      selectedFile = fc.getSelectedFile();
      logger.debug("selectedFile=" + selectedFile);
      // Start loading of file in a new thread.
      start();
    }
    return selectedFile;
  }


  /**
   * Adds the incoming {@link java.beans.PropertyChangeListener} from the list of listeners.
   *
   * @param listener The {@link java.beans.PropertyChangeListener} to add.
   */
  public void addPropertyChangeListener(PropertyChangeListener listener) {
    logger.debug("Entering addPropertyChangeListener(listener=" + listener + ")");
    propertySupport.addPropertyChangeListener(listener);
  }


  /**
   * Removes the incoming {@link java.beans.PropertyChangeListener} from the list
   * of listeners.
   *
   * @param listener The {@link java.beans.PropertyChangeListener} to remove.
   */
  public void removePropertyChangeListener(PropertyChangeListener listener) {
    logger.debug("Entering removePropertyChangeListener(listener=" + listener + ")");
    propertySupport.removePropertyChangeListener(listener);
  }


  /**
   * The actual work to be done in the thread.
   *
   * @return The value resulting from running this method.
   */
  public Object construct() {
    logger.debug("Entering construct()");
    if (selectedFile.isFile()) {
      String[] tmp = selectedFile.getName().split("[.]");
      String fileEnding = tmp[tmp.length - 1];
      try {
        if (textFileEndings.contains(fileEnding))
          loadTextFile();

        else if (commaSeparatedEndings.contains(fileEnding))
          loadCSVFile();
      } catch (IOException e) {
        // Set errorMessage for use in finished() method.
        errorMessage = e;
      }
    }

    return resultTable;
  }


  public void finished() {
    logger.debug("Entering finished()");
    if (resultTable != null)
      propertySupport.firePropertyChange(FileLoader.RESULT, null, resultTable);

    else if (resultTextFile != null)
      propertySupport.firePropertyChange(FileLoader.TEXT, null, resultTextFile);

    else
      propertySupport.firePropertyChange(FileLoader.ERROR, null, errorMessage);
  }


  /**
   * This methods uses the file information in {@link FileLoader#selectedFile}
   * to load a text-file into memory.
   *
   * @throws IOException If something goes wrong while accessing the file.
   */
  private void loadTextFile()
      throws IOException {
    logger.debug("Entering loadTextFile()");
    FileReader fr = new FileReader(selectedFile);
    int ch = 0;
    StringBuffer readFile = new StringBuffer();
    while (ch != -1) {
      ch = fr.read();
      if (ch != -1)
        readFile.append((char) ch);
    }
    fr.close();
    resultTextFile = readFile.toString();
  }


  /**
   * This methods uses the file information in {@link FileLoader#selectedFile}
   * to load a comma-separated file into memory.
   *
   * @throws IOException If something goes wrong while accessing the file.
   */
  private void loadCSVFile()
      throws IOException {
    logger.debug("Entering loadCSVFile()");
    RandomAccessFile rafile = new RandomAccessFile(selectedFile, "r");
    // Set this before calling getValuesFromLine.
    // TODO: Make dialog for changing these values if necessary.
    loadProperties = GlobalSettings.getInstance().getSaveProperties();
    DefaultTableModel dtm = new DefaultTableModel();
    if (loadProperties.includeHeaders) {
      // Initialize columnSizes.
      columnSizes = getValuesFromLine(rafile.readLine());
      dtm.setColumnIdentifiers(columnSizes);
    }
    String line = "";
    while ((line = rafile.readLine()) != null) {
      String[] values = getValuesFromLine(line);
      for (int i = 0; i < values.length; i++) {
        // Find longest value of each column.
        if (columnSizes[i].length() < values[i].length())
          columnSizes[i] = values[i];
        // Replace nullValue with null.
        if (values[i].equals(loadProperties.nullValue))
          values[i] = null;
      }
      dtm.addRow(values);
    }
    rafile.close();
    resultTable = dtm;
  }


  /**
   * This method splits an incoming String containing commaseparated values into
   * an array of String-values.
   *
   * @param line The String containing the comma-separated values.
   * @return A String-array of values.
   */
  private String[] getValuesFromLine(String line) {
    logger.debug("Entering getValuesFromLine(line=" + line + ")");
    line = line.trim();
    if (line.startsWith(loadProperties.cellEnclosing) &&
        line.endsWith(loadProperties.cellEnclosing))
      line = line.substring(1, line.length() - 1);
    return line.split(
        "[" + loadProperties.cellEnclosing + "][" +
            loadProperties.betweenCells + "][" +
            loadProperties.cellEnclosing + "]");
  }


  /**
   * Gets an array of Strings containing all the longest values of each column.
   * This can be used to automatically set width of the table-columns.
   *
   * @return An array of Strings containing the longest values (in number of
   *         characters) of each column.
   */
  public String[] getColumnSizes() {
    return columnSizes;
  }
}
