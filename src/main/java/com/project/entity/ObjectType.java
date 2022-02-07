package com.project.entity;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class ObjectType {

    private int objTypesId;

    private String objTypesName;

    private List<Attribute> objTypeAttributes;

    @Override
    public String toString() {
        return "ObjectType{" +
                "obj_types_id=" + objTypesId +
                ", obj_types_name='" + objTypesName + '\'' +
                ", objTypeAttributes=" + objTypeAttributes +
                '}';
    }
}
