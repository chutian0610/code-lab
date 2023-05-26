package info.victorchu.webflux.quickstart.r2dbc;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/customer")
public class CustomerController {

    @Autowired
    private CustomTransactionalService service;

    @GetMapping("/all/{page}")
    public Mono<Page<Customer>> getAll(@PathVariable("page") int page, @RequestParam("size") int size){
        if(page>0){
            page = page-1;
        }
        return this.service.findByPage(PageRequest.of(page, size));
    }
}
