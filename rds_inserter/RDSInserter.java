import java.sql.*;
import java.io.*;

class RDSInserter {

    public static void main (String[] args) throws java.io.IOException{
        String endpoint = args[0];
        String username = args[1];
        String password = args[2];

        String insert_tplt = "INSERT INTO balances (ssn" +
                ", last_name" +
                ", first_name" +
                ", account_number" +
                ", balance" +
                ", type" +
                ", address)\n" +
                "VALUES ('%s','%s','%s','%s','%s','%s','%s');";

        FileInputStream fstream = new FileInputStream("../input/SSN_LN_FN_AN_Bal_Type_Addr.txt");
        BufferedReader br = new BufferedReader(new InputStreamReader(fstream));
        try {
            String url = String.format("jdbc:postgresql://%s:5432/transactions", endpoint);
            Class.forName("org.postgresql.Driver");
            Connection conn = DriverManager.getConnection(url,username,password);
            Statement st = conn.createStatement();
            st.executeUpdate("CREATE TABLE balances (\n" +
                    " ssn varchar(16) \n" +
                    " ,last_name varchar(16)\n" +
                    " ,first_name varchar(16)\n" +
                    " ,account_number varchar(16)\n" +
                    " ,balance varchar(16)\n" +
                    " ,type varchar(16)\n" +
                    " ,address varchar(255)\n" +
                    ");");
            String strLine = "";
            String query = "";
            while ((strLine = br.readLine()) != null) {
                String[] parts = strLine.split(",");
                query = String.format(insert_tplt, (Object[]) parts);
                System.out.println(query);
                st.executeUpdate(query);
            }

            conn.close();
        } catch (Exception e) {
            System.err.println("Got an exception! ");
            System.err.println(e.getMessage());
        }

    }
}