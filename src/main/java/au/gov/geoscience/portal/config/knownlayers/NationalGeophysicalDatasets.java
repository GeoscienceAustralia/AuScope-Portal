package au.gov.geoscience.portal.config.knownlayers;

import org.auscope.portal.core.view.knownlayer.KnownLayer;
import org.auscope.portal.core.view.knownlayer.KnownLayerSelector;
import org.auscope.portal.core.view.knownlayer.WMSSelector;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;

@Configuration
public class NationalGeophysicalDatasets {

    private static final String GROUP = "National Geophysical Datasets";
    private static final int GROUP_ORDER = GroupOrder.NATIONAL_GEOPHYSICAL_DATASETS;


    @Bean(name = "knownTypeOnshoreSeismicSurveys")
    @Order(GROUP_ORDER)
    KnownLayer knownTypeOnshoreSeismicSurveys() {


        String id = "OnshoreSeismicSurveys";

        KnownLayerSelector selector = new WMSSelector("Onshore_Seismic_Surveys");
        KnownLayer knownLayer = new KnownLayer(id, selector);


        String name = "Onshore Seismic Surveys (government-funded)";
        knownLayer.setName(name);

        String description = "The Onshore Seismic Data of Australia is a collection of all land seismic traverses " +
                "cross the Australian continent and its margins. The data includes raw and processed data in SEGY " +
                "format. The metadata includes acquisition reports, processing reports, processed images, logs, and " +
                "so on. The data acquisition was carried out in Australia from 1949-2012 by Geoscience Australia " +
                "and various partners. The set of reflection and refraction data comprises over 12,000 km of " +
                "coverage, and provides an insight into the variations in crustal architecture in the varied " +
                "geological domains. The complete processed dataset was first available for public access in Oct " +
                "2013 (http://www.ga.gov.au/minerals/projects/current-projects/seismic-acquisition-processing.html). " +
                "The location of seismic traverses is shown by the Gallery link on the web page. The new survey data " +
                "will be updated on the web page by the official data release date. The attribute structure of the " +
                "dataset has also been revised to be more compatible with the GeoSciML data standard, published by " +
                "the IUGS Commission for Geoscience Information. The onshore seismic data were collected with " +
                "partner organizations: Australian Geodynamics Cooperative Research Centre, National Research " +
                "Facility for Earth Sounding, Australian Nuclear Science and Technology Organisation, Cooperative " +
                "Research Centre for Greenhouse Gas Technologies, Curtin University of Technology, Geological Survey " +
                "of New South Wales, NSW Department of Mineral Resources, NSW Department of Primary Industries " +
                "Mineral Resources, An organisation for a National Earth Science Infrastructure Program, Geological " +
                "Survey Western Australia, Northern Territory Geological Survey, Primary Industries and Resources " +
                "South Australia, Predictive Mineral Discovery Cooperative Research Centre, Queensland Geological " +
                "Survey, GeoScience Victoria Department of Primary Industries, Tasmania Development and Resources, " +
                "University of Western Australia.";

        knownLayer.setDescription(description);

        knownLayer.setGroup(GROUP);

        return knownLayer;
    }

    @Bean(name = "knownTypeAirborneSurveys2014")
    @Order(GROUP_ORDER + 1)
    KnownLayer knownTypeAirborneSurveys2014() {


        String id = "AirborneSurveys2014";

        KnownLayerSelector selector = new WMSSelector("AirborneSurveys2014");
        KnownLayer knownLayer = new KnownLayer(id, selector);


        String name = "Airborne Geophysical Surveys - June 2014";
        knownLayer.setName(name);

        String description = "This layer provides the outlines and specifications of 1085 airborne geophysical " +
                "surveys conducted by or for the Australian, State and Territory governments. Most of the data for " +
                "surveys referred to in this Index are available free on- line via the Geophysical Archive Data " +
                "Delivery System (GADDS - http://www.geoscience.gov.au/gadds). They comprise more than 34 million " +
                "line kilometres of mainly total magnetic intensity and gamma-ray spectrometric data. Land elevation " +
                "data derived from GPS recordings made during airborne magnetic and gamma-ray spectrometric surveys, " +
                "and electromagnetic data are also available for some areas. The associated index (Percival, P.J., " +
                "2014. Index of airborne geophysical surveys (Fourteenth Edition). Geoscience Australia Record " +
                "2014/014) contains the specifications of surveys.";
        knownLayer.setDescription(description);
        knownLayer.setGroup(GROUP);

        return knownLayer;
    }

    @Bean(name = "knownTypeTotalMagneticIntensity2015Greyscale")
    @Order(GROUP_ORDER + 2)
    KnownLayer knownTypeTotalMagneticIntensity2015Greyscale() {


        String id = "TotalMagneticIntensity2015Greyscale";

        KnownLayerSelector selector = new WMSSelector("TotalMagneticIntensity2015_greyscale");
        KnownLayer knownLayer = new KnownLayer(id, selector);


        String name = "Total Magnetic Intensity greyscale image - 2015";
        knownLayer.setName(name);

        String description = "The Total Magnetic Intensity (greyscale) image is created from a composite TMI grid of the Australian region with a grid cell spacing of ~3 seconds of arc (approximately 80 m). This image only includes airborne-derived TMI data for onshore and near-offshore continental areas. Since the fifth edition was released in 2010 data from 41 new surveys have been added to the database, acquired mainly by the State and Territory Geological Surveys. It is estimated that 31 500 000 line-kilometres of survey data were acquired to produce the grid data, 4 500 000 line-kilometres more than for the previous edition. Details of the specifications of individual airborne surveys can be found in the Fourteenth Edition of the Index of Airborne Geophysical Surveys (Percival, 2014). Matching of the grids in the database was achieved using a program called Gridmerge, which was originally developed within Geoscience Australia and has now been commercialised. This program was used to merge 41 new surveys to the 5th Edition Total Magnetic Intensity Anomaly Grid of Australia (Milligan et al., 2010). The 5th Edition merged 795 individual grids to create the compilation and to constrain long wavelengths, an independent data set, the Australia-wide Airborne Geophysical Survey (AWAGS) airborne magnetic data, was used to control the base levels of those survey grids which overlapped the AWAGS data (Milligan et al., 2009). As the 5th Edition was used as a base grid for the Gridmerge operation the new 6th Edition is essentially levelled to AWAGS. Individual and merged grids may be downloaded from the Geophysical Archive Data Delivery System (GADDS) on the Australian Government's Geoscience Portal at http://www.geoscience.gov.au/gadds. References: Milligan, P.R., Minty, B.R.S., Richardson, M. and Franklin, R., 2009. The Australia-wide Airborne Geophysical Survey - accurate continental magnetic coverage. Preview, 138, 70. Milligan, P.R., Franklin, R., Minty, B.R.S., Richardson, L.M. and Percival, P.J., 2010. Magnetic Anomaly Map of Australia (Fifth Edition), 1:5 000 000 scale, Geoscience Australia, Canberra. Percival, P.J., 2014. Index of airborne geophysical surveys (Fourteenth Edition). Geoscience Australia Record 2014/014.";
        knownLayer.setDescription(description);
        knownLayer.setGroup(GROUP);

        return knownLayer;
    }

    @Bean(name = "knownTypeTotalMagneticIntensity2015HSI")
    @Order(GROUP_ORDER + 3)
    KnownLayer knownTypeTotalMagneticIntensity2015HSI() {


        String id = "TotalMagneticIntensity2015HSI";

        KnownLayerSelector selector = new WMSSelector("TotalMagneticIntensity2015_HSI");
        KnownLayer knownLayer = new KnownLayer(id, selector);


        String name = "Total Magnetic Intensity HSI colour image - 2015";
        knownLayer.setName(name);

        String description = "The Total Magnetic Intensity (HSI colour) image is created from the magmap_v6_2015 which is a composite TMI grid of the Australian region with a grid cell spacing of ~3 seconds of arc (approximately 80 m). This image only includes airborne-derived TMI data for onshore and near-offshore continental areas. Since the fifth edition was released in 2010 data from 41 new surveys have been added to the database, acquired mainly by the State and Territory Geological Surveys. It is estimated that 31 500 000 line-kilometres of survey data were acquired to produce the grid data, 4 500 000 line-kilometres more than for the previous edition. Details of the specifications of individual airborne surveys can be found in the Fourteenth Edition of the Index of Airborne Geophysical Surveys (Percival, 2014). Matching of the grids in the database was achieved using a program called Gridmerge, which was originally developed within Geoscience Australia and has now been commercialised. This program was used to merge 41 new surveys to the 5th Edition Total Magnetic Intensity Anomaly Grid of Australia (Milligan et al., 2010). The 5th Edition merged 795 individual grids to create the compilation and to constrain long wavelengths, an independent data set, the Australia-wide Airborne Geophysical Survey (AWAGS) airborne magnetic data, was used to control the base levels of those survey grids which overlapped the AWAGS data (Milligan et al., 2009). As the 5th Edition was used as a base grid for the Gridmerge operation the new 6th Edition is essentially levelled to AWAGS. Individual and merged grids may be downloaded from the Geophysical Archive Data Delivery System (GADDS) on the Australian Government's Geoscience Portal at http://www.geoscience.gov.au/gadds. References: Milligan, P.R., Minty, B.R.S., Richardson, M. and Franklin, R., 2009. The Australia-wide Airborne Geophysical Survey - accurate continental magnetic coverage. Preview, 138, 70. Milligan, P.R., Franklin, R., Minty, B.R.S., Richardson, L.M. and Percival, P.J., 2010. Magnetic Anomaly Map of Australia (Fifth Edition), 1:5 000 000 scale, Geoscience Australia, Canberra. Percival, P.J., 2014. Index of airborne geophysical surveys (Fourteenth Edition). Geoscience Australia Record 2014/014.";
        knownLayer.setDescription(description);
        knownLayer.setGroup(GROUP);

        return knownLayer;
    }

    @Bean(name = "knownTypeTotalMagneticIntensityVRTP2015Greyscale")
    @Order(GROUP_ORDER + 4)
    KnownLayer knownTypeTotalMagneticIntensityVRTP2015Greyscale() {


        String id = "TotalMagneticIntensityVRTP2015Greyscale";

        KnownLayerSelector selector = new WMSSelector("TotalMagneticIntensityVRTP2015_greyscale");
        KnownLayer knownLayer = new KnownLayer(id, selector);


        String name = "Total Magnetic Intensity - Variable Reduction to Pole (VRTP) greyscale image - 2015";
        knownLayer.setName(name);

        String description = "The file is an image created from magmap_v6_2015_VRTP, a Variable Reduction to Pole " +
                "TMI grid of the Australian region with a grid cell spacing of ~3 seconds of arc (approximately " +
                "80 m). This image only includes airborne-derived TMI data for onshore and near-offshore continental " +
                "areas. The VRTP processing followed Cooper and Cowan's (2005) differential reduction to pole up to " +
                "5th order polynomial. Magnetic inclination and declination were derived from the IGRF-11 " +
                "geomagnetic reference model (Finlay et al., 2010) using a data representative date of January 2005 " +
                "and elevation 300 m.";
        knownLayer.setDescription(description);
        knownLayer.setGroup(GROUP);

        return knownLayer;
    }

    @Bean(name = "knownTypeGravityBouguerOnshore2010Greyscale")
    @Order(GROUP_ORDER + 5)
    KnownLayer knownTypeGravityBouguerOnshore2010Greyscale() {


        String id = "GravityBouguerOnshore2010Greyscale";

        KnownLayerSelector selector = new WMSSelector("GravityBouguerOnshore2010_greyscale");
        KnownLayer knownLayer = new KnownLayer(id, selector);


        String name = "Gravity Bouguer Onshore greyscale image - 2010";
        knownLayer.setName(name);

        String description = "The onshore Bouguer gravity grid (2010) represents gravity anomalies of the Australian " +
                "continent and surrounding region. The cell values represent simple Bouguer anomalies at a density " +
                "of 2.67 tonnes per cubic metre. The grid cell size is 0.5 minutes of arc, which is equivalent to " +
                "about 800 metres.";
        knownLayer.setDescription(description);
        knownLayer.setGroup(GROUP);

        return knownLayer;
    }

    @Bean(name = "knownTypeGravityAnomaly2010Greyscale")
    @Order(GROUP_ORDER + 6)
    KnownLayer knownTypeGravityAnomaly2010Greyscale() {


        String id = "GravityAnomaly2010Greyscale";

        KnownLayerSelector selector = new WMSSelector("GravityAnomaly2010_greyscale");
        KnownLayer knownLayer = new KnownLayer(id, selector);


        String name = "Gravity Anomaly greyscale image of the Australian Region - 2010";
        knownLayer.setName(name);

        String description = "The gravity anomaly grid (2010) represents gravity anomalies of the Australian continent and surrounding region. The grid combines accurate onshore gravity measurements, with satellite data over the offshore region. The cell values represent simple Bouguer anomalies at a density of 2.67 tonnes per cubic metre onshore and free-air anomalies offshore. The grid cell size is 0.5 minutes of arc, which is equivalent to about 800 metres.";
        knownLayer.setDescription(description);
        knownLayer.setGroup(GROUP);

        return knownLayer;
    }

    @Bean(name = "knownTypeGravityAnomaly2010HSI")
    @Order(GROUP_ORDER + 7)
    KnownLayer knownTypeGravityAnomaly2010HSI() {


        String id = "GravityAnomaly2010HSI";

        KnownLayerSelector selector = new WMSSelector("GravityAnomaly2010_HSI");
        KnownLayer knownLayer = new KnownLayer(id, selector);


        String name = "Gravity Anomaly image 2010 - HSI colour";
        knownLayer.setName(name);

        String description = "Hue-Saturation-Intensity (HSI) colour image of the national Gravity Anomaly grid, 2010. This image is made from a grid that represents gravity anomalies of the Australian region. The grid combines accurate onshore gravity measurements, with satellite data over the offshore region. The cell values represent simple Bouguer anomalies at a density of 2.67 tonnes per cubic metre onshore and free-air anomalies offshore. The grid cell size is 0.5 minutes of arc, which is equivalent to about 800 metres.";
        knownLayer.setDescription(description);
        knownLayer.setGroup(GROUP);

        return knownLayer;
    }

    @Bean(name = "knownTypeRadiometrics2015TernaryKThU")
    @Order(GROUP_ORDER + 8)
    KnownLayer knownTypeRadiometrics2015TernaryKThU() {


        String id = "Radiometrics2015TernaryKThU";

        KnownLayerSelector selector = new WMSSelector("Radiometrics2015_ternary_KThU");
        KnownLayer knownLayer = new KnownLayer(id, selector);


        String name = "Radiometrics Ternary KThU image 2010";
        knownLayer.setName(name);

        String description = "The national ternary radiometric image (2015 edition) shows the concentrations of the " +
                "radioelements potassium (K), uranium (U) and thorium (Th) at the Earth's surface as measured using " +
                "the airborne gamma-ray spectrometric method. The image is a false colour composite using the " +
                "colours red, blue and green to represent potassium, uranium and thorium respectively. White areas " +
                "have high concentrations of all the radioelements and dark areas have low concentrations. Areas " +
                "high in K only appear red, and areas high in U and Th but low in K appear turquoise (a mixture of " +
                "blue and green).";
        knownLayer.setDescription(description);
        knownLayer.setGroup(GROUP);

        return knownLayer;
    }

    @Bean(name = "knownTypeRadiometrics2015UnfilteredDose")
    @Order(GROUP_ORDER + 9)
    KnownLayer knownTypeRadiometrics2015UnfilteredDose() {


        String id = "Radiometrics2015UnfilteredDose";

        KnownLayerSelector selector = new WMSSelector("Radiometrics2015_unfiltered_dose");
        KnownLayer knownLayer = new KnownLayer(id, selector);


        String name = "Radiometrics Unfiltered Dose Image - 2015";
        knownLayer.setName(name);

        String description = "Unfiltered terrestrial dose rate derived as a linear combination of the unfiltered K, U and Th grids (Minty et al., 2009). Unit of measure: nG/h. The Radiometric Map of Australia dataset comprises gridsof potassium (K), uranium (U) and thorium (Th) element concentrations, and derivatives of these grids. This third edition was derived by seamlessly merging 45 new survey grids with the Second Edition Radiometric Map of Australia (Minty et al., 2010). Details of the specifications of individual airborne surveys can be found in the 2014 Edition of the Index of Airborne Geophysical Surveys (Percival, 2014). Matching of the grids in the database was achieved using a program called Gridmerge, originally developed within Geoscience Australia and has now been commercialised. This program was used to merge 45 new surveys to the Second Edition Radiometric Map of Australia. The second edition merged over 550 individual grids to create the compilation (Minty et al., 2009), and the Australia-wide Airborne Geophysical Survey (AWAGS) airborne radiometric data was used to control the base levels of those survey grids which overlapped the AWAGS data (Milligan et al., 2009). As the second edition was used as a base grid for the Gridmerge operation the new Third Edition is essentially levelled to AWAGS. Filtering: Potassium, uranium, thorium and dose rate grids are available in both filtered and unfiltered versions. The low-pass filtering was achieved by applying a 7-point, degree-3 Savitzky-Golay filter (Savitzky and Golay, 1964) to each of the original survey grids prior to grid merging.";
        knownLayer.setDescription(description);
        knownLayer.setGroup(GROUP);

        return knownLayer;
    }

    @Bean(name = "knownTypeRadiometrics2015FilteredDose")
    @Order(GROUP_ORDER + 10)
    KnownLayer knownTypeRadiometrics2015FilteredDose() {


        String id = "Radiometrics2015FilteredDose";

        KnownLayerSelector selector = new WMSSelector("Radiometrics2015_filtered_dose");
        KnownLayer knownLayer = new KnownLayer(id, selector);


        String name = "Radiometrics Filtered Dose Image - 2015";
        knownLayer.setName(name);

        String description = "Terrestrial dose rate derived as a linear combination of the filtered K, U and Th grids described above (see Minty et al., 2009). Unit of measure = nG/h. The Radiometric Map of Australia dataset comprises gridsof potassium (K), uranium (U) and thorium (Th) element concentrations, and derivatives of these grids. This third edition was derived by seamlessly merging 45 new survey grids with the Second Edition Radiometric Map of Australia (Minty et al., 2010). Details of the specifications of individual airborne surveys can be found in the 2014 Edition of the Index of Airborne Geophysical Surveys (Percival, 2014). Matching of the grids in the database was achieved using a program called Gridmerge, originally developed within Geoscience Australia and has now been commercialised. This program was used to merge 45 new surveys to the Second Edition Radiometric Map of Australia. The second edition merged over 550 individual grids to create the compilation (Minty et al., 2009), and the Australia-wide Airborne Geophysical Survey (AWAGS) airborne radiometric data was used to control the base levels of those survey grids which overlapped the AWAGS data (Milligan et al., 2009). As the second edition was used as a base grid for the Gridmerge operation the new Third Edition is essentially levelled to AWAGS. Filtering: Potassium, uranium, thorium and dose rate grids are available in both filtered and unfiltered versions. The low-pass filtering was achieved by applying a 7-point, degree-3 Savitzky-Golay filter (Savitzky and Golay, 1964) to each of the original survey grids prior to grid merging.";
        knownLayer.setDescription(description);
        knownLayer.setGroup(GROUP);

        return knownLayer;
    }

    @Bean(name = "knownTypeRadiometrics2015UnfilteredPcK")
    @Order(GROUP_ORDER + 11)
    KnownLayer knownTypeRadiometrics2015UnfilteredPcK() {


        String id = "Radiometrics2015UnfilteredPcK";

        KnownLayerSelector selector = new WMSSelector("Radiometrics2015_unfiltered_pcK");
        KnownLayer knownLayer = new KnownLayer(id, selector);


        String name = "Radiometrics Unfiltered K percent Image - 2015";
        knownLayer.setName(name);

        String description = "Unfiltered Potassium element concentrations (percent K). The Radiometric Map of Australia dataset comprises gridsof potassium (K), uranium (U) and thorium (Th) element concentrations, and derivatives of these grids. This third edition was derived by seamlessly merging 45 new survey grids with the Second Edition Radiometric Map of Australia (Minty et al., 2010). Details of the specifications of individual airborne surveys can be found in the 2014 Edition of the Index of Airborne Geophysical Surveys (Percival, 2014). Matching of the grids in the database was achieved using a program called Gridmerge, originally developed within Geoscience Australia and has now been commercialised. This program was used to merge 45 new surveys to the Second Edition Radiometric Map of Australia. The second edition merged over 550 individual grids to create the compilation (Minty et al., 2009), and the Australia-wide Airborne Geophysical Survey (AWAGS) airborne radiometric data was used to control the base levels of those survey grids which overlapped the AWAGS data (Milligan et al., 2009). As the second edition was used as a base grid for the Gridmerge operation the new Third Edition is essentially levelled to AWAGS. Filtering: Potassium, uranium, thorium and dose rate grids are available in both filtered and unfiltered versions. The low-pass filtering was achieved by applying a 7-point, degree-3 Savitzky-Golay filter (Savitzky and Golay, 1964) to each of the original survey grids prior to grid merging.";
        knownLayer.setDescription(description);
        knownLayer.setGroup(GROUP);

        return knownLayer;
    }

    @Bean(name = "knownTypeRadiometrics2015FilteredPcK")
    @Order(GROUP_ORDER + 12)
    KnownLayer knownTypeRadiometrics2015FilteredPcK() {


        String id = "Radiometrics2015FilteredPcK";

        KnownLayerSelector selector = new WMSSelector("Radiometrics2015_filtered_pcK");
        KnownLayer knownLayer = new KnownLayer(id, selector);


        String name = "Radiometrics Filtered K percent Image - 2015";
        knownLayer.setName(name);

        String description = "Low-pass filtered K element concentration (percent K). The Radiometric Map of Australia dataset comprises gridsof potassium (K), uranium (U) and thorium (Th) element concentrations, and derivatives of these grids. This third edition was derived by seamlessly merging 45 new survey grids with the Second Edition Radiometric Map of Australia (Minty et al., 2010). Details of the specifications of individual airborne surveys can be found in the 2014 Edition of the Index of Airborne Geophysical Surveys (Percival, 2014). Matching of the grids in the database was achieved using a program called Gridmerge, originally developed within Geoscience Australia and has now been commercialised. This program was used to merge 45 new surveys to the Second Edition Radiometric Map of Australia. The second edition merged over 550 individual grids to create the compilation (Minty et al., 2009), and the Australia-wide Airborne Geophysical Survey (AWAGS) airborne radiometric data was used to control the base levels of those survey grids which overlapped the AWAGS data (Milligan et al., 2009). As the second edition was used as a base grid for the Gridmerge operation the new Third Edition is essentially levelled to AWAGS. Filtering: Potassium, uranium, thorium and dose rate grids are available in both filtered and unfiltered versions. The low-pass filtering was achieved by applying a 7-point, degree-3 Savitzky-Golay filter (Savitzky and Golay, 1964) to each of the original survey grids prior to grid merging.";
        knownLayer.setDescription(description);
        knownLayer.setGroup(GROUP);

        return knownLayer;
    }

    @Bean(name = "knownTypeRadiometrics2015UnfilteredPpmTh")
    @Order(GROUP_ORDER + 13)
    KnownLayer knownTypeRadiometrics2015UnfilteredPpmTh() {


        String id = "Radiometrics2015UnfilteredPpmTh";

        KnownLayerSelector selector = new WMSSelector("Radiometrics2015_unfiltered_ppmTh");
        KnownLayer knownLayer = new KnownLayer(id, selector);


        String name = "Radiometrics Unfiltered Th ppm Image - 2015";
        knownLayer.setName(name);

        String description = "Unfiltered Thorium element concentrations (ppm eTh). The Radiometric Map of Australia dataset comprises gridsof potassium (K), uranium (U) and thorium (Th) element concentrations, and derivatives of these grids. This third edition was derived by seamlessly merging 45 new survey grids with the Second Edition Radiometric Map of Australia (Minty et al., 2010). Details of the specifications of individual airborne surveys can be found in the 2014 Edition of the Index of Airborne Geophysical Surveys (Percival, 2014). Matching of the grids in the database was achieved using a program called Gridmerge, originally developed within Geoscience Australia and has now been commercialised. This program was used to merge 45 new surveys to the Second Edition Radiometric Map of Australia. The second edition merged over 550 individual grids to create the compilation (Minty et al., 2009), and the Australia-wide Airborne Geophysical Survey (AWAGS) airborne radiometric data was used to control the base levels of those survey grids which overlapped the AWAGS data (Milligan et al., 2009). As the second edition was used as a base grid for the Gridmerge operation the new Third Edition is essentially levelled to AWAGS. Filtering: Potassium, uranium, thorium and dose rate grids are available in both filtered and unfiltered versions. The low-pass filtering was achieved by applying a 7-point, degree-3 Savitzky-Golay filter (Savitzky and Golay, 1964) to each of the original survey grids prior to grid merging.";
        knownLayer.setDescription(description);
        knownLayer.setGroup(GROUP);

        return knownLayer;
    }

    @Bean(name = "knownTypeRadiometrics2015FilteredPpmTh")
    @Order(GROUP_ORDER + 14)
    KnownLayer knownTypeRadiometrics2015FilteredPpmTh() {


        String id = "Radiometrics2015FilteredPpmTh";

        KnownLayerSelector selector = new WMSSelector("Radiometrics2015_filtered_ppmTh");
        KnownLayer knownLayer = new KnownLayer(id, selector);


        String name = "Radiometrics Filtered Th ppm Image - 2015";
        knownLayer.setName(name);

        String description = "Low-pass filtered Th element concentration (ppm eTh). The Radiometric Map of Australia dataset comprises gridsof potassium (K), uranium (U) and thorium (Th) element concentrations, and derivatives of these grids. This third edition was derived by seamlessly merging 45 new survey grids with the Second Edition Radiometric Map of Australia (Minty et al., 2010). Details of the specifications of individual airborne surveys can be found in the 2014 Edition of the Index of Airborne Geophysical Surveys (Percival, 2014). Matching of the grids in the database was achieved using a program called Gridmerge, originally developed within Geoscience Australia and has now been commercialised. This program was used to merge 45 new surveys to the Second Edition Radiometric Map of Australia. The second edition merged over 550 individual grids to create the compilation (Minty et al., 2009), and the Australia-wide Airborne Geophysical Survey (AWAGS) airborne radiometric data was used to control the base levels of those survey grids which overlapped the AWAGS data (Milligan et al., 2009). As the second edition was used as a base grid for the Gridmerge operation the new Third Edition is essentially levelled to AWAGS. Filtering: Potassium, uranium, thorium and dose rate grids are available in both filtered and unfiltered versions. The low-pass filtering was achieved by applying a 7-point, degree-3 Savitzky-Golay filter (Savitzky and Golay, 1964) to each of the original survey grids prior to grid merging.";
        knownLayer.setDescription(description);
        knownLayer.setGroup(GROUP);

        return knownLayer;
    }

    @Bean(name = "knownTypeRadiometrics2015UnfilteredPpmU")
    @Order(GROUP_ORDER + 15)
    KnownLayer knownTypeRadiometrics2015UnfilteredPpmU() {


        String id = "Radiometrics2015UnfilteredPpmU";

        KnownLayerSelector selector = new WMSSelector("Radiometrics2015_unfiltered_ppmU");
        KnownLayer knownLayer = new KnownLayer(id, selector);


        String name = "Radiometrics Unfiltered U ppm Image - 2015";
        knownLayer.setName(name);

        String description = "Unfiltered uranium element concentrations (ppm eU). The Radiometric Map of Australia dataset comprises gridsof potassium (K), uranium (U) and thorium (Th) element concentrations, and derivatives of these grids. This third edition was derived by seamlessly merging 45 new survey grids with the Second Edition Radiometric Map of Australia (Minty et al., 2010). Details of the specifications of individual airborne surveys can be found in the 2014 Edition of the Index of Airborne Geophysical Surveys (Percival, 2014). Matching of the grids in the database was achieved using a program called Gridmerge, originally developed within Geoscience Australia and has now been commercialised. This program was used to merge 45 new surveys to the Second Edition Radiometric Map of Australia. The second edition merged over 550 individual grids to create the compilation (Minty et al., 2009), and the Australia-wide Airborne Geophysical Survey (AWAGS) airborne radiometric data was used to control the base levels of those survey grids which overlapped the AWAGS data (Milligan et al., 2009). As the second edition was used as a base grid for the Gridmerge operation the new Third Edition is essentially levelled to AWAGS. Filtering: Potassium, uranium, thorium and dose rate grids are available in both filtered and unfiltered versions. The low-pass filtering was achieved by applying a 7-point, degree-3 Savitzky-Golay filter (Savitzky and Golay, 1964) to each of the original survey grids prior to grid merging.";
        knownLayer.setDescription(description);
        knownLayer.setGroup(GROUP);

        return knownLayer;
    }

    @Bean(name = "knownTypeRadiometrics2015FilteredPpmU")
    @Order(GROUP_ORDER + 16)
    KnownLayer knownTypeRadiometrics2015FilteredPpmU() {


        String id = "Radiometrics2015FilteredPpmU";

        KnownLayerSelector selector = new WMSSelector("Radiometrics2015_filtered_ppmU");
        KnownLayer knownLayer = new KnownLayer(id, selector);


        String name = "Radiometrics Filtered U ppm Image - 2015";
        knownLayer.setName(name);

        String description = "Low-pass filtered U element concentration (ppm eU). The Radiometric Map of Australia dataset comprises gridsof potassium (K), uranium (U) and thorium (Th) element concentrations, and derivatives of these grids. This third edition was derived by seamlessly merging 45 new survey grids with the Second Edition Radiometric Map of Australia (Minty et al., 2010). Details of the specifications of individual airborne surveys can be found in the 2014 Edition of the Index of Airborne Geophysical Surveys (Percival, 2014). Matching of the grids in the database was achieved using a program called Gridmerge, originally developed within Geoscience Australia and has now been commercialised. This program was used to merge 45 new surveys to the Second Edition Radiometric Map of Australia. The second edition merged over 550 individual grids to create the compilation (Minty et al., 2009), and the Australia-wide Airborne Geophysical Survey (AWAGS) airborne radiometric data was used to control the base levels of those survey grids which overlapped the AWAGS data (Milligan et al., 2009). As the second edition was used as a base grid for the Gridmerge operation the new Third Edition is essentially levelled to AWAGS. Filtering: Potassium, uranium, thorium and dose rate grids are available in both filtered and unfiltered versions. The low-pass filtering was achieved by applying a 7-point, degree-3 Savitzky-Golay filter (Savitzky and Golay, 1964) to each of the original survey grids prior to grid merging.";
        knownLayer.setDescription(description);
        knownLayer.setGroup(GROUP);

        return knownLayer;
    }

    @Bean(name = "knownTypeRadiometrics2015FilteredThKRatio")
    @Order(GROUP_ORDER + 17)
    KnownLayer knownTypeRadiometrics2015FilteredThKRatio() {


        String id = "Radiometrics2015FilteredThKRatio";

        KnownLayerSelector selector = new WMSSelector("Radiometrics_ThK_ratio_filtered_2015");
        KnownLayer knownLayer = new KnownLayer(id, selector);


        String name = "Radiometrics Th/K Ratio Image - 2015";
        knownLayer.setName(name);

        String description = "Ratio of Th over K derived from the filtered Th and K grids. The Radiometric Map of Australia dataset comprises gridsof potassium (K), uranium (U) and thorium (Th) element concentrations, and derivatives of these grids. This third edition was derived by seamlessly merging 45 new survey grids with the Second Edition Radiometric Map of Australia (Minty et al., 2010). Details of the specifications of individual airborne surveys can be found in the 2014 Edition of the Index of Airborne Geophysical Surveys (Percival, 2014). Matching of the grids in the database was achieved using a program called Gridmerge, originally developed within Geoscience Australia and has now been commercialised. This program was used to merge 45 new surveys to the Second Edition Radiometric Map of Australia. The second edition merged over 550 individual grids to create the compilation (Minty et al., 2009), and the Australia-wide Airborne Geophysical Survey (AWAGS) airborne radiometric data was used to control the base levels of those survey grids which overlapped the AWAGS data (Milligan et al., 2009). As the second edition was used as a base grid for the Gridmerge operation the new Third Edition is essentially levelled to AWAGS. Filtering: Potassium, uranium, thorium and dose rate grids are available in both filtered and unfiltered versions. The low-pass filtering was achieved by applying a 7-point, degree-3 Savitzky-Golay filter (Savitzky and Golay, 1964) to each of the original survey grids prior to grid merging.";
        knownLayer.setDescription(description);
        knownLayer.setGroup(GROUP);

        return knownLayer;
    }

    @Bean(name = "knownTypeRadiometrics2015FilteredUKRatio")
    @Order(GROUP_ORDER + 18)
    KnownLayer knownTypeRadiometrics2015FilteredUKRatio() {


        String id = "Radiometrics2015FilteredUKRatio";

        KnownLayerSelector selector = new WMSSelector("Radiometrics_UK_ratio_filtered_2015");
        KnownLayer knownLayer = new KnownLayer(id, selector);


        String name = "Radiometrics U/K Ratio Image - 2015";
        knownLayer.setName(name);

        String description = "Ratio of U over K derived from the filtered U and K grids. The Radiometric Map of Australia dataset comprises grids of potassium (K), uranium (U) and thorium (Th) element concentrations, and derivatives of these grids. This third edition was derived by seamlessly merging 45 new survey grids with the Second Edition Radiometric Map of Australia (Minty et al., 2010). Details of the specifications of individual airborne surveys can be found in the 2014 Edition of the Index of Airborne Geophysical Surveys (Percival, 2014). Matching of the grids in the database was achieved using a program called Gridmerge, originally developed within Geoscience Australia and has now been commercialised. This program was used to merge 45 new surveys to the Second Edition Radiometric Map of Australia. The second edition merged over 550 individual grids to create the compilation (Minty et al., 2009), and the Australia-wide Airborne Geophysical Survey (AWAGS) airborne radiometric data was used to control the base levels of those survey grids which overlapped the AWAGS data (Milligan et al., 2009). As the second edition was used as a base grid for the Gridmerge operation the new Third Edition is essentially levelled to AWAGS. Filtering: Potassium, uranium, thorium and dose rate grids are available in both filtered and unfiltered versions. The low-pass filtering was achieved by applying a 7-point, degree-3 Savitzky-Golay filter (Savitzky and Golay, 1964) to each of the original survey grids prior to grid merging.";
        knownLayer.setDescription(description);
        knownLayer.setGroup(GROUP);

        return knownLayer;
    }

    @Bean(name = "knownTypeRadiometrics2015FilteredUThRatio")
    @Order(GROUP_ORDER + 19)
    KnownLayer knownTypeRadiometrics2015FilteredUThRatio() {


        String id = "Radiometrics2015FilteredUthRatio";

        KnownLayerSelector selector = new WMSSelector("Radiometrics_UTh_ratio_filtered_2015");
        KnownLayer knownLayer = new KnownLayer(id, selector);


        String name = "Radiometrics U/Th Ratio Image - 2015";
        knownLayer.setName(name);

        String description = "Ratio of U over Th derived from the filtered U and Th grids. The Radiometric Map of Australia dataset comprises grids of potassium (K), uranium (U) and thorium (Th) element concentrations, and derivatives of these grids. This third edition was derived by seamlessly merging 45 new survey grids with the Second Edition Radiometric Map of Australia (Minty et al., 2010). Details of the specifications of individual airborne surveys can be found in the 2014 Edition of the Index of Airborne Geophysical Surveys (Percival, 2014). Matching of the grids in the database was achieved using a program called Gridmerge, originally developed within Geoscience Australia and has now been commercialised. This program was used to merge 45 new surveys to the Second Edition Radiometric Map of Australia. The second edition merged over 550 individual grids to create the compilation (Minty et al., 2009), and the Australia-wide Airborne Geophysical Survey (AWAGS) airborne radiometric data was used to control the base levels of those survey grids which overlapped the AWAGS data (Milligan et al., 2009). As the second edition was used as a base grid for the Gridmerge operation the new Third Edition is essentially levelled to AWAGS. Filtering: Potassium, uranium, thorium and dose rate grids are available in both filtered and unfiltered versions. The low-pass filtering was achieved by applying a 7-point, degree-3 Savitzky-Golay filter (Savitzky and Golay, 1964) to each of the original survey grids prior to grid merging.";
        knownLayer.setDescription(description);
        knownLayer.setGroup(GROUP);

        return knownLayer;
    }

    @Bean(name = "knownTypeRadiometrics2015FilteredU2ThRatio")
    @Order(GROUP_ORDER + 20)
    KnownLayer knownTypeRadiometrics2015FilteredU2ThRatio() {


        String id = "Radiometrics2015FilteredU2thRatio";

        KnownLayerSelector selector = new WMSSelector("Radiometrics_U2Th_ratio_filtered_2015");
        KnownLayer knownLayer = new KnownLayer(id, selector);


        String name = "Radiometrics U2/Th Ratio Image - 2015";
        knownLayer.setName(name);

        String description = "Ratio of U-squared over Th derived from the filtered U and Th grids. The Radiometric Map of Australia dataset comprises grids of potassium (K), uranium (U) and thorium (Th) element concentrations, and derivatives of these grids. This third edition was derived by seamlessly merging 45 new survey grids with the Second Edition Radiometric Map of Australia (Minty et al., 2010). Details of the specifications of individual airborne surveys can be found in the 2014 Edition of the Index of Airborne Geophysical Surveys (Percival, 2014). Matching of the grids in the database was achieved using a program called Gridmerge, originally developed within Geoscience Australia and has now been commercialised. This program was used to merge 45 new surveys to the Second Edition Radiometric Map of Australia. The second edition merged over 550 individual grids to create the compilation (Minty et al., 2009), and the Australia-wide Airborne Geophysical Survey (AWAGS) airborne radiometric data was used to control the base levels of those survey grids which overlapped the AWAGS data (Milligan et al., 2009). As the second edition was used as a base grid for the Gridmerge operation the new Third Edition is essentially levelled to AWAGS. Filtering: Potassium, uranium, thorium and dose rate grids are available in both filtered and unfiltered versions. The low-pass filtering was achieved by applying a 7-point, degree-3 Savitzky-Golay filter (Savitzky and Golay, 1964) to each of the original survey grids prior to grid merging.";
        knownLayer.setDescription(description);
        knownLayer.setGroup(GROUP);

        return knownLayer;
    }

}
