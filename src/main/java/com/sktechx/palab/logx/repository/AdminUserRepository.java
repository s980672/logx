package com.sktechx.palab.logx.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

import com.sktechx.palab.logx.model.AdminUser;

/**
 * Created by 1100449 on 2016. 10. 28..
 */
public interface AdminUserRepository extends JpaRepository<AdminUser, Integer> {
	
	public List<AdminUser> findByUsername(@Param("username") String username);
	
}