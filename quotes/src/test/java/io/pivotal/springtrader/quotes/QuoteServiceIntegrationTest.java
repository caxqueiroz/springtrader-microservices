package io.pivotal.springtrader.quotes;


import io.pivotal.springtrader.quotes.domain.CompanyInfo;
import io.pivotal.springtrader.quotes.domain.Quote;
import io.pivotal.springtrader.quotes.exceptions.SymbolNotFoundException;
import io.pivotal.springtrader.quotes.services.QuoteService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.greaterThan;
import static org.junit.Assert.*;

/**
 * Tests the QuoteService.
 * @author David Ferreira Pinto
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(QuotesApplication.class)
public class QuoteServiceIntegrationTest {

    @Autowired
	QuoteService quoteService;
	/**
	 * Tests retrieving a quote from the external quoteService.
	 * @throws Exception
	 */
	@Test
	public void getQuote() throws Exception {
		Quote quote = quoteService.getQuote(TestConfiguration.QUOTE_SYMBOL);
		assertEquals(TestConfiguration.QUOTE_SYMBOL, quote.getSymbol());
		assertEquals(TestConfiguration.QUOTE_NAME, quote.getName());
	}
	/**
	 * Tests retrieving a quote with an unknown/null symbol from the external quoteService.
	 * @throws Exception
	 */
	@Test(expected=SymbolNotFoundException.class)
	public void getNullQuote() throws Exception{
		Quote quote = quoteService.getQuote(TestConfiguration.NULL_QUOTE_SYMBOL);
	}
	
	/**
	 * tests retrieving company information from external quoteService.
	 * @throws Exception
	 */
	@Test
	public void getCompanyInfo() throws Exception {
		List<CompanyInfo> comps = quoteService.getCompanyInfo(TestConfiguration.QUOTE_SYMBOL);
		assertFalse(comps.isEmpty());
		boolean pass = false;
		for (CompanyInfo info : comps) {
			if (info.getSymbol().equals(TestConfiguration.QUOTE_SYMBOL)) {
				pass = true;
			}
		}
		assertTrue(pass);
	}
	/**
	 * tests retrieving company information from external quoteService with unkown company.
	 * @throws Exception
	 */
	@Test
	public void getNullCompanyInfo() throws Exception {
		List<CompanyInfo> comps = quoteService.getCompanyInfo(TestConfiguration.NULL_QUOTE_SYMBOL);
		assertTrue(comps.isEmpty());
	}

	@Test
	public void searchForCompanies() throws Exception{
		List<CompanyInfo> comps = quoteService.getCompanyInfo("alphabet");
		comps.stream().forEach(System.out::println);
		assertThat(comps.size(),greaterThan(1));
		assertThat(comps.stream().anyMatch(c-> c.getSymbol().equalsIgnoreCase("GOOGL")), equalTo(true));
		assertThat(comps.stream().anyMatch(c-> c.getSymbol().equalsIgnoreCase("GOOG")), equalTo(true));


	}
	
}
