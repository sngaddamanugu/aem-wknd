package com.adobe.aem.guides.wknd.core.workflows;
import com.adobe.granite.workflow.exec.WorkItem;
import com.adobe.granite.workflow.exec.WorkflowData;
import com.adobe.granite.workflow.WorkflowSession;
import com.adobe.granite.workflow.metadata.MetaDataMap;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import javax.jcr.Node;
import javax.jcr.RepositoryException;
import javax.jcr.Session;
import static org.mockito.Mockito.never;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static com.adobe.aem.guides.wknd.core.util.Constants.PROCESS_ARGS;

@ExtendWith(MockitoExtension.class)
class SetPageApprovalProcessTest {

    @InjectMocks
    private SetPageApprovalProcess process;

    @Mock
    private WorkItem workItem;

    @Mock
    private WorkflowSession workflowSession;

    @Mock
    private WorkflowData workflowData;

    @Mock
    private MetaDataMap metaDataMap;

    @Mock
    private Session session;

    @Mock
    private Node node;

    @Test
    void testExecute_Success() throws Exception {
        when(workItem.getWorkflowData()).thenReturn(workflowData);
        when(workflowData.getPayload()).thenReturn("/content/page");
        when(workflowSession.adaptTo(Session.class)).thenReturn(session);
        when(session.getNode("/content/page/jcr:content")).thenReturn(node);
        when(metaDataMap.containsKey(PROCESS_ARGS)).thenReturn(true);
        when(metaDataMap.get(PROCESS_ARGS, String.class)).thenReturn("approvalStatus=approved");
        process.execute(workItem, workflowSession, metaDataMap);
        verify(node).setProperty("approvalStatus", "approved");
        verify(session).save();
    }

    @Test
    void testExecute_MultipleProperties() throws Exception {
        when(workItem.getWorkflowData()).thenReturn(workflowData);
        when(workflowData.getPayload()).thenReturn("/content/page");
        when(workflowSession.adaptTo(Session.class)).thenReturn(session);
        when(session.getNode("/content/page/jcr:content")).thenReturn(node);
        when(metaDataMap.containsKey(PROCESS_ARGS)).thenReturn(true);
        when(metaDataMap.get(PROCESS_ARGS, String.class)).thenReturn("status=approved,reviewer=admin");
        process.execute(workItem, workflowSession, metaDataMap);
        verify(node).setProperty("status", "approved");
        verify(node).setProperty("reviewer", "admin");
        verify(session).save();
    }

    @Test
    void testExecute_EmptyArgs() throws RepositoryException {
        when(workItem.getWorkflowData()).thenReturn(workflowData);
        when(workflowData.getPayload()).thenReturn("/content/page");
        when(workflowSession.adaptTo(Session.class)).thenReturn(session);
        when(metaDataMap.containsKey(PROCESS_ARGS)).thenReturn(false);
        process.execute(workItem, workflowSession, metaDataMap);
        verify(session, never()).save();
    }

    @Test
    void testExecute_NullSession() {
        when(workItem.getWorkflowData()).thenReturn(workflowData);
        when(workflowData.getPayload()).thenReturn("/content/page");
        when(workflowSession.adaptTo(Session.class)).thenReturn(null);
        when(metaDataMap.containsKey(PROCESS_ARGS)).thenReturn(true);
        when(metaDataMap.get(PROCESS_ARGS, String.class)).thenReturn("key=value");
        process.execute(workItem, workflowSession, metaDataMap);
        verify(workflowSession).adaptTo(Session.class);
    }

    @Test
    void testExecute_NullNode() throws Exception {
        when(workItem.getWorkflowData()).thenReturn(workflowData);
        when(workflowData.getPayload()).thenReturn("/content/page");
        when(workflowSession.adaptTo(Session.class)).thenReturn(session);
        when(session.getNode(anyString())).thenReturn(null);
        when(metaDataMap.containsKey(PROCESS_ARGS)).thenReturn(true);
        when(metaDataMap.get(PROCESS_ARGS, String.class)).thenReturn("key=value");
        process.execute(workItem, workflowSession, metaDataMap);
        verify(session, never()).save();
    }

}
