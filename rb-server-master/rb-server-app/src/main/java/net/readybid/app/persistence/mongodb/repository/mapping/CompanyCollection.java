package net.readybid.app.persistence.mongodb.repository.mapping;

import net.readybid.mongodb.collections.fields.CreatedFields;
import net.readybid.mongodb.collections.fields.IdField;
import net.readybid.mongodb.collections.fields.StatusFields;

public class CompanyCollection extends EntityCollection implements IdField, StatusFields, CreatedFields{

    public static final String COLLECTION_NAME = "Company";
    public static final String UNVALIDATED_COLLECTION_NAME = "UnvalidatedCompany";

    private CompanyCollection() {}
}
