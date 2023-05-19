package api.compras.online.service.impl;

import api.compras.online.domain.request.company.CompanyCreateAccountRequest;
import api.compras.online.domain.request.company.CompanySearchRequest;
import api.compras.online.domain.response.company.CompanyResponse;
import api.compras.online.domain.vo.company.CompanyVO;
import api.compras.online.entity.company.Company;
import api.compras.online.repository.company.CompanyRepository;
import api.compras.online.service.CompanyService;
import org.junit.platform.commons.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class CompanyServiceImpl implements CompanyService {
    private static final String CREATED_FEEDBACK ="Cadastro realizado com sucesso !!";
    private static final String INTERNAL_ERROR = "Não foi possível realizar esta operação no momento, tente novamente mais tarde.";

    private static final Integer PAGINATION_SIZE = 10;
    @Autowired
    private CompanyRepository repository;

    @Override
    public Object companies(CompanySearchRequest request) throws Exception {
        this.validateCompanySearchRequest(request);
        try {
            Pageable pagination = PageRequest.of(request.getPage(), PAGINATION_SIZE, Sort.by("id"));
            Page<Company> companies = repository.findAll(pagination);
            return transformCompanyToVO(companies);
        }catch (Exception e) {
            e.printStackTrace();
            throw new Exception(INTERNAL_ERROR);
        }
    }

    private List<CompanyVO> transformCompanyToVO(Page<Company> companies) {
        List<CompanyVO> response = new ArrayList<>();
        if(!companies.isEmpty()) {
            for(Company c : companies) {
                response.add(new CompanyVO(c));
            }
        }
        return response;
    }

    private void validateCompanySearchRequest(CompanySearchRequest request) throws Exception {
        if(request.getPage() == null) {
            throw new Exception("Página inexistente");
        }
    }

    private void passCustomerAttributes(CompanyCreateAccountRequest request, Company c) {
        c.setName(request.getName());
        c.setCnpj(request.getCnpj());
        c.setZipCode(request.getZipCode());
        c.setPhone(request.getPhone());
        c.setEMail(request.getEmail());
        c.setRegisterDt(new Date());
        c.setModifyDt(new Date());
    }

    private void validateAccountCreationRequest(CompanyCreateAccountRequest request) throws Exception {
        if(StringUtils.isBlank(request.getName())) {
            throw new Exception("Nome inválido.");
        }
        if(StringUtils.isBlank(request.getCnpj())) {
            throw new Exception("CNPJ inválido.");
        }
        Company c = repository.findByCnpj(request.getCnpj());
        if(c != null){
            throw new Exception("Empresa já cadastrada pelo CNPJ:"+ request.getCnpj());
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

    @Override
    public Object create(CompanyCreateAccountRequest request) throws Exception {
        this.validateAccountCreationRequest(request);
        Company c = new Company();
        try{
            passCustomerAttributes(request, c);
            repository.save(c);
        } catch (Exception e) {
            e.printStackTrace();
            throw new Exception(INTERNAL_ERROR);
        }
        return new CompanyResponse(CREATED_FEEDBACK, null, new CompanyVO(c));
    }
}
