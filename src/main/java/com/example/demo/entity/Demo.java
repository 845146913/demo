package com.example.demo.entity;

import com.silencew.plugins.jpaenums.BaseEnum;
import com.silencew.plugins.jpaenums.BaseEnumConverter;
import com.silencew.plugins.jpaenums.JsonEnumDeserialze;

/**
 * Created by IntelliJ IDEA.
 * author: wangshuiping
 * date: 2021/1/19
 */
public class Demo {
    private Long id;

    @JsonEnumDeserialze
    private Gender gender;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Gender getGender() {
        return gender;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }

    @Override
    public String toString() {
        return "Demo{" +
                "id=" + id +
                ", gender=" + gender +
                '}';
    }

    public enum Gender implements BaseEnum<Gender, Integer> {
        MAN(1);
        private int code;

        Gender(int code) {
            this.code = code;
        }

        @Override
        public Integer getCode() {
            return this.code;
        }
        public class GenderConvert extends BaseEnumConverter<Gender, Integer> {

        }
    }
}
