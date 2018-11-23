package net.readybid.elasticsearch;

/**
 * Created by DejanK on 11/1/2016.
 */
public interface ElasticSearchFactory {
    ElasticSearchClient createClient();
}
