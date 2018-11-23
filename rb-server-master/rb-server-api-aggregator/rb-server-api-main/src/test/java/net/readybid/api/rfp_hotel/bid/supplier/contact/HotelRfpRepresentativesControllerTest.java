package net.readybid.api.rfp_hotel.bid.supplier.contact;

import com.fasterxml.jackson.databind.ObjectMapper;
import net.readybid.app.entities.rfp_hotel.HotelRfpRepresentative;
import net.readybid.app.entities.rfp_hotel.HotelRfpRepresentativeFactory;
import net.readybid.app.use_cases.rfp_hotel.ListHotelRfpRepresentatives;
import net.readybid.test_utils.RbRandom;
import net.readybid.web.RbViewModel;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.skyscreamer.jsonassert.JSONAssert;
import org.skyscreamer.jsonassert.JSONCompareMode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

import static org.assertj.core.api.Java6Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@DisplayName("HotelRfpRepresentativesController ")
@ExtendWith(SpringExtension.class)
@WebMvcTest(HotelRfpRepresentativesController.class)
@ActiveProfiles("default,test")
class HotelRfpRepresentativesControllerTest {

    private static final String URL_CHAIN = "/rfps/hotel/representatives/chain/%s";
    private static final String URL_HOTEL = "/rfps/hotel/representatives/hotel/%s";

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper mapper;

    @MockBean
    private ListHotelRfpRepresentatives listHotelRfpRepresentatives;

    private JacksonTester<RbViewModel> jsonResponse;

    @BeforeEach
    void setUp(){
        JacksonTester.initFields(this, mapper);
    }

    @AfterEach
    void teardown(){
        Mockito.reset(listHotelRfpRepresentatives);
    }

    @Nested
    class ListChainRepresentativesTest {

        private String chainId;
        private final Supplier<MockHttpServletResponse> mut = () -> {
            try {
                return mockMvc
                        .perform(MockMvcRequestBuilders.get(String.format(URL_CHAIN, chainId)).contentType(MediaType.APPLICATION_JSON_UTF8))
                        .andReturn()
                        .getResponse();
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        };

        @BeforeEach
        void setup(){
            chainId = RbRandom.oid().toString();
        }

        @Test
        void returnsEmptyListIfNoRepresentatives() throws Exception {
            doReturn(buildViewModel(new ArrayList<>()))
                    .when(listHotelRfpRepresentatives).forChain(eq(chainId));

            final MockHttpServletResponse response = mut.get();

            assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
            JSONAssert.assertEquals("{ data: [], count: 0 }", response.getContentAsString(), JSONCompareMode.LENIENT);
        }

        @Test
        void returnsList() throws Exception {
            final List<HotelRfpRepresentative> representatives = HotelRfpRepresentativeFactory.listOfRandom(50);
            final RbViewModel representativesResponse = buildViewModel(representatives);

            doReturn(representativesResponse)
                    .when(listHotelRfpRepresentatives).forChain(eq(chainId));

            final MockHttpServletResponse response = mut.get();

            assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
            JSONAssert.assertEquals(jsonResponse.write(representativesResponse).getJson(), response.getContentAsString(), JSONCompareMode.LENIENT);
            verify(listHotelRfpRepresentatives, times(1)).forChain(any());
        }
    }

    @Nested
    class ListHotelRepresentativesTest {

        private String hotelId;
        private final Supplier<MockHttpServletResponse> mut = () -> {
            try {
                return mockMvc
                        .perform(MockMvcRequestBuilders.get(String.format(URL_HOTEL, hotelId)).contentType(MediaType.APPLICATION_JSON_UTF8))
                        .andReturn()
                        .getResponse();
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        };


        @BeforeEach
        void setup(){
            hotelId = RbRandom.oid().toString();
        }

        @Test
        void returnsEmptyListIfNoRepresentatives() throws Exception {
            doReturn(buildViewModel(new ArrayList<>()))
                    .when(listHotelRfpRepresentatives).forHotel(eq(hotelId));

            final MockHttpServletResponse response = mut.get();

            assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
            JSONAssert.assertEquals("{ data: [], count: 0 }", response.getContentAsString(), JSONCompareMode.LENIENT);
        }

        @Test
        void returnsList() throws Exception {
            final List<HotelRfpRepresentative> representatives = HotelRfpRepresentativeFactory.listOfRandom(50);
            final RbViewModel representativesResponse = buildViewModel(representatives);

            doReturn(representativesResponse)
                    .when(listHotelRfpRepresentatives).forHotel(eq(hotelId));

            final MockHttpServletResponse response = mut.get();

            assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
            JSONAssert.assertEquals(jsonResponse.write(representativesResponse).getJson(), response.getContentAsString(), JSONCompareMode.LENIENT);
            verify(listHotelRfpRepresentatives, times(1)).forHotel(any());
        }
    }

    private RbViewModel buildViewModel(List<HotelRfpRepresentative> representatives) {
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
}