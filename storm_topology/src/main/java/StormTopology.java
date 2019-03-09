import org.apache.storm.Config;
import org.apache.storm.LocalCluster;
import org.apache.storm.StormSubmitter;
import org.apache.storm.kafka.spout.KafkaSpout;
import org.apache.storm.kafka.spout.KafkaSpoutConfig;
import org.apache.storm.topology.TopologyBuilder;
import java.util.Arrays;
import static org.apache.storm.kafka.spout.KafkaSpoutConfig.FirstPollOffsetStrategy.LATEST;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by tigstep on 2/18/2019.
 */

public class StormTopology {
    private static final Logger LOG = LoggerFactory.getLogger(StormTopology.class);
    public static void main(String[] args) throws Exception {
        TopologyBuilder builder = new TopologyBuilder();
        //Setting arguments
        String topologyName = args[0];
        LOG.info(String.format("The topology name is : %s", topologyName));
        String zkConnString = args[1] + ":9092";
        LOG.info(String.format("The zookeeper host is : %s", zkConnString));
        String topicName = args[2];
        LOG.info(String.format("The topic name is : %s", topicName));
        String nimbusHost = args[3];
        LOG.info(String.format("Nibus host is : %s", nimbusHost));
        String redisEndpoint = args[4];
        LOG.info(String.format("Redis endpoint  is : %s", redisEndpoint));

        //Configuring the spout
        KafkaSpoutConfig spoutConf = KafkaSpoutConfig.builder(zkConnString, topicName).setFirstPollOffsetStrategy(LATEST).build();
        builder.setSpout("kafka_spout", new KafkaSpout<>(spoutConf));
        builder.setBolt("RedisLookupBolt", new RedisLookupBolt(), 1).shuffleGrouping("kafka_spout");

        //Setting topology's configuration
        Config conf = new Config();
        conf.put(Config.NIMBUS_SEEDS, Arrays.asList(new String[]{nimbusHost}));
        conf.setDebug(false);
        conf.put("redisEndpoint", redisEndpoint);

        //If there are arguments, we are running on a cluster
        if (args != null && args.length > 0) {
            conf.setNumWorkers(1);
            StormSubmitter.submitTopology(topologyName, conf, builder.createTopology());
        }
        else {
            conf.setMaxTaskParallelism(1);
            LocalCluster cluster = new LocalCluster();
            cluster.submitTopology(topologyName, conf, builder.createTopology());
            Thread.sleep(10000);
            cluster.shutdown();
        }
    }
}
