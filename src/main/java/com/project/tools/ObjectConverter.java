package com.project.tools;

import com.project.entity.AttrEnum;
import com.project.entity.Obj;
import com.project.entity.ObjAttr;

import java.util.*;

public class ObjectConverter {

    public static Map<Integer, String> convertObject(Obj obj) {
        Map<Integer,String> result = new HashMap<>();
        for(ObjAttr objAttr: obj.getObjAttrs()) {
            result.put(objAttr.getAttribute().getAttrId(), objAttr.getValue());
        }
        result.put(-1, Integer.toString(obj.getObjId()));
        return result;
    }

    public static List<Map<Integer, String>> convertListOfObjects(List<Obj> list) {
        List<Map<Integer, String>> result = new ArrayList<>();
        for (Obj obj: list) {
            Map<Integer, String> mappedObj = convertObject(obj);
            result.add(mappedObj);
        }
        return result;
    }

    public static Map<String, Integer> convertObjectToEtcAttrNames(Obj obj) {
        Map<String, Integer> result = new HashMap<>();
        for(ObjAttr objAttr: obj.getObjAttrs()) {
            if (objAttr.getAttribute().getAttrId() > AttributeTool.PRIMARY_ATTRIBUTE_ID_SPACE) {
                result.put( objAttr.getAttribute().getAttrName(),objAttr.getObjAttrId());
            }
        }
        return result;
    }

    public static boolean validateMappedObject(Map<Integer, String> mappedObj) {
        List<String> attrValues =new ArrayList<>();
        attrValues.add(mappedObj.get(AttrEnum.COURSE_NAME.getValue()));
        attrValues.add(mappedObj.get(AttrEnum.COURSE_DESCRIPTION.getValue()));
        for (String attrValue: attrValues) {
            if (attrValue == null || attrValue.length() <= 2 || attrValue.length() > 250) {
                return false;
            }
        }
        return true;
    }

    public static int getIdFromMappedObj(Map<Integer, String> mappedObj) {
        return Integer.parseInt(mappedObj.get(-1));
    }

}
