# kafka_storm_transformer
<h2>
  Tools/Services Used
</h2>
  <ul>
  <li>Java</li>
  <li>Ansible</li>
  <li>Docker</li>
  <li>Apache Zookeeper</li>	
  <li>Apache Kafka</li>
  <li>Apache Storm</li>	
  <li>AWS</li>
    <ul>
      <li>EC2</li>
      <li>RDS</li>
      <li>Elasticache(Redis)</li>
    </ul>
  </ul>
<h2>

<h2>
	To Do
</h2>
<ul>
<li>Storm Transformer</li>
<ol>
	<li>Pass the redis endpoint into Topology</li>
	<li>Add Jedis to Topology Maven</li>
	<li>Change the Obfuscator bolt name to LookupBolt</li>
  	<li>Modify the transformer to do a Redis lookup</li>
</ol>
<li>PSQL</li>
<ol>
  <li>start a psql database</li>
</ol>
<li>STORM</li>
<ol>
  <li>implement time/count based batching</li>
</ol>
</ul>

Observations
<ul>
	<li>If possible, always use Terraform for infrastructure setup</li>
</ul>
