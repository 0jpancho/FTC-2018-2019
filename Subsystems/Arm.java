package org.firstinspires.ftc.teamcode.Subsystems;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.Telemetry;

public class  Arm {

    public DcMotor armPivot, armExtend;

    public LinearOpMode l;
    public Telemetry realTelemetry;

    public Arm(LinearOpMode Input, HardwareMap hardwareMap, Telemetry telemetry){

        l = Input;
        realTelemetry = telemetry;

        armPivot = hardwareMap.dcMotor.get("armPivot");
        armPivot.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        armPivot.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        armExtend = hardwareMap.dcMotor.get("armExtend");
        armExtend.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        armExtend.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

    }

    public void pivotByJoystick(double inputY, double multiplier){

        armPivot.setPower(inputY * multiplier);

        /*
        final int lastPos = arm.getCurrentPosition();

        arm.setTargetPosition((int)(inputY / 5040) - 5040);
        arm.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        arm.setPower(0.1);
         */
    }

    public void extendByButton() {

        if (l.gamepad2.y) {
            armExtend.setPower(0.75);
        } else if (l.gamepad2.a) {
            armExtend.setPower(-0.75);
        }
        armExtend.setPower(0);

    }
}
