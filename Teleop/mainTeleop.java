package org.firstinspires.ftc.teamcode.Teleop;

import org.firstinspires.ftc.teamcode.Subsystems.Arm;
import org.firstinspires.ftc.teamcode.Subsystems.DriveTrain;
import org.firstinspires.ftc.teamcode.Subsystems.Hanger;
import org.firstinspires.ftc.teamcode.Subsystems.Intake;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

@TeleOp (name = "mainTeleop")
public class mainTeleop extends LinearOpMode {

    DriveTrain driveTrain;
    Hanger hanger;
    Arm arm;
    Intake intake;

    @Override
    public void runOpMode() {

            driveTrain = new DriveTrain(this, hardwareMap, telemetry);
            hanger = new Hanger(this, hardwareMap, telemetry);
            arm = new Arm(this, hardwareMap, telemetry);
            intake = new Intake(this, hardwareMap, telemetry);

            waitForStart();

            while (opModeIsActive() && !isStopRequested()) {

                driveTrain.mainMecanumDrive(gamepad1.left_stick_y, gamepad1.left_stick_x, gamepad1.right_stick_x);
                arm.PivotByJoystick(gamepad2.left_stick_y, 0.5);
                hanger.moveHanger(gamepad2.right_stick_y, 0.675);

                telemetry.update();
            }
    }
}