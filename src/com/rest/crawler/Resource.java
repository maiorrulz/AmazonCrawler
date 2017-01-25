package com.rest.crawler;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.json.JSONException;

import com.google.gson.Gson;
//import javax.ws.rs.core.Response.Status;
import com.rest.crawler.ReturnObject.Status;

@Path("/amazonPages")
public class Resource {
	
 
    @GET
	@Consumes(MediaType.TEXT_PLAIN)
    @Produces(MediaType.TEXT_HTML)
    public Response reverser(@Context HttpServletRequest  rq) throws JSONException {
    	
    	String result;
    	
	    ProductCrawler pc = new ProductCrawler();
		ReturnObject ro = pc.startCrawling(rq.getParameter("URL"));
		
		if(ro.status != Status.OK)
			result = "<h1>" + ro.description + "</h1>";
		else
			result = "<h1>" + pc.getProductTitle() + "</h1>" 
							+ "<img src="+pc.getImageUrl()+"> <hr>"
							+"<a href=\"https://github.com/maiorrulz/AmazonCrawler\" style=\"margin-left: 300px\">Source GitHub</a>" 
							+"<div>"+ new Gson().toJson(pc.processedProduct()) +"</div>";
		
	    return Response.status(200)
        		.header("Access-Control-Allow-Methods", "POST")
        		.header("Access-Control-Allow-Origin", "*")
        		.entity(result).build();
    }
    
    

}
