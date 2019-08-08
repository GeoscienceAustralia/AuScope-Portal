package au.gov.geoscience.portal.server.controllers;

import org.auscope.portal.core.services.VocabularyCacheService;
import org.auscope.portal.core.test.PortalTestClass;
import org.jmock.Expectations;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class TestVocabularyCacheController extends PortalTestClass {

    private VocabularyCacheController vocabularyCacheController;
    private VocabularyCacheService mockVocabularyCacheService;

    @Before
    public void setUp() {
        this.mockVocabularyCacheService = context.mock(VocabularyCacheService.class);

        context.checking(new Expectations() {
            {
                oneOf(mockVocabularyCacheService).updateCache();
            }
        });
        this.vocabularyCacheController = new VocabularyCacheController(this.mockVocabularyCacheService);

    }

    @Test
    public void testGetVocabularies() {

        context.checking(new Expectations() {
            {
                oneOf(mockVocabularyCacheService).getVocabularyCache();
            }
        });

        vocabularyCacheController.getVocabularies();
    }


    @Test
    public void testUpdateVocabularyCache() {

        context.checking(new Expectations() {
            {
                oneOf(mockVocabularyCacheService).updateCache();
                will(returnValue(true));
            }
        });

        vocabularyCacheController.updateVocabularyCache();
    }
}