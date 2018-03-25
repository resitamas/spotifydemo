package hu.bme.aut.android.spotifydemo.network;

import hu.bme.aut.android.spotifydemo.model.ArtistsResult;
import hu.bme.aut.android.spotifydemo.model.Credentials;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface ArtistsApi {
    @GET("search")
    Call<ArtistsResult> getArtists(@Query("query") String artist,
                                   @Query("type") String type,
                                   @Query("offset") int offset,
                                   @Query("limit") int limit);
}
