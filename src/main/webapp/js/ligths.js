var imgligthon;
var imgligthoff;
var strpanel;
var strtoken;
var continuar;
var ligthstatus;
imgligthon = new Image();
imgligthon.src = "images/ligthon.png";
imgligthoff = new Image();
imgligthoff.src = "images/ligthoff.png";
function inicio() {


    strpanel = "pnllogin";
    strtoken = "";
    continuar = true;
    ligthstatus = true;
    document.getElementById("btnconmutar").style.display = "none";
    document.getElementById("btncerrar").style.display = "none";
    document.getElementById("btnaccess").onclick = identificacion
    document.getElementById("btnconmutar").onclick = conmutar;
    document.getElementById("btncerrar").onclick = cerrar;
}


function identificacion() {



    var strnombredeusuario;
    var strcontrasena;
    var ajaxobj;
    var strhandhask;
    var restoperation;
    var retval;
    document.getElementById("txtusernameerror").className = "form-text text-danger d-none";
    document.getElementById("txtpassworderror").className = "form-text text-danger d-none";
    document.getElementById("altalerta").className = "alert alert-danger alert-dismissible fade  fixed-top text-center"



    if (document.getElementById("txtusername").validity.valid == false) {

        document.getElementById("txtusernameerror").className = "form-text text-danger";
    } else if (document.getElementById("txtpassword").validity.valid == false) {

        document.getElementById("txtpassworderror").className = "form-text text-danger";
    } else {

        strnombredeusuario = document.getElementById("txtusername").value;
        strcontrasena = document.getElementById("txtpassword").value;
        strhandhask = generatehanshask();
        ajaxobj = new REST.Request();
        ajaxobj.setURI(REST.apiURL + "/luces/authServer");
        ajaxobj.setMethod("POST");
        ajaxobj.setContentType("application/x-www-form-urlencoded");
        ajaxobj.setAsync(true);
        ajaxobj.setEntity("username=" + escape(strnombredeusuario) + "&handshask=" + escape(strhandhask))
        ajaxobj.execute(function (status, request, restoperation) {

            if (status != 200) {
                showerroralert("Error", "Error inesperado");
            } else if (restoperation.responsetype != 0) {
                showerroralert("Error", "Error inesperado");
            } else if (restoperation.operationstatus != 0) {
                showerroralert("Error", "Error de autentificacion");
            } else if (validatehandshak(strhandhask, strcontrasena, restoperation.info[0]) == false) {
                showerroralert("Error", "Error de autentificacion");
            } else {
                strtoken = restoperation.token;
                strhandhask = CryptoJS.enc.Base64.stringify(CryptoJS.HmacSHA256(restoperation.info[1], strcontrasena));
                ajaxobj.setURI(REST.apiURL + "/luces/authClient");
                ajaxobj.setMethod("POST");
                ajaxobj.setContentType("application/x-www-form-urlencoded");
                ajaxobj.setAsync(true);
                ajaxobj.setEntity("handshaskresponse=" + escape(strhandhask) + "&token=" + escape(strtoken));
                ajaxobj.execute(function (status, request, restoperation) {
                    if (status != 200) {
                        showerroralert("Error", "Error inesperado");
                    } else if (restoperation.responsetype != 1) {
                        showerroralert("Error", "Error inesperado");
                    } else if (restoperation.operationstatus == 1) {
                        showerroralert("Error", "Error de autentificacion");
                    } else if (restoperation.operationstatus == 2) {
                        showerroralert("Error", "Token error");
                    } else {
                        strtoken = restoperation.token;
                        document.getElementById("txtusername").value = "";
                        document.getElementById("txtpassword").value = "";
                        continuar = true;
                        document.getElementById(strpanel).style.display = "none";
                        document.getElementById("pnlligth").style.display = "block";
                        strpanel = "pnlligth";
                        comprobarestado();
                    }

                });
            }


        });
    }
}


function comprobarestado() {

    var restoperation;
    var mensajeerror;
    var ajaxobj;
    mensajeerror = ""
    if (continuar == true) {
        ajaxobj = new REST.Request();
        ajaxobj.setURI(REST.apiURL + "/luces/getStatus");
        ajaxobj.setMethod("POST");
        ajaxobj.setContentType("application/x-www-form-urlencoded");
        ajaxobj.setAsync(true);
        ajaxobj.setEntity("token=" + escape(strtoken));
        ajaxobj.execute(function (status, request, restoperation) {
            if (status != 200) {
                mensajeerror = "Error Inesperado";
            } else if (restoperation.responsetype != 2) {

                mensajeerror = "Error Inesperado";
            } else if (restoperation.operationstatus == 1) {

                mensajeerror = "Error de autentificacion";
            } else if (restoperation.operationstatus == 2) {

                mensajeerror = "Token error";
            } else if (restoperation.operationstatus == -1) {

                mensajeerror = "Error Inesperado";
            }



            if (mensajeerror != "") {

                continuar = false;
                document.getElementById(strpanel).style.display = "none";
                document.getElementById("pnllogin").style.display = "block";
                strpanel = "pnllogin";
                document.getElementById("imgbombilla").style.display = "none";
                document.getElementById("btncerrar").style.display = "none";
                showerroralert("Error", mensajeerror);
            } else {



                if (restoperation.info[0] == "1") {

                    document.getElementById("imgbombilla").src = imgligthon.src
                    document.getElementById("btnconmutar").innerHTML = "Apagar"
                    ligthstatus = true;
                } else {

                    document.getElementById("imgbombilla").src = imgligthoff.src
                    document.getElementById("btnconmutar").innerHTML = "Encender"
                    ligthstatus = false;
                }


                document.getElementById("imgbombilla").style.display = "block";
                document.getElementById("btnconmutar").style.display = "block";
                document.getElementById("btncerrar").style.display = "block";
                strtoken = restoperation.token;
                if (continuar == true) {

                    window.setTimeout(comprobarestado, 500);

                }
            }



        });
    }
}


function conmutar() {

    var restoperation;
    var mensajeerror;
    var resturl;
    var ajaxobj;

    if (continuar == true) {



        mensajeerror = "";


        if (ligthstatus == true) {

            resturl = REST.apiURL + "/luces/ligthOff";
        } else {

            resturl = REST.apiURL + "/luces/ligthOn";
        }

        ajaxobj = new REST.Request();
        ajaxobj.setURI(resturl);
        ajaxobj.setMethod("POST");
        ajaxobj.setContentType("application/x-www-form-urlencoded");
        ajaxobj.setAsync(true);
        ajaxobj.setEntity("token=" + escape(strtoken));
        ajaxobj.execute(function (status, request, restoperation) {

            if (status != 200) {//

                mensajeerror = "Error Inesperado";
            } else if (restoperation.responsetype != 2) {

                mensajeerror = "Error Inesperado";
            } else if (restoperation.operationstatus == 1) {

                mensajeerror = "Error de autentificacion";
            } else if (restoperation.operationstatus == 2) {

                mensajeerror = "Token error";
            }

            if (mensajeerror != "") {

                continuar = false;
                document.getElementById(strpanel).style.display = "none";
                document.getElementById("pnllogin").style.display = "block";
                strpanel = "pnllogin";
                document.getElementById("imgbombilla").style.display = "none";
                document.getElementById("btncerrar").style.display = "none";
                showerroralert("Error", mensajeerror);
            } else {

                strtoken = restoperation.token;

            }

        });


    }

}

function cerrar() {

    location.reload();
}


function generatehanshask() {

    var size;
    var position;
    var characters = "ABCDEFGHIJKMNOPQRSTUVWXYZabcdefghijkmnopqrstuvwxyz01023456789_.=";
    var retval;
    retval = "";
    size = Math.round(Math.random() * (40 - 15)) + 15;
    for (var i = 1; i <= size; i++) {
        position = Math.round(Math.random() * (characters.length - 1));
        retval = retval + characters.charAt(position);
    }

    return retval;
}



function validatehandshak(plaintext, key, digesttext) {

    return (CryptoJS.enc.Base64.stringify(CryptoJS.HmacSHA256(plaintext, key)) == digesttext);
}


function showerroralert(title, message, closefunction) {

    document.getElementById("hdralerta").innerHTML = title;
    document.getElementById("bdyalerta").innerHTML = message;
    if (closefunction != undefined && closefunction != null) {

        $('#altalerta').on('closed.bs.alert', closefuntion);
    }
    document.getElementById("altalerta").className = "alert alert-danger alert-dismissible fade show fixed-top text-center"


}






window.onload = inicio;


