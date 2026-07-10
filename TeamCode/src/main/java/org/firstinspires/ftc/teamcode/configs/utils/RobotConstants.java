package org.firstinspires.ftc.teamcode.configs.utils;

import com.bylazar.configurables.annotations.Configurable;
import com.pedropathing.geometry.Pose;
@Configurable
public class RobotConstants {
    @Configurable
    public static class DrivetrainConstants {
        public static final Pose BLUE_START_CLOSE_POSE = new Pose(14, 107.5, Math.toRadians(90));
        public static final Pose BLUE_START_FAR_POSE = new Pose(62,8.2, Math.toRadians(90));
        public static final Pose RED_START_CLOSE_POSE = new Pose(127.5,107.5,Math.toRadians(90));
        public static final Pose RED_START_FAR_POSE = new Pose(86.28,8.5,Math.toRadians(90));
        public static Pose autonEndPose = new Pose(127.5,107.5,Math.toRadians(82));

    }
    @Configurable
    public static class ShooterConstants {    //constants for shooter

        public static final long FEED_TIME_MILLISECONDS = 550; // servo time to get ball to launcher
        public static final  double STOP_SPEED = 0.0; // cr servo stop
        public static final double FULL_SPEED = 0.7; // cr servo speed
        public static final double RPM_TOLERANCE = 30.0;
        public static  double kP = 0.00175;     // launcher kP //TODO tune kp
        public static  double kS = 0.03456; // launcher kS //TODO tune, check when motor starts moving
        public static  double kV = 0.00015; // launcher kA // TODO tune, check when vel=targetvel without any kp
        public static double TESTRPM = 0;
        public static double TESTANGLE = 0;
        public static LookUpTable addPoints() //TODO get new points by checking from diffrent distances
        {
            org.firstinspires.ftc.teamcode.configs.utils.LookUpTable lookUpTable = new LookUpTable(2); // create a lookUpTable

            lookUpTable.add(0, 0, 2300); //13V
            lookUpTable.add(76, 0.033,2500);//dist (CM),angle , RPM
            lookUpTable.add(89.5, 0.0435, 2600); // 13V // TODO keep working on LUT
            lookUpTable.add(102, 0.0535,2675); // 12.5V
            lookUpTable.add(120, 0.06,2750); // 12.35v
            lookUpTable.add(145, 0.06195,2950); // 12.93V //TODO
            lookUpTable.add(159, 0.065, 3100); // 13V
            lookUpTable.add(194, 0.07, 3225); // 13V //TODO
            lookUpTable.add(210, 0.077, 3425); // 13.5V
            lookUpTable.add(231, 0.08, 3605); // 13.5V
            lookUpTable.add(276, 0.08, 3760);
            lookUpTable.add(294, 0.086, 3875); // 13.2V
            return lookUpTable;
        }
    }

    public static class IntakeConstants { // constants for intake
        public static final double CLOSE_POS = 0.8; // gate pos to stop artifacts //0.575
        public static final double OPEN_POS = 0.0; // gate pos to let artifacts go through
        public static final double FORWARD_POWER = -1.0; // max intake speed
        public static final double REVERSE_POWER = 1.0; // max revers intake speed
    }

    public static class VisionConstants { // constants for vison
        public static final double kP = 0.0175     ; // drivetrain kp for rotation
        public static final double RED_GOAL_OFFSET = -6; // offset for
        public static final double BLUE_GOAL_OFFSET = -10;
        public static final int RED_ID = 24;
        public static final int BLUE_ID = 20;
        public static final double OFFSET_TOLERANCE = 1.5;
    }

    public static class KickerConstants{ // constants for kicker
        public static final int EXPAND_TIME = 1000; // Ms time for kicker to run
        public static final int COMPACT_TIME = 1000; // Ms time for kicker to compact
    }
}
