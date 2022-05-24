package cn.edu.hit.request;

import android.app.Activity;
import android.util.Log;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.Map;

public class HttpRequest {
    private Activity context;

    public HttpRequest(Activity context) {
        this.context = context;
    }

    /**
     * HttpURLConnection Get 方式请求
     * 拼接后的完整路径：http://10.250.0.1:8080/LoginInfo?name=admin&pwd=123456
     */
    public String get(String urlStr, Map<String, String> map) {

        StringBuilder result = new StringBuilder();   //StringBuilder用于单线程多字符串拼接，返回参数

        // 拼接路径地址 拼接后的完整路径：http://192.168.101.10:9090/Login_Server/login?name=admin&pwd=123456
        StringBuilder pathString = new StringBuilder(urlStr);
        pathString.append("?");
        pathString.append(getStringFromEntry(map));

        // 以下是 HttpURLConnection Get 访问 代码
        try {
            // 第一步 包装网络地址
            URL url = new URL(pathString.toString());
            // 第二步 创建连接对象
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            // 第三步 设置请求方式Get
            httpURLConnection.setRequestMethod("GET");
            // 第四步 设置读取和连接超时时长
            httpURLConnection.setReadTimeout(5000);
            httpURLConnection.setConnectTimeout(5000);
            // 第五步 发生请求 ⚠注意：只有在httpURLConnection.getResponseCode()非-1时，才向服务器发请求
            int responseCode = httpURLConnection.getResponseCode();
            // 第六步 判断请求码是否成功  注意：只有在执行conn.getResponseCode() 的时候才开始向服务器发送请求
            if (responseCode == HttpURLConnection.HTTP_OK) {
                // 第七步 获取服务器响应的流
                BufferedReader reader = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream()));
                String temp;
                while ((temp = reader.readLine()) != null) {
                    result.append(temp);
                }
            } else {
                return "failed";
            }
            httpURLConnection.disconnect();
        } catch (MalformedURLException e) {
            e.printStackTrace();
            threadRunToToast("登录失败，请检查网络！");
        } catch (IOException e) {
            e.printStackTrace();
            threadRunToToast("IO发生异常");
        }
        threadRunToToast(result.toString());
        return result.toString();
    }

    /**
     * HttpURLConnection Post式请求
     * 路径：http://192.168.101.10:9090/Login_Server/login
     * 参数：
     * name=admin
     * pwd=123456
     */
    public String post(String urlStr, Map<String, String> map) {

        StringBuilder result = new StringBuilder();  //StringBuilder用于单线程多字符串拼接，返回参数
        String paramsString = getStringFromEntry(map);  //获取拼接参数：name=admin&pwd=123456

        // 以下是 HttpURLConnection Post 访问 代码
        try {
            // 第一步 包装网络地址
            URL url = new URL(urlStr);
            // 第二步 创建连接对象
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            // 第三步 设置请求方式 POST
            conn.setRequestMethod("POST");
            // 第四步 设置读取和连接超时时长
            conn.setReadTimeout(5000);
            conn.setConnectTimeout(5000);
            // 第五步 允许对外输出
            conn.setDoOutput(true);
            conn.setInstanceFollowRedirects(true);
            // 第六步 得到输出流 并把实体输出写出去
            OutputStream outputStream = conn.getOutputStream();
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
            writer.write(paramsString);
            writer.flush();
            writer.close();
            outputStream.close();
            // 第七步 判断请求码是否成功 注意：只有在执行conn.getResponseCode() 的时候才开始向服务器发送请求
            if (conn.getResponseCode() == HttpURLConnection.HTTP_OK) {
                // 第八步 获取服务器响应的流
                BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                String temp;
                while ((temp = reader.readLine()) != null) {
                    result.append(temp);
                }
            } else {
                return "failed";
            }
            conn.disconnect();
        } catch (MalformedURLException e) {
            e.printStackTrace();
            threadRunToToast("登录失败，请检查网络！");
        } catch (IOException e) {
            e.printStackTrace();
            threadRunToToast("IO发生异常");
        }
        return result.toString();
    }

    /**
     * 将map转换成key1=value1&key2=value2的形式
     *
     * @return
     * @throws UnsupportedEncodingException
     */
    private String getStringFromEntry(Map<String, String> map) {

        StringBuilder sb = new StringBuilder(); //StringBuilder用于单线程多字符串拼接
        boolean isFirst = true;
        try {
            for (Map.Entry<String, String> entry : map.entrySet()) {
                if (isFirst)
                    isFirst = false;
                else
                    sb.append("&");
                sb.append(URLEncoder.encode(entry.getKey(), "UTF-8"));
                sb.append("=");
                sb.append(URLEncoder.encode(entry.getValue(), "UTF-8"));
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return sb.toString();
    }

    /**
     * 在子线程中提示，属于UI操作
     */
    private void threadRunToToast(final String text) {
        context.runOnUiThread(() -> Toast.makeText(context, text, Toast.LENGTH_LONG).show());
    }
}
