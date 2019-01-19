package org.firstinspires.ftc.teamcode.Auto;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.Subsystems.DriveTrain;
import org.firstinspires.ftc.teamcode.Subsystems.Hanger;

@Autonomous(name = "DepotSideAuto")
public class DepotSideAuto extends LinearOpMode {

    DriveTrain m_DriveTrain;
    Hanger m_Hanger;

    double UP = 0;
    double DOWN = 1;

    public void runOpMode(){

        m_DriveTrain = new DriveTrain(this, hardwareMap, telemetry);
        m_Hanger = new Hanger(this, hardwareMap, telemetry);

        waitForStart();

        m_DriveTrain = new DriveTrain(this, hardwareMap, telemetry);
        m_Hanger = new Hanger(this, hardwareMap, telemetry);

        waitForStart();

        while (opModeIsActive() && !isStopRequested()){

            m_Hanger.moveByTimer(0.75 , 1, DOWN);

            m_DriveTrain.driveByEncoder(6, 1, DriveTrain.Direction.BACKWARD);

            m_DriveTrain.driveByEncoder(8, 1, DriveTrain.Direction.STRAFE_RIGHT);

            m_DriveTrain.driveByEncoder(3, 1, DriveTrain.Direction.FORWARD);

            m_DriveTrain.driveByEncoder(32, 1, DriveTrain.Direction.STRAFE_RIGHT);

            m_DriveTrain.rotateLeftByGyro(45, 0.33);

            m_DriveTrain.driveByEncoder(32, 1, DriveTrain.Direction.STRAFE_RIGHT);

            m_DriveTrain.driveByEncoder(4, 1, DriveTrain.Direction.STRAFE_LEFT );

            m_DriveTrain.driveByEncoder(80, 1, DriveTrain.Direction.FORWARD);


            break;

        }


    }
}
