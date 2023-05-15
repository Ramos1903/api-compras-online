package api.compras.online.repository.company;

import api.compras.online.entity.company.Company;
import api.compras.online.entity.customer.Customer;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

public interface CompanyRepository extends PagingAndSortingRepository<Company, Long>, JpaSpecificationExecutor<Company> {

    Company findByName(@Param("name") String name);

    Company findByCnpj(@Param("cnpj") String cnpj);
}
