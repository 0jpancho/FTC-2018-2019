package org.firstinspires.ftc.teamcode.Subsystems;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.Telemetry;

public class Intake {

    LinearOpMode l;
    Telemetry realTelemetry;

    public Intake(LinearOpMode opMode, HardwareMap hardwareMap, Telemetry telemetry){

        l = opMode;
        realTelemetry = telemetry;
    }
}
