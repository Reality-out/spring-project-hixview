package site.hixview.jpa.service;

import jakarta.persistence.EntityExistsException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import site.hixview.aggregate.domain.Press;
import site.hixview.aggregate.error.EntityNotFoundWithNumberException;
import site.hixview.aggregate.service.PressService;
import site.hixview.jpa.entity.PressEntity;
import site.hixview.jpa.mapper.PressEntityMapperImpl;
import site.hixview.jpa.repository.PressEntityRepository;

import java.util.List;
import java.util.Optional;

import static site.hixview.aggregate.util.ExceptionUtils.getFormattedExceptionMessage;
import static site.hixview.aggregate.vo.ExceptionMessage.ALREADY_EXISTED_ENTITY;
import static site.hixview.aggregate.vo.WordCamel.ENGLISH_NAME;
import static site.hixview.aggregate.vo.WordCamel.NUMBER;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class PressEntityService implements PressService {

    private final PressEntityRepository pressEntityRepository;
    private final PressEntityMapperImpl mapper = new PressEntityMapperImpl();

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
            throw new EntityExistsException(getFormattedExceptionMessage(
                    ALREADY_EXISTED_ENTITY, NUMBER, number, Press.class));
        }
        if (pressEntityRepository.findByEnglishName(englishName).isPresent()) {
            throw new EntityExistsException(getFormattedExceptionMessage(
                    ALREADY_EXISTED_ENTITY, ENGLISH_NAME, englishName, Press.class));
        }
        return mapper.toPress(pressEntityRepository.save(mapper.toPressEntity(press)));
    }

    @Override
    @Transactional
    public Press update(Press press) {
        Long number = press.getNumber();
        String englishName = press.getEnglishName();
        if (!pressEntityRepository.existsByNumber(number)) {
            throw new EntityNotFoundWithNumberException(number, Press.class);
        }
        if (pressEntityRepository.findByEnglishName(englishName).isPresent()) {
            throw new EntityExistsException(getFormattedExceptionMessage(
                    ALREADY_EXISTED_ENTITY, ENGLISH_NAME, englishName, Press.class));
        }
        return mapper.toPress(pressEntityRepository.save(mapper.toPressEntity(press)));
    }

    @Override
    @Transactional
    public void removeByNumber(Long number) {
        if (!pressEntityRepository.existsByNumber(number)) {
            throw new EntityNotFoundWithNumberException(number, Press.class);
        }
        pressEntityRepository.deleteByNumber(number);
    }

    private Optional<Press> getOptionalPress(PressEntity pressEntity) {
        if (pressEntity == null) {
            return Optional.empty();
        }
        return Optional.of(mapper.toPress(pressEntity));
    }
}
