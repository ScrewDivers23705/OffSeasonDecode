package org.firstinspires.ftc.teamcode.opmodes.competition.autonomous;




import static com.pedropathing.ivy.Scheduler.schedule;
import static com.pedropathing.ivy.commands.Commands.instant;
import static com.pedropathing.ivy.commands.Commands.waitMs;
import static com.pedropathing.ivy.groups.Groups.parallel;
import static com.pedropathing.ivy.groups.Groups.sequential;
import static com.pedropathing.ivy.pedro.PedroCommands.follow;

import com.bylazar.configurables.annotations.Configurable;
import com.pedropathing.geometry.BezierCurve;
import com.pedropathing.geometry.BezierLine;
import com.pedropathing.geometry.Pose;
import com.pedropathing.ivy.Scheduler;
import com.pedropathing.paths.PathChain;
import com.qualcomm.hardware.lynx.LynxModule;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.configs.subsystems.Drivetrain;
import org.firstinspires.ftc.teamcode.configs.subsystems.Intake;
import org.firstinspires.ftc.teamcode.configs.subsystems.Launcher;
import org.firstinspires.ftc.teamcode.configs.utils.Alliance;
import org.firstinspires.ftc.teamcode.configs.utils.RobotConstants;

import static org.firstinspires.ftc.teamcode.configs.utils.RobotPoses.Red.Close.Alliance.*;

import java.util.List;

@Autonomous(name = "CloseRedAlliance9", group = "RED")
@Configurable
public class CloseRedAlliance9 extends LinearOpMode {
    private List<LynxModule> hubs;

    /* ================================ Subsystems ================================ */
    private Drivetrain drivetrain;
    private Launcher launcher;
    private Intake intake;
    private Alliance alliance;
    /* ================================ PathChains ================================ */
    private PathChain preLoadsPose;
    private PathChain intakeSecond;
    private PathChain openGate;
    private PathChain shootSecond;
    private PathChain intakeFirst;
    private PathChain shootFirst;


    public void initialize()
    {
        alliance = Alliance.RED; // alliance for vision and localization
        drivetrain = new Drivetrain(hardwareMap, alliance); // construct drivetrain object
        intake = new Intake(hardwareMap); // construct intake object
        launcher = new Launcher(hardwareMap, intake); // construct the launcher object
        Scheduler.reset();

        drivetrain.follower.setPose(startPose);

        buildPaths();

        hubs = hardwareMap.getAll(LynxModule.class);
        for (LynxModule h : hubs)
            h.setBulkCachingMode(LynxModule.BulkCachingMode.MANUAL);
        Scheduler.reset(); // Clean schedule before running

    }
    private void buildPaths()
    {
        preLoadsPose = drivetrain.follower.pathBuilder()
                .addPath(new BezierLine(startPose, shootPreloadPose))
                .setLinearHeadingInterpolation(startPose.getHeading(), shootPreloadPose.getHeading())
                .build();
        intakeSecond = drivetrain.follower.pathBuilder()
                .addPath(new BezierCurve(shootPreloadPose, intakeSecondControl1, intakeSecondControl2, intakeSecondPose))
                .setConstantHeadingInterpolation(intakeFirstPose.getHeading())
                .build();
        openGate = drivetrain.follower.pathBuilder()
                .addPath(new BezierCurve(intakeSecondPose, gateControl1, gatePose))
                .setConstantHeadingInterpolation(gatePose.getHeading())
                .build();
        shootSecond = drivetrain.follower.pathBuilder()
                .addPath(new BezierCurve(gatePose, shootSecondControl1, shootSecondPose))
                .setLinearHeadingInterpolation(gatePose.getHeading(),shootSecondPose.getHeading())
                .setTValueConstraint(0.995)
                .build();
        intakeFirst = drivetrain.follower.pathBuilder()
                .addPath(new BezierLine(shootSecondPose, intakeFirstPose))
                .setTValueConstraint(0.85)
                .setTimeoutConstraint(500)
                .setConstantHeadingInterpolation(intakeSecondPose.getHeading())
                .build();
        shootFirst = drivetrain.follower.pathBuilder()
                .addPath(new BezierLine(intakeFirstPose, shootFirstPose))
                .setLinearHeadingInterpolation(intakeSecondPose.getHeading(), shootFirstPose.getHeading() + Math.toRadians(1))
                .setTValueConstraint(0.98)
                .setTimeoutConstraint(250)
                .build();
    }
    private void buildCommands()
    {
        schedule(
                sequential(
                        parallel(
                                follow(drivetrain.follower,preLoadsPose),
                                launcher.runFlywheelClose()
                        ),
                        launcher.buildShootCommand(89.5),
                        waitMs(25),
                        launcher.buildShootCommand(89.5),
                        waitMs(25),
                        launcher.buildShootCommand(89.5),
                        instant(launcher::disable),
                        intake.intakeCommandAuton(launcher),
                        follow(drivetrain.follower, intakeFirst,true,0.5),
                        waitMs(350),
                        intake.reverseIntakeCommandAuton(),
                        waitMs(50),
                        intake.intakeCommandAuton(launcher),
                        waitMs(300),
                        intake.disableIntakeCommandAuton(),
                        follow(drivetrain.follower, openGate, true, 0.8),
                        parallel(
                                follow(drivetrain.follower, shootSecond),
                                launcher.runFlywheelMid(),
                                launcher.openGate()
                        ),
                        launcher.buildShootCommand(152),
                        waitMs(25),
                        launcher.buildShootCommand(152),
                        waitMs(25),
                        launcher.buildShootCommand(152),
                        instant(launcher::disable),
                        intake.intakeCommandAuton(launcher),
                        follow(drivetrain.follower, intakeSecond,true,0.5),
                        waitMs(350),
                        intake.reverseIntakeCommandAuton(),
                        waitMs(75),
                        intake.intakeCommandAuton(launcher),
                        waitMs(300),
                        intake.disableIntakeCommandAuton(),
                        parallel(
                                follow(drivetrain.follower, shootFirst),
                                launcher.runFlywheelMid(),
                                launcher.openGate()
                        ),
                        launcher.buildShootCommand(130),
                        waitMs(25),
                        launcher.buildShootCommand(130),
                        waitMs(25),
                        launcher.buildShootCommand(130), //TODO check for acutal distance
                        instant(launcher::disable)
                )
        );
    }

    public void runOpMode() {

        initialize();

        waitForStart();
        buildCommands();
        while (opModeIsActive()) {
            for (LynxModule h : hubs) h.clearBulkCache();
            drivetrain.periodic();
            launcher.periodic();
            Scheduler.execute();
        }
        RobotConstants.DrivetrainConstants.autonEndPose = drivetrain.follower.getPose();

    }
}