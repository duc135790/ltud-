package com.example.foodorderapp;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import java.util.ArrayList;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeFragment extends Fragment {

    private ListView lvMenu;
    private EditText etSearch;
    private List<MenuItemApi> menuList = new ArrayList<>();
    private ApiService apiService;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        lvMenu   = view.findViewById(R.id.lv_menu);
        etSearch = view.findViewById(R.id.et_search);
        TextView tvGreeting = view.findViewById(R.id.tv_greeting);
        apiService = ApiClient.getClient().create(ApiService.class);

        SharedPreferences prefs = requireActivity()
                .getSharedPreferences("user_prefs", requireActivity().MODE_PRIVATE);
        String fullname = prefs.getString("fullname", "bạn");
        tvGreeting.setText("Xin chào, " + fullname + "! 👋");

        loadMenu(null);

        etSearch.addTextChangedListener(new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override public void afterTextChanged(Editable s) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String keyword = s.toString().trim();
                loadMenu(keyword.isEmpty() ? null : keyword);
            }
        });

        lvMenu.setOnItemClickListener((parent, v, position, id) -> {
            MenuItemApi item = menuList.get(position);
            CartManager.getInstance().addApiItem(item);
            Toast.makeText(requireContext(), "✅ Đã thêm " + item.getName(), Toast.LENGTH_SHORT).show();
        });
    }

    private void loadMenu(String search) {
        apiService.getMenu(search).enqueue(new Callback<ApiResponse<List<MenuItemApi>>>() {
            @Override
            public void onResponse(Call<ApiResponse<List<MenuItemApi>>> call,
                                   Response<ApiResponse<List<MenuItemApi>>> response) {
                if (response.isSuccessful() && response.body() != null && response.body().isSuccess()) {
                    menuList = response.body().getData();
                    MenuItemApiAdapter adapter = new MenuItemApiAdapter(
                            requireContext(), menuList, null, null);
                    lvMenu.setAdapter(adapter);
                }
            }
            @Override
            public void onFailure(Call<ApiResponse<List<MenuItemApi>>> call, Throwable t) {
                Toast.makeText(requireContext(), "Lỗi tải menu!", Toast.LENGTH_SHORT).show();
            }
        });
    }
}