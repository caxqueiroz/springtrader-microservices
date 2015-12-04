package io.pivotal.springtrader.quotes;


import io.pivotal.springtrader.quotes.controllers.QuoteController;
import io.pivotal.springtrader.quotes.domain.Stock;
import io.pivotal.springtrader.quotes.exceptions.SymbolNotFoundException;
import io.pivotal.springtrader.quotes.services.QuoteService;
import net.minidev.json.JSONArray;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.isA;
import static org.hamcrest.Matchers.notNullValue;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Tests for the QuoteController.
 *
 * @author David Ferreira Pinto
 * @author cq
 */
public class QuoteControllerTest {
    MockMvc mockMvc;

    @InjectMocks
    QuoteController controller;

    @Mock
    QuoteService service;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);

        this.mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
    }

    /*
     * Tests the <code>/stock</code> REST endpoint.
     * test fetching a stock succesfully.
     */
    @Test
    public void getQuote() throws Exception {
        when(service.getQuote(TestConfiguration.QUOTE_SYMBOL)).thenReturn(TestConfiguration.stock());

        mockMvc.perform(
                get("/quote/" + TestConfiguration.QUOTE_SYMBOL).contentType(
                        MediaType.APPLICATION_JSON)).andExpect(status().isOk())
                .andDo(print())
                .andExpect(
                        content().contentTypeCompatibleWith(
                                MediaType.APPLICATION_JSON))
                .andExpect(
                        jsonPath("$.Name").value(
                                TestConfiguration.QUOTE_NAME))
                .andExpect(
                        jsonPath("$.Symbol").value(
                                TestConfiguration.QUOTE_SYMBOL))
                .andExpect(
                        jsonPath("$.LastPrice").value(
                                TestConfiguration.QUOTE_LAST_PRICE))
                //.andExpect(
                //		jsonPath("$.Change",Matchers.equalTo( (Number)TestConfiguration.QUOTE_CHANGE))))
                .andExpect(
                        jsonPath("$.ChangePercent").value(
                                TestConfiguration.QUOTE_CHANGE_PERCENT))
                .andExpect(
                        jsonPath("$.Timestamp", notNullValue()))
                .andExpect(
                        jsonPath("$.MSDate").value(
                                TestConfiguration.QUOTE_MSDATE));
    }

    /*
     * Tests the <code>/stock</code> REST endpoint.
     * test fetching a stock that has a null symbol and throws exception.
     */
    @Test
    public void getNullQuote() throws Exception {
        when(service.getQuote(TestConfiguration.NULL_QUOTE_SYMBOL)).thenThrow(
                new SymbolNotFoundException(TestConfiguration.NULL_QUOTE_SYMBOL));

        mockMvc.perform(
                get("/quote/" + TestConfiguration.NULL_QUOTE_SYMBOL).contentType(
                        MediaType.APPLICATION_JSON)).andExpect(status().isBadRequest())
                .andDo(print());

    }

    /*
     * Tests the <code>/company</code> REST endpoint.
     * test fetching a company information.
     */
    @Test
    public void getCompanies() throws Exception {
        List<Stock> comps = new ArrayList<>();
        comps.add(TestConfiguration.stock());
        when(service.companiesByNameOrSymbol(TestConfiguration.QUOTE_NAME)).thenReturn(comps);
        mockMvc.perform(
                get("/company/" + TestConfiguration.QUOTE_NAME).contentType(
                        MediaType.APPLICATION_JSON)).andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$..*", isA(JSONArray.class)));
    }
}
