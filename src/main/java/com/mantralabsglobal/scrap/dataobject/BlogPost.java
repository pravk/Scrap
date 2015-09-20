package com.mantralabsglobal.scrap.dataobject;

public class BlogPost {

	private String title;
	private String content;
	private long lastModified;
	private String author;
	
	public String getTitle() {
		return title;
	}

	public String getContent() {
		return content;
	}

	public long getLastModified() {
		return lastModified;
	}

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
