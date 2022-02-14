package com.project.entity;

public enum ObjectTypeEnum {
    COURSE(1);

    private final int objTypeId;

    private ObjectTypeEnum(int objTypeId) {
        this.objTypeId = objTypeId;
    }

    public int getValue() {
        return objTypeId;
    }


}
