package net.readybid.core.rfp.hotel.traveldestination;

public class DeleteTravelDestinationResult {

    public final long successCount;
    public final long failCount;

    DeleteTravelDestinationResult(long successCount, long failCount) {
        this.successCount = successCount;
        this.failCount = failCount;
    }
}
