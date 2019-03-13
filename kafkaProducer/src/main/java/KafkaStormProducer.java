//import util.properties packages
import java.util.Properties;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

//import simple producer packages
import org.apache.kafka.clients.producer.Producer;

//import KafkaProducer packages
import org.apache.kafka.clients.producer.KafkaProducer;

//import ProducerRecord packages
import org.apache.kafka.clients.producer.ProducerRecord;

//Create java class named “KafkaProducer”
public class KafkaStormProducer {

    public static void main(String[] args) throws Exception{

        // Check arguments length value
        if(args.length == 0){
            System.out.println("Enter topic name");
            return;
        }

        //Assign topicName to string variable
        String topicName = args[0].toString();

        // create instance for properties to access producer configs
        Properties props = new Properties();

        //Assign localhost id
        props.put("bootstrap.servers", args[1].toString() + ":9092");

        //Set acknowledgements for producer requests.
        props.put("acks", "all");

        //If the request fails, the producer can automatically retry,
        props.put("retries", 0);

        //Specify buffer size in config
        props.put("batch.size", 16384);

        //Reduce the no of requests less than 0
        props.put("linger.ms", 1);

        //The buffer.memory controls the total amount of memory available to the producer for buffering.
        props.put("buffer.memory", 33554432);

        props.put("key.serializer",
                "org.apache.kafka.common.serialization.StringSerializer");

        props.put("value.serializer",
                "org.apache.kafka.common.serialization.StringSerializer");

        Producer<String, String> producer = new KafkaProducer
                <String, String>(props);

        try {
            BufferedReader br = new BufferedReader(new FileReader("../../input/CustID_Bal.txt"));
            String line;
            while ((line = br.readLine()) != null) {
                System.out.println(line);
                producer.send(new ProducerRecord<String, String>(topicName, line));
                Thread.sleep(1000);
            }
            producer.close();
        } catch (IOException e){
            System.exit(1);
        }

    }
}