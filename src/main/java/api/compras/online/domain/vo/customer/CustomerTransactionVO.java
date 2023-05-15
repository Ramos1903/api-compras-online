package api.compras.online.domain.vo.customer;

import api.compras.online.domain.vo.company.CompanyVO;
import api.compras.online.entity.customer.CustomerTransaction;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_EMPTY)
@JsonIgnoreProperties(ignoreUnknown = true)
public class CustomerTransactionVO {

    private Long id;
    private Date date;
    private Double amount;
    private Boolean isCredit;
    private Boolean isSuccessfully;
    private String customerEncriptedCard;
    private CustomerVO customer;
    private CompanyVO company;

    public CustomerTransactionVO(CustomerTransaction t) {
        if (t == null) return;
        this.id = t.getId();
        this.date = t.getDate();
        this.amount = t.getAmount();
        this.isCredit = t.getIsCredit();
        this.customerEncriptedCard = t.getCustomerEncriptedCard();
        this.isSuccessfully = t.getIsSuccessfully();
        this.customer = new CustomerVO(t.getCustomer());
        this.company = new CompanyVO(t.getCompany());
    }
}
