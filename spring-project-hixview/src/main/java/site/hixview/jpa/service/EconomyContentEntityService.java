package site.hixview.jpa.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import site.hixview.aggregate.domain.EconomyContent;
import site.hixview.aggregate.error.EntityExistsWithNameException;
import site.hixview.aggregate.error.EntityExistsWithNumberException;
import site.hixview.aggregate.error.EntityNotFoundWithNumberException;
import site.hixview.aggregate.service.EconomyContentService;
import site.hixview.jpa.entity.EconomyContentEntity;
import site.hixview.jpa.mapper.EconomyContentEntityMapperImpl;
import site.hixview.jpa.repository.EconomyContentEntityRepository;

import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class EconomyContentEntityService implements EconomyContentService {

    private final EconomyContentEntityRepository economyContentEntityRepository;
    private final EconomyContentEntityMapperImpl mapper = new EconomyContentEntityMapperImpl();

    @Override
    public List<EconomyContent> getAll() {
        return economyContentEntityRepository.findAll().stream().map(mapper::toEconomyContent).toList();
    }

    @Override
    public Optional<EconomyContent> getByNumber(Long number) {
        return getOptionalEconomyContent(economyContentEntityRepository.findByNumber(number).orElse(null));
    }

    @Override
    public Optional<EconomyContent> getByName(String name) {
        return getOptionalEconomyContent(economyContentEntityRepository.findByName(name).orElse(null));
    }

    @Override
    @Transactional
    public EconomyContent insert(EconomyContent economyContent) {
        Long number = economyContent.getNumber();
        String name = economyContent.getName();
        if (economyContentEntityRepository.existsByNumber(number)) {
            throw new EntityExistsWithNumberException(number, EconomyContent.class);
        }
        if (economyContentEntityRepository.findByName(name).isPresent()) {
            throw new EntityExistsWithNameException(name, EconomyContent.class);
        }
        return mapper.toEconomyContent(economyContentEntityRepository.save(mapper.toEconomyContentEntity(economyContent)));
    }

    @Override
    @Transactional
    public EconomyContent update(EconomyContent economyContent) {
        Long number = economyContent.getNumber();
        String name = economyContent.getName();
        if (!economyContentEntityRepository.existsByNumber(number)) {
            throw new EntityNotFoundWithNumberException(number, EconomyContent.class);
        }
        if (economyContentEntityRepository.findByName(name).isPresent()) {
            throw new EntityExistsWithNameException(name, EconomyContent.class);
        }
        return mapper.toEconomyContent(economyContentEntityRepository.save(mapper.toEconomyContentEntity(economyContent)));
    }

    @Override
    @Transactional
    public void removeByNumber(Long number) {
        if (!economyContentEntityRepository.existsByNumber(number)) {
            throw new EntityNotFoundWithNumberException(number, EconomyContent.class);
        }
        economyContentEntityRepository.deleteByNumber(number);
    }

    private Optional<EconomyContent> getOptionalEconomyContent(EconomyContentEntity economyContentEntity) {
        if (economyContentEntity == null) {
            return Optional.empty();
        }
        return Optional.of(mapper.toEconomyContent(economyContentEntity));
    }
}
