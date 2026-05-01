package org.firstinspires.ftc.teamcode.configs.utils;

import com.bylazar.configurables.annotations.Configurable;
import com.pedropathing.geometry.Pose;

@Configurable
public class RobotConstants {
    public static class DrivetrainConstants {
    }
    public static class ShooterConstants {    //constants for shooter

        public static final long FEED_TIME_MILLISECONDS = 500; // servo time to get ball to launcher
        public static final  double STOP_SPEED = 0.0; // cr servo stop
        public static final double FULL_SPEED = 1.0; // cr servo speed
        public static final double RPM_TOLERANCE = 50.0;
        public static final double kP = 0.1; // launcher kP //TODO tune kp
        public static final double kS = 0.043; // launcher kS //TODO tune, check when motor starts moving
        public static final double kV = 0.00047; // launcher kA // TODO tune, check when vel=targetvel without any kp
        public static LookUpTable addPoints() //TODO get new points by checking from diffrent distances
        {
            org.firstinspires.ftc.teamcode.configs.utils.LookUpTable lookUpTable = new LookUpTable(2); // create a lookUpTable

            lookUpTable.add(0,0,2500);
            lookUpTable.add(300,0,2500);
            //lookUpTable.add(72, 0.26, 3100); //dist (CM),angle , RPM
            //lookUpTable.add(86, 0.265, 3350);
            //lookUpTable.add(100,0.27, 3600);
            //lookUpTable.add(114, 0.28, 3800);
            //lookUpTable.add(126, 0.285, 4115);
            //lookUpTable.add(150, 0.285, 4345);
            //lookUpTable.add(207, 0.293, 4670);
            //lookUpTable.add(245,0.289,5100);

            return lookUpTable;
        }
    }

    public static class IntakeConstants { // constants for intake
        public static final double CLOSE_POS = 0.49; // gate pos to stop artifacts
        public static final double OPEN_POS = 0.1; // gate pos to let artifacts go through
        public static final double FORWARD_POWER = -1.0; // max intake speed
        public static final double REVERSE_POWER = 1.0; // max revers intake speed
    }

    public static class VisionConstants { // constants for vison
        public static final double kP = 0.02 ; // drivetrain kp for rotation
        public static final double RED_GOAL_OFFSET = -12; // offset for
        public static final double BLUE_GOAL_OFFSET = 12; //todo make sure
        public static final int RED_ID = 24;
        public static final int BLUE_ID = 20;
        public static final double OFFSET_TOLERANCE = 1.5;
    }

    public static class KickerConstants{ // constants for kicker
        public static final int EXPAND_TIME = 1000; // Ms time for kicker to run
        public static final int COMPACT_TIME = 1000; // Ms time for kicker to compact
    }
}
