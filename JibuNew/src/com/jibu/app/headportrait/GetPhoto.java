package com.jibu.app.headportrait;

import java.io.File;

import com.jibu.app.R;
import com.jibu.app.main.ToastUtil;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;


/**
 * 从相册或者相机拍照获得图片。
 * 
 * @author Administrator
 * 
 */
public class GetPhoto {

	private Context mContex;

	// 图片在SD卡中的路径
	public static final String FILE_SAVEPATH = IConstant.FILE_SAVEPATH
			+ "/img/";

	public String origFilePath; // 裁剪前图片绝对路径

	public static String protraitPath; // 裁剪后的图片的路径
	private File protraitFile;

	public static Uri origUri; // 原始图片统一资源

	public static Uri cropUri; // 裁剪后图片统一资源

	public String cropFileName;

	protected String origFileName;

	/** 请求相册 */
	public static final int REQUEST_CODE_GETIMAGE_BYSDCARD = 0;
	/** 请求相机 */
	public static final int REQUEST_CODE_GETIMAGE_BYCAMERA = 1;
	/** 请求裁剪 */
	public static final int REQUEST_CODE_GETIMAGE_BYCROP = 2;

	// 常量
	private final static int CROP = 300; // 图片大小

	/**
	 * activity中使用调用这个 构造函数
	 * 
	 * @param context
	 */
	public GetPhoto(Context context) {
		mContex = context;
	}

	private Fragment fg = null;

	/**
	 * fragment中调用使用这个。 构造函数
	 * 
	 * @param fg
	 */
	public GetPhoto(Fragment fg) {
		mContex = fg.getActivity();
		this.fg = fg;
	}

	/**
	 * 编辑图片操作选择
	 * 
	 * @param items
	 * @param imgPath
	 *            图片名称
	 */
	public void imageChooseItem(CharSequence[] items,
			final String origFileNameStr, final String cropFileNameStr) {
		AlertDialog imageDialog = new AlertDialog.Builder(mContex) 
				.setTitle("上传头像").setIcon(android.R.drawable.btn_star)
				.setItems(items, new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int item) {
						// 判断是否挂载了SD卡
						String storageState = Environment
								.getExternalStorageState();
						if (storageState.equals(Environment.MEDIA_MOUNTED)) {
							File savedir = new File(FILE_SAVEPATH);
							if (!savedir.exists()) {
								savedir.mkdirs(); // 创建文件夹
							}
						} else {
							ToastUtil.toast(mContex, R.string.save_head_wrong_check_sdcard);
							return;
						}

						origFileName = origFileNameStr; // 原始照片
						cropFileName = cropFileNameStr; // 裁剪后的照片

						// 裁剪图片的绝对路径
						protraitPath = FILE_SAVEPATH + cropFileName;
						protraitFile = new File(protraitPath);

						// origFilePath = FILE_SAVEPATH + origFileName;

						// 创建统一资源地址uri
						origUri = Uri.fromFile(new File(FILE_SAVEPATH,
								origFileName));
						cropUri = Uri.fromFile(protraitFile);

						// Log.d("origUri裁剪前资源：" + origUri + "裁剪后的资源:" +
						// cropUri);

						// 相册选图
						if (item == 0) {
							startActionPickCrop(cropUri);
						}
						// 手机拍照
						else if (item == 1) {
							startActionCamera(origUri);
						}
					}
				}).create();

		imageDialog.show();
	}

	public void imageChooseItem(int arrayRes, final String origFileNameStr,
			final String cropFileNameStr) {
		showSelectPictureDialog(mContex, origFileNameStr, cropFileNameStr);
	}

	/**
	 * 选择图片裁剪。
	 * 
	 * @param output
	 */
	public void startActionPickCrop(Uri output) {
		Intent intent = new Intent(Intent.ACTION_PICK);
		intent.setType("image/*");
		intent.putExtra("output", output); // 保存的资源
		intent.putExtra("crop", "true"); // 可裁剪
		intent.putExtra("aspectX", 1);// 裁剪框比例
		intent.putExtra("aspectY", 1);
		intent.putExtra("outputX", CROP);// 输出图片大小
		intent.putExtra("outputY", CROP);
		// 执行子Activity（结束）后，回调函数onActivityResult执行……
		if (fg != null) {
			fg.startActivityForResult(Intent.createChooser(intent, mContex.getString(R.string.choose_picture)),
					REQUEST_CODE_GETIMAGE_BYSDCARD);
		} else {
			((Activity) mContex).startActivityForResult(
					Intent.createChooser(intent, mContex.getString(R.string.choose_picture)),
					REQUEST_CODE_GETIMAGE_BYSDCARD);
		}
	}

	/**
	 * 选择图片裁剪。
	 * 
	 * @param output
	 */
	public void startActionPickCrop1(Uri output) {
		Intent intent = new Intent(Intent.ACTION_PICK);
		intent.setType("image/*");
		intent.putExtra("output", output); // 保存的资源
		intent.putExtra("crop", "true"); // 可裁剪
		// intent.putExtra("aspectX", 1);// 裁剪框比例
		// intent.putExtra("aspectY", 1);
		// intent.putExtra("outputX", CROP);// 输出图片大小
		// intent.putExtra("outputY", CROP);
		// 执行子Activity（结束）后，回调函数onActivityResult执行……
		if (fg != null) {
			fg.startActivityForResult(Intent.createChooser(intent, mContex.getString(R.string.choose_picture)),
					REQUEST_CODE_GETIMAGE_BYSDCARD);
		} else {
			((Activity) mContex).startActivityForResult(
					Intent.createChooser(intent, mContex.getString(R.string.choose_picture)),
					REQUEST_CODE_GETIMAGE_BYSDCARD);
		}
	}

	/**
	 * 相机拍照
	 * 
	 * @param output
	 */
	public void startActionCamera(Uri output) {
		Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		intent.putExtra(MediaStore.EXTRA_OUTPUT, output);
		if (fg != null) {
			fg.startActivityForResult(intent, REQUEST_CODE_GETIMAGE_BYCAMERA);
		} else {
			((Activity) mContex).startActivityForResult(intent,
					REQUEST_CODE_GETIMAGE_BYCAMERA);
		}
	}

	/**
	 * 拍照后裁剪
	 * 
	 * @param data
	 *            原始图片
	 * @param output
	 *            裁剪后图片
	 */
	public void startActionCrop(Uri data, Uri output) {
		Intent intent = new Intent("com.android.camera.action.CROP");
		intent.setDataAndType(data, "image/*");
		intent.putExtra("output", output);
		intent.putExtra("crop", "true");
		intent.putExtra("aspectX", 1);// 裁剪框比例
		intent.putExtra("aspectY", 1);
		intent.putExtra("outputX", CROP);// 输出图片大小
		intent.putExtra("outputY", CROP);
		if (fg != null) {
			fg.startActivityForResult(intent, REQUEST_CODE_GETIMAGE_BYCROP);
		} else {
			((Activity) mContex).startActivityForResult(intent,
					REQUEST_CODE_GETIMAGE_BYCROP);
		}
	}
	AlertDialog selectPictureDiag;
	private void showSelectPictureDialog(Context context, final String origFileNameStr, final String cropFileNameStr) {
	    selectPictureDiag = new AlertDialog.Builder(context).create();
		
		Window view = selectPictureDiag.getWindow();
//		view.setGravity(Gravity.BOTTOM);
        WindowManager.LayoutParams params = view.getAttributes();  
        params.dimAmount = 1.0f;  
        params.alpha = 1.0f;
        view.setAttributes(params);   
        
		selectPictureDiag.show();
		selectPictureDiag.setContentView(R.layout.sel_upload_head_dialog);
		TextView cancel_textView = (TextView) view.findViewById(R.id.id_textview_cancel_upload);
		ListView lv = (ListView) view.findViewById(R.id.id_listView_upload_list);
		
		
		cancel_textView.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				selectPictureDiag.dismiss();
			}
		});
		String[] strs = context.getResources().getStringArray(R.array.GetImgType);
		lv.setAdapter(new ArrayAdapter<String>(context, android.R.layout.simple_list_item_1, strs));
		lv.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				// 判断是否挂载了SD卡
				String storageState = Environment
						.getExternalStorageState();
				if (storageState.equals(Environment.MEDIA_MOUNTED)) {
					File savedir = new File(FILE_SAVEPATH);
					if (!savedir.exists()) {
						savedir.mkdirs(); // 创建文件夹
					}
				} else {
					ToastUtil.toast(mContex, R.string.save_head_wrong_check_sdcard);
					return;
				}

				origFileName = origFileNameStr; // 原始照片
				cropFileName = cropFileNameStr; // 裁剪后的照片

				// 裁剪图片的绝对路径
				protraitPath = FILE_SAVEPATH + cropFileName;
				protraitFile = new File(protraitPath);

				// origFilePath = FILE_SAVEPATH + origFileName;

				// 创建统一资源地址uri
				origUri = Uri.fromFile(new File(FILE_SAVEPATH,
						origFileName));
				cropUri = Uri.fromFile(protraitFile);

				// Log.d("origUri裁剪前资源：" + origUri + "裁剪后的资源:" +
				// cropUri);

				if (position == 0) { // 相册选图
					startActionPickCrop(cropUri);
				} else if (position == 1) {// 手机拍照
					startActionCamera(origUri);
				}
				selectPictureDiag.dismiss();
			}
		});
		
		
	}
}
