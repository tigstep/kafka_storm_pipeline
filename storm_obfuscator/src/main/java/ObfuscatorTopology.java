import org.apache.storm.Config;
import org.apache.storm.LocalCluster;
import org.apache.storm.StormSubmitter;
import org.apache.storm.kafka.spout.KafkaSpout;
import org.apache.storm.kafka.spout.KafkaSpoutConfig;
import org.apache.storm.topology.TopologyBuilder;
import java.util.Arrays;

import java.util.UUID;

import static org.apache.storm.kafka.spout.KafkaSpoutConfig.FirstPollOffsetStrategy.LATEST;
import static org.apache.storm.kafka.spout.KafkaSpoutConfig.FirstPollOffsetStrategy.UNCOMMITTED_LATEST;

/**
 * Created by tigstep on 2/18/2019.
 */

public class ObfuscatorTopology {
    //Entry point for the topology
    public static void main(String[] args) throws Exception {
        //Used to build the topology
        TopologyBuilder builder = new TopologyBuilder();


        //Defining the kafka spout
        String zkConnString = "18.144.53.122:9092";
        String topicName = "test";

        KafkaSpoutConfig spoutConf = KafkaSpoutConfig.builder(zkConnString, topicName).setFirstPollOffsetStrategy(LATEST).build();
        builder.setSpout("kafka_spout", new KafkaSpout<>(spoutConf));

        //BrokerHosts hosts = new ZkHosts(zkConnString);
        //SpoutConfig spoutConfig = new SpoutConfig(hosts, topicName, "/" + topicName, UUID.randomUUID().toString());
        //spoutConfig.scheme = new SchemeAsMultiScheme(new StringScheme());
        //KafkaSpout kafkaSpout = new KafkaSpout(spoutConfig);
        //Add the spout, with a name of 'kafkaSpout'
        //and parallelism hint of 5 executors
        //builder.setSpout("kafkaSpout", kafkaSpout, 5);

        //Add the SplitSentence bolt, with a name of 'split'
        //and parallelism hint of 8 executors
        //shufflegrouping subscribes to the spout, and equally distributes
        //tuples (sentences) across instances of the SplitSentence bolt
        builder.setBolt("obfuscatorBolt", new ObfuscatorBolt(), 8).shuffleGrouping("kafka_spout");
        //Add the counter, with a name of 'count'
        //and parallelism hint of 12 executors
        //fieldsgrouping subscribes to the split bolt, and
        //ensures that the same word is sent to the same instance (group by field 'word')
        // builder.setBolt("count", new WordCount(), 12).fieldsGrouping("split", new Fields("word"));

        //new configuration
        Config conf = new Config();
        conf.put(Config.NIMBUS_SEEDS, Arrays.asList(new String[]{"18.144.53.122"}));
        //Set to false to disable debug information when
        // running in production on a cluster
        conf.setDebug(false);
        //If there are arguments, we are running on a cluster
        if (args != null && args.length > 0) {
            //parallelism hint to set the number of workers
            conf.setNumWorkers(1);
            //submit the topology
            StormSubmitter.submitTopology(args[0], conf, builder.createTopology());
        }
        //Otherwise, we are running locally
        else {
            //Cap the maximum number of executors that can be spawned
            //for a component to 3
            conf.setMaxTaskParallelism(1);
            //LocalCluster is used to run locally
            LocalCluster cluster = new LocalCluster();
            //submit the topology
            cluster.submitTopology("obfuscatorTopology", conf, builder.createTopology());
            //sleep
            Thread.sleep(10000);
            //shut down the cluster
            cluster.shutdown();
        }
    }
}
