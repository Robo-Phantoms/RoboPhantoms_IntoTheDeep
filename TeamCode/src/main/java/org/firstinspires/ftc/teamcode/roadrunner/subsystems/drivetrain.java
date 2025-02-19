package org.firstinspires.ftc.teamcode.roadrunner.subsystems;

import androidx.annotation.NonNull;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;
import org.firstinspires.ftc.robotcore.external.Telemetry;

import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import dev.frozenmilk.dairy.core.dependency.Dependency;
import dev.frozenmilk.dairy.core.dependency.annotation.SingleAnnotation;
import dev.frozenmilk.dairy.core.wrapper.Wrapper;
import dev.frozenmilk.mercurial.Mercurial;
import dev.frozenmilk.mercurial.bindings.BoundGamepad;
import dev.frozenmilk.mercurial.commands.Lambda;
import dev.frozenmilk.mercurial.subsystems.SDKSubsystem;
import dev.frozenmilk.mercurial.subsystems.Subsystem;

public class drivetrain extends SDKSubsystem {

    public static final drivetrain INSTANCE = new drivetrain();
    private DcMotor leftFront, rightFront, leftBack, rightBack;
    private Telemetry telemetry;

    private drivetrain() {}
    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.TYPE)
    @Inherited
    public @interface Attach {
    }

    public void preInitUserHook(@NonNull Wrapper opMode) {
        HardwareMap hwMap = opMode.getOpMode().hardwareMap;
        telemetry = opMode.getOpMode().telemetry;

        leftFront = hwMap.get(DcMotor.class, "leftFront");
        rightFront = hwMap.get(DcMotor.class, "rightFront");
        leftBack = hwMap.get(DcMotor.class, "leftBack");
        rightBack = hwMap.get(DcMotor.class, "rightBack");

        // Set motor directions (adjust if needed)
        leftFront.setDirection(DcMotorSimple.Direction.REVERSE);
        leftBack.setDirection(DcMotorSimple.Direction.REVERSE);
        rightFront.setDirection(DcMotorSimple.Direction.FORWARD);
        rightBack.setDirection(DcMotorSimple.Direction.FORWARD);

        // Ensure motors stop when no power is applied
        leftFront.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        rightFront.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        leftBack.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        rightBack.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
    }

    @Override
    public void postUserLoopHook(@NonNull Wrapper opMode) {
    }
    private Dependency<?> dependency = Subsystem.DEFAULT_DEPENDENCY.and(new SingleAnnotation<>(Attach.class));
    @NonNull
    @Override
    public Dependency<?> getDependency() {
        return dependency;
    }

    @Override
    public void setDependency(@NonNull Dependency<?> dependency) {
        this.dependency = dependency;
    }
    public Lambda robotCentricDriveCommand() {
        BoundGamepad gamepad1 = Mercurial.gamepad1();
        return new Lambda("control-drive")
                .addRequirements(this)
                .setInterruptible(true)
                .setInit(() -> {})
                .setExecute(() -> {
                    double y = -gamepad1.leftStickY().state();  // Left joystick Y-axis (forward/backward)
                    double x = gamepad1.leftStickX().state();  // Left joystick X-axis (left/right)
                    double rx = gamepad1.rightStickX().state();  // Right joystick X-axis (rotation)
                    double denominator = Math.max(Math.abs(y) + Math.abs(x) + Math.abs(rx), 1);
                    double frontLeftPower = (y + x + rx) / denominator;
                    double backLeftPower = (y - x + rx) / denominator;
                    double frontRightPower = (y - x - rx) / denominator;
                    double backRightPower = (y + x - rx) / denominator;

                    leftFront.setPower(frontLeftPower);
                    rightFront.setPower(frontRightPower);
                    leftBack.setPower(backLeftPower);
                    rightBack.setPower(backRightPower);
                })
                .setFinish(()-> false);
    }
    public Lambda strafeRight(){
        return new Lambda("strafe-right")
                .addRequirements(this)
                .setInterruptible(true)
                .setExecute(() -> {
                    leftFront.setPower(0.3);
                    rightFront.setPower(-0.3);
                    leftBack.setPower(0.3);
                    rightBack.setPower(-0.3);
                });

    }
    public Lambda strafeLeft(){
        return new Lambda("strafe-left")
                .addRequirements(this)
                .setInterruptible(true)
                .setExecute(() -> {
                    leftFront.setPower(-0.3);
                    rightFront.setPower(0.3);
                    leftBack.setPower(-0.3);
                    rightBack.setPower(0.3);
                });

    }
}
