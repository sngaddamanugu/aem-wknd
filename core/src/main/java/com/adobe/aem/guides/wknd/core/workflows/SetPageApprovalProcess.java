package com.adobe.aem.guides.wknd.core.workflows;

import com.adobe.granite.workflow.exec.WorkflowProcess;
import com.adobe.granite.workflow.exec.WorkItem;
import com.adobe.granite.workflow.WorkflowSession;
import com.adobe.granite.workflow.metadata.MetaDataMap;
import com.adobe.aem.guides.wknd.core.util.AssetUtil;
import org.osgi.service.component.annotations.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import javax.jcr.Node;
import javax.jcr.Session;
import java.util.Map;

import static com.adobe.aem.guides.wknd.core.util.Constants.JCR_CONTENT;

@Component(service = WorkflowProcess.class, property = {"process.label=Set Page Status"})

public class SetPageApprovalProcess implements WorkflowProcess {

    private static final Logger LOG = LoggerFactory.getLogger(SetPageApprovalProcess.class);

    @Override
    public void execute(WorkItem item, WorkflowSession workflowSession, MetaDataMap args) {

        try {
            String payloadPath = item.getWorkflowData().getPayload().toString();
            LOG.info("Processing page: {}", payloadPath);
            Session session = workflowSession.adaptTo(Session.class);
            Map<String, String> argumentMap = AssetUtil.buildArguments(args);
            if (argumentMap.isEmpty()) {
                LOG.warn("No PROCESS_ARGS provided");
                return;
            }
            Node pageNode = session.getNode(payloadPath + JCR_CONTENT);
            for (Map.Entry<String, String> entry : argumentMap.entrySet()) {
                LOG.info("Setting property {} = {}", entry.getKey(), entry.getValue());
                pageNode.setProperty(entry.getKey(), entry.getValue());
            }
            session.save();
            LOG.info("Page status updated successfully");
        } catch (Exception e) {
            LOG.error("Error updating page status", e);
        }
    }
}
