package com.mantralabsglobal.scrap.scheduletask;

import java.text.ParseException;
import java.text.SimpleDateFormat;

import org.jsoup.nodes.Document;
import org.springframework.stereotype.Component;

import com.mantralabsglobal.scrap.HtmlUtil;
import com.mantralabsglobal.scrap.blog.BlogParser;
import com.mantralabsglobal.scrap.dataobject.BlogPost;

@Component
public class ValuePickParser extends BlogParser{
	private final static String BLOG="value-picks-blogspot";
	
	public BlogPost parseBlogPost(Document document){
		BlogPost post = new BlogPost();
		post.setTitle(document.select("h3.post-title").select("a").text());
		post.setContent(document.select("div.post-body").html());
		post.setSummary(HtmlUtil.abbreviateHtmlString(post.getContent(), 100, true));
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