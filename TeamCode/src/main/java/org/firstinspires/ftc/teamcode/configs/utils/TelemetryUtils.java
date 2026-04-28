package org.firstinspires.ftc.teamcode.configs.utils;

import org.firstinspires.ftc.robotcore.external.Telemetry;

import com.bylazar.field.FieldManager;
import com.bylazar.field.PanelsField;

import com.bylazar.telemetry.PanelsTelemetry;
import com.bylazar.telemetry.TelemetryManager;
import com.bylazar.camerastream.PanelsCameraStream;

// Your Subsystems
import org.firstinspires.ftc.teamcode.configs.subsystems.Drivetrain;
import org.firstinspires.ftc.teamcode.configs.subsystems.Launcher;
import org.firstinspires.ftc.teamcode.configs.subsystems.Vision;
import org.firstinspires.ftc.teamcode.configs.subsystems.Intake;

public class TelemetryUtils {

    private final Telemetry baseTelemetry;
    private final TelemetryManager panelsTelemetry;
    private final FieldManager fieldManager;

    // Subsystem References
    private final Drivetrain dt;
    private final Launcher launcherSubsystem;
    private final Vision vision;
    private final Intake intake;

    public TelemetryUtils(Telemetry baseTelemetry, Drivetrain dt, Launcher launcher, Vision vision, Intake intake) {
        this.baseTelemetry = baseTelemetry;
        this.panelsTelemetry = PanelsTelemetry.INSTANCE.getTelemetry();
        this.fieldManager = PanelsField.INSTANCE.getField();

        this.dt = dt;
        this.launcherSubsystem = launcher;
        this.vision = vision;
        this.intake = intake;

        // Start stream right here
        if (vision != null && vision.getPortal() != null) {
            PanelsCameraStream.INSTANCE.startStream(vision.getPortal(), 30);
        }
    }

    /**
     * Call this in your OpMode to enable the webcam stream in the Panels Dashboard.
     * Works best with OpenCv/VisionPortal.
     */

    public void stopCameraStream() {
        PanelsCameraStream.INSTANCE.stopStream();
    }

    /**
     * Updates everything: Text telemetry, Field drawings, and physical Driver Station.
     */
    public void updateAll() {
        // 1. Queue Text Telemetry
        updateDrivetrainText();
        updateLauncherText();
        updateVisionText();
        updateIntakeText();

        // 2. Draw on the Field Panel
        drawField();

        // 3. Push to both Panels Web UI and Driver Station
        panelsTelemetry.update(baseTelemetry);
    }

    private void updateDrivetrainText() {
        panelsTelemetry.debug("── DRIVETRAIN ──────────────────");
        if (dt != null && dt.follower != null) {
            panelsTelemetry.debug(String.format("Heading: %.1f°", Math.toDegrees(dt.follower.getPose().getHeading())));
            panelsTelemetry.debug(String.format("X / Y: %.1f | %.1f cm", dt.follower.getPose().getX(), dt.follower.getPose().getY()));
        } else {
            panelsTelemetry.debug("Status: DISCONNECTED");
        }
        panelsTelemetry.debug(""); // Spacer
    }

    private void updateLauncherText() {
        panelsTelemetry.debug("── LAUNCHER ────────────────────");
        if (launcherSubsystem != null) {
            panelsTelemetry.debug(String.format("RPM: %.0f / %.0f", launcherSubsystem.getRPM(), launcherSubsystem.getTarget()));
            panelsTelemetry.debug("Ready: " + (launcherSubsystem.isReady() ? "✓ YES" : "✗ NO"));
        } else {
            panelsTelemetry.debug("Status: DISCONNECTED");
        }
        panelsTelemetry.debug("");
    }

    private void updateVisionText() {
        panelsTelemetry.debug("── VISION ──────────────────────");
        if (vision != null) {
            double dist = vision.getDistance();
            panelsTelemetry.debug("Detected: " + (dist > 0 ? "✓" : "✗"));
            panelsTelemetry.debug(String.format("Dist: %.1f cm | Offset: %.2f°", dist, vision.getOffset()));
        }
        panelsTelemetry.debug("");
    }

    private void updateIntakeText() {
        panelsTelemetry.debug("── INTAKE ──────────────────────");
        if (intake != null) {
            String state = intake.getState() == 1 ? "▶ IN" : intake.getState() == -1 ? "◀ OUT" : "■ OFF";
            panelsTelemetry.debug("State: " + state);
        }
        panelsTelemetry.debug("");
    }

    /**
     * Handles the visual field drawing in the Panels "Field View" tab.
     */
    private void drawField() {
        if (dt != null && dt.follower != null) {
            double x = dt.follower.getPose().getX();
            double y = dt.follower.getPose().getY();
            double heading = dt.follower.getPose().getHeading();

            // 1. Set the style (fill color, outline color, outline width)
            fieldManager.setStyle("#3F51B5", "#FFFFFF", 1.0);

            // 2. Move cursor to robot position
            fieldManager.moveCursor(x, y);

            // 3. Draw the robot body as a circle
            fieldManager.circle(9.0); // radius in cm

            // 4. Draw a heading line (from robot center to a point in front)
            double lineLength = 15.0;
            fieldManager.line(
                    x + lineLength * Math.cos(heading),
                    y + lineLength * Math.sin(heading)
            );

            // 5. Push the canvas
            fieldManager.update();
        }
    }

    public void update() {
        panelsTelemetry.update(baseTelemetry);
    }
}