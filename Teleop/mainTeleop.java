package org.firstinspires.ftc.teamcode.Teleop;

import org.firstinspires.ftc.teamcode.Subsystems.Arm;
import org.firstinspires.ftc.teamcode.Subsystems.DriveTrain;
import org.firstinspires.ftc.teamcode.Subsystems.Hanger;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;

@TeleOp (name = "mainTeleop")
public class mainTeleop extends LinearOpMode {

    @Override
    public void runOpMode(){

        try {

            DriveTrain driveTrain = new DriveTrain(mainTeleop.this, hardwareMap, telemetry);
            Hanger hanger = new Hanger(mainTeleop.this, hardwareMap, telemetry);
            Arm arm = new Arm(mainTeleop.this, hardwareMap, telemetry);

            waitForStart();

            while (opModeIsActive()) {
                driveTrain.simpleMecanumTeleop(gamepad1.left_stick_x, -gamepad1.left_stick_y);
                driveTrain.simpleRotate(gamepad1.right_stick_x);
                arm.moveByJoystick(gamepad2.left_stick_y);
            }
        }

        catch (NullPointerException e){
            System.out.println(e.getCause());
        }
    }
}
