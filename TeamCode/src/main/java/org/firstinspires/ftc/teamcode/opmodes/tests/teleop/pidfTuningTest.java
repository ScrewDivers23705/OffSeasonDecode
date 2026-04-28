package org.firstinspires.ftc.teamcode.opmodes.tests.teleop;

import com.bylazar.configurables.annotations.Configurable;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.configs.subsystems.Intake;
import org.firstinspires.ftc.teamcode.configs.subsystems.Launcher;

import com.bylazar.telemetry.PanelsTelemetry;
import com.bylazar.telemetry.TelemetryManager;

@TeleOp(name="PIDF Tuning Test", group = "Tests")
@Configurable
public class pidfTuningTest extends OpMode {
    private Launcher launcher;
    private Intake intake;

    private Telemetry baseTelemetry;
    private TelemetryManager panelsTelemetry;

    public static double kP = 0;
    public static double kS = 0;
    public static double kV = 0;

    public void init()
    {
        intake = new Intake(hardwareMap);
        launcher = new Launcher(hardwareMap, intake);

        this.baseTelemetry = telemetry;
        this.panelsTelemetry = PanelsTelemetry.INSTANCE.getTelemetry();
    }

    public void loop()
    {
        if (gamepad1.right_bumper)
        {
            launcher.setPower(kS);
        }
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
