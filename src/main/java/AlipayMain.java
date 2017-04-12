import alipay.BizContent;
import alipay.Order;
import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.internal.util.AlipaySignature;
import com.alipay.api.internal.util.codec.Base64;
import com.alipay.api.request.AlipayTradeQueryRequest;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by yaoandw on 2017/1/17.
 */
public class AlipayMain {
    public static void main(String[] args) throws UnsupportedEncodingException, AlipayApiException {
        String app_id = "2017010604887678";
        String private_key = "MIIEvgIBADANBgkqhkiG9w0BAQEFAASCBKgwggSkAgEAAoIBAQDZ0rYzaKKf/XrM93/5FNr6k1vtJVng3ldWoknqsSWW8Aib+ppZIGdOf1Ww8P/S4Ba4EQsc2Ap9skr3JgTrWecxCcnmBjvKp+PPXoOfhjczvVI4vZoGTTe1tspsdhlaMMfmi9ndhhkCxnyF0BAtBHM+HoAsqUzYNSAof3qbd6UdjbLxs8FFa/2NnJ9AilO12+aEZFDjx/wq3uW5LDVISFPLDAQeuRp9PTQF46c0POQwBIX2EC8qFCKdQWnzZH2M6XsGVvDNrhLTtsSkOHOMVn8hgdhhR1ABQTfbNl0ZA0eA0DlD3eIDPi2D0DaGn77/xGtmUfp0YhARtytb6Hnh5UwXAgMBAAECggEBAJ+3Jy3uzT+upzpfSBClQCN3pXHvb0W5AiVd/gD+P2e7PTTRvhcX8WfXakfBjnNbKI1ywck2iAq+jsVFtVm0pBaK7OWjMHhaPmoxDSYNTbC8YuuD6sowGGc/TpHUzqYZGpprdSY/S1uDmo6rjxDTMIUd7DHTOatW2OuY/Ze9VO6jOuJtwsMmNSD/B0MRgU2Gu3KdENhrVKAj7HEBjIbhXSvx0Q6GTweSqTOZRTORHdmBjM7GmA4LtrFiw2Hcy6lJ1nsKipxc+6A9oR4VxOnyqxedqJTG0SmrI0Qy1v6ejXwuuyI7D4GW7CICJD/APu2UHxoIEcMhrY7+EVmf5JEJWkECgYEA+tT9mWE/JXlV7pV/dwgvsCSR+dAecgpMHrK25rifuH/t5RU92Trf02gsdCdZzw+QSxT6Z7Kpaz3iUND9+x8B6IBYKPxpG1g1TFFSaHj4mm9Tilx3ki0/k8sOccuSwzm84guu2AIvjfBuRN9oNqoXWQaDHjgEt8k5BF+tNDLtQpECgYEA3k+dvVbz75krzpYBiYYXIzh+Ez2/T+my5fCuHAYTR+9VPkObKwYHaUxandvqm5tZKloi1l50RQVJ/3FO28+cVIpbs6zgC7AXLq4hQbQl6zRbEx4OPgBZfo/qZ0BTdfmi3OYLuGAKk3ePz5yv/aILEFEsRV6qPyUsrJNGvFGBqCcCgYBH18eHLe5bOmSdgF5Q5pxaFC9nYO/HdCGXTVLLxKeivamSysG7PXysXZMV9ctoXinTB4AtQWP7Hm96/FEyow/12wTlQF6OgXZKlKRI3hU+jpJb994blTA3kZpih49SsW/jQrtijUW3ntzGh0KfHZqwWggmGJ9pUI4vLYQ7hjaksQKBgFPgqDxjDWCuYxfsAyJ3RE2WqALsfj1RiJ03RQnvxOCUTY5Thb0i5jL1iw8ahZc2ctsG7TQCqU3NZBEMGFvZJjvDhDvzkwj15JlJO+UFanUm6OH6qhb8nMei1ycj3xmZGbjM10k2e2cOamVD0icZN6Ftbw2xXPy8MvHeE4dEHW5rAoGBAKO/gPg3Gn8DWz563UdvKVRoJzu/9FbJLMK8lupZqoylB9s+PP72ZN8Q5ynpAtPahsrj2ln7Yulo+dyXt/bdZ4068Z5YUrUxHH8phDqOEib7fWuhklfqwaH6C1BTp/Wzzj4/dGXQ1DA8oNhfzVaJfvNlJuxmAlH5qH5UgLjLlGh/";

        Order order = new Order();
        order.setApp_id(app_id);
        order.setMethod("alipay.trade.app.pay");
        order.setCharset("utf-8");
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        order.setTimestamp(format.format(new Date()));
        order.setVersion("1.0");
        order.setSign_type("RSA");

        BizContent bizContent = new BizContent();
        bizContent.setBody("我是测试数据");
        bizContent.setSubject("1");
        bizContent.setOut_trade_no("NO45735205357205720");
        bizContent.setTimeout_express("30m");
        bizContent.setTotal_amount("0.01");
        order.setBiz_content(bizContent);

        order.setNotify_url("http://www.shangtan.cn");

        //将商品信息拼接成字符串
        String orderInfo = order.orderInfoEncoded(false);
        String orderInfoEncoded = order.orderInfoEncoded(true);
        System.out.println("orderSpec="+orderInfo);

        // NOTE: 获取私钥并将商户信息签名，外部商户的加签过程请务必放在服务端，防止公私钥数据泄露；
        //       需要遵循RSA签名规范，并将签名字符串base64编码和UrlEncode
        String signedString = null;
        signedString = AlipaySignature.rsaSign(orderInfo,private_key,"utf-8","RSA");
        System.out.println("signedString="+signedString);
//        byte[] bytes = Base64.encodeBase64(signedString.getBytes());
//        signedString = new String(bytes);
        signedString = URLEncoder.encode(signedString,"utf-8");

        // NOTE: 将签名成功字符串格式化为订单字符串,请严格按照该格式
        String orderString = orderInfoEncoded + "&sign="+signedString;
        System.out.println("orderString="+orderString);
    }
}
