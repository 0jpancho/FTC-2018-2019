package org.firstinspires.ftc.teamcode.Auto;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.Subsystems.DriveTrain;
import org.firstinspires.ftc.teamcode.Subsystems.Hanger;

@Autonomous(name = "DepotSideAuto")
public class DepotSideAuto extends LinearOpMode {

    DriveTrain m_DriveTrain;
    Hanger m_Hanger;

    double DOWN = 0;
    double UP = 1;

    public void runOpMode(){

        m_DriveTrain = new DriveTrain(this, hardwareMap, telemetry);
        m_Hanger = new Hanger(this, hardwareMap, telemetry);

        waitForStart();

        m_DriveTrain = new DriveTrain(this, hardwareMap, telemetry);
        m_Hanger = new Hanger(this, hardwareMap, telemetry);

        waitForStart();

        //While play has been pressed and the opMode hasn't been stopped
        while (opModeIsActive() && !isStopRequested()){

            //Move down from lander
            m_Hanger.moveByTimer(0.75 , 1, DOWN);

            //Back away from hook
            m_DriveTrain.driveByEncoder(6, 1, DriveTrain.Direction.BACKWARD);

            //Strafe towards depot
            m_DriveTrain.driveByEncoder(8, 1, DriveTrain.Direction.STRAFE_RIGHT);

            //Move forward to center sampling position
            m_DriveTrain.driveByEncoder(3, 1, DriveTrain.Direction.FORWARD);

            //Atempt to push center mineral out
            m_DriveTrain.driveByEncoder(12, 1, DriveTrain.Direction.STRAFE_RIGHT);

            //Move towards lander again
            m_DriveTrain.driveByEncoder(12, 1, DriveTrain.Direction.STRAFE_LEFT);

            //Move forward
            m_DriveTrain.driveByEncoder(20, 1, DriveTrain.Direction.FORWARD);

            //Turn towards crater
            m_DriveTrain.rotateLeftByGyro(45, 0.33);

            //Strafe to wall to reposition
            m_DriveTrain.driveByEncoder(32, 1, DriveTrain.Direction.STRAFE_RIGHT);

            //Move away from the wall
            m_DriveTrain.driveByEncoder(4, 1, DriveTrain.Direction.STRAFE_LEFT );

            //Drive to crater
            m_DriveTrain.driveByEncoder(72, 1, DriveTrain.Direction.FORWARD);

            break;
        }
    }
}
