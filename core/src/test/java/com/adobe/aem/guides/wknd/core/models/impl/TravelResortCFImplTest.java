package com.adobe.aem.guides.wknd.core.models.impl;
import com.day.cq.tagging.Tag;
import com.day.cq.tagging.TagManager;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TravelResortCFImplTest {

    @Mock
    private Resource resource;

    @Mock
    private ResourceResolver resolver;

    @Mock
    private TagManager tagManager;

    @Mock
    private Tag tag;

    private TravelResortCFImpl createModel(String[] amenities) {
        lenient().when(resource.getResourceResolver()).thenReturn(resolver);
        lenient().when(resolver.adaptTo(TagManager.class)).thenReturn(tagManager);
        return new TravelResortCFImpl(
                resource, "Resort1", "Bangalore",
                "Karnataka", "India", new String[]{"img1", "img2"}, 100L, 200L,
                "INR", amenities, "2024-01-01T00:00:00.000Z", "2024-02-01T00:00:00.000Z");
    }

    @Test
    void testBasicGetters() {
        TravelResortCFImpl model = createModel(new String[]{"tag1"});
        assertEquals("Resort1", model.getResortName());
        assertEquals("Bangalore", model.getCity());
        assertEquals("Karnataka", model.getState());
        assertEquals("India", model.getCountry());
        assertEquals(100L, model.getMin());
        assertEquals(200L, model.getMax());
        assertEquals("INR", model.getCurrency());
        assertEquals(2, model.getHeroImageGallery().length);
    }

    @Test
    void testAmenities_Null() {
        TravelResortCFImpl model = createModel(null);
        String[] result = model.getAmenities();
        assertNotNull(result);
        assertEquals(0, result.length);
    }

    @Test
    void testAmenities_TagManagerNull() {
        when(resource.getResourceResolver()).thenReturn(resolver);
        when(resolver.adaptTo(TagManager.class)).thenReturn(null);
        TravelResortCFImpl model = new TravelResortCFImpl(
                resource, "Resort1", "City", "State", "Country",
                null, 100L, 200L, "INR", new String[]{"tag1"},
                "2024-01-01T00:00:00.000Z", "2024-02-01T00:00:00.000Z");
        String[] result = model.getAmenities();
        assertEquals(0, result.length);
    }

    @Test
    void testAmenities_WithTags() {
        when(tagManager.resolve("tag1")).thenReturn(tag);
        when(tag.getTitle()).thenReturn("Spa");
        TravelResortCFImpl model = createModel(new String[]{"tag1"});
        String[] result = model.getAmenities();
        assertEquals(1, result.length);
        assertEquals("Spa", result[0]);
    }

    @Test
    void testDateMethods() {
        TravelResortCFImpl model = createModel(new String[]{"tag1"});
        assertNotNull(model.getStartDate());
        assertNotNull(model.getEndDate());
    }
}
