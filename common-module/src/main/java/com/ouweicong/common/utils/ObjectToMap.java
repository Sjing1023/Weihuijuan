package com.ouweicong.common.utils;

import com.ouweicong.common.pojo.System;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

public class ObjectToMap {
    //object有目标类型
    public static Map<String, Object> convertObjectToMap(Object object) {
        Map<String, Object> map = new HashMap<>();
        Class<?> clazz = object.getClass();
        Field[] fields = clazz.getDeclaredFields();
        for (Field field : fields) {
            field.setAccessible(true);
            try {
                Object value = field.get(object);
                map.put(field.getName(), value);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }

        //加入系统字段
        System system = (System) object;
        map.put("create_time",system.getCreate_time());
        map.put("update_time",system.getUpdate_time());
        map.put("sort",system.getSort());

        return map;
    }

    //object有目标类型且不包含空值
    public static Map<String, Object> convertObjectToMap(Object object, Boolean notNull) {
        if (notNull) {
            Map<String, Object> map = convertObjectToMap(object);
            for (Map.Entry item : convertObjectToMap(object).entrySet()) {
                if (item.getValue() == null) {
                    map.remove(item.getKey());
                }
            }

            //加入系统字段
            System system = (System) object;
            map.put("create_time",system.getCreate_time());
            map.put("update_time",system.getUpdate_time());
            map.put("sort",system.getSort());

            return map;
        }
        return convertObjectToMap(object);
    }

    /**
     * 生成目标对象类型的Map
     */
    //object无目标类型
    public static Map<String, Object> convertObjectToMap(Object object, Class tagerCalss) {
        Map entity = (Map) object;
        Map<String, Object> map = new HashMap<>();
        //生成目标Class的字段Map
        Field[] fields = tagerCalss.getDeclaredFields();
        for (Field field : fields) {
            field.setAccessible(true);
            try {
                Object value = entity.get(field.getName());
                map.put(field.getName(), value);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        //加入系统字段
        Field[] systemFields = System.class.getDeclaredFields();
        for (Field systemField : systemFields) {
            systemField.setAccessible(true);
            try {
                Object value = entity.get(systemField.getName());
                map.put(systemField.getName(), value);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return map;
    }
}
