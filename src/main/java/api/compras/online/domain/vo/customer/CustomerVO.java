package api.compras.online.domain.vo.customer;

import api.compras.online.entity.customer.Customer;
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
public class CustomerVO {

    private Long id;
    private String name;
    private String zipCode;
    private String email;
    private String phone;

    private String cpf;
    private Date birthDate;

    public CustomerVO(Customer c) {
        if (c == null) return;
        this.id = c.getId();
        this.name = c.getName();
        this.zipCode = c.getZipCode();
        this.email = c.getEMail();
        this.phone = c.getPhone();
        this.cpf = c.getCpf();
        this.birthDate = c.getBirthDate();
    }
}
