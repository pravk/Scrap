package com.mantralabsglobal.scrap.rest.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.mantralabsglobal.scrap.dataobject.BlogPost;
import com.mantralabsglobal.scrap.repository.BlogPostRepository;

@Controller
@RequestMapping("/blog/*")
public class BlogController {

	@Autowired
	BlogPostRepository repository;
	
	@RequestMapping(method=RequestMethod.GET, value="post")
    public @ResponseBody BlogPost getBlogPost(@RequestParam(value="id", required=false) String postId) {
        return repository.findOne(postId);
    }
	
	@RequestMapping(method=RequestMethod.GET, value="search")
    public @ResponseBody List<BlogPost> searchBlog(@RequestParam(required=true) String search, @RequestParam(required=false, defaultValue="0") int page, @RequestParam(required=false, defaultValue="10") int size) {
        List<BlogPost> results = repository.findByTitleLikeIgnoreCase(search);
        PageRequest pageRequest = new PageRequest(page, size, Direction.DESC, "lastModified");
        results.addAll(repository.findByContentLikeIgnoreCase(search,  pageRequest));
        return results;
    }	
	
	@RequestMapping(method=RequestMethod.GET, value="recent")
    public @ResponseBody List<BlogPost> recent(@RequestParam(required=false, defaultValue="0") int page, @RequestParam(required=false, defaultValue="10") int size) {
		PageRequest pageRequest = new PageRequest(page, size, Direction.DESC, "lastModified");
        Page<BlogPost> blogPage = repository.findAll(pageRequest);
        return blogPage.getContent();
    }
	
}
