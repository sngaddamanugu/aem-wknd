package com.adobe.aem.guides.wknd.core.models.impl;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import com.adobe.aem.guides.wknd.core.models.PromoBannerModel;
import com.adobe.aem.guides.wknd.core.util.Constants;
import org.apache.sling.models.factory.ModelFactory;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import io.wcm.testing.mock.aem.junit5.AemContext;
import io.wcm.testing.mock.aem.junit5.AemContextExtension;

@ExtendWith(AemContextExtension.class)
class PromoBannerModelImplTest {

    private final AemContext context = new AemContext();

    @Test
    void getBannerImage_usesComponentFileReference_whenPresent() {

        ModelFactory modelFactory = mock(ModelFactory.class);
        context.registerService(ModelFactory.class, modelFactory);
        var page = context.create().page("/content/wknd/us/en/test");
        var component = context.create().resource(page.getContentResource(),
                "root/promobanner", "sling:resourceType", Constants.PROMO_BANNER_RESOURCE_TYPE,
                Constants.FILE_REFERENCE, "/content/dam/wknd/banner.jpg", "heading", "Hello", "subHeading", "Sub", "description",
                "<p>Desc</p>", "backgroundType", "image", "backgroundColor", "#ffffff");
        context.create().resource(component, Constants.PRIMARY_BUTTON, "sling:resourceType", "wknd/components/button");
        context.create().resource(component, Constants.SECONDARY_BUTTON, "sling:resourceType", "wknd/components/button");
        context.currentResource(component);
        PromoBannerModel model = context.request().adaptTo(PromoBannerModel.class);
        assertNotNull(model);
        assertEquals("/content/dam/wknd/banner.jpg", model.getBannerImage());
        assertEquals("Hello", model.getHeading());
        assertEquals("Sub", model.getSubHeading());
        assertEquals("<p>Desc</p>", model.getDescription());
        assertEquals("image", model.getBackgroundType());
        assertEquals("#ffffff", model.getBackgroundColor());
        assertNotNull(model.getPrimaryButton());
        assertNotNull(model.getSecondaryButton());
        verifyNoInteractions(modelFactory);
    }

    @Test
    void getBannerImage_isNullOrEmpty_whenNoComponentImageAndNoFeaturedImage() {
        ModelFactory modelFactory = mock(ModelFactory.class);
        context.registerService(ModelFactory.class, modelFactory);
        var page = context.create().page("/content/wknd/us/en/test");
        var component = context.create().resource(page.getContentResource(),
                "root/promobanner", "sling:resourceType", Constants.PROMO_BANNER_RESOURCE_TYPE);
        context.currentResource(component);
        PromoBannerModel model = context.request().adaptTo(PromoBannerModel.class);
        assertNotNull(model);
        assertTrue(model.getBannerImage() == null || model.getBannerImage().isEmpty());
        verifyNoInteractions(modelFactory);
    }
}
