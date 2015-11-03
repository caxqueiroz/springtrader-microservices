package io.pivotal.springtrader.web.controllers;


import io.pivotal.springtrader.web.domain.Order;
import io.pivotal.springtrader.web.integration.PortfolioIntegrationService;
import io.pivotal.springtrader.web.integration.QuotesIntegrationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.security.core.Authentication;

import javax.servlet.http.HttpServletRequest;

@Controller
public class PortfolioController {
	private static final Logger logger = LoggerFactory
			.getLogger(PortfolioController.class);
	
	@Autowired
	private PortfolioIntegrationService portfolioIntegrationService;

	@Autowired
	private QuotesIntegrationService quotesIntegrationService;

	@RequestMapping(value = "/portfolio", method = RequestMethod.GET)
	public String portfolio(Model model) {
		logger.debug("/portfolio");
		model.addAttribute("marketSummary", quotesIntegrationService.getMarketSummary());
		
		//check if user is logged in!
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (!(authentication instanceof AnonymousAuthenticationToken)) {
		    String currentUserName = authentication.getName();
		    logger.debug("portfolio: User logged in: " + currentUserName);
		    
		    //TODO: add account summary.
		    try {
		    	model.addAttribute("portfolio", portfolioIntegrationService.getPortfolio(currentUserName));
		    } catch (HttpServerErrorException e) {
		    	logger.debug("error retrieving portfolfio: " + e.getMessage());
		    	model.addAttribute("portfolioRetrievalError",e.getMessage());
		    }
		    model.addAttribute("order", new Order());
		}
		
		return "portfolio";
	}

	@ExceptionHandler({ Exception.class })
	public ModelAndView error(HttpServletRequest req, Exception exception) {
		logger.debug("Handling error: " + exception);
		ModelAndView model = new ModelAndView();
		model.addObject("errorCode", exception.getMessage());
		model.addObject("errorMessage", exception);
		model.setViewName("error");
		exception.printStackTrace();
		return model;
	}

}
