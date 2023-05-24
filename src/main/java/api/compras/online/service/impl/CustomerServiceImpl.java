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
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.platform.commons.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.security.*;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.persistence.EntityManager;

import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.time.Instant;
import java.util.*;

import static org.apache.catalina.manager.Constants.CHARSET;


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
    private EntityManager entityManager;

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
        Customer c = repository.findByCpf(request.getCpf());
        if(c != null){
            throw new Exception("Cleinte já cadastrado pelo CPF:"+ request.getCpf());
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
            Pageable pagination = PageRequest.of(request.getPage(), PAGINATION_SIZE, Sort.by("id"));
            Page<Customer> customers = repository.findAll(pagination);
            return transformCustomerToVO(customers);
        }catch (Exception e) {
            e.printStackTrace();
            throw new Exception(INTERNAL_ERROR);
        }
    }

    @Override
    public CustomerCardVO encriptCard(CustomerCreateCardRequest request) throws Exception {
        this.validateCustomerCreateCardRequest(request);
        try {
            Customer customer = findCustomerByCpf(request.getCustomer().getCpf());

            if(customer.getCards().isEmpty()) {
                customer.setCards(new ArrayList<>());
            }

            request.setCustomer(null);
            KeyPair keyPair = this.createKeyPair();
            String encriptedCardRequestString = encryptHashMessage(keyPair, request);

            CustomerCard card = new CustomerCard();

            Optional<CustomerCard> existingCar;
            existingCar = customer.getCards().stream().filter(i -> i.getEncriptedCardJson().equals(encriptedCardRequestString)).findFirst();
            if(existingCar.isPresent()) {
                card = existingCar.get();
            }

            card.setCustomer(customer);
            card.setEncriptedCardJson(encriptedCardRequestString);
            card.setPrivateKey(savePrivateKey(keyPair.getPrivate()));
            card.setPublicKey(savePublicKey(keyPair.getPublic()));
            card.setIsActive(Boolean.TRUE);
            card.setModifyDt(new Date());
            card.setRegisterDt(new Date());

            customer.getCards().add(card);
            repository.save(customer);
            return new CustomerCardVO(card);

        } catch (Exception e) {
            e.printStackTrace();
            throw new Exception(INTERNAL_ERROR);
        }
    }

    public static String savePrivateKey(PrivateKey privateKey) {
        byte[] privateKeyBytes = privateKey.getEncoded();
        return Base64.getEncoder().encodeToString(privateKeyBytes);
    }

    public static String savePublicKey(PublicKey publicKey) {
        byte[] publicKeyBytes = publicKey.getEncoded();
        return Base64.getEncoder().encodeToString(publicKeyBytes);
    }

    private String decryptHashMessage(String privateKey, String encryptedMessage) throws Exception {
        byte[] privateKeyBytes = Base64.getDecoder().decode(privateKey);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        PrivateKey privateKeyObj = keyFactory.generatePrivate(new PKCS8EncodedKeySpec(privateKeyBytes));

        Cipher cipher = Cipher.getInstance("RSA/ECB/OAEPWithSHA-256AndMGF1Padding");
        cipher.init(Cipher.DECRYPT_MODE, privateKeyObj);

        byte[] encryptedBytes = Base64.getDecoder().decode(encryptedMessage);
        byte[] decryptedBytes = cipher.doFinal(encryptedBytes);

        return new String(decryptedBytes, StandardCharsets.UTF_8);
    }

    private PublicKey publicKeyFromBase64String(String publicKeyString) throws Exception {
        byte[] publicKeyBytes = Base64.getDecoder().decode(publicKeyString);
        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(publicKeyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        return keyFactory.generatePublic(keySpec);
    }

    private String encryptHashMessage(KeyPair keyPair, CustomerCreateCardRequest request) throws NoSuchPaddingException, NoSuchAlgorithmException, IllegalBlockSizeException, BadPaddingException, InvalidKeyException {
        Cipher cipher = Cipher.getInstance("RSA/ECB/OAEPWithSHA-256AndMGF1Padding");
        cipher.init(Cipher.ENCRYPT_MODE, keyPair.getPublic());

        byte[] encryptedBytes = cipher.doFinal(request.toString().getBytes(StandardCharsets.UTF_8));
        String encryptedBase64 = Base64.getEncoder().encodeToString(encryptedBytes);

        return encryptedBase64;
    }

    private KeyPair createKeyPair() throws NoSuchAlgorithmException {
        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
        keyPairGenerator.initialize(2048);
        return keyPairGenerator.generateKeyPair();
    }

    private void validateCustomerCreateCardRequest(CustomerCreateCardRequest request) throws Exception {
        if(request == null) {
            throw new Exception("Dados inválidos.");
        }
        if(request.getCustomer() == null) {
            throw new Exception("Dados do cliente inválidos.");
        }
        if(request.getCustomer().getCpf() == null) {
            throw new Exception("Dados do cliente inválidos.");
        }
        if(request.getNumber() == null) {
            throw new Exception("Número do cartão inválido.");
        }
        if(request.getValidThru() == null) {
            throw new Exception("Validade do cartão inválida.");
        }
        if(request.getFlagName() == null) {
            throw new Exception("Bandeira do cartão inválida.");
        }
        if(request.getFullName() == null) {
            throw new Exception("Nome do cartão inválido.");
        }
        if(request.getSecCode() == null) {
            throw new Exception("Cód. de segurança do cartão inválido.");
        }
        if(request.getIsCredit() == null) {
            throw new Exception("Tipo de PGTO. do cartão inválido.");
        }
    }

    @Override
    public Object deCriptCard(CustomerCryptCardRequest request) throws Exception {

        validateDecriptRequest(request);
        Customer customer = repository.findByCpf(request.getCustomer().getCpf());
        if(customer == null) {
            throw new Exception("Cliente não encontrado.");
        }

        CustomerCard card = cardRepository.findByEncriptedCardJson(request.getEncriptedCard());
        if(card == null) {
            throw new Exception("Cartão não encontrado");
        }
        return this.deCriptedCard(card, new CustomerVO(customer));
    }

    private Object deCriptedCard(CustomerCard card, CustomerVO customerVO) throws Exception {

        String decript = this.decryptHashMessage(card.getPrivateKey(), card.getEncriptedCardJson());
        return decript;
//TODO encript card message as JSON not as entity class
//        ObjectMapper objectMapper = new ObjectMapper();
//        CustomerCreateCardRequest cardData = objectMapper.readValue(decript, CustomerCreateCardRequest.class);
//
//        CustomerCardVO cardVO = new CustomerCardVO();
//        cardVO.setCustomer(customerVO);
//        cardVO.setFullName(cardData.getFullName());
//        cardVO.setFlagName(cardData.getFlagName());
//        cardVO.setNumber(cardData.getNumber());
//        cardVO.setIsCredit(cardData.getIsCredit());
//        cardVO.setSecCode(cardData.getSecCode());
//        cardVO.setValidThru(cardData.getValidThru());
//
//        return cardVO;
    }

    private void validateDecriptRequest(CustomerCryptCardRequest request) throws Exception {
        if(request == null) {
            throw new Exception("Request inválido.");
        }
        if(request.getCustomer() == null) {
            throw new Exception("Cliente inválido.");
        }
        if(request.getCustomer().getCpf() == null) {
            throw new Exception("CPF inválido.");
        }
        if(request.getEncriptedCard() == null) {
            throw new Exception("Cartão inválido.");
        }
    }

    @Override
    public Object cards(CustomerCardSearchRequest request) throws Exception {
        this.validateCustomerCardRequest(request);
        try {
            Customer customer = repository.findByCpf(request.getCpf());
            Pageable filteredPagination = PageRequest.of(request.getPage(), PAGINATION_SIZE, Sort.by("id"));
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
