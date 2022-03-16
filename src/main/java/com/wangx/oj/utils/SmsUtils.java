package com.wangx.oj.utils;

import com.alibaba.druid.util.StringUtils;
import com.aliyuncs.CommonRequest;
import com.aliyuncs.CommonResponse;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.http.MethodType;
import com.aliyuncs.profile.DefaultProfile;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

@Component
@Slf4j
public class SmsUtils {

    @Autowired
    private RedisUtils redisUtils;

    @Value("${sms.accesskey-id}")
    private String accessKeyId;

    @Value("${sms.accesskey-secret}")
    private String accessKeySecret;

    @Value("${sms.sign-name}")
    private String signName;

    @Value("${sms.template-code}")
    private String templateCode;

    public void send(String phone){
        DefaultProfile profile = DefaultProfile.getProfile("cn-hangzhou", accessKeyId, accessKeySecret);
        IAcsClient client = new DefaultAcsClient(profile);

        CommonRequest request = new CommonRequest();
        request.setSysMethod(MethodType.POST);
        request.setSysDomain("dysmsapi.aliyuncs.com");
        request.setSysVersion("2017-05-25");
        request.setSysAction("SendSms");
        request.putQueryParameter("RegionId", "cn-hangzhou");
        request.putQueryParameter("PhoneNumbers", phone);
        request.putQueryParameter("SignName", signName);
        request.putQueryParameter("TemplateCode", templateCode);
        String code = randomNumber();
        //发送的验证码
        request.putQueryParameter("TemplateParam", "{\"code\":\""+code+"\"}");
        try {
            CommonResponse response = client.getCommonResponse(request);
            // 5 * 60 = 5分钟
            redisUtils.hmSet("sms", phone, code, 5*60);

            System.out.println(response.getData());
        } catch (ClientException e) {
            e.printStackTrace();
        }
    }

    public boolean checkSmsCode(String code, String phone) {
        String rightCode = (String) redisUtils.hmGet("sms", phone);
        log.info(code, rightCode);
        return StringUtils.equals(code ,rightCode);
    }

    public  String randomNumber(){
        Random random = new Random();
        StringBuffer str = new StringBuffer();
        for(int i=0;i<6;i++){
            int i1 = random.nextInt(10) ;
            str.append(i1);
        }

        return str.toString();
    }

}
