package site.hixview.aggregate.service;

import site.hixview.aggregate.domain.BlogPost;
import site.hixview.aggregate.enums.Classification;
import site.hixview.aggregate.service.supers.CrudAllowedServiceWithNumberId;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface BlogPostService extends CrudAllowedServiceWithNumberId<BlogPost> {
    List<BlogPost> getByDate(LocalDate date);

    List<BlogPost> getByDateRange(LocalDate startDate, LocalDate endDate);

    Optional<BlogPost> getByName(String name);

    Optional<BlogPost> getByLink(String link);

    Optional<BlogPost> getByClassification(Classification classification);
}
