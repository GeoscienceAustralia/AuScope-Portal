package au.gov.geoscience.portal.server.nvcldataservice;

import java.io.Serializable;

/**
 * Forms part of a NVCLDataServices getAlgorithms response
 * @author Josh Vote (CSIRO)
 *
 */
public class AlgorithmVersion implements Serializable {
    private int algorithmOutputId;
    private int version;

    public AlgorithmVersion() {
        super();
    }
    public AlgorithmVersion(int algorithmOutputId, int version) {
        super();
        this.algorithmOutputId = algorithmOutputId;
        this.version = version;
    }
    public int getAlgorithmOutputId() {
        return algorithmOutputId;
    }
    public void setAlgorithmOutputId(int algorithmOutputId) {
        this.algorithmOutputId = algorithmOutputId;
    }
    public int getVersion() {
        return version;
    }
    public void setVersion(int version) {
        this.version = version;
    }
}
