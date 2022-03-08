package com.edencoding;

import java.awt.image.BufferedImage;

/**
 * @author Launois Remy
 * Project: drag-and-drop
 * Package: com.edencoding
 */
public interface ExecutableAlgorithm {
    public BufferedImage executeAlgorithm(BufferedImage image, DistanceMethod method);
}
