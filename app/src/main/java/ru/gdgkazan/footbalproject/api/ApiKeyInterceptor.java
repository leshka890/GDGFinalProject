package ru.gdgkazan.footbalproject.api;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;
import ru.gdgkazan.footbalproject.BuildConfig;

/**
 * Created by Sergei Riabov
 */
public class ApiKeyInterceptor implements Interceptor {
    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();
        request = request.newBuilder().addHeader("X-Auth-Token", BuildConfig.API_KEY).build();
        return chain.proceed(request);
    }
}
