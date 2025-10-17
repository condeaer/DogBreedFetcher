package dogapi;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.*;

/**
 * BreedFetcher implementation that relies on the dog.ceo API.
 * Note that all failures get reported as BreedNotFoundException
 * exceptions to align with the requirements of the BreedFetcher interface.
 */
public class DogApiBreedFetcher implements BreedFetcher {
    /**
     * Fetch the list of sub breeds for the given breed from the dog.ceo API.
     * @param breed the breed to fetch sub breeds for
     * @return list of sub breeds for the given breed
     * @throws BreedNotFoundException if the breed does not exist (or if the API call fails for any reason)
     */
    @Override
    public List<String> getSubBreeds(String breed) {

        final OkHttpClient client = new OkHttpClient();
        final String url = "https://dog.ceo/api/breed/" + breed + "/list";
        final Request request = new Request.Builder().url(url).build();

        try {
            Response response = client.newCall(request).execute();
            final JSONObject jsonResponse = new JSONObject(response.body().string());

            if (jsonResponse.getString("status").equals("success")) {
                final JSONArray subBreedsJson = jsonResponse.getJSONArray("message");
                final List<String> subBreeds = new ArrayList<>();

                for (int i = 0; i < subBreedsJson.length(); i++) {
                    subBreeds.add(subBreedsJson.getString(i));
                }
                return subBreeds;
            } else {
                throw new BreedNotFoundException(jsonResponse.getString("message"));
            }
        } catch (IOException | JSONException e) {
            throw new RuntimeException(e);
        }
    }
    }
