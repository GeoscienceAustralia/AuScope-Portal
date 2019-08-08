package au.gov.geoscience.portal.server.nvcldataservice;

import java.io.InputStream;

public class PlotScalarResponse extends AbstractStreamResponse {
    /**
     * Creates a GetPlotScalarResponse
     * 
     * @param response
     *            The raw binary response
     * @param contentType
     *            The content type as a MIME string (expect either html or image MIME's).
     */
    public PlotScalarResponse(InputStream response, String contentType) {
        super(response, contentType);
    }
}
