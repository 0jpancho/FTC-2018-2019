package org.firstinspires.ftc.teamcode.Auto;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.Subsystems.DriveTrain;
import org.firstinspires.ftc.teamcode.Subsystems.Hanger;

public class TestAuto extends LinearOpMode {

    DriveTrain m_DriveTrain;
    Hanger m_Hanger;

    double UP = 0;
    double DOWN = 1;

    @Override
    public void runOpMode(){

        m_DriveTrain = new DriveTrain(this, hardwareMap, telemetry);
        m_Hanger = new Hanger(this, hardwareMap, telemetry);

        waitForStart();

        m_Hanger.moveByTimer(0.5, 10, UP);
    }
}
