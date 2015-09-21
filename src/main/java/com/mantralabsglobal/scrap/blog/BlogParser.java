package com.mantralabsglobal.scrap.blog;

import java.util.List;

import org.jsoup.nodes.Document;
import org.springframework.beans.factory.annotation.Autowired;

import com.mantralabsglobal.scrap.dataobject.BlogComment;
import com.mantralabsglobal.scrap.dataobject.BlogPost;
import com.mantralabsglobal.scrap.repository.BlogPostRepository;

public abstract class BlogParser {
	public abstract BlogPost parseBlogPost(Document document, BlogPost blogPost);
	
	public abstract List<BlogComment> parseBlogComments(Document document);
	
	public abstract boolean isPostPage(String href);
	public abstract boolean isListPage(String href);
	
	public BlogPostRepository getRepository() {
		return repository;
	}
	public void setRepository(BlogPostRepository repository) {
		this.repository = repository;
	}

	@Autowired
	private BlogPostRepository repository;
}
