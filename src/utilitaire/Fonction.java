/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package utilitaire;

import annotation.Column;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Vector;

/**
 *
 * @author Christian
 */
public class Fonction {
    
    public static Vector<Field> getAnnotedField(Class cls){
        Field[] field = cls.getDeclaredFields();
        Vector<Field> vect = new Vector<>();
        for (Field fd : field) {
            if(fd.isAnnotationPresent(Column.class)){
                vect.add(fd);
            }
        }
        return vect;
    }
    public static void setObject(Class cls,ResultSet resultset,Vector<Object> vect)throws Exception{
        Vector<Field> fieldAnnoted = Fonction.getAnnotedField(cls);
        while (resultset.next()) {
        Object o = cls.newInstance();
            for (Field fd : fieldAnnoted) {
                Method meth = cls.getMethod("set"+capitalize(fd.getName()),fd.getType());
                System.out.println("retooo    "+resultset.getObject(fd.getAnnotation(Column.class).name()));
                meth.invoke(o,resultset.getObject(fd.getAnnotation(Column.class).name()));
            }
            vect.add(o);
        }
    }
    
     public static String getObjectInfo(Class cls,Object object)throws Exception {
        String sb = "";
        Field[] fields = cls.getDeclaredFields();

        for (Field field : fields) {
            String fieldName = field.getName();
            String capitalizedFieldName = fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);

                Method method = cls.getMethod("get" + capitalizedFieldName);
                
                Object value = method.invoke(object);
                
                if (isDefaultValue(field.getType(),value)) {
                    if(sb.equals("")){
                        sb = field.getAnnotation(Column.class).name() + " = '" + value.toString() + "'";
                    }else{
                        sb = sb + " and "+ field.getAnnotation(Column.class).name() + " = '" + value.toString() + "'";
                    }
                }
        }

        return sb;
    }
     private static boolean isDefaultValue(Class<?> fieldType, Object value) {
        if (fieldType == boolean.class) {
            return (boolean) value != false;
        } else if (fieldType == byte.class) {
            return (byte) value != 0;
        } else if (fieldType == short.class) {
            return (short) value != 0;
        } else if (fieldType == int.class) {
            return (int) value != 0;
        } else if (fieldType == long.class) {
            return (long) value != 0L;
        } else if (fieldType == float.class) {
            return (float) value != 0.0f;
        } else if (fieldType == double.class) {
            return (double) value != 0.0;
        } else if (fieldType == java.lang.String.class) {
            return  value != null;
        }
        
        return false;
    }
     public static String capitalize(String str) {
        if (str == null || str.isEmpty()) {
            return str;
        }
        return str.substring(0, 1).toUpperCase() + str.substring(1);
    }
     
    public static String countFieldValues(Class cls,Object object)throws Exception{
        
        String col = "";
        String values = "";
        String request = "";
        Field[] fields = cls.getDeclaredFields();

        for (Field field : fields) {
            String fieldName = field.getName();
            String capitalizedFieldName = fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);

                Method method = cls.getMethod("get" + capitalizedFieldName);
                
                Object val = method.invoke(object);
                
                if (isDefaultValue(field.getType(),val)) {
                    request = request + "," + " ? ";
                    col = col +","+field.getAnnotation(Column.class).name();
                }/*else if(!field.getAnnotation(Column.class).value().equals("")){
                    col = col +","+field.getAnnotation(Column.class).name();
                    request = request + "," + field.getAnnotation(Column.class).value();
                }*/
        }
        String rep = col.substring(1) + "//" + request.substring(1) ;

        return rep;
    }
    public static void setObjectInsert(PreparedStatement stmt,Class cls,Object obj)throws Exception{
        Field[] fields = cls.getDeclaredFields();
        int count = 1;
        int isa = 0;
        for (Field field : fields) {
            String fieldName = field.getName();
            String capitalizedFieldName = fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);

                Method method = cls.getMethod("get" + capitalizedFieldName);
                
                Object val = method.invoke(obj);
                
                if (isDefaultValue(field.getType(),val)) {
                    stmt.setObject(count, val);
                    count++;
                }
        }
    }
     
     public static String getInsertObject(Class cls,Object object)throws Exception {
        String sb = "";
        Field[] fields = cls.getDeclaredFields();

        for (Field field : fields) {
            String fieldName = field.getName();
            String capitalizedFieldName = fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);

                Method method = cls.getMethod("get" + capitalizedFieldName);
                
                Object value = method.invoke(object);
                
                if (isDefaultValue(field.getType(),value)) {
                    sb = field.getAnnotation(Column.class).name() + " = '" + value.toString() + "'";
                }
        }

        return sb;
    }
    public static Object castTo(String valeur, Class<?> type) {
        if (type == String.class) {
            return valeur;
        } else if (type == int.class || type == Integer.class) {
            return Integer.parseInt(valeur);
        } else if (type == double.class || type == Double.class) {
            return Double.parseDouble(valeur);
        } else if (type == float.class || type == Float.class) {
            return Float.parseFloat(valeur);
        } else if (type == long.class || type == Long.class) {
            return Long.parseLong(valeur);
        } else if (type == boolean.class || type == Boolean.class) {
            return Boolean.parseBoolean(valeur);
        } else {
            return null;
        }
    }
    public static String concatFieldValues(Class cls,Object object)throws Exception{
        
        String col = "";
        Field[] fields = cls.getDeclaredFields();

        for (Field field : fields) {
            String fieldName = field.getName();
            String capitalizedFieldName = fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);

                Method method = cls.getMethod("get" + capitalizedFieldName);
                
                Object val = method.invoke(object);
                
                if (isDefaultValue(field.getType(),val)) {
                    col = col +", "+field.getAnnotation(Column.class).name() +" = ?";
                }
        }
        String rep = col.substring(1);

        return rep;
    }
    
    public static void setUpdateObject(PreparedStatement stmt,Class cls,Object obj)throws Exception{
        Field[] fields = cls.getDeclaredFields();
        int count = 1;
        for (Field field : fields) {
            String fieldName = field.getName();
            String capitalizedFieldName = fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);

                Method method = cls.getMethod("get" + capitalizedFieldName);
                
                Object val = method.invoke(obj);
                
                if (isDefaultValue(field.getType(),val)) {
                    stmt.setObject(count, val);
                }
                count++;
        }
    }
}
