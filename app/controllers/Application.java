package controllers;

import play.*;
import play.mvc.*;
import java.util.*;
import models.*;
import play.data.validation.*;
import play.libs.Codec;
import play.libs.Images;
import play.cache.Cache;



public class Application extends Controller {
	//render front page
    public static void index() {
        Post frontPost = Post.find("order by postedAt desc").first();
        List<Post> olderPosts = Post.find("order by postedAt desc").from(1).fetch(10);
        render(frontPost,olderPosts);
    }
    
    //load config
    @Before
    public static void addDefaults(){
    		renderArgs.put("blogTitle", Play.configuration.getProperty("blog.title"));
    		renderArgs.put("blogBaseline", Play.configuration.getProperty("blog.baseline"));
    }
    
    public static void show(Long id){
    		Post post = Post.findById(id);
    		//this is to generate a unit id for captcha
    		String randomID = Codec.UUID();
    		render(post,randomID);
    }
    
    public static void postComment(Long postId, 
    							   @Required (message = "Author is required")String author,
    							   @Required (message = "Please fill in some content")String content,
    							   @Required (message = "Please enter the code")String code,
    							   String randomID
    							   ){
		 Post post = Post.findById(postId);
		 
		 validation.equals(code,Cache.get(randomID)).message("Ivalide code, please type again");
		 
		 if(validation.hasErrors()){
			 render("Application/show.html",post,randomID);
		 }
		 
		 post.addComment(author, content);
		 
		 flash.success("Thanks for posting %s", author);
		 Cache.delete(randomID);
		 show(postId);
	}
    
    public static void captcha(String id){
    	Images.Captcha captcha = Images.captcha(200,50);
    	captcha.setSquigglesBackground();
    	String code = captcha.getText();
    	Cache.set(id, code, "10mn");
    	renderBinary(captcha);
    }

}