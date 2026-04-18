package com.adobe.aem.guides.wknd.core.models.impl;

import javax.annotation.PostConstruct;
import com.adobe.aem.guides.wknd.core.util.Constants;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ValueMap;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.*;
import com.adobe.cq.wcm.core.components.models.Image;
import org.apache.sling.models.factory.ModelFactory;
import com.day.cq.wcm.api.Page;
import com.day.cq.wcm.api.PageManager;
import com.adobe.aem.guides.wknd.core.models.PromoBannerModel;
import static com.adobe.aem.guides.wknd.core.util.Constants.FEATURED_IMAGE;

@Model(adaptables = {SlingHttpServletRequest.class, Resource.class},
        adapters = PromoBannerModel.class,
        resourceType = Constants.PROMO_BANNER_RESOURCE_TYPE,
        defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class PromoBannerModelImpl implements PromoBannerModel {

    @Self
    private SlingHttpServletRequest request;

    @SlingObject
    private Resource resource;

    @OSGiService
    private ModelFactory modelFactory;

    // Banner Content
    @ValueMapValue
    private String heading;

    @ValueMapValue
    private String subHeading;

    @ValueMapValue
    private String description;

    // Background
    @ValueMapValue
    private String backgroundType;

    @ValueMapValue
    private String backgroundColor;

    // Image
    @ValueMapValue
    private String fileReference;

    // Buttons
    @ChildResource(name = Constants.PRIMARY_BUTTON)
    private Resource primaryButton;

    @ChildResource(name = Constants.SECONDARY_BUTTON)
    private Resource secondaryButton;

    private String bannerImage;

    @PostConstruct
    protected void init() {

        ValueMap properties = resource.getValueMap();
        bannerImage = properties.get(Constants.FILE_REFERENCE, String.class);
        if (bannerImage != null && !bannerImage.isEmpty()) {
            return;
        }
        PageManager pageManager = request.getResourceResolver().adaptTo(PageManager.class);
        if (pageManager != null) {
            Page currentPage = pageManager.getContainingPage(resource);
            if (currentPage != null) {
                Resource featuredImageRes = currentPage.getContentResource(FEATURED_IMAGE);
                if (featuredImageRes != null) {
                    Image imageModel = modelFactory.getModelFromWrappedRequest(
                            request, featuredImageRes, Image.class);
                    if (imageModel != null) {
                        bannerImage = imageModel.getSrc();
                    }
                }
            }
        }
    }

    @Override
    public String getHeading() {
        return heading;
    }

    @Override
    public String getSubHeading() {
        return subHeading;
    }

    @Override
    public String getDescription() {
        return description;
    }

    @Override
    public String getBackgroundType() {
        return backgroundType;
    }

    @Override
    public String getBackgroundColor() {
        return backgroundColor;
    }

    @Override
    public Resource getPrimaryButton() {
        return primaryButton;
    }

    @Override
    public Resource getSecondaryButton() {
        return secondaryButton;
    }

    @Override
    public String getBannerImage() {
        return bannerImage;
    }
}
