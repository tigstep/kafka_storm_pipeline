import org.apache.storm.task.OutputCollector;
import org.apache.storm.task.TopologyContext;
import org.apache.storm.topology.OutputFieldsDeclarer;
import org.apache.storm.topology.base.BaseRichBolt;
import org.apache.storm.tuple.Fields;
import org.apache.storm.tuple.Tuple;
import org.apache.storm.tuple.Values;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.Set;

import redis.clients.jedis.Jedis;

/**
 * Created by tigstep on 2/18/2019.
 */

public class RedisLookupBolt extends BaseRichBolt {
    private OutputCollector _collector;
    private static final Logger LOG = LoggerFactory.getLogger(RedisLookupBolt.class);
    private String redisEndpoint;
    private Jedis jedis;

    @Override
    public void prepare(Map conf, TopologyContext context, OutputCollector collector) {
        redisEndpoint = (String) conf.get("redisEndpoint");
        _collector = collector;
        jedis = new Jedis(redisEndpoint);
    }

    @Override
    public void execute(Tuple spout_tpl) {
        String custID = ((String) spout_tpl.getValueByField("value")).split(",")[0];
        LOG.info(String.format("custID is: %s", custID));
        String balance = ((String) spout_tpl.getValueByField("value")).split(",")[1];
        LOG.info(String.format("balance is: %s", balance));
        String ssn = jedis.get(custID);
        LOG.info(String.format("custId :  %s, ssn : %s", custID, ssn));
        _collector.emit(spout_tpl, new Values(ssn, balance));
        _collector.ack(spout_tpl);
    }

    @Override
    public void declareOutputFields(OutputFieldsDeclarer declarer) {
        declarer.declare(new Fields("ssn", "balance"));
    }
}
