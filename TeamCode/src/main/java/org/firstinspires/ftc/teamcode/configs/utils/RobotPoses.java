package org.firstinspires.ftc.teamcode.configs.utils;

import com.pedropathing.geometry.Pose;

public class RobotPoses {
    public static class Red{
        public static class Close {
            public static Pose startPose = RobotConstants.DrivetrainConstants.RED_START_CLOSE_POSE;
            public static Pose shootPreloadPose = new Pose(101,100,Math.toRadians(45));
            public static Pose controlPoint1 = new Pose(86.152, 80.110);
            public static Pose intakeFirst = new Pose(126.534,81.790,Math.toRadians(0));
            public static class Solo{
                public static Pose startPose = RobotConstants.DrivetrainConstants.RED_START_CLOSE_POSE;
                public static Pose shootPreloadPose = new Pose(101,100,Math.toRadians(45));
                public static Pose intakeFirstControl1 = new Pose(83.485, 79.326);
                public static Pose intakeFirstPose = new Pose(126.562, 81.634,Math.toRadians(0));
                public static Pose shootFirstPose = new Pose(95.000, 90.000, Math.toRadians(43));
                public static Pose intakeSecondControl1 = new Pose(81.407, 52.968);
                public static Pose intakeSecondPose = new Pose(132.876, 57.102, Math.toRadians(0));
                public static Pose shootSecondControl1 = new Pose(101.648, 56.636);
                public static Pose shootSecondPose = new Pose(86.266, 85.113, Math.toRadians(38));

            }
        }
        public class Far{

        }
    }
    public class Blue{

    }
}
