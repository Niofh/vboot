package com.carson.vboot.generator.common.enums;

import lombok.Getter;

import java.util.HashMap;

@Getter
public enum FormEnum {
    INPUT(1, "输入框","INPUT"),
    TEXTAREA(2, "文本框","TEXTAREA"),
    SELECT(3, "下拉框","SELECT"),
    DATE(4, "日期框","DATE"),
    RADIO(5, "单选框","RADIO"),
    CHECKBOX(6, "多选框","CHECKBOX"),
    INPUTNUMBER(7, "计数器","INPUTNUMBER");
    private Integer id;
    private String name;
    private String alias;

    FormEnum(Integer id, String name,String alias) {
        this.id = id;
        this.name = name;
        this.alias = alias;
    }

    public static HashMap<String,Integer> getObject(){
      HashMap<String,Integer> map = new HashMap<>();
      for (FormEnum formEnum : FormEnum.values()) {
        map.put(formEnum.getAlias(),formEnum.getId());
      }
      return map;
    }

}
