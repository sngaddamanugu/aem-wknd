package com.adobe.aem.guides.wknd.core.util;

public final class Constants {

    public static final String COUNTRY_SERVLET_RESOURCE_TYPE = "wknd/datasource/countries";
    public static final String GET_METHOD = "GET";
    public static final String TEXT = "text";
    public static final String VALUE = "value";
    public static final String FEATURED_IMAGE ="cq:featuredimage";
    public static final String PROMO_BANNER_RESOURCE_TYPE = "wknd/components/promobanner";
    public static final String TRIP_INFO_ACCORDION = "wknd/components/tripinfoaccordion";
    public static final String DESTINATION_TYPE = "destinationType";

    private Constants() {
        // This constructor is intentionally empty. Nothing special is needed here.
    }

    // Background
    public static final String BACKGROUND_TYPE = "backgroundType";
    public static final String BACKGROUND_COLOR = "backgroundColor";
    public static final String TRAVEL_RESORT_COMPONENT ="wknd/components/travelresort";

    public static final String DATE_FORMAT= "yyyy-MM-dd'T'HH:mm:ss.SSSXXX";
    public static final String FORMAT ="dd-MM-yy";
    public static final String CAL_FORMAT ="dd MMM yyyy";
    public static final String MANUAL ="manual";
    public static final String DYNAMIC ="dynamic";
    public static final String RESORT_PATH ="resortPath";

    // Image
    public static final String FILE_REFERENCE = "fileReference";
    public static final String PATH ="path";
    public static final String TRAVEL_CF= "/content/dam/wknd-shared/en/travel";

    // Buttons
    public static final String PRIMARY_BUTTON = "primaryButton";
    public static final String SECONDARY_BUTTON = "secondaryButton";
    public static final String TYPE ="type";
    public static final String DAM_ASSET = "dam:Asset";

    // Image Listing Component
    public static final String IMAGE_LISTING ="wknd/components/imagelisting";
    public static final String ASSET_SERVICE ="asset-service";
    public static final String ORDER_BY = "orderby";
    public static final String DESCENDING_ORDER ="desc";

    //dam constants
    public static final String DC_TITLE = "dc:title";
    public static final String DC_DESCRIPTION = "dc:description";
    public static final String DC_CREATOR = "dc:creator";
    public static final String DC_CREATED = "jcr:created";
    public static final String DC_FORMAT = "dc:format";
    public static final String CONTENT_METADATA = "jcr:content/metadata";
    public static final String DC_SIZE = "dam:size";
    public static final String ORDER_BY_SORT = "orderby.sort";
    public static final String LAST_MODIFIED = "@jcr:content/jcr:lastModified";

    //Workflows
    public static final String DAM_BASE_PATH ="/content/dam";
    public static final String WKND_BRAND_TAG ="brand:wknd-microsite";
    public static final String METADATA_PATH = "/jcr:content/metadata";
    public static final String CQ_TAGS = "cq:tags";
    public static final String PROPERTY ="property";
    public static final String PROPERTY_PATH ="jcr:content/data/cq:model";

    public static final String PROPERTY_VALUE ="property.value";
    public static final String PROPERTY_VALUE_PATH ="/conf/global/settings/dam/cfm/models/travel-resort";

    public static final String LIMIT ="p.limit";
    public static final String PROCESS_ARGS = "PROCESS_ARGS";
    public static final String COMMA = ",";
    public static final String EQUAL_TO = "=";
    public static final String JCR_CONTENT = "/jcr:content";

}
