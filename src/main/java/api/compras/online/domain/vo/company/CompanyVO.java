package api.compras.online.domain.vo.company;

import api.compras.online.entity.company.Company;
import api.compras.online.entity.customer.Customer;
import api.compras.online.repository.company.CompanyRepository;
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
public class CompanyVO {

    private Long id;
    private String name;
    private String zipCode;
    private String email;
    private String phone;

    private String cnpj;

    public CompanyVO(Company c) {
        if (c == null) return;
        this.id = c.getId();
        this.name = c.getName();
        this.zipCode = c.getZipCode();
        this.email = c.getEMail();
        this.phone = c.getPhone();
        this.cnpj = c.getCnpj();
    }
}
