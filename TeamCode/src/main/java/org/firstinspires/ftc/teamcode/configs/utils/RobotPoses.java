package org.firstinspires.ftc.teamcode.configs.utils;

import com.pedropathing.geometry.Pose;

import org.firstinspires.ftc.teamcode.R;

public class RobotPoses {
    public static class Red{
        public static class Close {
            public static Pose startPose = RobotConstants.DrivetrainConstants.RED_START_CLOSE_POSE;
            public static Pose shootPreloadPose = new Pose(101,100,Math.toRadians(45));
            public static Pose controlPoint1 = new Pose(86.152, 80.110);
            public static Pose intakeFirst = new Pose(127.534,81.790,Math.toRadians(0));
            public static class Solo{
                public static Pose startPose = RobotConstants.DrivetrainConstants.RED_START_CLOSE_POSE;
                public static Pose shootPreloadPose = new Pose(101,100,Math.toRadians(44));
                public static Pose intakeFirstControl1 = new Pose(45, 80);
                public static Pose intakeFirstPose = new Pose(128, 81,Math.toRadians(0));
                public static Pose shootFirstPose = new Pose(95.000, 90.000, Math.toRadians(46.5));
                public static Pose intakeSecondControl1 = new Pose(81.407, 49.968);
                public static Pose intakeSecondPose = new Pose(133.5, 55.4, Math.toRadians(-3));
                public static Pose shootSecondControl1 = new Pose(101.648, 56.636);
                public static Pose shootSecondPose = new Pose(84.5, 102, Math.toRadians(32));
            }
            public static class Alliance{
                public static Pose startPose = RobotConstants.DrivetrainConstants.RED_START_CLOSE_POSE;
                public static Pose shootPreloadPose = new Pose(101,100,Math.toRadians(44));
                public static Pose intakeSecondControl1 = new Pose(92.9, 46.5);
                public static Pose intakeSecondControl2 = new Pose(106,62.5);
                public static Pose intakeSecondPose = new Pose(134, 57.5, Math.toRadians(-3));
                public static Pose gateControl1 = new Pose(115,63);
                public static Pose gatePose = new Pose(128,64,Math.toRadians(-3));
                public static Pose shootSecondControl1 = new Pose(97.6, 61);
                public static Pose shootSecondPose = new Pose(90, 82, Math.toRadians(44));
                public static Pose intakeFirstPose = new Pose(128, 82,Math.toRadians(2));
                public static Pose shootFirstPose = new Pose(84.5,102,32);
            }
        }
        public static class Far{
            public static class Solo{
                public static Pose startPose = RobotConstants.DrivetrainConstants.RED_START_FAR_POSE;
                public static Pose shootPose = new Pose(84,18,Math.toRadians(63.5));
                public static Pose intakeFirstPose = new Pose(133.5, 34.8, Math.toRadians(0));
                public static Pose intakeFirstControl1 = new Pose(82.5,37.5);
                public static Pose intakeSecondPose = new Pose(131.4, 14.5, Math.toRadians(-20));
                public static Pose intakeSecondControl1 = new Pose(124.7, 27.4);
                public static Pose leavePose = new Pose(93.5,23.5);
            }
        }
    }
    public static class Blue{
        public static class Close{
            public static class Solo{
                public static Pose startPose = Red.Close.Solo.startPose.mirror();
                public static Pose shootPreloadPose = Red.Close.Solo.shootPreloadPose.mirror();
                public static Pose intakeFirstControl1 = Red.Close.Solo.intakeFirstControl1.mirror();
                public static Pose intakeFirstPose = Red.Close.Solo.intakeFirstPose.mirror();
                public static Pose shootFirstPose = Red.Close.Solo.shootFirstPose.mirror();
                public static Pose intakeSecondControl1 = Red.Close.Solo.intakeSecondControl1.mirror();
                public static Pose intakeSecondPose = Red.Close.Solo.intakeSecondPose.mirror();
                public static Pose shootSecondControl1 = Red.Close.Solo.shootSecondControl1.mirror();
                public static Pose shootSecondPose = Red.Close.Solo.shootSecondPose.mirror();
            }
            public static class Alliance{
                public static Pose startPose = Red.Close.Alliance.startPose.mirror();
                public static Pose shootPreloadPose = Red.Close.Alliance.shootPreloadPose.mirror();
                public static Pose intakeSecondControl1 = Red.Close.Alliance.intakeSecondControl1.mirror();
                public static Pose intakeSecondControl2 = Red.Close.Alliance.intakeSecondControl2.mirror();
                public static Pose intakeSecondPose = Red.Close.Alliance.intakeSecondPose.mirror();
                public static Pose gateControl1 = Red.Close.Alliance.gateControl1.mirror();
                public static Pose gatePose = Red.Close.Alliance.gatePose.mirror();
                public static Pose shootSecondControl1 = Red.Close.Alliance.shootSecondControl1.mirror();
                public static Pose shootSecondPose = Red.Close.Alliance.shootSecondPose.mirror();
                public static Pose intakeFirstPose = Red.Close.Alliance.intakeFirstPose.mirror();
                public static Pose shootFirstPose = Red.Close.Alliance.shootFirstPose.mirror();
            }

        }
        public static class Far{
            public static class Solo{
                public static Pose startPose = Red.Far.Solo.startPose.mirror();
                public static Pose shootPose = Red.Far.Solo.shootPose.mirror();
                public static Pose intakeFirstPose = Red.Far.Solo.intakeFirstPose.mirror();
                public static Pose intakeFirstControl1 = Red.Far.Solo.intakeFirstControl1.mirror();
                public static Pose intakeSecondPose = Red.Far.Solo.intakeSecondPose.mirror();
                public static Pose intakeSecondControl1 = Red.Far.Solo.intakeSecondControl1.mirror();
                public static Pose leavePose = Red.Far.Solo.leavePose.mirror();

            }

        }
    }
}
