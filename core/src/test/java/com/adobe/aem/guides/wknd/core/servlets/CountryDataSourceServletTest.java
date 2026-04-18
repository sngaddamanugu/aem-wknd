package com.adobe.aem.guides.wknd.core.servlets;
import static org.junit.jupiter.api.Assertions.*;
import java.util.*;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ValueMap;
import org.junit.jupiter.api.*;
import com.adobe.granite.ui.components.ds.DataSource;
import io.wcm.testing.mock.aem.junit5.AemContext;
import io.wcm.testing.mock.aem.junit5.AemContextExtension;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(AemContextExtension.class)
class CountryDataSourceServletTest {

    private final AemContext context = new AemContext();
    private Locale originalDefaultLocale;

    @BeforeEach
    void setUp() {
        originalDefaultLocale = Locale.getDefault();
        Locale.setDefault(Locale.ENGLISH);
    }

    @AfterEach
    void tearDown() {
        Locale.setDefault(originalDefaultLocale);
    }

    @Test
    void doGet_setsDatasourceWithAllIsoCountries() {
        CountryDataSourceServlet servlet = new CountryDataSourceServlet();
        servlet.doGet(context.request(), context.response());
        Object attr = context.request().getAttribute(DataSource.class.getName());
        assertNotNull(attr, "Expected DataSource request attribute to be set");
        assertTrue(attr instanceof DataSource);
        DataSource ds = (DataSource) attr;
        List<Resource> items = new ArrayList<>();
        ds.iterator().forEachRemaining(items::add);
        assertEquals(Locale.getISOCountries().length, items.size(), "Expected one entry per ISO country");
        Map<String, String> byValueToText = new HashMap<>();
        for (Resource r : items) {
            ValueMap vm = r.getValueMap();
            assertNotNull(vm.get("text", String.class));
            assertNotNull(vm.get("value", String.class));
            byValueToText.put(vm.get("value", String.class), vm.get("text", String.class));
        }
        assertTrue(byValueToText.containsKey("us"), "Expected 'us' value to exist");
        assertEquals(new Locale("", "US").getDisplayCountry(Locale.ENGLISH), byValueToText.get("us"));
    }

    @Test
    void doGet_valueIsLowercaseIsoCode() {
        CountryDataSourceServlet servlet = new CountryDataSourceServlet();
        servlet.doGet(context.request(), context.response());
        DataSource ds = (DataSource) context.request().getAttribute(DataSource.class.getName());
        assertNotNull(ds);
        Set<String> isoUpper = new HashSet<>(Arrays.asList(Locale.getISOCountries()));
        ds.iterator().forEachRemaining(r -> {
            String value = r.getValueMap().get("value", String.class);
            assertNotNull(value);
            String upper = value.toUpperCase(Locale.ROOT);
            assertTrue(isoUpper.contains(upper), "Value should map to an ISO code: " + value);
            assertEquals(value, value.toLowerCase(Locale.ROOT), "Value should be lowercase: " + value);
        });
    }
}

