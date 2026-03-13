package com.adobe.aem.guides.wknd.core.models.impl;
import com.adobe.aem.guides.wknd.core.models.TripInfoAccordion;
import com.adobe.aem.guides.wknd.core.util.Constants;
import com.day.cq.wcm.api.Page;
import io.wcm.testing.mock.aem.junit5.AemContext;
import io.wcm.testing.mock.aem.junit5.AemContextExtension;
import org.apache.sling.api.resource.ModifiableValueMap;
import org.apache.sling.api.resource.Resource;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import java.util.List;
import java.util.Objects;

import static com.adobe.aem.guides.wknd.core.util.Constants.DESTINATION_TYPE;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(AemContextExtension.class)
class TripInfoAccordionModelImplTest {

    final AemContext context = new AemContext();
    private Resource componentResource;

    @BeforeEach
    void setUp() {

        context.addModelsForClasses(
                TripInfoAccordionModelImpl.class,
                TripInfoAccordionModelImpl.FaqItem.class,
                TripInfoAccordionModelImpl.Section.class,
                TripInfoAccordionModelImpl.SectionItem.class);
        Page page = context.create().page("/content/wknd/us/en/trips/switzerland");
        Objects.requireNonNull(page.getContentResource().adaptTo(ModifiableValueMap.class))
                .put(DESTINATION_TYPE, "international");
        componentResource = context.create().resource(
                page.getContentResource().getPath() + "/tripInfoAccordion",
                "sling:resourceType", Constants.TRIP_INFO_ACCORDION, "contentType", "faq");

        context.create().resource(componentResource.getPath() + "/faqList/faq1",
                "question", "What should I pack?",
                "answer", "<p>Layers, rain jacket, and comfortable shoes.</p>",
                "icon", "info",
                "defaultExpandedValue", true);

        context.create().resource(componentResource.getPath() + "/sectionsList/section1",
                "sectionTitle", "Days 1-4: Zurich & Lucerne",
                "sectionSummary", "<p>Highlights and key logistics.</p>",
                "defaultExpanded", false);

        context.create().resource(componentResource.getPath() + "/sectionsList/section1/sectionItemList/item1",
                "itemTitle", "Chapel Bridge",
                "itemDetail", "Walk the historic bridge.",
                "icon", "walk",
                "link", "/content/wknd/us/en.html");

        context.currentPage(page);
        context.request().setResource(componentResource);
    }

    @Test
    void testAdaptFromRequest_PopulatesFieldsAndChildren() {
        TripInfoAccordion model = context.request().adaptTo(TripInfoAccordion.class);
        assertNotNull(model);
        assertEquals("faq", model.getContentType());
        List<TripInfoAccordionModelImpl.FaqItem> faqs = model.getFaqList();
        assertNotNull(faqs);
        assertEquals(1, faqs.size());
        assertEquals("What should I pack?", faqs.getFirst().getQuestion());
        assertEquals("<p>Layers, rain jacket, and comfortable shoes.</p>", faqs.getFirst().getAnswer());
        assertEquals("info", faqs.getFirst().getIcon());
        assertTrue(faqs.getFirst().isDefaultExpanded());

        List<TripInfoAccordionModelImpl.Section> sections = model.getSectionsList();
        assertNotNull(sections);
        assertEquals(1, sections.size());
        assertEquals("Days 1-4: Zurich & Lucerne", sections.getFirst().getSectionTitle());
        assertEquals("<p>Highlights and key logistics.</p>", sections.getFirst().getSectionSummary());
        assertFalse(sections.getFirst().isDefaultExpanded());

        List<TripInfoAccordionModelImpl.SectionItem> items = sections.getFirst().getSectionItemList();
        assertEquals(1, items.size());
        assertEquals("Chapel Bridge", items.getFirst().getItemTitle());
        assertEquals("Walk the historic bridge.", items.getFirst().getItemDetail());
        assertEquals("walk", items.getFirst().getIcon());
        assertEquals("/content/wknd/us/en.html", items.getFirst().getLink());

        assertThrows(UnsupportedOperationException.class, () -> items.add(null));
    }

    @Test
    void testGetDestinationType_WhenCurrentPageAvailable() {
        TripInfoAccordionModelImpl model = context.request().adaptTo(TripInfoAccordionModelImpl.class);
        assertNotNull(model);
        assertEquals("international", model.getDestinationType());
    }

    @Test
    void testGetDestinationType_WhenAdaptedFromResource_IsNull() {
        TripInfoAccordionModelImpl model = componentResource.adaptTo(TripInfoAccordionModelImpl.class);
        assertNotNull(model);
        assertNull(model.getDestinationType());
    }

}
