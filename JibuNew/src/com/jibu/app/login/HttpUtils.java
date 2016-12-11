package com.jibu.app.login;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpVersion;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.HttpHostConnectException;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.CoreConnectionPNames;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.widget.Toast;

@SuppressWarnings("deprecation")
public class HttpUtils {
	public static final int METHOD_GET = 0;
	public static final int METHOD_POST = 1;

	@SuppressWarnings("deprecation")
	private static HttpClient createHttpClient() {
		HttpParams params = new BasicHttpParams();
		params.setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, 5000);
		HttpProtocolParams.setVersion(params, HttpVersion.HTTP_1_1);
		HttpProtocolParams.setContentCharset(params,
				HTTP.DEFAULT_CONTENT_CHARSET);
		HttpProtocolParams.setUseExpectContinue(params, true);

		SchemeRegistry schReg = new SchemeRegistry();
		schReg.register(new Scheme("http", PlainSocketFactory
				.getSocketFactory(), 80));
		schReg.register(new Scheme("https",
				SSLSocketFactory.getSocketFactory(), 443));
		ClientConnectionManager conMgr = new ThreadSafeClientConnManager(
				params, schReg);

		return new DefaultHttpClient(conMgr, params);
	}

	public static String getJson(String url, String[] keys, String[] values,
			int method) {
		Log.i("log","getJson:url="+url);
		HttpEntity entity = getEntity(url, keys, values, method);
		if (entity != null) {
			byte[] bytes;
			try {
				bytes = EntityUtils.toByteArray(entity);
				return new String(bytes, "UTF-8");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return null;
			}
		}
		return null;
	}

	@SuppressWarnings("deprecation")
	private static HttpEntity getEntity(String url, String[] keys,
			String[] values, int method) {
		HttpClient httpClient = createHttpClient();
		httpClient.getParams().setParameter(
				CoreConnectionPNames.CONNECTION_TIMEOUT, 5000);
		HttpUriRequest request = null;
		switch (method) {
		case METHOD_GET:
			StringBuilder sb = new StringBuilder(url);
			if (null != keys && null != values && keys.length != 0
					&& keys.length == values.length) {
				sb.append("?");
				for (int i = 0; i < values.length; i++) {
					sb.append(keys[i]).append("=").append(values[i])
							.append("&");
				}
				sb.deleteCharAt(sb.length() - 1);
			}
			Log.i("log","sb.toString()="+sb.toString());
			request = new HttpGet(sb.toString());
			break;
		case METHOD_POST:
			request = new HttpPost(url);
			request.setHeader("Content-type",
					"application/x-www-form-urlencoded;charset=UTF-8");
			request.setHeader("Authorization",
					"key=AIzaSyBJAhCVeeqIErwTfYwy-t83_EwvZlCFo9I");
			if (null != keys && null != values) {
				List<NameValuePair> list = new ArrayList<NameValuePair>();
				for (int i = 0; i < values.length; i++) {
					list.add(new BasicNameValuePair(keys[i], values[i]));
				}
				try {
					((HttpPost) request).setEntity(new UrlEncodedFormEntity(
							list));
				} catch (UnsupportedEncodingException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
			break;
		}
		try {
			HttpEntity entity = null;
			HttpResponse response = httpClient.execute(request);
			Log.i("log","response.getStatusLine().getStatusCode()="+response.getStatusLine().getStatusCode());
			if (response.getStatusLine().getStatusCode() == 200) {
				entity = response.getEntity();
			}
			return entity;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		} finally {
			request.abort();
		}
	}

	public static boolean getByteAndSave(String url, String[] keys,
			String[] values, int method, String path) {
		HttpClient httpClient = createHttpClient();
		httpClient.getParams().setParameter(
				CoreConnectionPNames.CONNECTION_TIMEOUT, 5000);
		HttpUriRequest request = null;
		switch (method) {
		case METHOD_GET:
			StringBuilder sb = new StringBuilder(url);
			if (null != keys && null != values && keys.length != 0
					&& keys.length == values.length) {
				sb.append("?");
				for (int i = 0; i < values.length; i++) {
					sb.append(keys[i]).append("=").append(values[i])
							.append("&");
				}
				sb.deleteCharAt(sb.length() - 1);
			}
			request = new HttpGet(sb.toString());
			break;
		case METHOD_POST:
			request = new HttpPost(url);
			request.setHeader("Content-type",
					"application/x-www-form-urlencoded;charset=UTF-8");
			request.setHeader("Authorization",
					"key=AIzaSyBJAhCVeeqIErwTfYwy-t83_EwvZlCFo9I");
			if (null != keys && null != values) {
				List<NameValuePair> list = new ArrayList<NameValuePair>();
				for (int i = 0; i < values.length; i++) {
					list.add(new BasicNameValuePair(keys[i], values[i]));
				}
				try {
					((HttpPost) request).setEntity(new UrlEncodedFormEntity(
							list));
				} catch (UnsupportedEncodingException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
			break;
		}
		try {
			HttpEntity entity = null;
			HttpResponse response = httpClient.execute(request);
			if (response.getStatusLine().getStatusCode() == 200) {
				entity = response.getEntity();
				InputStream inputStream = getStream(entity);
				return true;
			}
		} catch (IllegalStateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			request.abort();
		}
		return false;
	}

	@SuppressWarnings("deprecation")
	public static InputStream getStream(HttpEntity entity)
			throws IllegalStateException, IOException {
		if (entity != null) {
			return entity.getContent();
		}
		return null;
	}

	public static Bitmap getBitmapForSer(String url, String[] keys,
			String[] values, int method) throws IOException {
		HttpEntity entity = getEntity(url, keys, values, method);
		Bitmap bitmap = null;
		if (entity != null) {
			bitmap = BitmapFactory.decodeStream(entity.getContent());
		}
		return bitmap;
	}

	// public static Bitmap getBitmapForSer(String url, String[] keys,
	// String[] values, int method, int width, int height)
	// throws IOException {
	// HttpEntity entity = getEntity(url, keys, values, method);
	// byte[] data = EntityUtils.toByteArray(entity);
	// return BitmapUtils.compBitmap(data, width, height);
	// }

	/* 涓婁紶鏂囦欢鑷砈erver鐨勬柟娉�*/
	@SuppressLint("ShowToast")
	public static void uploadFile(String imageName, File file,
			String uploadUrl, Context context) {
		String end = "\r\n";
		String twoHyphens = "--";
		String boundary = "*****";
		try {
			URL url = new URL(uploadUrl);
			HttpURLConnection con = (HttpURLConnection) url.openConnection();
			/* 鍏佽Input銆丱utput锛屼笉浣跨敤Cache */
			con.setDoInput(true);
			con.setDoOutput(true);
			con.setUseCaches(false);
			/* 璁剧疆浼犻�鐨刴ethod=POST */
			con.setRequestMethod("POST");
			/* setRequestProperty */
			con.setRequestProperty("Connection", "Keep-Alive");
			con.setRequestProperty("Charset", "UTF-8");
			con.setRequestProperty("Content-Type",
					"multipart/form-data;boundary=" + boundary);
			/* 璁剧疆DataOutputStream */
			DataOutputStream ds = new DataOutputStream(con.getOutputStream());
			ds.writeBytes(twoHyphens + boundary + end);
			ds.writeBytes("Content-Disposition: form-data; "
					+ "name=\"file1\";filename=\"" + imageName + "\"" + end);
			ds.writeBytes(end);
			/* 鍙栧緱鏂囦欢鐨凢ileInputStream */
			FileInputStream fStream = new FileInputStream(file);
			/* 璁剧疆姣忔鍐欏叆1024bytes */
			int bufferSize = 1024;
			byte[] buffer = new byte[bufferSize];
			int length = -1;
			/* 浠庢枃浠惰鍙栨暟鎹嚦缂撳啿鍖�*/
			while ((length = fStream.read(buffer)) != -1) {
				/* 灏嗚祫鏂欏啓鍏ataOutputStream涓�*/
				ds.write(buffer, 0, length);
			}
			ds.writeBytes(end);
			ds.writeBytes(twoHyphens + boundary + twoHyphens + end);
			/* close streams */
			fStream.close();
			ds.flush();
			/* 鍙栧緱Response鍐呭 */
			InputStream is = con.getInputStream();
			int ch;
			StringBuffer b = new StringBuffer();
			while ((ch = is.read()) != -1) {
				b.append((char) ch);
			}
			/* 灏哛esponse鏄剧ず浜嶥ialog */
			// showDialog("涓婁紶鎴愬姛" + b.toString().trim());
			/* 鍏抽棴DataOutputStream */
			Toast.makeText(context, "澶村儚涓婁紶鎴愬姛", Toast.LENGTH_LONG);
			ds.close();
		} catch (Exception e) {
			// showDialog("涓婁紶澶辫触" + e);
			Toast.makeText(context, "澶村儚涓婁紶澶辫触", Toast.LENGTH_LONG);
		}

	}

	@SuppressWarnings("deprecation")
	public static String getJsonAsNet(String url, String[] keys, String[] values) {
		HttpPost post = new HttpPost(url);
		post.addHeader("Content-Type", "application/json; charset=utf-8");
		JSONObject jsonParams = new JSONObject();
		if (null != keys && null != values) {
			for (int i = 0; i < values.length && values != null; i++) {
				try {
					jsonParams.put(keys[i], values[i]);
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}
		}

		HttpEntity body;
		String result = null;
		try {
			body = new StringEntity(jsonParams.toString(), "utf8");
			post.setEntity(body);
			HttpResponse response = new DefaultHttpClient().execute(post);
			if (response.getStatusLine().getStatusCode() == 200) {
				body = response.getEntity();
				result = EntityUtils.toString(body,
						EntityUtils.getContentCharSet(body));
			}
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return result;
	}

	public static Bitmap getImage(String path) throws Exception {
		URL url = new URL(path);
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		conn.setReadTimeout(10 * 1000);
		conn.setConnectTimeout(10 * 1000);
		conn.setRequestMethod("GET");
		InputStream is = null;
		if (conn.getResponseCode() == HttpURLConnection.HTTP_OK) {
			is = conn.getInputStream();
		} else {
			is = null;
		}
		if (is == null) {
			return null;
		} else {
			try {
				byte[] data = readStream(is);
				if (data != null) {
					Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0,
							data.length);
					return bitmap;
				}
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				if (is != null)
					is.close();
			}
			return null;
		}
	}

	/*
	 * 寰楀埌鍥剧墖瀛楄妭娴�鏁扮粍澶у皬
	 */
	public static byte[] readStream(InputStream inStream) throws Exception {
		ByteArrayOutputStream outStream = new ByteArrayOutputStream();
		byte[] buffer = new byte[1024];
		int len = 0;
		while ((len = inStream.read(buffer)) != -1) {
			outStream.write(buffer, 0, len);
		}
		outStream.close();
		inStream.close();
		return outStream.toByteArray();
	}

}
