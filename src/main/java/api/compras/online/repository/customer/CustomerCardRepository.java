package api.compras.online.repository.customer;

import api.compras.online.entity.customer.Customer;
import api.compras.online.entity.customer.CustomerCard;
import api.compras.online.entity.customer.CustomerTransaction;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

import java.util.Date;

public interface CustomerCardRepository extends PagingAndSortingRepository<CustomerCard, Long> {

    @Query(value = "from CustomerCard cc where cc.customer =:customer")
    Page<CustomerCard> getAllByCustomer(@Param("customer") Customer customer, Pageable pageable);

    CustomerCard findByEncriptedCardJsonAndCustomer_Cpf(String cpf, String encriptedCardJson);
}
