package edu.skku.sosil3.sky.telepathy;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.ContentBody;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import edu.skku.sosil3.sky.telepathy.MyMultipartEntity.ProgressListener;

@SuppressWarnings("deprecation")
public class ImageUploadTask extends AsyncTask<Void, Integer, Void> {

	private Context ctx;
	private String imgPath;

	private HttpClient client;

	private ProgressDialog pd;
	private long totalSize;

	String url;
	String imgFileName;

	String res_user; // 이름
	String res_content;
	double res_latitude;
	double res_longitude;
	private File file;

	public ImageUploadTask(Context context, String imgPath, String url,
			String res_user, String res_content, double res_latitude,
			double res_longitude) {
		super();
		this.ctx = context;
		this.imgPath = imgPath;
		this.url = url;
		this.res_user = res_user;
		this.res_content = res_content;
	}

	@Override
	protected void onPreExecute() {
		// Set timeout parameters
		int timeout = 10000;
		HttpParams httpParameters = new BasicHttpParams();
		HttpConnectionParams.setConnectionTimeout(httpParameters, timeout);
		HttpConnectionParams.setSoTimeout(httpParameters, timeout);

		// We'll use the DefaultHttpClient
		client = new DefaultHttpClient(httpParameters);

		pd = new ProgressDialog(ctx);
		pd.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
		pd.setMessage("Uploading Picture...");
		pd.setCancelable(false);
		pd.show();
	}

	@Override
	protected Void doInBackground(Void... params) {
		try {
			file = new File(imgPath);

			// Create the POST object
			HttpPost post = new HttpPost(url);

			// Create the multipart entity object and add a progress listener
			// this is a our extended class so we can know the bytes that have
			// been transfered
			MultipartEntity entity = new MyMultipartEntity(
					new ProgressListener() {
						@Override
						public void transferred(long num) {
							// Call the onProgressUpdate method with the percent
							// completed
							publishProgress((int) ((num / (float) totalSize) * 100));
							// Log.d("DEBUG", num + " / " + totalSize);
						}
					});
			// Add the file to the content's body
			ContentBody cbFile = new FileBody(file, "image/jpeg");
			entity.addPart("source", cbFile);

			// After adding everything we get the content's lenght
			totalSize = entity.getContentLength();

			// We add the entity to the post request
			post.setEntity(entity);

			// Execute post request
			HttpResponse response = client.execute(post);
			int statusCode = response.getStatusLine().getStatusCode();

			if (statusCode == HttpStatus.SC_OK) {
				// If everything goes ok, we can get the response
				imgFileName = EntityUtils.toString(response.getEntity());
				Log.d("DEBUG", imgFileName);

			} else {
				Log.d("DEBUG", "HTTP Fail, Response Code: " + statusCode);
			}

		} catch (ClientProtocolException e) {
			// Any error related to the Http Protocol (e.g. malformed url)
			e.printStackTrace();
		} catch (IOException e) {
			// Any IO error (e.g. File not found)
			e.printStackTrace();
		}finally{
			if(file!=null && file.exists()){
				file.delete();
			}
		}
		String res_image = Constants.URL_SERVER_HOST
				+ Constants.URI_GET_POSTIMAGE + '/' + imgFileName;

		TabActivity.upload_state = TabActivity.DEFAULT;

		String uriString = "http://telepathy.hyunha.org/post"; // 업로드할
																// url
		ArrayList<BasicNameValuePair> Parameter = new ArrayList<BasicNameValuePair>();

		try {
			URI uri = new URI(uriString);
			HttpClient httpclient = new DefaultHttpClient();
			HttpPost httpPost = new HttpPost(uri);

			Parameter.add(new BasicNameValuePair("p_user", res_user));
			Parameter.add(new BasicNameValuePair("p_image", res_image));
			Parameter.add(new BasicNameValuePair("p_content", res_content));
			Parameter.add(new BasicNameValuePair("p_lati", Double
					.toString(res_latitude)));
			Parameter.add(new BasicNameValuePair("p_long", Double
					.toString(res_longitude)));

			Log.d("Upload", "Uploading data");
			Log.d("Upload", "- " + res_user);
			Log.d("Upload", "- " + res_image);
			Log.d("Upload", "- " + res_content);
			Log.d("Upload", "- " + Double.toString(res_latitude));
			Log.d("Upload", "- " + Double.toString(res_longitude));

			httpPost.setEntity(new UrlEncodedFormEntity(Parameter, "UTF-8"));

			// JSON data 얻어내기
			HttpResponse httpResponse = httpclient.execute(httpPost);
			String responseString = EntityUtils.toString(
					httpResponse.getEntity(), HTTP.UTF_8);
			Log.d("Upload", "responseString");
			Log.d("Upload", responseString);

			// JSON data에서 success 여부 알아내기
			JSONObject jsonObject = new JSONObject(responseString);
			String success = jsonObject.getString("success");
			Log.d("Upload", "success : " + success);

			if (res_latitude != GPSLocationListener.DATA_GET_FAILED)
				TabActivity.upload_state = TabActivity.SUCCESS;
			else
				TabActivity.upload_state = TabActivity.LOCATION_NOT_FOUND;

		} catch (IOException e) {
			TabActivity.upload_state = TabActivity.NETWORK_ERROR;
		} catch (JSONException e) {
			TabActivity.upload_state = TabActivity.NETWORK_ERROR;
		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;
	}

	@Override
	protected void onProgressUpdate(Integer... progress) {
		// Set the pertange done in the progress dialog
		pd.setProgress((int) (progress[0]));
	}

	@Override
	protected void onPostExecute(Void result) {
		// Dismiss progress dialog
		pd.dismiss();

		if (TabActivity.upload_state == TabActivity.SUCCESS) { // 업로드 성공
			Log.d("Upload", "comment : SUCCESS");
			Toast.makeText(ctx, "성공적으로 업로드하였습니다.", Toast.LENGTH_SHORT).show();
			ctx.startActivity(new Intent(ctx, TabActivity.class));
			((TabActivity) ctx).finish();

			TabActivity ta = (TabActivity)ctx;
			((TextView)ta.findViewById(R.id.one_content)).setText("");
			((ImageView)ta.findViewById(R.id.one_image)).setImageResource(R.drawable.pushtopic);
			ta.tabHost.setCurrentTab(1);
			ta.refresh_all();

		} else if (TabActivity.upload_state == TabActivity.LOCATION_NOT_FOUND) { // 업로드
																					// 성공
																					// -
																					// 좌표
			// 찾기 실패
			Log.d("Upload", "comment : LOCATION_NOT_FOUND");
			Toast.makeText(ctx, "좌표 찾기에 실패하였습니다. 좌표에 대한 정보 없이 업로드합니다.",
					Toast.LENGTH_SHORT).show();
			TabActivity ta = (TabActivity)ctx;
			((TextView)ta.findViewById(R.id.one_content)).setText("");
			((ImageView)ta.findViewById(R.id.one_image)).setImageResource(R.drawable.pushtopic);
			ta.tabHost.setCurrentTab(1);
			ta.refresh_all();

		} else if (TabActivity.upload_state == TabActivity.NETWORK_ERROR) { // 업로드
																			// 실패
																			// -
																			// 네트워크
																			// 오류
			Log.d("Upload", "comment : NETWORK_ERROR");
			Toast.makeText(ctx, "네트워크 상태를 확인하십시오.", Toast.LENGTH_SHORT).show();

		} else { // 업로드 실패 - 알 수 없는 오류
			Log.d("Register", "comment : DEFAULT");
			Toast.makeText(ctx, "알 수 없는 오류가 발생했습니다.", Toast.LENGTH_SHORT)
					.show();
		}
	}

}
