package com.example.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.model.post.Post;

@Repository
public interface PostDao extends JpaRepository<Post, Long>{

}
