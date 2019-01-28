/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package esmeralda.proyectos.api;

//import com.sun.xml.internal.bind.v2.runtime.reflect.opt.Const;
import esmeralda.proyectos.exceptions.OperationNotWorkException;
import esmeralda.proyectos.exceptions.PinNumberNovalidException;
import com.sun.javafx.tk.Toolkit;
import esmeralda.proyectos.beans.RestOperation;
import esmeralda.proyectos.exceptions.ServerException;
import esmeralda.proyectos.logica.UserManager;
import esmeralda.proyectos.logica.HMACSHA;
import esmeralda.proyectos.beans.User;
import esmeralda.proyectos.logica.Consts;
import esmeralda.proyectos.logica.DigitalOutput;
import esmeralda.proyectos.logica.DigitalOutputTest;
import esmeralda.proyectos.logica.IDigitalOuput;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import java.io.BufferedReader;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.annotation.Annotation;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.Properties;
import java.util.Date;
import javafx.scene.chart.PieChart;
import javax.naming.InitialContext;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Context;
import javax.servlet.ServletContext;
import javax.servlet.jsp.jstl.core.Config;
import javax.sql.DataSource;
import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.POST;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

/**
 *
 * @author lingmoy
 */
public class Luces implements ILuces {

    @Context
    ServletContext context;

    public Luces() {
    }

    @Override
    public String prueba() {

        return "Prueba2";
    }

    @Override
    public RestOperation authServer(String username, String handshask) throws Exception {

        Properties configfile;
        String secret;
        UserManager usermanager;
        User user;
        String[] info;
        JwtBuilder jwtbuider;
        long milliseconds;
        RestOperation retval;

        secret = "";

        retval = null;

        retval = new RestOperation();
        retval.setOperationstatus(RestOperation.OPERATIONSTATUS_SUCCESS);
        retval.setResponsetype(RestOperation.RESPONSETYPE_AUTHSERVER);
        configfile = new Properties();

        try {//try1

            configfile.load(context.getResourceAsStream("/WEB-INF/configuration.properties"));
            verifyserverconfig(configfile);
            secret = configfile.getProperty("secret");

            if (username == null || username.trim().equals("") == true || handshask == null || handshask.trim().equals("") == true) {//if1

                retval.setOperationstatus(RestOperation.OPERATIONSTATUS_AUTHERROR);

            }//if1
            else {//else1

                usermanager = new UserManager("java:/comp/env/conexionluces");
                user = usermanager.getUserByUserName(username);

                if (user != null) {//if2

                    info = new String[2];
                    info[0] = (new HMACSHA()).encriptHMACSHA256(handshask, user.getPassword());
                    info[1] = this.generateserverhanshask();
                    retval.setInfo(info);
                    jwtbuider = Jwts.builder();
                    jwtbuider.setSubject("authserver");
                    jwtbuider.setIssuer(username);
                    jwtbuider = jwtbuider.claim("serverhandshask", info[1]);
                    milliseconds = (new Date()).getTime();

                    jwtbuider.setIssuedAt(new Date(milliseconds));
                    milliseconds = milliseconds + (5 * 60 * 1000);
                    jwtbuider.setExpiration(new Date(milliseconds));
                    jwtbuider = jwtbuider.signWith(SignatureAlgorithm.HS256, secret.getBytes("UTF-8"));
                    retval.setToken(jwtbuider.compact());

                }//if2
                else {//else2

                    retval.setOperationstatus(RestOperation.OPERATIONSTATUS_AUTHERROR);

                }//else2

            }//else1

        }//try1
        catch (Exception e) {//catch1-1
            throw new ServerException("Server Exccepton",e);
        }//catch1-1

        return retval;
    }

    @Override
    public RestOperation authClient(String handshaskresponse, String token) throws Exception {

        Properties configfile;
        String secret;
        UserManager usermanager;
        String username;
        User user;
        JwtBuilder jwtbuider;
        Jws<Claims> claims;
        long milliseconds;

        RestOperation retval;

        secret = "";

        retval = null;

        retval = new RestOperation();
        retval.setOperationstatus(RestOperation.OPERATIONSTATUS_SUCCESS);
        retval.setResponsetype(RestOperation.RESPONSETYPE_AUTHCLIENT);
        configfile = new Properties();

        try {//try1

            configfile.load(context.getResourceAsStream("/WEB-INF/configuration.properties"));
            verifyserverconfig(configfile);
            secret = configfile.getProperty("secret");

            if (handshaskresponse == null || handshaskresponse.trim().equals("") == true || token == null || token.trim().equals("") == true) {//if1
                retval.setOperationstatus(RestOperation.OPERATIONSTATUS_AUTHERROR);
                retval.setToken(token);

            }//if1
            else {//else1

                claims = verifyclaims(secret, token, (byte) 1);

                if (claims == null) {//if2
                    retval.setOperationstatus(RestOperation.OPERATIONSTATUS_TOKENERROR);
                }//if2
                else {//else2

                    username = claims.getBody().getIssuer();

                    usermanager = new UserManager("java:/comp/env/conexionluces");
                    user = usermanager.getUserByUserName(username);
                    if (user != null && validatehandshak(claims.getBody().get("serverhandshask").toString(), user.getPassword(), handshaskresponse) == true) {//if3

                        jwtbuider = Jwts.builder();
                        jwtbuider.setSubject("authclient");
                        jwtbuider.setIssuer(username);
                        jwtbuider = jwtbuider.claim("clienthandshask", claims.getBody().get("serverhandshask").toString());
                        jwtbuider = jwtbuider.claim("clienthandshaskresponse", handshaskresponse);
                        milliseconds = (new Date()).getTime();

                        jwtbuider.setIssuedAt(new Date(milliseconds));
                        milliseconds = milliseconds + (5 * 60 * 1000);
                        jwtbuider.setExpiration(new Date(milliseconds));
                        jwtbuider = jwtbuider.signWith(SignatureAlgorithm.HS256, secret.getBytes("UTF-8"));
                        retval.setToken(jwtbuider.compact());

                    }//if3
                    else {//else3

                        retval.setOperationstatus(RestOperation.OPERATIONSTATUS_AUTHERROR);
                        retval.setToken(token);

                    }//else3

                }//else2

            }//else1

        }//try1
        catch (Exception e) {//catch1-1
            throw new ServerException("Server Exccepton",e);
        }//catch1-1

        return retval;

    }

    @Override
    public RestOperation ligthOn(String token) throws Exception {//ligthOn

        Properties configfile;
        String secret;
        UserManager usermanager;
        String username;
        User user;
        JwtBuilder jwtbuider;
        Jws<Claims> claims;
        long milliseconds;
        int pinnumber;
        String[] info;
        RestOperation retval;

        secret = "";

        retval = null;

        retval = new RestOperation();
        retval.setOperationstatus(RestOperation.OPERATIONSTATUS_SUCCESS);
        retval.setResponsetype(RestOperation.RESPONSETYPE_OPERATION);
        configfile = new Properties();

        try {//try1

            configfile.load(context.getResourceAsStream("/WEB-INF/configuration.properties"));
            verifyserverconfig(configfile);
            secret = configfile.getProperty("secret");
            pinnumber = Integer.parseInt(configfile.getProperty("pinnumber"), 10);

            if (token == null || token.trim().equals("") == true) {//if1
                retval.setOperationstatus(RestOperation.OPERATIONSTATUS_AUTHERROR);
                retval.setToken(token);

            }//if1
            else {//else1

                claims = verifyclaims(secret, token, (byte) 2);

                if (claims == null) {//if2
                    retval.setOperationstatus(RestOperation.OPERATIONSTATUS_TOKENERROR);
                }//if2
                else {//else2

                    username = claims.getBody().getIssuer();

                    usermanager = new UserManager("java:/comp/env/conexionluces");
                    user = usermanager.getUserByUserName(username);
                    if (user != null && validatehandshak(claims.getBody().get("clienthandshask").toString(), user.getPassword(), claims.getBody().get("clienthandshaskresponse").toString()) == true) {//if3

                        info = new String[1];

                        if (switchpin(pinnumber, true) == true) {//if4

                            info[0] = "true";

                        }//if4 
                        else {//else4

                            info[0] = "false";

                        }//else4

                        retval.setInfo(info);
                        jwtbuider = Jwts.builder();
                        jwtbuider.setSubject("authclient");
                        jwtbuider.setIssuer(username);
                        jwtbuider = jwtbuider.claim("clienthandshask", claims.getBody().get("clienthandshask").toString());
                        jwtbuider = jwtbuider.claim("clienthandshaskresponse", claims.getBody().get("clienthandshaskresponse").toString());
                        milliseconds = (new Date()).getTime();

                        jwtbuider.setIssuedAt(new Date(milliseconds));
                        milliseconds = milliseconds + (5 * 60 * 1000);
                        jwtbuider.setExpiration(new Date(milliseconds));
                        jwtbuider = jwtbuider.signWith(SignatureAlgorithm.HS256, secret.getBytes("UTF-8"));
                        retval.setToken(jwtbuider.compact());

                    }//if3
                    else {//else3

                        retval.setOperationstatus(RestOperation.OPERATIONSTATUS_AUTHERROR);
                        retval.setToken(token);

                    }//else3

                }//else2

            }//else1

        }//try1
        catch (Exception e) {//catch1-1
            throw new ServerException("Server Exccepton",e);
        }//catch1-1

        return retval;

    }//ligthOn

    @Override
    public RestOperation ligthOff(String token) throws Exception {//ligthOff

        Properties configfile;
        String secret;
        UserManager usermanager;
        String username;
        User user;
        JwtBuilder jwtbuider;
        Jws<Claims> claims;
        long milliseconds;
        int pinnumber;
        String[] info;
        RestOperation retval;

        secret = "";

        retval = null;

        retval = new RestOperation();
        retval.setOperationstatus(RestOperation.OPERATIONSTATUS_SUCCESS);
        retval.setResponsetype(RestOperation.RESPONSETYPE_OPERATION);
        configfile = new Properties();

        try {//try1

            configfile.load(context.getResourceAsStream("/WEB-INF/configuration.properties"));
            verifyserverconfig(configfile);
            secret = configfile.getProperty("secret");
            pinnumber = Integer.parseInt(configfile.getProperty("pinnumber"), 10);

            if (token == null || token.trim().equals("") == true) {//if1
                retval.setOperationstatus(RestOperation.OPERATIONSTATUS_AUTHERROR);
                retval.setToken(token);

            }//if1
            else {//else1

                claims = verifyclaims(secret, token, (byte) 2);

                if (claims == null) {//if2
                    retval.setOperationstatus(RestOperation.OPERATIONSTATUS_TOKENERROR);
                }//if2
                else {//else2

                    username = claims.getBody().getIssuer();

                    usermanager = new UserManager("java:/comp/env/conexionluces");
                    user = usermanager.getUserByUserName(username);
                    if (user != null && validatehandshak(claims.getBody().get("clienthandshask").toString(), user.getPassword(), claims.getBody().get("clienthandshaskresponse").toString()) == true) {//if3

                        info = new String[1];

                        if (switchpin(pinnumber, false) == true) {//if4

                            info[0] = "true";

                        }//if4 
                        else {//else4

                            info[0] = "false";

                        }//else4
                        retval.setInfo(info);
                        jwtbuider = Jwts.builder();
                        jwtbuider.setSubject("authclient");
                        jwtbuider.setIssuer(username);
                        jwtbuider = jwtbuider.claim("clienthandshask", claims.getBody().get("clienthandshask").toString());
                        jwtbuider = jwtbuider.claim("clienthandshaskresponse", claims.getBody().get("clienthandshaskresponse").toString());
                        milliseconds = (new Date()).getTime();

                        jwtbuider.setIssuedAt(new Date(milliseconds));
                        milliseconds = milliseconds + (5 * 60 * 1000);
                        jwtbuider.setExpiration(new Date(milliseconds));
                        jwtbuider = jwtbuider.signWith(SignatureAlgorithm.HS256, secret.getBytes("UTF-8"));
                        retval.setToken(jwtbuider.compact());

                    }//if3
                    else {//else3

                        retval.setOperationstatus(RestOperation.OPERATIONSTATUS_AUTHERROR);
                        retval.setToken(token);

                    }//else3

                }//else2

            }//else1

        }//try1
        catch (Exception e) {//catch1-1
            throw new ServerException("Server Exccepton",e);
        }//catch1-1

        return retval;

    }//ligthOff

    @Override
    public RestOperation getStatus(String token) throws Exception {//getStatus

        Properties configfile;
        String secret;
        UserManager usermanager;
        String username;
        User user;
        JwtBuilder jwtbuider;
        Jws<Claims> claims;
        long milliseconds;
        int pinnumber;
        String[] info;
        RestOperation retval;

        secret = "";

        retval = null;

        retval = new RestOperation();
        retval.setOperationstatus(RestOperation.OPERATIONSTATUS_SUCCESS);
        retval.setResponsetype(RestOperation.RESPONSETYPE_OPERATION);
        configfile = new Properties();

        try {//try1

            configfile.load(context.getResourceAsStream("/WEB-INF/configuration.properties"));
            verifyserverconfig(configfile);
            secret = configfile.getProperty("secret");
            pinnumber = Integer.parseInt(configfile.getProperty("pinnumber"), 10);

            if (token == null || token.trim().equals("") == true) {//if1
                retval.setOperationstatus(RestOperation.OPERATIONSTATUS_AUTHERROR);
                retval.setToken(token);

            }//if1
            else {//else1

                claims = verifyclaims(secret, token, (byte) 2);

                if (claims == null) {//if2
                    retval.setOperationstatus(RestOperation.OPERATIONSTATUS_TOKENERROR);
                }//if2
                else {//else2

                    username = claims.getBody().getIssuer();

                    usermanager = new UserManager("java:/comp/env/conexionluces");
                    user = usermanager.getUserByUserName(username);
                    if (user != null && validatehandshak(claims.getBody().get("clienthandshask").toString(), user.getPassword(), claims.getBody().get("clienthandshaskresponse").toString()) == true) {//if3

                        info = new String[1];

                        info[0] = String.valueOf(this.pinstatus(pinnumber));

                        retval.setInfo(info);
                        jwtbuider = Jwts.builder();
                        jwtbuider.setSubject("authclient");
                        jwtbuider.setIssuer(username);
                        jwtbuider = jwtbuider.claim("clienthandshask", claims.getBody().get("clienthandshask").toString());
                        jwtbuider = jwtbuider.claim("clienthandshaskresponse", claims.getBody().get("clienthandshaskresponse").toString());
                        milliseconds = (new Date()).getTime();

                        jwtbuider.setIssuedAt(new Date(milliseconds));
                        milliseconds = milliseconds + (5 * 60 * 1000);
                        jwtbuider.setExpiration(new Date(milliseconds));
                        jwtbuider = jwtbuider.signWith(SignatureAlgorithm.HS256, secret.getBytes("UTF-8"));
                        retval.setToken(jwtbuider.compact());

                    }//if3
                    else {//else3

                        retval.setOperationstatus(RestOperation.OPERATIONSTATUS_AUTHERROR);
                        retval.setToken(token);

                    }//else3

                }//else2

            }//else1

        }//try1
        catch (Exception e) {//catch1-1
             throw new ServerException("Server Exccepton",e);
        }//catch1-1

        return retval;

    }//getStatus

    private String generateserverhanshask() {

        long size;
        long position;
        String characters = "ABCDEFGHIJKMNOPQRSTUVWXYZabcdefghijkmnopqrstuvwxyz01023456789_.=";
        String retval;

        retval = "";
        size = Math.round(Math.random() * (40 - 15)) + 15;

        for (int i = 1; i <= size; i++) {
            position = Math.round(Math.random() * (characters.length() - 1));
            retval = retval + characters.charAt((int) position);

        }

        return retval;
    }

    private void verifyserverconfig(Properties configfile) throws ServerException {
        int pinnumber;

        if (configfile.getProperty("secret") == null || configfile.getProperty("secret").trim().equals("") == true) {

            throw new ServerException("server secret is no specified");

        }
        try {
            pinnumber = Integer.parseInt(configfile.getProperty("pinnumber"), 10);

        } catch (Exception e) {

            throw new ServerException("pin number in not valid");

        }

        if (pinnumber < 1 || pinnumber > 18) {

            throw new ServerException("pin number in not valid");

        }
    }

    private boolean validatehandshak(String plaintext, String key, String digesttext) throws Exception {

        return (new HMACSHA()).encriptHMACSHA256(plaintext, key).equals(digesttext);

    }

    private Jws<Claims> verifyclaims(String secret, String token, byte operation) throws Exception {

        JwtBuilder jwtbuider;
        Jws<Claims> claims;

        claims = null;

        try {//try1
            claims = Jwts.parser().setSigningKey(secret.getBytes("UTF-8")).parseClaimsJws(token);
            if (claims.getBody().getSubject() == null || claims.getBody().getSubject().trim().equals("") == true || claims.getBody().getIssuer() == null || claims.getBody().getIssuer().trim().equals("") == true) {//if1

                claims = null;

            }//if
            else if (operation == 1 && (claims.getBody().getSubject().equals("authserver") == false || claims.getBody().get("serverhandshask") == null || claims.getBody().get("serverhandshask").toString().equals("") == true)) {

                claims = null;

            } else if (operation == 2 && (claims.getBody().getSubject().equals("authclient") == false || claims.getBody().get("clienthandshask") == null || claims.getBody().get("clienthandshask").toString().equals("") == true || claims.getBody().get("clienthandshaskresponse") == null || claims.getBody().get("clienthandshaskresponse").toString().equals("") == true)) {

                claims = null;

            }

        }//try1
        catch (Exception e) {//catch1-1

            claims = null;

        }//catch1-1

        return claims;

    }

    private boolean switchpin(int pinnumber, boolean ligthon) throws PinNumberNovalidException, IOException {//switchpin

        IDigitalOuput pin;

        boolean retval;
        retval = true;

        if (Consts.DEBUG == true) {
            pin = new DigitalOutputTest(pinnumber);
        } else {
            pin = new DigitalOutput(pinnumber);
        }
        try {//try1

            if (ligthon == true) {//if1
                pin.pinOn();
            }//if1 
            else {//else1-1

                pin.pinOff();

            }//else1-1
        }//try1
        catch (OperationNotWorkException e) {//catch1-1

            try {//try2

                throw new ServerException("Server Exception", e);
            }//try2
            catch (Exception e2) {//catch2-1

            }//catch2-1

            retval = false;
        }//catch1-1

        return retval;
    }//switchpin

    private int pinstatus(int pinnumber) throws PinNumberNovalidException, IOException {//pinstatus

        IDigitalOuput pin;

        int retval;
        retval = -1;

        if (Consts.DEBUG == true) {
            pin = new DigitalOutputTest(pinnumber);
        } else {
            pin = new DigitalOutput(pinnumber);
        }

        try {//try1

            if (pin.getPinStatus() == true) {//if2

                retval = 1;

            }//if2
            else {//else2

                retval = 0;

            }//else2

        }//try1
        catch (OperationNotWorkException e) {//catch1-1

            try {//try2

                throw new ServerException("Server Exception", e);
            }//try2
            catch (Exception e2) {//catch2-1

            }//catch2-1

        }//catch1-1

        return retval;
    }//pinstatus

}
