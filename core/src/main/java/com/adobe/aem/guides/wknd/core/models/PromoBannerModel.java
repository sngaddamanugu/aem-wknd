package com.adobe.aem.guides.wknd.core.models;

import org.apache.sling.api.resource.Resource;

public interface PromoBannerModel {

    String getHeading();

    String getSubHeading();

    String getDescription();

    String getBackgroundType();

    String getBackgroundColor();

    Resource getPrimaryButton();

    Resource getSecondaryButton();

    String getBannerImage();
}
