package com.adobe.aem.guides.wknd.core.models.impl;

import com.adobe.aem.guides.wknd.core.models.TravelResortCF;
import com.day.cq.search.PredicateGroup;
import com.day.cq.search.Query;
import com.day.cq.search.QueryBuilder;
import com.day.cq.search.result.Hit;
import com.day.cq.search.result.SearchResult;
import junitx.util.PrivateAccessor;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ValueMap;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import javax.jcr.Session;
import java.util.Collections;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TravelResortModelImplTest {

    @InjectMocks
    private TravelResortModelImpl model;

    @Mock
    private SlingHttpServletRequest request;

    @Mock
    private ResourceResolver resolver;

    @Mock
    private QueryBuilder queryBuilder;

    @Mock
    private Session session;

    @Mock
    private Query query;

    @Mock
    private SearchResult searchResult;

    @Mock
    private Hit hit;

    @Mock
    private Resource resource;

    @Mock
    private ValueMap valueMap;

    @Mock
    private TravelResortCF travelResortCF;

    @BeforeEach
    void setUp() throws NoSuchFieldException {
        PrivateAccessor.setField(model, "resolver", resolver);
        PrivateAccessor.setField(model, "queryBuilder", queryBuilder);
    }

    @Test
    void testManualVariation() throws NoSuchFieldException {
        Resource item = mock(Resource.class);
        ValueMap vm = mock(ValueMap.class);
        when(item.getValueMap()).thenReturn(vm);
        when(vm.get("resortPath", String.class)).thenReturn("/content/dam/resort1");
        List<Resource> resortList = List.of(item);
        Resource cfResource = mock(Resource.class);
        when(resolver.getResource("/content/dam/resort1")).thenReturn(cfResource);
        when(cfResource.adaptTo(TravelResortCF.class)).thenReturn(travelResortCF);
        PrivateAccessor.setField(model, "variation", "manual");
        PrivateAccessor.setField(model, "resortList", resortList);
        model.init();
        List<TravelResortCF> result = model.getResorts();
        assertNotNull(result);
        assertEquals(1, result.size());
    }

    @Test
    void testManualVariation_NullPath() throws NoSuchFieldException {
        Resource item = mock(Resource.class);
        ValueMap vm = mock(ValueMap.class);
        when(item.getValueMap()).thenReturn(vm);
        when(vm.get("resortPath", String.class)).thenReturn(null);
        PrivateAccessor.setField(model, "variation", "manual");
        PrivateAccessor.setField(model, "resortList", List.of(item));
        model.init();
        assertTrue(model.getResorts().isEmpty());
    }

    @Test
    void testDynamicVariation() throws Exception {
        when(resolver.adaptTo(Session.class)).thenReturn(session);
        when(queryBuilder.createQuery(any(PredicateGroup.class), eq(session))).thenReturn(query);
        when(query.getResult()).thenReturn(searchResult);
        when(searchResult.getHits()).thenReturn(List.of(hit));
        when(hit.getResource()).thenReturn(resource);
        when(resource.adaptTo(TravelResortCF.class)).thenReturn(travelResortCF);
        PrivateAccessor.setField(model, "variation", "dynamic");
        PrivateAccessor.setField(model, "count", 2);
        model.init();
        List<TravelResortCF> result = model.getResorts();
        assertNotNull(result);
        assertEquals(1, result.size());
    }

    @Test
    void testDynamicVariation_EmptyHits() throws Exception {
        when(resolver.adaptTo(Session.class)).thenReturn(session);
        when(queryBuilder.createQuery(any(), eq(session))).thenReturn(query);
        when(query.getResult()).thenReturn(searchResult);
        when(searchResult.getHits()).thenReturn(Collections.emptyList());
        PrivateAccessor.setField(model, "variation", "dynamic");
        model.init();
        assertTrue(model.getResorts().isEmpty());
    }

    @Test
    void testDynamicVariation_Exception() throws Exception {
        when(resolver.adaptTo(Session.class)).thenReturn(session);
        when(queryBuilder.createQuery(any(), eq(session))).thenReturn(query);
        when(query.getResult()).thenReturn(searchResult);
        when(searchResult.getHits()).thenReturn(List.of(hit));
        when(hit.getResource()).thenThrow(new RuntimeException("Error"));
        PrivateAccessor.setField(model, "variation", "dynamic");
        model.init();
        assertNotNull(model.getResorts());
    }

    @Test
    void testNoVariation() throws NoSuchFieldException {
        PrivateAccessor.setField(model, "variation", "other");
        model.init();
        assertTrue(model.getResorts().isEmpty());
    }
}
