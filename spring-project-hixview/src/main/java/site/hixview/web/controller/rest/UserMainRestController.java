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
import site.hixview.domain.service.CompanyService;

import java.util.HashMap;

import static site.hixview.domain.vo.Word.*;
import static site.hixview.domain.vo.name.ExceptionName.NOT_EXIST_COMPANY_ERROR;
import static site.hixview.domain.vo.name.ExceptionName.NOT_FOUND_COMPANY_ERROR;
import static site.hixview.domain.vo.user.RequestUrl.CHECK_URL;
import static site.hixview.domain.vo.user.RequestUrl.COMPANY_SEARCH_URL;

@RestController
@RequiredArgsConstructor
public class UserMainRestController {

    @Autowired
    private final CompanyService companyService;

    @SneakyThrows
    @GetMapping(value = {COMPANY_SEARCH_URL + CHECK, COMPANY_SEARCH_URL + CHECK_URL + "{codeOrName}"})
    public ResponseEntity<?> checkCodeOrNameCompanyShow(@PathVariable(name = "codeOrName", required = false) String codeOrName) {
        ObjectMapper objectMapper = new ObjectMapper();
        if (codeOrName == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(objectMapper.writeValueAsString(new HashMap<>(){{
                put(ERROR, NOT_EXIST_COMPANY_ERROR);
            }}));
        }
        if (companyService.findCompanyByCodeOrName(codeOrName).isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(objectMapper.writeValueAsString(new HashMap<>(){{
                put(ERROR, NOT_FOUND_COMPANY_ERROR);
            }}));
        }
        return ResponseEntity.ok().body(objectMapper.writeValueAsString(new HashMap<>(){{
            put(CODE, companyService.findCompanyByCodeOrName(codeOrName).orElseThrow().getCode());
        }}));
    }
}