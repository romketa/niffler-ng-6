package guru.qa.niffler.api;

import guru.qa.niffler.model.CategoryJson;
import guru.qa.niffler.model.CurrencyValues;
import guru.qa.niffler.model.SpendJson;
import java.util.List;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface SpendApi {

  @POST("internal/spends/add")
  Call<SpendJson> addSpend(@Body SpendJson spend);

  @GET("internal/spends/{id}")
  Call<SpendJson> getSpendById(@Path("id") String id, @Query("username") String username);

  @GET("internal/spends/all")
  Call<List<SpendJson>> getAllSpend(@Query("username") String username,
      @Query("filterCurrency") CurrencyValues filterCurrency,
      @Query("from") String from,
      @Query("to") String to);

  @PATCH("internal/spends/edit")
  Call<SpendJson> editSpend(@Body SpendJson spend);

  @DELETE("internal/spends/remove")
  Call<SpendJson> deleteSpend(@Query("username") String username, @Query("ids") List<String> ids);

  @GET("internal/categories/all")
  Call<CategoryJson> getCategories(@Query("username") String username,
      @Query("excludeArchived") boolean excludeArchived);

  @POST("internal/categories/add")
  Call<CategoryJson> addCategories(@Body CategoryJson category);

  @PATCH("internal/categories/update")
  Call<CategoryJson> updateCategory(@Body CategoryJson category);
}
