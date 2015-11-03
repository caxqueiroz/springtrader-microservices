package io.pivotal.springtrader.quotes;


import io.pivotal.springtrader.quotes.domain.CompanyInfo;
import io.pivotal.springtrader.quotes.domain.Quote;
import io.pivotal.springtrader.quotes.exceptions.SymbolNotFoundException;
import io.pivotal.springtrader.quotes.services.QuoteService;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.*;

/**
 * Tests the QuoteService.
 * @author David Ferreira Pinto
 *
 */
public class QuoteServiceTest {

	QuoteService service = new QuoteService();
	/**
	 * Tests retrieving a quote from the external service.
	 * @throws Exception
	 */
	@Test
	public void getQuote() throws Exception {
		Quote quote = service.getQuote(TestConfiguration.QUOTE_SYMBOL);
		assertEquals(TestConfiguration.QUOTE_SYMBOL, quote.getSymbol());
		assertEquals(TestConfiguration.QUOTE_NAME, quote.getName());
	}
	/**
	 * Tests retrieving a quote with an unknown/null symbol from the external service.
	 * @throws Exception
	 */
	@Test(expected=SymbolNotFoundException.class)
	public void getNullQuote() throws Exception{
		Quote quote = service.getQuote(TestConfiguration.NULL_QUOTE_SYMBOL);
	}
	
	/**
	 * tests retrieving company information from external service.
	 * @throws Exception
	 */
	@Test
	public void getCompanyInfo() throws Exception {
		List<CompanyInfo> comps = service.getCompanyInfo(TestConfiguration.QUOTE_SYMBOL);
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
	 * tests retrieving company information from external service with unkown company.
	 * @throws Exception
	 */
	@Test
	public void getNullCompanyInfo() throws Exception {
		List<CompanyInfo> comps = service.getCompanyInfo(TestConfiguration.NULL_QUOTE_SYMBOL);
		assertTrue(comps.isEmpty());
	}
	
}
