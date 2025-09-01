package frc.robot.controlpanel;

import frc.robot.utilities.LoggingSystem;
import edu.wpi.first.wpilibj.DigitalInput;

public class EmergencyStop implements Runnable{
    @Override
    public void run() {
        DigitalInput stop = null;
        try{
            stop = new DigitalInput(21);
            String command = "sudo bash /home/pi/kill.bash";
            ProcessBuilder builder = new ProcessBuilder(command.split(" "));

            while(!Thread.interrupted()){
                if(!stop.get()){
                    LoggingSystem.logInfo("Emergency Stop");
                    builder.start();
                }
                try {
                    Thread.sleep(250);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
            stop.close();
        
        } catch(Exception e){
            System.out.println(e);
        }  finally {
            if(stop != null) {
                stop.close();
            }
        }
    }
}
