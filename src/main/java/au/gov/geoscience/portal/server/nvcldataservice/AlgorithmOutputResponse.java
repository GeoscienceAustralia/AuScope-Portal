package au.gov.geoscience.portal.server.nvcldataservice;

import java.io.Serializable;
import java.util.List;

/**
 * Flattened response from a NVCLDataService getAlgorithms request
 * @author Josh Vote (CSIRO)
 *
 */
public class AlgorithmOutputResponse implements Serializable {
    private int algorithmId;
    private String algorithmName;
    private String outputName;
    List<AlgorithmVersion> versions;

    public AlgorithmOutputResponse() {
        super();
    }

    public AlgorithmOutputResponse(int algorithmId, String algorithmName, String outputName,
            List<AlgorithmVersion> versions) {
        super();
        this.algorithmId = algorithmId;
        this.algorithmName = algorithmName;
        this.outputName = outputName;
        this.versions = versions;
    }

    public int getAlgorithmId() {
        return algorithmId;
    }
    public void setAlgorithmId(int algorithmId) {
        this.algorithmId = algorithmId;
    }
    public String getAlgorithmName() {
        return algorithmName;
    }
    public void setAlgorithmName(String algorithmName) {
        this.algorithmName = algorithmName;
    }
    public String getOutputName() {
        return outputName;
    }
    public void setOutputName(String outputName) {
        this.outputName = outputName;
    }
    public List<AlgorithmVersion> getVersions() {
        return versions;
    }
    public void setVersions(List<AlgorithmVersion> versions) {
        this.versions = versions;
    }
}
