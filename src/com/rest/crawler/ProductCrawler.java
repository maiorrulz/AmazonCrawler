package com.rest.crawler;

import java.io.IOException;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Vector;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import com.rest.crawler.ReturnObject.Status;

public class ProductCrawler {
	
	private Queue<String> linksQueue;
	private String productTitle;
	private String imageUrl;
	private Vector<Review> reviews;
	
	private final String DEFAULT_IMAGE = "http://www.freeiconspng.com/uploads/no-image-icon-23.jpg";
	private final String DEFAULT_NAME = "*NONE*";
	private final String BASE_URL = "https://www.amazon.com";
	
	public ProductCrawler() {
		
		reviews = new Vector<Review>();
		linksQueue = new LinkedList<String>();
		
	}
	
	
	
	
	public ReturnObject startCrawling(String url) {
		
		if(!url.contains("www.amazon.com"))
			return new ReturnObject(Status.ERROR, null, "Please provide Amazon link");
		
		ReturnObject retVal = processMainProductPage(url);
		if(retVal.status != Status.OK)
			return retVal;
		
		String commentsUrl = (String)retVal.data;
		
		retVal = processReviewPage(commentsUrl);
		if(retVal.status != Status.OK)
			return retVal;
		ExecutorServiceThreadPool exService = new ExecutorServiceThreadPool(linksQueue);
		exService.startProcessing(reviews);
		exService.finish();	
		
		System.out.println("reviwSize: " +reviews.size());
//		reviews.forEach(e -> System.out.println(e));
		
		return new ReturnObject(Status.OK,null ,"All Chill");
	}
	

	
	private ReturnObject processMainProductPage(String url) {
		
		Document doc =null;
	
			
			try {
				url = checkUrl(url);
				doc = Jsoup.connect(url)
					  .data("query", "Java")
					  .userAgent("Mozilla")
					  .timeout(5000)
					  .get();

			
			
			Elements titles = doc.select("span#productTitle");
			String productImage = getProductImage(doc);
			productTitle = titles.size() > 0 ? titles.first().text() : DEFAULT_NAME;
			imageUrl = productImage != null ? productImage : DEFAULT_IMAGE;
			
			//getting link to comments;
			Elements linksToComments = doc.select("a.a-link-emphasis.a-text-bold");
			
			return linksToComments.size() > 0 ? new ReturnObject(Status.OK,linksToComments.first().attr("href"),"All Chill") : 
				new ReturnObject(Status.FAILED, null, "No Link to Review page. Check provided link");
	
			} catch (IOException e) {
				System.out.println("Cannont connect to " + url+ "\n Exception : " + e);
				return new ReturnObject(Status.ERROR, null, "Cannot connect to provided URL : " + url);
			}
			
	}
	
	private String  getProductImage(Document doc) {
		
		Elements productImage = doc.select("img.a-dynamic-image.a-stretch-vertical,img.a-dynamic-image.a-stretch-horizontal,img.a-dynamic-image.image-stretch-horizontal");
		if(productImage.size() < 1)
			return null;
		String images = productImage.first().attr("data-a-dynamic-image");
		
		Pattern pattern = Pattern.compile("(?:\\\").*?(?:\\\")");
		Matcher matcher = pattern.matcher(images);
		String image = matcher.find() ?  matcher.group(0).replaceAll("\"", "") : null;
		
		return image;
		
	}
	
	private ReturnObject processReviewPage(String commentsUrl) {
		
		Document doc =null;
			
			try {
				commentsUrl = checkUrl(commentsUrl);
				doc = Jsoup.connect(commentsUrl)
						  .data("query", "Java")
						  .userAgent("Mozilla")
						  .timeout(5000)
						  .get();
			} catch (IOException e) {
				System.out.println("Cannont connect to " + commentsUrl + "\n Exception : " + e);
				return new ReturnObject(Status.ERROR,null,"Cannot connect to provided url");
			}
			Elements li = doc.select("li[data-reftag]");
			String url = li.last().child(0).attr("href");
			Pattern re = Pattern.compile("\\d+");
			Matcher m = re.matcher((li.last().text()));
			
			//Remove spaces and commas and dots
			StringBuilder maxPageString= new StringBuilder();
		    while (m.find()){
		      for( int groupIdx = 0; groupIdx < m.groupCount()+1; groupIdx++ ){
		    	  maxPageString.append(m.group(groupIdx));
		      }
		    }
			int maxPage = Integer.parseInt(maxPageString.toString());
			
			for(int i=1 ; i <= maxPage ; i++) {
				String urlToReview = url.replaceFirst("(?<=ref=)[^&|?]+", "cm_cr_getr_d_paging_btm_" + i).replaceFirst("(?<=pageNumber=)[^&|?]+", "" + i);
				urlToReview = checkUrl(urlToReview);
				linksQueue.add(urlToReview);
			}
			
			return new ReturnObject(Status.OK, null, "All Chill");
	}
	
	
	public ProcessedProduct processedProduct () {
		
		return new ProcessedProduct(getProductTitle(), getImageUrl(), getReviews());
	}
	
	private String checkUrl(String url) {
		
		if(url.contains(BASE_URL))
			return url;
		return BASE_URL+url;
	}



	public String getProductTitle() {
		return productTitle;
	}




	public String getImageUrl() {
		return imageUrl;
	}
	
	public Vector<Review> getReviews() {
		return reviews;
	}




	public void setProductTitle(String productTitle) {
		this.productTitle = productTitle;
	}




	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}




	public void setReviews(Vector<Review> reviews) {
		this.reviews = reviews;
	}


	
		

}
