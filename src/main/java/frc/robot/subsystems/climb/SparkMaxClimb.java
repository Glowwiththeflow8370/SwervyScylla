package frc.robot.subsystems.climb;

import com.revrobotics.spark.SparkBase.PersistMode;
import com.revrobotics.spark.SparkBase.ResetMode;
import com.revrobotics.spark.SparkLowLevel.MotorType;
import com.revrobotics.spark.SparkMax;
import com.revrobotics.spark.config.SparkBaseConfig.IdleMode;
import com.revrobotics.spark.config.SparkMaxConfig;
import edu.wpi.first.math.controller.ProfiledPIDController;

public class SparkMaxClimb implements ClimbBase {

  private SparkMax climbMotor, climbMotorFollower;
  private SparkMaxConfig climbMotorConfig, climbMotorFollowerConfig;

  // Considering the angle of motion is similar
  // to the wrist, imma try to use code
  // similar to it

  // PID controller
  ProfiledPIDController profiledPIDController;

  public SparkMaxClimb() {
    // Create and configure the climb motors
    climbMotor = new SparkMax(ClimbConstants.climbMotorID, MotorType.kBrushless);
    climbMotorFollower = new SparkMax(ClimbConstants.climbMotorFollower, MotorType.kBrushless);

    climbMotorConfig = new SparkMaxConfig();
    // encoder setup?

    // Idle mode configuration
    climbMotorConfig.idleMode(IdleMode.kBrake);

    climbMotorFollowerConfig = new SparkMaxConfig();
    climbMotorFollowerConfig.idleMode(IdleMode.kBrake);
    climbMotorFollowerConfig.follow(climbMotor, true);

    // Apply configurations
    climbMotor.configure(
        climbMotorConfig, ResetMode.kResetSafeParameters, PersistMode.kPersistParameters);

    climbMotorFollower.configure(
        climbMotorFollowerConfig, ResetMode.kResetSafeParameters, PersistMode.kPersistParameters);
  }

  // refer to the class ClimbBase for more information on the
  // function of each method
  @Override
  public void runClimb(double value) {
    climbMotor.set(value);
  }

  @Override
  public void stopClimb() {
    climbMotor.set(0);
  }

  @Override
  public double getClimbAngle() {
    return 0.0;
  }
}
