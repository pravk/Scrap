package com.mantralabsglobal.scrap.blog;

import java.util.List;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
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
	
	protected String truncateAfteWords(int n, String str) {
		String trimmed = str.trim();
		int words = trimmed.isEmpty() ? 0 : trimmed.split("\\s+").length;
		
		if(n < words)
		{
			try
			{
				return str.replaceAll("^((?:\\W*\\w+){" + n + "}).*$", "$1");	
			}
			catch(Throwable e)
			{
				e.printStackTrace();
				return str;
			}
		}
		else 
			return str;
	}  
	
	protected void removeStyleAttributes(Elements el){
		for(Element e : el){
			if(e.hasAttr("style"))
			{
				e.removeAttr("style");
			}
			if(e.children() != null && e.children().size()>0)
			{
				removeStyleAttributes(e.children());
			}
		}
		
	}
}
