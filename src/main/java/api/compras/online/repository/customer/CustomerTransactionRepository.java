package api.compras.online.repository.customer;

import api.compras.online.entity.customer.Customer;
import api.compras.online.entity.customer.CustomerTransaction;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

import java.util.Date;

public interface CustomerTransactionRepository extends PagingAndSortingRepository<CustomerTransaction, Long> {

    @Query(value = "from CustomerTransaction ct where ct.customer =:customer and ct.date BETWEEN :startDate AND :endDate")
    Page<CustomerTransaction> getAllBetweenDates(@Param("startDate") Date startDate, @Param("endDate") Date endDate, @Param("customer") Customer customer, Pageable pageable);
}