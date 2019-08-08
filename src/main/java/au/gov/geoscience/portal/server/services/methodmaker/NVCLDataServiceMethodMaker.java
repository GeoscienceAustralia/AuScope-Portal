package au.gov.geoscience.portal.server.services.methodmaker;

import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.client.utils.URIBuilder;
import org.auscope.portal.core.services.methodmakers.AbstractMethodMaker;
import org.springframework.stereotype.Repository;

import java.net.URISyntaxException;

/**
 * Class for generating methods for communicating with an instance of the AuScope NVCL Data service
 *
 * Data Service API - https://twiki.auscope.org/wiki/CoreLibrary/WebServicesDevelopment
 * 
 * @author Josh Vote
 *
 */
@Repository
public class NVCLDataServiceMethodMaker extends AbstractMethodMaker {

    /**
     * The types of graphs that can be specified to the plot scalar service
     */
    public enum PlotScalarGraphType {
        StackedBarChart,
        ScatteredChart,
        LineChart
    }

    /**
     * Generates a method for making request for all NVCL DataSets that belong to a particular borehole
     * 
     * @param serviceUrl
     *            The URL of the NVCLDataService
     * @param holeIdentifier
     *            The unique ID of the borehole to query
     * @throws URISyntaxException
     */
    public HttpRequestBase getDatasetCollectionMethod(String serviceUrl, String holeIdentifier)
            throws URISyntaxException {
        HttpGet method = new HttpGet();
        URIBuilder builder = new URIBuilder(urlPathConcat(serviceUrl, "getDatasetCollection.html"));

        //set all of the parameters
        builder.setParameter("holeidentifier", holeIdentifier);
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
     * Generates a method for making a request for the Mosaic imagery for a particular logId
     *
     * The response will be either HTML or a binary stream representing an image
     * 
     * @param serviceUrl
     *            The URL of the NVCLDataService
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
    public HttpRequestBase getMosaicMethod(String serviceUrl, String logId, Integer width, Integer startSampleNo,
            Integer endSampleNo) throws URISyntaxException {
        HttpGet method = new HttpGet();

        URIBuilder builder = new URIBuilder(urlPathConcat(serviceUrl, "mosaic.html"));

        //set all of the parameters
        builder.setParameter("logid", logId);
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
     * Generates a method for making a request for the Mosaic imagery for a particular logId
     *
     * The response will be either HTML or a binary stream representing an image
     * 
     * @param serviceUrl
     *            The URL of the NVCLDataService
     * @param logId
     *            The logID (from a getLogCollection request) to query
     * @param width
     *            [Optional] the width of the image in pixel
     * @param height
     *            [Optional] the height of the image in pixel
     * @param startDepth
     *            [Optional] the start depth of a borehole collar
     * @param endDepth
     *            [Optional] the end depth of a borehole collar
     * @param samplingInterval
     *            [Optional] the interval of the sampling
     * @param graphType
     *            [Optional] The type of graph to plot
     * @return
     * @throws URISyntaxException
     */
    public HttpRequestBase getPlotScalarMethod(String serviceUrl, String logId, Integer startDepth, Integer endDepth,
            Integer width, Integer height, Double samplingInterval, PlotScalarGraphType graphType, Integer legend)
            throws URISyntaxException {
        HttpGet method = new HttpGet();

        URIBuilder builder = new URIBuilder(urlPathConcat(serviceUrl, "plotscalar.html"));

        //set all of the parameters
        builder.setParameter("logid", logId);
        if (width != null) {
            builder.setParameter("width", width.toString());
        }
        if (height != null) {
            builder.setParameter("height", height.toString());
        }
        if (startDepth != null) {
            builder.setParameter("startdepth", startDepth.toString());
        }
        if (endDepth != null) {
            builder.setParameter("enddepth", endDepth.toString());
        }
        if (samplingInterval != null) {
            builder.setParameter("samplinginterval", samplingInterval.toString());
        }

        if (legend != null) {
            builder.setParameter("legend", legend.toString());
        }

        if (graphType != null) {
            switch (graphType) {
            case LineChart:
                builder.setParameter("graphtype", "3");
                break;
            case ScatteredChart:
                builder.setParameter("graphtype", "2");
                break;
            case StackedBarChart:
                builder.setParameter("graphtype", "1");
                break;
            }
        }

        //attach them to the method
        method.setURI(builder.build());

        return method;
    }

    /**
     * TSG Download Service is part of the DownloadServices. When triggered, the tsg download service will download entire Hylogging dataset from Hylogging
     * database using TSG Adapter and deliver the full dataset in the form of TSG format. The user will have to first make a download request and come back to
     * check the download status.
     *
     * When the download is completed, a link will be provided to download the requested TSG Dataset in zip format.
     *
     * Note : Either one of the dataset id or match string must be provided and not both
     *
     * This method will return a HTML stream
     * 
     * @param serviceUrl
     *            The URL of the NVCLDataService
     * @param email
     *            The user's email address
     * @param datasetId
     *            [Optional] a dataset id chosen by user (list of dataset id can be obtained thru calling the get log collection service)
     * @param matchString
     *            [Optional] Its value is part or all of a proper drillhole name. The first dataset found to match in the database is downloaded
     * @param lineScan
     *            [Optional] yes or no. If no then the main image component is not downloaded. The default is yes.
     * @param spectra
     *            [Optional] yes or no. If no then the spectral component is not downloaded. The default is yes.
     * @param profilometer
     *            [Optional] yes or no. If no then the profilometer component is not downloaded. The default is yes.
     * @param trayPics
     *            [Optional] yes or no. If no then the individual tray pictures are not downloaded. The default is yes.
     * @param mosaicPics
     *            [Optional] yes or no. If no then the hole mosaic picture is not downloaded. The default is yes.
     * @param mapPics
     *            [Optional] yes or no. If no then the map pictures are not downloaded. The default is yes.
     * @return
     * @throws URISyntaxException
     */
    public HttpRequestBase getDownloadTSGMethod(String serviceUrl, String email, String datasetId, String matchString,
            Boolean lineScan, Boolean spectra, Boolean profilometer, Boolean trayPics, Boolean mosaicPics,
            Boolean mapPics) throws URISyntaxException {

        if ((datasetId == null && matchString == null) ||
                (datasetId != null && matchString != null)) {
            throw new IllegalArgumentException("must specify ONLY one of datasetId and matchString");
        }

        HttpGet method = new HttpGet();

        URIBuilder builder = new URIBuilder(urlPathConcat(serviceUrl, "downloadtsg.html"));

        //set all of the parameters
        builder.setParameter("email", email);
        if (datasetId != null) {
            builder.setParameter("datasetid", datasetId);
        }
        if (matchString != null) {
            builder.setParameter("match_string", matchString);
        }
        if (lineScan != null) {
            builder.setParameter("linescan", lineScan.booleanValue() ? "yes" : "no");
        }
        if (spectra != null) {
            builder.setParameter("spectra", spectra.booleanValue() ? "yes" : "no");
        }
        if (profilometer != null) {
            builder.setParameter("profilometer", profilometer.booleanValue() ? "yes" : "no");
        }
        if (trayPics != null) {
            builder.setParameter("traypics", trayPics.booleanValue() ? "yes" : "no");
        }
        if (mosaicPics != null) {
            builder.setParameter("mospic", mosaicPics.booleanValue() ? "yes" : "no");
        }
        if (mapPics != null) {
            builder.setParameter("mappics", mapPics.booleanValue() ? "yes" : "no");
        }

        //attach them to the method
        method.setURI(builder.build());

        return method;
    }

    /**
     * Checks a user's TSG download status
     *
     * This method will return a HTML stream
     *
     * @param serviceUrl
     *            The URL of the NVCLDataService
     * @param email
     *            The user's email address
     * @return
     * @throws URISyntaxException
     */
    public HttpRequestBase getCheckTSGStatusMethod(String serviceUrl, String email) throws URISyntaxException {
        HttpGet method = new HttpGet();
        URIBuilder builder = new URIBuilder(urlPathConcat(serviceUrl, "checktsgstatus.html"));

        //set all of the parameters
        builder.setParameter("email", email);

        //attach them to the method
        method.setURI(builder.build());

        return method;
    }

    /**
     * When triggered, the wfs download service will call the Observations and Measurements WFS request, get the GeoSciML? output and compress it into a zip
     * file for download. The user will have to first make a download request and come back to check the download status. When the download is completed, a link
     * will be provided to download the requested Observations and Measurements output in zip format.
     *
     * This method will return a HTML stream
     *
     * @param serviceUrl
     *            The URL of the NVCLDataService
     * @param email
     *            The user's email address
     * @param boreholeId
     *            selected borehole id (use as feature id for filtering purpose)
     * @param omUrl
     *            The valid url for the Observations and Measurements WFS
     * @param typeName
     *            The url parameter for the wfs request
     * @return
     * @throws URISyntaxException
     */
    public HttpRequestBase getDownloadWFSMethod(String serviceUrl, String email, String boreholeId, String omUrl,
            String typeName) throws URISyntaxException {
        HttpGet method = new HttpGet();

        URIBuilder builder = new URIBuilder(urlPathConcat(serviceUrl, "downloadwfs.html"));

        //set all of the parameters
        builder.setParameter("email", email);
        builder.setParameter("boreholeid", boreholeId);
        builder.setParameter("serviceurl", omUrl + "wfs");
        builder.setParameter("typename", typeName);

        //attach them to the method
        method.setURI(builder.build());

        return method;
    }

    /**
     * Checks a user's WFS download status
     *
     * This method will return a HTML stream
     *
     * @param serviceUrl
     *            The URL of the NVCLDataService
     * @param email
     *            The user's email address
     * @return
     * @throws URISyntaxException
     */
    public HttpRequestBase getCheckWFSStatusMethod(String serviceUrl, String email) throws URISyntaxException {
        HttpGet method = new HttpGet();

        URIBuilder builder = new URIBuilder(urlPathConcat(serviceUrl, "checkwfsstatus.html"));

        //set all of the parameters
        builder.setParameter("email", email);

        //attach them to the method
        method.setURI(builder.build());

        return method;
    }
}
