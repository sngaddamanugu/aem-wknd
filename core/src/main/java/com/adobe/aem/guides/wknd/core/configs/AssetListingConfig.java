package com.adobe.aem.guides.wknd.core.configs;

import org.osgi.service.metatype.annotations.AttributeDefinition;
import org.osgi.service.metatype.annotations.ObjectClassDefinition;

@ObjectClassDefinition(
        name = "Asset Listing Config",
        description = "Configuration for Asset Listing Component")

public @interface AssetListingConfig {

    @AttributeDefinition(
            name = "Default Asset Root Path",
            description = "Fallback DAM path if not authored in dialog"
    )
    String assetListRootPath();
}
