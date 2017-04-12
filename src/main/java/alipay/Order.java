package alipay;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.*;

/**
 * Created by yaoandw on 2017/1/17.
 */
public class Order {
    private String app_id;
    private String method;
    private String format;
    private String return_url;
    private String charset;
    private String timestamp;
    private String version;
    private String notify_url;
    private String app_auth_token;
    private BizContent biz_content;
    private String sign_type;

    public String getApp_id() {
        return app_id;
    }

    public void setApp_id(String app_id) {
        this.app_id = app_id;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public String getFormat() {
        return format;
    }

    public void setFormat(String format) {
        this.format = format;
    }

    public String getReturn_url() {
        return return_url;
    }

    public void setReturn_url(String return_url) {
        this.return_url = return_url;
    }

    public String getCharset() {
        return charset;
    }

    public void setCharset(String charset) {
        this.charset = charset;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getNotify_url() {
        return notify_url;
    }

    public void setNotify_url(String notify_url) {
        this.notify_url = notify_url;
    }

    public String getApp_auth_token() {
        return app_auth_token;
    }

    public void setApp_auth_token(String app_auth_token) {
        this.app_auth_token = app_auth_token;
    }

    public BizContent getBiz_content() {
        return biz_content;
    }

    public void setBiz_content(BizContent biz_content) {
        this.biz_content = biz_content;
    }

    public String getSign_type() {
        return sign_type;
    }

    public void setSign_type(String sign_type) {
        this.sign_type = sign_type;
    }

    public String orderInfoEncoded(boolean bEncoded) throws UnsupportedEncodingException {
        if (this.app_id.length() <= 0) {
            return null;
        }

        // NOTE: 增加不变部分数据
        Map<String,String> tmpDict = new HashMap();
        tmpDict.put("app_id",this.app_id);
        tmpDict.put("method",this.method!=null?this.method:"alipay.trade.app.pay");
        tmpDict.put("charset",this.charset!=null?this.charset:"utf-8");
        tmpDict.put("timestamp",this.timestamp!=null?this.timestamp:"");
        tmpDict.put("version",this.version!=null?this.version:"1.0");
        tmpDict.put("biz_content",this.biz_content.descrption()!=null?this.biz_content.descrption():"");
        tmpDict.put("sign_type",this.sign_type!=null?this.sign_type:"RSA");


        // NOTE: 增加可变部分数据
        if (this.format != null && this.format.length() > 0) {
            tmpDict.put("format",this.format);
        }

        if (this.return_url != null && this.return_url.length() > 0) {
            tmpDict.put("return_url",this.return_url);
        }

        if (this.notify_url != null && this.notify_url.length() > 0) {
            tmpDict.put("notify_url",this.notify_url);
        }

        if (this.app_auth_token != null && this.app_auth_token.length() > 0) {
            tmpDict.put("app_auth_token",this.app_auth_token);
        }

        // NOTE: 排序，得出最终请求字串
        List<String> sortedKeyArray = new ArrayList<String>(tmpDict.keySet());
        Collections.sort(sortedKeyArray, new Comparator<String>() {
            public int compare(String arg0, String arg1) {
                return arg0.compareTo(arg1);
            }
        });

        List<String> tmpArray = new ArrayList();
        for (String key : sortedKeyArray) {
            String orderItem = this.orderItemWithKey(key ,tmpDict.get(key),bEncoded);
            if (orderItem.length() > 0) {
                tmpArray.add(orderItem);
            }
        }
        String rtnStr = "";
        for (String orderItem : tmpArray){
            rtnStr += orderItem+"&";
        }
        if (rtnStr.length() > 0){
            rtnStr = rtnStr.substring(0,rtnStr.length()-1);
        }
        return rtnStr;
    }

    private String orderItemWithKey(String key ,String value ,boolean bEncoded) throws UnsupportedEncodingException {
        if (key.length() > 0 && value.length() > 0) {
            if (bEncoded) {
                value = URLEncoder.encode(value,"utf-8");
            }
            return key+"="+value;
        }
        return null;
    }
}
