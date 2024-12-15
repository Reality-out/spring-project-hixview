package site.hixview.aggregate.service;

import site.hixview.aggregate.domain.Post;
import site.hixview.aggregate.service.supers.OnlyGetAllowedServiceWithNumberId;

public interface PostService extends OnlyGetAllowedServiceWithNumberId<Post> {
}
