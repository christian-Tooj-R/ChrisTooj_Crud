/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package crud;

import annotation.Column;
import annotation.Table;
import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;
import util.Fonction;

/**
 *
 * @author Christian
 */
public class Generalisation {
    /*********SELECT**********/
    public Object[] select(Connection connection,String orderby) throws Exception{
        
        String query = "SELECT * FROM "+ this.getClass().getAnnotation(Table.class).name();
        String where = Fonction.getObjectInfo(this.getClass(),this);
        if(!where.equals("")){
            query = query + " where "+where;
        }
        if(!orderby.equals("")){
            query = query + " order by "+orderby;
        }
        
        System.out.println(query);
        PreparedStatement statement = connection.prepareStatement(query);
        ResultSet resultSet = statement.executeQuery();
        
        Vector<Object> vect = new Vector<>();
        Fonction.setObject(this.getClass(), resultSet, vect);
        
        connection.close();
        return vect.toArray(new Object[vect.size()]);
    }
    
    
    /*********INSERT**********/
    public void insert(Connection conn) throws Exception{
        conn.setAutoCommit(false);
        String req = Fonction.countFieldValues(this.getClass(), this);
        String[] data = req.split("//");
        String query = "INSERT INTO "+ this.getClass().getAnnotation(Table.class).name() +"(" + data[0] + ")" +" VALUES ("+data[1]+")";
        
        System.out.println(query);
        PreparedStatement statement = conn.prepareStatement(query);
        Fonction.setObjectInsert(statement, this.getClass(), this);
       
            
        statement.executeUpdate();
            
        System.out.println("Insertion réussie !");
        conn.commit();
        conn.close();
    }
    
    /*********UPDATE**********/
    public void update(Connection conn,String where,String values)throws Exception {
        conn.setAutoCommit(false);
        String req = Fonction.concatFieldValues(this.getClass(),this);
        String query = "UPDATE "+this.getClass().getAnnotation(Table.class).name()+" SET "+ req +" WHERE " + where + " = '"+values+"'";
        System.out.println(query);
        PreparedStatement statement = conn.prepareStatement(query);
        Fonction.setUpdateObject(statement,this.getClass(), this);
        statement.executeUpdate();
            
        System.out.println("Mise à jour réussie !");
        conn.commit();
        conn.close();
            
    }
    
    /*********DELETE**********/
    public void delete(Connection conn)throws Exception {
        conn.setAutoCommit(false);
        String where = Fonction.getObjectInfo(this.getClass(),this);
        String query = "DELETE from "+this.getClass().getAnnotation(Table.class).name();
        
        if(!where.equals("")){
            query = query + " WHERE "+where;
        }
        
        System.out.println(query);
        PreparedStatement statement = conn.prepareStatement(query);
        statement.executeUpdate();
            
        System.out.println("Effacement réussie !");
        conn.commit();
        conn.close();
    }
    
    
    public void print(Object[] obj) throws Exception {
        Field[] field = obj[0].getClass().getDeclaredFields();
        for (int i = 0; i < obj.length; i++) {
            for (int j = 0; j < field.length; j++) {
                System.out.print(obj[i].getClass().getMethod("get" + Fonction.capitalize(field[j].getName())).invoke(obj[i]) + "\t");
            }
            System.out.print("\n");
        }
    }
    
}
