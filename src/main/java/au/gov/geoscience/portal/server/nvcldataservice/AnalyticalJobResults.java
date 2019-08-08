package au.gov.geoscience.portal.server.nvcldataservice;

import java.io.Serializable;
import java.util.List;

/**
 * Represents the return value from a NVCL Analytical Services getNVCLAnalyticalJobResult.do request
 * @author Josh Vote (CSIRO)
 *
 */
public class AnalyticalJobResults implements Serializable {
    private String jobId;
    private String jobDescription;
    private String email;
    private List<String> passBoreholes;
    private List<String> failBoreholes;
    private List<String> errorBoreholes;

    public AnalyticalJobResults(String jobId) {
        super();
        this.jobId = jobId;
    }

    public String getJobId() {
        return jobId;
    }
    public void setJobId(String jobId) {
        this.jobId = jobId;
    }
    public String getJobDescription() {
        return jobDescription;
    }
    public void setJobDescription(String jobDescription) {
        this.jobDescription = jobDescription;
    }
    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public List<String> getPassBoreholes() {
        return passBoreholes;
    }
    public void setPassBoreholes(List<String> passBoreholes) {
        this.passBoreholes = passBoreholes;
    }
    public List<String> getFailBoreholes() {
        return failBoreholes;
    }
    public void setFailBoreholes(List<String> failBoreholes) {
        this.failBoreholes = failBoreholes;
    }
    public List<String> getErrorBoreholes() {
        return errorBoreholes;
    }
    public void setErrorBoreholes(List<String> errorBoreholes) {
        this.errorBoreholes = errorBoreholes;
    }
}
