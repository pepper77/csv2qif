package no.ol.hoff.csv2qif.data;


import no.stat.vdi.analyzer.Settings;
import no.stat.vdi.analyzer.gui.CopySavePropertiesEditor;
import org.apache.log4j.Logger;

import java.util.prefs.Preferences;


/**
 * Created by IntelliJ IDEA.
 *
 * @author Pål Arne Hoff
 * @serial 25.nov 2003
 */
public class GlobalSettings {
  private static Logger logger = Logger.getLogger(GlobalSettings.class);
  private static GlobalSettings instance;
  private CopySaveProperties copyProperties;
  private CopySaveProperties saveProperties;


  private GlobalSettings() {
    logger.debug("Entering GlobalSettings()");
    // Initialize (in case nothing is stored in preferences).
    copyProperties = new CopySaveProperties();
    copyProperties.cellEnclosing = "";
    copyProperties.betweenCells = '\t';
    saveProperties = new CopySaveProperties();

    // Read from preferences.
    Preferences prefs =
        Preferences.userRoot().node(Settings.GENERAL_PREFERENCES_NODE);
    copyProperties.cellEnclosing = prefs.get("copy/cellEnclosing", copyProperties.cellEnclosing);
    copyProperties.betweenCells = (char) prefs.getInt("copy/betweenCells", copyProperties.betweenCells);
    copyProperties.endOfLine =
        CopySavePropertiesEditor.getEndOfLine(prefs.getInt("copy/endOfLine",
            CopySavePropertiesEditor.getEndOfLineIndex(copyProperties.endOfLine)));
    copyProperties.includeHeaders = prefs.getBoolean("copy/includeHeader", copyProperties.includeHeaders);
    copyProperties.encloseSingleColumn = prefs.getBoolean("copy/encloseSingleColumn", copyProperties.encloseSingleColumn);
    copyProperties.nullValue = prefs.get("copy/nullValue", copyProperties.nullValue);

    saveProperties.cellEnclosing = prefs.get("save/cellEnclosing", saveProperties.cellEnclosing);
    saveProperties.betweenCells = (char) prefs.getInt("save/betweenCells", saveProperties.betweenCells);
    saveProperties.endOfLine =
        CopySavePropertiesEditor.getEndOfLine(prefs.getInt("save/endOfLine",
            CopySavePropertiesEditor.getEndOfLineIndex(saveProperties.endOfLine)));
    saveProperties.includeHeaders = prefs.getBoolean("save/includeHeader", saveProperties.includeHeaders);
    saveProperties.encloseSingleColumn = prefs.getBoolean("save/encloseSingleColumn", saveProperties.encloseSingleColumn);
    saveProperties.nullValue = prefs.get("save/nullValue", saveProperties.nullValue);
  }

  public static GlobalSettings getInstance() {
    logger.debug("Entering getInstance()");
    if (instance == null)
      instance = new GlobalSettings();
    return instance;
  }

  public CopySaveProperties getCopyProperties() {
    logger.debug("Entering getCopyProperties()");
    return copyProperties;
  }

  public void setCopyProperties(CopySaveProperties copyProperties) {
    logger.debug("Entering setCopyProperties(copyProperties=" + copyProperties + ")");
    this.copyProperties = copyProperties;
  }

  public CopySaveProperties getSaveProperties() {
    logger.debug("Entering getSaveProperties()");
    return saveProperties;
  }

  public void setSaveProperties(CopySaveProperties saveProperties) {
    logger.debug("Entering setSaveProperties(saveProperties=" + saveProperties + ")");
    this.saveProperties = saveProperties;
  }

  public void storePreferences() {
    logger.debug("Entering storePreferences()");
    Preferences prefs =
        Preferences.userRoot().node(Settings.GENERAL_PREFERENCES_NODE);
    prefs.put("copy/cellEnclosing", copyProperties.cellEnclosing);
    prefs.putInt("copy/betweenCells", copyProperties.betweenCells);
    prefs.putInt("copy/endOfLine", CopySavePropertiesEditor.getEndOfLineIndex(copyProperties.endOfLine));
    prefs.putBoolean("copy/includeHeader", copyProperties.includeHeaders);
    prefs.putBoolean("copy/encloseSingleColumn", copyProperties.encloseSingleColumn);
    prefs.put("copy/nullValue", copyProperties.nullValue);

    prefs.put("save/cellEnclosing", saveProperties.cellEnclosing);
    prefs.putInt("save/betweenCells", saveProperties.betweenCells);
    prefs.putInt("save/endOfLine", CopySavePropertiesEditor.getEndOfLineIndex(saveProperties.endOfLine));
    prefs.putBoolean("save/includeHeader", saveProperties.includeHeaders);
    prefs.putBoolean("save/encloseSingleColumn", saveProperties.encloseSingleColumn);
    prefs.put("save/nullValue", saveProperties.nullValue);
  }
}
