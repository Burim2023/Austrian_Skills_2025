package frc.robot.subsystems;
import frc.robot.utilities.LoggingSystem;
// For camera handling
import edu.wpi.first.cameraserver.CameraServer;
import edu.wpi.first.cscore.CvSink;
import edu.wpi.first.cscore.UsbCamera;
import org.opencv.core.Mat;
import org.opencv.core.MatOfByte;
import org.opencv.imgcodecs.Imgcodecs;
/**
 * Camera subsystem for barcode/QR scanning and color detection
 */
public class CameraSubsystem {
    
    // Last detected barcode data
    private String lastBarcodeData = "No barcode detected";
    private String lastBarcodeType = "";
    
    // Detected colors status
    private boolean redDetected = false;
    private boolean yellowDetected = false;
    private boolean greenDetected = false;
    
    // Camera objects
    private UsbCamera camera;
    private CvSink cvSink;
    private Mat mat;
    private boolean cameraInitialized = false;
    
    /**
     * Constructor for CameraSubsystem
     */
    public CameraSubsystem() {
        try {
            // Initialize the camera
            initializeCamera();
            
            // Log initialization
            LoggingSystem.logInfo("CameraSubsystem initialized");
        } catch (Exception e) {
            LoggingSystem.logError("Error initializing CameraSubsystem: " + e.getMessage());
        }
    }
    
    /**
     * Initialize the camera for capturing images
     */
    private void initializeCamera() {
        try {
            // Start camera server and get camera
            camera = CameraServer.startAutomaticCapture();
            
            // Configure camera settings for optimal barcode detection
            camera.setResolution(640, 480);
            camera.setFPS(15);
            camera.setBrightness(50);
            camera.setExposureAuto();
            
            // Get a CvSink to capture frames from the camera
            cvSink = CameraServer.getVideo();
            
            // Create a Mat to store the captured frame
            mat = new Mat();
            
            cameraInitialized = true;
            LoggingSystem.logInfo("Camera initialized successfully");
        } catch (Exception e) {
            LoggingSystem.logError("Error initializing camera: " + e.getMessage());
            cameraInitialized = false;
        }
    }
    
    /**
     * Trigger barcode/QR code scanning
     */
    public void scanBarcode() {
        try {
            LoggingSystem.logInfo("Barcode scan requested");
            
            // Simulate detecting a barcode - this would normally come from vision processing
            // In a real implementation, this would communicate with a vision coprocessor
            simulateBarcodeDetection();
        } catch (Exception e) {
            LoggingSystem.logError("Error during barcode scan: " + e.getMessage());
        }
    }
    
    /**
     * Trigger color detection
     */
    public void detectColors() {
        try {
            LoggingSystem.logInfo("Color detection requested");
            
            // Simulate detecting colors - this would normally come from vision processing
            // In a real implementation, this would communicate with a vision coprocessor
            simulateColorDetection();
        } catch (Exception e) {
            LoggingSystem.logError("Error during color detection: " + e.getMessage());
        }
    }
    
    /**
     * Get the data from the last detected barcode/QR code
     * 
     * @return The data contained in the barcode/QR code
     */
    public String getBarcodeData() {
        return lastBarcodeData;
    }
    
    /**
     * Get the type of the last detected barcode/QR code (e.g., QR_CODE, CODE_128)
     * 
     * @return The type of barcode/QR code
     */
    public String getBarcodeType() {
        return lastBarcodeType;
    }
    
    /**
     * Check if the color red is detected
     * 
     * @return True if red is detected, false otherwise
     */
    public boolean isRedDetected() {
        return redDetected;
    }
    
    /**
     * Check if the color yellow is detected
     * 
     * @return True if yellow is detected, false otherwise
     */
    public boolean isYellowDetected() {
        return yellowDetected;
    }
    
    /**
     * Check if the color green is detected
     * 
     * @return True if green is detected, false otherwise
     */
    public boolean isGreenDetected() {
        return greenDetected;
    }
    
    /**
     * Simulate barcode detection - would be replaced with actual vision processing
     */
    private void simulateBarcodeDetection() {
        try {
            // This is a placeholder that would normally communicate with vision processing
            lastBarcodeData = "EXAMPLE-QR-CODE-123";
            lastBarcodeType = "QR_CODE";
            
            // Log detection
            LoggingSystem.logInfo("Barcode detected: " + lastBarcodeData + " (Type: " + lastBarcodeType + ")");
        } catch (Exception e) {
            LoggingSystem.logError("Error simulating barcode detection: " + e.getMessage());
        }
    }
    
    /**
     * Simulate color detection - would be replaced with actual vision processing
     */
    private void simulateColorDetection() {
        try {
            // This is a placeholder that would normally communicate with vision processing
            // Simulate randomly detecting colors
            redDetected = Math.random() > 0.5;
            yellowDetected = Math.random() > 0.5;
            greenDetected = Math.random() > 0.5;
            
            // Log detection
            LoggingSystem.logInfo("Color detection results - Red: " + redDetected + 
                              ", Yellow: " + yellowDetected + 
                              ", Green: " + greenDetected);
        } catch (Exception e) {
            LoggingSystem.logError("Error simulating color detection: " + e.getMessage());
        }
    }
    
    /**
     * Called periodically to update the camera subsystem
     */
    public void periodic() {
        // In a complete implementation, we would check for user requests and update
        // the camera data here
    }
    
    /**
     * Capture an image from the camera and return it as a byte array
     * 
     * @return The captured image as a JPEG-encoded byte array, or null if capturing failed
     */
    public byte[] captureImage() {
        if (!cameraInitialized) {
            LoggingSystem.logError("Cannot capture image, camera not initialized");
            return null;
        }
        
        try {
            // Grab a frame from the camera
            long frameTime = cvSink.grabFrame(mat);
            
            // Check if the frame was successfully captured
            if (frameTime == 0) {
                LoggingSystem.logError("Error grabbing frame: " + cvSink.getError());
                return null;
            }
            
            // Convert the OpenCV Mat to a JPEG byte array
            MatOfByte matOfByte = new MatOfByte();
            Imgcodecs.imencode(".jpg", mat, matOfByte);
            byte[] imageBytes = matOfByte.toArray();
            
            LoggingSystem.logInfo("Image captured successfully, size: " + imageBytes.length + " bytes");
            return imageBytes;
        } catch (Exception e) {
            LoggingSystem.logError("Error capturing image: " + e.getMessage());
            return null;
        }
    }
}