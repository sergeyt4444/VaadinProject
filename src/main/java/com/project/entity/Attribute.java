package com.project.entity;

import lombok.Getter;
import lombok.Setter;


@Setter
@Getter
public class Attribute {

    private int attrId;

    private String attrName;
    private String attrType;
    private boolean isMultiple;
    private boolean isHidden;


    @Override
    public String toString() {
        return "Attribute{" +
                "attrId=" + attrId +
                ", attrName='" + attrName + '\'' +
                ", attrType='" + attrType + '\'' +
                ", isMultiple=" + isMultiple +
                ", isHidden=" + isHidden +
                '}';
    }
}
