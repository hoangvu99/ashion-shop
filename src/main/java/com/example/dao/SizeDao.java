package com.example.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.example.model.size.Size;

@Repository
public interface SizeDao extends JpaRepository<Size, Integer>{
	
	@Query(value = " select size_name from size", nativeQuery = true)
	public List<String>listSizeName();
	
	@Query(value =" select * from size where id= ?1", nativeQuery = true)
	public Size findSizeById(int id);

}
