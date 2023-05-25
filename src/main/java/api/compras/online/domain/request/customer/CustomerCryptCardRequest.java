package api.compras.online.domain.request.customer;

import api.compras.online.domain.vo.customer.CustomerVO;
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
public class CustomerCryptCardRequest {

    private CustomerVO customer;
    private String encriptedCard;
    private String privateKey;
    private String publicKey;

}
