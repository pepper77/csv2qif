/**
 * Created by IntelliJ IDEA.
 * User: pepper
 * Date: 11.08.11
 * Time: 15:09
 * To change this template use File | Settings | File Templates.
 */
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Created by IntelliJ IDEA.
 * User: pepper
 * Date: 8/10/11
 * Time: 9:44 PM
 * To change this template use File | Settings | File Templates.
 */
public class CSV2QIF {
    public static void main(String args[]) {
        try {
            InputStream fileInputStream = new FileInputStream(args[0]);
            BufferedReader reader = new BufferedReader(new InputStreamReader(fileInputStream, "ISO8859_15"));
            while (true) {
                String line = reader.readLine();
                if (line == null) break;
                String[] fields = line.split(",");
                // process fields here
                for (int x = 0; x < fields.length; x++) {
                    System.out.println(fields[x]);
                }

            }
            reader.close();
        } catch (Exception e) {
            System.out.println(e.toString());
        } finally {
        }
    }
}

