package com.example.duan1_nhom2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentManager;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.Transformation;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.example.duan1_nhom2.DialogClass.DialogResetViewThang;
import com.example.duan1_nhom2.DialogClass.DialogThemVaCapNhatAlbum;
import com.example.duan1_nhom2.DialogClass.DialogThemVaCapNhatNgheSi;
import com.example.duan1_nhom2.DialogClass.DialogThemVaCapNhatNhac;
import com.example.duan1_nhom2.DialogClass.DialogThemVaCapNhatTheLoai;
import com.example.duan1_nhom2.FragmentClass.AlbumChiTietFragment;
import com.example.duan1_nhom2.FragmentClass.AlbumsFragment;
import com.example.duan1_nhom2.FragmentClass.CaiDatFragment;
import com.example.duan1_nhom2.FragmentClass.ChiTietPlaylistFragment;
import com.example.duan1_nhom2.FragmentClass.ChuDeFragment;
import com.example.duan1_nhom2.FragmentClass.DoiMatKhauFragment;
import com.example.duan1_nhom2.FragmentClass.NgheSiFragment;
import com.example.duan1_nhom2.FragmentClass.NhacDangNgheFragment;
import com.example.duan1_nhom2.FragmentClass.NhacTheoChuDeFragment;
import com.example.duan1_nhom2.FragmentClass.PlaylistFragment;
import com.example.duan1_nhom2.FragmentClass.TimKiemFragment;
import com.example.duan1_nhom2.FragmentClass.TinTucFragment;
import com.example.duan1_nhom2.FragmentClass.TrangChuFragment;
import com.example.duan1_nhom2.FragmentClass.WebTinTucFragment;
import com.example.duan1_nhom2.FragmentClass.XemThemAlbumFragment;
import com.example.duan1_nhom2.FragmentClass.XemThemNgheSiFragment;
import com.example.duan1_nhom2.FragmentClass.XemThemNhacFragment;
import com.example.duan1_nhom2.Model.Admin;
import com.example.duan1_nhom2.Model.Albums;
import com.example.duan1_nhom2.Model.NgheSi;
import com.example.duan1_nhom2.Model.NguoiDung;
import com.example.duan1_nhom2.Model.Nhac;
import com.example.duan1_nhom2.Model.PlayList;
import com.example.duan1_nhom2.Model.TheLoai;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends AppCompatActivity {
    TextView txtTuaDeTrang, txtTenNhac, txtTenNgheSi;
    FragmentManager fragmentManager;
    NhacDangNgheFragment nhacDangNgheFragment;
    PlaylistFragment playlistFragment;
    TrangChuFragment trangChuFragment;
    ImageView ivAction1, ivAction2, ivSangKeTiep, ivChayVaDung, ivTroLaiTruoc;
    SeekBar sbNhacDangNghe;
    CircleImageView ivAnhNguoiDung;
    Toolbar tbMain;
    BottomNavigationView bottomNavigationView;
    Bundle bundle = new Bundle();
    NguoiDung nguoiDung;
    Admin admin;
    RelativeLayout rlMusicController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findView();
        setSupportActionBar(tbMain);
        getCurrentUser();
        setOnBottomNavigationItemSelected();
        sbNhacDangNghe.getThumb().mutate().setAlpha(0);
        sbNhacDangNghe.setPadding(0, 0, 0, 0);
        fragmentInitiation();
        fragmentManager = getSupportFragmentManager();
        if (savedInstanceState == null) {
            fragmentManager.beginTransaction().replace(R.id.fragmentContainer, trangChuFragment).commit();
            fragmentManager.beginTransaction().replace(R.id.fragmentContainer2, nhacDangNgheFragment).commit();
            txtTuaDeTrang.setText("Trang Chủ");
        }
        ivAnhNguoiDung.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fragmentManager.beginTransaction().replace(R.id.fragmentContainer, new CaiDatFragment(nguoiDung)).commit();
            }
        });
        ivAction2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        ifUserIsAnAdmin();
        collapse(rlMusicController);
    }

    private void fragmentInitiation() {
        String userID = nguoiDung.getMaNguoiDung();
        playlistFragment = new PlaylistFragment(userID);
        nhacDangNgheFragment = new NhacDangNgheFragment(userID);
        trangChuFragment = new TrangChuFragment();
    }

    private void findView() {
        tbMain = findViewById(R.id.tbMain);
        bottomNavigationView = findViewById(R.id.bottom_navigation);
        ivAnhNguoiDung = findViewById(R.id.ivAnhNguoiDung);
        ivAction1 = findViewById(R.id.ivAction1);
        txtTuaDeTrang = tbMain.findViewById(R.id.txtTuaDeTrang);
        txtTenNhac = findViewById(R.id.txtTenNhac);
        txtTenNgheSi = findViewById(R.id.txtTenNgheSi);
        ivChayVaDung = findViewById(R.id.ivChayVaDung);
        ivSangKeTiep = findViewById(R.id.ivSangKeTiep);
        ivTroLaiTruoc = findViewById(R.id.ivTroLaiTruoc);
        sbNhacDangNghe = findViewById(R.id.sbNhacDangNghe);
        rlMusicController = findViewById(R.id.rlMusicController);
        ivAction2 = findViewById(R.id.ivAction2);
    }

    private PopupMenu popupMenuSetUp() {
        PopupMenu popupMenu = new PopupMenu(MainActivity.this, ivAction1);
        popupMenu.inflate(R.menu.admin_fragment_trangchu_menu);
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.txtThemNhac: {
                        DialogThemVaCapNhatNhac dialog = new DialogThemVaCapNhatNhac(null, false);
                        dialog.show(getSupportFragmentManager(), "DialogThemVaCapNhatNhac");
                        return true;
                    }
                    case R.id.txtThemAlbum: {
                        DialogThemVaCapNhatAlbum dialog = new DialogThemVaCapNhatAlbum(null, false);
                        dialog.show(getSupportFragmentManager(), "DialogThemVaCapNhatAlbum");
                        return true;
                    }
                    case R.id.txtThemNgheSi: {
                        DialogThemVaCapNhatNgheSi dialog = new DialogThemVaCapNhatNgheSi(null, false);
                        dialog.show(getSupportFragmentManager(), "DialogThemVaCapNhatNgheSi");
                        return true;
                    }
                    case R.id.txtThemChuDe: {
                        DialogThemVaCapNhatTheLoai dialog = new DialogThemVaCapNhatTheLoai(null, false);
                        dialog.show(getSupportFragmentManager(), "DialogThemVaCapNhatTheLoai");
                        return true;
                    }
                    case R.id.txtTimKiem: {
                        fragmentManager.beginTransaction().replace(R.id.fragmentContainer, new TimKiemFragment()).commit();
                        return true;
                    }
                    default: {
                        return false;
                    }
                }
            }
        });
        return popupMenu;
    }

    private void ifUserIsAnAdmin() {
        if (isUserAnAdmin()) {
            final PopupMenu popupMenu = popupMenuSetUp();
            View.OnClickListener listener1 = new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    popupMenu.show();
                }
            };
            setOnClickToolbarAction1(listener1, R.drawable.ic_more, View.VISIBLE);
            View.OnClickListener listener2 = new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    DialogResetViewThang dialog = new DialogResetViewThang();
                    dialog.show(getSupportFragmentManager(), "DialogResetViewThang");
                }
            };
            setOnClickToolbarAction2(listener2, R.drawable.ic_repeat, View.VISIBLE);
        } else {
            ivAction2.setVisibility(View.INVISIBLE);
            View.OnClickListener listener = new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    fragmentManager.beginTransaction().replace(R.id.fragmentContainer, new TimKiemFragment()).commit();
                }
            };
            setOnClickToolbarAction1(listener, R.drawable.ic_search, View.VISIBLE);
        }
    }

    public void setSeekBarVisibility(int visibility) {
        sbNhacDangNghe.setVisibility(visibility);
    }

    private void setMediaFuctionaries() {
        setSeekBarVisibility(View.VISIBLE);
        setPlayAndPauseButtonEnabled(true);
        ivTroLaiTruoc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nhacDangNgheFragment.skipToPrevious();
            }
        });
        ivSangKeTiep.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nhacDangNgheFragment.skipToNext();
            }
        });
        ivChayVaDung.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nhacDangNgheFragment.playAndPauseMusic();
            }
        });
        txtTenNhac.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nhacDangNgheFragment.slideUp();
            }
        });
    }

    private void getCurrentUser() {
        bundle = getIntent().getBundleExtra("UserProfile");
        int type = bundle.getInt("LoginAs");
        if (type == 4) {
            admin = new Admin();
        }
        nguoiDung = new NguoiDung();
        String photoURL = bundle.getString("PhotoURL");
        nguoiDung.setMaNguoiDung(bundle.getString("UserID"));
        nguoiDung.setEmail(bundle.getString("Email"));
        nguoiDung.setTenHienThi(bundle.getString("DisplayName"));
        nguoiDung.setURLAnh(photoURL);
        if (photoURL.isEmpty()) {
            ivAnhNguoiDung.setImageResource(R.drawable.default_user_icon);
        } else {
            Picasso.with(this).load(photoURL).into(ivAnhNguoiDung);
        }
    }

    private void setOnBottomNavigationItemSelected() {
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                nhacDangNgheFragment.slideDown();
                switch (item.getItemId()) {
                    case R.id.btnvTrangChu: {
                        fragmentManager.beginTransaction().replace(R.id.fragmentContainer, trangChuFragment).commit();
                        txtTuaDeTrang.setText("Trang Chủ");
                        ivAction2.setVisibility(View.VISIBLE);
                        ifUserIsAnAdmin();
                        return true;
                    }
                    case R.id.btnvPlaylists: {
                        fragmentManager.beginTransaction().replace(R.id.fragmentContainer, playlistFragment).commit();
                        txtTuaDeTrang.setText("Playlist");
                        return true;
                    }
                    case R.id.btnvAlbums: {
                        fragmentManager.beginTransaction().replace(R.id.fragmentContainer, new AlbumsFragment()).commit();
                        txtTuaDeTrang.setText("Album");
                        return true;
                    }
                    case R.id.btnvTinTuc: {
                        fragmentManager.beginTransaction().replace(R.id.fragmentContainer, new TinTucFragment()).commit();
                        txtTuaDeTrang.setText("Tin Tức");
                        return true;
                    }
                    case R.id.btnvChuDe: {
                        fragmentManager.beginTransaction().replace(R.id.fragmentContainer, new ChuDeFragment()).commit();
                        txtTuaDeTrang.setText("Chủ Đề");
                        return true;
                    }
                    default: {
                        return false;
                    }
                }
            }
        });
    }

    public void changeFragmentWithData(int type, ArrayList data) {
        switch (type) {
            case 1: {
                fragmentManager.beginTransaction().replace(R.id.fragmentContainer, new XemThemNhacFragment(data)).commit();
                break;
            }
            case 2:{
                fragmentManager.beginTransaction().replace(R.id.fragmentContainer, new XemThemNgheSiFragment(data)).commit();
                break;
            }
            case 3:{
                fragmentManager.beginTransaction().replace(R.id.fragmentContainer, new XemThemAlbumFragment(data)).commit();
                break;
            }
        }
    }

    public void changeToWebTinTucFragment(String link) {
        fragmentManager.beginTransaction().replace(R.id.fragmentContainer, new WebTinTucFragment(link)).commit();
    }

    public void changeToNhacDangNgheFragment(ArrayList<Nhac> dsn) {
        nhacDangNgheFragment.replaceSongAndDisplayInfo(dsn);
        nhacDangNgheFragment.slideUp();
        fragmentManager.beginTransaction().replace(R.id.fragmentContainer2, nhacDangNgheFragment).commit();
        setMediaFuctionaries();
        expandMusicController();
    }
    public void changeToPlaylistChiTietFragment(PlayList playList){
        fragmentManager.beginTransaction().replace(R.id.fragmentContainer, new ChiTietPlaylistFragment(playList)).commit();
    }

    public void queueSongToCurrentPlaylist(Nhac nhac) {
        nhacDangNgheFragment.queueMoreSong(nhac);
    }

    public void changeToNgheSiFragment(NgheSi ngheSi) {
        fragmentManager.beginTransaction().replace(R.id.fragmentContainer, new NgheSiFragment(ngheSi)).commit();
    }
    public void changeToNhacTheoChuDeFragment(TheLoai theLoai){
        fragmentManager.beginTransaction().replace(R.id.fragmentContainer, new NhacTheoChuDeFragment(theLoai)).commit();
        txtTuaDeTrang.setText(theLoai.getTenTheLoai());
    }
    public void changeToAlbumChiTietFragment(Albums albums) {
        fragmentManager.beginTransaction().replace(R.id.fragmentContainer, new AlbumChiTietFragment(albums)).commit();
    }
    public void changeToDoiMatKhauFragment(){
        fragmentManager.beginTransaction().replace(R.id.fragmentContainer, new DoiMatKhauFragment()).commit();
    }

    private void expand(final View v) {
        int matchParentMeasureSpec = View.MeasureSpec.makeMeasureSpec(((View) v.getParent()).getWidth(), View.MeasureSpec.EXACTLY);
        int wrapContentMeasureSpec = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        v.measure(matchParentMeasureSpec, wrapContentMeasureSpec);
        final int targetHeight = v.getMeasuredHeight();

        // Older versions of android (pre API 21) cancel animations for views with a height of 0.
        v.getLayoutParams().height = 1;
        v.setVisibility(View.VISIBLE);
        Animation a = new Animation() {
            @Override
            protected void applyTransformation(float interpolatedTime, Transformation t) {
                v.getLayoutParams().height = interpolatedTime == 1
                        ? ViewGroup.LayoutParams.WRAP_CONTENT
                        : (int) (targetHeight * interpolatedTime);
                v.requestLayout();
            }

            @Override
            public boolean willChangeBounds() {
                return true;
            }
        };

        // Expansion speed of 1dp/ms
        a.setDuration((int) (targetHeight / v.getContext().getResources().getDisplayMetrics().density) * 4);
        v.startAnimation(a);
    }

    private void collapse(final View v) {
        final int initialHeight = v.getMeasuredHeight();

        Animation a = new Animation() {
            @Override
            protected void applyTransformation(float interpolatedTime, Transformation t) {
                if (interpolatedTime == 1) {
                    v.setVisibility(View.GONE);
                } else {
                    v.getLayoutParams().height = (initialHeight - (int) (initialHeight * interpolatedTime));
                    v.requestLayout();
                }
            }

            @Override
            public boolean willChangeBounds() {
                return true;
            }
        };

        // Collapse speed of 1dp/ms
        a.setDuration((int) (initialHeight / v.getContext().getResources().getDisplayMetrics().density) * 4);
        v.startAnimation(a);
    }

    public void finishMainActivity() {
        finish();
    }

    public void collapseMusicController() {
        collapse(rlMusicController);
    }

    public void expandMusicController(){
        expand(rlMusicController);
    }

    public void collapseMainToolbar() {
        collapse(tbMain);
    }

    public void expandMainToolbar() {
        expand(tbMain);
    }

    public void setSongInformation(String tenNhac, String tenNgheSi, int seekBarMax) {
        if (tenNhac.length() > 25) {
            String cutText = tenNhac.substring(0, 17) + "...";
            txtTenNhac.setText(cutText);
        } else {
            txtTenNhac.setText(tenNhac);
        }
        if (tenNgheSi.length() > 25) {
            String cutText = tenNgheSi.substring(0, 17) + "...";
            txtTenNgheSi.setText(cutText);
        } else {
            txtTenNgheSi.setText(tenNgheSi);
        }
        sbNhacDangNghe.setMax(seekBarMax);
        ivChayVaDung.setImageResource(R.drawable.ic_pause_red);
    }

    public void updateSeekbar(int progress) {
        sbNhacDangNghe.setProgress(progress);
    }

    public void setPlayAndPauseButtonEnabled(boolean enabled) {
        ivChayVaDung.setEnabled(enabled);
    }

    public void setPlayAndPauseButtonImage(int imageID) {
        ivChayVaDung.setImageResource(imageID);
    }

    public void setOnClickToolbarAction1(View.OnClickListener listener, int drawableID, int visibility) {
        ivAction1.setImageResource(drawableID);
        ivAction1.setOnClickListener(listener);
        ivAction1.setVisibility(visibility);
    }

    public void setOnClickToolbarAction2(View.OnClickListener listener, int drawableID, int visibility) {
        ivAction2.setImageResource(drawableID);
        ivAction2.setOnClickListener(listener);
        ivAction2.setVisibility(visibility);
    }

    public boolean isUserAnAdmin() {
        if (admin != null) {
            return true;
        } else {
            return false;
        }
    }
    public void changeToolbarImage(String urlAnh){
        Picasso.with(this).load(urlAnh).into(ivAnhNguoiDung);
    }
    public void setToolbarTitle(String title){
        txtTuaDeTrang.setText(title);
    }
    public void createAdmin() {
        DatabaseReference myDatabaseRef = FirebaseDatabase.getInstance().getReference("Admin");
        String maAdmin = FirebaseAuth.getInstance().getUid();
        if (nguoiDung != null && maAdmin != null) {
            String password = bundle.getString("Password");
            Admin admin = new Admin(maAdmin, nguoiDung.getTenHienThi(), password, nguoiDung.getEmail(), nguoiDung.getURLAnh());
            myDatabaseRef.child(maAdmin).setValue(admin);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (FirebaseAuth.getInstance().getCurrentUser() != null) {
            FirebaseAuth.getInstance().signOut();
        }
    }
}
