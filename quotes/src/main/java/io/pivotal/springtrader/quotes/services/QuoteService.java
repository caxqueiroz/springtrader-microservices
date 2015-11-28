package io.pivotal.springtrader.quotes.services;


import io.pivotal.springtrader.quotes.domain.CompanyInfo;
import io.pivotal.springtrader.quotes.domain.Quote;
import io.pivotal.springtrader.quotes.exceptions.SymbolNotFoundException;
import io.pivotal.springtrader.quotes.repositories.QuoteRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestOperations;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * A service to retrieve Company and Quote information.
 * 
 * @author David Ferreira Pinto
 * @author cq
 *
 */
@Service
public class QuoteService {


    private static final Logger logger = LoggerFactory.getLogger(QuoteService.class);

	@Value("${api.url.company}")
    private String companyUrl = "http://dev.markitondemand.com/MODApis/Api/v2/Lookup/json?input={name}";

    @Value("${api.url.quote}")
    private String quoteUrl = "http://dev.markitondemand.com/MODApis/Api/v2/Quote/json?symbol={symbol}";

	
	private RestOperations restOperations = new RestTemplate();

    @Autowired
    QuoteRepository quoteRepository;


	/**
	 * Retrieves an up to date quote for the given symbol.
	 * 
	 * @param symbol The symbol to retrieve the quote for.
	 * @return The quote object or null if not found.
	 * @throws SymbolNotFoundException 
	 */

    public Quote getQuote(String symbol) throws Exception {
        logger.debug("QuoteService.getQuote: retrieving quote for: " + symbol);
        Quote quote;
        symbol = symbol.toUpperCase();

        if(quoteRepository.exists(symbol)) return quoteRepository.findOne(symbol);

        Map<String, String> params = new HashMap<>();
        params.put("symbol", symbol);

        try{

            quote = restOperations.getForObject(quoteUrl, Quote.class, params);
            logger.debug("QuoteService.getQuote: retrieved quote: " + quote);

        } catch(Exception e){
            logger.error(e.getMessage(),e);
            throw new SymbolNotFoundException("Symbol not found: " + symbol);
        }

        if (quote.getSymbol() ==  null) {
            throw new SymbolNotFoundException("Symbol not found: " + symbol);
        }

        try{

            quote = quoteRepository.save(quote);

        }catch(Exception e){
            logger.error(e.getMessage(),e);
        }

        return quote;

    }

    /**
	 * Retrieves a list of CompanyInfo objects.
	 * Given the name parameters, the return list will contain objects that match the search both
	 * on company name as well as symbol.
	 * @param name The search parameter for company name or symbol.
	 * @return The list of company information.
	 */
	public List<CompanyInfo> getCompanyInfo(String name) {
        logger.debug("QuoteService.getCompanyInfo: retrieving info for: " + name);

        try {
            Map<String, String> params = new HashMap<>();
            params.put("name", name);
            CompanyInfo[] companies = restOperations.getForObject(companyUrl, CompanyInfo[].class, params);
            List<CompanyInfo> companiesInfo = Arrays.asList(companies);
            logger.debug("QuoteService.getCompanyInfo: retrieved info: " + companiesInfo);
            return companiesInfo.stream().filter(c-> c!=null).collect(Collectors.toList());
        }catch(Exception e){
            logger.error(e.getMessage(),e);
        }

        return Arrays.asList(new CompanyInfo());
	}
}