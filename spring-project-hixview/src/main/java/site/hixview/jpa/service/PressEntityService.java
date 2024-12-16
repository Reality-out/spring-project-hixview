package site.hixview.jpa.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import site.hixview.aggregate.domain.Press;
import site.hixview.aggregate.error.EntityExistsWithNameException;
import site.hixview.aggregate.error.EntityExistsWithNumberException;
import site.hixview.aggregate.error.EntityNotFoundWithNumberException;
import site.hixview.aggregate.service.PressService;
import site.hixview.jpa.entity.PressEntity;
import site.hixview.jpa.mapper.PressEntityMapperImpl;
import site.hixview.jpa.repository.PressEntityRepository;

import java.util.List;
import java.util.Optional;

import static site.hixview.aggregate.vo.ExceptionMessage.ALREADY_EXISTED_ENTITY_WITH_ENGLISH_NAME;
import static site.hixview.aggregate.vo.ExceptionMessage.FOR_THE_CLASS_NAMED;

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
            throw new EntityExistsWithNumberException(number, Press.class);
        }
        if (pressEntityRepository.findByEnglishName(englishName).isPresent()) {
            throw new EntityExistsWithNameException(ALREADY_EXISTED_ENTITY_WITH_ENGLISH_NAME + englishName
                    + FOR_THE_CLASS_NAMED + Press.class.getSimpleName());
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
            throw new EntityExistsWithNameException(ALREADY_EXISTED_ENTITY_WITH_ENGLISH_NAME + englishName
                    + FOR_THE_CLASS_NAMED + Press.class.getSimpleName());
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
