package api.compras.online.service;
import api.compras.online.domain.request.company.CompanyCreateAccountRequest;
import api.compras.online.domain.request.company.CompanySearchRequest;
import api.compras.online.domain.request.customer.CustomerCreateAccountRequest;
import api.compras.online.domain.request.customer.CustomerSearchRequest;
import org.springframework.stereotype.Service;

@Service
public interface CompanyService {

    Object companies(CompanySearchRequest request) throws Exception;

    Object create(CompanyCreateAccountRequest request) throws Exception;
}
