#!/usr/bin/python
import RPi.GPIO as GPIO;
import sys;
puerto=0;
retval=0;

if len(sys.argv)>=2:#if1
  try:#try1
    puerto=int(sys.argv[1]);
    
    if (puerto>=1 and puerto<=18):#if2
      try:#try2
        GPIO.setwarnings(False);
        GPIO.setmode(GPIO.BCM);
        GPIO.setup(puerto, GPIO.OUT);
        GPIO.output(puerto, False);

      except:#except2
        retval=2;
        
    else:#else2
      retval=1;
    


  except ValueError:#except1-1
    retval=1;
else:#else1
  retval=1;
sys.exit(retval);
