package ekol.usermgr.service;

import java.util.*;
import java.util.function.Supplier;
import java.util.stream.*;

import javax.annotation.PostConstruct;
import javax.transaction.Transactional;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.slf4j.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.*;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.security.oauth2.client.OAuth2RestTemplate;
import org.springframework.stereotype.Service;

import ekol.usermgr.common.Constants;
import ekol.usermgr.domain.*;
import ekol.usermgr.dto.AuthorizedMenu;
import ekol.usermgr.repository.UIMenuRepository;

/**
 * Created by burak on 17/08/16.
 */
@Service
public class UIMenuService {
	private static final Logger LOGGER = LoggerFactory.getLogger(UIMenuService.class);
	
	@Autowired
	private UIMenuRepository uiMenuRepository;

	@Autowired
	private OAuth2RestTemplate restTemplate;
	
	@Autowired
	private CacheManager cacheManager;

	@Value("${oneorder.authorization-service}")
	private String authorizationService;
	
	@PostConstruct
	private void init() {
		clearCacheUserMenu();
	}
	
	private void clearCacheUserMenu() {
		cacheManager.getCache(Constants.CACHE_NAME_USER_MENU).clear();
		if(LOGGER.isWarnEnabled()) {
			LOGGER.warn("All user menus are cleared from cache");
		}
	}

	public Iterable<UIMenu> list() {
		return uiMenuRepository.findAllByOrderByRankAsc();
	}

	@CacheEvict(cacheNames = Constants.CACHE_NAME_USER_MENU, allEntries = true)
	public UIMenu save(UIMenu uiMenu) {
		int siblingCount = uiMenuRepository.findAllByParentOrderByRankAsc(uiMenu.getParent()).size();
		siblingCount++;
		uiMenu.setRank(siblingCount);
		return uiMenuRepository.save(uiMenu);
	}
	
	@CacheEvict(cacheNames = Constants.CACHE_NAME_USER_MENU, allEntries = true)
	public UIMenu update(UIMenu uiMenu) {
		return uiMenuRepository.save(uiMenu);
	}

	@Transactional
	@CacheEvict(cacheNames = Constants.CACHE_NAME_USER_MENU, allEntries = true)
	public void changeRank(UIMenu menu, UIMenu parent, Integer rank){
		boolean parentHasChanged = !new EqualsBuilder().append(
						Optional.ofNullable(menu.getParent()).map(UIMenu::getId).orElse(null), 
						Optional.ofNullable(parent).map(UIMenu::getId).orElse(null))
				.isEquals();
		
		UIMenu oldParent = menu.getParent();
		menu.setParent(parent);

		if(parentHasChanged){
			//if parent has changed insert item into new siblings
			List<UIMenu> newSiblings = uiMenuRepository.findAllByParentOrderByRankAsc(parent);
			menu.setRank(rank);
			List<UIMenu> newSiblingsOrdered = newSiblings.stream().sorted((first, second) -> {
				if(first.getRank().equals(second.getRank())){
					return first.getId().equals(menu.getId()) ? -1 : 1;
				}
				return first.getRank().compareTo(second.getRank());
			}).collect(Collectors.toList());
			updateRanks(newSiblingsOrdered);

			List<UIMenu> oldSiblings = uiMenuRepository.findAllByParentOrderByRankAsc(oldParent);
			updateRanks(oldSiblings);

		}else{
			//if menu item moved upper new rank < old rank and rankCompared < 0
			//if menu item moved lower new rank > old rank and rankCompared > 0
			final int rankCompared = rank.compareTo(menu.getRank());

			if(rankCompared != 0){
				menu.setRank(rank);
				List<UIMenu> siblings = uiMenuRepository.findAllByParentOrderByRankAsc(parent);
				List<UIMenu> siblingsOrdered = siblings.stream().sorted((first, second) -> {
					if(first.getRank().equals(second.getRank())){
						return first.getId().equals(menu.getId()) ? rankCompared : -rankCompared;
					}
					return first.getRank().compareTo(second.getRank());
				}).collect(Collectors.toList());
				updateRanks(siblingsOrdered);
			}
		}
	}

	private void updateRanks(List<UIMenu> menuItems){
		int counter = 1;
		for(UIMenu sibling : menuItems){
			sibling.setRank(counter);
			uiMenuRepository.save(sibling);
			counter++;
		}
	}

	private void deleteTree(UIMenu uiMenu) {
		uiMenu.setDeleted(true);
		uiMenuRepository.save(uiMenu);
		uiMenu.getChildren().forEach(this::deleteTree);
	}
	
	@Transactional
	@CacheEvict(cacheNames = Constants.CACHE_NAME_USER_MENU, allEntries = true)
	public void delete(UIMenu uiMenu) {
		deleteTree(uiMenu);
		List<UIMenu> siblings = uiMenuRepository.findAllByParentOrderByRankAsc(uiMenu.getParent());
		updateRanks(siblings);
	}

	@Cacheable(cacheNames = Constants.CACHE_NAME_USER_MENU)
	public Iterable<MenuItem> getUserMenu(String username) {
		Set<UIMenu> menu = new LinkedHashSet<>();

		Set<Long> menuIds = getMenu(authorizationService + "/auth/menu-item/user/" + username).parallelStream().map(AuthorizedMenu::getExternalId).collect(Collectors.toSet());
		menu.addAll(getUIMenU(()->uiMenuRepository.findAll(menuIds)));

		Set<Long> allAuthorizedMenuIds = getMenu(authorizationService + "/auth/menu-item").parallelStream().map(AuthorizedMenu::getExternalId).collect(Collectors.toSet());
		menu.addAll(getUIMenU(()->uiMenuRepository.findAllByIdNotIn(allAuthorizedMenuIds)));
		
		Set<Long> noViewers = getMenu(authorizationService + "/auth/menu-item").parallelStream().filter(t->CollectionUtils.isEmpty(t.getViewers())).map(AuthorizedMenu::getExternalId).collect(Collectors.toSet());
		menu.addAll(getUIMenU(()->uiMenuRepository.findAllByIdIn(noViewers)));
		
		Set<UIMenu> parents = menu.parallelStream().filter(x->Objects.isNull(x.getParent())).collect(Collectors.toSet());
		return mapMenu(parents, menu);
	}
	
	private Set<MenuItem> mapMenu(Set<UIMenu> parents, Set<UIMenu> menu) {
		return Optional.ofNullable(parents)
				.map(Collection::stream)
				.orElseGet(Stream::empty)
				.map(item->mapMenuItem(item, menu))
				.filter(Objects::nonNull)
				.collect(Collectors.toCollection(TreeSet::new));
	}
	
	private MenuItem mapMenuItem(UIMenu uiMenu, Set<UIMenu> menu) {
		if(!menu.contains(uiMenu)) {
			return null;
		}
		MenuItem menuItem = new MenuItem();
		menuItem.setKey(UUID.randomUUID().toString());
		menuItem.setChildren(mapMenu(uiMenu.getChildren(), menu));
		menuItem.setName(uiMenu.getName());
		menuItem.setRank(uiMenu.getRank());
		menuItem.setUrl(uiMenu.getUrl());
		return menuItem;
	}

	private Set<UIMenu> getUIMenU(Supplier<Iterable<UIMenu>> supplier) {
		Set<UIMenu> menu = new LinkedHashSet<>();
		StreamSupport.stream(supplier.get().spliterator(), false)
				.filter(t -> StringUtils.isNotEmpty(t.getUrl()))
				.forEach(item -> {
					menu.add(item);
					UIMenu parent = item.getParent();
					while (parent != null) {
						menu.add(parent);
						parent = parent.getParent();
					}
				});
		return menu;
	}

	private List<AuthorizedMenu> getMenu(String path){
		return restTemplate.exchange(path,  HttpMethod.GET, null, new ParameterizedTypeReference<List<AuthorizedMenu>>() {}).getBody();
	}

}
