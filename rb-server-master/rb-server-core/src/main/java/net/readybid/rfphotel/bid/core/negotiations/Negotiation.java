package net.readybid.rfphotel.bid.core.negotiations;

import org.bson.types.ObjectId;

import java.util.Date;

/**
 * Created by DejanK on 7/25/2017.
 *
 */
public abstract class Negotiation {

    protected ObjectId _id;
    protected Negotiator from;
    protected String message;
    protected Date at;

    public ObjectId getId() {
        return _id;
    }

    public Negotiator getFrom() {
        return from;
    }

    public String getMessage() {
        return message;
    }

    public Date getAt() {
        return at;
    }

    public void setId(ObjectId id) {
        this._id = id;
    }

    public void setFrom(NegotiatorImpl from) {
        this.from = from;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setAt(Date sentAt) {
        this.at = sentAt;
    }

    public abstract NegotiationValues getValues();
}
