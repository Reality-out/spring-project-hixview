package site.hixview.jpa.service;

import jakarta.persistence.EntityExistsException;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import site.hixview.aggregate.domain.Press;
import site.hixview.aggregate.error.EntityExistsWithNumberException;
import site.hixview.aggregate.error.EntityNotFoundWithNumberException;
import site.hixview.aggregate.service.PressService;
import site.hixview.jpa.entity.PressEntity;
import site.hixview.jpa.mapper.PressEntityMapper;
import site.hixview.jpa.mapper.PressEntityMapperImpl;
import site.hixview.jpa.repository.CompanyArticleEntityRepository;
import site.hixview.jpa.repository.EconomyArticleEntityRepository;
import site.hixview.jpa.repository.IndustryArticleEntityRepository;
import site.hixview.jpa.repository.PressEntityRepository;

import java.util.List;
import java.util.Optional;

import static site.hixview.aggregate.util.ExceptionUtils.getFormattedExceptionMessage;
import static site.hixview.aggregate.vo.ExceptionMessage.ALREADY_EXISTED_ENTITY;
import static site.hixview.aggregate.vo.ExceptionMessage.REMOVE_REFERENCED_ENTITY;
import static site.hixview.aggregate.vo.WordCamel.ENGLISH_NAME;
import static site.hixview.aggregate.vo.WordCamel.NUMBER;
import static site.hixview.jpa.utils.MapperUtils.map;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class PressEntityService implements PressService {

    private final PressEntityRepository pressEntityRepository;
    private final CompanyArticleEntityRepository companyArticleEntityRepository;
    private final IndustryArticleEntityRepository industryArticleEntityRepository;
    private final EconomyArticleEntityRepository economyArticleEntityRepository;
    private final PressEntityMapper mapper = new PressEntityMapperImpl();

    @Override
    public List<Press> getAll() {
        return pressEntityRepository.findAll().stream().map(mapper::toPress).toList();
    }

    @Override
    public Optional<Press> getByNumber(Long number) {
        return getOptionalPress(pressEntityRepository.findByNumber(number).orElse(null));
    }

    @Override
    public Optional<Press> getByKoreanName(String koreanName) {
        return getOptionalPress(pressEntityRepository.findByKoreanName(koreanName).orElse(null));
    }

    @Override
    public Optional<Press> getByEnglishName(String englishName) {
        return getOptionalPress(pressEntityRepository.findByEnglishName(englishName).orElse(null));
    }

    @Override
    @Transactional
    public Press insert(Press press) {
        Long number = press.getNumber();
        String englishName = press.getEnglishName();
        if (pressEntityRepository.existsByNumber(number)) {
            throw new EntityExistsWithNumberException(number, PressEntity.class);
        }
        if (pressEntityRepository.findByEnglishName(englishName).isPresent()) {
            throw new EntityExistsException(getFormattedExceptionMessage(
                    ALREADY_EXISTED_ENTITY, ENGLISH_NAME, englishName, PressEntity.class));
        }
        return mapper.toPress(pressEntityRepository.save(mapper.toPressEntity(press)));
    }

    @Override
    @Transactional
    public Press update(Press press) {
        Long number = press.getNumber();
        String englishName = press.getEnglishName();
        if (!pressEntityRepository.existsByNumber(number)) {
            throw new EntityNotFoundWithNumberException(number, PressEntity.class);
        }
        if (pressEntityRepository.findByEnglishName(englishName).isPresent()) {
            throw new EntityExistsException(getFormattedExceptionMessage(
                    ALREADY_EXISTED_ENTITY, ENGLISH_NAME, englishName, PressEntity.class));
        }
        PressEntity pressEntity = pressEntityRepository.save(
                map(press, pressEntityRepository.findByNumber(number).orElseThrow()));
        propagatePressEntity(pressEntity);
        return mapper.toPress(pressEntity);
    }

    @Override
    @Transactional
    public void removeByNumber(Long number) {
        if (!pressEntityRepository.existsByNumber(number)) {
            throw new EntityNotFoundWithNumberException(number, PressEntity.class);
        }
        PressEntity pressEntity = pressEntityRepository.findByNumber(number).orElseThrow();
        if (!companyArticleEntityRepository.findByPress(pressEntity).isEmpty() ||
                !economyArticleEntityRepository.findByPress(pressEntity).isEmpty() ||
                !industryArticleEntityRepository.findByPress(pressEntity).isEmpty()) {
            throw new DataIntegrityViolationException(getFormattedExceptionMessage(
                    REMOVE_REFERENCED_ENTITY, NUMBER, number, PressEntity.class));
        }
        pressEntityRepository.deleteByNumber(number);
    }

    private Optional<Press> getOptionalPress(PressEntity pressEntity) {
        if (pressEntity == null) {
            return Optional.empty();
        }
        return Optional.of(mapper.toPress(pressEntity));
    }

    private void propagatePressEntity(PressEntity pressEntity) {
        companyArticleEntityRepository.saveAll(
                companyArticleEntityRepository.findByPress(pressEntity).stream().peek(article ->
                    article.updatePress(pressEntity)
                ).toList());
        economyArticleEntityRepository.saveAll(
                economyArticleEntityRepository.findByPress(pressEntity).stream().peek(article ->
                    article.updatePress(pressEntity)
                ).toList());
        industryArticleEntityRepository.saveAll(
                industryArticleEntityRepository.findByPress(pressEntity).stream().peek(article ->
                    article.updatePress(pressEntity)
                ).toList());
    }
}
