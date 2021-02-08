package com.example.demo.entity;

import com.example.demo.annotation.JsonDeserializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.silencew.plugins.jpaenums.BaseEnum;
import com.silencew.plugins.jpaenums.BaseEnumConverter;
import com.silencew.plugins.jpaenums.JsonEnumDeserializer;
import com.silencew.plugins.jpaenums.JsonEnumDeserialze;

import java.io.Serializable;

/**
 * Created by IntelliJ IDEA.
 * author: wangshuiping
 * date: 2021/1/19
 */
public class Demo {
    private Long id;

//    @JsonDeserializer
//    @JsonDeserialize(
//            using = JsonEnumDeserializer.class
//    )
    private Gender gender;

    private WindowType winType;

    private OsEnum os;

    public WindowType getWinType() {
        return winType;
    }

    public OsEnum getOs() {
        return os;
    }

    public void setOs(OsEnum os) {
        this.os = os;
    }

    public void setWinType(WindowType winType) {
        this.winType = winType;
    }

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
                ", winType=" + winType +
                ", os=" + os +
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

    public enum OsEnum implements Serializable {
        A,
        B;
    }
    public enum WindowType {
        LINUX(1),
        WINDOWS(2);
        private int code;
        WindowType(int code) {
            this.code = code;
        }
        public int getCode() {
            return this.code;
        }
    }
}
