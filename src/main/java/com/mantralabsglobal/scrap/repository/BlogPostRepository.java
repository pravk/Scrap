package com.mantralabsglobal.scrap.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import com.mantralabsglobal.scrap.dataobject.BlogPost;

public interface BlogPostRepository extends MongoRepository<BlogPost, String>{

	@Query(value="{$or:[{'title': { '$regex':?0, $options: 'i'}},{'content': { '$regex': ?0, $options: 'i'}}]}", fields="{ 'title' : 1, 'summary' : 1,'lastModified' : 1, 'author' : 1, 'imageUrlList' : 1, 'commentCount' : 1}")
	List<BlogPost> findByContentLikeIgnoreCase(String content, Pageable pageable);
	BlogPost findOneByUrl(String url);
	@Query(value="{}", fields="{ 'title' : 1, 'summary20' : 1, 'summary50' : 1, 'summary100' : 1, 'summary200' : 1, 'summary300' : 1, 'lastModified' : 1, 'author' : 1, 'imageUrlList' : 1, 'commentCount' : 1}")
	Page<BlogPost> findAll(Pageable pagable);
}
