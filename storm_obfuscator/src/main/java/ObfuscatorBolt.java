import org.apache.storm.task.OutputCollector;
import org.apache.storm.task.TopologyContext;
import org.apache.storm.topology.OutputFieldsDeclarer;
import org.apache.storm.topology.base.BaseRichBolt;
import org.apache.storm.tuple.Fields;
import org.apache.storm.tuple.Tuple;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.Set;

import redis.clients.jedis.Jedis;

/**
 * Created by tigstep on 2/18/2019.
 */

public class ObfuscatorBolt extends BaseRichBolt {
    private OutputCollector _collector;
    private static final Logger LOG = LoggerFactory.getLogger(ObfuscatorBolt.class);
    private String redisEndpoint;
    private Jedis jedis;

    @Override
    public void prepare(Map conf, TopologyContext context, OutputCollector collector) {
        redisEndpoint = (String) conf.get("redisEndpoint");
        _collector = collector;
        jedis = new Jedis(redisEndpoint);
    }

    @Override
    public void execute(Tuple input) {
        Set<String> keys = jedis.keys("*");
        for( String key : keys ){
            LOG.info(key);
        }
        LOG.info(redisEndpoint);
        LOG.info(input.toString());
        _collector.ack(input);
    }

    @Override
    public void declareOutputFields(OutputFieldsDeclarer declarer) {
        declarer.declare(new Fields("double", "triple"));
    }
}
