package ekol.hibernate5.domain.embeddable;

import static org.junit.Assert.assertTrue;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.ConfigFileApplicationContextInitializer;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * Created by kilimci on 20/09/16.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(initializers=ConfigFileApplicationContextInitializer.class)
public class DateWindowTest {

    @Test
    public void shouldContainDateWhenStartAndEndDateIsNull(){
        DateWindow dateWindow = new DateWindow(null, null);
        assertTrue(dateWindow.contains(LocalDate.now()));
    }

    @Test
    public void shouldContainDateWhenStartDateIsNull(){
        LocalDate endDate = LocalDate.now().plus(1, ChronoUnit.DAYS);
        DateWindow dateWindow = new DateWindow(null, endDate);
        assertTrue(dateWindow.contains(LocalDate.now()));
    }
    @Test
    public void shouldNotContainDateWhenStartDateIsNull(){
        LocalDate endDate = LocalDate.now().minus(1, ChronoUnit.DAYS);
        DateWindow dateWindow = new DateWindow(null, endDate);
        assertTrue(!dateWindow.contains(LocalDate.now()));
    }

    @Test
    public void shouldContainDateWhenEndDateIsNull(){
        LocalDate startDate = LocalDate.now().minus(1, ChronoUnit.DAYS);
        DateWindow dateWindow = new DateWindow(startDate, null);
        assertTrue(dateWindow.contains(LocalDate.now()));
    }

    @Test
    public void shouldNotContainDateWhenEndDateIsNull(){
        LocalDate startDate = LocalDate.now().plus(1, ChronoUnit.DAYS);
        DateWindow dateWindow = new DateWindow(startDate, null);
        assertTrue(!dateWindow.contains(LocalDate.now()));
    }

    @Test
    public void shouldContainDate(){
        LocalDate startDate = LocalDate.now().minus(1, ChronoUnit.DAYS);
        LocalDate endDate = LocalDate.now().plus(1, ChronoUnit.DAYS);
        DateWindow dateWindow = new DateWindow(startDate, endDate);
        assertTrue(dateWindow.contains(LocalDate.now()));
    }

    @Test
    public void shouldNotContainDate(){
        LocalDate startDate = LocalDate.now().plus(1, ChronoUnit.DAYS);
        LocalDate endDate = startDate.plus(1, ChronoUnit.DAYS);
        DateWindow dateWindow = new DateWindow(startDate, endDate);
        assertTrue(!dateWindow.contains(LocalDate.now()));
    }
}
