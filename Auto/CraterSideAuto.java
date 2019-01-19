package org.firstinspires.ftc.teamcode.Auto;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.Subsystems.DriveTrain;
import org.firstinspires.ftc.teamcode.Subsystems.Hanger;

@Autonomous(name = "CraterSideAuto")
public class CraterSideAuto extends LinearOpMode {

    DriveTrain m_DriveTrain;
    Hanger m_Hanger;

    public void runOpMode(){

        m_DriveTrain = new DriveTrain(this, hardwareMap, telemetry);
        m_Hanger = new Hanger(this, hardwareMap, telemetry);

        double UP = 0;
        double DOWN = 1;


        waitForStart();

        while (opModeIsActive() && !isStopRequested()){

            m_Hanger.moveByTimer(0.75 , 1, DOWN);

            m_DriveTrain.driveByEncoder(6  , 1, DriveTrain.Direction.BACKWARD);

            m_DriveTrain.driveByEncoder(8, 1, DriveTrain.Direction.STRAFE_RIGHT);

            m_DriveTrain.driveByEncoder(4, 1, DriveTrain.Direction.FORWARD);

            m_DriveTrain.driveByEncoder(20, 1, DriveTrain.Direction.STRAFE_RIGHT);

            break;
        }
    }
}
