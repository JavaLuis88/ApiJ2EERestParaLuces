/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package esmeralda.proyectos.beans;

import java.io.Serializable;

/**
 *
 * @author lingmoy
 */
public class User implements Serializable {

    private int id;
    private String username;
    private String password;
    private String rol;

    public User() {

        this.id = 0;
        this.username = "";
        this.password = "";
        this.rol = "";

    }

    public User(int id, String username, String password, String rol) {

        this.id = id;
        this.username = new String(username);
        this.password = new String(password);
        this.rol = new String(rol);

    }

    public int getId() {
        return this.id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return new String(this.username);
    }

    public void setUsername(String username) {
        this.username = new String(username);
    }

    public String getPassword() {
        return new String(this.password);
    }

    public void setPassword(String password) {
        this.password = new String(password);
    }

    public String getRol() {
        return new String(this.rol);
    }

    public void setRol(String rol) {
        this.rol = new String(rol);
    }

}
