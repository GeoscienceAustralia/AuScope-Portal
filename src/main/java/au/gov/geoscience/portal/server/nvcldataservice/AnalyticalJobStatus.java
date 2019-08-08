package au.gov.geoscience.portal.server.nvcldataservice;

import java.io.Serializable;

/**
 * Represents a response from a NVCL Analytical Services heckNVCLAnalyticalJobStatus.do
 * @author Josh Vote (CSIRO)
 *
 */
public class AnalyticalJobStatus implements Serializable {
    private String jobId;
    private String jobDescription;
    private String email;
    private String status;
    private String jobUrl;
    private String message;
    private String timeStamp;
    private String msgId;
    private String correlationId;

    public AnalyticalJobStatus() {
        super();
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
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getStatus() {
        return status;
    }
    public void setStatus(String status) {
        this.status = status;
    }
    public String getJobUrl() {
        return jobUrl;
    }
    public void setJobUrl(String jobUrl) {
        this.jobUrl = jobUrl;
    }
    public String getTimeStamp() {
        return timeStamp;
    }
    public void setTimeStamp(String timeStamp) {
        this.timeStamp = timeStamp;
    }
    public String getMsgId() {
        return msgId;
    }
    public void setMsgId(String msgId) {
        this.msgId = msgId;
    }
    public String getCorrelationId() {
        return correlationId;
    }
    public void setCorrelationId(String correlationId) {
        this.correlationId = correlationId;
    }

}
