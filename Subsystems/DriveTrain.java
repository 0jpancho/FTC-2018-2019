package org.firstinspires.ftc.teamcode.Subsystems;

import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.AxesOrder;
import org.firstinspires.ftc.robotcore.external.navigation.AxesReference;
import org.firstinspires.ftc.robotcore.external.navigation.Orientation;
import org.firstinspires.ftc.teamcode.util.Constants;

import java.util.Arrays;
import java.util.Locale;

public class DriveTrain {

    Constants constants;

    public double theta;

    public double x = 0;
    public double y = 0;

    public double vA, vB, vC, vD;
    public double r;

    public static double FORWARD = 0;
    public static double BACKWARD = 1;
    public static double STRAFE_LEFT = 2;
    public static double STRAFE_RIGHT = 3;

    public DcMotor frontLeft, frontRight, backLeft, backRight;

    public ColorSensor colorSensor;

    public Servo marker;

    public LinearOpMode l;
    public Telemetry realTelemetry;

    public double startTime;

    public static double RUN_USING_ENCODER = 0;
    public static double RUN_TO_POSITION = 1;
    public static double RUN_WITHOUT_ENCODER = 2;
    public static double STOP_AND_RESET_ENCODER =3;


    BNO055IMU imu;
    Orientation angles;

    public enum Direction {
        FORWARD,
        BACKWARD,
        STRAFE_LEFT,
        STRAFE_RIGHT
    }


    public DriveTrain(LinearOpMode Input, HardwareMap hardwareMap, Telemetry telemetry) {

        l = Input;
        realTelemetry = telemetry;

        realTelemetry.setAutoClear(true);

        startTime = l.getRuntime();

        frontLeft = hardwareMap.dcMotor.get("frontLeft");
        backLeft = hardwareMap.dcMotor.get("backLeft");

        frontRight = hardwareMap.dcMotor.get("frontRight");
        backRight = hardwareMap.dcMotor.get("backRight");

        colorSensor = hardwareMap.colorSensor.get("colorSensor");

        BNO055IMU.Parameters parameters = new BNO055IMU.Parameters();
        parameters.angleUnit           = BNO055IMU.AngleUnit.DEGREES;
        parameters.calibrationDataFile = "BNO055IMUCalibration.json"; // see the calibration sample opmode
        parameters.loggingEnabled      = true;
        parameters.loggingTag          = "IMU";

        byte AXIS_MAP_CONFIG_BYTE = 0x6; //This is what to write to the AXIS_MAP_CONFIG register to swap x and z axes
        byte AXIS_MAP_SIGN_BYTE = 0x1; //This is what to write to the AXIS_MAP_SIGN register to negate the z axis

        //Need to be in CONFIG mode to write to registers
        imu.write8(BNO055IMU.Register.OPR_MODE,BNO055IMU.SensorMode.CONFIG.bVal & 0x0F);

        l.sleep(100); //Changing modes requires a delay before doing anything else

        //Write to the AXIS_MAP_CONFIG register
        imu.write8(BNO055IMU.Register.AXIS_MAP_CONFIG,AXIS_MAP_CONFIG_BYTE & 0x0F);

        //Write to the AXIS_MAP_SIGN register
        imu.write8(BNO055IMU.Register.AXIS_MAP_SIGN,AXIS_MAP_SIGN_BYTE & 0x0F);

        //Need to change back into the IMU mode to use the gyro
        imu.write8(BNO055IMU.Register.OPR_MODE,BNO055IMU.SensorMode.IMU.bVal & 0x0F);

        l.sleep(100); //Changing modes again requires a delay


        //angles = imu.getAngularOrientation(AxesReference.INTRINSIC, AxesOrder.ZYX, AngleUnit.DEGREES);

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

    public void testMecanumTeleop(){
        double FrontLeftVal =  l.gamepad1.left_stick_y - (l.gamepad1.left_stick_x)  + -l.gamepad1.right_stick_x;
        double FrontRightVal =  l.gamepad1.left_stick_y  + (l.gamepad1.left_stick_x) - -l.gamepad1.right_stick_x;
        double BackLeftVal = l.gamepad1.left_stick_y  + (l.gamepad1.left_stick_x)  + -l.gamepad1.right_stick_x;
        double BackRightVal = l.gamepad1.left_stick_y - (l.gamepad1.left_stick_x) - -l.gamepad1.right_stick_x;

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
        frontRight.setPower(FrontRightVal);
        backLeft.setPower(BackLeftVal);
        backRight.setPower(BackRightVal);
    }

    public void simpleMecanumTeleop(double inputX, double inputY) {

        setBreakMode(false);
        setMotorRunMode(RUN_USING_ENCODER);

        if (l.gamepad1.left_stick_y < 0){

            frontLeft.setPower(-inputY);
            backLeft.setPower(-inputY);

            frontRight.setPower(inputY);
            backRight.setPower(inputY);

        }

        else if (l.gamepad1.left_stick_y > 0) {

            frontLeft.setPower(-inputY);
            backLeft.setPower(-inputY);

            frontRight.setPower(inputY);
            backRight.setPower(inputY);
        }

        else if (l.gamepad1.left_stick_x < 0){

            frontLeft.setPower(-inputX);
            backLeft.setPower(inputX);

            frontRight.setPower(-inputX);
            backRight.setPower(inputX);
        }

        else if (l.gamepad1.left_stick_x > 0){

            frontLeft.setPower(-inputX);
            backLeft.setPower(inputX);

            frontRight.setPower(-inputX);
            backRight.setPower(inputX);
        }

        else {
            stopMotors();
        }
    }

    public void simpleRotate(double inputX) {

        setBreakMode(true);

        if (l.gamepad1.right_stick_x < 0) {
            frontLeft.setPower(-inputX);
            frontRight.setPower(-inputX);

            backRight.setPower(-inputX);
            backLeft.setPower(-inputX);
        }

        else if (l.gamepad1.right_stick_x > 0) {
            frontLeft.setPower(-inputX);
            frontRight.setPower(-inputX);

            backRight.setPower(-inputX);
            backLeft.setPower(-inputX);
        }

        //realTelemetry.addData("DriveTrain Status", "Rotating DriveTrain");
        realTelemetry.update();
    }

    public void advMecanumTeleop(double inputX, double inputY)
    {
        x = inputX;
        y = inputY;
        r = Math.sqrt(x * x + y * y);

        theta = (Math.atan2(y, x) * (180/Math.PI));

        vA = (Math.cos((theta - 45) * (Math.PI/180)) * r) * l.gamepad1.right_stick_y;
        vB = (Math.cos((theta + 45) *  (Math.PI/180))* r) * l.gamepad1.right_stick_y;
        vC = -vA;
        vD = -vB;

        frontLeft.setPower(vA);
        frontRight.setPower(vB);

        backRight.setPower(vC);
        backLeft.setPower(vD);

        realTelemetry.addData("vA", vA);
        realTelemetry.addData("vB", vB);
        realTelemetry.addData("vC", vC);
        realTelemetry.addData("vD", vD);

        realTelemetry.addData("X", inputX);
        realTelemetry.addData("Y", inputY);

        //realTelemetry.addData("DriveTrain Status", "Set Motors");

        realTelemetry.update();
    }

    public void simpleMove(double timeOne, double timeTwo, double power, double startTime, double direction){

        setBreakMode(true);

        if (direction == FORWARD){

            while (l.getRuntime() - startTime >= timeOne && l.getRuntime() - startTime <= timeTwo && l.opModeIsActive()) {

                frontLeft.setPower(-power);
                backLeft.setPower(-power);

                frontRight.setPower(power);
                backRight.setPower(power);
            }
            stopMotors();
        }

        else if (direction == BACKWARD){

            while (l.getRuntime() - startTime >= timeOne && l.getRuntime() - startTime <= timeTwo && l.opModeIsActive()) {

                frontLeft.setPower(power);
                backLeft.setPower(power);

                frontRight.setPower(-power);
                backRight.setPower(-power);
            }
            stopMotors();
        }

        else if (direction == STRAFE_LEFT){

            while (l.getRuntime() - startTime >= timeOne && l.getRuntime() - startTime <= timeTwo && l.opModeIsActive()) {

                frontLeft.setPower(power);
                backLeft.setPower(-power);

                frontRight.setPower(power);
                backRight.setPower(-power);
            }
            stopMotors();
        }

        else if (direction == STRAFE_RIGHT){

            while (l.getRuntime() - startTime >= timeOne && l.getRuntime() - startTime <= timeTwo && l.opModeIsActive()) {

                frontLeft.setPower(-power);
                backLeft.setPower(power);

                frontRight.setPower(-power);
                backRight.setPower(power);
            }
            stopMotors();
        }
    }

    public void moveByEncoder(double inches, double power, Direction direction){

        int newFrontLeftTarget;
        int newBackLeftTarget;

        int newFrontRightTarget;
        int newBackRightTarget;

        double encoderStrafeMultiplier = Math.sqrt(2);

        setMotorRunMode(RUN_TO_POSITION);

        if (direction == Direction.FORWARD) {

            newFrontLeftTarget = frontLeft.getCurrentPosition() - (int)(inches * constants.countsPerInch);
            newBackLeftTarget = backLeft.getCurrentPosition() - (int)(inches * constants.countsPerInch);

            newFrontRightTarget = frontRight.getCurrentPosition() + (int)(inches * constants.countsPerInch);
            newBackRightTarget = backRight.getCurrentPosition() + (int)(inches * constants.countsPerInch);

            frontLeft.setPower(-power);
            backLeft.setPower(-power);

            frontRight.setPower(power);
            backRight.setPower(power);

            frontLeft.setTargetPosition(newFrontLeftTarget);
            backLeft.setTargetPosition(newBackLeftTarget);

            frontRight.setTargetPosition(newFrontRightTarget);
            backRight.setTargetPosition(newBackRightTarget);

            while (frontLeft.isBusy() || backLeft.isBusy()
                    || frontRight.isBusy() || backRight.isBusy()) {

                realTelemetry.addData("Current Function", "Encoder Forward");

                realTelemetry.addData("Front Motor Targets", "Running to %7d :%7d", newFrontLeftTarget, newFrontRightTarget);
                realTelemetry.addData("Front Motor Counts", "Running at %7d :%7d", frontLeft.getCurrentPosition(), frontRight.getCurrentPosition());
                realTelemetry.addData("--------------------------", null);
                realTelemetry.addData("Back Motor Targets", "Running to %7d :%7d", newBackLeftTarget, newBackRightTarget);
                realTelemetry.addData("Back Motor Counts", "Running at %7d :%7d", backLeft.getCurrentPosition(), backRight.getCurrentPosition());

                realTelemetry.update();
            }

            setMotorRunMode(RUN_USING_ENCODER);
        }

        else if (direction == Direction.BACKWARD){

            newFrontLeftTarget = frontLeft.getCurrentPosition() + (int)(inches * constants.countsPerInch);
            newBackLeftTarget = backLeft.getCurrentPosition() + (int)(inches * constants.countsPerInch);

            newFrontRightTarget = frontRight.getCurrentPosition() - (int)(inches * constants.countsPerInch);
            newBackRightTarget = backRight.getCurrentPosition() - (int)(inches * constants.countsPerInch);

            frontLeft.setPower(power);
            backLeft.setPower(power);

            frontRight.setPower(-power);
            backRight.setPower(-power);

            frontLeft.setTargetPosition(newFrontLeftTarget);
            backLeft.setTargetPosition(newBackLeftTarget);

            frontRight.setTargetPosition(newFrontRightTarget);
            backRight.setTargetPosition(newBackRightTarget);

            while (frontLeft.isBusy() || backLeft.isBusy()
                    || frontRight.isBusy() || backRight.isBusy()) {

                realTelemetry.addData("Current Function", "Encoder Backwards");

                realTelemetry.addData("Front Motor Targets", "Running to %7d :%7d", newFrontLeftTarget, newFrontRightTarget);
                realTelemetry.addData("Front Motor Counts", "Running at %7d :%7d", frontLeft.getCurrentPosition(), frontRight.getCurrentPosition());
                realTelemetry.addData("----------------", 0);
                realTelemetry.addData("Back Motor Targets", "Running to %7d :%7d", newBackLeftTarget, newBackRightTarget);
                realTelemetry.addData("Back Motor Counts", "Running at %7d :%7d", backLeft.getCurrentPosition(), backRight.getCurrentPosition());

                realTelemetry.update();
            }

            setMotorRunMode(RUN_USING_ENCODER);
        }

        else if (direction == Direction.STRAFE_LEFT){

            newFrontLeftTarget = frontLeft.getCurrentPosition() + (int)((inches * encoderStrafeMultiplier) * constants.countsPerInch);
            newBackLeftTarget = backLeft.getCurrentPosition() - (int)((inches * encoderStrafeMultiplier) * constants.countsPerInch);

            newFrontRightTarget = frontRight.getCurrentPosition() + (int)((inches * encoderStrafeMultiplier) * constants.countsPerInch);
            newBackRightTarget = backRight.getCurrentPosition() - (int)((inches * encoderStrafeMultiplier) * constants.countsPerInch);

            frontLeft.setPower(power);
            backLeft.setPower(-power);

            frontRight.setPower(power);
            backRight.setPower(-power);

            frontLeft.setTargetPosition(newFrontLeftTarget);
            backLeft.setTargetPosition(newBackLeftTarget);

            frontRight.setTargetPosition(newFrontRightTarget);
            backRight.setTargetPosition(newBackRightTarget);

            while (frontLeft.isBusy() || backLeft.isBusy()
                    || frontRight.isBusy() || backRight.isBusy()) {

                realTelemetry.addData("Current Function", "Encoder Strafe Left");

                realTelemetry.addData("Front Motor Targets", "Running to %7d :%7d", newFrontLeftTarget, newFrontRightTarget);
                realTelemetry.addData("Front Motor Counts", "Running at %7d :%7d", frontLeft.getCurrentPosition(), frontRight.getCurrentPosition());
                realTelemetry.addData("----------------", 0);
                realTelemetry.addData("Back Motor Targets", "Running to %7d :%7d", newBackLeftTarget, newBackRightTarget);
                realTelemetry.addData("Back Motor Counts", "Running at %7d :%7d", backLeft.getCurrentPosition(), backRight.getCurrentPosition());

                realTelemetry.update();
            }

            setMotorRunMode(RUN_USING_ENCODER);
        }

        else if (direction == Direction.STRAFE_RIGHT){

            newFrontLeftTarget = frontLeft.getCurrentPosition() - (int)((inches * encoderStrafeMultiplier) * constants.countsPerInch);
            newBackLeftTarget = backLeft.getCurrentPosition() + (int)((inches * encoderStrafeMultiplier) * constants.countsPerInch);

            newFrontRightTarget = frontRight.getCurrentPosition() - (int)((inches * encoderStrafeMultiplier) * constants.countsPerInch);
            newBackRightTarget = backRight.getCurrentPosition() + (int)((inches * encoderStrafeMultiplier) * constants.countsPerInch);

            frontLeft.setPower(-power);
            backLeft.setPower(power);

            frontRight.setPower(-power);
            backRight.setPower(power);

            frontLeft.setTargetPosition(newFrontLeftTarget);
            backLeft.setTargetPosition(newBackLeftTarget);

            frontRight.setTargetPosition(newFrontRightTarget);
            backRight.setTargetPosition(newBackRightTarget);

            while (frontLeft.isBusy() || backLeft.isBusy()
                    || frontRight.isBusy() || backRight.isBusy()) {

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

    public void timedRotate (double timeOne, double timeTwo, double power, double startTime, boolean left) {

        setMotorRunMode(RUN_WITHOUT_ENCODER);

        if (left == true){

            while (l.getRuntime() - startTime >= timeOne && l.getRuntime() - startTime <= timeTwo && l.opModeIsActive()) {

                frontLeft.setPower(power);
                backLeft.setPower(power);

                frontRight.setPower(power);
                backRight.setPower(power);
            }
            stopMotors();
        }

        else {

            while (l.getRuntime() - startTime >= timeOne && l.getRuntime() - startTime <= timeTwo && l.opModeIsActive()) {

                frontLeft.setPower(-power);
                backLeft.setPower(-power);

                frontRight.setPower(-power);
                backRight.setPower(-power);
            }
            stopMotors();
        }
    }

    public void rotateLeftByGyro(double inputAngle, double power){

        setMotorRunMode(RUN_WITHOUT_ENCODER);
        setBreakMode(true);

        angles = imu.getAngularOrientation(AxesReference.INTRINSIC, AxesOrder.ZYX,AngleUnit.DEGREES);
        double currentAngle = angles.firstAngle;
        double targetAngle = currentAngle + inputAngle;

        //Left
        while (currentAngle < targetAngle) {

            angles = imu.getAngularOrientation(AxesReference.INTRINSIC, AxesOrder.ZYX,AngleUnit.DEGREES);
            currentAngle = angles.firstAngle;

            frontLeft.setPower(power);
            backLeft.setPower(power);

            frontRight.setPower(power);
            backRight.setPower(power);

            realTelemetry.addData("Z Axis", imu.getAngularOrientation(AxesReference.INTRINSIC, AxesOrder.ZYX,AngleUnit.DEGREES).firstAngle);
            realTelemetry.addData("Y Axis", imu.getAngularOrientation(AxesReference.INTRINSIC, AxesOrder.ZYX,AngleUnit.DEGREES).secondAngle);
            realTelemetry.addData("X Axis", imu.getAngularOrientation(AxesReference.INTRINSIC, AxesOrder.ZYX,AngleUnit.DEGREES).thirdAngle);

            realTelemetry.addData("Target Angle", inputAngle);

            realTelemetry.update();
        }
        stopMotors();

        l.sleep(500);
    }

    public void rotateRightByGyro(double inputAngle, double power){

        inputAngle = -inputAngle;

        setMotorRunMode(RUN_WITHOUT_ENCODER);
        setBreakMode(true);

        angles = imu.getAngularOrientation(AxesReference.INTRINSIC, AxesOrder.ZYX,AngleUnit.DEGREES);
        double currentAngle = angles.firstAngle;
        double targetAngle = currentAngle + inputAngle;

        //Left
        while (currentAngle > targetAngle) {

            angles = imu.getAngularOrientation(AxesReference.INTRINSIC, AxesOrder.ZYX,AngleUnit.DEGREES);
            currentAngle = angles.firstAngle;

            frontLeft.setPower(-power);
            backLeft.setPower(-power);

            frontRight.setPower(-power);
            backRight.setPower(-power);

            realTelemetry.addData("Z Axis", imu.getAngularOrientation(AxesReference.INTRINSIC, AxesOrder.ZYX,AngleUnit.DEGREES).firstAngle);
            realTelemetry.addData("Y Axis", imu.getAngularOrientation(AxesReference.INTRINSIC, AxesOrder.ZYX,AngleUnit.DEGREES).secondAngle);
            realTelemetry.addData("X Axis", imu.getAngularOrientation(AxesReference.INTRINSIC, AxesOrder.ZYX,AngleUnit.DEGREES).thirdAngle);

            realTelemetry.addData("Target Angle", inputAngle);

            realTelemetry.update();
        }
        stopMotors();

        l.sleep(500);
    }


    public void detectRGB(){

        realTelemetry.addData("Red  ", colorSensor.red());
        realTelemetry.addData("Green", colorSensor.green());
        realTelemetry.addData("Blue ", colorSensor.blue());

        realTelemetry.update();
    }

    public void setMotorRunMode(double mode){

        if (mode == RUN_TO_POSITION){
            frontLeft.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            backLeft.setMode(DcMotor.RunMode.RUN_TO_POSITION);

            frontRight.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            backRight.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        }

        else if (mode == RUN_USING_ENCODER) {
            frontLeft.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            backLeft.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

            frontRight.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            backRight.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        }

        else if (mode == RUN_WITHOUT_ENCODER){
            frontLeft.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
            backLeft.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

            frontRight.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
            backRight.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        }

        else if (mode == STOP_AND_RESET_ENCODER){
            frontLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            backLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

            frontRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            backRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        }
    }

    public void setBreakMode(boolean enabled){

        if (enabled == true){
            frontLeft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
            backLeft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

            frontRight.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
            backRight.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        }

        else {
            frontLeft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
            backLeft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);

            frontRight.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
            backRight.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
        }
    }

    public void stopMotors (){

        frontLeft.setPower(0);
        backLeft.setPower(0);

        frontRight.setPower(0);
        backRight.setPower(0);

    }

    public void setMarker(boolean enabled){

        if (enabled == false) {
            marker.setPosition(0);
        }

        else{
            marker.setPosition(0.5);
        }
    }
}
