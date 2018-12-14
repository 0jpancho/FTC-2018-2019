package org.firstinspires.ftc.teamcode.VisionAuto;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer;
import org.firstinspires.ftc.teamcode.Subsystems.DriveTrain;
import org.firstinspires.ftc.teamcode.Subsystems.Hanger;
import org.firstinspires.ftc.teamcode.vision.MasterVision;
import org.firstinspires.ftc.teamcode.vision.SampleRandomizedPositions;

@Autonomous(name = "CraterVision")
public class DepotVision extends LinearOpMode {

    MasterVision vision;
    SampleRandomizedPositions goldPosition;

    @Override
    public void runOpMode(){

        VuforiaLocalizer.Parameters parameters = new VuforiaLocalizer.Parameters();
        parameters.cameraDirection = VuforiaLocalizer.CameraDirection.BACK;// recommended camera direction
        parameters.vuforiaLicenseKey = "ATTrTZj/////AAAAGcuqIv1tG0l2rtJHyHTkjMQg/aogzK1jRT4UF+1z1+ElhoWZYNSXXslT5PmhJZYURIBfaKiLT6AiZ58eOj9E6UMI16UXSek86LW0GK0pvVzCfX7N594z4UH+c7H4MCnnB0urwR25TRXbn/WE65bKMJkj3DJps2yV+5e7gwvb6ccHrRTd+BVUvgbeCA/u1tSbL/nUq49ar0xKDYxUpnSXvvc1TAF4rWyHGMAQx1IU/cvxme8ta4qbv724IjtJVh1NS1aJp/ybtPpMlLh96yIFc6nDFr3EAPolKw/MAV71FJB1D0Bpu20TgOUQH2gZ9nxxmShx9QKIIId32qKs/EdEjS659IqI9d8eOatQ/CZBTpQo";

        vision = new MasterVision(parameters, hardwareMap, true, MasterVision.TFLiteAlgorithm.INFER_NONE);
        vision.init();// enables the camera overlay. this will take a couple of seconds
        vision.enable();// enables the tracking algorithms. this might also take a little time

        DriveTrain driveTrain = new DriveTrain(DepotVision.this, hardwareMap, telemetry);
        Hanger hanger = new Hanger(DepotVision.this, hardwareMap, telemetry);

        waitForStart();

        goldPosition = vision.getTfLite().getLastKnownSampleOrder();

        waitForStart();

        goldPosition = vision.getTfLite().getLastKnownSampleOrder();

        while(opModeIsActive()){
            telemetry.addData("goldPosition was", goldPosition);// giving feedback

            vision.disable();

            visionTelemetry();

            if (goldPosition == SampleRandomizedPositions.LEFT){

                driveTrain.encoderDrive(13.5, 1, DriveTrain.Direction.BACKWARD);

                driveTrain.encoderDrive(24,1, DriveTrain.Direction.STRAFE_LEFT);

                visionTelemetry();

                break;
            }

            else if (goldPosition == SampleRandomizedPositions.CENTER)
            {
                visionTelemetry();

                driveTrain.encoderDrive(44,0.5, DriveTrain.Direction.STRAFE_LEFT);

                driveTrain.setMarker(true);

                driveTrain.rotateRightByGyro(44,0.35);

                driveTrain.setMarker(false);

                driveTrain.encoderDrive(12, 0.5, DriveTrain.Direction.STRAFE_LEFT);

                driveTrain.encoderDrive(3,1, DriveTrain.Direction.STRAFE_RIGHT);

                driveTrain.rotateLeftByGyro(2, 0.5 );

                driveTrain.encoderDrive(84, 1, DriveTrain.Direction.FORWARD);

                break;
            }

            else if (goldPosition == SampleRandomizedPositions.RIGHT) {

                visionTelemetry();

                driveTrain.encoderDrive(13.5, 1, DriveTrain.Direction.FORWARD);

                driveTrain.encoderDrive(24,1, DriveTrain.Direction.STRAFE_LEFT);

                break;
            }

            else if (goldPosition == SampleRandomizedPositions.UNKNOWN) {

                driveTrain.encoderDrive(24, 1, DriveTrain.Direction.STRAFE_LEFT);

                visionTelemetry();
            }
            telemetry.update();
        }
        vision.shutdown();
    }

    public void visionTelemetry(){
        telemetry.addData("Current Mineral Pos", goldPosition);

        telemetry.update();
    }
}
