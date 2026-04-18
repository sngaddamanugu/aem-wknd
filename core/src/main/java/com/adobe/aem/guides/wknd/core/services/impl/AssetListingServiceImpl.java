package com.adobe.aem.guides.wknd.core.services.impl;

import com.adobe.aem.guides.wknd.core.configs.AssetListingConfig;
import com.adobe.aem.guides.wknd.core.dtos.AssetItem;
import com.adobe.aem.guides.wknd.core.services.AssetListingService;
import com.adobe.aem.guides.wknd.core.util.AssetUtil;

import com.day.cq.search.PredicateGroup;
import com.day.cq.search.Query;
import com.day.cq.search.QueryBuilder;
import com.day.cq.search.result.Hit;
import com.day.cq.search.result.SearchResult;
import org.apache.sling.api.resource.*;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Modified;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.metatype.annotations.Designate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.jcr.RepositoryException;
import javax.jcr.Session;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.adobe.aem.guides.wknd.core.util.Constants.*;

@Component(service = AssetListingService.class, immediate = true)
@Designate(ocd = AssetListingConfig.class)
public class AssetListingServiceImpl implements AssetListingService {

    private static final Logger LOG = LoggerFactory.getLogger(AssetListingServiceImpl.class);

    @Reference
    private QueryBuilder queryBuilder;

    private String assetListRootPath;

    @Reference
    private ResourceResolverFactory factory;

    @Activate
    @Modified
    protected void activate(AssetListingConfig config) {
        this.assetListRootPath = config.assetListRootPath();
    }

    @Override
    public Map<String, List<AssetItem>> getAssets(String rootPath) {

        Map<String, Object> param = new HashMap<>();

        param.put(ResourceResolverFactory.SUBSERVICE, ASSET_SERVICE);
        Map<String, List<AssetItem>> assetMap = new HashMap<>();

        try (ResourceResolver resolver = factory.getServiceResourceResolver(param)) {
            //Fallback logic
            if (rootPath == null || rootPath.trim().isEmpty()) {
                rootPath = assetListRootPath;
                LOG.info("Using OSGi default path: {}", rootPath);
            } else {
                LOG.info("Using dialog path: {}", rootPath);
            }
            Map<String, String> map = new HashMap<>();
            map.put("path", rootPath);
            map.put("type", "dam:Asset");
            map.put("p.limit", "-1");

            Session session = resolver.adaptTo(Session.class);
            Query query = queryBuilder.createQuery(PredicateGroup.create(map), session);

            SearchResult result = query.getResult();
            for (Hit hit : result.getHits()) {
                Resource assetResource = hit.getResource();
                if (assetResource != null) {
                    AssetItem item = buildAssetItem(assetResource, resolver);
                    if (item != null) {
                        String type = item.getType();
                        assetMap.computeIfAbsent(type, k -> new ArrayList<>()).add(item);
                    }
                }
            }
        }
        catch (RepositoryException | LoginException e) {
            LOG.error("get asset listing service error {}",e.getMessage());
        }
        return assetMap;
    }

    private AssetItem buildAssetItem(Resource assetResource, ResourceResolver resolver) {

        ValueMap metadataMap = AssetUtil.getAssetMetadataMap(assetResource.getPath(), resolver);
        if (metadataMap == null) return null;

        String title = AssetUtil.getValue(metadataMap, DC_TITLE, assetResource.getName());
        String description = AssetUtil.getValue(metadataMap, DC_DESCRIPTION, "");
        String creator = AssetUtil.getValue(metadataMap, DC_CREATOR, "");
        String size = AssetUtil.getAssetSize(metadataMap);

        ValueMap assetMap = assetResource.getValueMap();
        String created = assetMap.get(DC_CREATED, "");

        String fileFormat = AssetUtil.getValue(metadataMap,DC_FORMAT, "");
        fileFormat= fileFormat.split("/")[1];

        AssetItem item = new AssetItem();
        item.setTitle(title);
        item.setPath(assetResource.getPath());
        item.setDescription(description);
        item.setCreator(creator);
        item.setCreated(created);
        item.setType(fileFormat);
        item.setSize(size);
        return item;
    }
}
