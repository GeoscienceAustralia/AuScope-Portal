package au.gov.geoscience.portal.server.services;

import au.com.bytecode.opencsv.CSVReader;
import au.gov.geoscience.portal.server.nvcldataservice.*;
import au.gov.geoscience.portal.server.nvcldataservice.BinnedCSVResponse.Bin;
import au.gov.geoscience.portal.server.services.methodmaker.NVCL2_0_DataServiceMethodMaker;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpRequestBase;
import org.auscope.portal.core.server.http.HttpServiceCaller;
import org.auscope.portal.core.util.DOMUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Service
public class NVCL2_0_DataService {

    private static final String NVCL_ANALYTICAL_SERVICES_URL = "http://auscope-services-test.arrc.csiro.au/sissvoc/";

    private final Log log = LogFactory.getLog(getClass());
    private NVCL2_0_DataServiceMethodMaker nvclMethodMaker;
    private HttpServiceCaller httpServiceCaller;
    private String analyticalServicesUrl;

    @Autowired
    public NVCL2_0_DataService(HttpServiceCaller httpServiceCaller,
                               NVCL2_0_DataServiceMethodMaker nvclMethodMaker,
                               @Value(value = NVCL_ANALYTICAL_SERVICES_URL) String analyticalServicesUrl) {
        this.nvclMethodMaker = nvclMethodMaker;
        this.httpServiceCaller = httpServiceCaller;
        this.analyticalServicesUrl = analyticalServicesUrl;
    }

    /**
     * Makes a CSV download request from NVCL 2.0 service and returns the resulting data in a CSVDownloadResponse
     *
     * Response should be a stream of bytes for a CSV file
     *
     * @param serviceUrl
     *            The URL of an observation and measurements URL (obtained from a getDatasetCollection response)
     * @param datasetId
     *            The dataset to download
     * @return
     * @throws Exception
     */

    public CSVDownloadResponse getNVCL2_0_CSVDownload(String serviceUrl, String[] logIds) throws Exception {

        serviceUrl += "downloadscalars.html";

        HttpRequestBase method = nvclMethodMaker.getDownloadCSVMethod(serviceUrl, logIds);
        HttpResponse httpResponse = httpServiceCaller.getMethodResponseAsHttpResponse(method);
        InputStream responseStream = httpResponse.getEntity().getContent();
        Header contentHeader = httpResponse.getEntity().getContentType();

        return new CSVDownloadResponse(responseStream, contentHeader == null ? null : contentHeader.getValue());
    }

    public TrayThumbNailResponse getTrayThumbNail(String dataSetId, String serviceUrl, String logId,
                                                  Integer width, Integer startSampleNo, Integer endSampleNo) throws Exception {

        HttpRequestBase method = nvclMethodMaker.getTrayThumbNailMethodMaker(dataSetId, serviceUrl, logId, width,
                startSampleNo, endSampleNo);
        HttpResponse httpResponse = httpServiceCaller.getMethodResponseAsHttpResponse(method);
        InputStream responseStream = httpResponse.getEntity().getContent();
        Header contentHeader = httpResponse.getEntity().getContentType();

        return new TrayThumbNailResponse(responseStream, contentHeader == null ? null : contentHeader.getValue());

    }

    /**
     * Makes and parses a getLogCollection request to a NVCLDataService
     * 
     * @param serviceUrl
     *            The NVCLDataService url
     * @param datasetId
     *            The unique dataset ID to query
     * @param forMosaicService
     *            [Optional] indicates if the getLogCollection service should generate a result specifically for the use of a Mosaic Service
     * @throws Exception
     */
    public List<GetLogCollectionResponse> getLogCollection(String serviceUrl, String datasetId, Boolean forMosaicService)
            throws Exception {
        HttpRequestBase method = nvclMethodMaker.getLogCollectionMethod(serviceUrl, datasetId, forMosaicService);

        //Make our request, parse it into a DOM document
        InputStream responseStream = httpServiceCaller.getMethodResponseAsStream(method);
        Document responseDoc = DOMUtil.buildDomFromStream(responseStream);

        //Get our dataset nodes
        XPathExpression expr = DOMUtil.compileXPathExpr("LogCollection/Log");
        NodeList nodeList = (NodeList) expr.evaluate(responseDoc, XPathConstants.NODESET);

        //Parse our response objects
        List<GetLogCollectionResponse> responseObjs = new ArrayList<GetLogCollectionResponse>();
        XPathExpression exprLogId = DOMUtil.compileXPathExpr("LogID");
        XPathExpression exprLogName = DOMUtil.compileXPathExpr("LogName");
        XPathExpression exprSampleCount = DOMUtil.compileXPathExpr("SampleCount");

        for (int i = 0; i < nodeList.getLength(); i++) {
            Node node = nodeList.item(i);

            String logId = (String) exprLogId.evaluate(node, XPathConstants.STRING);
            String logName = (String) exprLogName.evaluate(node, XPathConstants.STRING);
            String sampleCountString = (String) exprSampleCount.evaluate(node, XPathConstants.STRING);
            int sampleCount = 0;
            if (sampleCountString != null && !sampleCountString.isEmpty()) {
                sampleCount = Integer.parseInt(sampleCountString);
            }

            responseObjs.add(new GetLogCollectionResponse(logId, logName, sampleCount));
        }

        return responseObjs;
    }

    /**
     * Makes and parses an NVCL getAlgorithms request.
     * @param serviceUrl
     * @return
     */
    public List<AlgorithmOutputResponse> getAlgorithms(String serviceUrl) throws Exception {
        HttpRequestBase method = nvclMethodMaker.getAlgorithms(serviceUrl);
        String responseText = httpServiceCaller.getMethodResponseAsString(method);
        Document responseDoc = DOMUtil.buildDomFromString(responseText, false);

        XPathExpression expr = DOMUtil.compileXPathExpr("Algorithms/algorithms");
        XPathExpression exprOutputs = DOMUtil.compileXPathExpr("outputs");
        XPathExpression exprVersions = DOMUtil.compileXPathExpr("versions");
        XPathExpression exprVersion = DOMUtil.compileXPathExpr("version");
        XPathExpression exprAlgorithmOutputId = DOMUtil.compileXPathExpr("algorithmoutputID");
        NodeList algorithmNodeList = (NodeList) expr.evaluate(responseDoc, XPathConstants.NODESET);

        XPathExpression exprAlgorithmId = DOMUtil.compileXPathExpr("algorithmID");
        XPathExpression exprName = DOMUtil.compileXPathExpr("name");

        ArrayList<AlgorithmOutputResponse> responseObjs = new ArrayList<AlgorithmOutputResponse>();

        for (int i = 0; i < algorithmNodeList.getLength(); i++) {
            Node node = algorithmNodeList.item(i);

            int algorithmId = Integer.parseInt((String) exprAlgorithmId.evaluate(node, XPathConstants.STRING));
            String algorithmName = (String) exprName.evaluate(node, XPathConstants.STRING);

            NodeList outputNodeList = (NodeList) exprOutputs.evaluate(node, XPathConstants.NODESET);
            for (int j = 0; j < outputNodeList.getLength(); j++) {
                Node outputNode = outputNodeList.item(j);
                String outputName = (String) exprName.evaluate(outputNode, XPathConstants.STRING);

                NodeList versionNodeList = (NodeList) exprVersions.evaluate(outputNode, XPathConstants.NODESET);
                ArrayList<AlgorithmVersion> versions = new ArrayList<AlgorithmVersion>(versionNodeList.getLength());
                for (int k = 0; k < versionNodeList.getLength(); k++) {
                    Node versionNode = versionNodeList.item(k);

                    int versionId = Integer.parseInt((String) exprVersion.evaluate(versionNode, XPathConstants.STRING));
                    int algorithmOutputId = Integer.parseInt((String) exprAlgorithmOutputId.evaluate(versionNode, XPathConstants.STRING));

                    versions.add(new AlgorithmVersion(algorithmOutputId, versionId));
                }

                responseObjs.add(new AlgorithmOutputResponse(algorithmId, algorithmName, outputName, versions));
            }
        }

        return responseObjs;
    }

    /**
     * Makes and parses a getClassifications request
     * @param serviceUrl
     * @param algorithmOutputId
     * @return
     * @throws Exception
     */
    public List<AlgorithmOutputClassification> getClassifications(String serviceUrl, int algorithmOutputId) throws Exception {
        HttpRequestBase method = nvclMethodMaker.getClassifications(serviceUrl, algorithmOutputId);
        String responseText = httpServiceCaller.getMethodResponseAsString(method);
        Document responseDoc = DOMUtil.buildDomFromString(responseText, false);

        XPathExpression exprNodes = DOMUtil.compileXPathExpr("ClassificationsCollection/classifications");
        XPathExpression exprClassText = DOMUtil.compileXPathExpr("classText");
        XPathExpression exprColor = DOMUtil.compileXPathExpr("colour");
        XPathExpression exprIndex = DOMUtil.compileXPathExpr("index");

        NodeList classNodeList = (NodeList) exprNodes.evaluate(responseDoc, XPathConstants.NODESET);
        ArrayList<AlgorithmOutputClassification> responseObjs = new ArrayList<AlgorithmOutputClassification>(classNodeList.getLength());
        for (int i = 0; i < classNodeList.getLength(); i++) {
            Node classNode = classNodeList.item(i);

            String classText = (String) exprClassText.evaluate(classNode, XPathConstants.STRING);
            int color = Integer.parseInt((String) exprColor.evaluate(classNode, XPathConstants.STRING));
            int index = Integer.parseInt((String) exprIndex.evaluate(classNode, XPathConstants.STRING));

            responseObjs.add(new AlgorithmOutputClassification(classText, color, index));
        }

        return responseObjs;
    }


    /**
     * Submits a NVCL analytical processing job. Returns true if the remote service reports success, false if it reports failure
     * @param serviceUrl Base endpoint for the NVCL Analytical Services
     * @param email
     * @param jobName
     * @param wfsUrls
     * @param wfsFilter
     * @param algorithmOutputId
     * @param classification
     * @param startDepth
     * @param endDepth
     * @param operator
     * @param value
     * @param units
     * @param span
     */
    public boolean submitProcessingJob(String email, String jobName, String[] wfsUrls, String wfsFilter,
            int algorithmOutputId, String classification, int startDepth, int endDepth, String operator, String value, String units, int span) throws Exception {
        HttpRequestBase method = nvclMethodMaker.submitProcessingJob(analyticalServicesUrl, email, jobName, wfsUrls, wfsFilter, algorithmOutputId, classification, startDepth, endDepth, operator, value, units, span);
        String responseText = httpServiceCaller.getMethodResponseAsString(method);
        JSONObject response = JSONObject.fromObject(responseText);
        return response.getString("response").toString().toLowerCase().equals("success");
    }

    /**
     * Queries for the list of analytical processing jobs submitted for a particular email.
     * @param serviceUrl
     * @param email
     * @return
     * @throws Exception
     */
    public List<AnalyticalJobStatus> checkProcessingJobs(String email) throws Exception {
        List<AnalyticalJobStatus> parsedStatuses = new ArrayList<AnalyticalJobStatus>();

        HttpRequestBase method = nvclMethodMaker.checkProcessingJob(analyticalServicesUrl, email);
        String responseText = httpServiceCaller.getMethodResponseAsString(method);
        JSONArray response = JSONArray.fromObject(responseText);
        for (Object i : response) {
            JSONObject obj = (JSONObject) i;

            AnalyticalJobStatus status = new AnalyticalJobStatus();
            status.setJobId(obj.getString("jobid"));
            status.setJobDescription(obj.getString("jobDescription"));
            status.setEmail(obj.getString("email"));
            status.setStatus(obj.getString("status"));
            status.setJobUrl(obj.getString("joburl"));
            status.setMessage(obj.getString("message"));
            status.setTimeStamp(obj.getString("jmstimestamp"));
            status.setMsgId(obj.getString("jmsmsgID"));
            status.setCorrelationId(obj.getString("jmscorrelationID"));

            parsedStatuses.add(status);
        }

        return parsedStatuses;
    }

    /**
     * Queries for the results of a given job.
     * @param jobId
     * @return
     * @throws Exception
     */
    public AnalyticalJobResults getProcessingResults(String jobId) throws Exception {
        HttpRequestBase method = nvclMethodMaker.getProcessingJobResults(analyticalServicesUrl, jobId);
        String responseText = httpServiceCaller.getMethodResponseAsString(method);
        JSONObject response = JSONObject.fromObject(responseText);

        AnalyticalJobResults results = new AnalyticalJobResults(response.getString("jobid"));
        results.setJobDescription(response.getString("jobDescription"));
        results.setEmail(response.getString("email"));


        JSONArray successes = response.getJSONArray("boreholes");
        List<String> successIds = new ArrayList<String>(successes.size());
        for (Object obj : successes) {
            successIds.add(((JSONObject) obj).getString("id"));
        }
        results.setPassBoreholes(successIds);

        JSONArray fails = response.getJSONArray("failedBoreholes");
        List<String> failIds = new ArrayList<String>(fails.size());
        for (Object obj : fails) {
            failIds.add(((JSONObject) obj).getString("id"));
        }
        results.setFailBoreholes(failIds);

        JSONArray errors = response.getJSONArray("errorBoreholes");
        List<String> errorIds = new ArrayList<String>(errors.size());
        for (Object obj : errors) {
            errorIds.add(((JSONObject) obj).getString("id"));
        }
        results.setErrorBoreholes(errorIds);

        return results;
    }

    /**
     * Makes a request for scalar data from NVCL Analytics job and initiates binning
     * @param serviceUrl
     * @param jobId
     * @param boreholeId
     * @return
     * @throws Exception
     */
    public BinnedCSVResponse getNVCL2_0_JobsScalarBinned(String[] jobIds, String boreholeId, double binSizeMetres) throws Exception {
        BinnedCSVResponse binnedResponse = new BinnedCSVResponse();
        Bin[] totalBins = new Bin[0];
        for (String jobId: jobIds) {
            HttpRequestBase method = nvclMethodMaker.getNVCLJobsScalarMethod(analyticalServicesUrl, jobId, boreholeId);
            InputStream responseStream = httpServiceCaller.getMethodResponseAsStream(method);
            Bin[] bins = doBinning(binnedResponse, responseStream, binSizeMetres, '"', 1, 2, jobId);
            totalBins = (Bin[])ArrayUtils.addAll(totalBins, bins);
        }
        binnedResponse.setBinnedValues(totalBins);
        binnedResponse.setBinSize(binSizeMetres);
        return binnedResponse;
    }

    /**
     * Makes JSON download requests from an NVCL 2.0 service and parses the resulting data
     * @param serviceUrl
     * @param logIds
     * @return
     * @throws Exception
     */
    public String getNVCL2_0_JSONDownsampledData(String serviceUrl, String[] logIds) throws Exception {
        serviceUrl += "getDownsampledData.html";
        JSONArray outArr = new JSONArray();
        for (String logId: logIds) {
            HttpRequestBase method = nvclMethodMaker.getDownloadJSONMethod(serviceUrl, logId);
            String httpResponseStr = httpServiceCaller.getMethodResponseAsString(method);
            JSONArray inArr = JSONArray.fromObject(httpResponseStr);
            if (inArr.size()>0) {
                JSONObject firstObj = inArr.getJSONObject(0);
                if (firstObj.has("classCount")) {
                    JSONObject baseObj = new JSONObject();
                    baseObj.element("logId", logId);
                    baseObj.element("stringValues", inArr);
                    outArr.element(baseObj);
                } else if (firstObj.has("averageValue")) {
                    JSONObject baseObj = new JSONObject();
                    baseObj.element("logId", logId);
                    baseObj.element("numericValues", inArr);
                    outArr.element(baseObj);
                }
            }
        }
        return outArr.toString();
    }

    /**
     * Performs the binning by parsing the resulting data into a series of binSizeMetres bins where
     * each bin represents the average value for that range of the borehole. Uses CSV header as name for each bin.
     * @param method
     * @param binSizeMetres
     * @param startAtCol column number (1..N) where the data starts. If -1 use then it defaults to 2
     * @param stopAtCol column number (1..N) where the data stops (non-inclusive) -1 = data goes all the way to the last column
     * @param altName alternative name for a bin. Use null to force it to use CSV header
     * @return
     */
    private Bin[] doBinning(BinnedCSVResponse binnedResponse, InputStream responseStream, double binSizeMetres, char quoteChar, int startAtCol, int stopAtCol, String altName) throws Exception {
        final String MISSING_DATA_STRING = "null";
        final int INITIAL_LIST_SIZE = 512;

        CSVReader reader = null;
        Bin[] bins = null;

        try {
            //Prepare parsing
            reader = new CSVReader(new InputStreamReader(responseStream), ',', quoteChar, 0);
            String[] headerLine = reader.readNext();
            if (headerLine == null || headerLine.length <= startAtCol) {
                throw new IOException("No or malformed CSV header sent");
            }
            // Set start & stop columns to default
            if (stopAtCol<0) {
                stopAtCol=headerLine.length;
            }
            if (startAtCol<0) {
                startAtCol=2;
            }
            //Prepare our bins
            bins = new Bin[stopAtCol - startAtCol];
            List<HashMap<String, Integer>> valueCounts = new ArrayList<HashMap<String, Integer>>(bins.length);
            double[] numericTotal = new double[bins.length];
            int[] numericCount = new int[bins.length];
            double currentBinStartDepth = -Double.MAX_VALUE;
            int currentBinSize = 0;
            for (int i = 0; i < bins.length; i++) {
                String name = headerLine[startAtCol + i];
                if (altName!=null) {
                    name=altName;
                }
                bins[i] = binnedResponse.new Bin(name, new ArrayList<Double>(INITIAL_LIST_SIZE), true, new ArrayList<Map<String, Integer>>(INITIAL_LIST_SIZE), new ArrayList<String>(INITIAL_LIST_SIZE), new ArrayList<Double>(INITIAL_LIST_SIZE));
                bins[i].setNumeric(true);
                valueCounts.add(new HashMap<String, Integer>());
            }

            //Start parsing our data - loading it into bins
            String[] dataLine = null;
            while ((dataLine = reader.readNext()) != null) {
                if (dataLine.length != headerLine.length) {
                    continue; //skip malformed lines
                }

                //If we've exceeded our current bin size - save the data and start a new bin
                double depth = Double.parseDouble(dataLine[0]);
                if (depth - currentBinStartDepth >= binSizeMetres) {

                    if (currentBinStartDepth == -Double.MAX_VALUE) {
                        currentBinStartDepth = depth;
                    }

                    for (int i = 0; i < bins.length; i++) {
                        if (bins[i].isNumeric()) {
                            if (numericCount[i] > 0) {
                                bins[i].getNumericValues().add(numericTotal[i] / (double) numericCount[i]);
                                bins[i].getStartDepths().add(currentBinStartDepth);
                            }
                        } else {
                            String value = getMostCountedValue(valueCounts.get(i));
                            if (value != null) {
                                bins[i].getStartDepths().add(currentBinStartDepth);
                                bins[i].getHighStringValues().add(value);
                                bins[i].getStringValues().add(valueCounts.get(i));
                            }
                        }
                    }

                    //Reset our working bin data
                    for (int i = 0; i < bins.length; i++) {
                        valueCounts.set(i, new HashMap<String, Integer>());
                        numericTotal[i] = 0.0;
                        numericCount[i] = 0;
                    }

                    currentBinStartDepth = depth;
                    currentBinSize = 0;
                }

                //Build up our current bin
                boolean dataAdded = false;
                for (int i = 0; i < bins.length; i++) {
                    String rawBinData = dataLine[startAtCol + i];
                    if (rawBinData == null || rawBinData.isEmpty() || rawBinData.equals(MISSING_DATA_STRING)) {
                        continue; //skip missing data
                    } else {
                        dataAdded = true;
                    }

                    if (bins[i].isNumeric()) {
                        try {
                            double newData = Double.parseDouble(rawBinData);
                            numericCount[i]++;
                            numericTotal[i] += newData;
                        } catch (NumberFormatException nfe) {
                            //OK - this column isn't actually numeric
                            bins[i].setNumeric(false);
                        }
                    }

                    if (!bins[i].isNumeric()) {
                        Integer currentCount = valueCounts.get(i).get(rawBinData);
                        if (currentCount == null) {
                            valueCounts.get(i).put(rawBinData, 1);
                        } else {
                            valueCounts.get(i).put(rawBinData, currentCount + 1);
                        }
                    }
                }
                if (dataAdded) {
                    currentBinSize++;
                }
            }

            //If we've got a partial bin at the end - let's include the data
            if (currentBinSize > 0) {
                for (int i = 0; i < bins.length; i++) {
                    if (bins[i].isNumeric()) {
                        if (numericCount[i] > 0) {
                            bins[i].getNumericValues().add(numericTotal[i] / (double) numericCount[i]);
                            bins[i].getStartDepths().add(currentBinStartDepth);
                        }
                    } else {
                        String value = getMostCountedValue(valueCounts.get(i));
                        if (value != null) {
                            bins[i].getStartDepths().add(currentBinStartDepth);
                            bins[i].getHighStringValues().add(value);
                            bins[i].getStringValues().add(valueCounts.get(i));
                        }
                    }
                }
            }


        } finally {
            IOUtils.closeQuietly(reader);
            IOUtils.closeQuietly(responseStream);
        }

        return bins;
    }

    private String getMostCountedValue(HashMap<String, Integer> map) {
        String largestValue = null;
        int largestCount = Integer.MIN_VALUE;

        for (Map.Entry<String, Integer> entry : map.entrySet()) {
            if (entry.getValue() > largestCount) {
                largestCount = entry.getValue();
                largestValue = entry.getKey();
            }
        }

        return largestValue;
    }

    /**
     * Given boreholeId, gets the TSG job Id and job name
     *
     * @param serviceUrl
     * @param logIds
     * @return string of colour tables in JSON format
     * @throws Exception
     */
    public JSONArray getNVCL2_0_getTsgJobsByBoreholeId(String boreholeId) throws Exception {
        JSONArray outArr = new JSONArray();
        HttpRequestBase method = nvclMethodMaker.getTSGJobsByBoreholeIdMethod(analyticalServicesUrl, boreholeId);
        String httpResponseStr = httpServiceCaller.getMethodResponseAsString(method);
        JSONArray inArray = JSONArray.fromObject(httpResponseStr);
        for (Object i : inArray) {
            JSONObject inObj = (JSONObject) i;
            if (inObj.has("jobid") && inObj.has("jobName")) {
                String jobId = inObj.getString("jobid");
                String jobName = inObj.getString("jobName");
                JSONObject outObj = new JSONObject();
                outObj.element("boreholeId", boreholeId);
                outObj.element("jobId", jobId);
                outObj.element("jobName", jobName);
                outArr.add(outObj);
            }
        }
        return outArr;
    }


}
