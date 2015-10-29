package com.suneee.openfire.plugin;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cn.jpush.api.JPushClient;
import cn.jpush.api.common.resp.APIConnectionException;
import cn.jpush.api.common.resp.APIRequestException;
import cn.jpush.api.push.PushResult;
import cn.jpush.api.push.model.Message;
import cn.jpush.api.push.model.Options;
import cn.jpush.api.push.model.Platform;
import cn.jpush.api.push.model.PushPayload;
import cn.jpush.api.push.model.audience.Audience;
import cn.jpush.api.push.model.audience.AudienceTarget;
import cn.jpush.api.push.model.notification.AndroidNotification;
import cn.jpush.api.push.model.notification.IosNotification;
import cn.jpush.api.push.model.notification.Notification;

public class JPushAllUtil {
    public  static JPushClient jpushClient=null;
    private static final Logger Log = LoggerFactory.getLogger(JPushAllUtil.class); 
    public static void main(String[] args) {
    	System.out.println("这是啥么意思");
    	Log.info("这是啥么意思");
    	/*String appKey ="c2c95e81e055b68348538fc4";    
        String masterSecret = "9e6be0cb6f43f35b6e3e6753";
        String alert = "测试ALERT";
        String msgContent = "测试MSG_CONTENT";
        String iosUsername="210suneeedev";
    	sendPush(appKey,masterSecret,alert,msgContent,iosUsername);*/
    }
    
    
    
	public static void sendPush(String appKey ,String masterSecret,String alert,Integer badge,String msgContent,String iosUsername) {
		jpushClient = new JPushClient(masterSecret, appKey);
		 //生成推送的内容，这里我们先测试全部推送
        PushPayload payload=buildPushObject_all_alias_alert(alert,badge,msgContent,iosUsername);
        Log.info("ios 极光推送");
        try {
            PushResult result = jpushClient.sendPush(payload);
        } catch (APIConnectionException e) {
        	e.printStackTrace();
        } catch (APIRequestException e) {
        	e.printStackTrace();
        }
	}
    public static PushPayload buildPushObject_all_alias_alert(String alert,Integer badge,String msgContent,String iosUsername) {
        return PushPayload.newBuilder()
                .setPlatform(Platform.all())//设置接受的平台
                .setAudience(Audience.newBuilder().addAudienceTarget(AudienceTarget.alias(iosUsername)).build())
               // .setAudience(Audience.tag(tagValue))//Audience设置为all，说明采用广播方式推送，所有用户都可以接收到
                .setNotification(Notification.alert(alert))
                .setNotification(Notification.ios_set_badge(badge))
                .setMessage(Message.content(msgContent))
                .setOptions(Options.newBuilder().setApnsProduction(false).build())
                .build();
    }
    
//    public static PushPayload buildPushObject_android_tag_alertWithTitle() {
//        return PushPayload.newBuilder()
//                .setPlatform(Platform.android())
//                .setAudience(Audience.all())
//                .setNotification(Notification.android(ALERT, TITLE, null))
//                .build();
//    }
    
    public static PushPayload buildPushObject_android_and_ios() {
        return PushPayload.newBuilder()
                .setPlatform(Platform.android_ios())
                .setAudience(Audience.tag("tags"))
                .setNotification(Notification.newBuilder()
                		.setAlert("alert content")
                		.addPlatformNotification(AndroidNotification.newBuilder()
                				.setTitle("IOS Title").build())
                		.addPlatformNotification(IosNotification.newBuilder().incrBadge(1).addExtra("extra_key", "extra_value").build())
                		.build())
                .build();
    }
    
//    public static PushPayload buildPushObject_ios_tagAnd_alertWithExtrasAndMessage() {
//        return PushPayload.newBuilder()
//                .setPlatform(Platform.ios())
//                .setAudience(Audience.tag_and("tag1", "tag_all"))
//                .setNotification(Notification.newBuilder()
//                        .addPlatformNotification(IosNotification.newBuilder()
//                                .setAlert(ALERT)
//                                .setBadge(5)
//                                .setSound("happy")
//                                .addExtra("from", "JPush")
//                                .build())
//                        .build())
//                 .setMessage(Message.content(MSG_CONTENT))
//                 .setOptions(Options.newBuilder()
//                         .setApnsProduction(true)
//                         .build())
//                 .build();
//    }
    
//    public static PushPayload buildPushObject_ios_audienceMore_messageWithExtras() {
//        return PushPayload.newBuilder()
//                .setPlatform(Platform.android_ios())
//                .setAudience(Audience.newBuilder()
//                        .addAudienceTarget(AudienceTarget.tag("tag1", "tag2"))
//                        .addAudienceTarget(AudienceTarget.alias("alias1", "alias2"))
//                        .build())
//                .setMessage(Message.newBuilder()
//                        .setMsgContent(MSG_CONTENT)
//                        .addExtra("from", "JPush")
//                        .build())
//                .build();
//    }
}