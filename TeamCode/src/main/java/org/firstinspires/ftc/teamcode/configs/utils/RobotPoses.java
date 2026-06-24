package org.firstinspires.ftc.teamcode.configs.utils;

import com.pedropathing.geometry.Pose;

public class RobotPoses {
    public static class Red{
        public static class Close {
            public static Pose startPose = RobotConstants.DrivetrainConstants.RED_START_CLOSE_POSE;
            public static Pose shootPreloadPose = new Pose(101,100,Math.toRadians(45));
            public static Pose controlPoint1 = new Pose(86.152, 80.110);
            public static Pose intakeFirst = new Pose(127.534,81.790,Math.toRadians(0));
            public static class Solo{
                public static Pose startPose = RobotConstants.DrivetrainConstants.RED_START_CLOSE_POSE;
                public static Pose shootPreloadPose = new Pose(101,100,Math.toRadians(46));
                public static Pose intakeFirstControl1 = new Pose(45, 80);
                public static Pose intakeFirstPose = new Pose(128, 81,Math.toRadians(0));
                public static Pose shootFirstPose = new Pose(95.000, 90.000, Math.toRadians(45));
                public static Pose intakeSecondControl1 = new Pose(81.407, 49.968);
                public static Pose intakeSecondPose = new Pose(132.5, 56.25, Math.toRadians(0));
                public static Pose shootSecondControl1 = new Pose(101.648, 56.636);
                public static Pose shootSecondPose = new Pose(84.5, 102, Math.toRadians(33));
                public static Pose leavePose = new Pose(86.266, 110, Math.toRadians(90));
            }
        }
        public class Far{

        }
    }
    public class Blue{

    }
}
