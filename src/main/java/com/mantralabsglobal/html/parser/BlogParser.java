package com.mantralabsglobal.html.parser;

import org.jsoup.nodes.Document;

import com.mantralabsglobal.scrap.blog.BlogPost;

public interface BlogParser {
	public BlogPost parseBlogPost(Document document);
	public boolean isPostPage(String href);
	public boolean isListPage(String href);
}
