package org.firstinspires.ftc.teamcode.VisionAuto;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer;
import org.firstinspires.ftc.teamcode.Subsystems.DriveTrain;
import org.firstinspires.ftc.teamcode.Subsystems.Hanger;
import org.firstinspires.ftc.teamcode.vision.MasterVision;
import org.firstinspires.ftc.teamcode.vision.SampleRandomizedPositions;

@Disabled
@Autonomous(name = "CraterVision", group = "Main Autonomous")
public class CraterVision extends LinearOpMode {
    MasterVision vision;
    SampleRandomizedPositions goldPosition;

    @Override
    public void runOpMode() throws InterruptedException {

        VuforiaLocalizer.Parameters parameters = new VuforiaLocalizer.Parameters();
        parameters.cameraDirection = VuforiaLocalizer.CameraDirection.BACK;// recommended camera direction
        parameters.vuforiaLicenseKey = "ATTrTZj/////AAAAGcuqIv1tG0l2rtJHyHTkjMQg/aogzK1jRT4UF+1z1+ElhoWZYNSXXslT5PmhJZYURIBfaKiLT6AiZ58eOj9E6UMI16UXSek86LW0GK0pvVzCfX7N594z4UH+c7H4MCnnB0urwR25TRXbn/WE65bKMJkj3DJps2yV+5e7gwvb6ccHrRTd+BVUvgbeCA/u1tSbL/nUq49ar0xKDYxUpnSXvvc1TAF4rWyHGMAQx1IU/cvxme8ta4qbv724IjtJVh1NS1aJp/ybtPpMlLh96yIFc6nDFr3EAPolKw/MAV71FJB1D0Bpu20TgOUQH2gZ9nxxmShx9QKIIId32qKs/EdEjS659IqI9d8eOatQ/CZBTpQo";

        vision = new MasterVision(parameters, hardwareMap, true, MasterVision.TFLiteAlgorithm.INFER_NONE);
        vision.init();// enables the camera overlay. this will take a couple of seconds
        vision.enable();// enables the tracking algorithms. this might also take a little time

        DriveTrain driveTrain = new DriveTrain(CraterVision.this, hardwareMap, telemetry);
        Hanger hanger = new Hanger(CraterVision.this, hardwareMap, telemetry);

        waitForStart();

        goldPosition = vision.getTfLite().getLastKnownSampleOrder();

        while(opModeIsActive() && !isStopRequested()){
            telemetry.addData("goldPosition was", goldPosition);// giving feedback

            vision.disable();


            //Come down from Lander
            hanger.moveByEncoder(24, 0.25, Hanger.DOWN);

            //Reposition from hook
            driveTrain.driveByEncoder(6, 1, DriveTrain.Direction.BACKWARD);
            driveTrain.driveByEncoder(6,1, DriveTrain.Direction.STRAFE_LEFT);
            driveTrain.driveByEncoder(6, 1, DriveTrain.Direction.FORWARD);

            visionTelemetry();

            if (goldPosition == SampleRandomizedPositions.LEFT){

                driveTrain.driveByEncoder(13.5, 1, DriveTrain.Direction.BACKWARD);
                driveTrain.driveByEncoder(24,1, DriveTrain.Direction.STRAFE_LEFT);

                visionTelemetry();

                break;
            }

            else if (goldPosition == SampleRandomizedPositions.CENTER)
            {
                visionTelemetry();

                driveTrain.driveByEncoder(24, 1, DriveTrain.Direction.STRAFE_LEFT);
                break;
            }

            else if (goldPosition == SampleRandomizedPositions.RIGHT) {
                driveTrain.driveByEncoder(13.5, 1, DriveTrain.Direction.FORWARD);
                driveTrain.driveByEncoder(24,1, DriveTrain.Direction.STRAFE_LEFT);

                visionTelemetry();

                break;
            }

            else if (goldPosition == SampleRandomizedPositions.UNKNOWN) {

                driveTrain.driveByEncoder(24, 1, DriveTrain.Direction.STRAFE_LEFT);

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
