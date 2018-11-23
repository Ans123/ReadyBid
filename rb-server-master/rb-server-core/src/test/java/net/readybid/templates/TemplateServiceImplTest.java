package net.readybid.templates;

import org.junit.Before;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by DejanK on 10/6/2016.
 *
 */
public class TemplateServiceImplTest {

    private TemplateServiceImpl $service;

    private final String LOADED_TEMPLATE = "Loaded HTML Template";
    private final String PARSED_TEMPLATE = "Parsed HTML Template";
    private final String TEMPLATE_PATH = "templates\\path";
    private Map<String, String> MODEL;

    @Before
    public void before() {
        MODEL = new HashMap<>();
        $service = new TemplateServiceImpl();
    }

//    @Test
//    public void testParseTemplate() throws Exception {
//        when(loadTemplateService.loadTemplate(TEMPLATE_PATH)).thenReturn(LOADED_TEMPLATE);
//        when(parseTemplateService.parseTemplate(LOADED_TEMPLATE, MODEL, "{%s}")).thenReturn(PARSED_TEMPLATE);

//        final String $result = $service.parseTemplate(TEMPLATE_PATH, MODEL);

//        verify(loadTemplateService, times(1)).loadTemplate(TEMPLATE_PATH);
//        verify(parseTemplateService, times(1)).parseTemplate(LOADED_TEMPLATE, MODEL, "{%s}");
//        Assert.assertEquals(PARSED_TEMPLATE, $result);
//    }

//    @Test(expected = IOException.class)
//    public void testParseTemplateThrowsException() throws Exception {
////        doThrow(new IOException()).when(loadTemplateService).loadTemplate(TEMPLATE_PATH);
////        $service.parseTemplate(TEMPLATE_PATH, MODEL);
//    }
}