// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems.endeffector.intake;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.Commands;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import java.util.function.DoubleSupplier;

public class Intake extends SubsystemBase {

  public IntakeBase intake;

  /** Creates a new Intake. */
  public Intake(IntakeBase intake) {
    this.intake = intake;
  }

  public void RunIntake(double value) {
    intake.runIntake(value);
  }

  public void StopIntake() {
    intake.stopIntake();
  }

  public Command manualIntake(Intake intake, DoubleSupplier value) {
    return Commands.run(
        () -> {
          // System.out.println("Intake run value: " + value.getAsDouble());
          intake.RunIntake(value.getAsDouble());
        },
        intake);
  }

  public Command RunIntakeCommand(Intake intake, DoubleSupplier value) {
    return Commands.run(
        () -> {
          // System.out.println("running intake");
          intake.RunIntake(value.getAsDouble());
        },
        intake);
  }

  @Override
  public void periodic() {
    // This method will be called once per scheduler run
  }
}
