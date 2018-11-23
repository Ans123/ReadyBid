package net.readybid.app.use_cases.rfp_hotel.implementation;

import net.readybid.app.RbViewModelAssert;
import net.readybid.app.core.entities.entity.EntityType;
import net.readybid.app.entities.rfp_hotel.HotelRfpRepresentative;
import net.readybid.app.entities.rfp_hotel.HotelRfpRepresentativeFactory;
import net.readybid.app.interactors.rfp_hotel.gate.HotelRfpRepresentativesLoader;
import net.readybid.test_utils.RbRandom;
import net.readybid.web.RbViewModel;
import org.assertj.core.api.ListAssert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.function.Supplier;

import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.same;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;

@DisplayName("ListHotelRfpRepresentatives ")
class ListHotelRfpRepresentativesImplTest {

    private ListHotelRfpRepresentativesImpl sut;

    private HotelRfpRepresentativesLoader hotelRfpRepresentativesLoader;

    @BeforeEach
    void setup(){
        hotelRfpRepresentativesLoader = mock(HotelRfpRepresentativesLoader.class);

        sut = new ListHotelRfpRepresentativesImpl(hotelRfpRepresentativesLoader);
    }

    @Nested
    @DisplayName("for Chain ")
    class ForChain{

        private String chainEntityId;
        private String masterChainAccountId;

        private Supplier<RbViewModel> mut = () -> sut.forChain(chainEntityId);

        @BeforeEach
        void setup(){
            chainEntityId = RbRandom.idAsString();
            masterChainAccountId = RbRandom.idAsString();
            doReturn(masterChainAccountId).when(hotelRfpRepresentativesLoader)
                    .getAccountIdForEntity(chainEntityId);
        }


        @DisplayName("gives empty list when no Representatives or Contacts are found")
        @Test
        void givesEmptyListWhenNoRepresentativesOrContactsFound(){
            final RbViewModel response = mut.get();

            RbViewModelAssert.that(response)
                    .dataAsList(ListAssert::isEmpty)
                    .countIs(0L);
        }

        @DisplayName("lists all Chain Representatives and Contacts from Bids with unique email address")
        @Test
        void listsAllChainRepresentativesAndContactsFromBidsTest(){
            final List<HotelRfpRepresentative> chainRepresentatives = HotelRfpRepresentativeFactory.listOfRandom(10);
            final List<HotelRfpRepresentative> representativesFromBids = HotelRfpRepresentativeFactory.listOfRandom(10);

            doReturn(chainRepresentatives).when(hotelRfpRepresentativesLoader).getRepresentativesFromAccounts(anyList());
            doReturn(representativesFromBids).when(hotelRfpRepresentativesLoader).getSupplierContactsFromBidsWithAccounts(anyList());

            final RbViewModel response = mut.get();

            RbViewModelAssert.that(response)
                    .dataAsList(data -> data
                            .containsAll(chainRepresentatives)
                            .containsAll(representativesFromBids));
        }

        @DisplayName("lists Chain Representative when both Representative and Contact have same email address")
        @Test
        void test(){
            final String duplicateEmailAddress = RbRandom.emailAddress();
            final HotelRfpRepresentative chainRepresentative = HotelRfpRepresentativeFactory.random(duplicateEmailAddress, EntityType.CHAIN);
            final HotelRfpRepresentative chainContact = HotelRfpRepresentativeFactory.random(duplicateEmailAddress, EntityType.CHAIN);

            doReturn(Collections.singletonList(chainRepresentative))
                    .when(hotelRfpRepresentativesLoader).getRepresentativesFromAccounts(anyList());
            doReturn(Collections.singletonList(chainContact))
                    .when(hotelRfpRepresentativesLoader).getSupplierContactsFromBidsWithAccounts(anyList());

            final RbViewModel response = mut.get();

            RbViewModelAssert.that(response)
                    .dataAsList(data -> data
                            .contains(chainRepresentative)
                            .doesNotContain(chainContact));
        }
    }


    @Nested
    @DisplayName("for Hotel ")
    class ForHotel{

        private String hotelEntityId;
        private List<String> hotelAndMasterChainAccountIds;

        private Supplier<RbViewModel> mut = () -> sut.forHotel(hotelEntityId);

        @BeforeEach
        void setup(){
            hotelEntityId = RbRandom.idAsString();
            hotelAndMasterChainAccountIds = mock(List.class);
            doReturn(hotelAndMasterChainAccountIds).when(hotelRfpRepresentativesLoader)
                    .getHotelAndMasterChainAccountIdsForHotel(hotelEntityId);
        }

        @DisplayName("gives empty list when no Representatives or Contacts are found")
        @Test
        void givesEmptyListWhenNoRepresentativesOrContactsFound(){
            final RbViewModel response = mut.get();

            RbViewModelAssert.that(response)
                    .dataAsList(ListAssert::isEmpty)
                    .countIs(0L);
        }

        @DisplayName("lists all Representatives for Hotel and Hotel Master Chain and all Contacts for Hotel and Hotel Master Chain")
        @Test
        void listsAllHotelRepresentatives(){
            final List<HotelRfpRepresentative> representatives = HotelRfpRepresentativeFactory.listOfRandom(10);
            final List<HotelRfpRepresentative> contacts = HotelRfpRepresentativeFactory.listOfRandom(10);

            doReturn(representatives).when(hotelRfpRepresentativesLoader)
                    .getRepresentativesFromAccounts(hotelAndMasterChainAccountIds);

            doReturn(contacts).when(hotelRfpRepresentativesLoader)
                    .getSupplierContactsFromBidsWithAccounts(hotelAndMasterChainAccountIds);

            final RbViewModel response = mut.get();

            RbViewModelAssert.that(response)
                    .dataAsList(data -> data
                            .containsAll(representatives)
                            .containsAll(contacts));
        }

        @DisplayName("lists Representatives if they have same email address as Contacts")
        @Test
        void test(){
            final String duplicateEmailAddress = RbRandom.emailAddress();
            final HotelRfpRepresentative hotelRepresentative =
                    HotelRfpRepresentativeFactory.random(duplicateEmailAddress, EntityType.HOTEL);
            final HotelRfpRepresentative hotelContact =
                    HotelRfpRepresentativeFactory.random(duplicateEmailAddress, EntityType.HOTEL);
            final HotelRfpRepresentative chainRepresentative =
                    HotelRfpRepresentativeFactory.random(duplicateEmailAddress, EntityType.CHAIN);
            final HotelRfpRepresentative chainContact =
                    HotelRfpRepresentativeFactory.random(duplicateEmailAddress, EntityType.CHAIN);

            doReturn(Arrays.asList(hotelRepresentative, chainRepresentative)).when(hotelRfpRepresentativesLoader)
                    .getRepresentativesFromAccounts(same(hotelAndMasterChainAccountIds));

            doReturn(Arrays.asList(hotelContact, chainContact)).when(hotelRfpRepresentativesLoader)
                    .getSupplierContactsFromBidsWithAccounts(same(hotelAndMasterChainAccountIds));

            final RbViewModel response = mut.get();

            RbViewModelAssert.that(response)
                    .dataAsList(data -> data
                            .contains(hotelRepresentative)
                            .contains(chainRepresentative)
                            .doesNotContain(hotelContact)
                            .doesNotContain(chainContact));
        }


        @DisplayName("lists both Contacts if they have same email address but different type")
        @Test
        void listsBothRepresentativesEvenIfTheyHaveSameEmailAddress(){
            final String duplicateEmailAddress = RbRandom.emailAddress();
            final HotelRfpRepresentative hotelContact =
                    HotelRfpRepresentativeFactory.random(duplicateEmailAddress, EntityType.HOTEL);
            final HotelRfpRepresentative chainContact =
                    HotelRfpRepresentativeFactory.random(duplicateEmailAddress, EntityType.CHAIN);

            doReturn(Arrays.asList(hotelContact, chainContact)).when(hotelRfpRepresentativesLoader)
                    .getSupplierContactsFromBidsWithAccounts(same(hotelAndMasterChainAccountIds));

            final RbViewModel response = mut.get();

            RbViewModelAssert.that(response)
                    .dataAsList(data -> data
                            .contains(hotelContact)
                            .contains(chainContact));
        }

        @DisplayName("lists both Representative and Contact if they have same email address but different account type")
        @Test
        void listsBothRepresentativeAndContactIfTheyHaveSameEmailAddressButDifferentAccountType(){
            final String duplicateEmailAddress = RbRandom.emailAddress();
            final HotelRfpRepresentative hotelRepresentative =
                    HotelRfpRepresentativeFactory.random(duplicateEmailAddress, EntityType.HOTEL);
            final HotelRfpRepresentative chainContact =
                    HotelRfpRepresentativeFactory.random(duplicateEmailAddress, EntityType.CHAIN);

            doReturn(Collections.singletonList(hotelRepresentative)).when(hotelRfpRepresentativesLoader)
                    .getRepresentativesFromAccounts(same(hotelAndMasterChainAccountIds));

            doReturn(Collections.singletonList(chainContact)).when(hotelRfpRepresentativesLoader)
                    .getSupplierContactsFromBidsWithAccounts(same(hotelAndMasterChainAccountIds));

            final RbViewModel response = mut.get();

            RbViewModelAssert.that(response)
                    .dataAsList(data -> data
                            .contains(hotelRepresentative)
                            .contains(chainContact));
        }
    }

}