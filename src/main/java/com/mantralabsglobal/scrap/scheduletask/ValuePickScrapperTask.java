package com.mantralabsglobal.scrap.scheduletask;

import java.io.IOException;
import java.util.Iterator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.mantralabsglobal.scrap.blog.BlogScrapper;
import com.mantralabsglobal.scrap.dataobject.BlogPost;
import com.mantralabsglobal.scrap.repository.BlogPostRepository;

@Component
public class ValuePickScrapperTask {

	@Autowired
	private BlogPostRepository repository;
	
	@Autowired
	private ValuePickParser parser;
	
	@Scheduled(fixedDelay=3600000)
	public void invokeScrap() throws IOException{
		BlogScrapper scrapper = new BlogScrapper("http://value-picks.blogspot.in/", parser);
		scrapper.setThreshold(5);
		scrapper.scrap();
		Iterator<BlogPost> iterator = scrapper.iterator();
		while(iterator.hasNext()){
			BlogPost post = iterator.next();
			if(post != null){
				System.out.println("New post: " + post.getUrl());
				repository.save(post);
			}
		}
	}

	public ValuePickParser getParser() {
		return parser;
	}

	public void setParser(ValuePickParser parser) {
		this.parser = parser;
	}
	
	
}
