package org.firstinspires.ftc.teamcode.opmodes.competition.autonomous;




import static com.pedropathing.ivy.Scheduler.schedule;
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
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.configs.subsystems.Drivetrain;
import org.firstinspires.ftc.teamcode.configs.subsystems.Intake;
import org.firstinspires.ftc.teamcode.configs.subsystems.Launcher;
import org.firstinspires.ftc.teamcode.configs.subsystems.Vision;
import org.firstinspires.ftc.teamcode.configs.utils.Alliance;
import static org.firstinspires.ftc.teamcode.configs.utils.RobotPoses.Red.Close.Solo.*;
import org.firstinspires.ftc.teamcode.configs.utils.TelemetryUtils;
@Autonomous(name = "CloseRedSolo9", group = "RED")
@Configurable
public class CloseRedSolo9 extends LinearOpMode {


    /* ================================ Subsystems ================================ */
    private Drivetrain drivetrain;
    private Launcher launcher;
    private Intake intake;
    private Vision vision;
    private TelemetryUtils comms;
    private Alliance alliance;
    /* ================================ PathChains ================================ */
    private PathChain preLoadsPose;
    private PathChain intakeClose;
    private PathChain shootClose;
    private PathChain intakeSecond;
    private PathChain shootSecond;
    private PathChain leave;
    private PathChain shakeFirst;
    private PathChain shakeSecond;


    public void initialize()
    {
        alliance = Alliance.RED; // alliance for vision and localization
        drivetrain = new Drivetrain(hardwareMap, alliance); // construct drivetrain object
        intake = new Intake(hardwareMap, launcher); // construct intake object
        launcher = new Launcher(hardwareMap, intake); // construct the launcher object
        vision = new Vision(hardwareMap, alliance); // construct the camera object
        comms = new TelemetryUtils(telemetry, drivetrain, launcher, vision, intake); // construct the telemtryutils object sending it all the data
        Scheduler.reset();

        drivetrain.follower.setPose(startPose);

        buildPaths();
    }
    private void buildPaths()
    {
        preLoadsPose = drivetrain.follower.pathBuilder()
                .addPath(new BezierLine(startPose, shootPreloadPose))
                .setLinearHeadingInterpolation(startPose.getHeading(), shootPreloadPose.getHeading())
                .build();
        intakeClose = drivetrain.follower.pathBuilder()
                .addPath(new BezierCurve(drivetrain.follower.getPose(), intakeFirstControl1, intakeFirstControl1 ,intakeFirstControl1, intakeFirstPose))
                .setConstantHeadingInterpolation(intakeFirstPose.getHeading())
                .build();
        shakeFirst = drivetrain.follower.pathBuilder()
                .addPath(
                        new BezierCurve(
                                new Pose(127.000, 81),
                                new Pose(128.071, 80.568),
                                new Pose(128.000, 80.600)
                        )
                )
                .setTimeoutConstraint(800)
                .setTangentHeadingInterpolation()
                .build();
        shootClose = drivetrain.follower.pathBuilder()
                .addPath(new BezierLine(intakeFirstPose, shootFirstPose))
                .setLinearHeadingInterpolation(intakeFirstPose.getHeading(),shootFirstPose.getHeading())
                .setTValueConstraint(0.925)
                .build();
        intakeSecond = drivetrain.follower.pathBuilder()
                .addPath(new BezierCurve(shootFirstPose,intakeSecondControl1,intakeSecondPose))
                .setConstantHeadingInterpolation(intakeSecondPose.getHeading())
                .setTValueConstraint(0.95)
                .build();
        shakeSecond = drivetrain.follower.pathBuilder()
                .addPath(
                        new BezierCurve(
                                new Pose(132.5, 56.25),
                                new Pose(132.571, 56),
                                new Pose(133.5, 56.35)
                        )
                )
                .setTValueConstraint(0.925)
                .setTangentHeadingInterpolation()
                .build();
        shootSecond = drivetrain.follower.pathBuilder()
                .addPath(new BezierCurve(new Pose(133.5,56.35),shootSecondControl1,shootSecondPose))
                .setLinearHeadingInterpolation(intakeSecondPose.getHeading(), shootSecondPose.getHeading())
                .setTValueConstraint(0.925)
                .build();
        leave = drivetrain.follower.pathBuilder()
                .addPath(new BezierLine(shootPreloadPose,leavePose))
                .setLinearHeadingInterpolation(shootSecondPose.getHeading(),leavePose.getHeading())
                .build();
    }
    private void buildCommands()
    {
        schedule(
                sequential(
                        follow(drivetrain.follower,preLoadsPose),
                        launcher.shootAutonCommand(90, 292),
                        waitMs(25),
                        launcher.shootAutonCommand(90, 263),
                        waitMs(30),
                        launcher.shootAutonCommand(90, 290),
                        intake.intakeCommandAuton(),
                        follow(drivetrain.follower,intakeClose,true,0.7),
                        waitMs(500),
                        follow(drivetrain.follower, shakeFirst, true , 0.75),
                        waitMs(600),
                        intake.disableIntakeCommandAuton(),
                        parallel(
                                follow(drivetrain.follower, shootClose),
                                launcher.runFlywheelMid()
                        ),
                        launcher.openGate(),
                        waitMs(75),
                        launcher.shootAutonCommand(110, 270),
                        waitMs(25),
                        launcher.shootAutonCommand(110, 265),
                        waitMs(25),
                        launcher.shootAutonCommand(110, 265),
                        intake.intakeCommandAuton(),
                        follow(drivetrain.follower,intakeSecond,true,0.7),
                        waitMs(500),
                        follow(drivetrain.follower, shakeSecond, true, 0.7),
                        waitMs(600),
                        intake.disableIntakeCommandAuton(),
                        parallel(
                                follow(drivetrain.follower, shootSecond),
                                launcher.runFlywheelMid()
                        ),
                        launcher.openGate(),
                        waitMs(75),
                        launcher.shootAutonCommand(125, 270),
                        waitMs(25),
                        launcher.shootAutonCommand(125, 265),
                        waitMs(25),
                        launcher.shootAutonCommand(125, 265),
                        follow(drivetrain.follower,leave)
                )
        );
    }

    public void runOpMode() {

        initialize();

        waitForStart();
        buildCommands();
        while (opModeIsActive()) {
            Scheduler.execute();
            drivetrain.periodic();
            launcher.periodic();
            vision.periodic();
            comms.periodic();
        }
    }
}