package io.pivotal.springtrader.web.controllers;


import io.pivotal.springtrader.web.domain.Account;
import io.pivotal.springtrader.web.domain.AuthenticationRequest;
import io.pivotal.springtrader.web.integration.AccountsIntegrationService;
import io.pivotal.springtrader.web.integration.PortfolioIntegrationService;
import io.pivotal.springtrader.web.integration.QuotesIntegrationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.Map;

@Controller
public class AccountsController {

	private static final Logger logger = LoggerFactory.getLogger(AccountsController.class);
	
	@Autowired
	private AccountsIntegrationService accountsIntegrationService;
		
	@Autowired
	private QuotesIntegrationService quotesIntegrationService;

    @Autowired
    private PortfolioIntegrationService portfolioIntegrationService;
	
	@RequestMapping(value = "/", method = RequestMethod.GET)
	public String showHome(Model model) {
		if (!model.containsAttribute("login")) {
			model.addAttribute("login", new AuthenticationRequest());
		}
		model.addAttribute("marketSummary", quotesIntegrationService.getMarketSummary());
		
		//check if user is logged in!
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (!(authentication instanceof AnonymousAuthenticationToken)) {
		    String currentUserName = authentication.getName();
		    logger.debug("User logged in: " + currentUserName);
		    
		    try {
		    	model.addAttribute("portfolio",portfolioIntegrationService.getPortfolio(currentUserName));
		    } catch (HttpServerErrorException e) {
		    	model.addAttribute("portfolioRetrievalError",e.getMessage());
		    }
		    model.addAttribute("account", accountsIntegrationService.getAccount(currentUserName));
		}
		
		return "index";
	}
	
	//TODO: never gets called?
	@RequestMapping(value = "/login", method = RequestMethod.POST)
	//@RequestMapping(value = "/login")
	public String login(Model model, @ModelAttribute(value="login") AuthenticationRequest login) {
		logger.info("Logging in, user: " + login.getUsername());
		//need to add account object to session?
		//CustomDetails userDetails = (CustomDetails)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		logger.debug("Principal: " + SecurityContextHolder.getContext().
				   getAuthentication().getPrincipal());
		Map<String,Object> params = accountsIntegrationService.login(login);
		model.addAllAttributes(params);
		//logger.info("got user details, token: " + userDetails.getToken());
		return "index";
	}
	
	@RequestMapping(value = "/login", method = RequestMethod.GET)
	public String getLogin(Model model, @ModelAttribute(value="login") AuthenticationRequest login) {
		logger.info("Logging in GET, user: " + login.getUsername());
		return "index";
	}
	
	@RequestMapping(value="/logout", method = RequestMethod.POST)
	public String postLogout(Model model, @ModelAttribute(value="login") AuthenticationRequest login) {
		logger.info("Logout, user: " + login.getUsername());
		logger.info(model.asMap().toString());
		return "index";
	}
	
	@RequestMapping(value = "/registration", method = RequestMethod.GET)
	public String registration(Model model) {
		model.addAttribute("account", new Account());
		return "registration";
	}
	@RequestMapping(value = "/registration", method = RequestMethod.POST)
	public String register(Model model, @ModelAttribute(value="account") Account account) {
		logger.info("register: user:" + account.getUserid());
		
		//need to set some stuff on account...
		
		account.setOpenbalance(account.getBalance());
		account.setCreationdate(new Date());
		
		AuthenticationRequest login = new AuthenticationRequest();
		login.setUsername(account.getUserid());
		model.addAttribute("login", login);
		model.addAttribute("marketSummary", quotesIntegrationService.getMarketSummary());
		accountsIntegrationService.createAccount(account);
		return "index";
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
