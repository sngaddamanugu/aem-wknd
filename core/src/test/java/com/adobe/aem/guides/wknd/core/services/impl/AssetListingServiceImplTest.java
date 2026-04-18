package com.adobe.aem.guides.wknd.core.services.impl;
import com.adobe.aem.guides.wknd.core.configs.AssetListingConfig;
import com.adobe.aem.guides.wknd.core.dtos.AssetItem;
import com.day.cq.search.*;
import com.day.cq.search.result.Hit;
import com.day.cq.search.result.SearchResult;
import org.apache.sling.api.resource.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import javax.jcr.RepositoryException;
import javax.jcr.Session;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class AssetListingServiceImplTest {

    @InjectMocks
    private AssetListingServiceImpl service;

    @Mock
    private QueryBuilder queryBuilder;

    @Mock
    private ResourceResolverFactory factory;

    @Mock
    private ResourceResolver resolver;

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
    private AssetListingConfig config;

    @BeforeEach
    void setUp() throws Exception {

        when(factory.getServiceResourceResolver(anyMap())).thenReturn(resolver);
        when(resolver.adaptTo(Session.class)).thenReturn(session);
        when(queryBuilder.createQuery(any(PredicateGroup.class), eq(session))).thenReturn(query);
        when(query.getResult()).thenReturn(searchResult);
        when(searchResult.getHits()).thenReturn(Collections.singletonList(hit));

        Resource assetResource = mock(Resource.class);
        Resource metadataResource = mock(Resource.class);

        when(hit.getResource()).thenReturn(assetResource);
        when(assetResource.getPath()).thenReturn("/content/dam/sample.pdf");
        when(assetResource.getName()).thenReturn("sample.pdf");
        when(resolver.getResource("/content/dam/sample.pdf")).thenReturn(assetResource);
        when(assetResource.getChild("jcr:content/metadata")).thenReturn(metadataResource);
        ValueMap metadataMap = mock(ValueMap.class);
        when(metadataResource.getValueMap()).thenReturn(metadataMap);
        when(metadataMap.get("dc:title", String.class)).thenReturn("Sample Title");
        when(metadataMap.get("dc:description", String.class)).thenReturn("Sample Description");
        when(metadataMap.get("dc:creator", String.class)).thenReturn("Admin");
        when(metadataMap.get("dc:format", String.class)).thenReturn("application/pdf");
        ValueMap assetVM = mock(ValueMap.class);
        when(assetResource.getValueMap()).thenReturn(assetVM);
        when(assetVM.get(eq("jcr:created"), anyString())).thenReturn("2024-01-01");
    }

    @Test
    void testGetAssets_EmptyHits() {
        when(searchResult.getHits()).thenReturn(Collections.emptyList());
        Map<String, List<AssetItem>> result = service.getAssets("/content/dam");
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    void testGetAssets_NullResource() throws RepositoryException {
        when(hit.getResource()).thenReturn(null);
        Map<String, List<AssetItem>> result = service.getAssets("/content/dam");
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    void testGetAssets_ExceptionHandling() throws Exception {
        when(factory.getServiceResourceResolver(anyMap())).thenThrow(new LoginException("Error"));
        Map<String, List<AssetItem>> result = service.getAssets("/content/dam");
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    void testGetAssets_ValidRootPath() throws LoginException {
        when(factory.getServiceResourceResolver(anyMap())).thenReturn(resolver);
        when(resolver.adaptTo(Session.class)).thenReturn(session);
        List<Hit> hits = List.of(hit);
        when(searchResult.getHits()).thenReturn(hits);
        when(resolver.adaptTo(QueryBuilder.class)).thenReturn(queryBuilder);
        when(queryBuilder.createQuery(any(), eq(session))).thenReturn(query);
        Map<String, List<AssetItem>> result = service.getAssets("/content/dam");
        assertNotNull(result);
        assertEquals(1,result.get("pdf").size());
        assertTrue(result.containsKey("pdf"));

    }

    @Test
    void testNullRootPathUsesConfig() throws LoginException {
        when(factory.getServiceResourceResolver(anyMap())).thenReturn(resolver);
        when(resolver.adaptTo(Session.class)).thenReturn(session);
        List<Hit> hits = List.of(hit);
        when(searchResult.getHits()).thenReturn(hits);
        when(resolver.adaptTo(QueryBuilder.class)).thenReturn(queryBuilder);
        when(queryBuilder.createQuery(any(), eq(session))).thenReturn(query);
        when(config.assetListRootPath()).thenReturn("/content/dam/wknd");
        service.activate(config);
        Map<String, List<AssetItem>> result = service.getAssets(null);
        assertNotNull(result);
        assertEquals(1,result.get("pdf").size());
        assertTrue(result.containsKey("pdf"));
    }

}