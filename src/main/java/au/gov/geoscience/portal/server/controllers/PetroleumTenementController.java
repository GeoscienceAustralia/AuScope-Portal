package au.gov.geoscience.portal.server.controllers;

import org.auscope.portal.core.server.controllers.BasePortalController;
import org.auscope.portal.core.services.WMSService;

import au.gov.geoscience.portal.server.services.PetroleumTenementService;

public class PetroleumTenementController extends BasePortalController {
    private WMSService wmsService;

    public PetroleumTenementController(WMSService wmsService, PetroleumTenementService petroleumTenementService){

    }
}
