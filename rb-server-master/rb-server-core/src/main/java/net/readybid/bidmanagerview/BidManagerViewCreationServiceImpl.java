package net.readybid.bidmanagerview;

import net.readybid.app.core.entities.entity.EntityType;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by DejanK on 5/19/2017.
 *
 */
@Service
public class BidManagerViewCreationServiceImpl implements BidManagerViewCreationService {

    private final BidManagerViewFactory bidManagerViewFactory;
    private final BidManagerViewCreationRepository repository;

    @Autowired
    public BidManagerViewCreationServiceImpl(BidManagerViewFactory bidManagerViewFactory, BidManagerViewCreationRepository repository) {
        this.bidManagerViewFactory = bidManagerViewFactory;
        this.repository = repository;
    }

    @Override
    public BidManagerView createDefaultViewsForUser(EntityType accountType, ObjectId accountId) {
        BidManagerView view = bidManagerViewFactory.createDefaultView(accountType, accountId);
        view = repository.createViewIfDoesNotExist(view);
        return view;
    }

    @Override
    public void createChainRepRfpViews(List<HotelRfpCreateChainRfpBidManagerViewCommand> commands) {
        final List<BidManagerView> views = commands.stream()
        .map(c -> bidManagerViewFactory.createChainHotelRfpView(c.name, c.rfpId, c.accountId))
        .collect(Collectors.toList());

        repository.createViews(views);
    }
}
