package ekol.hibernate5.domain.embeddable;

import static org.junit.Assert.assertTrue;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.ConfigFileApplicationContextInitializer;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(initializers=ConfigFileApplicationContextInitializer.class)
public class DateTimeWindowTest {

    @Test
    public void shouldContainWhenStartAndEndIsNull() {
        DateTimeWindow window = new DateTimeWindow(null, null);
        assertTrue(window.contains(LocalDateTime.now()));
    }

    @Test
    public void shouldContainWhenStartIsNull() {
        LocalDateTime end = LocalDateTime.now().plus(1, ChronoUnit.MINUTES);
        DateTimeWindow window = new DateTimeWindow(null, end);
        assertTrue(window.contains(LocalDateTime.now()));
    }

    @Test
    public void shouldNotContainWhenStartIsNull() {
        LocalDateTime end = LocalDateTime.now().minus(1, ChronoUnit.MINUTES);
        DateTimeWindow window = new DateTimeWindow(null, end);
        assertTrue(!window.contains(LocalDateTime.now()));
    }

    @Test
    public void shouldContainWhenEndIsNull() {
        LocalDateTime start = LocalDateTime.now().minus(1, ChronoUnit.MINUTES);
        DateTimeWindow window = new DateTimeWindow(start, null);
        assertTrue(window.contains(LocalDateTime.now()));
    }

    @Test
    public void shouldNotContainWhenEndIsNull() {
        LocalDateTime start = LocalDateTime.now().plus(1, ChronoUnit.MINUTES);
        DateTimeWindow window = new DateTimeWindow(start, null);
        assertTrue(!window.contains(LocalDateTime.now()));
    }

    @Test
    public void shouldContain() {
        LocalDateTime start = LocalDateTime.now().minus(1, ChronoUnit.MINUTES);
        LocalDateTime end = LocalDateTime.now().plus(1, ChronoUnit.MINUTES);
        DateTimeWindow window = new DateTimeWindow(start, end);
        assertTrue(window.contains(LocalDateTime.now()));
    }

    @Test
    public void shouldNotContain() {
        LocalDateTime start = LocalDateTime.now().plus(1, ChronoUnit.MINUTES);
        LocalDateTime end = start.plus(1, ChronoUnit.MINUTES);
        DateTimeWindow window = new DateTimeWindow(start, end);
        assertTrue(!window.contains(LocalDateTime.now()));
    }
    
}
