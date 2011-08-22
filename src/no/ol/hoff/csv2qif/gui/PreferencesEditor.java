package no.ol.hoff.csv2qif.gui;

import javax.swing.*;

/**
 * Allows editing of a set of related user preferences.
 * <p/>
 * Implementations of this interface do not define a "stand-alone" GUI,
 * but rather a component (usually a <code>JPanel</code>) which can be used by the
 * caller in any way they want. Typically, a set of <code>PreferencesEditor</code>
 * objects are placed in a <code>JTabbedPane</code>, one per pane.
 *
 * @author <a href="http://www.javapractices.com/">javapractices.com</a>
 */
public interface PreferencesEditor {

  /**
   * Return a GUI component which allows the user to edit this set of related
   * preferences.
   */
  JComponent getUI();

  /**
   * The name of the tab in which this <code>PreferencesEditor</code>
   * will be placed.
   */
  String getTitle();

  /**
   * The mnemonic to appear in the tab name.
   * <p/>
   * Must match a letter appearing in {@link #getTitle}.
   * Use constants defined in <code>KeyEvent</code>, for example <code>KeyEvent.VK_A</code>.
   */
  int getMnemonic();

  /**
   * Store the related preferences as they are currently displayed, overwriting
   * all corresponding settings.
   */
  void savePreferences();

  /**
   * Reset the related preferences to their default values, but only as
   * presented in the GUI, without affecting stored preference values.
   * <p/>
   * This method may not apply in all cases. For example, if the item
   * represents a config which has no meaningful default value (such as a mail server
   * name), the desired behavior may be to only allow a manual change. In such a
   * case, the implementation of this method must be a no-operation.
   */
  void matchGuiToDefaultPreferences();
}