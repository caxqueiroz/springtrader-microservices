package io.pivotal.springtrader.accounts.services;


import io.pivotal.springtrader.accounts.domain.Account;
import io.pivotal.springtrader.accounts.exceptions.AuthenticationException;
import io.pivotal.springtrader.accounts.exceptions.NoRecordsFoundException;
import io.pivotal.springtrader.accounts.repositories.AccountRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * The service in the accountRepository microservice.
 * 
 * @author David Ferreira Pinto
 * @author cq
 *
 */
@Service
public class AccountService {

	private static final Logger logger = LoggerFactory.getLogger(AccountService.class);

	/**
	 * The accountRepository repository.
	 */
	@Autowired
	AccountRepository accountRepository;

	/**
	 * Retrieve an account with given id.
	 * The id here is the unique id value of the account managed by the repository (auto-increment).
	 * 
	 * @param id The id of the account.
	 * @return The account object if found or throws a NoRecordsFoundException.
	 */
	public Account getAccount(Integer id) {

		logger.debug("AccountService.getAccount: id=" + id);

		Account account = accountRepository.findOne(id);
		if (account == null) {
			logger.warn("AccountService.getAccount: could not find account with id: " + id);
			throw new NoRecordsFoundException();
		}

		logger.info(String.format("AccountService.getAccount - retrieved account with id: %s. Payload is: %s", id, account));

		return account;
	}

	/**
	 * Retrieve an account with given id.
	 * The id here is the unique user id value of the account, ie the username.
	 * 
	 * @param id The user id of the account.
	 * @return The account object if found or throws a NoRecordsFoundException.
	 */
	public Account getAccount(String id) {

		logger.debug("AccountService.getAccount: id=" + id);

		Account account = accountRepository.findByUserid(id);
		if (account == null) {
			logger.warn("AccountService.getAccount: could not find account with id: " + id);
			throw new NoRecordsFoundException();
		}

		logger.info(String.format("AccountService.getAccount - retrieved account with id: %s. Payload is: %s", id, account));

		return account;
	}

	/**
	 * Retrieves the account by the authorization token associated with that account and current login.
	 * 
	 * @param token The token to search for.
	 * @return The account object if found or AuthenticationException otherwise.
	 */
	@Cacheable(value = "authorizationCache")
	public Account findAccountProfileByAuthToken(String token) {
		logger.debug("AccountService.findAccountProfileByAuthToken looking for authToken: " + token);
		if (token == null) {
			//TODO: no point in checking database. throw exception here.
			logger.error("AccountService.findAccountProfileByAuthToken(): token is null");
			throw new AuthenticationException("Authorization Token is null");
		}
		Account accountProfile = null;
		accountProfile = accountRepository.findByAuthtoken(token);
		if (accountProfile == null) {
			logger.error("AccountService.findAccountProfileByAuthToken(): accountProfile is null for token="
					+ token);
			throw new AuthenticationException("Authorization Token not found");
		}

		return accountProfile;
	}

	/**
	 * Saves the given account in the repository.
	 * 
	 * @param accountRequest The account to save.
	 * @return the id of the account.
	 */
	public Integer saveAccount(Account accountRequest) {

		logger.debug("AccountService.saveAccount:" + accountRequest.toString());
		// need to set some stuff that cannot be null!
		if (accountRequest.getLogincount() == null) accountRequest.setLogincount(0);
		if (accountRequest.getLogoutcount() == null) accountRequest.setLogoutcount(0);
		if(accountRequest.getCreationdate() == null) accountRequest.setCreationdate(new Date());


		Account account = accountRepository.save(accountRequest);
		logger.info("AccountService.saveAccount: account saved: " + account);
		return account.getId();
	}

	/**
	 * Attempts to login the user with the given username and password.
	 * Throws AuthenticationException if an account with the given username and password cannot be found.
	 * 
	 * @param username The username to login.
	 * @param password The password to use.
	 * @return a map with the authtoken, account Id.
	 */
	public Map<String, Object> login(String username, String password) {
		logger.debug("login in user: " + username);
		Map<String, Object> loginResponse;
		Account account = accountRepository.findByUseridAndPasswd(username, password);
		if (account != null) {
			logger.debug("Found Account for user: " + username);
			account.setAuthtoken(UUID.randomUUID().toString());
			account.setLogincount(account.getLogincount() + 1);
			account.setLastlogin(new Date());
			account = accountRepository.save(account); // persist new auth token and last
												// login
			loginResponse = new HashMap<>();

			loginResponse.put("authToken", account.getAuthtoken());
			loginResponse.put("accountid", account.getId());
			
			logger.info("AccountService.login success for " + username
					+ " username::token=" + loginResponse.get("authToken"));
			
		} else {
			logger.warn("AccountService.login failed to find username="
					+ username + " password=" + password);
			throw new AuthenticationException("Login failed for user: "
					+ username);
		}
		return loginResponse;
	}

	/**
	 * logs the give user out of the system.
	 * 
	 * @param userId the userid to logout.
	 * @return The account object or null;
	 */
	public Account logout(String userId) {
		logger.debug("AccountService.logout: Logging out account with userId: " + userId);
		Account account = accountRepository.findByUserid(userId);
		if (account != null) {
			account.setAuthtoken(null); // remove token
			account.setLogoutcount(account.getLogoutcount() + 1);
			accountRepository.save(account);
			logger.info("AccountService.logout: Account logged out: " + account.getUserid());
		} else {
			logger.warn("AccountService.logout: Could not find account to logout with userId: " + userId);
		}
		return account;
	}

	/**
	 * Only positive amounts can be added to the balance.
	 * @param amount
	 * @param userId
	 * @return
	 */
	public double increaseBalance(double amount, String userId) {

		Account accountResponse = accountRepository.findByUserid(userId);

		BigDecimal currentBalance = accountResponse.getBalance();

		BigDecimal newBalance = currentBalance.add(new BigDecimal(amount));

		if(amount > 0.0){
			logger.debug("AccountController.increaseBalance: new balance='" + newBalance + "'.");
			accountResponse.setBalance(newBalance);
			saveAccount(accountResponse);
		}

		return accountResponse.getBalance().doubleValue();

	}

	/**
	 *
	 * @param amount
	 * @param accountId
	 * @return
	 */
	public double decreaseBalance(double amount, String accountId) {

		Account accountResponse = accountRepository.findByUserid(accountId);

		BigDecimal currentBalance = accountResponse.getBalance();

		if(currentBalance.doubleValue() >= amount){
            BigDecimal newBalance = currentBalance.subtract(new BigDecimal(amount));
			logger.debug("AccountController.decreaseBalance: new balance='" + newBalance + "'.");
			accountResponse.setBalance(newBalance);
			saveAccount(accountResponse);
		}

		return accountResponse.getBalance().doubleValue();

	}
}
