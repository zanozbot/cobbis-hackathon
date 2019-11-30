package si.zanozbot.cobisshackathon;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Path;

public interface CobissService {

    @Headers({
            "apikey: 9e0f071c",
            "token: fa92-40d1-86d5-0c5791ffb3b0"
    })
    @GET("start")
    Call<CobissModel> start();

    @Headers({
            "apikey: 9e0f071c",
            "token: fa92-40d1-86d5-0c5791ffb3b0"
    })
    @GET("stop")
    Call<CobissModel> stop();

    @Headers({
            "apikey: 9e0f071c",
            "token: fa92-40d1-86d5-0c5791ffb3b0"
    })
    @GET("scan/{number}")
    Call<CobissModel> scan(@Path("number") String number);
}
