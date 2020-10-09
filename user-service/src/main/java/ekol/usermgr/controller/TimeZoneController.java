package ekol.usermgr.controller;


import ekol.model.LookupValueLabel;
import ekol.resource.oauth2.SessionOwner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeSet;
import java.util.stream.Collectors;

/**
 * Created by ozer on 15/11/16.
 */
@RestController
public class TimeZoneController {

    @Autowired
    private SessionOwner sessionOwner;


    @RequestMapping(value = "/timezone", method = RequestMethod.GET)
    public List<LookupValueLabel> getZones() {
        TreeSet<String> sortedZones = new TreeSet<>(ZoneId.getAvailableZoneIds());
        return sortedZones.stream().map(zone -> new LookupValueLabel(zone, zone, zone)).collect(Collectors.toList());
    }

    @RequestMapping(value = "/time", method = RequestMethod.GET)
    public String getTimeOfMyZone() {
        String timeZone = sessionOwner.getCurrentUser().getTimeZoneId();

        if (timeZone != null) {
            return DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm").format(Instant.now().atZone(ZoneId.of(timeZone)).toLocalDateTime()) + " " + timeZone;
        } else {
            return null;
        }
    }

    @RequestMapping(value = "/time/by-timezone", method = RequestMethod.GET)
    public String getTimeOfMyZone(@RequestParam String timezone) {
        return DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm").format(Instant.now().atZone(ZoneId.of(timezone))) + " " + timezone;
    }

    @RequestMapping(value = "/get-office-timezone", method = RequestMethod.GET)
    public String getOfficeTimezone(@RequestParam String office) {
        Map<String, String> officeTimezoneMap = new HashMap<>();
        officeTimezoneMap.put("Kardelen", "Europe/Istanbul");
        officeTimezoneMap.put("Köln", "Europe/Berlin");
        officeTimezoneMap.put("Mannheim", "Europe/Berlin");
        officeTimezoneMap.put("Bükreş", "Europe/Bucharest");
        officeTimezoneMap.put("Budapest", "Europe/Budapest");
        return officeTimezoneMap.get(office);
    }
}
