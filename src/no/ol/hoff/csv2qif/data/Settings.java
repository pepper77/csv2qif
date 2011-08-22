package no.ol.hoff.csv2qif.data;

import org.apache.log4j.Logger;

import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Enumeration;
import java.util.prefs.Preferences;

/**
 * A Singleton class for keeping various settings used in the application.
 *
 * @author Pål Arne Hoff
 * @version 1.0
 * @serial 6.oct 2003
 */
public class Settings {
  private static Logger logger = Logger.getLogger(Settings.class);
  /**
   * This is the max number of lines that will be fetched from the database, no
   * matter how many lines is set as limit in the GUI.
   */
  private String pathToResultFiles = "~";
  Connection con;
  //  private DBConnectionData currentConnection;
  private DefaultTreeModel packageTree;
  private DefaultTreeModel eventTypeTree;
  public static final String GENERAL_PREFERENCES_NODE = "cvs2qif";


  /**
   * Gets the packageTree stored in this object.
   *
   * @return A {@link DefaultTreeModel} containing the tree with appliances,
   *         packages and backends.
   */
  public DefaultTreeModel getPackageTree() {
    logger.debug("Entering getPackageTree()");
    return packageTree;
  }


  /**
   * Gets the eventTypeTree stored in this object.
   *
   * @return A {@link DefaultTreeModel} containing the tree event-types.
   */
  public DefaultTreeModel getEventTypeTree() {
    logger.debug("Entering getEventTypeTree()");
    return eventTypeTree;
  }


  /**
   * This method is run before the class is removed by the GarbageCollector. It
   * closes the database connection in case it hasn't been closed already.
   *
   * @throws Throwable
   */
  protected void finalize()
      throws Throwable {
    super.finalize();
    if (con != null)
      if (!con.isClosed())
        con.close();
  }


  /**
   * Sets the path where you will save result-files.
   *
   * @param path A String containing a path where you want to save result-files.
   */
  public void setPathToResultFiles(String path) {
    logger.debug("Entering setPathToResultFiles(path=" + path + ")");
    if (!path.equals(pathToResultFiles)) {
      pathToResultFiles = path;
      Preferences prefs = Preferences.userRoot().node(Settings.GENERAL_PREFERENCES_NODE);
      prefs.put("pathToResultFiles", pathToResultFiles.replace('/', '#'));
      logger.debug("New path set.");
    }
  }

  /**
   * Returns the path to where the result-files should be saved.
   *
   * @return A String containing the path to the result-files.
   */
  public String getPathToResultFiles() {
    logger.debug("Entering getPathToResultFiles()");
    return pathToResultFiles;
  }
}
