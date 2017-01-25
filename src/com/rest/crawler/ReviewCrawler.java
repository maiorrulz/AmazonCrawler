package com.rest.crawler;

import java.io.IOException;
import java.util.Vector;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class ReviewCrawler implements Runnable {

	private Vector<Review> reviews;
	private String url;
	public ReviewCrawler (String url, Vector<Review> reviews) {
		
		this.url = url;
		this.reviews = reviews;
	}
	 
	@Override
	public void run() {
		
		System.out.println(Thread.currentThread().getName() + " Vector size" + reviews.size());
		Document doc = null;

			try {
				doc = Jsoup.connect(url)
						  .data("query", "Java")
						  .userAgent("Mozilla")
						  .timeout(3000)
						  .get();
			} catch (IOException e) {
				System.out.println("Cannont connect to " + url+ "\n Exception : " + e);
				return;
			}
			
			Elements es = doc.select("div[data-hook=review]");
			for (Element e : es) {

				Review r = new Review();

				r.setTitle(e.select("a[data-hook=review-title]").first().text());
				r.setBy(e.select("a[data-hook=review-author]").first().text());
				r.setDate(e.select("span[data-hook=review-date]").first().text());
				r.setBody(e.select("span[data-hook=review-body]").first().text());
	
				reviews.add(r);

			}

		int size = es.size();
		System.err.println(size);

	}
	
	
}
