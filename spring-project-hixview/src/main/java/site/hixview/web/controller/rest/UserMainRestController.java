package site.hixview.web.controller.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import site.hixview.domain.entity.company.Company;
import site.hixview.domain.service.CompanyService;

import java.util.HashMap;
import java.util.Optional;

import static site.hixview.domain.vo.Word.*;
import static site.hixview.domain.vo.name.ExceptionName.NOT_EXIST_COMPANY_ERROR;
import static site.hixview.domain.vo.name.ExceptionName.NOT_FOUND_COMPANY_ERROR;
import static site.hixview.domain.vo.user.RequestPath.CHECK_PATH;
import static site.hixview.domain.vo.user.RequestPath.COMPANY_SEARCH_PATH;

@RestController
@RequiredArgsConstructor
public class UserMainRestController {

    @Autowired
    private final CompanyService companyService;

    @SneakyThrows
    @GetMapping(value = {COMPANY_SEARCH_PATH + CHECK, COMPANY_SEARCH_PATH + CHECK_PATH + "{codeOrName}"})
    public ResponseEntity<?> checkCodeOrNameCompanyShow(@PathVariable(name = CODE_OR_NAME, required = false) String codeOrName) {
        ObjectMapper objectMapper = new ObjectMapper();
        if (codeOrName == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(objectMapper.writeValueAsString(new HashMap<>(){{
                put(ERROR, NOT_EXIST_COMPANY_ERROR);
            }}));
        }
        Optional<Company> companyOrEmpty = companyService.findCompanyByCodeOrName(codeOrName);
        if (companyOrEmpty.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(objectMapper.writeValueAsString(new HashMap<>(){{
                put(ERROR, NOT_FOUND_COMPANY_ERROR);
            }}));
        }
        return ResponseEntity.ok().body(objectMapper.writeValueAsString(new HashMap<>(){{
            put(CODE, companyOrEmpty.orElseThrow().getCode());
        }}));
    }
}