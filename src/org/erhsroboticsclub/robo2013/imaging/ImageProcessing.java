package org.erhsroboticsclub.robo2013.imaging;

import edu.wpi.first.wpilibj.camera.AxisCamera;
import edu.wpi.first.wpilibj.image.BinaryImage;
import edu.wpi.first.wpilibj.image.CriteriaCollection;
import edu.wpi.first.wpilibj.image.NIVision.MeasurementType;
import edu.wpi.first.wpilibj.image.ParticleAnalysisReport;
import org.erhsroboticsclub.robo2013.utilities.MathX;
import org.erhsroboticsclub.robo2013.utilities.Messenger;

public class ImageProcessing {

    Messenger msg = new Messenger();
    
    static final double CAMERA_PIXEL_WIDTH = 640;
    static final double CAMERA_PIXEL_HEIGHT = 480;
    static final double CAMERA_DISPLACEMENT = 10;
    
    
    

    public ImageProcessing() {
        
    }

    /**
     * fills the targetParticles[] array with all found particle analysis reports
     *
     * @param camera the camera to get the particle analysis report from
     * @throws Exception
     */
    public ParticleAnalysisReport[] getParticleAnalysisReports(AxisCamera camera) throws Exception {
        CriteriaCollection criteriaCollection = new CriteriaCollection();
        
        criteriaCollection.addCriteria(
                MeasurementType.IMAQ_MT_BOUNDING_RECT_WIDTH, 30, 400, false);
        criteriaCollection.addCriteria(
                MeasurementType.IMAQ_MT_BOUNDING_RECT_HEIGHT, 40, 400, false);
        
        ParticleAnalysisReport targetParticles[];
        int erosionCount = 2;
        boolean useConnectivity8 = false;  // use connectivity4 instead
        // get color image
        // seperate the light and dark image creating a binary image,
        // fill rectanges created from colorImage rgb threshold, 
        // clean out residual small objects,
        // convexHull, 
        // apply a particle filter based on Criteria Collection constraints
        BinaryImage filteredImage = camera.getImage()
                .thresholdRGB(0, 42, 71, 255, 0, 255)
                .removeSmallObjects(useConnectivity8, erosionCount)
                .convexHull(useConnectivity8)
                .particleFilter(criteriaCollection);
        targetParticles = filteredImage.getOrderedParticleAnalysisReports();
        filteredImage.free();
        return targetParticles;
    }
    

        public double getDistance(ParticleAnalysisReport targetReport, double targetHeight) {
       return 0;
    }   
    
}
