// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems.climb;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.Commands;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import java.util.function.DoubleSupplier;

public class Climb extends SubsystemBase {

  private ClimbBase climb;

  /** Creates a new Climb. */
  public Climb(ClimbBase climb) {
    this.climb = climb;
  }

  public void RunClimb(double value) {
    climb.runClimb(value);
  }

  public void stopClimb() {
    climb.stopClimb();
  }

  public double getAngle() {
    return climb.getClimbAngle();
  }

  public Command manualClimb(Climb climb, DoubleSupplier value) {
    return Commands.run(
        () -> {
          // System.out.println("Climb run value: " + value.getAsDouble());
          climb.RunClimb(value.getAsDouble());
        },
        climb);
  }

  @Override
  public void periodic() {
    // This method will be called once per scheduler run
  }
}
