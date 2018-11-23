package net.readybid.app.gate.repository;

import com.mongodb.BulkWriteException;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import net.readybid.app.core.entities.traveldestination.TravelDestinationImpl;
import net.readybid.exceptions.UnrecoverableException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;
import static org.mockito.internal.verification.VerificationModeFactory.times;

class TravelDestinationRepositoryImplTest {

    @Nested
    class SaveTravelDestinationRepositoryTest {


        @DisplayName("[ CreateTravelDestination ] Repository: ")
        @Nested
        class CreateTravelDestinationRepositoryTest{

            @DisplayName("When Creating in Bulk ")
            @Nested
            class CreateTravelDestinationsInBulkTest {

                MongoCollection<TravelDestinationImpl> destinationCollection;
                TravelDestinationRepositoryImpl repository;
                List<TravelDestinationImpl> destinationList;

                @BeforeEach
                void setup(){
                    //noinspection unchecked
                    destinationCollection = mock(MongoCollection.class);

                    final MongoDatabase mongoDatabase = mock(MongoDatabase.class);
                    when(mongoDatabase.getCollection(anyString(), eq(TravelDestinationImpl.class))).thenReturn(destinationCollection);

                    repository = new TravelDestinationRepositoryImpl(mongoDatabase);

                    destinationList = new ArrayList<>();
                }

                @DisplayName("it should save all Travel Destinations")
                @Test
                void saveAllTravelDestinationsTest() {
                    repository.createAll(destinationList);

                    verify(destinationCollection, times(1)).insertMany(eq(destinationList));
                }

                @DisplayName("it should clean up after failed write")
                @Test
                void onSaveAllFailedCleanUpDatabaseTest() {

                    doThrow(BulkWriteException.class).when(destinationCollection).insertMany(eq(destinationList));

                    try {
                        repository.createAll(destinationList);
                    } catch (Exception ignored){}

                    verify(destinationCollection, times(1)).deleteMany(any());
                }

                @DisplayName("it should throw UnrecoverableException after failed write")
                @Test
                void onSaveAllFailedThrowExceptionTest() {
                    doThrow(BulkWriteException.class).when(destinationCollection).insertMany(eq(destinationList));

                    assertThrows(UnrecoverableException.class, () -> { repository.createAll(destinationList); });
                }
            }
        }
    }
}