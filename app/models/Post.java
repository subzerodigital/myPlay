package models;

import play.db.jpa.*;
import java.util.*;
import javax.persistence.*;

@Entity
public class Post extends Model {
	public String title;
	public Date postedAt;
	@Lob
	public String content;
	@ManyToOne
	public User author;
	@OneToMany (mappedBy="post",cascade=CascadeType.ALL)
	public List<Comment> comments;
	
	public Post(User author,String title, String content){
		this.author = author;
		this.title = title;
		this.content = content;
		this.postedAt = new Date();
		this.comments = new ArrayList<Comment>();
	}
	
	public Post addComment(String author, String content){
		Comment comment = new Comment(this,author,content);
		this.comments.add(comment);
		this.save();
		return this;
	}
}
