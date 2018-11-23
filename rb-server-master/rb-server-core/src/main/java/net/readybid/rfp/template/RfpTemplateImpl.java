package net.readybid.rfp.template;

import net.readybid.app.core.entities.rfp.Questionnaire;
import net.readybid.rfp.type.RfpType;
import org.bson.types.ObjectId;

/**
 * Created by DejanK on 9/12/2016.
 *
 */
public class RfpTemplateImpl implements RfpTemplate {

    private ObjectId id;
    private RfpType type;
    private String name;
    private String description;
    private String coverLetter;
    private Questionnaire questionnaire;
    private String finalAgreementTemplate;

    @Override
    public ObjectId getId() {
        return id;
    }

    public void setId(ObjectId id) {
        this.id = id;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public RfpType getType() {
        return type;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String getCoverLetter() {
        return coverLetter;
    }

    public void setCoverLetter(String coverLetter) {
        this.coverLetter = coverLetter;
    }

    @Override
    public Questionnaire getQuestionnaire() {
        return questionnaire;
    }

    @Override
    public String getFinalAgreementTemplate() {
        return finalAgreementTemplate;
    }

    @Override
    public void setQuestionnaire(Questionnaire questionnaire) {
        this.questionnaire = questionnaire;
    }

    public void setType(RfpType type) {
        this.type = type;
    }

    public void setFinalAgreementTemplate(String finalAgreementTemplate) {
        this.finalAgreementTemplate = finalAgreementTemplate;
    }
}
