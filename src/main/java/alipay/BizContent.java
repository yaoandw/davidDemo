package alipay;

import com.alipay.api.internal.util.json.JSONValidatingWriter;
import jdk.nashorn.internal.ir.debug.JSONWriter;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by yaoandw on 2017/1/17.
 */
public class BizContent {
    private String body;
    private String subject;
    private String out_trade_no;
    private String timeout_express;
    private String total_amount;
    private String seller_id;
    private String product_code;

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getOut_trade_no() {
        return out_trade_no;
    }

    public void setOut_trade_no(String out_trade_no) {
        this.out_trade_no = out_trade_no;
    }

    public String getTimeout_express() {
        return timeout_express;
    }

    public void setTimeout_express(String timeout_express) {
        this.timeout_express = timeout_express;
    }

    public String getTotal_amount() {
        return total_amount;
    }

    public void setTotal_amount(String total_amount) {
        this.total_amount = total_amount;
    }

    public String getSeller_id() {
        return seller_id;
    }

    public void setSeller_id(String seller_id) {
        this.seller_id = seller_id;
    }

    public String getProduct_code() {
        return product_code;
    }

    public void setProduct_code(String product_code) {
        this.product_code = product_code;
    }

    public String descrption(){
        Map tmpDict = new HashMap();
        // NOTE: 增加不变部分数据
        tmpDict.put("subject",this.subject == null?"":this.subject);
        tmpDict.put("out_trade_no",this.out_trade_no == null?"":this.out_trade_no);
        tmpDict.put("total_amount",this.total_amount == null?"":this.total_amount);
        tmpDict.put("seller_id",this.seller_id == null?"":this.seller_id);
        tmpDict.put("product_code",this.product_code == null?"QUICK_MSECURITY_PAY":this.product_code);

        // NOTE: 增加可变部分数据
        if (this.body != null && this.body.length() > 0) {
        tmpDict.put("body",this.body);
        }

        if (this.timeout_express != null && this.timeout_express.length() > 0) {
            tmpDict.put("timeout_express",this.timeout_express);
        }

        return new JSONValidatingWriter().write(tmpDict);
    }
}
