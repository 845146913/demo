package com.example.demo.repository;

import com.example.demo.core.GenericRepository;
import com.example.demo.core.NativeQuery;
import com.example.demo.dto.SysUserDTO;
import com.example.demo.entity.SysUser;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Created by IntelliJ IDEA.
 * author: wangshuiping
 * date: 2021/1/20
 */
public interface SysUserRepository extends GenericRepository<SysUser, Long> {

    @Query(value = "select new com.example.demo.dto.SysUserDTO(u.id, u.name) from SysUser u")
    List<SysUserDTO> findDTO();

    <S> Optional<S> findById(Long id, Class<S> clazz);

//    @NativeQuery("select u.id u.name from sys_user u")
//    List<SysUserDTO> findAllDTO();

    @Query(value = "select u.id as id, u.name as name from sys_user u", nativeQuery = true)
    List<SysUserDTO> findAllDTO();

    @Query(value = "select u.id as id, u.name as name from sys_user u", nativeQuery = true)
    List<Map<String, Object>> findList();

    @Query(value = "select u.id as id, u.name as name from sys_user u limit 1", nativeQuery = true)
    Map<String, Object> findMap();
}
