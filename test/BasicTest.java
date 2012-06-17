import org.junit.*;
import java.util.*;
import play.test.*;
import models.*;

public class BasicTest extends UnitTest {
	
	@Before 
	public void setup(){
		Fixtures.deleteDatabase();
	}

    @Test
    public void createAndRetrieveUser(){
    		new User("bob@gmail.com","secret","Bob").save();
    		
    		User bob = User.find("byEmail", "bob@gmail.com").first();
    		
    		assertNotNull(bob);
    		assertEquals("Bob", bob.fullname);
    }
    
    @Test
    public void createPost(){
    		//create a new user and save
    		User bob = new User("bob@gmail.com","secret","Bob").save();
    		//Create a new post
    		new Post( bob,"First Post by Pop about Beer","not much about beer in this post, see next one, dude").save();
    		//Test that the post has been created//
    		assertEquals(1,Post.count());
    		
    		List<Post> bobPosts = Post.find("byAuthor",bob).fetch();
    		assertEquals(1,bobPosts.size()); 
    		
    		Post firstPost = bobPosts.get(0);
    		assertNotNull(firstPost);
    		assertNotNull(firstPost.postAt);
    }
    
    @Test
    public void postComents(){
    		User bob = new User("bob@gmail.com","secret","Bob").save();
    		//create a new post
    		Post bobPost = new Post(bob,"My first post","Hello World").save();
    		//create a few comments against this post
    		new Comment(bobPost,"Yinan","This kinda of shit").save();
    		new Comment(bobPost,"Pengcheng","Muhahha, a good comments").save();
    		
    		List<Comment> bobPostComments = Comment.find("byPost", bobPost).fetch();
    		
    		assertEquals(2,bobPostComments.size());
    		Comment firstComment = bobPostComments.get(0);
    		Comment secondComment = bobPostComments.get(1);
    		assertNotNull(firstComment);
    		assertEquals("Yinan",firstComment.author);
    		assertEquals("Pengcheng",secondComment.author);
    		
    		
    		
    		
    	
    }

}
