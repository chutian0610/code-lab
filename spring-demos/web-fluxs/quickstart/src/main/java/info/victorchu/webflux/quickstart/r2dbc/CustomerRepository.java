package info.victorchu.webflux.quickstart.r2dbc;

import org.springframework.data.domain.Pageable;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.query.ReactiveQueryByExampleExecutor;
import org.springframework.data.repository.reactive.ReactiveSortingRepository;
import reactor.core.publisher.Flux;

public interface CustomerRepository extends
        ReactiveQueryByExampleExecutor<Customer> , // 查询 Example
        ReactiveSortingRepository<Customer,Long>  // 分页
{

    @Query("select id, first_name, last_name from customer c where c.last_name = :lastName")
    Flux<Customer> findByLastname(String lastName);

    Flux<Customer> findAllBy(Pageable pageable);
}