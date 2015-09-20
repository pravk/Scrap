package com.mantralabsglobal.scrap.rest.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.mantralabsglobal.scrap.dataobject.BlogPost;
import com.mantralabsglobal.scrap.dataobject.BlogSearchCriteria;

import sun.reflect.generics.reflectiveObjects.NotImplementedException;

@Controller
@RequestMapping("/blog/*")
public class BlogController {

	@RequestMapping(method=RequestMethod.GET, value="post")
    public @ResponseBody BlogPost getBlogPost(@RequestParam(value="id", required=false) String postId) {
        return new BlogPost();
		//throw new NotImplementedException();
		//return new Greeting(counter.incrementAndGet(), String.format(template, name));
    }
	
	@RequestMapping(method=RequestMethod.GET, value="search")
    public @ResponseBody List<BlogPost> searchBlog(@RequestParam(required=true) BlogSearchCriteria searchCriteria) {
        throw new NotImplementedException();
    }	
	
}
