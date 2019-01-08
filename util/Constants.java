package org.firstinspires.ftc.teamcode.util;

import com.qualcomm.hardware.motors.NeveRest20Gearmotor;
import com.qualcomm.hardware.motors.NeveRest40Gearmotor;
import com.qualcomm.robotcore.hardware.configuration.typecontainers.MotorConfigurationType;

public class Constants {

    public enum DriveDirection{
        FORWARD, BACKWARD, STRAFE_LEFT, STRAFE_RIGHT;
    }

    public enum MotorRunMode{
        RUN_WITHOUT_ENCODER, RUN_WITH_ENCODER, RUN_TO_POSITION, STOP_AND_RESET_ENCODER;
    }

    public static final MotorConfigurationType NEV_40_MOTOR_CONFIG =
            MotorConfigurationType.getMotorType(NeveRest40Gearmotor.class);


    public final double wheelDiameter = 4;

    public final double wheelCircumference = wheelDiameter * Math.PI;

    public final double countsPerInch = NEV_40_MOTOR_CONFIG.getTicksPerRev() / wheelCircumference;


}
