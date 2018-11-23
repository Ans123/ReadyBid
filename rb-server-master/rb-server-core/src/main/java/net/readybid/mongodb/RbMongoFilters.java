package net.readybid.mongodb;

import com.mongodb.client.model.Aggregates;
import com.mongodb.client.model.UnwindOptions;
import net.readybid.app.core.transaction.exceptions.NotFoundException;
import net.readybid.entities.Id;
import net.readybid.mongodb.collections.fields.CreatedFields;
import net.readybid.mongodb.collections.fields.StatusFields;
import net.readybid.rfphotel.bid.core.HotelRfpBidStateStatus;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static com.mongodb.client.model.Filters.*;

/**
 * Created by DejanK on 1/5/2017.
 *
 */
public class RbMongoFilters {


    public static Bson byId(String id) {
        return eq("_id", oid(id));
    }

    public static Bson byId(ObjectId id) {
            return eq("_id", id);
    }

    public static Bson byId(Id id) {
            return eq("_id", id == null ? "" : oId(id));
    }

    public static Bson byId(List<Id> ids) {
        return in("_id", oId(ids));
    }

    public static Bson byIds(List<String> ids) {
            return in("_id", oid(ids));
    }

    public static Bson byStatusValue(Object status) {
        return eq("status.value", String.valueOf(status));
    }

    public static Bson byState(HotelRfpBidStateStatus status) {
        return eq("state.status", status);
    }

    public static Bson byState(HotelRfpBidStateStatus... status) {
        return in("state.status", status);
    }

    public static Bson skipStatus(Object status) {
        return ne("status.value", String.valueOf(status));
    }

    public static Bson skipState(HotelRfpBidStateStatus... status) {
        return nin("status.value", status);
    }

    public static ObjectId oid(String id) {
        if(ObjectId.isValid(id)){
            return new ObjectId(id);
        } else {
            // todo: log
            System.out.println("NotFoundException = " + String.format("%s is not valid ObjectId", id));
            throw new NotFoundException();
        }
    }

    public static ObjectId oId(Id id) {
        if(id == null || id.value == null || !ObjectId.isValid(id.value)){
            // todo: log
            System.out.println("NotFoundException = " + String.format("%s is not valid ObjectId", id));
            throw new NotFoundException();
        } else {
            return new ObjectId(id.value);
        }
    }

    public static List<ObjectId> oId(List<Id> ids) {
        return ids.stream()
                .filter(id -> id != null && id.value != null && ObjectId.isValid(id.value))
                .map(o -> new ObjectId(o.value)).collect(Collectors.toList());
    }

    public static List<ObjectId> oid(List<String> ids) {
        return ids.stream()
                .filter(Objects::nonNull)
                .map(RbMongoFilters::oid)
                .collect(Collectors.toList());
    }

    public static Document include(String... fields) {
        final Document d = new Document();
        for(String field : fields){
            d.append(field, true);
        }
        return d;
    }

    public static Document include(Collection<String> fields) {
        final Document d = new Document();
        for(String field : fields){
            d.append(field, true);
        }
        return d;
    }

    public static Document exclude(String... fields) {
        final Document d = new Document();
        for(String field : fields){
            d.append(field, false);
        }
        return d;
    }

    public static Bson project(Bson project) {
        return Aggregates.project(project);
    }

    public static Bson match(Document criteria) {
        return Aggregates.match(criteria);
    }

    public static Bson match(Bson criteria) {
        return Aggregates.match(criteria);
    }

    public static Bson lookup(String from, String localField, String foreignField, String as) {
        return Aggregates.lookup(from, localField, foreignField, as);
    }

    public static Bson unwind(String field, boolean preserveNullOrEmptyArrays) {
        return Aggregates.unwind(field, new UnwindOptions().preserveNullAndEmptyArrays(preserveNullOrEmptyArrays));
    }

    public static Bson unwind(String field) {
        return unwind(field, true);
    }

    public static Document addFields(Document fields) {
        return doc("$addFields", fields);
    }

    public static Document doc(String field, Object value){
        return new Document(field, value);
    }

    public static List<Bson> joinCreatedAndStatus(Bson match, Bson projection) {
        final List<Bson> aggregates = new ArrayList<>();
        aggregates.add(match(match));
        aggregates.addAll(CreatedFields.CREATED_BY_JOIN);
        aggregates.addAll(StatusFields.STATUS_BY_JOIN);
        if (projection != null) {
            aggregates.add(project(projection));
        }
        return aggregates;
    }

    public static List<Bson> joinCreatedAndStatus(Bson match) {
        return joinCreatedAndStatus(match, null);
    }
}
