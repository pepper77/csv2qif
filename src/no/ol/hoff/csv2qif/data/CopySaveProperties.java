package no.ol.hoff.csv2qif.data;


import org.apache.log4j.Logger;

/**
 * Created by IntelliJ IDEA.
 *
 * @author Pål Arne Hoff
 * @serial 25.nov 2003
 */
public class CopySaveProperties {
  private static Logger logger = Logger.getLogger(CopySaveProperties.class);
  public boolean includeHeaders;
  public String endOfLine;
  public char betweenCells;
  public String cellEnclosing;
  public boolean encloseSingleColumn;
  public String nullValue;

  public CopySaveProperties() {
    logger.debug("Creating CopySaveProperties()");
    includeHeaders = true;
    if (System.getProperty("os.name").equals("Linux"))
      endOfLine = "\n";
      // todo: Check if this is right ending for Windows and Mac.
    else if (System.getProperty("os.name").equals("Windows"))
      endOfLine = "\r\n";
    else
      endOfLine = "\r";

    betweenCells = ';';
    cellEnclosing = "\"";
    encloseSingleColumn = false;
    nullValue = "{NULL}";
  }
}
