package KarateStructure.Database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Properties;

public class DBAccess {

    private static Connection _con = null;
    private static void createDBConncetion(){
        String url;
        try {
            Class.forName("org.postgresql.Driver");
            Properties props = new Properties();
            props.setProperty("user", "pgwadmin");
            props.setProperty("password", "auvGP5TGcIPgdxtvxlnX");
            url = "jdbc:postgresql://pgw.cluster-caq5m9vcrmcb.eu-west-1.rds.amazonaws.com:5432/rms_pgw_db";
            _con = DriverManager.getConnection(url, props);
            System.out.println("Connection created");
        }
        catch (Exception e) {
            e.printStackTrace();
            System.out.println(e.toString());
        }

    }

    private static void closeDBConnection(){
        try {
            _con.close();
            System.out.println("Connection closed");
        }
        catch (Exception e) {
            System.out.println(e.toString());
        }
    }

    public static void deleteRecord(String apiKey){

        String sessionID = "";
        try {
            createDBConncetion();
            String query = "delete from public.checkout_session  where  api_key ='" + apiKey+"'";

            // create the java statement
            Statement st = _con.createStatement();

            // execute the query, and get a java resultset
            ResultSet rs = st.executeQuery(query);
            st.close();
            closeDBConnection();
        }
        catch (Exception e) {
            System.out.println(e.toString());
        }
    }

    public static String getValue(String columnName){
        String url;
        String item = "";
        try {
            createDBConncetion();
            String query = "select "+ columnName + " from public.checkout_session where api_key='apiKey1'";

            // create the java statement
            Statement st = _con.createStatement();

            // execute the query, and get a java resultset
            ResultSet rs = st.executeQuery(query);
            while (rs.next()) {
                item = rs.getString(columnName);
            }
            st.close();
            closeDBConnection();
        }
        catch (Exception e) {
            System.out.println(e.toString());
        }

        return item;
    }

    public static String getValue(String columnName, String apiKay){
        String url;
        String item = "";
        try {
            createDBConncetion();
            String query = "select "+ columnName + " from public.checkout_session where api_key='"+apiKay+"'";

            // create the java statement
            Statement st = _con.createStatement();

            // execute the query, and get a java resultset
            ResultSet rs = st.executeQuery(query);
            while (rs.next()) {
                item = rs.getString(columnName);
            }
            st.close();
            closeDBConnection();
        }
        catch (Exception e) {
            System.out.println(e.toString());
        }

        return item;
    }
}
