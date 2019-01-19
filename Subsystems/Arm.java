package org.firstinspires.ftc.teamcode.Subsystems;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.Telemetry;

public class  Arm {

    public DcMotor armPivot, armExtend;

    DcMotorEx test;


    public LinearOpMode l;
    public Telemetry realTelemetry;

    public Arm(LinearOpMode Input, HardwareMap hardwareMap, Telemetry telemetry){

        l = Input;
        realTelemetry = telemetry;

        armPivot = hardwareMap.dcMotor.get("armPivot");
        armPivot.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        armPivot.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
    }

    public void PivotByJoystick(double inputY, double multiplier){

        armPivot.setPower(inputY * multiplier);

        /*
        final int lastPos = arm.getCurrentPosition();

        arm.setTargetPosition((int)(inputY / 5040) - 5040);
        arm.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        arm.setPower(0.1);
         */
    }

    public void extendByJoystick(double inputY, double multiplier){

        armExtend.setPower(inputY * multiplier);
    }
}
