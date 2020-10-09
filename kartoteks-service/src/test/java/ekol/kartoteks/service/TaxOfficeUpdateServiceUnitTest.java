package ekol.kartoteks.service;

import ekol.kartoteks.domain.TaxOffice;
import ekol.kartoteks.repository.common.TaxOfficeRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.client.RestTemplate;

import java.util.*;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

/**
 * Created by kilimci on 20/10/16.
 */
@RunWith(SpringRunner.class)
public class TaxOfficeUpdateServiceUnitTest {

    @Mock
    private RestTemplate restTemplate;

    @Mock
    private TaxOfficeRepository taxOfficeRepository;

    @InjectMocks
    private TaxOfficeUpdateService taxOfficeUpdateService;

    private final String cityCode = "000";

    private TaxOffice existingTaxOffice = new TaxOffice();

    @Before
    public void init(){
        MockitoAnnotations.initMocks(this);
        existingTaxOffice.setCityCode(cityCode);
        existingTaxOffice.setCode("001");
        existingTaxOffice.setName("AAA");
        ReflectionTestUtils.setField(taxOfficeUpdateService, "taxOfficeHost", "host");
    }
    private Map<String, String> addTaxOffice(String code, String name){
        Map<String, String> taxOffice = new HashMap<>();
        taxOffice.put("kod", code);
        taxOffice.put("vdadi", name);
        return taxOffice;
    }

    @Test
    public void shouldCreateNewTaxOffice(){
        HashMap<String, List<Map<String, String>>> newData = new HashMap<>();
        List<Map<String, String>> taxOfficeList = new ArrayList<>();
        taxOfficeList.add(addTaxOffice("001", "AAA"));
        newData.put(cityCode, taxOfficeList);

        when(restTemplate.getForObject("host", HashMap.class)).thenReturn(newData);
        when(taxOfficeRepository.findAll()).thenReturn(new ArrayList<>());
        taxOfficeUpdateService.updateAll();
        ArgumentCaptor<TaxOffice> taxOfficeParam = ArgumentCaptor.forClass(TaxOffice.class);
        verify(taxOfficeRepository).save(taxOfficeParam.capture());
        assertEquals("AAA", taxOfficeParam.getValue().getName());
        assertEquals("001", taxOfficeParam.getValue().getCode());
        assertEquals(cityCode, taxOfficeParam.getValue().getCityCode());
        assertEquals(false, taxOfficeParam.getValue().isDeleted());

    }

    @Test
    public void shouldUpdateExistingTaxOffice(){
        HashMap<String, List<Map<String, String>>> newData = new HashMap<>();
        List<Map<String, String>> taxOfficeList = new ArrayList<>();
        taxOfficeList.add(addTaxOffice("001", "BBB"));
        newData.put(cityCode, taxOfficeList);

        when(restTemplate.getForObject("host", HashMap.class)).thenReturn(newData);
        when(taxOfficeRepository.findAll()).thenReturn(Arrays.asList(existingTaxOffice));
        taxOfficeUpdateService.updateAll();
        ArgumentCaptor<TaxOffice> taxOfficeParam = ArgumentCaptor.forClass(TaxOffice.class);
        verify(taxOfficeRepository).save(taxOfficeParam.capture());
        assertEquals("BBB", taxOfficeParam.getValue().getName());
        assertEquals("001", taxOfficeParam.getValue().getCode());
        assertEquals(cityCode, taxOfficeParam.getValue().getCityCode());
        assertEquals(false, taxOfficeParam.getValue().isDeleted());

    }

    @Test
    public void shouldDeleteExistingTaxOfficeIfNotFoundInUpdateData(){
        HashMap<String, List<Map<String, String>>> newData = new HashMap<>();
        List<Map<String, String>> taxOfficeList = new ArrayList<>();
        taxOfficeList.add(addTaxOffice("003", "CCC"));
        newData.put(cityCode, taxOfficeList);

        when(restTemplate.getForObject("host", HashMap.class)).thenReturn(newData);
        when(taxOfficeRepository.findAll()).thenReturn(Arrays.asList(existingTaxOffice));
        taxOfficeUpdateService.updateAll();
        ArgumentCaptor<TaxOffice> taxOfficeParam = ArgumentCaptor.forClass(TaxOffice.class);
        verify(taxOfficeRepository, times(2)).save(taxOfficeParam.capture());
        assertEquals("AAA", taxOfficeParam.getAllValues().get(0).getName());
        assertEquals("001", taxOfficeParam.getAllValues().get(0).getCode());
        assertEquals(cityCode, taxOfficeParam.getAllValues().get(0).getCityCode());
        assertEquals(true, taxOfficeParam.getAllValues().get(0).isDeleted());

        assertEquals("CCC", taxOfficeParam.getAllValues().get(1).getName());
        assertEquals("003", taxOfficeParam.getAllValues().get(1).getCode());
        assertEquals(cityCode, taxOfficeParam.getAllValues().get(1).getCityCode());
        assertEquals(false, taxOfficeParam.getAllValues().get(1).isDeleted());

    }

    @Test
    public void shouldDeleteAndCreateNewTaxOfficeWhenCodeChanges(){
        HashMap<String, List<Map<String, String>>> newData = new HashMap<>();
        List<Map<String, String>> taxOfficeList = new ArrayList<>();
        taxOfficeList.add(addTaxOffice("003", "AAA"));
        newData.put(cityCode, taxOfficeList);

        when(restTemplate.getForObject("host", HashMap.class)).thenReturn(newData);
        when(taxOfficeRepository.findAll()).thenReturn(Arrays.asList(existingTaxOffice));
        taxOfficeUpdateService.updateAll();
        ArgumentCaptor<TaxOffice> taxOfficeParam = ArgumentCaptor.forClass(TaxOffice.class);
        verify(taxOfficeRepository, times(2)).save(taxOfficeParam.capture());
        assertEquals("AAA", taxOfficeParam.getAllValues().get(0).getName());
        assertEquals("001", taxOfficeParam.getAllValues().get(0).getCode());
        assertEquals(cityCode, taxOfficeParam.getAllValues().get(0).getCityCode());
        assertEquals(true, taxOfficeParam.getAllValues().get(0).isDeleted());

        assertEquals("AAA", taxOfficeParam.getAllValues().get(1).getName());
        assertEquals("003", taxOfficeParam.getAllValues().get(1).getCode());
        assertEquals(cityCode, taxOfficeParam.getAllValues().get(1).getCityCode());
        assertEquals(false, taxOfficeParam.getAllValues().get(1).isDeleted());

    }
}
