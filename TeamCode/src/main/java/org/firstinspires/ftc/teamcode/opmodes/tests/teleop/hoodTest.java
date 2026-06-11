package org.firstinspires.ftc.teamcode.opmodes.tests.teleop;

import com.bylazar.configurables.annotations.Configurable;
import com.pedropathing.ivy.Scheduler;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.configs.subsystems.Intake;
import org.firstinspires.ftc.teamcode.configs.subsystems.Launcher;
import org.firstinspires.ftc.teamcode.configs.subsystems.Vision;
import org.firstinspires.ftc.teamcode.configs.utils.Alliance;
import org.firstinspires.ftc.teamcode.configs.utils.RobotConstants;

import com.bylazar.telemetry.PanelsTelemetry;
import com.bylazar.telemetry.TelemetryManager;
import com.qualcomm.robotcore.hardware.Servo;

@TeleOp(name="hoodTest Test", group = "Tests")
@Configurable
public class hoodTest extends OpMode {
    private Launcher launcher;
    private Intake intake;
    private Alliance alliance;
    private Telemetry baseTelemetry;
    private TelemetryManager panelsTelemetry;
    private Vision vision;
    private Servo servo;
    public void init()
    {
        alliance = Alliance.RED;
        intake = new Intake(hardwareMap, launcher);
        launcher = new Launcher(hardwareMap, intake);
        vision = new Vision(hardwareMap, alliance); // construct the camera object
        servo = hardwareMap.get(Servo.class, "hood");

        this.baseTelemetry = telemetry;
        this.panelsTelemetry = PanelsTelemetry.INSTANCE.getTelemetry();
    }

    public void loop() {
        if (gamepad2.right_bumper) {
            Scheduler.schedule(launcher.SHOOTBYVALUEFORTEST(RobotConstants.ShooterConstants.TESTRPM, RobotConstants.ShooterConstants.TESTANGLE));
        }
        launcher.periodic();
        vision.periodic();
        Scheduler.execute();
    }

    private void updateLauncherText() {
        panelsTelemetry.debug("── LAUNCHER ────────────────────");
        if (launcher != null) {
            panelsTelemetry.debug(String.format("RPM: %.0f / %.0f", launcher.getRPM(), launcher.getTarget()));
            panelsTelemetry.debug("Ready: " + (launcher.isReady() ? "✓ YES" : "✗ NO"));
        } else {
            panelsTelemetry.debug("Status: DISCONNECTED");
        }
        panelsTelemetry.debug("");
    }
}
