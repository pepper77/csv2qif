package no.ol.hoff.csv2qif;

/**
 * This interface is created solely to have a common interface for accessing methods
 * in classes that produce a RESULT-property in the form of a
 * {@link javax.swing.table.DefaultTableModel}.
 *
 * @author Pål Arne Hoff
 * @version 1.0
 * @serial 16.jan 2004
 */
public interface ResultProvider {
  /**
   * This field is used to describe a {@link java.beans.PropertyChangeEvent} that signals
   * an error in the query. The actual {@link java.beans.PropertyChangeEvent} contains null
   * as old value and the {@link Exception} as new value.
   */
  public final static String ERROR = "Error Property";
  /**
   * This field is used to describe a {@link java.beans.PropertyChangeEvent} that signals
   * a succesful query. The actual {@link java.beans.PropertyChangeEvent} contains null
   * as old value and the resulting {@link javax.swing.table.DefaultTableModel} as new value.
   */
  public final static String RESULT = "Result Property";

  /**
   * Gets an array of Strings containing the values with the greatest length
   * from all columns in the result-table.
   *
   * @return An array of Strings containing the values with the greatest length
   *         from all columns in the result-table.
   */
  public String[] getColumnSizes();
}
