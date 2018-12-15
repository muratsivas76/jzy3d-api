package org.jzy3d.demos.incubation.volume;

import java.nio.ByteBuffer;

import org.jzy3d.analysis.AbstractAnalysis;
import org.jzy3d.analysis.AnalysisLauncher;
import org.jzy3d.chart.factories.AWTChartComponentFactory;
import org.jzy3d.colors.Color;
import org.jzy3d.colors.ColorMapper;
import org.jzy3d.colors.colormaps.ColorMapGrayscale;
import org.jzy3d.incubation.volume.Texture3D;
import org.jzy3d.maths.BoundingBox3d;
import org.jzy3d.plot3d.rendering.canvas.Quality;

import com.jmatio.io.MatFileReader;
import com.jmatio.types.MLNumericArray;
import com.jogamp.opengl.util.GLBuffers;

/**
 * 
 * @author Jacok Filik
 *
 */
public class LizardVolumeRenderDemo extends AbstractAnalysis {
    public static void main(String[] args) throws Exception {
        AnalysisLauncher.open(new LizardVolumeRenderDemo());
    }

    @Override
    public void init() {
        
        ByteBuffer buffer = null;
        int[] shape = null;
        float max = Float.NEGATIVE_INFINITY;
        float min = Float.POSITIVE_INFINITY;
        
         try {
            MatFileReader mfr = new MatFileReader("data/lizard.mat");
            MLNumericArray<Integer> data = (MLNumericArray<Integer>)mfr.getMLArray("data");
            shape = data.getDimensions();
            int size = data.getSize();
            data.toString();
            
            buffer = GLBuffers.newDirectByteBuffer(size*4);
            
            for (int i = 0; i < size; i++) {
                float f = data.get(i).floatValue();
                buffer.putFloat(f);
                
                if (f < min) {
                    min = f;
                }
                
                if (f > max) {
                    max = f;
                }
            }
            
            
        } catch (Exception e) {
            return;
        }
         
        int temp = shape[0];
        shape[0] = shape[2];
        shape[2] = temp;
        
        ColorMapper colorMapper = new ColorMapper(new ColorMapGrayscale(), min, max, new Color(1, 1, 1, .5f));
        
        Texture3D volume = new Texture3D(buffer, shape,(float)min+ ((max-min)/10),(float)max-((max-min)/10),colorMapper,new BoundingBox3d(0,shape[2],0,shape[1],0,shape[0]));   
        
        // Create a chart
        chart = AWTChartComponentFactory.chart(Quality.Intermediate, getCanvasType());
        chart.getScene().getGraph().add(volume);
//        chart.getView().setBackgroundColor(new Color(0, 0, 0));
//        IAxeLayout axeLayout = chart.getAxeLayout();
//        axeLayout.setMainColor(new Color(0.7f, 0.7f, 0.7f));
        chart.getView().setSquared(false);
        chart.getView();
    }
}