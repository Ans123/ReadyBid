package net.readybid.app;

import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.time.Instant;
import java.util.Date;
import java.util.List;

@Profile({"e2eTest", "integrationTest"})
@Service
public class MongoDbHelperService {

    public static final String PATH_IN_RESOURCES = "db-setups/";
    private final MongoDatabase mongoDatabase;

    @Autowired
    public MongoDbHelperService(
            MongoDatabase mongoDatabase
    ) {
        this.mongoDatabase = mongoDatabase;
    }


    public void load(String resource) throws IOException {
        final String data = loadJsonFromResources(resource);
        prepareMongoDb(data);
    }

    public void drop() {
        mongoDatabase.drop();
    }


    private String loadJsonFromResources(String resource) throws IOException {
        final ClassLoader classLoader = getClass().getClassLoader();
        final InputStream inputStream = classLoader.getResourceAsStream(PATH_IN_RESOURCES + resource);
        return readFromInputStream(inputStream);
    }

    private String readFromInputStream(InputStream inputStream) throws IOException {
        StringBuilder resultStringBuilder = new StringBuilder();
        try ( BufferedReader br = new BufferedReader(new InputStreamReader(inputStream))) {
            String line;
            while ((line = br.readLine()) != null) {
                resultStringBuilder.append(line).append("\n");
            }
        }
        return resultStringBuilder.toString();
    }

    private void prepareMongoDb(String json) {
        final Document document = loadDocument(json);
        for(Object collectionObject : (List) document.get("collections")){
            final Document collection = (Document) collectionObject;
            final String collectionName = (String) collection.get("name");
            final List collectionDocuments = (List) collection.get("documents");

            mongoDatabase
                    .getCollection(collectionName)
                    .insertMany(collectionDocuments);
        }

    }

    private Document loadDocument(String json) {
        final Document d = Document.parse(removeComments(json));
        loadDocument(d);
        return d;
    }

    private String removeComments(String json) {
        return json
                .replaceAll("(?s)^/*.*\\*/.{1}", "");
    }

    private Object loadDocument(Object o) {
        if(o instanceof Document) {
            final Document d = (Document) o;
            if(d.containsKey("ObjectId")) return toObjectId(d.get("ObjectId"));
            if(d.containsKey("ISODate")) return toDate(d.get("ISODate"));
            if(d.containsKey("NumberLong")) return toLong(d.get("NumberLong"));
            for(String key : d.keySet()){
                d.put(key, loadDocument(d.get(key)));
            }
        } else if( o instanceof List ){
            final List oList = (List) o;
            for(int i=0; i < oList.size(); i++){
                oList.set(i, loadDocument(oList.get(i)));
            }
        }
        return o;
    }

    private ObjectId toObjectId(Object objectId) {
        return new ObjectId(String.valueOf(objectId));
    }

    private Date toDate(Object isoDate) {
        return Date.from(Instant.parse(isoDate.toString()));
    }

    private Long toLong(Object numberLong) {
        return Long.parseLong(String.valueOf(numberLong));
    }

}
