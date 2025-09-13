package frc.robot.subsystems.endeffector.wrist;

import com.revrobotics.spark.SparkBase.PersistMode;
import com.revrobotics.spark.SparkBase.ResetMode;
import com.revrobotics.spark.SparkClosedLoopController;
import com.revrobotics.spark.SparkLowLevel.MotorType;
import com.revrobotics.spark.SparkMax;
import com.revrobotics.spark.config.ClosedLoopConfig;
import com.revrobotics.spark.config.SparkBaseConfig.IdleMode;
import com.revrobotics.spark.config.SparkMaxConfig;
import edu.wpi.first.math.controller.ProfiledPIDController;
import edu.wpi.first.math.trajectory.TrapezoidProfile;
import edu.wpi.first.wpilibj.DutyCycleEncoder;

public class SparkMaxWrist implements WristBase {
  private final SparkMax wristMotor;
  private final SparkClosedLoopController pidController;
  private final DutyCycleEncoder wristEncoder;
  ProfiledPIDController profiledPIDController;

  // PID Constants (Tune these)
  private static final double kP = 0.1;
  private static final double kI = 0.0;
  private static final double kD = 0.0;
  private static final double kF = 0.0;
  private static final double kMaxVelocity = 10000;
  private static final double kMaxAcceleration = 15000;

  public SparkMaxWrist() {
    // Initialize Wrist Motor
    wristMotor = new SparkMax(WristConstants.WristMotorID, MotorType.kBrushless);

    // Initialize Absolute Encoder
    wristEncoder = new DutyCycleEncoder(0);
    profiledPIDController =
        new ProfiledPIDController(
            kP, kI, kD, new TrapezoidProfile.Constraints(kMaxVelocity, kMaxAcceleration));

    // Create Configuration
    SparkMaxConfig wristConfig = new SparkMaxConfig();
    wristConfig.idleMode(IdleMode.kBrake); // Set brake mode to hold position

    // Apply Configuration
    wristMotor.configure(
        wristConfig, ResetMode.kResetSafeParameters, PersistMode.kPersistParameters);

    // Setup Closed Loop Controller
    pidController = wristMotor.getClosedLoopController();

    // Configure PID
    ClosedLoopConfig pidConfig = new ClosedLoopConfig();

    pidConfig.p(kP).i(kI).d(kD).outputRange(-1, 1);
    // Disable soft limits unless needed

    // Apply PID Configuration

  }

  public void rotateWrist(double speed) {
    wristMotor.set(speed);
  }

  public void setWristPosition(double targetAngle) {
    double currentAngle = getWristAngle();
    double setpoint = profiledPIDController.calculate(currentAngle, targetAngle);
    wristMotor.setVoltage(setpoint);
  }

  public void stopWrist() {
    wristMotor.set(0);
  }

  public double getWristAngle() {
    return wristEncoder.get() * 360;
  }
}
