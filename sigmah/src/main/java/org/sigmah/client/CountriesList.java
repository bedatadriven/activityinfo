package org.sigmah.client;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.sigmah.shared.dto.CountryDTO;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.inject.Singleton;

/**
 * Utility class to cache the countries list on the client-side.
 * 
 * @author tmi
 * 
 */
@Singleton
public class CountriesList {

    private static final class CountryAsyncCallback {

        private final int id;
        private final AsyncCallback<CountryDTO> callback;

        private CountryAsyncCallback(int id, AsyncCallback<CountryDTO> callback) {
            this.id = id;
            this.callback = callback;
        }
    }

    private static final class CountriesAsyncCallback {

        private final AsyncCallback<List<CountryDTO>> callback;

        private CountriesAsyncCallback(AsyncCallback<List<CountryDTO>> callback) {
            this.callback = callback;
        }
    }

    /**
     * Countries list.
     */
    private final HashMap<Integer, CountryDTO> countriesMap;
    private final ArrayList<CountryDTO> countriesList;
    private boolean countriesHaveBeenSet;

    private final ArrayList<CountryAsyncCallback> queueCountry;
    private final ArrayList<CountriesAsyncCallback> queueCountries;

    public CountriesList() {
        countriesMap = new HashMap<Integer, CountryDTO>();
        countriesList = new ArrayList<CountryDTO>();
        countriesHaveBeenSet = false;
        queueCountry = new ArrayList<CountryAsyncCallback>();
        queueCountries = new ArrayList<CountriesAsyncCallback>();
    }

    private CountryDTO getInternalCountry(int id) {
        return countriesMap.get(id);
    }

    private List<CountryDTO> getInternalCountries() {
        return countriesList;
    }

    /**
     * Tries to get a country without waiting.
     * 
     * @param id
     *            The country id.
     * @return The country if the cache has been set, <code>null</code>
     *         otherwise.
     */
    public CountryDTO getCountry(int id) {

        if (countriesHaveBeenSet) {
            return getInternalCountry(id);
        } else {
            return null;
        }
    }

    /**
     * Gets the country with the given id. If the countries list isn't available
     * immediately, the callback will be called after the list has been set by
     * the first server call.
     * 
     * @param id
     *            The country id.
     * @param callback
     *            The callback.
     */
    public void getCountry(int id, AsyncCallback<CountryDTO> callback) {

        // If the countries list is available, returns the country immediately.
        if (countriesHaveBeenSet) {
            callback.onSuccess(getInternalCountry(id));
        }
        // Else put the callback in queue to be called later.
        else {
            queueCountry.add(new CountryAsyncCallback(id, callback));
        }
    }

    /**
     * Gets the countries list with the given id. If the countries list isn't
     * available immediately, the callback will be called after the list has
     * been set by the first server call.
     * 
     * @param callback
     *            The callback.
     */
    public void getCountries(AsyncCallback<List<CountryDTO>> callback) {

        // If the countries list is available, returns the list immediately.
        if (countriesHaveBeenSet) {
            callback.onSuccess(getInternalCountries());
        }
        // Else put the callback in queue to be called later.
        else {
            queueCountries.add(new CountriesAsyncCallback(callback));
        }
    }

    /**
     * Sets the countries list and call all waiting jobs.
     * 
     * @param countries
     *            The countries list.
     */
    public void setCountries(List<CountryDTO> countries) {

        countriesList.addAll(countries);
        for (final CountryDTO country : countries) {
            this.countriesMap.put(country.getId(), country);
        }

        for (final CountryAsyncCallback pair : queueCountry) {
            pair.callback.onSuccess(getInternalCountry(pair.id));
        }

        for (final CountriesAsyncCallback pair : queueCountries) {
            pair.callback.onSuccess(getInternalCountries());
        }

        // Clears the queues.
        queueCountry.clear();
        queueCountries.clear();

        countriesHaveBeenSet = true;
    }
}
