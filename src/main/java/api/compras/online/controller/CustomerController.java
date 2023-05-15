package api.compras.online.controller;

import api.compras.online.domain.request.customer.*;
import api.compras.online.domain.vo.customer.CustomerTransactionVO;
import api.compras.online.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin("*")
@RequestMapping(path = "/customer")
@RestController
public class CustomerController {

    @Autowired
    private CustomerService customerService;

    @PostMapping("/create")
    public ResponseEntity<Object> createCustomer(@RequestBody CustomerCreateAccountRequest request) throws Exception {
        return new ResponseEntity<>(customerService.create(request),HttpStatus.CREATED);
    }

    @PutMapping("/card/encript")
    public ResponseEntity<Object> encript(@RequestBody CustomerCryptCardRequest request) throws Exception {
        return new ResponseEntity<>(customerService.encriptCard(request), HttpStatus.OK);
    }

    @GetMapping("/card/deCript")
    public ResponseEntity<Object> decript(@RequestBody CustomerCryptCardRequest request) throws Exception {
        return new ResponseEntity<>(customerService.dencriptCard(request), HttpStatus.OK);
    }

    @GetMapping("/search")
    public ResponseEntity<Object> customers(CustomerSearchRequest request) throws Exception {
        return new ResponseEntity<>(customerService.customers(request), HttpStatus.OK);
    }

    @GetMapping("/cards")
    public ResponseEntity<Object> cards(CustomerCardSearchRequest request) throws Exception {
        return new ResponseEntity<>(customerService.cards(request), HttpStatus.OK);
    }

    @GetMapping("/transactions")
    public ResponseEntity<Object> transactionsByDate(CustomerTransactionSearchRequest request) throws Exception {
        return new ResponseEntity<>(customerService.transactionsByDate(request), HttpStatus.OK);
    }

    @GetMapping("/transactions/save")
    public ResponseEntity<Object> transaction(CustomerTransactionVO request) throws Exception {
        return new ResponseEntity<>(customerService.transaction(request), HttpStatus.OK);
    }
}
