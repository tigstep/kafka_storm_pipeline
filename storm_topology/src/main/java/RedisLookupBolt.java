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
    public void execute(Tuple sprout_tpl) {
        LOG.info(String.format("tuple is %s", sprout_tpl.getString(3)));
        LOG.info(String.format("tuple is %s", sprout_tpl.toString()));
        LOG.info(String.format("tuple is %s", sprout_tpl.getFields().toString()));
        LOG.info(String.format("tuple is %s", sprout_tpl.getValue(0)));
        /*String custID = sprout_tpl.getString(4);
        LOG.info(String.format("custID is %s", custID));
        String balance = sprout_tpl.getString(5);
        LOG.info(String.format("balance is %s", balance));
        String ssn = jedis.get(custID);
        LOG.info(String.format("custId :  %s, ssn : %s", custID, ssn));
        _collector.emit(sprout_tpl, new Values(ssn, balance));
        _collector.ack(sprout_tpl);*/
    }

    @Override
    public void declareOutputFields(OutputFieldsDeclarer declarer) {
        declarer.declare(new Fields("ssn", "balance"));
    }
}
