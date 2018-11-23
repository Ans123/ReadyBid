package net.readybid.rfp.template;

import net.readybid.app.core.entities.rfp.Questionnaire;
import net.readybid.rfp.type.RfpType;
import org.bson.types.ObjectId;

/**
 * Created by DejanK on 9/12/2016.
 *
 */
public interface RfpTemplate {

    ObjectId getId();

    String getName();

    RfpType getType();

    String getDescription();

    String getCoverLetter();

    Questionnaire getQuestionnaire();

    void setCoverLetter(String coverLetter);

    String getFinalAgreementTemplate();

    void setQuestionnaire(Questionnaire questionnaire);
}
