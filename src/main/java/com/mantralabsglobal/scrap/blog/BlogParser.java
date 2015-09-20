package com.mantralabsglobal.scrap.blog;

import org.jsoup.nodes.Document;

import com.mantralabsglobal.scrap.dataobject.BlogPost;

public interface BlogParser {
	public BlogPost parseBlogPost(Document document);
	public boolean isPostPage(String href);
	public boolean isListPage(String href);
}
