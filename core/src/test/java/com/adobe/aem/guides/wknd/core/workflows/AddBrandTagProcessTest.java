package com.adobe.aem.guides.wknd.core.workflows;
import com.adobe.granite.workflow.WorkflowException;
import com.adobe.granite.workflow.exec.WorkItem;
import com.adobe.granite.workflow.exec.WorkflowData;
import com.adobe.granite.workflow.WorkflowSession;
import com.adobe.granite.workflow.metadata.MetaDataMap;
import org.apache.sling.api.resource.ModifiableValueMap;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.verifyNoInteractions;
import static com.adobe.aem.guides.wknd.core.util.Constants.CQ_TAGS;
import static com.adobe.aem.guides.wknd.core.util.Constants.METADATA_PATH;
import static com.adobe.aem.guides.wknd.core.util.Constants.WKND_BRAND_TAG;

@ExtendWith(MockitoExtension.class)
class AddBrandTagProcessTest {

    @InjectMocks
    private AddBrandTagProcess process;

    @Mock
    private WorkItem workItem;

    @Mock
    private WorkflowSession workflowSession;

    @Mock
    private WorkflowData workflowData;

    @Mock
    private MetaDataMap metaDataMap;

    @Mock
    private ResourceResolver resolver;

    @Mock
    private Resource metadataResource;

    @Mock
    private ModifiableValueMap map;

    @Test
    void testExecute_AddTag_WhenNoExistingTags() throws Exception {
        String payload = "/content/dam/test";
        when(workItem.getWorkflowData()).thenReturn(workflowData);
        when(workflowData.getPayload()).thenReturn(payload);
        when(workflowSession.adaptTo(ResourceResolver.class)).thenReturn(resolver);
        when(resolver.getResource(payload + METADATA_PATH)).thenReturn(metadataResource);
        when(metadataResource.adaptTo(ModifiableValueMap.class)).thenReturn(map);
        when(map.get(CQ_TAGS, String[].class)).thenReturn(null);
        process.execute(workItem, workflowSession, metaDataMap);
        verify(map).put(CQ_TAGS, WKND_BRAND_TAG);
        verify(resolver).commit();
    }

    @Test
    void testExecute_AddTag_WhenNotPresent() throws Exception {
        String payload = "/content/dam/test";
        when(workItem.getWorkflowData()).thenReturn(workflowData);
        when(workflowData.getPayload()).thenReturn(payload);
        when(workflowSession.adaptTo(ResourceResolver.class)).thenReturn(resolver);
        when(resolver.getResource(payload + METADATA_PATH)).thenReturn(metadataResource);
        when(metadataResource.adaptTo(ModifiableValueMap.class)).thenReturn(map);
        when(map.get(CQ_TAGS, String[].class)).thenReturn(new String[]{"tag1"});
        process.execute(workItem, workflowSession, metaDataMap);
        verify(map).put(CQ_TAGS, WKND_BRAND_TAG);
        verify(resolver).commit();
    }

    @Test
    void testExecute_TagAlreadyExists() throws Exception {
        String payload = "/content/dam/test";
        when(workItem.getWorkflowData()).thenReturn(workflowData);
        when(workflowData.getPayload()).thenReturn(payload);
        when(workflowSession.adaptTo(ResourceResolver.class)).thenReturn(resolver);
        when(resolver.getResource(payload + METADATA_PATH)).thenReturn(metadataResource);
        when(metadataResource.adaptTo(ModifiableValueMap.class)).thenReturn(map);
        when(map.get(CQ_TAGS, String[].class)).thenReturn(new String[]{WKND_BRAND_TAG});
        process.execute(workItem, workflowSession, metaDataMap);
        verify(map, never()).put(CQ_TAGS, WKND_BRAND_TAG);
        verify(resolver).commit();
    }

    @Test
    void testExecute_InvalidPayload() throws WorkflowException {
        when(workItem.getWorkflowData()).thenReturn(workflowData);
        when(workflowData.getPayload()).thenReturn("/content/page");
        process.execute(workItem, workflowSession, metaDataMap);
        verifyNoInteractions(resolver);
    }

    @Test
    void testExecute_MetadataNull() throws Exception {
        String payload = "/content/dam/test";
        when(workItem.getWorkflowData()).thenReturn(workflowData);
        when(workflowData.getPayload()).thenReturn(payload);
        when(workflowSession.adaptTo(ResourceResolver.class)).thenReturn(resolver);
        when(resolver.getResource(payload + METADATA_PATH)).thenReturn(null);
        process.execute(workItem, workflowSession, metaDataMap);
        verify(resolver, never()).commit();
    }

    @Test
    void testExecute_Exception() throws Exception {
        String payload = "/content/dam/test";
        when(workItem.getWorkflowData()).thenReturn(workflowData);
        when(workflowData.getPayload()).thenReturn(payload);
        when(workflowSession.adaptTo(ResourceResolver.class)).thenReturn(resolver);
        when(resolver.getResource(anyString())).thenThrow(new RuntimeException("Error"));
        process.execute(workItem, workflowSession, metaDataMap);
        verify(resolver, never()).commit();
    }
}
