package com.example.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.model.size.Size;

@Repository
public interface SizeDao extends JpaRepository<Size, Integer>{

}
