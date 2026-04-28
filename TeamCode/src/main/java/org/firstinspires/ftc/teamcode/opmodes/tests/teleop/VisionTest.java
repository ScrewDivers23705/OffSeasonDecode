package org.firstinspires.ftc.teamcode.opmodes.tests.teleop;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.configs.utils.VisionUtils;
import org.firstinspires.ftc.vision.apriltag.AprilTagDetection;

@TeleOp(name = "Vision test", group = "test")
public class VisionTest extends OpMode {

    VisionUtils aprilTagWebcam = new VisionUtils();

    @Override
    public void init()
    {
        aprilTagWebcam.init(hardwareMap, telemetry);
    }
    @Override
    public void loop()
    {
        aprilTagWebcam.update();
        AprilTagDetection id20 = aprilTagWebcam.getTagById(24);
        telemetry.addData("isnull?",id20);
        aprilTagWebcam.displayDetectionTelemetry(id20);
        telemetry.addLine("Testing");
        telemetry.update();
    }
}