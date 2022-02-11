package com.project.entity;

import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class ObjAttr {

    private int objAttrId;

    private int objId;

    private Attribute attribute;

    private String value;

    @Override
    public String toString() {
        return "ObjAttr{" +
                "objAttrId=" + objAttrId +
                ", objId=" + objId +
                ", attribute=" + attribute +
                ", value='" + value + '\'' +
                '}';
    }
}
