package net.readybid.app.persistence.mongodb.repository.mapping.codec;

import net.readybid.app.entities.rfp.questionnaire.form.question.ListQuestionnaireQuestionOption;
import net.readybid.mongodb.RbMongoDecoder;
import org.bson.BsonWriter;
import org.bson.Document;
import org.bson.codecs.BsonTypeClassMap;
import org.bson.codecs.EncoderContext;

import java.util.Map;


public class ListQuestionnaireQuestionOptionCodec extends RbMongoCodecForBuilder<ListQuestionnaireQuestionOption, ListQuestionnaireQuestionOption.Builder> {

    public ListQuestionnaireQuestionOptionCodec(BsonTypeClassMap bsonTypeClassMap) {
        super(ListQuestionnaireQuestionOption.class, bsonTypeClassMap);
    }

    @Override
    protected void builderDecoderMapSetup(Map<String, RbMongoDecoder<ListQuestionnaireQuestionOption.Builder>> decoderMap) {
        decoderMap.put("value", (builder, reader, context) -> builder.setValue(reader.readString()));
        decoderMap.put("label", (builder, reader, context) -> builder.setLabel(reader.readString()));
    }

    @Override
    protected ListQuestionnaireQuestionOption.Builder getBuilderInstance() {
        return new ListQuestionnaireQuestionOption.Builder();
    }

    @Override
    public void encodeDocument(Document d, ListQuestionnaireQuestionOption option, BsonWriter bsonWriter, EncoderContext encoderContext) {
        putIfNotNull(d, "value", option.value);
        putIfNotNull(d, "label", option.label);
    }
}
