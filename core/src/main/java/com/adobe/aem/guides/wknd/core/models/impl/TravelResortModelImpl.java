package com.adobe.aem.guides.wknd.core.models.impl;

import com.adobe.aem.guides.wknd.core.models.TravelResortCF;
import com.adobe.aem.guides.wknd.core.models.TravelResortModel;
import com.adobe.aem.guides.wknd.core.util.Constants;
import com.day.cq.search.PredicateGroup;
import com.day.cq.search.Query;
import com.day.cq.search.QueryBuilder;
import com.day.cq.search.result.Hit;
import com.day.cq.search.result.SearchResult;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import javax.annotation.PostConstruct;
import javax.jcr.Session;
import org.apache.sling.models.annotations.injectorspecific.ChildResource;
import org.apache.sling.models.annotations.injectorspecific.OSGiService;
import org.apache.sling.models.annotations.injectorspecific.Self;
import org.apache.sling.models.annotations.injectorspecific.SlingObject;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

import static com.adobe.aem.guides.wknd.core.util.Constants.*;
import static com.adobe.cq.wcm.core.components.commons.editor.dialog.childreneditor.Item.LOG;

@Model(adaptables = SlingHttpServletRequest.class,
        adapters = TravelResortModel.class,
        resourceType = Constants.TRAVEL_RESORT_COMPONENT,
        defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)

public class TravelResortModelImpl implements TravelResortModel {

    @Self
    private SlingHttpServletRequest request;

    @SlingObject
    private ResourceResolver resolver;

    @OSGiService
    private QueryBuilder queryBuilder;

    @ValueMapValue
    private String variation;

    @ValueMapValue
    private int count;

    @ChildResource(name = "resortList")
    private List<Resource> resortList;

    private final List<TravelResortCF> resorts = new ArrayList<>();

    @PostConstruct
    protected void init() {
        if (MANUAL.equals(variation)) {
            handleManual();
        } else if (DYNAMIC.equals(variation)) {
            handleDynamic();
        }
    }

    private void handleManual() {
        if (resortList != null) {
            for (Resource item : resortList) {
                String path = item.getValueMap().get(RESORT_PATH, String.class);
                if (path != null) {
                    Resource cfRes = resolver.getResource(path);
                    if (cfRes != null) {
                        TravelResortCF model = cfRes.adaptTo(TravelResortCF.class);
                        if (model != null) {
                            resorts.add(model);
                        }
                    }
                }
            }
        }
    }

    private void handleDynamic() {

        Map<String, String> map = new HashMap<>();
        map.put(PATH, TRAVEL_CF);
        map.put(TYPE, DAM_ASSET);
        map.put(PROPERTY, PROPERTY_PATH);
        map.put(PROPERTY_VALUE, PROPERTY_VALUE_PATH);
        map.put(ORDER_BY, LAST_MODIFIED);
        map.put(ORDER_BY_SORT, DESCENDING_ORDER);
        map.put(LIMIT, String.valueOf(count > 0 ? count : 3));

        Session session = resolver.adaptTo(Session.class);
        Query query = queryBuilder.createQuery(PredicateGroup.create(map), session);
        SearchResult result = query.getResult();

        for (Hit hit : result.getHits()) {
            try {
                Resource res = hit.getResource();
                TravelResortCF model = res.adaptTo(TravelResortCF.class);
                if (model != null) {
                    resorts.add(model);
                }
            } catch (Exception e) {
                LOG.error("error {}",e.getMessage());
            }
        }
    }

    @Override
    public List<TravelResortCF> getResorts() {
        return resorts;
    }
}
