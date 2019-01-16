package org.firstinspires.ftc.teamcode.Subsystems;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.Telemetry;

public class Arm {

    public DcMotor arm;

    public LinearOpMode l;
    public Telemetry realTelemetry;

    public Arm(LinearOpMode Input, HardwareMap hardwareMap, Telemetry telemetry){

        l = Input;
        realTelemetry = telemetry;

        arm = hardwareMap.dcMotor.get("arm");
        arm.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
    }

    public void moveByJoystick(double inputY){

//        final int lastPos = arm.getCurrentPosition();

            arm.setTargetPosition((int)(inputY / 5040) - 5040);
        arm.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        arm.setPower(0.1);

   //     else {
   //         arm.setPower(-inputY * .25);
        //}
    }
}
