package ekol.kartoteks.controller.api;

import ekol.exceptions.BadRequestException;
import ekol.exceptions.ResourceNotFoundException;
import ekol.kartoteks.builder.SectorBuilder;
import ekol.kartoteks.controller.SectorController;
import ekol.kartoteks.domain.Sector;
import ekol.kartoteks.repository.SectorRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

/**
 * Created by kilimci on 22/11/16.
 */
@RunWith(SpringRunner.class)
public class SectorControllerUnitTest {

    @Mock
    private SectorRepository sectorRepository;

    @InjectMocks
    private SectorController sectorController;

    @Before
    public void init(){
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void shouldAddParentSector(){
        String code = "code";
        Long id = 1L;
        SectorBuilder sectorBuilder = SectorBuilder.aSector().withCode(code);
        Sector sector = sectorBuilder.build();
        when(sectorRepository.save(sector)).thenReturn(sectorBuilder.withId(id).build());
        Sector result = sectorController.addParentSector(sector);
        verify(sectorRepository).save(sector);
        assertEquals(new Long(1), result.getId());
    }
    @Test
    public void shouldGetParentSectorList(){
        String code = "code";
        Long id = 1L;
        Sector sector = SectorBuilder.aSector().withCode(code).withId(id).build();
        when(sectorRepository.findByParent(null)).thenReturn(Arrays.asList(sector));
        List<Sector> result = sectorController.getParentSectorList();

        assertEquals(1, result.size());
        assertEquals(id, result.get(0).getId());
    }

    @Test
    public void shouldUpdateParentSector(){
        String code = "code";
        Long id = 1L;
        Sector sector = SectorBuilder.aSector().withCode(code).withId(id).build();
        when(sectorRepository.findOne(id)).thenReturn(sector);
        when(sectorRepository.save(sector)).thenReturn(sector);
        Sector result = sectorController.updateParentSector(id, sector);
        verify(sectorRepository).save(sector);

        assertEquals(new Long(1), result.getId());
    }

    @Test(expected = ResourceNotFoundException.class)
    public void shouldThrowResourceNotFoundWhenUpdateSectorDoesNotExist(){
        String code = "code";
        Long id = 1L;
        Sector sector = SectorBuilder.aSector().withCode(code).withId(id).build();
        when(sectorRepository.findOne(id)).thenReturn(null);
        sectorController.updateParentSector(id, sector);
        verify(sectorRepository, times(0)).save(sector);
    }

    @Test(expected = BadRequestException.class)
    public void shouldThrowBadRequestWhenUpdateSectorIsNull(){
        String code = "code";
        Long id = 1L;
        Sector sector = SectorBuilder.aSector().withCode(code).withId(id).build();
        when(sectorRepository.findOne(id)).thenReturn(sector);
        sectorController.updateParentSector(id, null);
        verify(sectorRepository, times(0)).save(sector);
    }

    @Test(expected = BadRequestException.class)
    public void shouldThrowBadRequestWhenIdsDontMatch(){
        String code = "code";
        Long id = 1L;
        Sector sector = SectorBuilder.aSector().withCode(code).withId(id).build();
        when(sectorRepository.findOne(id)).thenReturn(sector);
        sectorController.updateParentSector(2L, sector);
        verify(sectorRepository, times(0)).save(sector);
    }

    @Test
    public void shouldDeleteParentSector(){
        String code = "code";
        Long id = 1L;
        Sector sector = SectorBuilder.aSector().withCode(code).withId(id).withDeleted(false).build();
        ArgumentCaptor<Sector> argumentCaptor = ArgumentCaptor.forClass(Sector.class);
        when(sectorRepository.findOne(id)).thenReturn(sector);
        sectorController.deleteParentSector(id);
        verify(sectorRepository).save(argumentCaptor.capture());

        assertEquals(id, argumentCaptor.getValue().getId());
        assertTrue(argumentCaptor.getValue().isDeleted());

    }

    @Test(expected = ResourceNotFoundException.class)
    public void shouldThrowResourceNotFoundWhenDeleteSector(){
        String code = "code";
        Long id = 1L;
        Sector sector = SectorBuilder.aSector().withCode(code).withId(id).build();
        when(sectorRepository.findOne(id)).thenReturn(null);
        sectorController.deleteParentSector(id);
        verify(sectorRepository, times(0)).save(sector);
    }

    @Test
    public void shouldGetSubSectors(){
        String parentCode = "parent";
        String childCode = "child";
        Long parentId = 1L;
        Long childId = 2L;
        Sector sector = SectorBuilder.aSector().withCode(parentCode).withId(parentId).build();
        Sector child = SectorBuilder.aSector().withCode(childCode).withId(childId).withParent(sector).build();
        when(sectorRepository.findOne(parentId)).thenReturn(sector);
        when(sectorRepository.findByParent(sector)).thenReturn(Arrays.asList(child));
        List<Sector> result = sectorController.getSubSectors(parentId);

        assertEquals(1, result.size());
        assertEquals(childId, result.get(0).getId());

    }

    @Test(expected = ResourceNotFoundException.class)
    public void shouldThrowResourceNotFoundWhenParentSectorDoesNotExist(){
        String parentCode = "parent";
        Long parentId = 1L;
        Sector sector = SectorBuilder.aSector().withCode(parentCode).withId(parentId).build();

        when(sectorRepository.findOne(parentId)).thenReturn(null);
        sectorController.getSubSectors(parentId);
        verify(sectorRepository, times(0)).findByParent(sector);
    }

    @Test
    public void shouldAddSubSector(){
        String parentCode = "parent";
        String childCode = "child";
        Long parentId = 1L;
        Long childId = 2L;
        Sector sector = SectorBuilder.aSector().withCode(parentCode).withId(parentId).build();
        Sector child = SectorBuilder.aSector().withCode(childCode).withId(childId).withParent(sector).build();
        ArgumentCaptor<Sector> argumentCaptor = ArgumentCaptor.forClass(Sector.class);
        when(sectorRepository.findOne(parentId)).thenReturn(sector);
        sectorController.addSubSector(parentId, child);
        verify(sectorRepository).save(argumentCaptor.capture());

        assertEquals(childId, argumentCaptor.getValue().getId());
        assertNotNull(argumentCaptor.getValue().getParent());
        assertEquals(parentId, argumentCaptor.getValue().getParent().getId());
    }

    @Test(expected = ResourceNotFoundException.class)
    public void shouldThrowResourceNotFoundWhenAddSubSectorParentSectorDoesNotExist(){
        String parentCode = "parent";
        String childCode = "child";
        Long parentId = 1L;
        Long childId = 2L;
        Sector sector = SectorBuilder.aSector().withCode(parentCode).withId(parentId).build();
        Sector child = SectorBuilder.aSector().withCode(childCode).withId(childId).withParent(sector).build();

        when(sectorRepository.findOne(parentId)).thenReturn(null);
        sectorController.addSubSector(parentId, child);
        verify(sectorRepository, times(0)).save(child);
    }
}
