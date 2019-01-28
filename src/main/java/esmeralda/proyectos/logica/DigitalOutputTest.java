package esmeralda.proyectos.logica;

import esmeralda.proyectos.exceptions.OperationNotWorkException;
import esmeralda.proyectos.exceptions.PinNumberNovalidException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class DigitalOutputTest implements IDigitalOuput {//class

    ////////////////
    //Constructor//
    //////////////
    private static boolean status;
    private int pin;

    public DigitalOutputTest() {//constructor

        this.pin = 0;

    }//constructor

    public DigitalOutputTest(int pin) throws PinNumberNovalidException, IOException {//constructor

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

        if (this.pin <= 0 || this.pin > 18) {//if1

            throw new PinNumberNovalidException();

        }//if1
        else {//else1

            DigitalOutputTest.status = true;

            if (this.getPinStatus() != true) {//if2

                throw new OperationNotWorkException();
            }//if2

        }//else1

    }//pinOn

    @Override
    public void pinOff() throws PinNumberNovalidException, IOException, OperationNotWorkException {//pinOff

        if (this.pin <= 0 || this.pin > 18) {//if1

            throw new PinNumberNovalidException();

        }//if1
        else {//else1

            DigitalOutputTest.status = false;

            if (this.getPinStatus() != false) {//if2

                throw new OperationNotWorkException();
            }//if2

        }//else1

    }//pinOff

    @Override
    public boolean getPinStatus() throws PinNumberNovalidException, IOException, OperationNotWorkException {//getPinStatus

        if (this.pin <= 0 || this.pin > 18) {//if1

            throw new PinNumberNovalidException();

        }//if1

        return DigitalOutputTest.status;

    }//getPinStatus

    @Override
    public void destroyPin(int pin) throws PinNumberNovalidException, IOException, OperationNotWorkException {//destroyPin

        DigitalOutputTest.status = false;

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
