package com.adobe.aem.guides.wknd.core.models.impl;

import com.adobe.aem.guides.wknd.core.models.TravelResortCF;
import com.adobe.aem.guides.wknd.core.util.DateUtil;
import com.day.cq.tagging.Tag;
import com.day.cq.tagging.TagManager;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.Self;
import javax.inject.Inject;
import javax.inject.Named;
import java.util.ArrayList;
import java.util.List;
import static com.adobe.aem.guides.wknd.core.util.Constants.FORMAT;

@Model(adaptables = Resource.class,
        adapters = TravelResortCF.class,
        defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)

public class TravelResortCFImpl implements TravelResortCF {

    private final Resource resource;
    private final String resortName;
    private final String city;
    private final String state;
    private final String country;
    private final Long min;
    private final Long max;
    private final String currency;
    private final String[] amenities;
    private final String startDate;
    private final String endDate;
    private final String[] heroImageGallery;

    @Inject
    public TravelResortCFImpl(@Self Resource resource,
                              @Named("jcr:content/data/master/resortName") String resortName,
                              @Named("jcr:content/data/master/city") String city,
                              @Named("jcr:content/data/master/state") String state,
                              @Named("jcr:content/data/master/country") String country,
                              @Named("jcr:content/data/master/heroImageGallery") String[] heroImageGallery,
                              @Named("jcr:content/data/master/min") Long min,
                              @Named("jcr:content/data/master/max") Long max,
                              @Named("jcr:content/data/master/currency") String currency,
                              @Named("jcr:content/data/master/amenities") String[] amenities,
                              @Named("jcr:content/data/master/startDate") String startDate,
                              @Named("jcr:content/data/master/endDate") String endDate) {
        this.resource = resource;
        this.resortName = resortName;
        this.city = city;
        this.state = state;
        this.country = country;
        this.heroImageGallery = heroImageGallery;
        this.min = min;
        this.max = max;
        this.currency = currency;
        this.amenities = amenities;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public String getResortName() {
        return resortName;
    }

    public String getCity() {
        return city;
    }

    public String getState() {
        return state;
    }

    public String getCountry() {
        return country;
    }

    public String[] getHeroImageGallery() {
        return heroImageGallery;
    }

    public Long getMin() {
        return min;
    }

    public Long getMax() {
        return max;
    }

    public String getCurrency() {
        return currency;
    }

    @Override
    public String[] getAmenities() {
        if (amenities == null) {
            return new String[0];
        }
        List<String> titles = new ArrayList<>();
        TagManager tagManager = resource.getResourceResolver().adaptTo(TagManager.class);
        if (tagManager != null) {
            for (String tagId : amenities) {
                Tag tag = tagManager.resolve(tagId);
                if (tag != null) {
                    titles.add(tag.getTitle());
                }
            }
        }
        return titles.toArray(new String[0]);
    }

    public String getStartDate() {
        return DateUtil.getDisplayDate(startDate, FORMAT);
    }

    public String getEndDate() {
        return DateUtil.getDisplayDate(endDate, FORMAT);
    }
}
