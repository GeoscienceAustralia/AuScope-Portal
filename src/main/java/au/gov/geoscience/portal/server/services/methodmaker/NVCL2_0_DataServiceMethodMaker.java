package au.gov.geoscience.portal.server.services.methodmaker;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.client.utils.URIBuilder;
import org.auscope.portal.core.services.methodmakers.AbstractMethodMaker;
import org.springframework.stereotype.Repository;

import java.net.URISyntaxException;

@Repository
public class NVCL2_0_DataServiceMethodMaker extends AbstractMethodMaker {

    public HttpRequestBase getDownloadCSVMethod(String serviceUrl, String[] logIds) throws Exception {

        HttpGet method = new HttpGet(serviceUrl);
        URIBuilder builder = new URIBuilder(serviceUrl);

        for (String logId : logIds) {
            builder.addParameter("logid", logId);
        }

        method.setURI(builder.build());
        return method;
    }

    /**
     * Generates a method for making a request to NVCL 2.0 for the Mosaic imagery for a particular logId
     *
     * The response will be either HTML or a binary stream representing an image
     *
     * @param serviceUrl
     *            The URL of the NVCLDataService
     * @param logId
     *            The dataSetId (from a getLogCollection request) to query
     * @param logId
     *            The logID (from a getLogCollection request) to query
     * @param width
     *            [Optional] specify the number of column the images are to be displayed
     * @param startSampleNo
     *            [Optional] the first sample image to be displayed
     * @param endSampleNo
     *            [Optional] the last sample image to be displayed
     * @return
     * @throws URISyntaxException
     */
    public HttpRequestBase getTrayThumbNailMethodMaker(String dataSetId, String serviceUrl, String logId,
            Integer width, Integer startSampleNo, Integer endSampleNo) throws URISyntaxException {
        HttpGet method = new HttpGet();

        URIBuilder builder = new URIBuilder(urlPathConcat(serviceUrl, "mosaictraythumbnail.html"));

        //set all of the parameters
        builder.setParameter("logid", logId);
        builder.setParameter("datasetid", dataSetId);
        if (width != null) {
            builder.setParameter("width", width.toString());
        }
        if (startSampleNo != null) {
            builder.setParameter("startsampleno", startSampleNo.toString());
        }
        if (endSampleNo != null) {
            builder.setParameter("endsampleno", endSampleNo.toString());
        }

        //attach them to the method
        method.setURI(builder.build());

        return method;
    }

    /**
     * Generates a method for making a request for all NVCL logged elements that belong to a particular dataset
     *
     * @param serviceUrl
     *            The URL of the NVCLDataService
     * @param datasetId
     *            The dataset ID to query
     * @param forMosaicService
     *            [Optional] indicates if the getLogCollection service should generate a result specifically for the use of a Mosaic Service
     * @return
     * @throws URISyntaxException
     */
    public HttpRequestBase getLogCollectionMethod(String serviceUrl, String datasetId, Boolean forMosaicService)
            throws URISyntaxException {
        HttpGet method = new HttpGet();

        URIBuilder builder = new URIBuilder(urlPathConcat(serviceUrl, "getLogCollection.html"));

        //set all of the parameters
        builder.setParameter("datasetid", datasetId);
        if (forMosaicService != null) {
            builder.setParameter("mosaicsvc", forMosaicService.booleanValue() ? "yes" : "no");
        }

        //attach them to the method
        method.setURI(builder.build());

        return method;
    }

    /**
     * Generates a method for returning all algorithms supported by the NVCL analytical engine
     * @param serviceUrl
     * @return
     * @throws URISyntaxException
     */
    public HttpRequestBase getAlgorithms(String serviceUrl) throws URISyntaxException {
        HttpGet method = new HttpGet();
        URIBuilder builder = new URIBuilder(urlPathConcat(serviceUrl, "getAlgorithms.html"));
        method.setURI(builder.build());
        return method;
    }

    /**
     * Generates a method for returning all classifications for a given algorithm (supported by the NVCL analytical engine)
     * @param serviceUrl
     * @return
     * @throws URISyntaxException
     */
    public HttpRequestBase getClassifications(String serviceUrl, int algorithmOutputId) throws URISyntaxException {
        HttpGet method = new HttpGet();
        URIBuilder builder = new URIBuilder(urlPathConcat(serviceUrl, "getClassifications.html"));
        builder.setParameter("algorithmoutputid", Integer.toString(algorithmOutputId));
        method.setURI(builder.build());
        return method;
    }

    /**
     * Generates a method for submitting an NVCL processing job for a given (user) email
     *
     * @return
     * @throws URISyntaxException
     */
    public HttpRequestBase submitProcessingJob(String serviceUrl, String email, String jobName, String[] wfsUrls, String wfsFilter,
            int algorithmOutputId, String classification, int startDepth, int endDepth, String operator, String value, String units, int span) throws URISyntaxException {
        HttpGet method = new HttpGet();

        URIBuilder builder = new URIBuilder(urlPathConcat(serviceUrl, "submitNVCLAnalyticalJob.do"));
        builder.setParameter("email", email);
        builder.setParameter("jobname", jobName);
        builder.setParameter("algorithmoutputid", Integer.toString(algorithmOutputId));
        builder.setParameter("classification", classification);
        builder.setParameter("startdepth", Integer.toString(startDepth));
        builder.setParameter("enddepth", Integer.toString(endDepth));
        builder.setParameter("logicalop", operator);
        builder.setParameter("value", value);
        builder.setParameter("units", units);
        builder.setParameter("span", Integer.toString(span));
        builder.setParameter("serviceurls", StringUtils.join(wfsUrls, ','));
        builder.setParameter("filter", wfsFilter);

        method.setURI(builder.build());
        return method;
    }

    /**
     * Generates a method for checking an NVCL processing job for a given (user) email
     *
     * @return
     * @throws URISyntaxException
     */
    public HttpRequestBase checkProcessingJob(String serviceUrl, String email) throws URISyntaxException {
        HttpGet method = new HttpGet();

        URIBuilder builder = new URIBuilder(urlPathConcat(serviceUrl, "checkNVCLAnalyticalJobStatus.do"));
        builder.setParameter("email", email);

        method.setURI(builder.build());
        return method;
    }

    /**
     * Generates a method for get an NVCL processing job outputs
     *
     * @return
     * @throws URISyntaxException
     */
    public HttpRequestBase getProcessingJobResults(String serviceUrl, String jobId) throws URISyntaxException {
        HttpGet method = new HttpGet();

        URIBuilder builder = new URIBuilder(urlPathConcat(serviceUrl, "getNVCLAnalyticalJobResult.do"));
        builder.setParameter("jobid", jobId);

        method.setURI(builder.build());
        return method;
    }

    /**
     * Generates a method to get scalars that result from an NVCL scalar processing job
     *
     * @return
     * @throws URISyntaxException
     */
    public  HttpRequestBase getNVCLJobsScalarMethod(String serviceUrl, String jobId, String boreholeId) throws URISyntaxException {
        HttpGet method = new HttpGet();
        URIBuilder builder = new URIBuilder(urlPathConcat(serviceUrl, "doDownloadscalar.do"));
        builder.setParameter("jobid", jobId);
        builder.setParameter("boreholeid", boreholeId);

        method.setURI(builder.build());
        return method;
    }

    /**
     * Generates a method for making a request to NVCL 2.0 for downloading data for plotting
     *
     * The response will be 'json'
     *
     *
     * @param serviceUrl
     *            The URL of the NVCLDataService
     * @param logId
     *            The logID (from a getLogCollection request) to query
     * @return
     * @throws URISyntaxException
     */
    public HttpRequestBase getDownloadJSONMethod(String serviceUrl, String logId) throws Exception {
        HttpGet method = new HttpGet(serviceUrl);
        URIBuilder builder = new URIBuilder(serviceUrl);
        builder.addParameter("logid", logId);
        builder.addParameter("outputformat","json");
        method.setURI(builder.build());
        return method;
    }

    /**
     * Generates a method to get job data for a TSG job, given borehole id
     *
     * @return
     * @throws URISyntaxException
     */
    public HttpRequestBase getTSGJobsByBoreholeIdMethod(String serviceUrl, String boreholeId) throws URISyntaxException {
        HttpGet method = new HttpGet();
        URIBuilder builder = new URIBuilder(urlPathConcat(serviceUrl, "getTsgJobsByBoreholeid.do"));
        builder.setParameter("boreholeid", boreholeId);

        method.setURI(builder.build());
        return method;
    }
}
