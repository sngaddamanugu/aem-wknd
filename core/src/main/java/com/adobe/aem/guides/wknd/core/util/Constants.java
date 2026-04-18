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

    // Image
    public static final String FILE_REFERENCE = "fileReference";

    // Buttons
    public static final String PRIMARY_BUTTON = "primaryButton";
    public static final String SECONDARY_BUTTON = "secondaryButton";

    // Image Listing Component
    public static final String IMAGE_LISTING ="wknd/components/imagelisting";
    public static final String ASSET_SERVICE ="my-subservice";

    //dam constants
    public static final String DC_TITLE = "dc:title";
    public static final String DC_DESCRIPTION = "dc:description";
    public static final String DC_CREATOR = "dc:creator";
    public static final String DC_CREATED = "jcr:created";
    public static final String DC_FORMAT = "dc:format";
    public static final String CONTENT_METADATA = "jcr:content/metadata";
    public static final String DC_SIZE = "dam:size";

    //Workflows
    public static final String DAM_BASE_PATH ="/content/dam";
    public static final String WKND_BRAND_TAG ="brand:wknd-microsite";
    public static final String METADATA_PATH = "/jcr:content/metadata";
    public static final String CQ_TAGS = "cq:tags";

}
