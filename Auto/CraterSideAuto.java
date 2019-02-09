package org.firstinspires.ftc.teamcode.Auto;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.Subsystems.DriveTrain;
import org.firstinspires.ftc.teamcode.Subsystems.Hanger;

@Autonomous(name = "CraterSideAuto")
public class CraterSideAuto extends LinearOpMode {

    //Declare subsystems
    DriveTrain m_DriveTrain;
    Hanger m_Hanger;

    public void runOpMode(){

        //Initialize subsystems
        m_DriveTrain = new DriveTrain(this, hardwareMap, telemetry);
        m_Hanger = new Hanger(this, hardwareMap, telemetry);

        double DOWN = 0;
        double UP = 1;


        waitForStart();

        //While play has been pressed and the opMode hasn't been stopped
        while (opModeIsActive() && !isStopRequested()){

            //Move down from lander
            m_Hanger.moveByTimer(0.75 , 1, DOWN);

            //Back away from hook
            m_DriveTrain.driveByEncoder(6  , 1, DriveTrain.Direction.BACKWARD);

            //Strafe towards the crater
            m_DriveTrain.driveByEncoder(8, 1, DriveTrain.Direction.STRAFE_RIGHT);

            //Move forward to center sampling position
            m_DriveTrain.driveByEncoder(4, 1, DriveTrain.Direction.FORWARD);

            //Strafe to crater
            m_DriveTrain.driveByEncoder(20, 1, DriveTrain.Direction.STRAFE_RIGHT);

            break;
        }
    }
}
