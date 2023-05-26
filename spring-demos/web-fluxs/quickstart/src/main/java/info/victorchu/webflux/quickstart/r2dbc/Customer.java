package info.victorchu.webflux.quickstart.r2dbc;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Customer {
    @Id
    @EqualsAndHashCode.Exclude
    private Long id;

    public boolean hasId() {
        return id != null;
    }


    private String firstName;

    private String lastName;

    public Customer withId(long id) {
        return new Customer(id, firstName, lastName);
    }

}
