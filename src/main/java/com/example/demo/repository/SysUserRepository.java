package com.example.demo.repository;

import com.example.demo.core.GenericRepository;
import com.example.demo.dto.SysUserDTO;
import com.example.demo.entity.SysUser;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * author: wangshuiping
 * date: 2021/1/20
 */
public interface SysUserRepository extends GenericRepository<SysUser, Long> {

    @Query("select u.id, u.name from SysUser u")
    List<SysUser> findDTO();
}
