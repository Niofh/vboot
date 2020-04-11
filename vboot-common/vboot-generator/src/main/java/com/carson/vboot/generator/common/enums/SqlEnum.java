package com.carson.vboot.generator.common.enums;

import lombok.Getter;

import java.util.HashMap;

@Getter
public enum SqlEnum {

    DENGYU(1, "=","DENGYU"),
    DAYU(2, ">=","DAYU"),
    XIAOYU(3, "<=>","XIAOYU"),
    BUDENGYU(4, "!=","BUDENGYU"),
    LIKE(5, "Like","LIKE"),
    NOTNULL(6, "NOTNULL","NOTNULL"),
    BETWEEN(7, "BETWEEN","BETWEEN");

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
