package ekol.orders.search.service;

import ekol.orders.search.domain.SavedSearch;
import ekol.orders.search.repository.SavedSearchRepository;
import ekol.resource.oauth2.SessionOwner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by ozer on 24/10/16.
 */
@Service
public class SavedSearchService {

    @Value("${oneorder.user-service}")
    private String userServiceUrl;

    private SavedSearchRepository savedSearchRepository;
    private SessionOwner sessionOwner;


    @Autowired
    public SavedSearchService(SavedSearchRepository savedSearchRepository, SessionOwner sessionOwner){
        this.savedSearchRepository = savedSearchRepository;
        this.sessionOwner = sessionOwner;
    }

    public List<SavedSearch> getMySavedSearches() {
        return savedSearchRepository.findByUserId(sessionOwner.getCurrentUser().getId());
    }

    public SavedSearch getMySavedSearch(long id) {
        SavedSearch savedSearch = savedSearchRepository.findOne(id);
        if (savedSearch.getUserId().equals(sessionOwner.getCurrentUser().getId())) {
            return savedSearch;
        }
        return null;
    }

    public SavedSearch saveMySavedSearch(SavedSearch savedSearch) {
        if (savedSearch.getId() != null) {
            SavedSearch target = getMySavedSearch(savedSearch.getId());
            if (target != null) {
                target.setName(savedSearch.getName());
                target.setFilter(savedSearch.getFilter());
                return savedSearchRepository.save(target);
            }
        } else {
            savedSearch.setUserId(sessionOwner.getCurrentUser().getId());
            return savedSearchRepository.save(savedSearch);
        }

        return null;
    }

    public void deleteMySavedSearch(long id) {
        SavedSearch savedSearch = getMySavedSearch(id);
        if (savedSearch != null) {
            savedSearch.setDeleted(true);
            savedSearchRepository.save(savedSearch);
        }
    }
}
