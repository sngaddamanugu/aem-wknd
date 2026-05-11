package com.adobe.aem.guides.wknd.core.util;
import com.adobe.granite.workflow.metadata.MetaDataMap;
import org.apache.commons.lang3.StringUtils;
import java.util.Arrays;
import java.util.Collections;
import java.util.Map;
import java.util.stream.Collectors;

import static com.adobe.aem.guides.wknd.core.util.Constants.*;

import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ValueMap;

import static com.adobe.aem.guides.wknd.core.util.Constants.CONTENT_METADATA;
import static com.adobe.aem.guides.wknd.core.util.Constants.DC_SIZE;

public class AssetUtil {

    private AssetUtil() {
    }

    public static Map<String, String> buildArguments(MetaDataMap args) {
        if (args != null && args.containsKey(PROCESS_ARGS)) {
            String argumentsString = args.get(PROCESS_ARGS, String.class);
            if (StringUtils.isNotBlank(argumentsString)) {
                String[] arguments = StringUtils.split(argumentsString, COMMA);
                return Arrays.stream(arguments)
                        .map(String::trim)
                        .map(s -> s.split(EQUAL_TO))
                        .filter(arr -> arr.length == 2)
                        .collect(Collectors.toMap(arr -> arr[0], arr -> arr[1]));
            }
        }
        return Collections.emptyMap();
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
