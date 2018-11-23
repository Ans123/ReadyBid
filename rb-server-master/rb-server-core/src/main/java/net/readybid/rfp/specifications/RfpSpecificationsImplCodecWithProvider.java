package net.readybid.rfp.specifications;

import net.readybid.mongodb.RbMongoCodecWithProvider;
import net.readybid.mongodb.RbMongoDecoder;
import net.readybid.rfp.buyer.BuyerImpl;
import net.readybid.rfp.type.RfpType;
import org.bson.BsonWriter;
import org.bson.Document;
import org.bson.codecs.BsonTypeClassMap;
import org.bson.codecs.EncoderContext;

import java.time.LocalDate;
import java.util.Map;

/**
 * Created by DejanK on 12/25/2016.
 *
 */
public class RfpSpecificationsImplCodecWithProvider extends RbMongoCodecWithProvider<RfpSpecificationsImpl> {

    public RfpSpecificationsImplCodecWithProvider(BsonTypeClassMap bsonTypeClassMap) {
        super(RfpSpecificationsImpl.class, bsonTypeClassMap);
    }

    @Override
    protected RfpSpecificationsImpl newInstance() {
        return new RfpSpecificationsImpl();
    }

    @Override
    public void decodeMapSetup(Map<String, RbMongoDecoder<RfpSpecificationsImpl>> decodeMap) {
        decodeMap.put("name", (c, reader, context) -> c.setName(reader.readString()));
        decodeMap.put("type", (c, reader, context) -> c.setType(read(reader, context, RfpType.class)));
        decodeMap.put("chainSupport", (c, reader, context) -> c.setChainSupported(reader.readBoolean()));
        decodeMap.put("dueDate", (c, reader, context) -> c.setDueDate(read(reader, context, LocalDate.class)));
        decodeMap.put("programStartDate", (c, reader, context) -> c.setProgramStartDate(read(reader, context, LocalDate.class)));
        decodeMap.put("programEndDate", (c, reader, context) -> c.setProgramEndDate(read(reader, context, LocalDate.class)));
        decodeMap.put("programYear", (c, reader, context) -> c.setProgramYear(readInt(reader)));
        decodeMap.put("sentDate", (c, reader, context) -> c.setBidSentDate(read(reader, context, LocalDate.class)));
        decodeMap.put("buyer", (c, reader, context) -> c.setBuyer(read(reader, context, BuyerImpl.class)));
    }

    @Override
    public void encodeDocument(Document d, RfpSpecificationsImpl specs, BsonWriter bsonWriter, EncoderContext encoderContext) {
        putIfNotNull(d, "name", specs.getName());
        putIfNotNull(d, "type", specs.getType());
        putIfNotNull(d, "chainSupport", specs.isChainSupportEnabled());
        putIfNotNull(d, "dueDate", specs.getDueDate());
        putIfNotNull(d, "programStartDate", specs.getProgramStartDate());
        putIfNotNull(d, "programEndDate", specs.getProgramEndDate());
        putIfNotNull(d, "programYear", specs.getProgramYear());
        putIfNotNull(d, "buyer", specs.getBuyer());
        putIfNotNull(d, "sentDate", specs.getBidSentDate());
    }
}
