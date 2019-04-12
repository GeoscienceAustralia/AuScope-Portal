package au.gov.geoscience.portal.services.filters;

import org.auscope.portal.core.services.methodmakers.filter.FilterBoundingBox;
import org.opengis.filter.Filter;

import java.util.ArrayList;
import java.util.List;

public class BoreholeViewFilter extends AbstractFilter {

    private static final String NAME = "gsmlp:name";
    private static final String DRILLSTARTDATE = "gsmlp:drillStartDate";
    private static final String DRILLENDDATE = "gsmlp:drillEndDate";

    public BoreholeViewFilter(String boreholeName, String dateOfDrilling, FilterBoundingBox bbox) {

        if (boreholeName !=null && !boreholeName.isEmpty()) {
            filterList.add(generateLikeFilter(NAME, boreholeName));
        }

        List<Filter> dateList = new ArrayList<>();

        if (dateOfDrilling !=null && !dateOfDrilling.isEmpty()) {
            dateList.add(generateLikeFilter(DRILLSTARTDATE,"*" + dateOfDrilling + "*"));
            dateList.add(generateLikeFilter(DRILLENDDATE,"*" + dateOfDrilling + "*"));
        }


        if (!dateList.isEmpty()) {
            filterList.add(filterFactory.or(dateList));
        }
        filter = filterFactory.and(filterList);
    }
}
