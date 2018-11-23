package net.readybid.app.persistence.mongodb.repository.mapping;

import net.readybid.mongodb.RbMongoCodecWithProvider;
import net.readybid.mongodb.RbMongoDecoder;
import net.readybid.test_utils.RbAbstractAssert;
import net.readybid.test_utils.RbMapAssert;
import org.bson.BsonReader;
import org.bson.BsonType;
import org.bson.BsonWriter;
import org.bson.Document;
import org.bson.codecs.DecoderContext;
import org.bson.codecs.EncoderContext;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;

public class RbMongoCodecWithProviderAssert<T> extends RbAbstractAssert<RbMongoCodecWithProviderAssert<T>, RbMongoCodecWithProvider<T>> {

    public static <T> RbMongoCodecWithProviderAssert<T> that(RbMongoCodecWithProvider<T> actual) {
        return new RbMongoCodecWithProviderAssert<>(actual);
    }

    private RbMongoCodecWithProviderAssert(RbMongoCodecWithProvider<T> actual) {
        super(actual, RbMongoCodecWithProviderAssert.class);
    }

    public RbMongoCodecWithProviderAssert<T> readsInto(Class<T> tClass){
        isNotNull();

        final BsonReader bsonReader = mock(BsonReader.class);
        doReturn(BsonType.DOCUMENT).when(bsonReader).getCurrentBsonType();
        doReturn(BsonType.END_OF_DOCUMENT).when(bsonReader).readBsonType();

        final T result = actual.decode(bsonReader, DecoderContext.builder().build());

        if(result == null){
            failWithMessage("Instance expected but was null");
        }

        if(!tClass.isInstance(result)){
            failWithMessage("Expected instance of class <%s> but was <%s>", tClass.getName(), result.getClass().getName());
        }
        return this;
    }

    public RbMongoCodecWithProviderAssert<T> cannotRead() {
        isNotNull();
        final BsonReader bsonReader = mock(BsonReader.class);
        doReturn(BsonType.DOCUMENT).when(bsonReader).getCurrentBsonType();
        doReturn(BsonType.END_OF_DOCUMENT).when(bsonReader).readBsonType();

        final T result = actual.decode(bsonReader, DecoderContext.builder().build());

        if(result != null){
            failWithMessage("Instance should be null");
        }
        return this;
    }

    public RbMongoCodecWithProviderAssert<T> decodeMapIsEmpty(){
        isNotNull();

        final Map<String, RbMongoDecoder<T>> decodeMap = new HashMap<>();
        actual.decodeMapSetup(decodeMap);

        if(!decodeMap.isEmpty()){
            failWithMessage("Expected DecodeMap to be empty but it was <%s>", decodeMap);
        }
        return this;
    }

    public RbMongoCodecWithProviderAssert<T> encodesToDocument(T t, Consumer<RbMapAssert<String, Object>> mapAssertion){
        isNotNull();
        final Document document = new Document();
        actual.encodeDocument(document, t, mock(BsonWriter.class), EncoderContext.builder().build());

        mapAssertion.accept(RbMapAssert.that(document));
        return this;
    }
}
