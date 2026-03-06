package com.adobe.aem.guides.wknd.core.servlets;

import java.util.*;
import javax.servlet.Servlet;
import com.adobe.aem.guides.wknd.core.util.Constants;
import com.adobe.granite.ui.components.ds.ValueMapResource;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.servlets.SlingSafeMethodsServlet;
import org.apache.sling.api.wrappers.ValueMapDecorator;
import org.osgi.service.component.annotations.Component;
import com.adobe.granite.ui.components.ds.DataSource;
import com.adobe.granite.ui.components.ds.SimpleDataSource;
import org.apache.sling.api.resource.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Component(service = Servlet.class, property = {
                "sling.servlet.resourceTypes=" + Constants.COUNTRY_SERVLET_RESOURCE_TYPE,
                "sling.servlet.methods=" + Constants.GET_METHOD})
public class CountryDataSourceServlet extends SlingSafeMethodsServlet {

    private static final Logger LOG = LoggerFactory.getLogger(CountryDataSourceServlet.class);

    @Override
    protected void doGet(SlingHttpServletRequest request,
                         SlingHttpServletResponse response) {

        LOG.info("Country datasource servlet triggered");
        List<Resource> countries = new ArrayList<>();
        for (String countryCode : Locale.getISOCountries()) {
            Locale locale = new Locale("", countryCode);
            String countryName = locale.getDisplayCountry();
            ValueMap vm = new ValueMapDecorator(new HashMap<>());
            vm.put(Constants.TEXT, countryName);
            vm.put(Constants.VALUE, countryCode.toLowerCase());
            countries.add(new ValueMapResource(request.getResourceResolver(),
                    new ResourceMetadata(), "nt:unstructured", vm));}
        DataSource ds = new SimpleDataSource(countries.iterator());
        request.setAttribute(DataSource.class.getName(), ds);
    }
}
