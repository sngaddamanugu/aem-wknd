package com.adobe.aem.guides.wknd.core.services;
import com.adobe.aem.guides.wknd.core.dtos.AssetItem;

import java.util.List;
import java.util.Map;

public interface AssetListingService {

    Map<String, List<AssetItem>> getAssets(String rootPath);

}
