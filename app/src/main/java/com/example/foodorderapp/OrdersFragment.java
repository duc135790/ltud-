package com.example.foodorderapp;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OrdersFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_orders, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        SharedPreferences prefs = requireActivity()
                .getSharedPreferences("user_prefs", Context.MODE_PRIVATE);
        String username = prefs.getString("username", "");

        ListView lvOrders = view.findViewById(R.id.lv_orders);
        ApiService apiService = ApiClient.getClient().create(ApiService.class);
        NumberFormat fmt = NumberFormat.getInstance(new Locale("vi", "VN"));

        apiService.getOrders(username).enqueue(new Callback<ApiResponse<List<OrderApi>>>() {
            @Override
            public void onResponse(Call<ApiResponse<List<OrderApi>>> call,
                                   Response<ApiResponse<List<OrderApi>>> response) {
                if (response.isSuccessful() && response.body() != null && response.body().isSuccess()) {
                    List<OrderApi> orders = response.body().getData();

                    ArrayAdapter<OrderApi> adapter = new ArrayAdapter<OrderApi>(
                            requireContext(), R.layout.item_order, orders) {
                        @NonNull
                        @Override
                        public View getView(int position, View convertView, @NonNull ViewGroup parent) {
                            if (convertView == null) {
                                convertView = LayoutInflater.from(getContext())
                                        .inflate(R.layout.item_order, parent, false);
                            }
                            OrderApi order = orders.get(position);
                            ((TextView) convertView.findViewById(R.id.tv_order_id))
                                    .setText("Đơn #" + order.getId());
                            ((TextView) convertView.findViewById(R.id.tv_order_items))
                                    .setText(order.getItems());
                            ((TextView) convertView.findViewById(R.id.tv_order_total))
                                    .setText(fmt.format(order.getTotal()) + "đ");
                            ((TextView) convertView.findViewById(R.id.tv_order_date))
                                    .setText(order.getCreatedAt());

                            TextView tvStatus = convertView.findViewById(R.id.tv_order_status);
                            tvStatus.setText(order.getStatus());
                            switch (order.getStatus()) {
                                case "pending":
                                    tvStatus.setBackgroundColor(0xFFFF9800);
                                    tvStatus.setText("⏳ Chờ xử lý");
                                    break;
                                case "preparing":
                                    tvStatus.setBackgroundColor(0xFF2196F3);
                                    tvStatus.setText("🔥 Đang làm");
                                    break;
                                case "ready":
                                    tvStatus.setBackgroundColor(0xFF4CAF50);
                                    tvStatus.setText("✅ Sẵn sàng");
                                    break;
                                case "done":
                                    tvStatus.setBackgroundColor(0xFF9E9E9E);
                                    tvStatus.setText("✔ Hoàn thành");
                                    break;
                            }
                            return convertView;
                        }
                    };
                    lvOrders.setAdapter(adapter);
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<List<OrderApi>>> call, Throwable t) {
                Toast.makeText(requireContext(), "Lỗi tải đơn hàng!", Toast.LENGTH_SHORT).show();
            }
        });
    }
}