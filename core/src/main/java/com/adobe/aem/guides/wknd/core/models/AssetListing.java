package com.adobe.aem.guides.wknd.core.models;

import com.adobe.aem.guides.wknd.core.dtos.AssetItem;

import java.util.List;
import java.util.Map;

public interface AssetListing {

    Map<String, List<AssetItem>> getAssets();

}