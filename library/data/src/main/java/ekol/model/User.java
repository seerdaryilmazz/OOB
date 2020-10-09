package ekol.model;

import java.io.Serializable;
import java.util.*;
import java.util.stream.Collectors;

import org.apache.commons.lang3.builder.*;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * Created by ozer on 25/10/16.
 */

@JsonIgnoreProperties(ignoreUnknown = true)
public class User implements Serializable {

    private static final long serialVersionUID = 1L;

    private String clientId;
    
    private long id;
    private String username;
    private String displayName;
    private String email;
    private String phoneNumber;
    private String timeZoneId;
    private String sapNumber;
    private String office;
    private String mobileNumber;
    private String thumbnailPath;
    private List<IdNamePair> subsidiaries = new ArrayList<>();
    private List<IdNamePair> locations = new ArrayList<>();
    private List<IdNamePair> departments = new ArrayList<>();
    private List<IdNamePair> teams = new ArrayList<>();

    
    public String getClientId() {
		return clientId;
	}

	public void setClientId(String clientId) {
		this.clientId = clientId;
	}

	public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getTimeZoneId() {
        return timeZoneId;
    }

    public void setTimeZoneId(String timeZoneId) {
        this.timeZoneId = timeZoneId;
    }

    public List<IdNamePair> getSubsidiaries() {
        return subsidiaries;
    }

    public void setSubsidiaries(List<IdNamePair> subsidiaries) {
        this.subsidiaries = subsidiaries;
    }

    public List<IdNamePair> getLocations() {
        return locations;
    }

    public void setLocations(List<IdNamePair> locations) {
        this.locations = locations;
    }

    public List<IdNamePair> getDepartments() {
        return departments;
    }

    public void setDepartments(List<IdNamePair> departments) {
        this.departments = departments;
    }

    public List<IdNamePair> getTeams() {
        return teams;
    }

    public void setTeams(List<IdNamePair> teams) {
        this.teams = teams;
    }

    public IdNamePair getFirstSubsidiary(){
        return getSubsidiaries().isEmpty() ? null : getSubsidiaries().get(0);
    }

    public IdNamePair getFirstLocation(){
        return getLocations().isEmpty() ? null : getLocations().get(0);
    }
    
    public String getSapNumber() {
		return sapNumber;
	}

	public void setSapNumber(String sapNumber) {
		this.sapNumber = sapNumber;
	}

	public String getOffice() {
		return office;
	}

	public void setOffice(String office) {
		this.office = office;
	}

	public String getMobileNumber() {
		return mobileNumber;
	}

	public void setMobileNumber(String mobileNumber) {
		this.mobileNumber = mobileNumber;
	}

	public String getThumbnailPath() {
		return thumbnailPath;
	}

	public void setThumbnailPath(String thumbnailPath) {
		this.thumbnailPath = thumbnailPath;
	}

	@Override
    public boolean equals(Object obj) {
        if (!(obj instanceof User)) {
            return false;
        }
        User user = (User) obj;
        return new EqualsBuilder()
                .append(username, user.username)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 39)
                .append(username)
                .toHashCode();
    }

    public static User createFromJwtMap(Map<String, Object> map) {
        User user = new User();
        user.setId(Optional.ofNullable(map.get("id")).map(String::valueOf).map(Long::valueOf).orElse(null));
        user.setUsername(Optional.ofNullable(map.get("username")).map(String::valueOf).orElse(null));
        user.setDisplayName(Optional.ofNullable(map.get("displayName")).map(String::valueOf).orElse(null));
        user.setEmail(Optional.ofNullable(map.get("email")).map(String::valueOf).orElse(null));
        user.setPhoneNumber(Optional.ofNullable(map.get("phoneNumber")).map(String::valueOf).orElse(null));
        user.setTimeZoneId(Optional.ofNullable(map.get("timeZoneId")).map(String::valueOf).orElse(null));
        user.setSapNumber(Optional.ofNullable(map.get("sapNumber")).map(String::valueOf).orElse(null));
        user.setOffice(Optional.ofNullable(map.get("office")).map(String::valueOf).orElse(null));
        user.setMobileNumber(Optional.ofNullable(map.get("mobileNumber")).map(String::valueOf).orElse(null));
        user.setThumbnailPath(Optional.ofNullable(map.get("thumbnailPath")).map(String::valueOf).orElse(null));
        user.setSubsidiaries(getCollection(map, "subsidiaries"));
        user.setLocations(getCollection(map, "locations"));
        user.setDepartments(getCollection(map, "departments"));
        user.setTeams(getCollection(map, "teams"));
        user.setClientId(Optional.ofNullable(map.get("clientId")).map(String::valueOf).orElse(null));

        return user;
    }

    private static List<IdNamePair> getCollection(Map<String, Object> map, String key){
        List<IdNamePair> result = new ArrayList<>();
        if(map.get(key) != null) {
            List<Map<String, Object>> data = (List<Map<String, Object>>) map.get(key);
            result = data.stream().map(IdNamePair::fromMap).collect(Collectors.toList());
        }
        return result;
    }
}
