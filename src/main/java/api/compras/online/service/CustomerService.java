package api.compras.online.service;

import api.compras.online.domain.request.customer.*;
import api.compras.online.domain.vo.customer.CustomerTransactionVO;
import org.springframework.stereotype.Service;

@Service
public interface CustomerService {

    public Object create(CustomerCreateAccountRequest request) throws Exception;
    public Object customers(CustomerSearchRequest request) throws Exception;
    public Object encriptCard(CustomerCreateCardRequest request) throws Exception;
    public Object deCriptCard(CustomerCryptCardRequest request) throws Exception;
    public Object cards(CustomerCardSearchRequest request) throws Exception;
    public Object transactionsByDate(CustomerTransactionSearchRequest request) throws Exception;
    public Object transaction(CustomerTransactionVO request) throws Exception;
}
