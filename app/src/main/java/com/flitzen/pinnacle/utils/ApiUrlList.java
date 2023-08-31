package com.flitzen.pinnacle.utils;

import com.flitzen.pinnacle.inventory_management.model.CrateItemListModel;
import com.flitzen.pinnacle.inventory_management.model.CratesListInternalMovementsModel;
import com.flitzen.pinnacle.inventory_management.model.CratesListModel;
import com.flitzen.pinnacle.inventory_management.model.DashBoardDataModel;
import com.flitzen.pinnacle.inventory_management.model.DeliveredOrderListModel;
import com.flitzen.pinnacle.inventory_management.model.InternalMovementItemListModel;
import com.flitzen.pinnacle.inventory_management.model.InternalMovementListModel;
import com.flitzen.pinnacle.inventory_management.model.JobWorkStatusCount;
import com.flitzen.pinnacle.inventory_management.model.LocationModel;
import com.flitzen.pinnacle.inventory_management.model.LoginModel;
import com.flitzen.pinnacle.inventory_management.model.PendingOrderBOMListModel;
import com.flitzen.pinnacle.inventory_management.model.PendingOrderListModel;
import com.flitzen.pinnacle.inventory_management.model.CommonModel;
import com.flitzen.pinnacle.inventory_management.model.StockAlertListModel;
import com.flitzen.pinnacle.inventory_management.model.StockCheckPartsListModel;

import java.util.Map;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.QueryMap;

public interface ApiUrlList {

    // Login api
    @FormUrlEncoded
    @POST("users/login")
    Call<LoginModel> loginApi(@Field("api_key") String api_key , @Field("email") String email , @Field("password") String password);

    //Logout api
    @FormUrlEncoded
    @POST("users/logout")
    Call<CommonModel> logoutApi(@Field("api_key") String api_key , @Field("user_id") String user_id);

    //Pin verify api
    @FormUrlEncoded
    @POST("users/verify_pin")
    Call<CommonModel> pinApi(@Field("api_key") String api_key , @Field("user_id") String user_id , @Field("user_pin") String user_pin);

    //Pending order list api
    @FormUrlEncoded
    @POST("order/pending")
    Call<PendingOrderListModel> pendingOrderListApi(@Field("api_key") String api_key);

    //Delivered order list api
    @FormUrlEncoded
    @POST("order/deliver")
    Call<DeliveredOrderListModel> deliveredOrderListApi(@Field("api_key") String api_key);

    //Pending order BOM list api
    @FormUrlEncoded
    @POST("order/bom")
    Call<PendingOrderBOMListModel> pendingOrderBOMListApi(@Field("api_key") String api_key, @Field("order_id") String order_id, @Field("machine_id") String machine_id);

    //Crates list api
    @FormUrlEncoded
    @POST("order/get_bom_crates")
    Call<CratesListModel> cratesListApi(@Field("api_key") String api_key, @Field("product_id") String product_id);

    //Add BOM
    @FormUrlEncoded
    @POST("order/add_bom")
    Call<CommonModel> addBOMApi(@Field("api_key") String api_key, @Field("order_id") String order_id, @Field("machine_id") String machine_id, @Field("bom_id") String bom_id, @Field("product_id") String product_id,
                                @Field("user_id") String user_id, @Field("qty") String qty, @Field("crate_id") String crate_id);

    //get products of category List
    @FormUrlEncoded
    @POST("stock/get_products")
    Call<StockCheckPartsListModel> getPartsListApi(@Field("api_key") String api_key, @Field("category_id") String category_id);

    //Location List
    @FormUrlEncoded
    @POST("internal_movement/get_location")
    Call<LocationModel> getLocationListApi(@Field("api_key") String api_key);

    //Crates List
    @FormUrlEncoded
    @POST("internal_movement/get_crates")
    Call<CratesListInternalMovementsModel> getCratesListApi(@Field("api_key") String api_key, @Field("location_id") String location_id);

    //Internal movement List
    @FormUrlEncoded
    @POST("internal_movement/all")
    Call<InternalMovementListModel> getInternalMovementsListApi(@Field("api_key") String api_key);

    //Internal movement Item List
    @FormUrlEncoded
    @POST("internal_movement/items")
    Call<InternalMovementItemListModel> getInternalMovementsItemListApi(@Field("api_key") String api_key,@Field("movement_id") String movement_id );

    //Crate Item List
    @FormUrlEncoded
    @POST("internal_movement/get_crate_data")
    Call<CrateItemListModel> getCrateItemListApi(@Field("api_key") String api_key, @Field("crate_id") String crate_id );

    //Move items from one crate to other crate
    @GET("internal_movement/move_crate_data")
    Call<CommonModel> moveItems(@QueryMap Map<String, String> params);

    //Get All job status count
    @FormUrlEncoded
    @POST("jobwork/status_total")
    Call<JobWorkStatusCount> getJobWorkStatusCountApi(@Field("api_key") String api_key);

    //Crate Item List
    @FormUrlEncoded
    @POST("internal_movement/get_crate_data")
    Call<StockCheckPartsListModel> getCrateItemListApi1(@Field("api_key") String api_key, @Field("crate_id") String crate_id );

    //Get Stock alert list
    @FormUrlEncoded
    @POST("stock/alert")
    Call<StockAlertListModel> getStockAlertListApi(@Field("api_key") String api_key);

    //Get Dash Board Data
    @FormUrlEncoded
    @POST("dashboard/data")
    Call<DashBoardDataModel> getDashBoardData(@Field("api_key") String api_key);

    //Get Shortage Data List
    @FormUrlEncoded
    @POST("order/bom_shortage")
    Call<PendingOrderBOMListModel> getShortageBOMList(@Field("api_key") String api_key, @Field("order_id") String order_id, @Field("machine_id") String machine_id);
}