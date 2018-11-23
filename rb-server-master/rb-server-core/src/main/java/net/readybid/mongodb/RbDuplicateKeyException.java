package net.readybid.mongodb;

import com.mongodb.MongoException;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by DejanK on 3/29/2017.
 *
 */
public class RbDuplicateKeyException extends RuntimeException {

    private static final String ID_INDEX_NAME = "_id_";

    private MongoException mongoException;

    public String errorCode;
    public String index;
    public String value;

    public RbDuplicateKeyException(MongoException we) {
        mongoException = we;

        final Pattern r = Pattern.compile("(?:.*?index: )(\\w+)(?:.*?: )(.+)");
        final Matcher m = r.matcher(we.getMessage());
        if (m.find()) {
            index = m.group(1);
            value = m.group(2);
        }
    }

    public RbDuplicateKeyException(String errorCode) {
        this.errorCode = errorCode;
    }

    public boolean isOnIdKey() {
        return ID_INDEX_NAME.equals(index);
    }
}
