package com.example.demo.dto;

import com.silencew.plugins.jpaenums.BaseEnum;
import com.silencew.plugins.jpaenums.BaseEnumConverter;

import javax.persistence.Convert;

/**
 * Created by IntelliJ IDEA.
 * author: wangshuiping
 * date: 2021/1/20
 */
public class SysUserDTO {

    private Long id;
    private String name;

    private TypeEnum type;


    public SysUserDTO() {
    }

    public SysUserDTO(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public enum TypeEnum implements BaseEnum<TypeEnum, Integer> {
        A(0),
        B(1);
        private int code;

        TypeEnum(int code) {
            this.code = code;
        }

        @Override
        public Integer getCode() {
            return this.code;
        }

        public static class TypeEnumConverter extends BaseEnumConverter<TypeEnum, Integer> {

        }
    }
    public TypeEnum getType() {
        return type;
    }

    @Convert(converter = TypeEnum.TypeEnumConverter.class)
    public void setType(TypeEnum type) {
        this.type = type;
    }
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
