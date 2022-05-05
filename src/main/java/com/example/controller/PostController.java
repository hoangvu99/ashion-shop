package com.example.controller;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.dto.PostDTO;
import com.example.model.post.Post;
import com.example.service.PostService;

@Controller
public class PostController {
	
	@Autowired
	PostService postService;
	
	@Autowired
	SimpleDateFormat dateFormat;
	
	@RequestMapping(value="/add-post", method = RequestMethod.GET)
	public String addPost(Model model) {
		
		model.addAttribute("post", new PostDTO());
		return "add-post";
	}
	
	@RequestMapping(value="/add-post", method = RequestMethod.POST, consumes = "multipart/form-data")
	public String addPostSubmit(@ModelAttribute(name = "post") PostDTO postDTO) {
		
		try {
			Post post = new Post();
			post.setTitle(postDTO.getTitle());
			post.setBody(postDTO.getBody());
			post.setCreatedAt(dateFormat.format(new Date()));
			postService.savePost(post);
		} catch (Exception e) {
			
		}
		return "redirect:/list-post";
	}
	
	@RequestMapping(value="/list-post", method = RequestMethod.GET)
	public String listPosts(Model model) {
		model.addAttribute("posts", postService.listPosts());
		return "list-post";
	}
	
	@RequestMapping(value= "/view-post")
	public String blogDetails(@RequestParam(name="id") long id, Model model){
		Post p = postService.findPostById(id);
		PostDTO postDTO = new PostDTO(p.getTitle(), p.getBody());
		model.addAttribute("post",postDTO);
		model.addAttribute("id", id);
		return "post-details";
	}
	
	@RequestMapping(value="/edit-post", method = RequestMethod.POST, consumes = "multipart/form-data")
	public String editPostSubmit(@RequestParam(name = "id")long id,@ModelAttribute(name = "post") PostDTO postDTO) {
		
		try {
			System.out.println(postDTO.getBody());
			Post post = postService.findPostById(id);
			post.setTitle(postDTO.getTitle());
			post.setBody(postDTO.getBody());
			post.setCreatedAt(dateFormat.format(new Date()));
			postService.updatePost(post);
		} catch (Exception e) {
			
		}
		return "redirect:/list-post";
	}
	
	
}
