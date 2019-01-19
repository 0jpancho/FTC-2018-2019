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

        timer = new ElapsedTime( );

        realTelemetry.setAutoClear(true);

        hanger = hardwareMap.dcMotor.get("hanger");
        hanger.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        setRunMode(STOP_AND_RESET_ENCODER);
        setRunMode(RUN_TO_POSITION);

        //realTelemetry.addData("Arm Status", "Lift Initialization");
        realTelemetry.update();
        l.idle();
    }

    public void moveHanger (double inputY, double multiplier){
    
        setRunMode(RUN_USING_ENCODER);
        
        hanger.setPower(-inputY * multiplier);

        realTelemetry.update();
    }

    public void moveByTimer(double power, double duration, double direction){
        setRunMode(RUN_USING_ENCODER);

        timer.reset();
        if (direction == UP){
            while(timer.seconds() < duration && l.opModeIsActive() && !l.isStopRequested()){
                hanger.setPower(power);

                realTelemetry.addData("Current Time", timer.seconds());
                realTelemetry.addData("Target", duration);
            }
        }

        else if (direction == DOWN){
            while(timer.seconds() < duration && l.opModeIsActive() && !l.isStopRequested()){
                hanger.setPower(-power);

                realTelemetry.addData("Current Time", timer.seconds());
                realTelemetry.addData("Target", duration);
            }
        }
        hanger.setPower(0);
    }


    public void moveByEncoder (double inches, double power, double direction){
    
    int newHangerTargetCounts;
    
    setRunMode(RUN_TO_POSITION);
        
        if (direction == UP){
            
            newHangerTargetCounts = hanger.getCurrentPosition() + (int)(countsPerInch * inches);
                
            hanger.setPower(-power);

            while(l.opModeIsActive() && !l.isStopRequested()
                    && hanger.getCurrentPosition() > -newHangerTargetCounts){

                hanger.setPower(power);

                realTelemetry.addData("Hanger Encoder Target", newHangerTargetCounts);
                realTelemetry.addData("Hanger Counts", hanger.getCurrentPosition());
            }
        }
        else if (direction == DOWN){
            
            newHangerTargetCounts = hanger.getCurrentPosition() - (int)(countsPerInch * inches);

            hanger.setTargetPosition(newHangerTargetCounts );
            
            while(l.opModeIsActive() && !l.isStopRequested()
                    && hanger.getCurrentPosition() < newHangerTargetCounts){

                hanger.setPower(-power);

                realTelemetry.addData("Hanger Encoder Target", newHangerTargetCounts);
                realTelemetry.addData("Hanger Counts", hanger.getCurrentPosition());
            }
        }
        hanger.setPower(0);
        setRunMode(RUN_WITHOUT_ENCODER);
    }

    public void setRunMode(double mode) {

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
