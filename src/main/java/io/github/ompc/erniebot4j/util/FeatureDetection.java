package io.github.ompc.erniebot4j.util;

public class FeatureDetection {

    private final byte[] feature;
    private boolean isDetected = false;
    private int index = 0;

    public FeatureDetection(byte[] feature) {
        this.feature = feature;
    }

    private void screeningByte(final byte b) {
        if (b != feature[index]) {
            reset();
            isDetected = false;
            return;
        }
        if (++index == feature.length) {
            reset();
            isDetected = true;
        }
    }

    public int screening(final byte[] bytes, int offset, int length) {
        int position = -1;
        for (int index = offset; index < offset + length; index++) {
            screeningByte(bytes[index]);
            if (isDetected) {
                position = index;
                break;
            }
        }
        return position;
    }

    private void reset() {
        index = 0;
    }

}
