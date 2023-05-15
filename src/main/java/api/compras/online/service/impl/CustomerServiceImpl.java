package api.compras.online.service.impl;
import api.compras.online.domain.request.customer.*;
import api.compras.online.domain.response.customer.CustomerResponse;
import api.compras.online.domain.vo.customer.CustomerCardVO;
import api.compras.online.domain.vo.customer.CustomerTransactionVO;
import api.compras.online.domain.vo.customer.CustomerVO;
import api.compras.online.entity.company.Company;
import api.compras.online.entity.customer.Customer;
import api.compras.online.entity.customer.CustomerCard;
import api.compras.online.entity.customer.CustomerTransaction;
import api.compras.online.repository.company.CompanyRepository;
import api.compras.online.repository.customer.CustomerCardRepository;
import api.compras.online.repository.customer.CustomerRepository;
import api.compras.online.repository.customer.CustomerTransactionRepository;
import api.compras.online.service.CustomerService;
import org.junit.platform.commons.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.*;

@Service
public class CustomerServiceImpl implements CustomerService {
    private static final String CREATED_CUSTOMER_FEEDBACK ="Cliente Cadastrado com Sucesso!!";
    private static final String INTERNAL_ERROR = "Não foi possível realizar esta operação no momento, tente novamente mais tarde.";

    private static final Integer PAGINATION_SIZE = 10;
    @Autowired
    private CustomerRepository repository;

    @Autowired
    private CustomerTransactionRepository transactionRepository;

    @Autowired
    private CompanyRepository companyRepository;

    @Autowired
    private CustomerCardRepository cardRepository;

    public CustomerResponse create(CustomerCreateAccountRequest request) throws Exception {
        validateAccountCreationRequest(request);
        Customer customer = new Customer();
        try{
            passCustomerAttributes(request, customer);
            repository.save(customer);
        } catch (Exception e) {
            e.printStackTrace();
            throw new Exception(INTERNAL_ERROR);
        }
        return new CustomerResponse(CREATED_CUSTOMER_FEEDBACK, null,new CustomerVO(customer), null);
    }

    private void passCustomerAttributes(CustomerCreateAccountRequest request, Customer c) {
        c.setName(request.getName());
        c.setCpf(request.getCpf());
        c.setZipCode(request.getZipCode());
        c.setPhone(request.getPhone());
        c.setEMail(request.getEmail());
        c.setBirthDate(request.getBirthDate());
        c.setCards(new ArrayList<>());
        c.setTransactions(new ArrayList<>());
        c.setRegisterDt(new Date());
        c.setModifyDt(new Date());
    }

    private void validateAccountCreationRequest(CustomerCreateAccountRequest request) throws Exception {
        if(StringUtils.isBlank(request.getName())) {
            throw new Exception("Nome inválido.");
        }
        if(StringUtils.isBlank(request.getCpf())) {
            throw new Exception("CPF inválido.");
        }
        if(request.getBirthDate() == null) {
            throw new Exception("Data de aniversário inválida.");
        }
        if(StringUtils.isBlank(request.getZipCode())) {
            throw new Exception("Código postal inválido.");
        }
        if(StringUtils.isBlank(request.getPhone())) {
            throw new Exception("Celular inválido.");
        }
        if(StringUtils.isBlank(request.getEmail())) {
            throw new Exception("E-mail inválido.");
        }
    }

    private void passTransactionData(CustomerTransactionVO request, CustomerTransaction customerTransaction) throws Exception {
        customerTransaction.setCustomer(findCustomerByCpf(request.getCustomer().getCpf()));
        customerTransaction.setCompany(findCompanyByCnpj(request.getCompany().getCnpj()));
        customerTransaction.setDate(new Date());
        customerTransaction.setAmount(request.getAmount());
        customerTransaction.setIsCredit(request.getIsCredit());
        customerTransaction.setCustomerEncriptedCard(request.getCustomerEncriptedCard());
        customerTransaction.setIsSuccessfully(request.getIsSuccessfully());
    }

    private Customer findCustomerByCpf(String cpf) throws Exception {
        try {
            return repository.findByCpf(cpf);

        } catch (Exception e){
            throw new Exception("Usuário não cadastrado. ");
        }
    }

    private Company findCompanyByCnpj(String cnpj) throws Exception {
        try {
           return companyRepository.findByCnpj(cnpj);

        } catch (Exception e){
            throw new Exception("Empresa não cadastrada. ");
        }
    }

    public List<CustomerVO> customers(CustomerSearchRequest request) throws Exception {
        validateCustomerSearchRequest(request);
        try {
            Pageable pagination = PageRequest.of(request.getPage(), PAGINATION_SIZE, Sort.by("name"));
            Page<Customer> customers = repository.findAll(pagination);
            return transformCustomerToVO(customers);
        }catch (Exception e) {
            e.printStackTrace();
            throw new Exception(INTERNAL_ERROR);
        }
    }

    @Override
    public Object encriptCard(CustomerCryptCardRequest request) {
        return null;
    }

    @Override
    public Object dencriptCard(CustomerCryptCardRequest request) {
        return null;
    }

    @Override
    public Object cards(CustomerCardSearchRequest request) throws Exception {
        this.validateCustomerCardRequest(request);
        try {
            Customer customer = repository.findByCpf(request.getCpf());
            Pageable filteredPagination = PageRequest.of(request.getPage(), PAGINATION_SIZE, Sort.by("date"));
            Page<CustomerCard> cards = cardRepository.getAllByCustomer(customer, filteredPagination);
            return transformCardToVO(cards);
        } catch (Exception e) {
            e.printStackTrace();
            throw new Exception(INTERNAL_ERROR);
        }
    }

    private List<CustomerCardVO> transformCardToVO(Page<CustomerCard> cards) {
        List<CustomerCardVO> response = new ArrayList<>();
        if(!cards.isEmpty()) {
            for(CustomerCard c : cards) {
                response.add(new CustomerCardVO(c));
            }
        }
        return response;
    }

    private void validateCustomerCardRequest(CustomerCardSearchRequest request) throws Exception {
        if(StringUtils.isBlank(request.getCpf())) {
            throw new Exception("CPF inválido.");
        }
        if(request.getPage() == null) {
            throw new Exception("Página inexistente");
        }
    }

    private void validateCustomerSearchRequest(CustomerSearchRequest request) throws Exception {
        if(request.getPage() == null) {
            throw new Exception("Página inexistente");
        }
    }

    private List<CustomerVO> transformCustomerToVO(Page<Customer> customers) {
        List<CustomerVO> response = new ArrayList<>();
        if(!customers.isEmpty()) {
            for(Customer c : customers) {
                response.add(new CustomerVO(c));
            }
        }
        return response;
    }

    public List<CustomerTransactionVO> transactionsByDate(CustomerTransactionSearchRequest request) throws Exception {
        validateCustomerTransactionRequest(request);
        try {
            Customer customer = repository.findByCpf(request.getCpf());
            Pageable filteredPagination = PageRequest.of(request.getPage(), PAGINATION_SIZE, Sort.by("date"));
            Page<CustomerTransaction> transactions = transactionRepository.getAllBetweenDates(convertUnixToDate(request.getInitialDate()), convertUnixToDate(request.getFinalDate()),
                    customer, filteredPagination);
            return transformTransactionToVO(transactions);
        } catch (Exception e) {
            e.printStackTrace();
            throw new Exception(INTERNAL_ERROR);
        }
    }

    @Override
    public Object transaction(CustomerTransactionVO request) throws Exception {
        validateTransactionRequest(request);
        try {
            CustomerTransaction transaction = new CustomerTransaction();
            passTransactionData(request,transaction);
            transactionRepository.save(transaction);
            return new CustomerResponse("Transação cadastrada com sucesso!", null,null,null);
        } catch (Exception e) {
            e.printStackTrace();
            throw new Exception(INTERNAL_ERROR);
        }
    }

    private void validateTransactionRequest(CustomerTransactionVO request) throws Exception {
        if(request.getCustomer() == null) {
            throw new Exception("Cliente inválido.");
        }
        if(request.getCustomer().getCpf() == null) {
            throw new Exception("CPF inválido.");
        }
        if(request.getCompany() == null) {
            throw new Exception("Empresa inválida.");
        }
        if(request.getCompany().getCnpj() == null) {
            throw new Exception("CNPJ inválido.");
        }
        if(request.getCustomerEncriptedCard() == null) {
            throw new Exception("Cartão inválidoj.");
        }
    }

    private Date convertUnixToDate(long longUnix) {
        try {
            Instant instant = Instant.ofEpochSecond( longUnix );
            return Date.from( instant );
        }catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private void validateCustomerTransactionRequest(CustomerTransactionSearchRequest request) throws Exception {
        if(request.getPage() == null) {
            throw new Exception("Página inexistente.");
        }
        if(request.getCpf() == null) {
            throw new Exception("CPF inválido.");
        }
        if(Objects.isNull(request.getInitialDate())) {
            throw new Exception("Data de início inválida.");
        }
        if(Objects.isNull(request.getFinalDate())) {
            throw new Exception("Data final inválida.");
        }
    }

    private List<CustomerTransactionVO> transformTransactionToVO(Page<CustomerTransaction> transactions) {
        List<CustomerTransactionVO> response = new ArrayList<>();
        if(!transactions.isEmpty()) {
            for(CustomerTransaction t : transactions) {
                response.add(new CustomerTransactionVO(t));
            }
        }
        return response;
    }
}
