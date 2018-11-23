package net.readybid.app.entities.rfp_hotel.dirty;

public class DeleteBidsResult {

    public final long successCount;
    public final long failCount;

    public DeleteBidsResult(long successCount, long failCount) {
        this.successCount = successCount;
        this.failCount = failCount;
    }
}
