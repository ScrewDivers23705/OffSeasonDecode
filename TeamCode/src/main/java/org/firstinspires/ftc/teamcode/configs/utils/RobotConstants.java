package org.firstinspires.ftc.teamcode.configs.utils;

import com.bylazar.configurables.annotations.Configurable;

@Configurable
public class RobotConstants {
    //constants for shooter
    public static class ShooterConstants {
        public static final long FEED_TIME_MILLISECONDS = 250; // servo time to get ball to launcher
        public static final  double STOP_SPEED = 0.0; // cr servo stop
        public static final double FULL_SPEED = 1.0; // cr servo speed
        public static final double RPM_TOLERANCE = 50.0;
        public static final double kP = 0.1; // launcher kP //TODO tune kp
        public static final double kS = 13; // launcher kS //TODO tune, check when motor starts moving
        public static final double kV = 13; // launcher kA // TODO tune, check when vel=targetvel without any kp

        public LookUpTable addPoints(LookUpTable lookUpTable)
        {
            lookUpTable.add(72, 0.26, 1327); //dist (CM),angle , RPM
            lookUpTable.add(86, 0.265, 1350);
            lookUpTable.add(100,0.27, 1385);
            lookUpTable.add(114, 0.28, 1455);
            lookUpTable.add(126, 0.285, 1515);
            lookUpTable.add(150, 0.285, 1605);
            lookUpTable.add(207, 0.293, 1770);
            lookUpTable.add(245,0.289,1837);

            return lookUpTable;
        }
    }

    public static class IntakeConstants {
        public static final double CLOSE_POS = 0.49; // gate pos to stop artifacts
        public static final double OPEN_POS = 0.1; // gate pos to let artifacts go through
        public static final double FORWARD_POWER = -1.0; // max intake speed
        public static final double REVERSE_POWER = 1.0; // max revers intake speed
    }

    public static class VisionConstants {
        public static final double kP = 0.025;
        public static final double kD = 0.0;
        public static final double GOAL_OFFSET = -12;
        public static final double OFFSET_TOLERANCE = 2;
    }
}
