package esmeralda.proyectos.logica;

import esmeralda.proyectos.exceptions.OperationNotWorkException;
import esmeralda.proyectos.exceptions.PinNumberNovalidException;
import java.io.InputStreamReader;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class DigitalOutput implements IDigitalOuput {//class

    ////////////////
    //Constructor//
    //////////////
    private Runtime runtime;
    private int pin;

    public DigitalOutput() {//constructor

        this.runtime = Runtime.getRuntime();
        this.pin = 0;

    }//constructor

    public DigitalOutput(int pin) throws PinNumberNovalidException, IOException {//constructor

        this.runtime = Runtime.getRuntime();
        this.setPin(pin);

    }//constructor

    ///////////////
    //Interfaces//
    /////////////
    @Override
    public void setPin(int pin) throws PinNumberNovalidException, IOException {//setPin

        if (pin <= 0 || pin > 18) {//if1

            throw new PinNumberNovalidException();

        }//if1
        else {//else1

            this.pin = pin;

        }//else1

    }//setPin

    @Override
    public int getPin() {//getPin

        return this.pin;

    }//getPin

    @Override
    public void pinOn() throws PinNumberNovalidException, IOException, OperationNotWorkException {//pinOn

        Process process;
        String[] cmdline;
        int retval;

        if (this.pin <= 0 || this.pin > 18) {//if1

            throw new PinNumberNovalidException();

        }//if1
        else {//else1

            cmdline = new String[3];
            cmdline[0] = "sudo";
            cmdline[1] = Consts.LIGTHONSCRIPT;
            cmdline[2] = Integer.toString(this.pin);
            
            process = this.runtime.exec(cmdline);
            try {//try1
                process.waitFor();
            }//try1
            catch (InterruptedException e) {//catch1

                throw new IOException();

            }//catch1
            retval = process.exitValue();

            if (retval != 0 || this.getPinStatus() != true) {//if2

                throw new OperationNotWorkException();
            }//if2

        }//else1

    }//pinOn

    @Override
    public void pinOff() throws PinNumberNovalidException, IOException, OperationNotWorkException {//pinOff

        Process process;
        String[] cmdline;
        int retval;

        if (this.pin <= 0 || this.pin > 18) {//if1

            throw new PinNumberNovalidException();

        }//if1
        else {//else1

            cmdline = new String[3];
            cmdline[0] = "sudo";
            cmdline[1] = Consts.LIGTHOFFSCRIPT;
            cmdline[2] = Integer.toString(this.pin);
            
            process = this.runtime.exec(cmdline);
            try {//try1
                process.waitFor();
            }//try1
            catch (InterruptedException e) {//catch1

                throw new IOException();

            }//catch1
            retval = process.exitValue();

            if (retval != 0 || this.getPinStatus() != false) {//if2

                throw new OperationNotWorkException();
            }//if2

        }//else1

    }//pinOff

    @Override
    public boolean getPinStatus() throws PinNumberNovalidException, IOException, OperationNotWorkException {//getPinStatus

        Process process;
        String[] cmdline;
        String linea;
        boolean retval;
        int retval2;

        if (this.pin <= 0 || this.pin > 18) {//if1

            throw new PinNumberNovalidException();

        }//if1
        else {//else1

            cmdline = new String[3];
            cmdline[0] = "sudo";
            cmdline[1] = Consts.STATUSPINCRIPT;
            cmdline[2] = Integer.toString(this.pin);
            
      
            
            process = this.runtime.exec(cmdline);

            try {//try1
                process.waitFor();
            }//try1
            catch (InterruptedException e) {//catch1

                throw new IOException();

            }//catch1
            retval2 = process.exitValue();

            if (retval2 != 0) {//if2

                throw new OperationNotWorkException();
            }//if2

            try (InputStreamReader filtro = new InputStreamReader(process.getInputStream())) {//try2

                try (BufferedReader buffer = new BufferedReader(filtro)) {//try3

                    linea = buffer.readLine();

                }//try3

            }//try2

            if (linea == null || (linea.trim().equals("1") == false && linea.trim().equals("0") == false)) {//if2
                new OperationNotWorkException();

            }//if2

            retval = linea.trim().equals("1");

        }//else1

        return retval;

    }//getPinStatus

    @Override
    public void destroyPin(int pin) throws PinNumberNovalidException, IOException, OperationNotWorkException {//destroyPin

        Process process;
        String[] cmdline;
        int retval;

        if (this.pin <= 0 || this.pin > 18) {//if1

            throw new PinNumberNovalidException();

        }//if1
        else {//else1

            cmdline = new String[3];
            cmdline[0] = "sudo";
            cmdline[1] = Consts.CLOSEPINCRIPT;
            cmdline[2] = Integer.toString(this.pin);
            

            process = this.runtime.exec(cmdline);
            
            process = this.runtime.exec(cmdline);
            try {//try1
                process.waitFor();
            }//try1
            catch (InterruptedException e) {//catch1

                throw new IOException();

            }//catch1
            retval = process.exitValue();

            if (retval != 0) {//if2

                throw new OperationNotWorkException();
            }//if2

        }//else1

    }//destroyPin

    @Override
    public void destroyAllPins() throws IOException, OperationNotWorkException {//destroyAllPins

        for (int i = 1; i <= 18; i++) {//for1
            try {//try1

                this.destroyPin(pin);

            }//try1
            catch (PinNumberNovalidException e) {//catch1

            }//catch1
        }//for1

    }//destroyAllPins

    @Override
    public void close() {//close

        try {//try1

            this.destroyAllPins();

        }//try1
        catch (IOException e) {//catch1-1

        }//catch1-1
        catch (OperationNotWorkException e) {//catch1-2

        }//catch1-2

    }//close
}//class
