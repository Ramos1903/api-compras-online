package api.compras.online.controller;

import api.compras.online.domain.request.company.CompanyCreateAccountRequest;
import api.compras.online.domain.request.company.CompanySearchRequest;
import api.compras.online.service.CompanyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin("*")
@RequestMapping(path = "/company")
@RestController
public class CompanyController {
    @Autowired
    private CompanyService service;

    @PostMapping("/create")
    public ResponseEntity<Object> create(@RequestBody CompanyCreateAccountRequest request) throws Exception {
        return new ResponseEntity<>(service.create(request),HttpStatus.CREATED);
    }

    @GetMapping("/search")
    public ResponseEntity<Object> companies(CompanySearchRequest request) throws Exception {
        return new ResponseEntity<>(service.companies(request), HttpStatus.OK);
    }
}
