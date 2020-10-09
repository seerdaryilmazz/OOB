package ekol.kartoteks.service;

import ekol.kartoteks.domain.Company;
import ekol.kartoteks.domain.CompanyLocation;
import ekol.kartoteks.repository.CompanyLocationRepository;
import ekol.kartoteks.repository.CompanyRepository;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

/**
 * Created by kilimci on 27/04/2017.
 */
@Component
public class CompanyNameGenerator {

    @Autowired
    private CompanyRepository companyRepository;

    @Autowired
    private CompanyLocationRepository companyLocationRepository;

    public static final int SHORT_NAME_LENGTH = 30;
    public static final int LOCATION_SHORT_NAME_LENGTH = 30;
    private static final int FIRST_WORD_MIN_LENGTH = 5;

    public String generateShortName(Long companyId, String name){
        if(StringUtils.isBlank(name) || name.length() < FIRST_WORD_MIN_LENGTH){
            return name;
        }
        String nameUpper = name.toUpperCase();
        StringBuilder result = new StringBuilder(SHORT_NAME_LENGTH);
        String[] words = StringUtils.split(nameUpper);
        int currentIndex = 0;
        if(words.length == 1){
            result.append(words[0]);
            currentIndex = 1;
        }else {
            result = result.append(words[0]);
            currentIndex = 1;
            if (result.length() < FIRST_WORD_MIN_LENGTH) {
                result.append(" ").append(words[1]);
                currentIndex = 2;
            }
        }
        if(findCompanyByShortNameAndId(result.toString(), companyId) == null){
            return result.toString();
        }

        for(int i = currentIndex; i < words.length; i++){
            int lengthWithNextWord = result.toString().length() + words[i].length() + 1;
            if(lengthWithNextWord > SHORT_NAME_LENGTH){
                break;
            }
            result.append(" ").append(words[i]);
            if(findCompanyByShortNameAndId(result.toString(), companyId) == null){
                return result.toString();
            }
        }

        int sequence = 2;
        while(true){
            StringBuilder resultFromName = new StringBuilder(result);
            resultFromName.append(" ").append(sequence);
            if(findCompanyByShortNameAndId(resultFromName.toString(), companyId) == null){
                return resultFromName.toString();
            }
            sequence++;
        }
    }
    public String generateLocationShortName(Long companyLocationId, String companyShortName, String city, String district, String exclude){
        StringBuilder result = new StringBuilder(LOCATION_SHORT_NAME_LENGTH);
        List<String> excludeList = Arrays.asList(StringUtils.split(exclude, ","));
        result.append(companyShortName);
        if(StringUtils.isNotBlank(city)){
            int length = result.toString().length() + city.length() + 1;
            if(length < LOCATION_SHORT_NAME_LENGTH){
                result.append(" ").append(city.toUpperCase());
                if(!excludeList.contains(result.toString()) && findLocationByNameAndId(result.toString(), companyLocationId).isEmpty()){
                    return result.toString();
                }else if(StringUtils.isNotBlank(district)){
                    length = result.toString().length() + district.length() + 1;
                    if(length < LOCATION_SHORT_NAME_LENGTH){
                        result.append(" ").append(district.toUpperCase());
                        if(!excludeList.contains(result.toString()) && findLocationByNameAndId(result.toString(), companyLocationId).isEmpty()){
                            return result.toString();
                        }
                    }
                }
            }
        }
        if(!excludeList.contains(result.toString()) && findLocationByNameAndId(result.toString(), companyLocationId).isEmpty()){
            return result.toString();
        }
        int sequence = 2;
        while(true){
            StringBuilder resultFromName = new StringBuilder(result);
            resultFromName.append(" ").append(sequence);
            if(!excludeList.contains(resultFromName.toString()) && findLocationByNameAndId(resultFromName.toString(), companyLocationId).isEmpty()){
                return resultFromName.toString();
            }
            sequence++;
        }
    }

    private List<CompanyLocation> findLocationByNameAndId(String name, Long id){
        if(id == null){
            return companyLocationRepository.findByName(name);
        }else{
            return companyLocationRepository.findByNameAndIdNot(name, id);
        }
    }

    private Company findCompanyByShortNameAndId(String name, Long id){
        if(id == null){
            return companyRepository.findByShortName(name);
        }else{
            return companyRepository.findByShortNameAndIdNot(name, id);
        }
    }

}
