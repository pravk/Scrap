package com.mantralabsglobal.scrap.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import com.mantralabsglobal.scrap.dataobject.BlogPost;

public interface BlogPostRepository extends MongoRepository<BlogPost, String>{

	@Query(value="{$or:[{'title':?0},{'content':?0}]}")
	List<BlogPost> findByContentLikeIgnoreCase(String content, Pageable pageable);
	List<BlogPost> findByUrl(String url);
	Page<BlogPost> findAll(Pageable pagable);
}
