import org.apache.storm.Config;
import org.apache.storm.LocalCluster;
import org.apache.storm.StormSubmitter;
import org.apache.storm.kafka.spout.KafkaSpout;
import org.apache.storm.kafka.spout.KafkaSpoutConfig;
import org.apache.storm.topology.TopologyBuilder;
import java.util.Arrays;
import static org.apache.storm.kafka.spout.KafkaSpoutConfig.FirstPollOffsetStrategy.LATEST;

/**
 * Created by tigstep on 2/18/2019.
 */

public class ObfuscatorTopology {
    public static void main(String[] args) throws Exception {
        TopologyBuilder builder = new TopologyBuilder();
        //Defining the kafka spout
        String zkConnString = "18.144.53.122:9092";
        String topicName = "test";

        KafkaSpoutConfig spoutConf = KafkaSpoutConfig.builder(zkConnString, topicName).setFirstPollOffsetStrategy(LATEST).build();
        builder.setSpout("kafka_spout", new KafkaSpout<>(spoutConf));
        builder.setBolt("obfuscatorBolt", new ObfuscatorBolt(), 8).shuffleGrouping("kafka_spout");

        //Setting topology's configuration
        Config conf = new Config();
        conf.put(Config.NIMBUS_SEEDS, Arrays.asList(new String[]{"18.144.53.122"}));
        conf.setDebug(false);

        //If there are arguments, we are running on a cluster
        if (args != null && args.length > 0) {
            conf.setNumWorkers(1);
            StormSubmitter.submitTopology(args[0], conf, builder.createTopology());
        }
        else {
            conf.setMaxTaskParallelism(1);
            LocalCluster cluster = new LocalCluster();
            cluster.submitTopology("obfuscatorTopology", conf, builder.createTopology());
            Thread.sleep(10000);
            cluster.shutdown();
        }
    }
}
