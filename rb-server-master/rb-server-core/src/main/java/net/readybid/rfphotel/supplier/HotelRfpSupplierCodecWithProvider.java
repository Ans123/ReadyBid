package net.readybid.rfphotel.supplier;

import net.readybid.mongodb.RbMongoCodecWithProvider;
import net.readybid.mongodb.RbMongoDecoder;
import net.readybid.rfp.contact.RfpContactImpl;
import org.bson.BsonWriter;
import org.bson.Document;
import org.bson.codecs.BsonTypeClassMap;
import org.bson.codecs.EncoderContext;

import java.util.Map;

/**
 * Created by DejanK on 1/17/2017.
 *
 */
public class HotelRfpSupplierCodecWithProvider extends RbMongoCodecWithProvider<HotelRfpSupplierImpl> {

    public HotelRfpSupplierCodecWithProvider(BsonTypeClassMap bsonTypeClassMap) {
        super(HotelRfpSupplierImpl.class, bsonTypeClassMap);
    }

    @Override
    protected HotelRfpSupplierImpl newInstance() {
        return new HotelRfpSupplierImpl();
    }

    @Override
    public void decodeMapSetup(Map<String, RbMongoDecoder<HotelRfpSupplierImpl>> decodeMap) {
        decodeMap.put("company", (c, reader, context) -> c.setCompany(read(reader, context, RfpHotelImpl.class)));
        decodeMap.put("contact", (c, reader, context) -> c.setContact(read(reader, context, RfpContactImpl.class)));
    }

    @Override
    public void encodeDocument(Document d, HotelRfpSupplierImpl supplier, BsonWriter bsonWriter, EncoderContext encoderContext) {
        putIfNotNull(d, "company", supplier.getCompany());
        putIfNotNull(d, "contact", supplier.getContact());
    }
}
