package io.pismo.payments;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import io.pismo.payments.domain.OperationsTypes;
import io.pismo.payments.utils.PaymentInputBuilder;
import io.pismo.payments.utils.TransactionInputBuilder;
import io.pismo.payments.utils.UpdateLimitInputBuilder;
import io.pismo.payments.web.UpdateLimitInput;
import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static io.pismo.payments.domain.OperationsTypes.*;
import static org.hamcrest.Matchers.is;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
public class PaymentsApplicationTests {

    private static final String LIMITS_ENDPOINT = "/api/v1/accounts/limits";
    private static final String ACCOUNT_ENDPOINT = "/api/v1/accounts/2";
    private static final String TRANSACTIONS_ENDPOINT = "/api/v1/transactions";
    private static final String PAYMENTS_ENDPOINT = "/api/v1/payments";

    private MockMvc mockMvc;

    private ObjectWriter objectWriter = new ObjectMapper().writer().withDefaultPrettyPrinter();

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Before
    public void
    setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }

    @Test
    public void contextLoads() {
    }

    @Test
    public void should_return_status_200_and_response_body_with_all_accounts_when_get_account_limits() throws Exception {
        mockMvc.perform(get(LIMITS_ENDPOINT))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.limits[0].accountId", is(1)))
                .andExpect(jsonPath("$.limits[0].availableCreditLimit", is(1000d)))
                .andExpect(jsonPath("$.limits[0].availableWithdrawalLimit", is(300d)));
    }

    @Test
    public void should_return_status_204_when_patch_account_and_get_updated_values_when_get_limits() throws Exception {
        String requestBody = buildJson(new UpdateLimitInputBuilder()
                                                .withAvailableCreditLimit(-100d)
                                                .withAvailableWithdrawalLimit(-50d)
                                                .build());

        mockMvc.perform(patch(ACCOUNT_ENDPOINT).contentType(APPLICATION_JSON).content(requestBody))
                .andExpect(status().isNoContent());

        mockMvc.perform(get(LIMITS_ENDPOINT))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.limits[1].accountId", is(2)))
                .andExpect(jsonPath("$.limits[1].availableCreditLimit", is(900d)))
                .andExpect(jsonPath("$.limits[1].availableWithdrawalLimit", is(250d)));
    }

    @Test
    public void should_return_status_201_when_post_transaction_and_200_with_updated_limits_afert_when_gets_limits() throws Exception {
        String requestBody = buildJson(new TransactionInputBuilder()
                                .withAccountId(3)
                                .withAmount(100d)
                                .withOperationTypeId(SAQUE)
                                .build());

        mockMvc.perform(post(TRANSACTIONS_ENDPOINT).contentType(APPLICATION_JSON).content(requestBody))
                .andExpect(status().isCreated());

        mockMvc.perform(get(LIMITS_ENDPOINT))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.limits[2].accountId", is(3)))
                .andExpect(jsonPath("$.limits[2].availableCreditLimit", is(900d)))
                .andExpect(jsonPath("$.limits[2].availableWithdrawalLimit", is(200d)));
    }

    @Test
    public void should_return_status_201_when_post_transaction_and_payment_and_200_with_updated_limits_afert_when_gets_limits() throws Exception {
        String requestBody = buildJson(new TransactionInputBuilder()
                .withAccountId(4)
                .withAmount(100d)
                .withOperationTypeId(SAQUE)
                .build());

        mockMvc.perform(post(TRANSACTIONS_ENDPOINT).contentType(APPLICATION_JSON).content(requestBody))
                .andExpect(status().isCreated());

        requestBody = buildJson(new PaymentInputBuilder()
                .withAccountId(4)
                .withAmount(50d)
                .buildList());

        mockMvc.perform(post(PAYMENTS_ENDPOINT).contentType(APPLICATION_JSON).content(requestBody))
                .andExpect(status().isCreated());

        mockMvc.perform(get(LIMITS_ENDPOINT))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.limits[3].accountId", is(4)))
                .andExpect(jsonPath("$.limits[3].availableCreditLimit", is(950d)))
                .andExpect(jsonPath("$.limits[3].availableWithdrawalLimit", is(250d)));
    }

    @Test
    public void should_return_status_400_when_post_transaction_with_value_greater_then_available_credit_limit() throws Exception {
        String requestBody = buildJson(new TransactionInputBuilder()
                .withAccountId(3)
                .withAmount(1500d)
                .withOperationTypeId(COMPRA_A_VISTA)
                .build());

        mockMvc.perform(post(TRANSACTIONS_ENDPOINT).contentType(APPLICATION_JSON).content(requestBody))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void should_return_status_400_when_post_transaction_with_value_greater_then_available_withdrawal_limit() throws Exception {
        String requestBody = buildJson(new TransactionInputBuilder()
                .withAccountId(3)
                .withAmount(500d)
                .withOperationTypeId(SAQUE)
                .build());

        mockMvc.perform(post(TRANSACTIONS_ENDPOINT).contentType(APPLICATION_JSON).content(requestBody))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void should_return_status_404_when_post_transaction_with_nonexixtent_account() throws Exception {
        String requestBody = buildJson(new TransactionInputBuilder()
                .withAccountId(13)
                .withAmount(500d)
                .withOperationTypeId(COMPRA_PARCELADA)
                .build());

        mockMvc.perform(post(TRANSACTIONS_ENDPOINT).contentType(APPLICATION_JSON).content(requestBody))
                .andExpect(status().isNotFound());
    }

    private String buildJson(Object o) throws JsonProcessingException {
        return objectWriter.writeValueAsString(o).replaceAll("\n", "");
    }

}
