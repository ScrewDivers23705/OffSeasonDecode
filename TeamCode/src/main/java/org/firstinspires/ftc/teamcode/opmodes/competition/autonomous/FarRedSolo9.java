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
import static org.firstinspires.ftc.teamcode.configs.utils.RobotPoses.Red.Far.Solo.*;

import java.util.List;
import java.util.function.BooleanSupplier;

@Autonomous(name = "FarRedSolo9", group = "RED")
@Configurable
public class FarRedSolo9 extends LinearOpMode {
    private List<LynxModule> hubs;

    /* ================================ Subsystems ================================ */
    private Drivetrain drivetrain;
    private Launcher launcher;
    private Intake intake;
    private Alliance alliance;
    /* ================================ PathChains ================================ */
    private PathChain shootPreLoad;
    private PathChain intakeClose;
    private PathChain shootFirst;
    private PathChain intakeSecond;
    private PathChain shootSecond;
    private PathChain leave;

    public void initialize()
    {
        alliance = Alliance.RED; // alliance for vision and localization
        drivetrain = new Drivetrain(hardwareMap, alliance); // construct drivetrain object
        intake = new Intake(hardwareMap, launcher); // construct intake object
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
        shootPreLoad = drivetrain.follower.pathBuilder()
                .addPath(new BezierLine(startPose, shootPose))
                .setLinearHeadingInterpolation(startPose.getHeading(), shootPose.getHeading())
                .build();
        intakeClose = drivetrain.follower.pathBuilder()
                .addPath(new BezierCurve(shootPose, intakeFirstControl1, intakeFirstPose))
                .setConstantHeadingInterpolation(intakeFirstPose.getHeading())
                .setTValueConstraint(0.95)
                .setTimeoutConstraint(500)
                .build();
        shootFirst = drivetrain.follower.pathBuilder()
                .addPath(new BezierLine(intakeFirstPose, shootPose))
                .setLinearHeadingInterpolation(intakeFirstPose.getHeading(),shootPose.getHeading() + Math.toRadians(1))
                .setTValueConstraint(0.98)
                .build();
        intakeSecond = drivetrain.follower.pathBuilder()
                .addPath(new BezierCurve(shootPose,intakeSecondControl1,intakeSecondPose))
                .setTValueConstraint(0.95)
                .setTimeoutConstraint(250)
                .setConstantHeadingInterpolation(intakeSecondPose.getHeading())
                .build();
        shootSecond = drivetrain.follower.pathBuilder()
                .addPath(new BezierLine(intakeSecondPose,shootPose))
                .setLinearHeadingInterpolation(intakeSecondPose.getHeading(), shootPose.getHeading() + Math.toRadians(2))
                .setTValueConstraint(0.98)
                .build();
        leave = drivetrain.follower.pathBuilder()
                .addPath(new BezierLine(shootPose,leavePose))
                .setLinearHeadingInterpolation(shootPose.getHeading(),leavePose.getHeading())
                .build();
    }
    private void buildCommands()
    {
        schedule(
                sequential(
                        parallel(
                                follow(drivetrain.follower,shootPreLoad),
                                launcher.runFlywheelFar()
                        ),
                        launcher.shootAutonCommand(275, 533),
                        launcher.shootAutonCommand(275, 433),
                        launcher.shootAutonCommand(275, 433),
                        instant(launcher::disable),
                        intake.intakeCommandAuton(),
                        follow(drivetrain.follower,intakeClose,true,0.6),
                        waitMs(350),
                        intake.reverseIntakeCommandAuton(),
                        waitMs(75),
                        intake.intakeCommandAuton(),
                        waitMs(300),
                        parallel(
                                follow(drivetrain.follower, shootFirst),
                                launcher.runFlywheelMid(),
                                launcher.openGate()
                        ),
                        launcher.shootAutonCommand(275, 533),
                        launcher.shootAutonCommand(275, 433),
                        launcher.shootAutonCommand(275, 433),
                        instant(launcher::disable),
                        intake.intakeCommandAuton(),
                        follow(drivetrain.follower,intakeSecond,true,0.5),
                        waitMs(750),
                        //follow(drivetrain.follower, shakeSecond, true, 0.7),
                        intake.disableIntakeCommandAuton(),
                        parallel(
                                follow(drivetrain.follower, shootSecond),
                                launcher.runFlywheelMid(),
                                launcher.openGate()
                        ),
                        launcher.shootAutonCommand(275, 533),
                        launcher.shootAutonCommand(275, 433),
                        launcher.shootAutonCommand(275, 433),
                        instant(launcher::disable),
                        follow(drivetrain.follower,leave)
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
    }
}