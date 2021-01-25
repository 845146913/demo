package com.example.demo.controller;

import com.example.demo.dto.SysUserDTO;
import com.example.demo.entity.SysRole;
import com.example.demo.entity.SysUser;
import com.example.demo.repository.SysUserRepository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * author: wangshuiping
 * date: 2021/1/20
 */
@RestController
@RequestMapping("/user")
public class UserController {
    @Resource
    private SysUserRepository sysUserRepository;

    @GetMapping("/init")
    public void init() {
        List<SysUser> users = new ArrayList<>();
        for(int i=0; i<10; i++) {
            SysUser u = new SysUser();
            u.setName("A"+i);
            u.setUrl("U"+i);
            SysRole r = new SysRole();
            r.setName("R" + i);
            u.setRoles(Collections.singleton(r));
            users.add(u);
        }
        sysUserRepository.saveAll(users);
    }

    @GetMapping
    public List<SysUser> list() {

        return sysUserRepository.findAll();
    }

    @GetMapping("/t")
    public List<SysUserDTO> listDto() {
        List<SysUserDTO> list = sysUserRepository.findDTO();
        return list;
    }

    @GetMapping("/native")
    public List<SysUserDTO> nativeList(){

        return sysUserRepository.findAllDTO();
    }

    @GetMapping("/native1")
    public List<Map<String, Object>> nativeList1(){

        return sysUserRepository.findList();
    }

    @GetMapping("/native2")
    public Map<String, Object> nativeList2(){

        return sysUserRepository.findMap();
    }
}
