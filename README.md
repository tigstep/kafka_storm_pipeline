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
	<li>Change the Topology name from Obfuscator to CustomerLookupTopology</li>
	<li>Change the Obfuscator Bolt name to LookupBolt</li>
	<li>Modify the LookupBolt to do a Redis lookup</li>
	<li>Implement RDS writer bolt<\li>
	<li>implement time/count based batching for S3WriterBolt</li>
</ol>
<li>PSQL</li>
<ol>
  <li>Start a psql database</li>
</ol>
</ul>

Observations
<ul>
	<li>If possible, always use Terraform for infrastructure setup</li>
</ul>
