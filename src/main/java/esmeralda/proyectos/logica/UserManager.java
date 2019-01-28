/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package esmeralda.proyectos.logica;
import esmeralda.proyectos.beans.User;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

/**
 *
 * @author lingmoy
 */
public class UserManager {

    private String databaseconection;

    public UserManager() {

        this.databaseconection = null;

    }

    public UserManager(String databaseconection) {

        this.databaseconection = new String(databaseconection);

    }

    public void setDatabaseconection(String databaseconection) {

        this.databaseconection = new String(databaseconection);

    }

    
    
    public String getDatabaseconection() {

        return new String(this.databaseconection);

    }

    
    public User getUserByUserName(String username) throws NamingException, SQLException {
        
        int id;
        String password;
        String rol;
        String sql;             
        InitialContext initContext;
        DataSource ds;
        User retval;
        
        retval=null;
        
       
        
   
       
        initContext = new InitialContext();
        ds = (DataSource) initContext.lookup(this.databaseconection);
        try (Connection cn = ds.getConnection()) {//try1

            try (PreparedStatement st = cn.prepareStatement("SELECT `id`, `username`, `password`, `rol` FROM `users` WHERE `username`=?")) {//try2

                st.setString(1, username);
                try (ResultSet rs = st.executeQuery()) {//try3
                    
                    if (rs.next()==true) {//if1
                        
                           id=rs.getInt("id");
                           password=rs.getString("password");
                           rol=rs.getString("rol");
                           retval=new User(id,username,password,rol);
                           
                        
                    }//if1
                
                }//try3
  
            }//try2
  
        }//try1

        
        
        
        
        return retval;
        
    } 
    
    
}
