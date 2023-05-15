package api.compras.online.domain.request.customer;

import api.compras.online.domain.vo.customer.CustomerVO;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_EMPTY)
@JsonIgnoreProperties(ignoreUnknown = true)
public class CustomerCreateCardRequest {

    private CustomerVO customer;
    private String fullName;
    private String number;
    private String validThru;
    private String secCode;
    private String flagName;
    private Boolean isCredit;
}
