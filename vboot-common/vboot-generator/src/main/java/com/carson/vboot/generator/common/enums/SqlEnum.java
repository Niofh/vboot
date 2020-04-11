package com.carson.vboot.generator.common.enums;

import lombok.Getter;

import java.util.HashMap;

@Getter
public enum SqlEnum {

    eq(1, "=","eq"),
    ge(2, ">=","ge"),
    le(3, "<=","le"),
    ne(4, "!=","ne"),
    like(5, "like","like");

    private Integer id;
    private String name;
    private String alias;

    SqlEnum(Integer id, String name,String alias) {
        this.id = id;
        this.name = name;
        this.alias = alias;
    }

    public static HashMap<String,Integer> getObject(){
        HashMap<String,Integer> map = new HashMap<>();
        for (SqlEnum sqlEnum : SqlEnum.values()) {
            map.put(sqlEnum.getAlias(),sqlEnum.getId());
        }
        return map;
    }
}
