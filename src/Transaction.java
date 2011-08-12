import java.sql.Date;

/**
 * Created Pål Arne Hoff
 * Date: 11.08.11
 * Time: 15:02
 */
public class Transaction {
    private Date date;
    private String Payee;
    private String Category;
    private float Value;
    private String Memo;

    public Transaction() {
    }

    public Date getDate() {

        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getPayee() {
        return Payee;
    }

    public void setPayee(String payee) {
        Payee = payee;
    }

    public String getCategory() {
        return Category;
    }

    public void setCategory(String category) {
        Category = category;
    }

    public float getValue() {
        return Value;
    }

    public void setValue(float value) {
        Value = value;
    }

    public String getMemo() {
        return Memo;
    }

    public void setMemo(String memo) {
        Memo = memo;
    }
}
