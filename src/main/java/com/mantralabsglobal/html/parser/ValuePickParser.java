package com.mantralabsglobal.html.parser;

import java.text.ParseException;
import java.text.SimpleDateFormat;

import org.jsoup.nodes.Document;

import com.mantralabsglobal.scrap.blog.BlogPost;

public class ValuePickParser implements BlogParser{

	
	public BlogPost parseBlogPost(Document document){
		ValuePickPost post = new ValuePickPost();
		post.setTitle(document.select("h3.post-title").select("a").text());
		post.setContent(document.select("div.post-body").html());
		post.setAuthor(document.select("span.post-author").select("span").text());
		
		SimpleDateFormat format = new SimpleDateFormat("EEE, MMM dd, yyyy");
		
		try {
			post.setLastModified( format.parse(document.select("h2.date-header").select("span").text()).getTime());
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return post;
		
	}
	
	private class ValuePickPost implements BlogPost{

		private String title;
		private String content;
		private long lastModified;
		private String author;
		
		@Override
		public String getTitle() {
			return title;
		}

		@Override
		public String getContent() {
			return content;
		}

		@Override
		public long getLastModified() {
			return lastModified;
		}

		@Override
		public String getAuthor() {
			return author;
		}

		public void setTitle(String title) {
			this.title = title;
		}

		public void setContent(String content) {
			this.content = content;
		}

		public void setLastModified(long lastModified) {
			this.lastModified = lastModified;
		}

		public void setAuthor(String author) {
			this.author = author;
		}
		
	}

	public boolean isPostPage(String href) {
		return href.matches("http:\\/\\/value-picks.blogspot.in\\/(\\d{4}\\/\\d{2})\\/(\\w+(-\\w+)*).html");
	}

	public boolean isListPage(String href) {
		return href.startsWith("http://value-picks.blogspot.in/search?updated-max=");
	}
	
}
