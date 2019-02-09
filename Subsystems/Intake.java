package org.firstinspires.ftc.teamcode.Subsystems;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.robotcore.external.Telemetry;

public class Intake {

    CRServo intakeLeft, intakeRight;
    Servo leftGrabber, rightGrabber;

    LinearOpMode l;
    Telemetry realTelemetry;

    boolean leftToggle = false;
    boolean rightToggle = false;

    public Intake(LinearOpMode opMode, HardwareMap hardwareMap, Telemetry telemetry){

        l = opMode;
        realTelemetry = telemetry;

       intakeLeft = hardwareMap.crservo.get("intakeLeft");
       intakeRight = hardwareMap.crservo.get("intakeRight");

       leftGrabber = hardwareMap.servo.get("leftGrabber");
       rightGrabber = hardwareMap.servo.get("rightGrabber");
    }

    //Controls the angle of the wrist
    public void moveWrist(boolean up, boolean down){

        if (up){
            intakeLeft.setPower(1);
            intakeRight.setPower(-1);
        }

        else if (down){
            intakeLeft.setPower(-1);
            intakeRight.setPower(1);
        }

        else {
            intakeLeft.setPower(0);
            intakeRight.setPower(0);
        }
    }

    //Toggles pinball grabbers. State is retained even if the button is pressed
    public void moveGrabbers(){
        if (l.gamepad2.left_bumper && !leftToggle){

            if (leftGrabber.getPosition() == 0.75){
                leftGrabber.setPosition(0);
            }

            else if (leftGrabber.getPosition() == 0){
                leftGrabber.setPosition(0.75);
            }
            leftToggle = true;
        }
        else if (!l.gamepad2.left_bumper){
            leftToggle = false;
        }

        if (l.gamepad2.right_bumper && !rightToggle){

            if (rightGrabber.getPosition() == 1){
                rightGrabber.setPosition(0.25);
            }
            else if (leftGrabber.getPosition() == 0.25){
                rightGrabber.setPosition(1);
            }
            rightToggle = true;
        }
        else if (!l.gamepad2.right_bumper){
            rightToggle = false;
        }
    }
}
