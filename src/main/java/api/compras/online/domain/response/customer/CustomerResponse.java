package api.compras.online.domain.response.customer;

import api.compras.online.domain.vo.customer.CustomerCardVO;
import api.compras.online.domain.vo.customer.CustomerVO;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_EMPTY)
@JsonIgnoreProperties(ignoreUnknown = true)
public class CustomerResponse {

    private String feedback;
    private String message;
    private CustomerVO customer;
    private List<CustomerCardVO> card;
}
