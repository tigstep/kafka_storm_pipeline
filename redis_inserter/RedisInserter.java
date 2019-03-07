import java.io.*;
import redis.clients.jedis.Jedis;

public class RedisInserter {
    public static void main(String[] args) throws java.io.IOException{
        FileInputStream fstream = new FileInputStream("../input/SSN_LN_FN_AN_Bal_Type_Addr.txt");
        BufferedReader br = new BufferedReader(new InputStreamReader(fstream));
        //Connecting to Redis server on localhost
        Jedis jedis = new Jedis("127.0.0.1");
        System.out.println("Connection to server sucessfully");
        //set the data in redis string
        //jedis.set("tutorial-name", "Redis tutorial");
        // Get the stored data and print it
        //System.out.println("Stored string in redis:: "+ jedis.get("tutorial-name"));
        String strLine = "";
        while ((strLine = br.readLine()) != null) {
            String[] parts = strLine.split(",");
            jedis.set(parts[0], parts[1]);
        }
        //Close the input stream
        fstream.close();

    }
}