package net.readybid.mongodb;

import net.readybid.rfphotel.bid.core.negotiations.values.NegotiationValue;
import org.bson.*;
import org.bson.codecs.BsonTypeClassMap;
import org.bson.codecs.Codec;
import org.bson.codecs.DecoderContext;
import org.bson.codecs.EncoderContext;
import org.bson.codecs.configuration.CodecProvider;
import org.bson.codecs.configuration.CodecRegistry;
import org.bson.types.ObjectId;

import java.math.BigDecimal;
import java.util.*;

import static org.bson.BsonType.END_OF_DOCUMENT;

/**
 * Created by DejanK on 1/2/2017.
 *
 * MUST BE LOADED INTO MONGO CONFIG AS PROVIDER!!!
 */
public abstract class RbMongoCodecWithProvider<T> implements Codec<T>, CodecProvider {

    protected CodecRegistry registry;
    protected final BsonTypeClassMap bsonTypeClassMap;
    protected final Map<String, RbMongoDecoder<T>> decodeMap;
    protected final Class<T> tClass;

    public RbMongoCodecWithProvider(Class<T> tClass, BsonTypeClassMap bsonTypeClassMap) {
        this.bsonTypeClassMap = bsonTypeClassMap;
        this.tClass = tClass;

        decodeMap = new HashMap<>();
        decodeMapSetup(decodeMap);
    }

    protected T newInstance(){ return null; }

    public void decodeMapSetup(Map<String, RbMongoDecoder<T>> decodeMap){}

    public void encodeDocument(Document document, T tObject, BsonWriter bsonWriter, EncoderContext encoderContext){}

    @Override
    public <S> Codec<S> get(Class<S> sClass, CodecRegistry codecRegistry) {
        if(sClass == tClass){
            this.registry = codecRegistry;
            //noinspection unchecked
            return (Codec<S>) this;
        } else {
            return null;
        }
    }

    @Override
    public T decode(BsonReader reader, DecoderContext context) {
        BsonType type = reader.getCurrentBsonType();
        if(type == null) type = reader.readBsonType();
        if (BsonType.DOCUMENT == type) {
            reader.readStartDocument();
            final T t = decodeInstance(reader, context);
            reader.readEndDocument();
            return t;
        } else {
            pass(type, reader );
            return null;
        }
    }

    protected T decodeInstance(BsonReader reader, DecoderContext context){
        return decode(reader, context, newInstance(), decodeMap);
    }

    protected <W> W decode(BsonReader reader, DecoderContext context, W instance, Map<String, RbMongoDecoder<W>> decoderMap){
        while (reader.readBsonType() != END_OF_DOCUMENT) {
            final String fieldName = reader.readName();
            final BsonType bsonType = reader.getCurrentBsonType();
            if (bsonType == null || BsonType.NULL.equals(bsonType)){
                reader.readNull();
            } else if (decoderMap.containsKey(fieldName)){
                decoderMap.get(fieldName).decode(instance, reader, context);
            } else {
                pass(bsonType, reader);
            }
        }
        return instance;
    }

    @Override
    public void encode(BsonWriter bsonWriter, T t, EncoderContext encoderContext) {
        final Document d = new Document();
        encodeDocument(d, t, bsonWriter, encoderContext);
        documentCodec().encode(bsonWriter, d, encoderContext);
    }

    @Override
    public Class<T> getEncoderClass() {
        return tClass;
    }

    protected <W> W readField(BsonReader reader, DecoderContext context, String field, Class<W> wClass) {
        final BsonReaderMark mark = reader.getMark();
        BsonType type = reader.getCurrentBsonType();
        if(type == null) type = reader.readBsonType();
        if (BsonType.DOCUMENT == type) {
            reader.readStartDocument();
            while (reader.readBsonType() != END_OF_DOCUMENT) {
                final String fieldName = reader.readName();
                final BsonType bsonType = reader.getCurrentBsonType();
                if(fieldName.equals(field)){
                    if (bsonType == null || BsonType.NULL.equals(bsonType)) {
                        mark.reset();
                        return null;
                    } else {
                        W value = read(reader, context, wClass);
                        mark.reset();
                        return value;
                    }
                } else {
                    pass(bsonType, reader);
                }
            }
        }
        mark.reset();
        return null;
    }


    protected Codec<Document> documentCodec() {
        return registry.get(Document.class);
    }

    protected void putIfNotNull(Document d, String key, Object o) {
        if(o != null) {
            d.put(key, o);
        }
    }

    protected <S> S read(BsonReader reader, DecoderContext decoderContext, Class<S> sClass) {
        return registry.get(sClass).decode(reader, decoderContext);
    }

    protected Date readDate(BsonReader bsonReader) {
        return new Date(bsonReader.readDateTime());
    }

    private BigDecimal readBigDecimal(BsonReader reader) {
        return reader.readDecimal128().bigDecimalValue();
    }

    protected int readInt(BsonReader reader) {
        switch(reader.getCurrentBsonType()){
            case DOUBLE:
                return (int) reader.readDouble();
            case INT32:
                return reader.readInt32();
            case INT64:
                return (int) reader.readInt64();
            default:
                return 0;
        }
    }

    protected Integer readIntObject(BsonReader reader) {
        switch(reader.getCurrentBsonType()){
            case DOUBLE:
                return (int) reader.readDouble();
            case INT32:
                return reader.readInt32();
            case INT64:
                return (int) reader.readInt64();
            default:
                return null;
        }
    }

    protected long readLong(BsonReader reader) {
        switch(reader.getCurrentBsonType()){
            case DOUBLE:
                return (long) reader.readDouble();
            case INT32:
                return (long) reader.readInt32();
            case INT64:
                return reader.readInt64();
            default:
                return 0L;
        }
    }

    protected Long readLongObject(BsonReader reader) {
        switch(reader.getCurrentBsonType()){
            case DOUBLE:
                return (long) reader.readDouble();
            case INT32:
                return (long) reader.readInt32();
            case INT64:
                return reader.readInt64();
            default:
                return null;
        }
    }

    protected double readDouble(BsonReader reader) {
        switch(reader.getCurrentBsonType()){
            case DOUBLE:
                return reader.readDouble();
            case INT32:
                return (double) reader.readInt32();
            case INT64:
                return (double) reader.readInt64();
            default:
                return 0d;
        }
    }

    protected String readToString(BsonReader reader) {
        final Object o = readObject(reader.getCurrentBsonType(), reader);
        return o == null ? null : String.valueOf(o);
    }

    protected Map<String, Object> readMap(BsonReader reader) {
        final Map<String, Object> map = new HashMap<>();
        reader.readStartDocument();
        while (reader.readBsonType() != END_OF_DOCUMENT) {
            final String fieldName = reader.readName();
            final BsonType bsonType = reader.getCurrentBsonType();
            if(bsonType == null){
                map.put(fieldName, null);
                reader.readNull();
            } else {
                map.put(fieldName, readObject(bsonType, reader));
            }
        }
        reader.readEndDocument();
        return map;
    }

    protected Map<String, String> readMapStringString(BsonReader reader) {
        final Map<String, String> map = new LinkedHashMap<>();
        reader.readStartDocument();
        while (reader.readBsonType() != END_OF_DOCUMENT) {
            final String fieldName = reader.readName();
            final BsonType bsonType = reader.getCurrentBsonType();
            if(bsonType == null){
                map.put(fieldName, null);
                reader.readNull();
            } else {
                map.put(fieldName, String.valueOf(readObject(bsonType, reader)));
            }
        }
        reader.readEndDocument();
        return map;
    }

    protected <U> List<U> readList(BsonReader reader, Class<U> uClass) {
        final List<U> list = new ArrayList<>();
        reader.readStartArray();
        while(reader.readBsonType() !=  END_OF_DOCUMENT) {
            list.add(uClass.cast(readObject(reader.getCurrentBsonType(), reader)));
        }
        reader.readEndArray();
        return list;
    }

    protected List<Map<String, Object>> readListOfMaps(BsonReader reader) {
        final List<Map<String, Object>> list = new ArrayList<>();
        reader.readStartArray();
        while(reader.readBsonType() !=  END_OF_DOCUMENT) {
            list.add(readMap(reader));
        }
        reader.readEndArray();
        return list;
    }

    protected <U> List<U> readListOfObjects (BsonReader reader, DecoderContext context, Class<U> uClass) {
        final List<U> list = new ArrayList<>();
        reader.readStartArray();
        while(reader.readBsonType() != END_OF_DOCUMENT) {
            list.add(read(reader, context, uClass));
        }
        reader.readEndArray();
        return list;
    }

    protected <U extends S, S> List<S> readListOfObjects (BsonReader reader, DecoderContext context, Class<U> uClass, Class<S> sClass) {
        final List<S> list = new ArrayList<>();
        reader.readStartArray();
        while(reader.readBsonType() != END_OF_DOCUMENT) {
            list.add(read(reader, context, uClass));
        }
        reader.readEndArray();
        return list;
    }

    protected <U> Set<U> readSet(BsonReader reader, Class<U> uClass) {
        final Set<U> set = new HashSet<>();
        reader.readStartArray();
        while(reader.readBsonType() !=  END_OF_DOCUMENT) {
            set.add(uClass.cast(readObject(reader.getCurrentBsonType(), reader)));
        }
        reader.readEndArray();
        return set;
    }

    protected Set<ObjectId> readObjectIdSet(BsonReader reader) {
        final Set<ObjectId> set = new HashSet<>();
        reader.readStartArray();
        while(reader.readBsonType() !=  END_OF_DOCUMENT) {
            set.add(reader.readObjectId());
        }
        reader.readEndArray();
        return set;
    }

    protected <U extends Enum<U>> Set<U> readEnumSet(BsonReader reader, Class<U> uClass) {
        final Set<U> set = new HashSet<>();
        reader.readStartArray();
        while(reader.readBsonType() !=  END_OF_DOCUMENT) {
            set.add(Enum.valueOf(uClass, String.valueOf(readObject(reader.getCurrentBsonType(), reader))));
        }
        reader.readEndArray();
        return set;
    }

    protected Object readObject(BsonType bsonType, BsonReader reader) {
        switch(bsonType){
            case END_OF_DOCUMENT:
                reader.readEndDocument();
                return null;
            case DOUBLE:
                return reader.readDouble();
            case STRING:
                return reader.readString();
            case DOCUMENT:
                return readMap(reader);
            case ARRAY:
                return readList(reader, Object.class);
            case BINARY:
                return reader.readBinaryData();
            case UNDEFINED:
                reader.readUndefined();
                return null;
            case OBJECT_ID:
                return reader.readObjectId();
            case BOOLEAN:
                return reader.readBoolean();
            case DATE_TIME:
                return readDate(reader);
            case NULL:
                reader.readNull();
                return null;
            case REGULAR_EXPRESSION:
                return reader.readRegularExpression();
            case DB_POINTER:
                return reader.readDBPointer();
            case JAVASCRIPT:
                return reader.readJavaScript();
            case SYMBOL:
                return reader.readSymbol();
            case JAVASCRIPT_WITH_SCOPE:
                return reader.readJavaScriptWithScope();
            case INT32:
                return reader.readInt32();
            case TIMESTAMP:
                return reader.readTimestamp();
            case INT64:
                return reader.readInt64();
            case DECIMAL128:
                return readBigDecimal(reader);
            case MIN_KEY:
                reader.readMinKey();
                return null;
            case MAX_KEY:
                reader.readMaxKey();
                return null;
            default:
                return null;
        }
    }

    protected void pass(BsonType bsonType, BsonReader reader) {
        switch(bsonType){
            case END_OF_DOCUMENT:
                reader.readEndDocument();
                break;
            case DOUBLE:
                reader.readDouble();
                break;
            case STRING:
                reader.readString();
                break;
            case DOCUMENT:
                reader.readStartDocument();
                while(reader.readBsonType() !=  END_OF_DOCUMENT) {
                    reader.readName();
                    pass(reader.getCurrentBsonType(), reader);
                }
                reader.readEndDocument();
                break;
            case ARRAY:
                reader.readStartArray();
                while(reader.readBsonType() !=  END_OF_DOCUMENT) {
                    pass(reader.getCurrentBsonType(), reader);
                }
                reader.readEndArray();
                break;
            case BINARY:
                reader.readBinaryData();
                break;
            case UNDEFINED:
                reader.readUndefined();
                break;
            case OBJECT_ID:
                reader.readObjectId();
                break;
            case BOOLEAN:
                reader.readBoolean();
                break;
            case DATE_TIME:
                reader.readDateTime();
                break;
            case NULL:
                reader.readNull();
                break;
            case REGULAR_EXPRESSION:
                reader.readRegularExpression();
                break;
            case DB_POINTER:
                reader.readDBPointer();
                break;
            case JAVASCRIPT:
                reader.readJavaScript();
                break;
            case SYMBOL:
                reader.readSymbol();
                break;
            case JAVASCRIPT_WITH_SCOPE:
                reader.readJavaScriptWithScope();
                break;
            case INT32:
                reader.readInt32();
                break;
            case TIMESTAMP:
                reader.readTimestamp();
                break;
            case INT64:
                reader.readInt64();
                break;
            case DECIMAL128:
                reader.readDecimal128();
                break;
            case MIN_KEY:
                reader.readMinKey();
                break;
            case MAX_KEY:
                reader.readMaxKey();
                break;
        }
    }

    protected Map<String, Object> writeMapWithInvalidKeys(Map<String, Object> map) {
        if(map == null) return null;
        Map<String, Object> encodedMap = new HashMap<>();
        for(Map.Entry<String, Object> e : map.entrySet()){
            if(e.getValue() instanceof Map){
                //noinspection unchecked
                encodedMap.put(encodeMapKey(e.getKey()), writeMapWithInvalidKeys((Map) e.getValue()));
            } else {
                encodedMap.put(encodeMapKey(e.getKey()), e.getValue());
            }
        }
        return encodedMap;
    }

    protected Map<String, Object> readMapWithInvalidKeys(Map<String, Object> map) {
        if(map == null) return null;
        Map<String, Object> decodedMap = new HashMap<>();
        for(Map.Entry<String, Object> e : map.entrySet()){
            if(e.getValue() instanceof Map){
                //noinspection unchecked
                decodedMap.put(decodeMapKey(e.getKey()), readMapWithInvalidKeys((Map) e.getValue()));
            } else {
                decodedMap.put(decodeMapKey(e.getKey()), e.getValue());
            }
        }
        return decodedMap;
    }

    private String encodeMapKey(String key) {
        key = key.replace(".", "//");
        return key.replace("$", "/$");
    }

    private String decodeMapKey(String key) {
        key = key.replace("//", ".");
        return key.replace("/$", "$");
    }

    protected Map<String, NegotiationValue> readNegotiationValuesMap(BsonReader reader) {
        final Map<String, NegotiationValue> map = new HashMap<>();
        reader.readStartDocument();
        while (reader.readBsonType() != END_OF_DOCUMENT) {
            final String fieldName = reader.readName();
            final BsonType bsonType = reader.getCurrentBsonType();
            if(BsonType.NULL.equals(bsonType) || String.valueOf(bsonType).equalsIgnoreCase("null")){
                map.put(fieldName, null);
                reader.readNull();
            } else {
                map.put(fieldName, readNegotiationValue(reader));
            }
        }
        reader.readEndDocument();
        return map;
    }

    protected NegotiationValue readNegotiationValue(BsonReader reader) {
        return NegotiationValue.fromMap(readMap(reader));
    }

    @Deprecated
    protected Map<String, Map<String, Object>> writeNegotiationValuesMap(Map<String, NegotiationValue> negotiationValues) {
        if(negotiationValues == null) return null;
        final Map<String, Map<String, Object>> m = new HashMap<>();
        for(String key: negotiationValues.keySet()){
            m.put(key, writeNegotiationValue(negotiationValues.get(key)));
        }
        return m;
    }

    @Deprecated
    protected Map<String, Object> writeNegotiationValue(NegotiationValue negotiationValue) {
        if(negotiationValue == null) return null;
        return negotiationValue.toMap();
    }
}
