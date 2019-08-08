package au.gov.geoscience.portal.server.services.methodmaker;

import org.apache.http.NameValuePair;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.message.BasicNameValuePair;
import org.auscope.portal.core.services.methodmakers.sissvoc.SISSVoc3MethodMaker;

import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

/**
 * Extension to SISSVoc3MethodMaker to specialise for the National Virtual Core Library vocab config which defines a few extra functions
 * 
 * @author Josh Vote
 *
 */
public class NvclVocabMethodMaker extends SISSVoc3MethodMaker {
    /**
     * Generates a method for requesting all concepts (as rdf:Descriptions) in the specified repository
     *
     * The request supports rudimentary paging of the returned results
     *
     * @param sissVocUrl
     *            The base URL of a SISSVoc service
     * @param repository
     *            The repository name to query
     * @param format
     *            How the response should be structured.
     * @param pageSize
     *            [Optional] How many concepts should be returned per page
     * @param pageNumber
     *            [Optional] The page number to request (0 based)
     * @return
     * @throws URISyntaxException
     */
    public HttpRequestBase getAllScalars(String sissVocUrl, String repository, Format format, Integer pageSize,
            Integer pageNumber) throws URISyntaxException {
        List<NameValuePair> params = new ArrayList<NameValuePair>();

        appendPagingParams(params, pageSize, pageNumber);

        return buildGetMethod(sissVocUrl, repository, "scalar", format, params);
    }

    /**
     * Generates a method for requesting all concepts (as rdf:Descriptions) in the specified repository
     *
     * The request supports rudimentary paging of the returned results
     *
     * @param sissVocUrl
     *            The base URL of a SISSVoc service
     * @param repository
     *            The repository name to query
     * @param format
     *            How the response should be structured.
     * @param pageSize
     *            [Optional] How many concepts should be returned per page
     * @param pageNumber
     *            [Optional] The page number to request (0 based)
     * @return
     * @throws URISyntaxException
     */
    public HttpRequestBase getScalarsByLabel(String sissVocUrl, String repository, String label, Format format,
            Integer pageSize, Integer pageNumber) throws URISyntaxException {
        List<NameValuePair> params = new ArrayList<NameValuePair>();

        params.add(new BasicNameValuePair("anylabel", label));
        appendPagingParams(params, pageSize, pageNumber);

        return buildGetMethod(sissVocUrl, repository, "scalar", format, params);
    }
}
