package net.readybid.app.use_cases.rfp_hotel.implementation;

import net.readybid.app.core.entities.entity.EntityType;
import net.readybid.app.entities.rfp_hotel.HotelRfpRepresentative;
import net.readybid.app.interactors.rfp_hotel.gate.HotelRfpRepresentativesLoader;
import net.readybid.app.use_cases.rfp_hotel.ListHotelRfpRepresentatives;
import net.readybid.web.RbViewModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;


@Service
public class ListHotelRfpRepresentativesImpl implements ListHotelRfpRepresentatives {

    private final HotelRfpRepresentativesLoader loader;

    @Autowired
    public ListHotelRfpRepresentativesImpl(HotelRfpRepresentativesLoader loader) {
        this.loader = loader;
    }

    @Override
    public RbViewModel forChain(String chainId) {
        final String chainAccountId = loader.getAccountIdForEntity(chainId);
        final List<String> chainAccountIdList = Collections.singletonList(chainAccountId);

        final List<HotelRfpRepresentative> registeredRepresentatives = loader.getRepresentativesFromAccounts(chainAccountIdList);
        final List<HotelRfpRepresentative> contactsFromBids = loader.getSupplierContactsFromBidsWithAccounts(chainAccountIdList);
        final List<HotelRfpRepresentative> representatives = mergeRepresentatives(registeredRepresentatives, contactsFromBids);
        return new RbViewModel() {
            @Override
            public Long getCount() {
                return (long) representatives.size();
            }

            @Override
            public Object getData() {
                return representatives;
            }
        };
    }

    @Override
    public RbViewModel forHotel(String hotelEntityId) {
        final List<String> hotelAndMasterChainAccountIds = loader.getHotelAndMasterChainAccountIdsForHotel(hotelEntityId);
        final List<HotelRfpRepresentative> representatives = loader.getRepresentativesFromAccounts(hotelAndMasterChainAccountIds);
        final List<HotelRfpRepresentative> contacts = loader.getSupplierContactsFromBidsWithAccounts(hotelAndMasterChainAccountIds);

        final List<HotelRfpRepresentative> result = mergeRepresentatives(representatives, contacts);

        return new RbViewModel() {
            @Override
            public Long getCount() {
                return (long) result.size();
            }

            @Override
            public Object getData() {
                return result;
            }
        };
    }

    private List<HotelRfpRepresentative> mergeRepresentatives(
            List<HotelRfpRepresentative> registeredRepresentatives,
            List<HotelRfpRepresentative> contactsFromBids
    ) {
        final ArrayList<HotelRfpRepresentative> representatives = new ArrayList<>();
        final Set<String> hotelRepresentativesEmails = new HashSet<>();
        final Set<String> chainRepresentativesEmails = new HashSet<>();

        registeredRepresentatives.forEach(r -> {
            representatives.add(r);
            final Set<String> repEmails =
                    EntityType.CHAIN.equals(r.accountType) ? chainRepresentativesEmails : hotelRepresentativesEmails;
            repEmails.add(r.emailAddress);
        });

        contactsFromBids.stream()
                .filter(c -> {
                    final Set<String> repEmails =
                            EntityType.CHAIN.equals(c.accountType) ? chainRepresentativesEmails : hotelRepresentativesEmails;
                    return !repEmails.contains(c.emailAddress);
                })
                .forEach(representatives::add);

        return representatives;
    }
}
