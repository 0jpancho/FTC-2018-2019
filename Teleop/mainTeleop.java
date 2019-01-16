package org.firstinspires.ftc.teamcode.Teleop;

import org.firstinspires.ftc.teamcode.Subsystems.Arm;
import org.firstinspires.ftc.teamcode.Subsystems.DriveTrain;
import org.firstinspires.ftc.teamcode.Subsystems.Hanger;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;

import java.util.Arrays;

@TeleOp (name = "mainTeleop")
public class mainTeleop extends LinearOpMode {

    DriveTrain driveTrain;
    Hanger hanger;
    Arm arm;

    @Override
    public void runOpMode() {

            driveTrain = new DriveTrain(this, hardwareMap, telemetry);
            hanger = new Hanger(this, hardwareMap, telemetry);
            arm = new Arm(this, hardwareMap, telemetry);

            waitForStart();

            while (opModeIsActive() && !isStopRequested()) {

                driveTrain.testMecanumTeleop(gamepad1.left_stick_y, gamepad1.left_stick_x, gamepad1.right_stick_x);
                //driveTrain.simpleMecanumTeleop(gamepad1.left_stick_x, gamepad1.right_stick_x, -gamepad1.left_stick_y);
                //driveTrain.simpleRotate(gamepad1.right_stick_x);
                //arm.moveByJoystick(gamepad2.left_stick_y);
                hanger.moveHanger(gamepad2.right_stick_y, 0.5);

                telemetry.addData("Left Stick Y", gamepad1.left_stick_y);
                telemetry.addData("Left Stick X", gamepad1.left_stick_x);
                telemetry.addData("Right Stick X", gamepad1.right_stick_x);

                telemetry.update();
            }
    }
}