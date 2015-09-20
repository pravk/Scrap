package com.mantralabsglobal.scrap.scheduletask;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Iterator;

import org.jsoup.nodes.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.mantralabsglobal.scrap.blog.BlogParser;
import com.mantralabsglobal.scrap.blog.BlogScrapper;
import com.mantralabsglobal.scrap.dataobject.BlogPost;
import com.mantralabsglobal.scrap.repository.BlogPostRepository;

@Component
public class ValuePickScrapperTask {

	private final static String BLOG="value-picks-blogspot";
	@Autowired
	private BlogPostRepository repository;
	
	@Scheduled(fixedDelay=3600000)
	public void invokeScrap() throws IOException{
		ValuePickParser parser = new ValuePickParser();
		BlogScrapper scrapper = new BlogScrapper("http://value-picks.blogspot.in/", parser);
		scrapper.scrap();
		Iterator<BlogPost> iterator = scrapper.iterator();
		while(iterator.hasNext()){
			repository.save(iterator.next());
		}
	}
	
	public class ValuePickParser implements BlogParser{
		
		public BlogPost parseBlogPost(Document document){
			BlogPost post = new BlogPost();
			post.setTitle(document.select("h3.post-title").select("a").text());
			post.setContent(document.select("div.post-body").html());
			post.setAuthor(document.select("span.post-author").select("span").text());
			post.setBlog(BLOG);
			SimpleDateFormat format = new SimpleDateFormat("EEE, MMM dd, yyyy");
			
			try {
				post.setLastModified( format.parse(document.select("h2.date-header").select("span").text()).getTime());
			} catch (ParseException e) {
				e.printStackTrace();
			}
			return post;
			
		}
		
		public boolean isPostPage(String href) {
			return href.matches("http:\\/\\/value-picks.blogspot.in\\/(\\d{4}\\/\\d{2})\\/(\\w+(-\\w+)*).html");
		}

		public boolean isListPage(String href) {
			return href.startsWith("http://value-picks.blogspot.in/search?updated-max=");
		}
		
	}
}
