package com.adobe.aem.guides.wknd.core.util;

import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ValueMap;

import static com.adobe.aem.guides.wknd.core.util.Constants.CONTENT_METADATA;
import static com.adobe.aem.guides.wknd.core.util.Constants.DC_SIZE;

public class AssetUtil {

    private AssetUtil() {
    }

    public static ValueMap getAssetMetadataMap(String assetPath, ResourceResolver resolver) {

        if (resolver != null && assetPath != null) {
            Resource assetResource = resolver.getResource(assetPath);
            if (assetResource != null) {
                Resource metadata = assetResource.getChild(CONTENT_METADATA);
                if (metadata != null) {
                    return metadata.getValueMap();
                }
            }
        }
        return null;
    }

    public static String getValue(ValueMap vm, String key, String defaultVal) {
        if (vm != null) {
            String value = vm.get(key, String.class);
            if (value != null && !value.isEmpty()) {
                return value;
            }
        }
        return defaultVal;
    }

    public static String getAssetSize(ValueMap metadataMap) {
        if (metadataMap != null) {
            Long size = metadataMap.get(DC_SIZE, Long.class);
            if (size != null && size > 0) {
                double kb = size / 1024.0;
                double mb = kb / 1024.0;
                if (mb >= 1) {
                    return String.format("%.2f MB", mb);
                } else {
                    return String.format("%.2f KB", kb);
                }
            }
        }
        return "";
    }
}
