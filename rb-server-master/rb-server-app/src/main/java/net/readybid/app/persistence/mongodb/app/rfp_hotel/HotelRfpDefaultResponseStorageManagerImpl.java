package net.readybid.app.persistence.mongodb.app.rfp_hotel;

import com.mongodb.client.model.BulkWriteOptions;
import com.mongodb.client.model.ReplaceOneModel;
import com.mongodb.client.model.UpdateOptions;
import com.mongodb.client.model.WriteModel;
import net.readybid.app.core.entities.entity.Entity;
import net.readybid.app.entities.rfp_hotel.HotelRfpDefaultResponse;
import net.readybid.app.interactors.rfp_hotel.gate.HotelRfpDefaultResponseStorageManager;
import net.readybid.app.persistence.mongodb.repository.HotelRfpDefaultResponseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

import static com.mongodb.client.model.Updates.combine;
import static net.readybid.mongodb.RbMongoFilters.byId;

@Service
public class HotelRfpDefaultResponseStorageManagerImpl implements HotelRfpDefaultResponseStorageManager {

    private final HotelRfpDefaultResponseRepository repository;

    @Autowired
    public HotelRfpDefaultResponseStorageManagerImpl(HotelRfpDefaultResponseRepository repository) {
        this.repository = repository;
    }

    @Override
    public void updateHotelData(Entity hotel) {
        repository.update(byId(hotel.getId()), combine(HotelRfpBidResponseUtils.createEntityUpdates(hotel, "answers")));
    }

    @Override
    public void setResponses(List<HotelRfpDefaultResponse> responses) {
        final List<WriteModel<HotelRfpDefaultResponse>> writes = responses.stream()
                .map(r -> new ReplaceOneModel<>(byId(r.hotelId), r, new UpdateOptions().upsert(true)) )
                .collect(Collectors.toList());

        if(!writes.isEmpty()) repository.bulkWrite(writes, new BulkWriteOptions().ordered(false));
    }
}
