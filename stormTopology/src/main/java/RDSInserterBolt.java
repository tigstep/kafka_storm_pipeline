import org.apache.storm.task.OutputCollector;
import org.apache.storm.task.TopologyContext;
import org.apache.storm.topology.OutputFieldsDeclarer;
import org.apache.storm.topology.base.BaseRichBolt;
import org.apache.storm.tuple.Fields;
import org.apache.storm.tuple.Tuple;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

import java.util.Map;

/**
 * Created by tigstep on 2/18/2019.
 */

public class RDSInserterBolt extends BaseRichBolt {
    private OutputCollector _collector;
    private static final Logger LOG = LoggerFactory.getLogger(RDSInserterBolt.class);
    String endpoint = "";
    String username = "";
    String password = "";
    String update_stmt_tplt = "UPDATE balances " +
            "SET balance = '%s' " +
            "WHERE  ssn = '%s';";
    String query = "";
    Statement st;
    @Override
    public void prepare(Map conf, TopologyContext context, OutputCollector collector) {
        endpoint = (String) conf.get("rdsEndpoint");
        username = (String) conf.get("rdsUsername");
        password = (String) conf.get("rdsPassword");
        try {
            String url = String.format("jdbc:postgresql://%s:5432/transactions", endpoint);
            LOG.info("The url is : " + url.toString());
            Class.forName("org.postgresql.Driver");
            Connection conn = DriverManager.getConnection(url, username, password);
            LOG.info("The conn is : " + conn.toString());
            st = conn.createStatement();
            LOG.info("The statement is : " + st.toString());
        } catch (Exception e) {
            System.err.println("Exception while setting connection! ");
            System.err.println(e);
        }
        _collector = collector;
    }

    @Override
    public void execute(Tuple redis_lkp_tpl) {
        String ssn = ((String) redis_lkp_tpl.getValueByField("ssn"));
        String balance = ((String) redis_lkp_tpl.getValueByField("balance"));
        LOG.info(String.format("ssn - balance : %s - %s", ssn, balance));
        query = String.format(update_stmt_tplt, balance, ssn);
        try {
            st.execute(query);
        } catch (Exception e) {
                System.err.println("Exception while executing the query! ");
                System.err.println(e);
        }
        _collector.ack(redis_lkp_tpl);
    }

    @Override
    public void declareOutputFields(OutputFieldsDeclarer declarer) {
        declarer.declare(new Fields("ssn", "balance"));
    }
}