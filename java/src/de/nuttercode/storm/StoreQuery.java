package de.nuttercode.storm;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * 
 * This class represents a query for {@link StoreItem}s in a
 * {@link de.nuttercode.storm.Store}. A query can be run as often as needed.
 * Changes to the {@link Store} will not be reflected in this query. This means
 * especially that changes to the {@link Store} may render this query invalid.
 * Changes to the query can be made by calling the intermediate functions. This
 * class is not thread-safe.
 * 
 * @author Johannes B. Latzel
 *
 * @param <T> the type of the content of items in the
 *        {@link de.nuttercode.storm.Store}
 */
public class StoreQuery<T> {

	/**
	 * all filters on {@link StoreItem#getID()}
	 */
	private final List<Predicate<Long>> storeIDFilterList;

	/**
	 * all filters on {@link StoreItem#getContent()}
	 */
	private final List<Predicate<T>> contentFilterList;

	/**
	 * the {@link Store} on which this query will be run
	 */
	private final Store<T> store;

	/**
	 * 
	 */
	private final Set<Long> storeIDSet;

	/**
	 * used by {@link Store#query()}. don't use this constructor manually. always
	 * call {@link Store#query()} instead.
	 * 
	 * @param store
	 * @param storeIDSet
	 */
	public StoreQuery(Store<T> store, Set<Long> storeIDSet) {
		assert (store != null);
		assert (storeIDSet != null);
		this.storeIDSet = storeIDSet;
		this.store = store;
		storeIDFilterList = new ArrayList<>();
		contentFilterList = new ArrayList<>();
	}

	/**
	 * tests if h satisfies the filterList
	 * 
	 * @param filterList
	 * @param h          some element
	 * @return true if h satisfies the filterList
	 */
	private <H> boolean evaluateH(List<Predicate<H>> filterList, H h) {
		boolean test = true;
		for (int a = 0; a < filterList.size() && test; a++) {
			test &= filterList.get(a).test(h);
		}
		return test;
	}

	/**
	 * @param storeID
	 * @return true if storeID satisfies the #storeIDFilterList
	 */
	private boolean evaluateStoreID(long storeID) {
		return evaluateH(storeIDFilterList, storeID);
	}

	/**
	 * @param content
	 * @return true if content satisfies the #contentFilterList
	 */
	private boolean evaluateContent(T content) {
		return evaluateH(contentFilterList, content);
	}

	/**
	 * intermediate operation - filters items in respect to their storeID
	 * 
	 * @param storeIDFilter
	 * @return this
	 */
	public StoreQuery<T> whereID(Predicate<Long> storeIDFilter) {
		storeIDFilterList.add(storeIDFilter);
		return this;
	}

	/**
	 * intermediate operation - filters items in respect to their content
	 * 
	 * @param contentFilter
	 * @return this
	 */
	public StoreQuery<T> whereContent(Predicate<T> contentFilter) {
		contentFilterList.add(contentFilter);
		return this;
	}

	/**
	 * terminal operation
	 * 
	 * @return first item which matches all filters or null if none matches
	 * @throws IOException when thrown by the {@link de.nuttercode.storm.Store}
	 */
	public T first() throws IOException {
		T content;
		for (long storeID : storeIDSet) {
			if (!evaluateStoreID(storeID))
				break;
			content = store.get(storeID).getContent();
			if (evaluateContent(content))
				return content;
		}
		return null;
	}

	/**
	 * terminal operation
	 * 
	 * @return last item which matches all filters or null if none matches
	 * @throws IOException when thrown by the {@link de.nuttercode.storm.Store}
	 */
	public T last() throws IOException {
		T content;
		T last = null;
		for (long storeID : storeIDSet) {
			if (!evaluateStoreID(storeID))
				break;
			content = store.get(storeID).getContent();
			if (evaluateContent(content))
				last = content;
		}
		return last;
	}

	/**
	 * terminal operation
	 * 
	 * @return a {@link Set} of all items which match all filters
	 * @throws IOException when thrown by the {@link de.nuttercode.storm.Store}
	 */
	public Set<StoreItem<T>> all() throws IOException {
		Set<StoreItem<T>> itemSet = new HashSet<>();
		StoreItem<T> item;
		for (long storeID : storeIDSet) {
			if (evaluateStoreID(storeID)) {
				item = store.get(storeID);
				if (evaluateContent(item.getContent()))
					itemSet.add(item);
			}
		}
		return itemSet;
	}

	/**
	 * terminal operation
	 * 
	 * @return {@link #all()} mapped to its {@link StoreItem#getContent()}
	 * @throws IOException when {@link #all()} does
	 */
	public Set<T> allContent() throws IOException {
		return all().parallelStream().map(i -> i.getContent()).collect(Collectors.toSet());
	}

}
