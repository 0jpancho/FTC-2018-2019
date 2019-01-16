package org.firstinspires.ftc.teamcode.Subsystems;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.Telemetry;

public class Hanger {

    public DcMotor hanger;

    public LinearOpMode l;
    public Telemetry realTelemetry;

    
    public double ppr = 28;
    public double gearboxRatio = 81;
    
    public double totalCountsPerRev = ppr * gearboxRatio;

    public double spoolDiameter = 2.5; //inches
    public double spoolCircumference = spoolDiameter * Math.PI;

    public double countsPerInch = totalCountsPerRev / spoolCircumference;
    
    public static double RUN_USING_ENCODER = 0;
    public static double RUN_TO_POSITION = 1;
    public static double RUN_WITHOUT_ENCODER = 2;
    public static double STOP_AND_RESET_ENCODER =3;
    
    public static double UP = 0;
    public static double DOWN = 1;

    public ElapsedTime timer;

    public Hanger(LinearOpMode Input, HardwareMap hardwareMap, Telemetry telemetry) {

        l = Input;
        realTelemetry = telemetry;

        timer = new ElapsedTime(0);

        realTelemetry.setAutoClear(true);

        hanger = hardwareMap.dcMotor.get("hanger");
        hanger.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        setHangerRunMode(STOP_AND_RESET_ENCODER);
        setHangerRunMode(RUN_TO_POSITION);

        //realTelemetry.addData("Arm Status", "Lift Initialization");
        realTelemetry.update();
        l.idle();
    }

    public void moveHanger (double inputY, double multiplier){
    
        setHangerRunMode(RUN_WITHOUT_ENCODER);
        
        hanger.setPower(-inputY * multiplier);

        realTelemetry.update();
    }
    
    public void moveByEncoder (double inches, double power, double direction){
    
    int newHangerTargetCounts;
    
    setHangerRunMode(RUN_TO_POSITION);
        
        if (direction == UP){
            
            newHangerTargetCounts = hanger.getCurrentPosition() + (int)(countsPerInch * inches);
                
            hanger.setPower(-power);
            
            hanger.setTargetPosition(newHangerTargetCounts);
            
            while(l.opModeIsActive() && hanger.isBusy()){
                realTelemetry.addData("Hanger Encoder Target", newHangerTargetCounts);
                realTelemetry.addData("Hanger Counts", hanger.getCurrentPosition());
            }
        }
        else if (direction == DOWN){
            
            newHangerTargetCounts = hanger.getCurrentPosition() - (int)(countsPerInch * inches);
                
            hanger.setPower(power);
            
            hanger.setTargetPosition(newHangerTargetCounts);
            
            while(l.opModeIsActive() && hanger.isBusy()){
                realTelemetry.addData("Hanger Encoder Target", newHangerTargetCounts);
                realTelemetry.addData("Hanger Counts", hanger.getCurrentPosition());
            }
        }
        hanger.setPower(0);
        setHangerRunMode(RUN_WITHOUT_ENCODER);
    }

    public void moveByTime(double time, double power, double direction){

        timer.startTime();

        if (direction == UP){

        }

    }
    
    public void setHangerRunMode(double mode) {

        if (mode == RUN_TO_POSITION) {
            hanger.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        } else if (mode == RUN_USING_ENCODER) {
            hanger.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        } else if (mode == RUN_WITHOUT_ENCODER) {
            hanger.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        } else if (mode == STOP_AND_RESET_ENCODER) {
            hanger.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        }
    }
}
