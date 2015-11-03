package io.pivotal.springtrader.accounts;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.pivotal.springtrader.accounts.controllers.AccountController;
import io.pivotal.springtrader.accounts.services.AccountService;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.math.BigDecimal;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Tests for the AccountsController.
 * @author David Ferreira Pinto
 *
 */
public class AccountsControllerTest {
	private static String API_ROLE = "API_USER";
	MockMvc mockMvc;

	@InjectMocks
	AccountController controller;

	@Mock
	AccountService service;

	@Before
	public void setup() {
		MockitoAnnotations.initMocks(this);

		this.mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
	}

	@Before
	public void login() {

	}

	@After
	public void logout() {
		//SecurityContextHolder.clearContext();
	}

	/**
	 * Test the POST to <code>/account</code>.
	 * test creation of accounts.
	 * @throws Exception
	 */
	@Test
	public void doPostAccount() throws Exception {
		when(service.saveAccount(ServiceTestConfiguration.account()))
				.thenReturn(ServiceTestConfiguration.PROFILE_ID);

		mockMvc.perform(
				post("/account").contentType(MediaType.APPLICATION_JSON)
						.content(
								convertObjectToJson(ServiceTestConfiguration
										.account())))
				.andExpect(status().isCreated()).andDo(print());
	}

	/**
	 * Test the GET to <code>/account</code>.
	 * test retrieval of accounts.
	 * @throws Exception
	 */
	@Test
	public void doGetAccount() throws Exception {
		when(service.findAccount(ServiceTestConfiguration.PROFILE_ID))
				.thenReturn(ServiceTestConfiguration.account());

		mockMvc.perform(
				get("/account/" + ServiceTestConfiguration.PROFILE_ID)
						.contentType(MediaType.APPLICATION_JSON).content(
								convertObjectToJson(ServiceTestConfiguration
										.account())))
				.andExpect(status().isOk())
				.andDo(print())
				.andExpect(
						content().contentTypeCompatibleWith(
								MediaType.APPLICATION_JSON))
				.andExpect(
						jsonPath("$.id").value(
								ServiceTestConfiguration.PROFILE_ID))
				.andExpect(
						jsonPath("$.creationdate").value(
								ServiceTestConfiguration.ACCOUNT_DATE.getTime()))
				.andExpect(
						jsonPath("$.openbalance").value(
								ServiceTestConfiguration.ACCOUNT_OPEN_BALANCE
										.doubleValue()))
				.andExpect(jsonPath("$.logoutcount").value(ServiceTestConfiguration.LOGOUT_COUNT.intValue()))
				.andExpect(
						jsonPath("$.balance").value(
								ServiceTestConfiguration.ACCOUNT_BALANCE))
				.andExpect(
						jsonPath("$.lastlogin").value(
								ServiceTestConfiguration.ACCOUNT_DATE.getTime()))
				.andExpect(
						jsonPath("$.logincount").value(
								ServiceTestConfiguration.LOGIN_COUNT))
				.andDo(print());
	}
	/**
	 * Test the GET to <code>/account/userid/increaseBalance/</code>.
	 * test increase of balance.
	 * @throws Exception
	 */
	@Test
	public void doIncreaseBalance() throws Exception {
		when(service.findAccount(ServiceTestConfiguration.USER_ID))
				.thenReturn(ServiceTestConfiguration.account());

		MvcResult result = mockMvc.perform(
				get("/accounts/" + ServiceTestConfiguration.USER_ID + "/increaseBalance/" + 1000)
						.contentType(MediaType.APPLICATION_JSON).content(
								convertObjectToJson(ServiceTestConfiguration
										.account())))
				.andExpect(status().isOk())
				.andDo(print())
				.andExpect(content().string(String.valueOf(ServiceTestConfiguration.ACCOUNT_BALANCE.doubleValue() + 1000)))
				.andReturn();
		String resultStr = result.getResponse().getContentAsString();
		
	}
	/**
	 * Test the GET to <code>/account/userid/increaseBalance/</code>.
	 * test increase of balance with negative amount.
	 * @throws Exception
	 */
	@Test
	public void doIncreaseBalanceNegative() throws Exception {
		when(service.findAccount(ServiceTestConfiguration.USER_ID))
				.thenReturn(ServiceTestConfiguration.account());

		MvcResult result = mockMvc.perform(
				get("/accounts/" + ServiceTestConfiguration.USER_ID + "/increaseBalance/" + -1000)
						.contentType(MediaType.APPLICATION_JSON).content(
								convertObjectToJson(ServiceTestConfiguration
										.account())))
				.andExpect(status().isExpectationFailed())
				.andDo(print())
				.andExpect(content().string(String.valueOf(ServiceTestConfiguration.ACCOUNT_BALANCE.doubleValue())))
				.andReturn();
		
	}

	/**
	 * Test the GET to <code>/account/userid/decreaseBalance/</code>.
	 * test decrease of balance.
	 * @throws Exception
	 */
	@Test
	public void doDecreaseBalance() throws Exception {
		when(service.findAccount(ServiceTestConfiguration.USER_ID))
				.thenReturn(ServiceTestConfiguration.account());

		mockMvc.perform(
				get("/accounts/" + ServiceTestConfiguration.USER_ID + "/decreaseBalance/" + 10)
						.contentType(MediaType.APPLICATION_JSON).content(
								convertObjectToJson(ServiceTestConfiguration
										.account())))
				.andExpect(status().isOk())
				.andDo(print())
				.andExpect(content().string(String.valueOf(ServiceTestConfiguration.ACCOUNT_BALANCE.doubleValue() - 10)))
				.andDo(print());
	}
	
	/**
	 * Test the GET to <code>/account/userid/decreaseBalance/</code>.
	 * test decrease of balance with not enough funds.
	 * @throws Exception
	 */
	@Test
	public void doDecreaseBalanceNoFunds() throws Exception {
		when(service.findAccount(ServiceTestConfiguration.USER_ID))
				.thenReturn(ServiceTestConfiguration.account());

		mockMvc.perform(
				get("/accounts/" + ServiceTestConfiguration.USER_ID + "/decreaseBalance/" + ServiceTestConfiguration.ACCOUNT_BALANCE.add(BigDecimal.TEN))
						.contentType(MediaType.APPLICATION_JSON).content(
								convertObjectToJson(ServiceTestConfiguration
										.account())))
				.andExpect(status().isExpectationFailed())
				.andDo(print())
				.andExpect(content().string(String.valueOf(ServiceTestConfiguration.ACCOUNT_BALANCE.doubleValue())))
				.andDo(print());
	}
	private byte[] convertObjectToJson(Object request) throws Exception {
		ObjectMapper mapper = new ObjectMapper();
		mapper.setSerializationInclusion(Include.NON_NULL);
		return mapper.writeValueAsBytes(request);
	}
}
