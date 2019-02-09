package org.firstinspires.ftc.teamcode.Auto;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.Subsystems.DriveTrain;
import org.firstinspires.ftc.teamcode.Subsystems.Hanger;


@Autonomous(name = "Backup Drive Forward")
public class BackupDriveForward extends LinearOpMode{

    //Declare subsystems
    DriveTrain m_DriveTrain;
    Hanger m_Hanger;

    public void runOpMode(){

        //Initialize Subsystems
        m_DriveTrain = new DriveTrain(this, hardwareMap, telemetry);
        m_Hanger = new Hanger(this, hardwareMap, telemetry);

        waitForStart();

        while (opModeIsActive() && !isStopRequested()){

            //Strafe Left into crater
            m_DriveTrain.driveByEncoder(28, 1, DriveTrain.Direction.STRAFE_RIGHT);
        }
    }
}
