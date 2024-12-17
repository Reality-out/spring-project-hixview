package site.hixview.jpa.service;

import jakarta.persistence.EntityExistsException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import site.hixview.aggregate.domain.SiteMember;
import site.hixview.aggregate.error.EntityNotFoundWithNumberException;
import site.hixview.aggregate.service.SiteMemberService;
import site.hixview.jpa.entity.SiteMemberEntity;
import site.hixview.jpa.mapper.SiteMemberEntityMapperImpl;
import site.hixview.jpa.repository.SiteMemberEntityRepository;

import java.util.List;
import java.util.Optional;

import static site.hixview.aggregate.util.ExceptionUtils.getFormattedExceptionMessage;
import static site.hixview.aggregate.vo.ExceptionMessage.ALREADY_EXISTED_ENTITY;
import static site.hixview.aggregate.vo.WordCamel.ID;
import static site.hixview.aggregate.vo.WordCamel.NUMBER;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class SiteMemberEntityService implements SiteMemberService {

    private final SiteMemberEntityRepository siteMemberEntityRepository;
    private final SiteMemberEntityMapperImpl mapper = new SiteMemberEntityMapperImpl();

    @Override
    public List<SiteMember> getAll() {
        return siteMemberEntityRepository.findAll().stream().map(mapper::toSiteMember).toList();
    }

    @Override
    public List<SiteMember> getByName(String name) {
        return siteMemberEntityRepository.findByName(name).stream().map(mapper::toSiteMember).toList();
    }

    @Override
    public Optional<SiteMember> getByNumber(Long number) {
        return getOptionalSiteMember(siteMemberEntityRepository.findByNumber(number).orElse(null));
    }

    @Override
    public Optional<SiteMember> getById(String id) {
        return getOptionalSiteMember(siteMemberEntityRepository.findById(id).orElse(null));
    }

    @Override
    public Optional<SiteMember> getByIdAndPw(String id, String pw) {
        return getOptionalSiteMember(siteMemberEntityRepository.findByIdAndPw(id, pw).orElse(null));
    }

    @Override
    public Optional<SiteMember> getByEmail(String email) {
        return getOptionalSiteMember(siteMemberEntityRepository.findByEmail(email).orElse(null));
    }

    @Override
    @Transactional
    public SiteMember insert(SiteMember siteMember) {
        Long number = siteMember.getNumber();
        String id = siteMember.getId();
        if (siteMemberEntityRepository.existsByNumber(number)) {
            throw new EntityExistsException(getFormattedExceptionMessage(
                    ALREADY_EXISTED_ENTITY, NUMBER, number, SiteMember.class));
        }
        if (siteMemberEntityRepository.findById(id).isPresent()) {
            throw new EntityExistsException(getFormattedExceptionMessage(
                    ALREADY_EXISTED_ENTITY, ID, id, SiteMember.class));
        }
        return mapper.toSiteMember(siteMemberEntityRepository.save(mapper.toSiteMemberEntity(siteMember)));
    }

    @Override
    @Transactional
    public SiteMember update(SiteMember siteMember) {
        Long number = siteMember.getNumber();
        String id = siteMember.getId();
        if (!siteMemberEntityRepository.existsByNumber(number)) {
            throw new EntityNotFoundWithNumberException(number, SiteMember.class);
        }
        if (siteMemberEntityRepository.findById(id).isPresent()) {
            throw new EntityExistsException(getFormattedExceptionMessage(
                    ALREADY_EXISTED_ENTITY, ID, id, SiteMember.class));
        }
        return mapper.toSiteMember(siteMemberEntityRepository.save(mapper.toSiteMemberEntity(siteMember)));
    }

    @Override
    @Transactional
    public void removeByNumber(Long number) {
        if (!siteMemberEntityRepository.existsByNumber(number)) {
            throw new EntityNotFoundWithNumberException(number, SiteMember.class);
        }
        siteMemberEntityRepository.deleteByNumber(number);
    }

    private Optional<SiteMember> getOptionalSiteMember(SiteMemberEntity siteMemberEntity) {
        if (siteMemberEntity == null) {
            return Optional.empty();
        }
        return Optional.of(mapper.toSiteMember(siteMemberEntity));
    }
}
