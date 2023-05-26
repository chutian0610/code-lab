package info.victorchu.webflux.quickstart.client;

import info.victorchu.webflux.quickstart.r2dbc.CustomTransactionalService;
import info.victorchu.webflux.quickstart.r2dbc.Customer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import reactor.test.StepVerifier;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@SpringBootTest
public class CustomServiceTest {
    @Autowired
    CustomTransactionalService service;


    @Test
    void findCustom() {
        Customer customer = new Customer(null,"David", "Palmer");
        service.findByLastName("Palmer").collectList()
                .log()
                .as(StepVerifier::create)
                .consumeNextWith(actual -> assertThat(actual).containsExactlyInAnyOrder(customer))
                .verifyComplete();
    }

    @Test
    void searchCustom() {
        Customer customer = new Customer(null,"David", "Palmer");
        service.searchByLastName("Palmer").collectList()
                .log()
                .as(StepVerifier::create)
                .consumeNextWith(actual -> assertThat(actual).containsExactlyInAnyOrder(customer))
                .verifyComplete();
    }

    @Test
    void exceptionTriggersRollback() {

        service.save(new Customer(null, "Dave", "Matthews")) //
                .as(StepVerifier::create)
                .expectError() // Error because of the exception triggered within the service
                .verify();

        // No data inserted due to rollback
        service.findByLastName("Matthews")
                .as(StepVerifier::create)
                .verifyComplete();
    }

    @Test
    void insertsDataTransactionally() {

        service.save(new Customer(null, "Carter", "Beauford")) //
                .as(StepVerifier::create)
                .expectNextMatches(Customer::hasId)
                .verifyComplete();

        // Data inserted due to commit
        service.findByLastName("Beauford")
                .as(StepVerifier::create)
                .expectNextMatches(Customer::hasId)
                .verifyComplete();
    }
}
