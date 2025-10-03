// Copyright 2021-2025 FRC 6328
// http://github.com/Mechanical-Advantage
//
// This program is free software; you can redistribute it and/or
// modify it under the terms of the GNU General Public License
// version 3 as published by the Free Software Foundation or
// available in the root directory of this project.
//
// This program is distributed in the hope that it will be useful,
// but WITHOUT ANY WARRANTY; without even the implied warranty of
// MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
// GNU General Public License for more details.

package frc.robot;

import com.pathplanner.lib.auto.AutoBuilder;
import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.wpilibj.GenericHID;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.Commands;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.button.CommandGenericHID;
import edu.wpi.first.wpilibj2.command.button.CommandXboxController;
import edu.wpi.first.wpilibj2.command.sysid.SysIdRoutine;
import frc.robot.commands.DriveCommands;
import frc.robot.generated.TunerConstants;
import frc.robot.subsystems.climb.Climb;
import frc.robot.subsystems.climb.ClimbBase;
import frc.robot.subsystems.climb.ClimbConstants;
import frc.robot.subsystems.climb.SimClimb;
import frc.robot.subsystems.climb.SparkMaxClimb;
import frc.robot.subsystems.drive.Drive;
import frc.robot.subsystems.drive.GyroIO;
import frc.robot.subsystems.drive.GyroIOPigeon2;
import frc.robot.subsystems.drive.ModuleIO;
import frc.robot.subsystems.drive.ModuleIOSim;
import frc.robot.subsystems.drive.ModuleIOTalonFX;
import frc.robot.subsystems.elevator.Elevator;
import frc.robot.subsystems.elevator.ElevatorBase;
import frc.robot.subsystems.elevator.SimElevator;
import frc.robot.subsystems.elevator.TalonFXElevator;
import frc.robot.subsystems.endeffector.intake.Intake;
import frc.robot.subsystems.endeffector.intake.IntakeBase;
import frc.robot.subsystems.endeffector.intake.IntakeConstants;
import frc.robot.subsystems.endeffector.intake.SimIntake;
import frc.robot.subsystems.endeffector.intake.SparkFlexIntake;
import frc.robot.subsystems.endeffector.wrist.SimWrist;
import frc.robot.subsystems.endeffector.wrist.SparkMaxWrist;
import frc.robot.subsystems.endeffector.wrist.Wrist;
import frc.robot.subsystems.endeffector.wrist.WristBase;
import frc.robot.util.Setpoints;
import org.littletonrobotics.junction.networktables.LoggedDashboardChooser;

/**
 * This class is where the bulk of the robot should be declared. Since Command-based is a
 * "declarative" paradigm, very little robot logic should actually be handled in the {@link Robot}
 * periodic methods (other than the scheduler calls). Instead, the structure of the robot (including
 * subsystems, commands, and button mappings) should be declared here.
 */
public class RobotContainer {
  // Subsystems
  private final Drive drive;
  private final Elevator elevator;
  private final Intake intake;
  private final Wrist wrist;
  private final Climb climb;

  // Controller
  private final CommandXboxController controller = new CommandXboxController(0);
  // private final CommandPS4Controller controller = new CommandPS4Controller(0);
  private final CommandGenericHID buttonBox = new CommandGenericHID(1);
  // Dashboard inputs
  private final LoggedDashboardChooser<Command> autoChooser;

  /** The container for the robot. Contains subsystems, OI devices, and commands. */
  public RobotContainer() {
    switch (Constants.currentMode) {
      case REAL:
        // Real robot, instantiate hardware IO implementations
        drive =
            new Drive(
                new GyroIOPigeon2(),
                new ModuleIOTalonFX(TunerConstants.FrontLeft),
                new ModuleIOTalonFX(TunerConstants.FrontRight),
                new ModuleIOTalonFX(TunerConstants.BackLeft),
                new ModuleIOTalonFX(TunerConstants.BackRight));
        // Subsystem declarations
        elevator = new Elevator(new TalonFXElevator());
        climb = new Climb(new SparkMaxClimb());
        wrist = new Wrist(new SparkMaxWrist());
        intake = new Intake(new SparkFlexIntake());
        break;

      case SIM:
        // Sim robot, instantiate physics sim IO implementations
        drive =
            new Drive(
                new GyroIO() {},
                new ModuleIOSim(TunerConstants.FrontLeft),
                new ModuleIOSim(TunerConstants.FrontRight),
                new ModuleIOSim(TunerConstants.BackLeft),
                new ModuleIOSim(TunerConstants.BackRight));

        elevator = new Elevator(new SimElevator());
        climb = new Climb(new SimClimb());
        wrist = new Wrist(new SimWrist());
        intake = new Intake(new SimIntake());
        break;

      default:
        // Replayed robot, disable IO implementations
        drive =
            new Drive(
                new GyroIO() {},
                new ModuleIO() {},
                new ModuleIO() {},
                new ModuleIO() {},
                new ModuleIO() {});
        // Blah blah blah
        elevator = new Elevator(new ElevatorBase() {});
        climb = new Climb(new ClimbBase() {});
        wrist = new Wrist(new WristBase() {});
        intake = new Intake(new IntakeBase() {});
        break;
    }

    // Set up auto routines
    autoChooser = new LoggedDashboardChooser<>("Auto Choices", AutoBuilder.buildAutoChooser());

    // Set up SysId routines
    autoChooser.addOption(
        "Drive Wheel Radius Characterization", DriveCommands.wheelRadiusCharacterization(drive));
    autoChooser.addOption(
        "Drive Simple FF Characterization", DriveCommands.feedforwardCharacterization(drive));
    autoChooser.addOption(
        "Drive SysId (Quasistatic Forward)",
        drive.sysIdQuasistatic(SysIdRoutine.Direction.kForward));
    autoChooser.addOption(
        "Drive SysId (Quasistatic Reverse)",
        drive.sysIdQuasistatic(SysIdRoutine.Direction.kReverse));
    autoChooser.addOption(
        "Drive SysId (Dynamic Forward)", drive.sysIdDynamic(SysIdRoutine.Direction.kForward));
    autoChooser.addOption(
        "Drive SysId (Dynamic Reverse)", drive.sysIdDynamic(SysIdRoutine.Direction.kReverse));

    // Configure the button bindings
    configureButtonBindings();
  }

  /**
   * Use this method to define your button->command mappings. Buttons can be created by
   * instantiating a {@link GenericHID} or one of its subclasses ({@link
   * edu.wpi.first.wpilibj.Joystick} or {@link XboxController}), and then passing it to a {@link
   * edu.wpi.first.wpilibj2.command.button.JoystickButton}.
   */
  private void configureButtonBindings() {
    // Default command, normal field-relative drive
    drive.setDefaultCommand(
        DriveCommands.joystickDrive(
            drive,
            () -> -controller.getLeftY(),
            () -> -controller.getLeftX(),
            () -> -controller.getRightX()));

    // Lock to 0° when A button is held
    controller
        .a()
        .whileTrue(
            DriveCommands.joystickDriveAtAngle(
                drive,
                () -> -controller.getLeftY(),
                () -> -controller.getLeftX(),
                () -> new Rotation2d()));

    // Switch to X pattern when X button is pressed
    controller.x().onTrue(Commands.runOnce(drive::stopWithX, drive));

    // Reset gyro to 0° when B button is pressed
    controller
        .b()
        .onTrue(
            Commands.runOnce(
                    () ->
                        drive.setPose(
                            new Pose2d(drive.getPose().getTranslation(), new Rotation2d())),
                    drive)
                .ignoringDisable(true));

    // Reference the commented print statements found in each
    // command method

    climb.setDefaultCommand(
        climb.manualClimb(
            climb,
            () ->
                controller.y().getAsBoolean()
                    ? ClimbConstants.climbRunValue
                    : controller.b().getAsBoolean() ? -ClimbConstants.climbRunValue : 0));
    elevator.setDefaultCommand(elevator.posElevator(elevator, Setpoints.IDLE.getElevatorPos()));
    intake.setDefaultCommand(
        intake.manualIntake(
            intake,
            () ->
                controller.leftBumper().getAsBoolean()
                    ? IntakeConstants.IntakeRunValue
                    : controller.rightBumper().getAsBoolean()
                        ? -IntakeConstants.OutTakeRunValue
                        : 0));
    wrist.setDefaultCommand(wrist.posWrist(wrist, Setpoints.IDLE.getWristPos()));

    // Careful using these, the wristToPosition and elevatorToPosition may not work
    // controller.a().whileTrue(elevator.posElevator(elevator, -1000.0));

    // L3
    buttonBox
        .button(17)
        .whileTrue(
            wrist
                .posWrist(wrist, Setpoints.L3_REEF.getWristPos())
                .alongWith(elevator.posElevator(elevator, Setpoints.L3_REEF.getElevatorPos())));
    controller.pov(270).whileTrue(wrist.posWrist(wrist, 100.0));
    // L2
    buttonBox
        .button(25)
        .whileTrue(
            wrist
                .posWrist(wrist, Setpoints.L2_REEF.getWristPos())
                .alongWith(elevator.posElevator(elevator, Setpoints.L2_REEF.getElevatorPos())));

    controller.start().whileTrue(new InstantCommand(() -> elevator.resetElevator()));
    // L4
    buttonBox
        .button(9)
        .whileTrue(
            wrist
                .posWrist(wrist, Setpoints.L4_REEF.getWristPos())
                .alongWith(elevator.posElevator(elevator, Setpoints.L4_REEF.getElevatorPos())));
    // L1
    buttonBox
        .button(6)
        .whileTrue(
            wrist
                .posWrist(wrist, Setpoints.L1_REEF.getWristPos())
                .alongWith(elevator.posElevator(elevator, Setpoints.L1_REEF.getElevatorPos())));
    // SOURCE
    buttonBox
        .button(7)
        .whileTrue(
            wrist
                .posWrist(wrist, Setpoints.SOURCE.getWristPos())
                .alongWith(elevator.posElevator(elevator, Setpoints.SOURCE.getElevatorPos())));

    // L1
    buttonBox
        .button(32)
        .whileTrue(
            wrist
                .posWrist(wrist, Setpoints.L1_REEF.getWristPos())
                .alongWith(elevator.posElevator(elevator, Setpoints.L1_REEF.getElevatorPos())));
    controller.pov(270).whileTrue(wrist.posWrist(wrist, 100.0));

    // Manual Elevator control
    buttonBox.button(23).whileTrue(elevator.manualElevator(elevator, () -> 0.15));
    buttonBox.button(22).whileTrue(elevator.manualElevator(elevator, () -> -0.15));

    // Manual Wrist control
    buttonBox.button(15).whileTrue(wrist.manualWrist(wrist, () -> 0.15));
    buttonBox.button(14).whileTrue(wrist.manualWrist(wrist, () -> 0.15));

    // Manual Climb control
    buttonBox.button(29).whileTrue(climb.manualClimb(climb, () -> 1));
    buttonBox.button(30).whileTrue(climb.manualClimb(climb, () -> -1));

    // buttonBox.button(4).whileTrue(EndEffectorCommands.WristToPosition(wrist, Setpoints.L1_REEF));
  }

  /**
   * Use this to pass the autonomous command to the main {@link Robot} class.
   *
   * @return the command to run in autonomous
   */
  public Command getAutonomousCommand() {
    return autoChooser.get();
  }
}
