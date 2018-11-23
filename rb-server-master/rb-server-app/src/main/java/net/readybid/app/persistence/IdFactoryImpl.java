package net.readybid.app.persistence;

import net.readybid.app.interactors.core.gate.IdFactory;
import net.readybid.entities.Id;
import org.bson.types.ObjectId;
import org.springframework.stereotype.Service;

@Service
public class IdFactoryImpl implements IdFactory{

    @Override
    public String create() {
        return new ObjectId().toString();
    }

    @Override
    public Id createId() {
        return Id.valueOf(new ObjectId());
    }
}
