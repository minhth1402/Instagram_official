package com.minhthieu.instagramofficial.Adapters;

import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.minhthieu.instagramofficial.Models.HomeRecyclerItem;
import com.minhthieu.instagramofficial.R;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class HomeRecyclerItemAdapter extends RecyclerView.Adapter<HomeRecyclerItemAdapter.HomeRecyclerItemViewHolder>{

    // ko cần initialized
    List<HomeRecyclerItem> homeRecyclerItems;


    // mỗi lần gọi adapter này, sẽ phải pass vào mấy cái này cho nó


    public HomeRecyclerItemAdapter(List<HomeRecyclerItem> homeRecyclerItems) {
        this.homeRecyclerItems = homeRecyclerItems;
    }

    @NonNull
    @NotNull
    @Override
    public HomeRecyclerItemViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        // cái dòng này có 1 ý nghĩa là lấy cái design (cái file xml của home recycler item) và pass vào đây, sau đó
        // sẽ return cho cái public HomeRecyclerItemViewHolder dưới kia
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.home_recycler_item, parent, false);

        HomeRecyclerItemViewHolder homeRecyclerItem = new HomeRecyclerItemViewHolder(view);

        return homeRecyclerItem;
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull HomeRecyclerItemAdapter.HomeRecyclerItemViewHolder holder, int position) {

        // đây là cái đoạn mà m sẽ gắn dữ liệu từ bên trong cái phần text (cái phần đc pass vào í) vào cái design

        // đầu tiên, m lấy cái item index 0 , rồi 1, rồi 2, thì cứ lấy index từng cái như v

        // dòng này, m pass cái dữ liệu từ home recycler item, là 1 list những cái home recycler item thôi, m bỏ vào 1 cái
        // HomeRecyclerItem (để lấy ra mà sài chứ, thằng homeRecyclerItems m pass vào ở dạng list, đ sài đc)
        HomeRecyclerItem LOCAL_homeRecyclerItem = homeRecyclerItems.get(position);

        // tiếp theo, m set cái cho mấy cái giá trị trong cái adapter này, như cái m có trong cái homeRecyclerItem,
        // là cái m pass vào

        // m set text như bthg thôi, m lấy string từ cái HomeRecyclerItem, t thêm chữ local để m hiểu rằng
        // cái này là index 1 (hay là 1 cái card ấy), trong cái list card m pass vào
        holder.userName.setText(LOCAL_homeRecyclerItem.getUserName());

        holder.fullName.setText(LOCAL_homeRecyclerItem.getFullName());

        holder.profileImage.setImageURI(Uri.parse(LOCAL_homeRecyclerItem.getImageUrl()));

        holder.demoImage1.setImageURI(Uri.parse(LOCAL_homeRecyclerItem.getDemoImageUrl().get(0)));
        holder.demoImage2.setImageURI(Uri.parse(LOCAL_homeRecyclerItem.getDemoImageUrl().get(1)));
        holder.demoImage3.setImageURI(Uri.parse(LOCAL_homeRecyclerItem.getDemoImageUrl().get(2)));



    }

    @Override
    public int getItemCount() {
        if(homeRecyclerItems.size() != 0){
            return homeRecyclerItems.size();
        }
        return 0;
    }

    // ================================= ++ ==============================================
    // cái class này là view
    public class HomeRecyclerItemViewHolder extends RecyclerView.ViewHolder {

        private CircleImageView profileImage;
        private TextView fullName;
        private TextView userName;
        private ImageView demoImage1;
        private ImageView demoImage2;
        private ImageView demoImage3;

        public HomeRecyclerItemViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);

            // Anh Xa
            profileImage = itemView.findViewById(R.id.profile_image);
            fullName = itemView.findViewById(R.id.fullname);
            userName = itemView.findViewById(R.id.user_name);
            demoImage1 = itemView.findViewById(R.id.demo_image_1);
            demoImage2 = itemView.findViewById(R.id.demo_image_2);
            demoImage3 = itemView.findViewById(R.id.demo_iamge_3);

        }
    }
}
