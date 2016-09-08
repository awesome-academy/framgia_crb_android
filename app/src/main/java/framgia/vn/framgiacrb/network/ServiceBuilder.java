package framgia.vn.framgiacrb.network;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.concurrent.TimeUnit;

import framgia.vn.framgiacrb.constant.Constant;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by nghicv on 18/07/2016.
 */
public class ServiceBuilder {
    public static final int READ_TIMEOUT = 5;
    public static final int REQUEST_TIMEOUT = 2;

    private static Retrofit sInstance;
    private static CrbService sService;

    private static Retrofit getRetrofit() {
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient client = new OkHttpClient.Builder().addInterceptor(interceptor)
                .connectTimeout(REQUEST_TIMEOUT, TimeUnit.SECONDS)
                .readTimeout(READ_TIMEOUT, TimeUnit.SECONDS)
                .build();
        Gson gson = new GsonBuilder()
                .setDateFormat("yyyy-MM-dd HH:mm")
                .create();
        if (sInstance == null) {
            sInstance = new Retrofit.Builder()
                    .baseUrl(Constant.BASE_URL)
                    .client(client)
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .build();
        }
        return sInstance;
    }

    public static CrbService getService() {
        if (sService == null) {
            sService = getRetrofit().create(CrbService.class);
        }
        return sService;
    }
}