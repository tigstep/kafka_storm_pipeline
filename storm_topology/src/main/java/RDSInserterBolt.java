import org.apache.storm.task.OutputCollector;
import org.apache.storm.task.TopologyContext;
import org.apache.storm.topology.OutputFieldsDeclarer;
import org.apache.storm.topology.base.BaseRichBolt;
import org.apache.storm.tuple.Fields;
import org.apache.storm.tuple.Tuple;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

/**
 * Created by tigstep on 2/18/2019.
 */

public class RDSInserterBolt extends BaseRichBolt {
    private OutputCollector _collector;
    private static final Logger LOG = LoggerFactory.getLogger(RDSInserterBolt.class);

    @Override
    public void prepare(Map conf, TopologyContext context, OutputCollector collector) {
        _collector = collector;
    }

    @Override
    public void execute(Tuple redis_lkp_tpl) {
        String ssn = ((String) redis_lkp_tpl.getValueByField("ssn"));
        String balance = ((String) redis_lkp_tpl.getValueByField("balance"));
        LOG.info(String.format("ssn - balance : %s - %s", ssn, balance));
        _collector.ack(redis_lkp_tpl);
    }

    @Override
    public void declareOutputFields(OutputFieldsDeclarer declarer) {
        declarer.declare(new Fields("ssn", "balance"));
    }
}