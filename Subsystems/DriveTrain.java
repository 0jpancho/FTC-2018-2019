package org.firstinspires.ftc.teamcode.Subsystems;

import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.HardwareMap;

import com.qualcomm.robotcore.hardware.configuration.typecontainers.MotorConfigurationType;
import com.qualcomm.hardware.motors.NeveRest20Gearmotor;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.AxesOrder;
import org.firstinspires.ftc.robotcore.external.navigation.AxesReference;
import org.firstinspires.ftc.robotcore.external.navigation.Orientation;

import java.util.Arrays;
import java.util.Locale;

public class DriveTrain {

    public final double wheelDiameter = 4;

    public final double ppr = 1120;
    public final double wheelCircumference = wheelDiameter * Math.PI;

    public final double countsPerInch = ppr / wheelCircumference;

    public DcMotor frontLeft, frontRight, backLeft, backRight;

    public Servo marker;

    public LinearOpMode l;
    public Telemetry realTelemetry;

    public double startTime;

    public static double RUN_USING_ENCODER = 0;
    public static double RUN_TO_POSITION = 1;
    public static double RUN_WITHOUT_ENCODER = 2;
    public static double STOP_AND_RESET_ENCODER = 3;

    BNO055IMU imu;
    Orientation angles;

    public enum Direction {
        FORWARD,
        BACKWARD,
        STRAFE_LEFT,
        STRAFE_RIGHT
    }

    private static final MotorConfigurationType MOTOR_CONFIG =
            MotorConfigurationType.getMotorType(NeveRest20Gearmotor.class);


    public DriveTrain(LinearOpMode Input, HardwareMap hardwareMap, Telemetry telemetry) {

        l = Input;
        realTelemetry = telemetry;

        realTelemetry.setAutoClear(true);

        startTime = l.getRuntime();

        frontLeft = hardwareMap.dcMotor.get("frontLeft");
        backLeft = hardwareMap.dcMotor.get("backLeft");

        frontRight = hardwareMap.dcMotor.get("frontRight");
        backRight = hardwareMap.dcMotor.get("backRight");

        MOTOR_CONFIG.getTicksPerRev();

        BNO055IMU.Parameters parameters = new BNO055IMU.Parameters();
        parameters.angleUnit = BNO055IMU.AngleUnit.DEGREES;
        parameters.calibrationDataFile = "BNO055IMUCalibration.json"; // see the calibration sample opmode
        parameters.loggingEnabled = true;
        parameters.loggingTag = "IMU";

        imu = hardwareMap.get(BNO055IMU.class, "imu");
        imu.initialize(parameters);

        setMotorRunMode(STOP_AND_RESET_ENCODER);
        setBreakMode(true);

        marker = hardwareMap.servo.get("marker");

        //realTelemetry.addData("DriveTrain Status", "DriveTrain Initialization");
        //realTelemetry.addData("startTime", startTime);
        //realTelemetry.update();
        l.idle();
    }

    public void mainMecanumDrive(double leftStickY, double leftStickX, double rightStickX) {

        setMotorRunMode(RUN_USING_ENCODER);

        //Sets motor values based on adding and subtracting joystick values
        double FrontLeftVal = leftStickY - (leftStickX) + -rightStickX;
        double FrontRightVal = leftStickY + (leftStickX) - -rightStickX;
        double BackLeftVal = leftStickY + (leftStickX) + -rightStickX;
        double BackRightVal = leftStickY - (leftStickX) - -rightStickX ;

        //Move range to between 0 and +1, if not already
        double[] wheelPowers = {FrontRightVal, FrontLeftVal, BackLeftVal, BackRightVal};
        Arrays.sort(wheelPowers);
        if (wheelPowers[3] > 1) {
            FrontLeftVal /= wheelPowers[3];
            FrontRightVal /= wheelPowers[3];
            BackLeftVal /= wheelPowers[3];
            BackRightVal /= wheelPowers[3];
        }
        frontLeft.setPower(FrontLeftVal);
        frontRight.setPower(-FrontRightVal);
        backLeft.setPower(BackLeftVal);
        backRight.setPower(-BackRightVal);
    }

    //Drive function that uses encoders to move a specific distance
    public void driveByEncoder(double inches, double power, Direction direction) {

        int newFrontLeftTarget;
        int newBackLeftTarget;

        int newFrontRightTarget;
        int newBackRightTarget;

        //Constant for mitigating distance loss on mecanum strafe
        double encoderStrafeMultiplier = Math.sqrt(2);

        setMotorRunMode(RUN_TO_POSITION);

        if (direction == Direction.FORWARD) {

            //Computes the target distance for each motor
            newFrontLeftTarget = frontLeft.getCurrentPosition() - (int) (inches * countsPerInch);
            newBackLeftTarget = backLeft.getCurrentPosition() - (int) (inches * countsPerInch);

            newFrontRightTarget = frontRight.getCurrentPosition() + (int) (inches * countsPerInch);
            newBackRightTarget = backRight.getCurrentPosition() + (int) (inches * countsPerInch);

            //Sets the motor power based on input
            frontLeft.setPower(-power);
            backLeft.setPower(-power);

            frontRight.setPower(power);
            backRight.setPower(power);

            //Sets the target position for each motor
            frontLeft.setTargetPosition(newFrontLeftTarget);
            backLeft.setTargetPosition(newBackLeftTarget);

            frontRight.setTargetPosition(newFrontRightTarget);
            backRight.setTargetPosition(newBackRightTarget);

            //Telemetry for encoder position
            while (frontLeft.isBusy() || backLeft.isBusy()
                    || frontRight.isBusy() || backRight.isBusy()
                    && l.opModeIsActive() && !l.isStopRequested()) {

                realTelemetry.addData("Current Function", "Encoder Forward");

                realTelemetry.addData("Front Motor Targets", "Running to %7d :%7d", newFrontLeftTarget, newFrontRightTarget);
                realTelemetry.addData("Front Motor Counts", "Running at %7d :%7d", frontLeft.getCurrentPosition(), frontRight.getCurrentPosition());
                realTelemetry.addData("--------------------------", null);
                realTelemetry.addData("Back Motor Targets", "Running to %7d :%7d", newBackLeftTarget, newBackRightTarget);
                realTelemetry.addData("Back Motor Counts", "Running at %7d :%7d", backLeft.getCurrentPosition(), backRight.getCurrentPosition());

                realTelemetry.update();
            }

            setMotorRunMode(RUN_USING_ENCODER);
        } else if (direction == Direction.BACKWARD) {

            newFrontLeftTarget = frontLeft.getCurrentPosition() + (int) (inches * countsPerInch);
            newBackLeftTarget = backLeft.getCurrentPosition() + (int) (inches * countsPerInch);

            newFrontRightTarget = frontRight.getCurrentPosition() - (int) (inches * countsPerInch);
            newBackRightTarget = backRight.getCurrentPosition() - (int) (inches * countsPerInch);

            frontLeft.setPower(power);
            backLeft.setPower(power);

            frontRight.setPower(-power);
            backRight.setPower(-power);

            frontLeft.setTargetPosition(newFrontLeftTarget);
            backLeft.setTargetPosition(newBackLeftTarget);

            frontRight.setTargetPosition(newFrontRightTarget);
            backRight.setTargetPosition(newBackRightTarget);

            while (frontLeft.isBusy() || backLeft.isBusy()
                    || frontRight.isBusy() || backRight.isBusy()
                    && l.opModeIsActive() && !l.isStopRequested()) {

                realTelemetry.addData("Current Function", "Encoder Backwards");

                realTelemetry.addData("Front Motor Targets", "Running to %7d :%7d", newFrontLeftTarget, newFrontRightTarget);
                realTelemetry.addData("Front Motor Counts", "Running at %7d :%7d", frontLeft.getCurrentPosition(), frontRight.getCurrentPosition());
                realTelemetry.addData("----------------", 0);
                realTelemetry.addData("Back Motor Targets", "Running to %7d :%7d", newBackLeftTarget, newBackRightTarget);
                realTelemetry.addData("Back Motor Counts", "Running at %7d :%7d", backLeft.getCurrentPosition(), backRight.getCurrentPosition());

                realTelemetry.update();
            }

            setMotorRunMode(RUN_USING_ENCODER);
        } else if (direction == Direction.STRAFE_LEFT) {

            newFrontLeftTarget = frontLeft.getCurrentPosition() + (int) ((inches * encoderStrafeMultiplier) * countsPerInch);
            newBackLeftTarget = backLeft.getCurrentPosition() - (int) ((inches * encoderStrafeMultiplier) * countsPerInch);

            newFrontRightTarget = frontRight.getCurrentPosition() + (int) ((inches * encoderStrafeMultiplier) * countsPerInch);
            newBackRightTarget = backRight.getCurrentPosition() - (int) ((inches * encoderStrafeMultiplier) * countsPerInch);

            frontLeft.setPower(power);
            backLeft.setPower(-power);

            frontRight.setPower(power);
            backRight.setPower(-power);

            frontLeft.setTargetPosition(newFrontLeftTarget);
            backLeft.setTargetPosition(newBackLeftTarget);

            frontRight.setTargetPosition(newFrontRightTarget);
            backRight.setTargetPosition(newBackRightTarget);

            while (frontLeft.isBusy() || backLeft.isBusy()
                    || frontRight.isBusy() || backRight.isBusy()
                    && l.opModeIsActive() && !l.isStopRequested()) {

                realTelemetry.addData("Current Function", "Encoder Strafe Left");

                realTelemetry.addData("Front Motor Targets", "Running to %7d :%7d", newFrontLeftTarget, newFrontRightTarget);
                realTelemetry.addData("Front Motor Counts", "Running at %7d :%7d", frontLeft.getCurrentPosition(), frontRight.getCurrentPosition());
                realTelemetry.addData("----------------", 0);
                realTelemetry.addData("Back Motor Targets", "Running to %7d :%7d", newBackLeftTarget, newBackRightTarget);
                realTelemetry.addData("Back Motor Counts", "Running at %7d :%7d", backLeft.getCurrentPosition(), backRight.getCurrentPosition());

                realTelemetry.update();
            }

            setMotorRunMode(RUN_USING_ENCODER);
        } else if (direction == Direction.STRAFE_RIGHT) {

            newFrontLeftTarget = frontLeft.getCurrentPosition() - (int) ((inches * encoderStrafeMultiplier) * countsPerInch);
            newBackLeftTarget = backLeft.getCurrentPosition() + (int) ((inches * encoderStrafeMultiplier) * countsPerInch);

            newFrontRightTarget = frontRight.getCurrentPosition() - (int) ((inches * encoderStrafeMultiplier) * countsPerInch);
            newBackRightTarget = backRight.getCurrentPosition() + (int) ((inches * encoderStrafeMultiplier) * countsPerInch);

            frontLeft.setPower(-power);
            backLeft.setPower(power);

            frontRight.setPower(-power);
            backRight.setPower(power);

            frontLeft.setTargetPosition(newFrontLeftTarget);
            backLeft.setTargetPosition(newBackLeftTarget);

            frontRight.setTargetPosition(newFrontRightTarget);
            backRight.setTargetPosition(newBackRightTarget);

            while (frontLeft.isBusy() || backLeft.isBusy()
                    || frontRight.isBusy() || backRight.isBusy()
                    && l.opModeIsActive() && !l.isStopRequested()) {

                realTelemetry.addData("Current Function", "Encoder Strafe Right");

                realTelemetry.addData("Front Motor Targets", "Running to %7d :%7d", newFrontLeftTarget, newFrontRightTarget);
                realTelemetry.addData("Front Motor Counts", "Running at %7d :%7d", frontLeft.getCurrentPosition(), frontRight.getCurrentPosition());
                realTelemetry.addData("----------------", 0);
                realTelemetry.addData("Back Motor Targets", "Running to %7d :%7d", newBackLeftTarget, newBackRightTarget);
                realTelemetry.addData("Back Motor Counts", "Running at %7d :%7d", backLeft.getCurrentPosition(), backRight.getCurrentPosition());

                realTelemetry.update();
            }

            setMotorRunMode(RUN_USING_ENCODER);
        }

        stopMotors();
        l.sleep(500);
    }

    public void timedRotate(double timeOne, double timeTwo, double power, double startTime, boolean left) {

        setMotorRunMode(RUN_WITHOUT_ENCODER);

        if (left == true) {

            while (l.getRuntime() - startTime >= timeOne && l.getRuntime() - startTime <= timeTwo && l.opModeIsActive()) {

                frontLeft.setPower(power);
                backLeft.setPower(power);

                frontRight.setPower(power);
                backRight.setPower(power);
            }
            stopMotors();
        } else {

            while (l.getRuntime() - startTime >= timeOne && l.getRuntime() - startTime <= timeTwo && l.opModeIsActive()) {

                frontLeft.setPower(-power);
                backLeft.setPower(-power);

                frontRight.setPower(-power);
                backRight.setPower(-power);
            }
            stopMotors();
        }
    }

    public void rotateLeftByGyro(double inputAngle, double power) {

        setMotorRunMode(RUN_WITHOUT_ENCODER);
        setBreakMode(true);

        angles = imu.getAngularOrientation(AxesReference.INTRINSIC, AxesOrder.ZYX, AngleUnit.DEGREES);
        double currentAngle = angles.firstAngle;
        double targetAngle = currentAngle + inputAngle;

        //Left
        while (currentAngle < targetAngle) {

            angles = imu.getAngularOrientation(AxesReference.INTRINSIC, AxesOrder.ZYX, AngleUnit.DEGREES);
            currentAngle = angles.firstAngle;

            frontLeft.setPower(power);
            backLeft.setPower(power);

            frontRight.setPower(power);
            backRight.setPower(power);

            realTelemetry.addData("Z Axis", imu.getAngularOrientation(AxesReference.INTRINSIC, AxesOrder.ZYX, AngleUnit.DEGREES).firstAngle);
            realTelemetry.addData("Y Axis", imu.getAngularOrientation(AxesReference.INTRINSIC, AxesOrder.ZYX, AngleUnit.DEGREES).secondAngle);
            realTelemetry.addData("X Axis", imu.getAngularOrientation(AxesReference.INTRINSIC, AxesOrder.ZYX, AngleUnit.DEGREES).thirdAngle);

            realTelemetry.addData("Target Angle", inputAngle);

            realTelemetry.update();
        }
        stopMotors();

        l.sleep(500);
    }

    public void rotateRightByGyro(double inputAngle, double power) {

        inputAngle = -inputAngle;

        setMotorRunMode(RUN_WITHOUT_ENCODER);
        setBreakMode(true);

        angles = imu.getAngularOrientation(AxesReference.INTRINSIC, AxesOrder.ZYX, AngleUnit.DEGREES);
        double currentAngle = angles.firstAngle;
        double targetAngle = currentAngle + inputAngle;

        //Left
        while (currentAngle > targetAngle) {

            angles = imu.getAngularOrientation(AxesReference.INTRINSIC, AxesOrder.ZYX, AngleUnit.DEGREES);
            currentAngle = angles.firstAngle;

            frontLeft.setPower(-power);
            backLeft.setPower(-power);

            frontRight.setPower(-power);
            backRight.setPower(-power);

            realTelemetry.addData("Z Axis", imu.getAngularOrientation(AxesReference.INTRINSIC, AxesOrder.ZYX, AngleUnit.DEGREES).firstAngle);
            realTelemetry.addData("Y Axis", imu.getAngularOrientation(AxesReference.INTRINSIC, AxesOrder.ZYX, AngleUnit.DEGREES).secondAngle);
            realTelemetry.addData("X Axis", imu.getAngularOrientation(AxesReference.INTRINSIC, AxesOrder.ZYX, AngleUnit.DEGREES).thirdAngle);

            realTelemetry.addData("Target Angle", inputAngle);

            realTelemetry.update();
        }
        stopMotors();

        l.sleep(500);
    }

    public void setMotorRunMode(double mode) {

        if (mode == RUN_TO_POSITION) {
            frontLeft.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            backLeft.setMode(DcMotor.RunMode.RUN_TO_POSITION);

            frontRight.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            backRight.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        } else if (mode == RUN_USING_ENCODER) {
            frontLeft.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            backLeft.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

            frontRight.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            backRight.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        } else if (mode == RUN_WITHOUT_ENCODER) {
            frontLeft.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
            backLeft.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

            frontRight.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
            backRight.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        } else if (mode == STOP_AND_RESET_ENCODER) {
            frontLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            backLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

            frontRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            backRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        }
    }

    public void setBreakMode(boolean enabled) {

        if (enabled == true) {
            frontLeft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
            backLeft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

            frontRight.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
            backRight.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        } else {
            frontLeft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
            backLeft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);

            frontRight.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
            backRight.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
        }
    }

    public void stopMotors() {

        frontLeft.setPower(0);
        backLeft.setPower(0);

        frontRight.setPower(0);
        backRight.setPower(0);

    }

    public void setMarker(boolean enabled) {

        if (enabled == false) {
            marker.setPosition(0);
        } else {
            marker.setPosition(0.5);
        }
    }
}