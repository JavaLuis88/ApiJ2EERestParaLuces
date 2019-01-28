package esmeralda.proyectos.exceptions;

public class PinNumberNovalidException  extends  Exception {

    ////////////////
    //Constructor//
    //////////////


    public PinNumberNovalidException() {//constructor

        super("Pin number is not valid");



    }//constructor


    public PinNumberNovalidException(String msg) {//constructor

        super(msg);



    }//constructor



}
