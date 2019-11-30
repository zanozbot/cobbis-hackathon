package si.zanozbot.cobbishack;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Headers;

public interface CobbisService {

    @Headers({
            "apikey: 9e0f071c",
            "token: fa92-40d1-86d5-0c5791ffb3b0"
    })
    @GET("start")
    Call<CobbisModel> start();
}
