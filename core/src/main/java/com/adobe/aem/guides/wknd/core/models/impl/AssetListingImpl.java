package com.adobe.aem.guides.wknd.core.models.impl;

import com.adobe.aem.guides.wknd.core.dtos.AssetItem;
import com.adobe.aem.guides.wknd.core.models.AssetListing;
import com.adobe.aem.guides.wknd.core.services.AssetListingService;
import com.adobe.aem.guides.wknd.core.util.Constants;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.Required;
import org.apache.sling.models.annotations.injectorspecific.OSGiService;
import org.apache.sling.models.annotations.injectorspecific.Self;
import org.apache.sling.models.annotations.injectorspecific.SlingObject;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.Map;

@Model(adaptables = {SlingHttpServletRequest.class, Resource.class},
        resourceType = Constants.IMAGE_LISTING,
        adapters = AssetListing.class,
        defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)

public class AssetListingImpl implements AssetListing{

    private static final Logger LOG = LoggerFactory.getLogger(AssetListingImpl.class);

    @ValueMapValue
    private String rootPath;

    @ValueMapValue
    private String assetType;

    @Self
    @Required
    private SlingHttpServletRequest request;

    @OSGiService
    private AssetListingService assetService;

    @SlingObject
    private ResourceResolver resourceResolver;

    private Map<String, List<AssetItem>> assets;

    @PostConstruct
    protected void init() {

        LOG.info("### MODEL INIT CALLED ###");
        if (assetService != null) {
            LOG.info("Calling service with path: {}", rootPath);
            assets = assetService.getAssets(rootPath);
            LOG.info("Assets size: {}", assets != null ? assets.size() : 0);
        } else {
            LOG.info("Service/Resolver/Path missing");
        }
    }

    @Override
    public Map<String, List<AssetItem>> getAssets() {
        return assets;
    }

    public String getAssetType() {
        return assetType != null ? assetType : "all";
    }
}
