package com.carson.vboot.core.common.enums;

import lombok.Getter;

@Getter
public enum FileEnums {

    MINIO(1,"MINIO对象存储"),
    ALI_SSO(2,"阿里对象存储");

    private Integer id;
    private String name;

     FileEnums(Integer id, String name) {
        this.id = id;
        this.name = name;
    }
}
