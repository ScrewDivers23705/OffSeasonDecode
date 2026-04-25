package org.firstinspires.ftc.teamcode.configs.subsystems;

import static com.qualcomm.robotcore.util.Range.clip;

import android.util.Size;

import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.teamcode.configs.utils.RobotConstants;
import org.firstinspires.ftc.vision.VisionPortal;
import org.firstinspires.ftc.vision.apriltag.AprilTagDetection;
import org.firstinspires.ftc.vision.apriltag.AprilTagProcessor;

import org.firstinspires.ftc.teamcode.configs.utils.RobotConstants.VisionConstants;

import java.util.ArrayList;
import java.util.List;

public class Vision {
    private AprilTagProcessor aprilTagProcessor;
    private VisionPortal visionPortal;
    private List<AprilTagDetection> detectedTags = new ArrayList<>();

    public static int id = 24;

    /* ========== Auto rotate variables ========= */

    public Vision(HardwareMap hwMap)
    {
        aprilTagProcessor = new AprilTagProcessor.Builder()
                .setDrawTagID(true)
                .setDrawTagOutline(true)
                .setDrawAxes(true)
                .setDrawCubeProjection(true)
                .setOutputUnits(DistanceUnit.CM, AngleUnit.DEGREES)
                .build();
        VisionPortal.Builder builder = new VisionPortal.Builder();
        builder.setCamera(hwMap.get(WebcamName.class, "Webcam 1"));
        builder.setCameraResolution(new Size(640,480));
        builder.addProcessor(aprilTagProcessor);

        visionPortal = builder.build();
    }

    public void periodic() { // periodic, call every loop
        detectedTags = aprilTagProcessor.getDetections();
    }
    /* ======================= GETTERS =======================  */

    public double getAutoRotate()
    {
        AprilTagDetection tag = getTagById(id);

        if (tag == null)
        {
            return 0;
        }

        double error = VisionConstants.GOAL_OFFSET - getOffset();

        if (Math.abs(error) < VisionConstants.OFFSET_TOLERANCE)
            return 0;

        double pTerm = error * VisionConstants.kP;

        return clip(pTerm,-0.4,0.4);
    }

    public AprilTagDetection getTagById(int id)
    {
        for (AprilTagDetection detection : detectedTags) {
            if (detection.id == id) {
                return detection;
            }
        }
        return null;
    }
    public double getDistance()
    {
        AprilTagDetection tag = getTagById(id);
        if (tag != null){
            return tag.ftcPose.range;
        }
        return 0;
    }

    public double getOffset()
    {
        AprilTagDetection tag = getTagById(id);
        if (tag != null){
            return tag.ftcPose.bearing;
        }
        return 0;
    }
    public void stop() {
        if (visionPortal != null) {
            visionPortal.close();
        }
    }
}
