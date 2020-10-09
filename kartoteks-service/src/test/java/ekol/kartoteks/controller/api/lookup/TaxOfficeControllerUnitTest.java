package ekol.kartoteks.controller.api.lookup;

import ekol.kartoteks.controller.lookup.TaxOfficeController;
import ekol.kartoteks.service.TaxOfficeUpdateService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.context.junit4.SpringRunner;

import static org.mockito.Mockito.verify;

/**
 * Created by kilimci on 22/11/16.
 */
@RunWith(SpringRunner.class)
public class TaxOfficeControllerUnitTest {

    @Mock
    private TaxOfficeUpdateService taxOfficeUpdateService;

    @InjectMocks
    private TaxOfficeController taxOfficeController;

    @Before
    public void init(){
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void shouldUpdateAll() {
        taxOfficeController.updateAll();
        verify(taxOfficeUpdateService).updateAll();
    }
}
