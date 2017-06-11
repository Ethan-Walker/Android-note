package com.example.ethanwalker.myapp;

import android.Manifest;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.PopupMenu;
import android.widget.RadioButton;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import static android.app.Activity.RESULT_OK;
import static android.content.Context.MODE_PRIVATE;

/**
 * Created by EthanWalker on 2017/4/14.
 */

public class ChangePersonalFrag extends Fragment {
    private ChangeCallBack callback;

    public interface ChangeCallBack {
        void submitMessage(Bundle bundle);
    }

    Button button;
    EditText editNickName;
    RadioButton male;
    EditText editPhone;
    EditText editPersonal;
    private Context mContext;
    ImageButton headImg;
    public static final int TAKE_PHOTO = 1;
    private static final int OPEN_ALBUM = 2;
    private static final int CHOOSE_PHOTO = 3;
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        if (callback == null) {
            callback = (ChangeCallBack) activity;
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.change_personal_frag, container, false);
        init(view);

        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("user",MODE_PRIVATE);
        myPath = sharedPreferences.getString("imagePath",null);
        displayImage(myPath);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putString("nickname", editNickName.getText().toString());
                String sex = male.isChecked() ? "男" : "女";
                bundle.putString("sex", sex);
                bundle.putString("phone", editPhone.getText().toString());
                bundle.putString("personal", editPersonal.getText().toString());
                bundle.putString("imagePath",myPath);
                callback.submitMessage(bundle);
                storeBySharedPreference(editNickName.getText().toString(), sex, editPhone.getText().toString(), editPersonal.getText().toString());
            }
        });

        mContext = getActivity();
        return view;
    }

    public void init(View view) {
        button = (Button) view.findViewById(R.id.submit);
        editNickName = (EditText) view.findViewById(R.id.edit_nickname);
        editPhone = (EditText) view.findViewById(R.id.edit_phone);
        editPersonal = (EditText) view.findViewById(R.id.edit_personal);
        male = (RadioButton) view.findViewById(R.id.male);
        headImg = (ImageButton) view.findViewById(R.id.head_img);
        headImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPopupMenu(v);
            }
        });
    }

    public void storeBySharedPreference(String nickName, String sex, String editPhone, String editPerson) {
        SharedPreferences preferences = getActivity().getSharedPreferences("user", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("nickName", nickName);
        editor.putString("sex", sex);
        editor.putString("editPhone", editPhone);
        editor.putString("editPerson", editPerson);
        editor.putString("imagePath",myPath);
        editor.apply();
    }

    public void showPopupMenu(View v){
        PopupMenu popupMenu = new PopupMenu(getActivity(),v);
        popupMenu.getMenuInflater().inflate(R.menu.choose_pic,popupMenu.getMenu());
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener(){
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch(item.getItemId()) {
                    case R.id.open_camera:
                        openCamera();
                        break;
                    case R.id.open_album:
                        if (ContextCompat.checkSelfPermission(mContext, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                            ActivityCompat.requestPermissions((Activity)mContext , new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, OPEN_ALBUM);
                        } else {
                            openAlbum();
                        }
                        break;
                    default:
                        break;
                }
                return true;
            }
        });
        popupMenu.setOnDismissListener(new PopupMenu.OnDismissListener() {
            @Override
            public void onDismiss(PopupMenu menu) {

            }
        });
        popupMenu.show();
    }


    Uri imageUri;
    private void openCamera() {
        File outputImage = new File(getActivity().getExternalCacheDir(), "output_image.img");
        // 创建指定名称的 file对象 ，存储位置为 sd卡的应用关联缓存目录
        //   /storage/13F0-2214/android/data/包名/cache
        try {
            if (outputImage.exists()) {
                outputImage.delete();
            }
            outputImage.createNewFile(); // 通过file对象创建实际文件
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (Build.VERSION.SDK_INT < 24) {
            // 如果android7.0 版本以下，使用该方法将 创建的文件转化成 uri对象
            imageUri = Uri.fromFile(outputImage);

        } else {
            // 7.0 版本及以上，利用内容提供器加载 获取uri对象， 注意要在AndroidManiFest.xml 中注册
            // 注册该内容提供器
            imageUri = FileProvider.getUriForFile(mContext, "com.example.ethanwalker.provider", outputImage);

        }
        // 隐式启动相机，指定相机对应的 action
        Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
        startActivityForResult(intent, TAKE_PHOTO);
    }
    private void openAlbum(){
        Intent intent = new Intent("android.intent.action.GET_CONTENT");
        intent.setType("image/*");
        startActivityForResult(intent,CHOOSE_PHOTO);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case OPEN_ALBUM:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    openAlbum();
                } else {
                    Toast.makeText(mContext, "你拒绝打开相册...", Toast.LENGTH_SHORT).show();
                }
                break;
            default:
                break;


        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case TAKE_PHOTO:
                if (resultCode == RESULT_OK) {
                    try {
                        // 将图片uri对象打开，获取 InputStream 流  :getContentResolver().openInputStream()
                        // 根据图片流解码成 Bitmap 对象 : BitFactory.decodeStream()
                        Bitmap bitmap = BitmapFactory.decodeStream(getActivity().getContentResolver().openInputStream(imageUri));
                        InputStream inputStream = getActivity().getContentResolver().openInputStream(imageUri);
                        String cameraPath = getActivity().getExternalCacheDir()+"/myCameraPhoto";
                        FileOutputStream fileOutputStream = new FileOutputStream(cameraPath);
                        byte[] bys = new byte[1024];
                        while((inputStream.read(bys))!=-1){
                            fileOutputStream.write(bys);
                        }
                        bys.clone();
                        inputStream.close();
                        myPath = cameraPath;
                        headImg.setImageBitmap(bitmap);

                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                break;
            case CHOOSE_PHOTO:
                if(resultCode ==  RESULT_OK){
                    // 判断Android系统版本，在4.4(KitKat)之前/之后 有不同的处理方式
                    if(Build.VERSION.SDK_INT>=19){
                        handleImageOnKitKat(data);
                    }else{
                        handleImageBeforeKitKat(data);
                    }
                }else{
                    Toast.makeText(mContext, "选择图片失败", Toast.LENGTH_SHORT).show();
                }
                break;
            default:
                break;
        }
    }


    private void handleImageOnKitKat(Intent data) {
        String imagePath = null;
        Uri uri = data.getData();
        Log.d("TAG", "handleImageOnKitKat: uri is " + uri);
        if (DocumentsContract.isDocumentUri(mContext, uri)) {
            // 如果是document类型的Uri，则通过document id处理
            String docId = DocumentsContract.getDocumentId(uri);
            if("com.android.providers.media.documents".equals(uri.getAuthority())) {
                String id = docId.split(":")[1]; // 解析出数字格式的id
                String selection = MediaStore.Images.Media._ID + "=" + id;
                imagePath = getImagePath(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, selection);
            } else if ("com.android.providers.downloads.documents".equals(uri.getAuthority())) {
                Uri contentUri = ContentUris.withAppendedId(Uri.parse("content://downloads/public_downloads"), Long.valueOf(docId));
                imagePath = getImagePath(contentUri, null);
            }
        } else if ("content".equalsIgnoreCase(uri.getScheme())) {
            // 如果是content类型的Uri，则使用普通方式处理
            imagePath = getImagePath(uri, null);
        } else if ("file".equalsIgnoreCase(uri.getScheme())) {
            // 如果是file类型的Uri，直接获取图片路径即可
            imagePath = uri.getPath();
        }
        displayImage(imagePath); // 根据图片路径显示图片
    }

    private void handleImageBeforeKitKat(Intent data) {
        Uri uri = data.getData();
        String imagePath = getImagePath(uri, null);
        displayImage(imagePath);
    }

    // 根据uri对象获取图片的真实路径
    private String getImagePath(Uri uri, String selection) {
        String path = null;
        // 通过Uri和selection来获取真实的图片路径
        Cursor cursor = getActivity().getContentResolver().query(uri, null, selection, null, null);
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
            }
            cursor.close();
        }
        myPath = path;
        return path;
    }
    // 显示图片
    private void displayImage(String imagePath) {
        if(imagePath!=null){
            Bitmap bitmap = BitmapFactory.decodeFile(imagePath);
            headImg.setImageBitmap(bitmap);
        }
    }


    private String myPath;
    private static final String TAG = "MeFragment";
}
