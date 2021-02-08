package com.example.demo.controller;

import com.example.demo.dto.SysUserDTO;
import com.example.demo.entity.Department;
import com.example.demo.repository.DepartmentRepository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Stream;

@RestController
@RequestMapping("/d")
public class DepartmentController {
    @Resource
    private DepartmentRepository departmentRepository;

    @PostConstruct
    public void init(){

        Stream.iterate(0L, x->x+1)
                .limit(10L)
                .forEach(i -> {
                    Department d = new Department();
                    d.setId(i+1);
                    d.setName("D"+i);
                    departmentRepository.save(d);
                });
    }


    @GetMapping
    public String find() {
        List<SysUserDTO> r = departmentRepository.findDTO(((root, query, cb) -> cb.equal(root.get("id"), 1L)));
        System.out.println(r);
        return "ok";
    }
}
