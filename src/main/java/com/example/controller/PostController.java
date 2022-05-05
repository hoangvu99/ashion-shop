package com.example.controller;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.MimeTypeUtils;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.example.dto.PostDTO;
import com.example.model.post.Post;
import com.example.service.PostService;
import com.example.service.UploadFileService;

@Controller
public class PostController {
	
	@Autowired
	PostService postService;
	
	@Autowired
	SimpleDateFormat dateFormat;
	
	@Autowired
	UploadFileService uploadFileService;
	
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
			post.setThumnail(postDTO.getThumnail());
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
		PostDTO postDTO = new PostDTO(p.getTitle(),p.getThumnail(),p.getBody());
		model.addAttribute("post",postDTO);
		model.addAttribute("id", id);
		return "post-details";
	}
	
	@RequestMapping(value="/edit-post", method = RequestMethod.POST, consumes = "multipart/form-data")
	public String editPostSubmit(@RequestParam(name = "id")long id,@ModelAttribute(name = "post") PostDTO postDTO) {
		
		try {
			
			Post post = postService.findPostById(id);
			post.setTitle(postDTO.getTitle());
			post.setBody(postDTO.getBody());
			post.setThumnail(postDTO.getThumnail());
			post.setCreatedAt(dateFormat.format(new Date()));
			postService.updatePost(post);
		} catch (Exception e) {
			
		}
		return "redirect:/list-post";
	}
	@RequestMapping(value = "/upload-ckeditor", method = RequestMethod.POST,produces = {
			MimeTypeUtils.APPLICATION_JSON_VALUE
	})
	public ResponseEntity<JSONFileUpload> uploadFile(@RequestParam(name = "upload") MultipartFile file) {
		
		try {
			uploadFileService.saveUploadFile(file, "blog", file.getOriginalFilename());
		} catch (Exception e) {
			// TODO: handle exception
		}
		
		return new ResponseEntity<JSONFileUpload>(new JSONFileUpload("src/main/resources/static/img/upload/"+file.getOriginalFilename()), HttpStatus.OK);
	}
	
	@Value("${app.upload.path}")
	String rootLocation;
	
	@RequestMapping(  value="/file-browse",method = RequestMethod.GET)
	public String imageBrowser(Model model) {
		try {
			
			List<Path> paths =  Files.walk(Paths.get(rootLocation+"blog/")).filter(Files::isRegularFile).collect(Collectors.toList());
			
			model.addAttribute("paths", paths);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return "br";
	}
	
	@RequestMapping(value = "/blog")
	public String blogController(Model model) {
		model.addAttribute("blogs", postService.listPosts());
		return "blog";
	}
	
	@RequestMapping(value = "/blog-details")
	public String blogDetailsController(@RequestParam(name="id")long id, Model model) {	
		
		model.addAttribute("blog", postService.findPostById(id));
		return "blog-details";
	}
	
	@RequestMapping(value="/delete-post")
	public String deletePost(@RequestParam(name = "id")long id) {
		
		try {
			
			postService.deletePost(id);
			
			
		} catch (Exception e) {
			
		}
		return "redirect:/list-post";
	}
	
}
