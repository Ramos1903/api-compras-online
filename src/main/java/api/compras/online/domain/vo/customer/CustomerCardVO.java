package api.compras.online.domain.vo.customer;

import api.compras.online.entity.customer.CustomerCard;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_EMPTY)
@JsonIgnoreProperties(ignoreUnknown = true)
public class CustomerCardVO {

    private Long id;
    private String encriptedCardJson;
   // private CustomerVO customer;

    public CustomerCardVO(CustomerCard c) {
        if (c == null) return;
        this.id = c.getId();
        this.encriptedCardJson = c.getEncriptedCardJson();
        // this.customer = new CustomerVO(c.getCustomer());
    }
}
