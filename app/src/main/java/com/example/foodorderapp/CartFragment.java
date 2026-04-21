package com.example.foodorderapp;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import java.text.NumberFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CartFragment extends Fragment {

    private ListView lvCart;
    private TextView tvTotal;
    private List<MenuItemApi> cartItems;
    private ApiService apiService;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_cart, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        lvCart  = view.findViewById(R.id.lv_cart);
        tvTotal = view.findViewById(R.id.tv_total);
        Button btnOrder = view.findViewById(R.id.btn_order);
        apiService = ApiClient.getClient().create(ApiService.class);

        loadCart();

        btnOrder.setOnClickListener(v -> placeOrder());
    }

    private void loadCart() {
        cartItems = CartManager.getInstance().getItems();
        NumberFormat fmt = NumberFormat.getInstance(new Locale("vi", "VN"));

        ArrayAdapter<MenuItemApi> adapter = new ArrayAdapter<MenuItemApi>(
                requireContext(), R.layout.item_cart, cartItems) {
            @NonNull
            @Override
            public View getView(int position, View convertView, @NonNull ViewGroup parent) {
                if (convertView == null) {
                    convertView = LayoutInflater.from(getContext())
                            .inflate(R.layout.item_cart, parent, false);
                }
                MenuItemApi item = cartItems.get(position);
                ((TextView) convertView.findViewById(R.id.tv_name)).setText(item.getName());
                ((TextView) convertView.findViewById(R.id.tv_price))
                        .setText(fmt.format(item.getPrice()) + "đ");
                convertView.findViewById(R.id.btn_remove).setOnClickListener(v -> {
                    CartManager.getInstance().getItems().remove(position);
                    loadCart();
                });
                return convertView;
            }
        };

        lvCart.setAdapter(adapter);
        tvTotal.setText(fmt.format(CartManager.getInstance().getTotal()) + "đ");
    }

    private void placeOrder() {
        if (CartManager.getInstance().getCount() == 0) {
            Toast.makeText(requireContext(), "Giỏ hàng trống!", Toast.LENGTH_SHORT).show();
            return;
        }

        SharedPreferences prefs = requireActivity()
                .getSharedPreferences("user_prefs", Context.MODE_PRIVATE);
        String username = prefs.getString("username", "");

        Map<String, Object> body = new HashMap<>();
        body.put("username", username);
        body.put("items", CartManager.getInstance().getItemsSummary());
        body.put("total", CartManager.getInstance().getTotal());

        apiService.createOrder(body).enqueue(new Callback<ApiResponse<Map<String, Integer>>>() {
            @Override
            public void onResponse(Call<ApiResponse<Map<String, Integer>>> call,
                                   Response<ApiResponse<Map<String, Integer>>> response) {
                if (response.isSuccessful() && response.body() != null && response.body().isSuccess()) {
                    CartManager.getInstance().clear();
                    loadCart();
                    Toast.makeText(requireContext(), "✅ Đặt món thành công!", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(requireContext(), "Lỗi đặt món!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<Map<String, Integer>>> call, Throwable t) {
                Toast.makeText(requireContext(), "Lỗi kết nối server!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        loadCart();
    }
}