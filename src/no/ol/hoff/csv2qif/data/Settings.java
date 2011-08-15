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
  public static int maxRowsInResult = 100000;
  private String pathToSQLFiles = "";
  private String pathToResultFiles = "~";
  static Logger logger = Logger.getLogger(Settings.class);
  Connection con;
  //  private DBConnectionData currentConnection;
  private DefaultTreeModel packageTree;
  private DefaultTreeModel eventTypeTree;
  public static final String GENERAL_PREFERENCES_NODE = "cvs2qif";


  /**
   * Creates a new instance of Settings
   *
   * @param connectionData The {@link DBConnectionData} to store in the
   *                       Settings-object.
   * @throws Exception Thrown if something goes wrong when loading the
   *                   package three.
   */
  public Settings(DBConnectionData connectionData)
      throws Exception {
    logger.debug("Entering Settings()");
    // Initializing.
    currentConnection = connectionData;

    // Read pathToSQLPath variable from preferences.
    Preferences prefs = Preferences.userRoot().node(Settings.GENERAL_PREFERENCES_NODE);
    pathToSQLFiles = prefs.get("pathToSQLFiles", "none").replace('#', '/');
    // If path is not set, set a default path.
    if (pathToSQLFiles.equals("none")) {
      if (System.getProperty("os.name").equals("Linux"))
        pathToSQLFiles = "/mnt/nfrwork/DRIFT/SQL";
      else
        pathToSQLFiles = "N:/DRIFT/SQL";
      prefs.put("pathToSQLFiles", pathToSQLFiles.replace('/', '#'));
    }

    pathToResultFiles = prefs.get("pathToResultFiles", "none").replace('#', '/');
    if (pathToResultFiles.equals("none")) {
      pathToResultFiles = System.getProperty("user.home");
      prefs.put("pathToResultFiles", pathToResultFiles.replace('/', '#'));
    }

    loadTrees();
  }


  /**
   * Gets the currentConnection that's set in the Settings object.
   *
   * @return The value of the currentConnection.
   */
  public DBConnectionData getCurrentConnection() {
    logger.debug("Entering getCurrentConnection()");
    return currentConnection;
  }


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
   * Sets the current chosen ConnectionData for later use by different object.
   *
   * @param currentConnection The {@link DBConnectionData} to use when connecting
   *                          to the database from this {@link Settings}-object.
   */
  public void setCurrentConnection(DBConnectionData currentConnection) {
    logger.debug("Entering setCurrentConnection(currentConnection=" + currentConnection + ")");
    this.currentConnection = currentConnection;
  }


  /**
   * Loads the package three and the event-type trees from the database.
   *
   * @throws Exception Thrown if something goes wrong when querying the database.
   */
  public void loadTrees()
      throws Exception {
    logger.debug("Entering loadTrees()");

    DefaultMutableTreeNode top = new DefaultMutableTreeNode("Packages");
    packageTree = new DefaultTreeModel(top);

    con = ConnectionFactory.getConnection(currentConnection);

    PreparedStatement prepStmt = null;
    if (!currentConnection.name.startsWith("filosofus"))
      prepStmt = con.prepareStatement(
          "SELECT appliance_name, long_desc as descriptive_name, sensor_ip_addr_int as appliance_type " +
              "FROM vdi_appliances " +
              "ORDER BY long_desc");
    else
      prepStmt = con.prepareStatement(
          "SELECT appliance_name, descriptive_name, appliance_type " +
              "FROM nfrdb_appliances " +
              "ORDER BY descriptive_name");
    ResultSet rs = prepStmt.executeQuery();
    while (rs.next()) {
      NFRAppliance tmpAppliance = new NFRAppliance();
      tmpAppliance.appliance_name = rs.getString("appliance_name");
      tmpAppliance.descriptive_name = rs.getString("descriptive_name");
      tmpAppliance.appliance_type = rs.getString("appliance_type");
      top.add(new DefaultMutableTreeNode(tmpAppliance));
    }
    rs.close();
    prepStmt.close();

    if (!currentConnection.name.startsWith("filosofus"))
      prepStmt = con.prepareStatement(
          "SELECT " +
              "  p.package_int as package_id, p.package_title as package_name, " +
              "  b.backend_int as backend_id, b.backend_title as backend_name, " +
              "  '4' as version, b.backend_title as descriptive_name, p.package_title as package_desc " +
              "FROM vdi_packages p, vdi_backends b " +
              "WHERE p.package_int=b.package_int " +
              "ORDER BY p.package_title, b.backend_title");
    else
      prepStmt = con.prepareStatement(
          "SELECT v.package_id,v.backend_id,v.package_name,v.backend_name,v.version," +
              "t.descriptive_name,pt.descriptive_name package_desc " +
              "FROM nfrdb_backend_versions v,nfrdb_backend_types t, nfrdb_package_types pt " +
              "WHERE v.package_id>9999 AND v.backend_id>0 " +
              "  AND v.package_name=t.package_name " +
              "  AND v.backend_name=t.backend_name " +
              "  AND v.package_name=pt.package_name " +
              "ORDER BY v.package_name, v.backend_name");
    rs = prepStmt.executeQuery();

    int oldPackageID = 0;
    DefaultMutableTreeNode branch = null;
    NFRPackage tmpPackage = null;
    NFRBackend tmpBackend = null;
    while (rs.next()) {
      tmpPackage = new NFRPackage();
      tmpPackage.package_id = rs.getInt("package_id");
      tmpPackage.package_name = rs.getString("package_name");
      tmpPackage.descriptive_name = rs.getString("package_desc");
      logger.debug("Read package: " + tmpPackage.descriptive_name);
      if (oldPackageID != tmpPackage.package_id) {
        if (oldPackageID != 0)
          addBranchToTree(branch, top);
        branch = new DefaultMutableTreeNode(tmpPackage);
        oldPackageID = tmpPackage.package_id;
      }

      tmpBackend = new NFRBackend();
      tmpBackend.backend_id = rs.getInt("backend_id");
      tmpBackend.package_id = rs.getInt("package_id");
      tmpBackend.version = rs.getInt("version");
      tmpBackend.backend_name = rs.getString("backend_name");
      tmpBackend.descriptive_name = rs.getString("descriptive_name");
      branch.add(new DefaultMutableTreeNode(tmpBackend));
    }
    // Add last branch
    logger.debug("Outside loop.");
    addBranchToTree(branch, top);
    // close query-spesific connection objects.
    prepStmt.close();
    rs.close();

    //
    // Build event-tree
    //

    // Make top node of Event-tree.
    EventType topNode = new EventType();
    topNode.setEvent_type_name("Events");
    topNode.setEvent_type_id(0);
    topNode.setParent_id(0);
    top = new DefaultMutableTreeNode(topNode);
    // Create tree starting with the given top-node.
    eventTypeTree = new DefaultTreeModel(top);

    try {
      // Load event-types.
      prepStmt = con.prepareStatement(
          "SELECT * " +
              "FROM type " +
              "ORDER BY parent_id, type_name");
      rs = prepStmt.executeQuery();

      // Create an event-type object to use as temporary storage when going
      // thorugh the ResultSet from the query.
      EventType tmpEventType = null;
      while (rs.next()) {
        // Make a new EventType object...
        tmpEventType = new EventType();
        // ... and fill it with values.
        tmpEventType.setEvent_type_id(rs.getInt("type_id"));
        tmpEventType.setParent_id(rs.getInt("parent_id"));
        tmpEventType.setEvent_type_name(rs.getString("type_name"));
        tmpEventType.setComment_id(rs.getInt("comment_id"));
        // Add the new object to the tree.
        addEventToTree(top, tmpEventType);
      }
    } catch (Exception e) {
      logger.error(e);
    }

    // Close all connection objects.
    prepStmt.close();
    rs.close();
    con.close();
  }

  /**
   * This method takes a top TreeNode and an {@link EventType} and put the incoming
   * {@link no.stat.vdi.analyzer.data.EventType} in the right location in the
   * tree-structure.
   *
   * @param top
   * @param eventType
   */
  private void addEventToTree(DefaultMutableTreeNode top, EventType eventType) {
    logger.debug("Entering addEventToTree(top=" + top + ", eventType=" + eventType + ")");
    // Get an enumeration of all elements in the tree.
    Enumeration enu = top.breadthFirstEnumeration();
    // Go through all the elements.
    while (enu.hasMoreElements()) {
      // Get next element.
      DefaultMutableTreeNode tmp = (DefaultMutableTreeNode) enu.nextElement();
      // If event_type_id in the tmp element equals the parent_id of the incoming
      // element then add the incoming element to the tmp element.
      if (((EventType) tmp.getUserObject()).getEvent_type_id() == eventType.getParent_id()) {
        // Make a new TreeNode...
        DefaultMutableTreeNode newNode = new DefaultMutableTreeNode(eventType);
        // ...and add it on the right place.
        tmp.add(newNode);
      }
    }
  }


  /**
   * Returns a String representation of the object.
   *
   * @return A String representation of this object.
   */
  public String toString() {
    return "Settings: currentConnection=" + currentConnection +
        ", packageTree=" + packageTree;
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
   * Adds a new branch to the top object.
   *
   * @param branch The {@link DefaultMutableTreeNode} to add.
   * @param top    The {@link DefaultMutableTreeNode} to add the branch to.
   */
  private void addBranchToTree(DefaultMutableTreeNode branch, DefaultMutableTreeNode top) {
    logger.debug("Entering addBranchToTree(branch=" + branch + ", top=" + top + ")");
    if (branch != null) {
      Enumeration children = top.children();
      while (children.hasMoreElements()) {
        /* Because the add command in DefaultMuteableTreeNode removes the
         * TreeNode from the source when adding, I have to make a new code
         * of the branch before each add...
         */
        DefaultMutableTreeNode tmpBranch = (DefaultMutableTreeNode) branch.clone();
        Enumeration tmpChildren = branch.children();
        while (tmpChildren.hasMoreElements())
          tmpBranch.add((DefaultMutableTreeNode) ((DefaultMutableTreeNode) tmpChildren.nextElement()).clone());
        // And finally I can add it.
        ((DefaultMutableTreeNode) children.nextElement()).add(tmpBranch);
      }
    }
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
   * Sets the path where you can find the SQL-files.
   *
   * @param path A String containing the path to the SQL-files.
   */
  public void setPathToSQLFiles(String path) {
    logger.debug("Entering setPathToSQLFiles(path=" + path + ")");
    if (!path.equals(pathToSQLFiles)) {
      pathToSQLFiles = path;
      Preferences prefs = Preferences.userRoot().node(Settings.GENERAL_PREFERENCES_NODE);
      prefs.put("pathToSQLFiles", pathToSQLFiles.replace('/', '#'));
      logger.debug("New path set.");
    }
  }

  /**
   * Returns the path where the SQL-files are located.
   *
   * @return A String containing the path where you can find the SQL-files.
   */
  public String getPathToSQLFiles() {
    logger.debug("Entering getPathToSQLFiles()");
    return pathToSQLFiles;
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
