/*----------------------------------------------------------------------------*/
/* Copyright (c) 2017-2018 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot;

import edu.wpi.first.cscore.UsbCamera;
import edu.wpi.first.wpilibj.CameraServer;
import edu.wpi.first.wpilibj.GenericHID;// <-- Needed for xbox style controllers
import edu.wpi.first.wpilibj.Spark;
import edu.wpi.first.wpilibj.SpeedController;
import edu.wpi.first.wpilibj.TimedRobot;// <-- New for 2019, takes over for the depricated Iteritive robot
import edu.wpi.first.wpilibj.XboxController;// <-- For using a gamepad controller
import edu.wpi.first.wpilibj.drive.MecanumDrive;// <-- Needed for the drive base.
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;// <-- For writing data back to the drivers station.


/**
 * This is a demo program showing how to use Mecanum control with the RobotDrive
 * class.  It's been modifed to call the Spark controllers, which use PWM. 
 */
public class Robot extends TimedRobot {
  // These will need to be updated to the CAN Ids of the Spark's
  private static int kFrontLeftChannel = 3;
  private static int kRearLeftChannel = 2;
  private static int kFrontRightChannel = 1;
  private static int kRearRightChannel = 0;
  private static int kLiftChannel = 4;

  // What ever USB port we have the controller plugged into.
  private static int kGamePadChannel = 0;

  //Lets map out the buttons
  //private static final int kXboxButtonA = 1;
  //private static final int kXboxButtonB = 2;
  //private static final int kXboxButtonX = 3;
  //private static final int kXboxButtonY = 4;

  //private static final int kXboxButtonLB = 5; // <-- Left Button
  //private static final int kXboxButtonRB = 6; // <-- Right Button
  private static final int kXboxButtonLT = 2; // <-- Left Trigger
  private static final int kXboxButtonRT = 3; // <-- Right Trigger

  //private static final double kRampUpRate = 0.0; // The rate that the motor controller will speed up to full;
  

  private MecanumDrive m_robotDrive;

  private XboxController m_controllerDriver;

  private DeadBand m_stick;

  private SpeedController m_liftMotor;


  
  /**
   * This function if called when the robot boots up.
   * It creates the objects that are called by the other robot functions.
   */
  @Override
  public void robotInit() {
    CameraServer.getInstance().startAutomaticCapture();
    
    Spark frontLeftSpark = new Spark(kFrontLeftChannel);
    Spark frontRightSpark = new Spark(kFrontRightChannel);
    Spark rearLeftSpark = new Spark(kRearLeftChannel);
    Spark rearRightSpark = new Spark(kRearRightChannel);
    Spark liftSpark = new Spark(kLiftChannel);

    // Invert the motors.
    // You may need to change or remove this to match your robot.
    frontLeftSpark.setInverted(true);
    rearRightSpark.setInverted(true);

    m_robotDrive = new MecanumDrive(frontLeftSpark, rearLeftSpark, frontRightSpark, rearRightSpark);

    //Construct the lift motor

    m_liftMotor = liftSpark;

    // m_controllerDriver = new Joystick(kJoystickChannel);
    
    m_controllerDriver = new GenericHID(kGamePadChannel);


    m_stick = new DeadBand();
  
  } // *********************** End of roboInit **********************************
  
  /**
   * When in teleop this function is called periodicly
   */
  @Override
  public void teleopPeriodic() {
    // Need to come up with a way to tone down the joysticks

    // If I did this right, this should allow for direction of travel to be set by using the left joystick
    // while the rotation of the robot is set by the right stick on the controller.
    m_robotDrive.driveCartesian(m_stick.SmoothAxis(m_controllerDriver.getRawAxis(1)), 
                                m_stick.SmoothAxis(m_controllerDriver.getRawAxis(0)), 
                                m_stick.SmoothAxis(m_controllerDriver.getRawAxis(4)));
    m_liftMotor.set(m_stick.SmoothAxis(m_controllerDriver.getRawAxis(kXboxButtonLT)) - m_stick.SmoothAxis(m_controllerDriver.getRawAxis(kXboxButtonRT)));



    
  } // ************************** End of teleopPeriodic *************************
    
  /**
   * testPeriodic function is called periodicly when the DS is 
   * in test mode.
   */
  @Override
  public void testPeriodic(){
  // Ok, so how do we read that a button has been pressed?  Also can we output it to a dashboard?
  //  public static final String ButtonStatus = "Button Pressed:";

    //This block of code should be moved down to
    if (m_controllerDriver.getRawButtonPressed(1)){
      System.out.println("Button A Pressed");
      SmartDashboard.putString("Button A = ", "I was pushed");
    }
    else if (m_controllerDriver.getRawButtonReleased(1)){
      System.out.println("Button A Released");
      SmartDashboard.putString("Button A = " , "I was released");
    }

  } // ************************ End of testPeriodic **************************

}
