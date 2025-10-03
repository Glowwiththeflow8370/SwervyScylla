package frc.robot.subsystems.elevator;

import com.ctre.phoenix6.BaseStatusSignal;
import com.ctre.phoenix6.StatusSignal;
import com.ctre.phoenix6.configs.TalonFXConfiguration;
import com.ctre.phoenix6.hardware.TalonFX;
import com.ctre.phoenix6.signals.NeutralModeValue;
import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.units.measure.Angle;
import edu.wpi.first.units.measure.AngularVelocity;

public class TalonFXElevator implements ElevatorBase {

  private TalonFX leftMotor, rightMotor;
  private TalonFXConfiguration elevatorMotorConfigs, elevatorFollowerConfigs;

  private StatusSignal<Angle> positionSignal;
  private StatusSignal<AngularVelocity> velocitySignal;

  private PIDController elevatorPID;
  private double targetPosition;
  private double speedMultiplier; // Adjusts speed when moving up/down

  // PID Constants (Tune these based on your system)
  private static final double kP = .06;
  private static final double kI = 0;
  private static final double kD = 0;

  // Default speed multipliers (Can be tuned)
  private static final double UP_SPEED = 0.05; // Full speed up
  private static final double DOWN_SPEED = 0.015; // Slower speed down (gravity helps)

  private BaseStatusSignal[] signals;

  public TalonFXElevator() {
    leftMotor = new TalonFX(ElevatorConstants.elevatorMotorID);
    rightMotor = new TalonFX(ElevatorConstants.elevatorMotorFolowerID);

    elevatorMotorConfigs = new TalonFXConfiguration();
    elevatorMotorConfigs.CurrentLimits.StatorCurrentLimit = 80;
    elevatorMotorConfigs.CurrentLimits.StatorCurrentLimitEnable = true;

    elevatorFollowerConfigs = new TalonFXConfiguration();
    elevatorFollowerConfigs.CurrentLimits.StatorCurrentLimit = 80;
    elevatorFollowerConfigs.CurrentLimits.StatorCurrentLimitEnable = true;

    leftMotor.getConfigurator().apply(elevatorMotorConfigs);
    rightMotor.getConfigurator().apply(elevatorFollowerConfigs);

    // rightMotor.setControl(new Follower(leftMotor.getDeviceID(), true));

    rightMotor.setNeutralMode(NeutralModeValue.Brake);
    leftMotor.setNeutralMode(NeutralModeValue.Brake);

    positionSignal = leftMotor.getPosition();
    velocitySignal = leftMotor.getVelocity();
    // New encoder value stuffs
    signals = new BaseStatusSignal[] {positionSignal, velocitySignal};
    // Bandwith/Update frequency setting 50hz for all status signals in
    // signals BaseStatusSignal thing
    BaseStatusSignal.setUpdateFrequencyForAll(50, signals);
    leftMotor.optimizeBusUtilization();
    rightMotor.optimizeBusUtilization();

    // Initialize PID Controller
    elevatorPID = new PIDController(kP, kI, kD);
    elevatorPID.setTolerance(100); // Allowable error in encoder ticks

    // Default target position is current position
    targetPosition = getElevatorEncoderValues();
  }

  @Override
  public void runElevator(double value) {
    leftMotor.set(value);
    // rightMotor.set(-value);
  }

  @Override
  public void stopElevator() {
    rightMotor.set(0);
  }

  @Override
  public void updateSignalValues() {
    // Refresh all status signals specified
    BaseStatusSignal.refreshAll(signals);
  }

  @Override
  public double getElevatorEncoderValues() {
    return positionSignal.getValueAsDouble() * 360;
  }

  public void setElevatorPosition(double newTargetPosition) {
    targetPosition = newTargetPosition;
    double currentPosition = getElevatorEncoderValues();

    // Adjust speed based on direction
    if (targetPosition > currentPosition) {
      speedMultiplier = UP_SPEED; // Going up
    } else {
      speedMultiplier = DOWN_SPEED; // Going down
    }

    double pidOutput = elevatorPID.calculate(currentPosition, targetPosition);

    // Apply speed control
    leftMotor.setVoltage(pidOutput * speedMultiplier);
    rightMotor.setVoltage(pidOutput * speedMultiplier);

    System.out.println(pidOutput * speedMultiplier);
  }
}
