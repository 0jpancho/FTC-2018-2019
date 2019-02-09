package org.firstinspires.ftc.teamcode.Teleop;

import org.firstinspires.ftc.teamcode.Subsystems.Arm;
import org.firstinspires.ftc.teamcode.Subsystems.DriveTrain;
import org.firstinspires.ftc.teamcode.Subsystems.Hanger;
import org.firstinspires.ftc.teamcode.Subsystems.Intake;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

@TeleOp (name = "mainTeleop")
public class mainTeleop extends LinearOpMode {

    //Declare subsystems
    DriveTrain m_DriveTrain;
    Hanger m_Hanger;
    Arm m_Arm;
    Intake m_Intake;


    @Override
    public void runOpMode() {

            //Initialize subsystems
            m_DriveTrain = new DriveTrain(this, hardwareMap, telemetry);
            m_Hanger = new Hanger(this, hardwareMap, telemetry);
            m_Arm = new Arm(this, hardwareMap, telemetry);
            m_Intake = new Intake(this, hardwareMap, telemetry);

            waitForStart();


            while (opModeIsActive() && !isStopRequested()) {

                m_DriveTrain.mainMecanumDrive(gamepad1.left_stick_y, gamepad1.left_stick_x, gamepad1.right_stick_x);

                m_Hanger.moveHanger(gamepad2.right_stick_y, 0.675);

                m_Arm.pivotByJoystick(gamepad2.left_stick_y, 0.3);
                m_Arm.extendByButton();

                m_Intake.moveGrabbers();
                m_Intake.moveWrist(gamepad2.dpad_up, gamepad2.dpad_down);

                telemetry.update();
            }
    }
}