package net.readybid.elasticsearch;

/**
 * Created by DejanK on 11/1/2016.
 *
 */
public class AwsElasticSearchFactory implements ElasticSearchFactory {

    private final String hostname;
    private final int port;
    private final String protocol;

    public AwsElasticSearchFactory(String hostname, int port, String protocol) {

        this.hostname = hostname;
        this.port = port;
        this.protocol = protocol;
    }

    public ElasticSearchClient createClient(){
        return new AwsElasticSearchClientImpl(hostname, port, protocol);
    }
}
