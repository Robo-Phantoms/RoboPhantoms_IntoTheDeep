package org.firstinspires.ftc.teamcode.teleop;



import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;
import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.dashboard.telemetry.MultipleTelemetry;

@Config
@TeleOp(name="BasicRobot2425")
public class BasicRobot2425 extends OpMode {
    private DcMotorEx Intake2;
    DcMotor FrontLeft = null;
    DcMotor FrontRight = null;
    DcMotor BackLeft = null;
    DcMotor BackRight = null;
    Servo claw = null;
    private DcMotorEx Intake;
    DcMotor Act1 = null;
    DcMotor Act2= null;
    Servo elbow = null;
    Servo elbow_2 = null;
    boolean isClawOpen = false;
    boolean wasXPressed = false; // Track if X was previously pressed
    boolean iselbowOpen = false;
    boolean wasBPressed = false; //toggle elbow when B pressed

    public static double kp = 0.0007, ki = 0, kd = 0.0001;
    public static double kf = 0.001;
    public static double target = -4000;
    private final double encoderTicksInDegrees = 537.7;
    private boolean isPidActive = false;





    @Override
    public void init() {
        FrontLeft = hardwareMap.dcMotor.get("leftFront");
        FrontRight = hardwareMap.dcMotor.get("rightFront");
        BackLeft = hardwareMap.dcMotor.get("leftBack");
        BackRight = hardwareMap.dcMotor.get("rightBack");
        Intake = hardwareMap.get(DcMotorEx.class,"Slides");
        Intake2 = hardwareMap.get(DcMotorEx.class, "Slides2");
        claw = hardwareMap.servo.get("claw");
        elbow = hardwareMap.servo.get("elbow");
        Act1 = hardwareMap.dcMotor.get("Act1");
        Act2 = hardwareMap.dcMotor.get("Act2");
        elbow_2 = hardwareMap.servo.get("elbow_2");
        Intake.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        Intake2.setMode(DcMotor.RunMode.RUN_USING_ENCODER);



        telemetry = new MultipleTelemetry(telemetry, FtcDashboard.getInstance().getTelemetry());
    }




    @Override
    public void loop() {
        Intake.setPower(0);
        FrontRight.setPower(0);
        BackLeft.setPower(0);
        FrontLeft.setPower(0);
        BackRight.setPower(0);
        Intake2.setPower(0);


        if (gamepad1.a) {
            FrontLeft.setPower(-0.3);
            BackLeft.setPower(-0.3);
            FrontRight.setPower(-0.3);
            BackRight.setPower(-0.3);
        }
        if (gamepad1.y) {
            FrontLeft.setPower(0.3);
            BackLeft.setPower(0.3);
            FrontRight.setPower(0.3);
            BackRight.setPower(0.3);
        }


        if (gamepad1.left_stick_y!=0.0){
            FrontLeft.setPower(-gamepad1.left_stick_y);
            BackLeft.setPower(-gamepad1.left_stick_y);
        }
        if (gamepad1.right_stick_y!=0.0){
            FrontRight.setPower(-gamepad1.right_stick_y);
            BackRight.setPower(-gamepad1.right_stick_y);
        }
        if (gamepad1.right_bumper) {
            FrontLeft.setPower(0.75);
            BackLeft.setPower(-0.75);
            FrontRight.setPower(-0.75);
            BackRight.setPower(0.75);
        } else if (gamepad1.left_bumper) {
            FrontLeft.setPower(-0.75);
            BackLeft.setPower(0.75);
            FrontRight.setPower(0.75);
            BackRight.setPower(-0.75);

        }
        if (gamepad2.right_stick_y == 1.0) {
            Intake.setVelocity(-2300);
            Intake2.setVelocity(-2300);
        } else if (gamepad2.right_stick_y == -1.0) {
            Intake.setVelocity(2300);
            Intake2.setVelocity(2300);
        } else if(gamepad2.right_stick_y == 0.0) {
            Intake.setVelocity(0);
            Intake2.setVelocity(0);
        }





        // Toggle claw state when x is pressed
        if (gamepad2.x && !wasXPressed) {
            isClawOpen = !isClawOpen; // Toggle state
            if (isClawOpen) {
                claw.setPosition(1.0); // Close Position
            } else {
                claw.setPosition(0.0); // Open position
            }
        }

        if (gamepad2.b && !wasBPressed){
            iselbowOpen = !iselbowOpen;
            if (iselbowOpen){
                elbow.setPosition(0.45);
                elbow_2.setPosition(0.645);
            } else {
                elbow.setPosition(0.188);
                elbow_2.setPosition(0.878);
            }
        }
        // 0.45, 0.645
        if (gamepad2.y) {
            elbow.setPosition(0.5);
            elbow_2.setPosition(0.45);
        }


            // Add telemetry for debugging




        // Update wasXPressed state
        wasXPressed = gamepad2.x;
        wasBPressed = gamepad2.b;

        if(gamepad1.right_trigger == 1.0){
            Act1.setPower(1);
        } else {
            Act1.setPower(0.0);
        }
        if(gamepad1.right_trigger == 1.0){
            Act2.setPower(1);
        } else {
            Act2.setPower(0.0);
        }
        if(gamepad1.left_trigger == 1.0){
            Act1.setPower(-1);
        } else {
            Act1.setPower(0.0);
        }
        if(gamepad1.left_trigger == 1.0){
            Act2.setPower(-1);
        } else {
            Act2.setPower(0.0);
        }
    }
}