package smart.tracking.service;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface TrackingServiceAPI {

    @POST("tracking/SendLocation")
    Call<MyLocation> save(@Body MyLocation tst);
}
