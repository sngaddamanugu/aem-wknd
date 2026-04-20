package com.adobe.aem.guides.wknd.core.workflows;

import com.adobe.granite.workflow.WorkflowException;
import com.adobe.granite.workflow.WorkflowSession;
import com.adobe.granite.workflow.exec.WorkItem;
import com.adobe.granite.workflow.exec.WorkflowProcess;
import com.adobe.granite.workflow.metadata.MetaDataMap;
import org.apache.sling.api.resource.ModifiableValueMap;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.osgi.service.component.annotations.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.adobe.aem.guides.wknd.core.util.Constants.DAM_BASE_PATH;
import static com.adobe.aem.guides.wknd.core.util.Constants.METADATA_PATH;
import static com.adobe.aem.guides.wknd.core.util.Constants.CQ_TAGS;
import static com.adobe.aem.guides.wknd.core.util.Constants.WKND_BRAND_TAG;

@Component(service = WorkflowProcess.class, property = { "process.label=Add Brand Tag to Asset" })
public class AddBrandTagProcess implements WorkflowProcess {

    private static final Logger LOG = LoggerFactory.getLogger(AddBrandTagProcess.class);

    @Override
    public void execute(WorkItem item, WorkflowSession session, MetaDataMap args) throws WorkflowException {
        String payload = item.getWorkflowData().getPayload().toString();
        try {
            ResourceResolver resolver = session.adaptTo(ResourceResolver.class);
            if (payload.startsWith(DAM_BASE_PATH)) {
                Resource metadataRes = resolver.getResource(payload + METADATA_PATH);
                if (metadataRes != null) {
                    ModifiableValueMap map = metadataRes.adaptTo(ModifiableValueMap.class);
                    String[] existingTags = map.get(CQ_TAGS, String[].class);
                    List<String> tagList = new ArrayList<>();

                    if (existingTags != null) {
                        tagList.addAll(Arrays.asList(existingTags));
                    }
                    if (!tagList.contains(WKND_BRAND_TAG)) {
                        map.put(CQ_TAGS, WKND_BRAND_TAG);
                    }
                    resolver.commit();
                }
            }
        } catch (Exception e) {
            LOG.error("Error while loading brand tag for payload:{}",payload, e);
        }
    }
}
