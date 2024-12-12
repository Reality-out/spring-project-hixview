package site.hixview.aggregate.service;

import site.hixview.aggregate.domain.Post;
import site.hixview.aggregate.service.supers.OnlyAllowedToSearch;

public interface PostService extends OnlyAllowedToSearch<Post> {
}
