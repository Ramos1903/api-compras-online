package api.compras.online.domain.vo.customer;

import api.compras.online.domain.request.customer.CustomerCreateCardRequest;
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
    private String privateKey;
    private String publicKey;

    private CustomerVO customer;
    private String fullName;
    private String number;
    private String validThru;
    private String secCode;
    private String flagName;
    private Boolean isCredit;

    public CustomerCardVO(CustomerCard c) {
        if (c == null) return;
       // this.id = c.getId();
        this.encriptedCardJson = c.getEncriptedCardJson();
        this.privateKey = c.getPrivateKey();
        this.publicKey = c.getPublicKey();
        // this.customer = new CustomerVO(c.getCustomer());
    }
}
