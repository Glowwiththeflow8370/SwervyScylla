// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems.endeffector.intake;

import com.revrobotics.spark.SparkBase.PersistMode;
import com.revrobotics.spark.SparkBase.ResetMode;
import com.revrobotics.spark.SparkFlex;
import com.revrobotics.spark.SparkLowLevel.MotorType;
import com.revrobotics.spark.config.SparkBaseConfig.IdleMode;
import com.revrobotics.spark.config.SparkFlexConfig;

public class SparkFlexIntake implements IntakeBase {

  // Initialize motors and configurations
  private SparkFlex IntakeMotor, IntakeMotorFollower;
  private SparkFlexConfig IntakeMotorConfig, IntakeMotorFollowerConfig;

  public SparkFlexIntake() {
    // Create new motor objects
    IntakeMotor = new SparkFlex(IntakeConstants.IntakeMotorFollowerID, MotorType.kBrushless);
    IntakeMotorFollower = new SparkFlex(IntakeConstants.IntakeMotorID, MotorType.kBrushless);

    // Configs for main motor
    IntakeMotorConfig = new SparkFlexConfig();
    IntakeMotorConfig.idleMode(IdleMode.kBrake);

    // Configs for follower
    IntakeMotorFollowerConfig = new SparkFlexConfig();
    IntakeMotorFollowerConfig.idleMode(IdleMode.kBrake);
    IntakeMotorFollowerConfig.follow(IntakeMotor, true);

    // Apply configuration
    IntakeMotor.configure(
        IntakeMotorConfig, ResetMode.kResetSafeParameters, PersistMode.kPersistParameters);

    IntakeMotorFollower.configure(
        IntakeMotorFollowerConfig, ResetMode.kResetSafeParameters, PersistMode.kPersistParameters);
  }

  // Functionality of each method is described in
  // IntakeBase class
  @Override
  public void runIntake(double value) {
    IntakeMotor.set(value);
  }

  @Override
  public void stopIntake() {
    IntakeMotor.set(0);
  }
}
