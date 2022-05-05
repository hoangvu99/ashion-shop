package com.example.service;

import java.util.List;

import com.example.model.post.Post;

public interface PostService {
	
	public void savePost(Post post);
	
	public void updatePost(Post post);
	
	public Post findPostById(long id);
	
	public List<Post> listPosts();
	
	public void deletePost(long id);
}
