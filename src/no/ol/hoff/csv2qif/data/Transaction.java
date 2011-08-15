package no.ol.hoff.csv2qif.data;

import org.apache.log4j.Logger;

import java.sql.Date;

/**
 * Created Pål Arne Hoff
 * Date: 11.08.11
 * Time: 15:02
 */
public class Transaction {
  private static Logger logger = Logger.getLogger(Transaction.class);
  private Date date;
  private String Payee;
  private String Category;
  private float Value;
  private String Memo;

  public Transaction() {
    logger.debug("Creating");
  }

  public Date getDate() {
    logger.debug("Entering getDate()");
    return date;
  }

  public void setDate(Date date) {
    logger.debug("Entering setDate(" + date + ")");
    this.date = date;
  }

  public String getPayee() {
    logger.debug("Entering getPayee()");
    return Payee;
  }

  public void setPayee(String payee) {
    logger.debug("Entering setPayee(" + payee + ")");
    Payee = payee;
  }

  public String getCategory() {
    logger.debug("Entering getCategory()");
    return Category;
  }

  public void setCategory(String category) {
    logger.debug("Entering setCategory(" + category + ")");
    Category = category;
  }

  public float getValue() {
    logger.debug("Entering getValue()");
    return Value;
  }

  public void setValue(float value) {
    logger.debug("Entering setValue(" + value + ")");
    Value = value;
  }

  public String getMemo() {
    logger.debug("Entering getMemo()");
    return Memo;
  }

  public void setMemo(String memo) {
    logger.debug("Entering setMemo(memo=" + memo + ")");
    Memo = memo;
  }
}
