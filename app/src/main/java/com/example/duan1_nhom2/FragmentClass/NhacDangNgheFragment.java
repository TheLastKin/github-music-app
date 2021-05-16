package com.example.duan1_nhom2.FragmentClass;

import android.animation.ObjectAnimator;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.session.MediaSession;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.media.session.MediaSessionCompat;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.LinearSmoothScroller;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.example.duan1_nhom2.AdapterClass.BinhLuan_rvAdapter;
import com.example.duan1_nhom2.AdapterClass.PlaylistDangNghe_rvAdapter;
import com.example.duan1_nhom2.AdapterClass.ViewPager_Adapter;
import com.example.duan1_nhom2.AdditionalFunctions.AdditionalFunctions;
import com.example.duan1_nhom2.AppWrapperClass.MusicApp;
import com.example.duan1_nhom2.BroadcastReceiverClass.Controller;
import com.example.duan1_nhom2.DAOClass.DAO_BinhLuan;
import com.example.duan1_nhom2.DAOClass.DAO_LuotThich;
import com.example.duan1_nhom2.DAOClass.DAO_Nhac;
import com.example.duan1_nhom2.DialogClass.DialogThemNhacVaoAlbum;
import com.example.duan1_nhom2.DialogClass.DialogThemVaCapNhatNhac;
import com.example.duan1_nhom2.DialogClass.DialogThemVaoPlaylist;
import com.example.duan1_nhom2.DialogClass.DialogXoaDuLieu;
import com.example.duan1_nhom2.DialogClass.DialogXoaTatCaNhacDangNghe;
import com.example.duan1_nhom2.InterfaceClass.MusicController;
import com.example.duan1_nhom2.MainActivity;
import com.example.duan1_nhom2.Model.BinhLuan;
import com.example.duan1_nhom2.Model.Nhac;
import com.example.duan1_nhom2.R;
import com.example.duan1_nhom2.ServiceClass.OnClearFromRecentService;
import com.google.android.material.bottomsheet.BottomSheetBehavior;

import java.util.ArrayList;

public class NhacDangNgheFragment extends Fragment implements PlaylistDangNghe_rvAdapter.UpdateSongTime,
        DialogXoaTatCaNhacDangNghe.RemoveQueue, MusicController {
    private ViewPager viewPager;
    private ImageView ivChayVaDung, ivTroLaiTruoc, ivSangKeTiep, ivXaoTron, ivLapLai, ivAnCuaSo, ivNhacDangNgheIcon, ivThich, ivThemVaoPlaylist;
    private TextView txtThoiGianHienTai, txtThoiLuongBaiHat, txtBinhLuan, txtLuotThich, txtLyrics, txtTenNhac, txtTenNgheSi, txtTuaDeTrang;
    private EditText txtNhapBinhLuan;
    private Button btnDangBinhLuan;
    private RecyclerView rvBinhLuan, rvPlaylistDangNghe;
    private View viewNhacDangNgheTrangChinh, viewPlaylistDangNghe, viewLyrics, nsvNhacDangNghe;
    private PlaylistDangNghe_rvAdapter adapterPlaylistDangNghe;
    private BinhLuan_rvAdapter adapterBinhLuan;
    private SeekBar sbNhacDangNghe;
    private ArrayList<View> dslayout = new ArrayList<>();
    private Toolbar tbNhacDangNgheTrangChinh;
    private BottomSheetBehavior bottomSheetBehavior;
    private int tracker1 = 0;
    private int tracker2 = 2;
    private boolean tracker3 = false;
    private int degree = 360;
    private ObjectAnimator animator;
    private LinearLayoutManager layoutManager, layoutManager2;
    private String userID = "";
    private String maLuotThich = "";
    private String maNhac = "";
    private ArrayList<Nhac> dsn;
    private NotificationManagerCompat managerCompat;
    private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getExtras().getString("ActionName");
            switch (action) {
                case "Back": {
                    mcSkipToPrevious();
                    break;
                }
                case "Play": {
                    mcPlayAndPause();
                    break;
                }
                case "Next": {
                    mcSkipToNext();
                    break;
                }
            }
        }
    };
    private MediaSessionCompat mediaSessionCompat; //Unnecessary

    public NhacDangNgheFragment(String userID) {
        this.userID = userID;
        dsn = new ArrayList<>();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_nhacdangnghe, container, false);
        inflateLayout();
        findView(view);
        viewPagerSetUp();
        slideDown();
        setMediaPlayerFunctionaries();
        seekBarSetUp();
        recyclerViewSetUp();
        mediaSessionCompat = new MediaSessionCompat(getContext(), "tag");
        ivChayVaDung.setClickable(false);
        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
        sbNhacDangNghe.setClickable(false);
        animator = ObjectAnimator.ofFloat(ivNhacDangNgheIcon, "rotation", degree);
        animator.setRepeatCount(Animation.INFINITE);
        animator.setInterpolator(new LinearInterpolator());
        animator.setDuration(7000);
        managerCompat = NotificationManagerCompat.from(getContext());
        btnDangBinhLuan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String binhLuan = txtNhapBinhLuan.getText().toString();
                if (!AdditionalFunctions.isStringEmpty(userID, maNhac, binhLuan)) {
                    DAO_BinhLuan.postComment(userID, maNhac, binhLuan, adapterBinhLuan);
                }
            }
        });
        ivAnCuaSo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                slideDown();
            }
        });
        registerBroadcastReceiverAndService();
        return view;
    }

    private void registerBroadcastReceiverAndService() {
        getContext().registerReceiver(broadcastReceiver, new IntentFilter("PerformAction"));
        getContext().startService(new Intent(getContext(), OnClearFromRecentService.class));
    }

    private void recyclerViewSetUp() {
        layoutManager = new LinearLayoutManager(getContext());
        adapterPlaylistDangNghe = new PlaylistDangNghe_rvAdapter(getContext());
        adapterPlaylistDangNghe.setUpdateSongTimeListener(this);
        rvPlaylistDangNghe.setLayoutManager(layoutManager);
        rvPlaylistDangNghe.setAdapter(adapterPlaylistDangNghe);
        layoutManager2 = new LinearLayoutManager(getContext());
        layoutManager2.setReverseLayout(true);
        adapterBinhLuan = new BinhLuan_rvAdapter(getContext(), this);
        rvBinhLuan.setLayoutManager(layoutManager2);
        rvBinhLuan.setAdapter(adapterBinhLuan);
        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                adapterPlaylistDangNghe.removeSongFromQueue(viewHolder.getAdapterPosition());
                if (adapterPlaylistDangNghe.getItemCount()==0){
                    onMediaReleased();
                    ((MainActivity)getContext()).collapseMusicController();
                    slideDown();
                }
            }
        }).attachToRecyclerView(rvPlaylistDangNghe);
    }

    private PopupMenu popupMenuSetUp() {
        PopupMenu popupMenu = new PopupMenu(getContext(), ivThemVaoPlaylist);
        popupMenu.inflate(R.menu.admin_fragment_nhacdangnghe_menu);
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.txtCapNhat: {
                        DialogThemVaCapNhatNhac dialog = new DialogThemVaCapNhatNhac(adapterPlaylistDangNghe.getCurrentSong(), true);
                        dialog.show(getChildFragmentManager(), "DialogThemVaCapNhatNhac");
                        return true;
                    }
                    case R.id.txtXoa: {
                        if (adapterPlaylistDangNghe.isPlaying()){
                            Nhac nhac = adapterPlaylistDangNghe.getCurrentSong();
                            DialogXoaDuLieu dialog = new DialogXoaDuLieu(nhac.getMaNhac(), nhac.getURL(), nhac.getTenNhac(), nhac.getTenNgheSi());
                            dialog.setType(1);
                            dialog.show(getChildFragmentManager(), "DialogXoaDuLieu");
                        }else{
                            Toast.makeText(getContext(), "Bấm Dừng Bài Hát Trước Đã!", Toast.LENGTH_SHORT).show();
                        }
                        return true;
                    }
                    case R.id.txtThemVaoPlaylist: {
                        DialogThemVaoPlaylist dialog = new DialogThemVaoPlaylist(userID, adapterPlaylistDangNghe.getCurrentSong());
                        dialog.show(getChildFragmentManager(), "DialogThemVaoPlaylist");
                        return true;
                    }
                    case R.id.txtThemVaoAlbum:{
                        DialogThemNhacVaoAlbum dialog = new DialogThemNhacVaoAlbum(adapterPlaylistDangNghe.getCurrentSong().getMaNhac());
                        dialog.show(getChildFragmentManager(), "DialogThemNhacVaoAlbum");
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
        MainActivity mainActivity = (MainActivity) getContext();
        if (mainActivity.isUserAnAdmin()) {
            final PopupMenu popupMenu = popupMenuSetUp();
            ivThemVaoPlaylist.setImageResource(R.drawable.ic_more);
            ivThemVaoPlaylist.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    popupMenu.show();
                }
            });
        } else {
            ivThemVaoPlaylist.setImageResource(R.drawable.ic_add);
            ivThemVaoPlaylist.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    DialogThemVaoPlaylist dialog = new DialogThemVaoPlaylist(userID, adapterPlaylistDangNghe.getCurrentSong());
                    dialog.show(getChildFragmentManager(), "DialogThemVaoPlaylist");
                }
            });
        }
    }

    private void seekBarSetUp() {
        sbNhacDangNghe.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                adapterPlaylistDangNghe.onSeekbarProgressChanged(seekBar.getProgress());
            }
        });
    }

    private void setMediaPlayerFunctionaries() {
        ivXaoTron.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (tracker1 % 2 == 0) {
                    ivXaoTron.setImageResource(R.drawable.ic_shuffle_red);
                    adapterPlaylistDangNghe.setShuffling(true);
                } else {
                    ivXaoTron.setImageResource(R.drawable.ic_shuffle);
                    adapterPlaylistDangNghe.setShuffling(false);
                }
                tracker1 += 1;
            }
        });
        ivLapLai.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (tracker2 == 3) {
                    ivLapLai.setImageResource(R.drawable.ic_repeat_one);
                    adapterPlaylistDangNghe.setLoopingOne(true);
                    tracker2 = 1;
                } else if (tracker2 == 2) {
                    ivLapLai.setImageResource(R.drawable.ic_repeat_red);
                    adapterPlaylistDangNghe.setLooping(true);
                    adapterPlaylistDangNghe.setLoopingOne(false);
                    tracker2 = 3;
                } else {
                    ivLapLai.setImageResource(R.drawable.ic_repeat);
                    adapterPlaylistDangNghe.setLooping(false);
                    adapterPlaylistDangNghe.setLoopingOne(false);
                    tracker2 = 2;
                }
            }
        });
        ivTroLaiTruoc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                skipToPrevious();
            }
        });
        ivSangKeTiep.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                skipToNext();
            }
        });
        ivChayVaDung.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                playAndPauseMusic();
            }
        });
    }

    private void viewPagerSetUp() {
        dslayout.add(viewPlaylistDangNghe);
        dslayout.add(viewNhacDangNgheTrangChinh);
        dslayout.add(viewLyrics);
        ViewPager_Adapter adapter = new ViewPager_Adapter(dslayout);
        viewPager.setAdapter(adapter);
        viewPager.setCurrentItem(1);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                switch (position) {
                    case 1: {
                        ifUserIsAnAdmin();
                        txtTuaDeTrang.setText("Nhạc Đang Nghe");
                        break;
                    }
                    case 0: {
                        ivThemVaoPlaylist.setImageResource(R.drawable.ic_delete);
                        ivThemVaoPlaylist.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                DialogXoaTatCaNhacDangNghe dialog = new DialogXoaTatCaNhacDangNghe();
                                dialog.setRemoveQueueListener(NhacDangNgheFragment.this);
                                dialog.show(getChildFragmentManager(), "DialogXoaTatCaNhacDangNghe");
                            }
                        });
                        txtTuaDeTrang.setText("Playlist Đang Nghe");
                        break;
                    }
                    case 2: {
                        txtTuaDeTrang.setText("Lyrics");
                        break;
                    }
                }
            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    private void inflateLayout() {
        LayoutInflater layoutInflater = getLayoutInflater();
        viewNhacDangNgheTrangChinh = layoutInflater.inflate(R.layout.fragment_nhacdangnghe_trangchinh, null);
        viewPlaylistDangNghe = layoutInflater.inflate(R.layout.fragment_nhacdangnghe_playlist, null);
        viewLyrics = layoutInflater.inflate(R.layout.fragment_nhacdangnghe_lyrics, null);
    }

    private void findView(View view) {
        nsvNhacDangNghe = view.findViewById(R.id.nsvNhacDangNghe);
        bottomSheetBehavior = BottomSheetBehavior.from(nsvNhacDangNghe);
        ivXaoTron = view.findViewById(R.id.ivXaoTron);
        ivLapLai = view.findViewById(R.id.ivLapLai);
        viewPager = view.findViewById(R.id.vpNhacDangNghe);
        ivChayVaDung = view.findViewById(R.id.ivChayVaDung);
        ivSangKeTiep = view.findViewById(R.id.ivSangKeTiep);
        ivTroLaiTruoc = view.findViewById(R.id.ivTroLaiTruoc);
        txtBinhLuan = view.findViewById(R.id.txtBinhLuan);
        txtLuotThich = view.findViewById(R.id.txtLuotThich);
        txtNhapBinhLuan = view.findViewById(R.id.txtNhapBinhLuan);
        txtThoiGianHienTai = view.findViewById(R.id.txtThoiGianHienTai);
        txtThoiLuongBaiHat = view.findViewById(R.id.txtThoiLuongBaiHat);
        rvBinhLuan = view.findViewById(R.id.rvBinhLuan);
        rvPlaylistDangNghe = viewPlaylistDangNghe.findViewById(R.id.rvPlaylistDangNghe);
        txtLyrics = viewLyrics.findViewById(R.id.txtLyrics);
        sbNhacDangNghe = view.findViewById(R.id.sbNhacDangNghe);
        txtTenNhac = viewNhacDangNgheTrangChinh.findViewById(R.id.txtTenNhac);
        txtTenNgheSi = viewNhacDangNgheTrangChinh.findViewById(R.id.txtTenNgheSi);
        ivNhacDangNgheIcon = viewNhacDangNgheTrangChinh.findViewById(R.id.ivNhacDangNgheIcon);
        tbNhacDangNgheTrangChinh = viewNhacDangNgheTrangChinh.findViewById(R.id.tbNhacDangNgheTrangChinh);
        ivAnCuaSo = view.findViewById(R.id.ivAnCuaSo);
        ivThich = view.findViewById(R.id.ivThich);
        ivThemVaoPlaylist = view.findViewById(R.id.ivThemVaoPlaylist);
        btnDangBinhLuan = view.findViewById(R.id.btnDangBinhLuan);
        txtTuaDeTrang = view.findViewById(R.id.txtTuaDeTrang);
    }

    private void toolBarTrangChinhSetUp(final Nhac nhac) {
        ivThich.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (nhac != null) {
                    if (tracker3) {
                        ivThich.setImageResource(R.drawable.heart_icon);
                        tracker3 = false;
                        if (!AdditionalFunctions.isStringEmpty(maLuotThich)) {
                            DAO_LuotThich.removeFromFavorite(maLuotThich, nhac.getMaNhac(), NhacDangNgheFragment.this);
                        }
                    } else {
                        ivThich.setImageResource(R.drawable.heart_icon_filled);
                        tracker3 = true;
                        if (!AdditionalFunctions.isStringEmpty(userID)) {
                            DAO_LuotThich.addToFavorite(maLuotThich, userID, nhac.getMaNhac(), NhacDangNgheFragment.this);
                        }
                    }
                }
            }
        });
    }

    private void createNotificationChannelForController(Nhac nhac, int playAndPauseImage) {
        Intent intent1 = new Intent(getContext(), Controller.class);
        intent1.setAction("Back");
        Intent intent2 = new Intent(getContext(), Controller.class);
        intent2.setAction("Play");
        Intent intent3 = new Intent(getContext(), Controller.class);
        intent3.setAction("Next");
        PendingIntent pendingIntent1 = PendingIntent.getBroadcast(getContext(), 0, intent1, PendingIntent.FLAG_UPDATE_CURRENT);
        PendingIntent pendingIntent2 = PendingIntent.getBroadcast(getContext(), 0, intent2, PendingIntent.FLAG_UPDATE_CURRENT);
        PendingIntent pendingIntent3 = PendingIntent.getBroadcast(getContext(), 0, intent3, PendingIntent.FLAG_UPDATE_CURRENT);
        Bitmap background = BitmapFactory.decodeResource(getContext().getResources(), R.drawable.album_default_icon);
        Notification notification = new NotificationCompat.Builder(getContext(), MusicApp.CONTROLLER_CHANNEL)
                .setSmallIcon(R.drawable.ic_library_music)
                .setContentTitle(nhac.getTenNhac())
                .setContentText(nhac.getTenNgheSi())
                .setLargeIcon(background)
                .addAction(R.drawable.ic_skip_previous, "SkipToPrevious", pendingIntent1)
                .addAction(playAndPauseImage, "PlayAndPause", pendingIntent2)
                .addAction(R.drawable.ic_skip_next_red, "SkipToNext", pendingIntent3)
                .setStyle(new androidx.media.app.NotificationCompat.MediaStyle()
                        .setMediaSession(mediaSessionCompat.getSessionToken())
                        .setShowActionsInCompactView(0, 1, 2))
                .setOnlyAlertOnce(true)
                .setPriority(NotificationCompat.PRIORITY_LOW)
                .build();
        managerCompat.notify(1, notification);
    }

    public void skipToNext() {
        adapterPlaylistDangNghe.skipToNext();
    }

    public void skipToPrevious() {
        adapterPlaylistDangNghe.skipToPrevious();
    }

    public void playAndPauseMusic() {
        if (adapterPlaylistDangNghe.playAndPauseMusic()) {
            ivChayVaDung.setImageResource(R.drawable.ic_play_arrow_red);
            ((MainActivity) getContext()).setPlayAndPauseButtonImage(R.drawable.ic_play_arrow_red);
            mcOnPause();
            animator.pause();
        } else {
            ivChayVaDung.setImageResource(R.drawable.ic_pause_red);
            ((MainActivity) getContext()).setPlayAndPauseButtonImage(R.drawable.ic_pause_red);
            mcOnStart();
            animator.resume();
        }
    }

    public void replaceSongAndDisplayInfo(ArrayList<Nhac> dsn) {
        if (dsn.size() > 0) {
            this.dsn = dsn;
            this.maNhac = dsn.get(0).getMaNhac();
            toolBarTrangChinhSetUp(dsn.get(0));
            adapterPlaylistDangNghe.setMusicList(dsn);
        }
    }

    public void queueMoreSong(Nhac nhac) {
        if (adapterPlaylistDangNghe.getItemCount() > 0) {
            adapterPlaylistDangNghe.addSongToQueue(nhac);
            Toast.makeText(getContext(), "Đã thêm vào hàng chờ", Toast.LENGTH_SHORT).show();
        } else {
            ArrayList<Nhac> newDSN = new ArrayList<>();
            newDSN.add(nhac);
            replaceSongAndDisplayInfo(newDSN);
            MainActivity mainActivity = (MainActivity)getContext();
            mainActivity.expandMusicController();
            mainActivity.setPlayAndPauseButtonEnabled(true);
        }
    }

    public void slideUp() {
        ((MainActivity) getContext()).collapseMainToolbar();
        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
    }

    public void slideDown() {
        ((MainActivity) getContext()).expandMainToolbar();
        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
        bottomSheetBehavior.setDraggable(false);
    }

    public void setMaLuotThich(String maLuotThich) {
        this.maLuotThich = maLuotThich;
    }

    public void isUserFavorite(boolean isFavourite, String maLuotThich) {
        if (isFavourite) {
            ivThich.setImageResource(R.drawable.heart_icon_filled);
            tracker3 = true;
            this.maLuotThich = maLuotThich;
        } else {
            ivThich.setImageResource(R.drawable.heart_icon);
            tracker3 = false;
        }
    }

    public void setTotalComment(int total) {
        String m;
        if (total < 1000) {
            m = total + " Bình Luận";
        } else {
            m = String.valueOf(total / 1000).substring(0, 2) + "k Bình Luận";
        }
        txtBinhLuan.setText(m);
    }

    public void setTotalLike(int total) {
        String m;
        if (total < 1000) {
            m = total + " Lượt Thích";
        } else {
            m = String.valueOf(total / 1000).substring(0, 2) + "k Lượt Thích";
        }
        txtLuotThich.setText(m);
    }
    public String getUserID(){
        return userID;
    }

    @Override
    public void updateTimeTextView(String currentTime) {
        txtThoiGianHienTai.setText(currentTime);
    }

    @Override
    public void setSongInfomation(Nhac nhac, String endTime, int seekbarMax) {
        sbNhacDangNghe.setMax(seekbarMax);
        txtThoiLuongBaiHat.setText(endTime);
        txtTenNhac.setText(nhac.getTenNhac());
        txtTenNgheSi.setText(nhac.getTenNgheSi());
        this.maNhac = nhac.getMaNhac();
        DAO_LuotThich.loadFavorite(userID, maNhac, this);
        DAO_LuotThich.loadTotalLike(maNhac, this);
        DAO_BinhLuan.readMusicComments(maNhac, adapterBinhLuan);
        ((MainActivity) getContext()).setSongInformation(nhac.getTenNhac(), nhac.getTenNgheSi(), seekbarMax);
    }


    @Override
    public void updateSeekBar(int progress) {
        sbNhacDangNghe.setProgress(progress);
        ((MainActivity) getContext()).updateSeekbar(progress);
    }

    @Override
    public void onMediaPrepared() {
        sbNhacDangNghe.setEnabled(true);
        ivChayVaDung.setClickable(true);
        ivChayVaDung.setImageResource(R.drawable.ic_pause_red);
        animator.start();
        createNotificationChannelForController(adapterPlaylistDangNghe.getCurrentSong(), R.drawable.ic_pause_red);
        MainActivity mainActivity = (MainActivity)getContext();
        mainActivity.setPlayAndPauseButtonImage(R.drawable.ic_pause_red);
        mainActivity.setPlayAndPauseButtonEnabled(true);
        mainActivity.setSeekBarVisibility(View.VISIBLE);
    }

    @Override
    public void onMediaFinished() {
        MainActivity mainActivity = (MainActivity)getContext();
        mainActivity.setSeekBarVisibility(View.VISIBLE);
        mainActivity.setPlayAndPauseButtonEnabled(false);
        mainActivity.setPlayAndPauseButtonImage(R.drawable.ic_play_arrow_red);
    }

    @Override
    public void onMediaReleased() {
        sbNhacDangNghe.setEnabled(false);
        ivChayVaDung.setClickable(false);
        ivChayVaDung.setImageResource(R.drawable.ic_play_arrow_red);
        animator.pause();
        MainActivity mainActivity = (MainActivity) getContext();
        mainActivity.setSeekBarVisibility(View.INVISIBLE);
        mainActivity.setPlayAndPauseButtonEnabled(false);
    }

    @Override
    public void scrollToCurrentPlayingMusic(int position) {

    }

    @Override
    public void removeAllSongsFromQueue() {
        adapterPlaylistDangNghe.releaseUpdateSongTimeListener();
        adapterPlaylistDangNghe.releaseMedia();
        adapterPlaylistDangNghe.clearQueue();
        MainActivity mainActivity = (MainActivity) getContext();
        mainActivity.setSeekBarVisibility(View.INVISIBLE);
        mainActivity.setPlayAndPauseButtonEnabled(false);
        mainActivity.collapseMusicController();
        slideDown();
    }

    @Override
    public void mcSkipToPrevious() {
        adapterPlaylistDangNghe.skipToPrevious();
        createNotificationChannelForController(adapterPlaylistDangNghe.getCurrentSong(), R.drawable.ic_pause_red);
    }

    @Override
    public void mcPlayAndPause() {
        if (adapterPlaylistDangNghe.playAndPauseMusic()) {
            createNotificationChannelForController(adapterPlaylistDangNghe.getCurrentSong(), R.drawable.ic_play_arrow_red);
            ivChayVaDung.setImageResource(R.drawable.ic_play_arrow_red);
            animator.pause();
        } else {
            createNotificationChannelForController(adapterPlaylistDangNghe.getCurrentSong(), R.drawable.ic_pause_red);
            ivChayVaDung.setImageResource(R.drawable.ic_pause_red);
            animator.start();
        }
    }

    @Override
    public void mcSkipToNext() {
        adapterPlaylistDangNghe.skipToNext();
        createNotificationChannelForController(adapterPlaylistDangNghe.getCurrentSong(), R.drawable.ic_pause_red);
    }

    @Override
    public void mcOnStart() {
        ivChayVaDung.setImageResource(R.drawable.ic_pause_red);
        createNotificationChannelForController(adapterPlaylistDangNghe.getCurrentSong(), R.drawable.ic_pause_red);
    }

    @Override
    public void mcOnPause() {
        ivChayVaDung.setImageResource(R.drawable.ic_play_arrow_red);
        createNotificationChannelForController(adapterPlaylistDangNghe.getCurrentSong(), R.drawable.ic_play_arrow_red);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        adapterPlaylistDangNghe.releaseUpdateSongTimeListener();
        adapterPlaylistDangNghe.releaseMedia();
        managerCompat.cancelAll();
        getContext().unregisterReceiver(broadcastReceiver);
    }
}
