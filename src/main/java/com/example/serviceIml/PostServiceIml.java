package com.example.serviceIml;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.dao.PostDao;
import com.example.model.post.Post;
import com.example.service.PostService;

@Service
public class PostServiceIml implements PostService{
	@Autowired
	PostDao postDao;
	@Override
	public void savePost(Post post) {
		postDao.save(post);
		
	}

	@Override
	public void updatePost(Post post) {
		postDao.saveAndFlush(post);
		
	}

	@Override
	public Post findPostById(long id) {
		Optional<Post>p = postDao.findById(id);
		if(p.isPresent()) {
			return p.get();
		}
		return null;
	}

	@Override
	public List<Post> listPosts() {
		// TODO Auto-generated method stub
		return postDao.findAll();
	}
	
	@Override
	public void deletePost(long id) {
		postDao.deleteById(id);
		
	}
	
		
	
}
