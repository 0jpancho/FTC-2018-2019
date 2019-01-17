package org.firstinspires.ftc.teamcode.Subsystems;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.Telemetry;

public class Intake {

    CRServo intake;

    LinearOpMode l;
    Telemetry realTelemetry;

    public Intake(LinearOpMode opMode, HardwareMap hardwareMap, Telemetry telemetry){

        l = opMode;
        realTelemetry = telemetry;

        //intake = hardwareMap.crservo.get("intake");
    }

    public void moveByButton(boolean up, boolean down){

        if (up){
            intake.setPower(1);
        }

        else if (down){
            intake.setPower(-1);
        }

        else {
            intake.setPower(0);
        }

    }
}
