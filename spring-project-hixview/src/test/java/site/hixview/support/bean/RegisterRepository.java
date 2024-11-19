package site.hixview.support.bean;

import org.springframework.context.annotation.Import;
import site.hixview.support.scan.ScanRepository;

@Import(ScanRepository.class)
public @interface RegisterRepository {
}
