package com.adobe.aem.guides.wknd.core.models.impl;
import com.adobe.aem.guides.wknd.core.models.TripInfoAccordion;
import com.adobe.aem.guides.wknd.core.util.Constants;
import com.day.cq.wcm.api.Page;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.ChildResource;
import org.apache.sling.models.annotations.injectorspecific.ScriptVariable;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;
import java.util.Collections;
import java.util.List;
import static com.adobe.aem.guides.wknd.core.util.Constants.DESTINATION_TYPE;

@Model(adaptables = {SlingHttpServletRequest.class,Resource.class},
        adapters = TripInfoAccordion.class,
        resourceType = Constants.TRIP_INFO_ACCORDION,
        defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)

public class TripInfoAccordionModelImpl implements TripInfoAccordion {

    @ValueMapValue
    private String contentType;

    @ChildResource(name = "faqList")
    private List<FaqItem> faqList;

    @ChildResource(name = "sectionsList")
    private List<Section> sectionsList;

    @ScriptVariable
    private Page currentPage;

    @Override
    public String getContentType() {
        return contentType;
    }

    @Override
    public List<FaqItem> getFaqList() {
        return faqList;
    }

    @Override
    public List<Section> getSectionsList() {
        return sectionsList;
    }

    public String getDestinationType() {
        return currentPage != null
                ? currentPage.getProperties().get(DESTINATION_TYPE, String.class)
                : null;
    }

    @Model(adaptables = Resource.class, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
    public static class FaqItem {

        @ValueMapValue
        private String question;

        @ValueMapValue
        private String answer;

        @ValueMapValue
        private String icon;

        @ValueMapValue
        private boolean defaultExpandedValue;

        public String getQuestion() {
            return question;
        }

        public String getAnswer() {
            return answer;
        }

        public String getIcon() {
            return icon;
        }

        public boolean isDefaultExpanded() {
            return defaultExpandedValue;
        }

    }

    @Model(adaptables = Resource.class, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
    public static class Section {

        @ValueMapValue
        private String sectionTitle;

        @ValueMapValue
        private String sectionSummary;

        @ValueMapValue
        private boolean defaultExpanded;

        @ChildResource(name = "sectionItemList")
        private List<SectionItem> sectionItemList;

        public String getSectionTitle() {
            return sectionTitle;
        }

        public String getSectionSummary() {
            return sectionSummary;
        }

        public boolean isDefaultExpanded() {
            return defaultExpanded;
        }

        public List<SectionItem> getSectionItemList() {
            return sectionItemList == null ? Collections.emptyList() :
                    Collections.unmodifiableList(sectionItemList);
        }

    }

    @Model(adaptables = Resource.class, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
    public static class SectionItem {

        @ValueMapValue
        private String itemTitle;

        @ValueMapValue
        private String itemDetail;

        @ValueMapValue
        private String icon;

        @ValueMapValue
        private String link;

        public String getItemTitle() {
            return itemTitle;
        }

        public String getItemDetail() {
            return itemDetail;
        }

        public String getIcon() {
            return icon;
        }

        public String getLink() {
            return link;
        }

    }

}
