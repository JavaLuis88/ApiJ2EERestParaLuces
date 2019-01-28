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
public class RestOperation implements Serializable {

    private int operationstatus;
    private String token;
    private int responsetype;
    private String[] info;
    public final static int OPERATIONSTATUS_SUCCESS = 0;
    public final static int OPERATIONSTATUS_AUTHERROR = 1;
    public final static int OPERATIONSTATUS_TOKENERROR = 2;
    public final static int RESPONSETYPE_AUTHSERVER = 0;
    public final static int RESPONSETYPE_AUTHCLIENT = 1;
    public final static int RESPONSETYPE_OPERATION = 2;    
    public RestOperation() {

        this.operationstatus = 0;
        this.token = "";
        this.responsetype = 0;
        this.info = null;

    }

    public RestOperation(int operationstatus, String token, int responsetype, String[] info) {

        String[] restinfo;
        restinfo = null;

        this.operationstatus = operationstatus;
        this.token = new String(token);
        this.responsetype = responsetype;
        if (info != null) {

            restinfo = new String[info.length];
            for (int i = 0; i < info.length; i++) {

                restinfo[i] = new String(info[i]);

            }

        }

        this.info = restinfo;

    }

    public int getOperationstatus() {
        return operationstatus;
    }

    public void setOperationstatus(int operationstatus) {
        this.operationstatus = operationstatus;
    }

    public String getToken() {
        return this.token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public int getResponsetype() {
        return this.responsetype;
    }

    public void setResponsetype(int responsetype) {
        this.responsetype = responsetype;
    }

    /**
     * @return the info
     */
    public String[] getInfo() {

        String[] restinfo;
        restinfo = null;

        if (this.info != null) {

            restinfo = new String[this.info.length];
            for (int i = 0; i < this.info.length; i++) {

                restinfo[i] = new String(this.info[i]);

            }

        }

        return restinfo;
    }

    public void setInfo(String[] info) {

        String[] restinfo;
        restinfo=null;
        long tt=info.length;

        if (info != null) {

            restinfo = new String[info.length];
            for (int i = 0; i <info.length; i++) {

                restinfo[i] = new String(info[i]);

            }

        }

        this.info=restinfo;
    }

}
