package com.meix.doublerecord.api;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import okhttp3.MediaType;
import okhttp3.RequestBody;

/**
 * @user steven
 * @createDate 2019/2/20 13:37
 * @description 自定义
 */
public class ApiRequest {
    private static JSONObject object;

    private static RequestBody getRequestBody(JSONObject object) {
        return RequestBody.create(MediaType.parse("application/json; charset=utf-8"), object.toString());
    }

    public static JSONObject getDefaultJSONObject() {
        JSONObject jsonObject = new JSONObject();
        return jsonObject;
    }

    /**
     * 双录第一步获取客户信息
     *
     * @param userCode  用户id
     * @param productId 产品唯一id
     * @return
     */
    public static Map<String, Object> getUserInfo(String userCode, String productId) {
        Map<String, Object> map = new HashMap<>();
        map.put("userCode", userCode);
        map.put("productId", productId);
        return map;
    }

    /**
     * 初始化题目
     *
     * @param unioncode 双录唯一id
     * @return
     */
    public static Map<String, Object> updateVoiceQuestions(String unioncode) {
        Map<String, Object> map = new HashMap<>();
        map.put("unioncode", unioncode);
        return map;
    }

    /**
     * 检查题目
     *
     * @param unioncode 双录唯一id
     * @return
     */
    public static Map<String, Object> checkAnswer(String unioncode, int questionId, String answerSeq) {
        Map<String, Object> map = new HashMap<>();
        map.put("unioncode", unioncode);
        map.put("questionId", questionId);
        map.put("answerSeq", answerSeq);
        return map;
    }

    /**
     * 腾讯云推流地址生成
     *
     * @param unioncode 双录唯一id
     * @return
     */
    public static Map<String, Object> getPushUrl(String unioncode) {
        Map<String, Object> map = new HashMap<>();
        map.put("unioncode", unioncode);
        return map;
    }

    /**
     * 腾讯云推流地址生成
     *
     * @param streamName        双录唯一id
     * @param questionStartTime
     * @param questionEndTime
     * @return
     */
    public static Map<String, Object> sendRecordStreamRequest(String streamName, long questionStartTime, long questionEndTime, String answerSeq) {
        Map<String, Object> map = new HashMap<>();
        map.put("answerSeq", answerSeq);
        map.put("streamName", streamName);
        map.put("questionStartTime", questionStartTime);
        map.put("questionEndTime", questionEndTime);
        return map;
    }

    /**
     * 人脸校验
     *
     * @param unioncode      双录唯一id
     * @param operatorName
     * @param operatorIdCard
     * @return
     */
    public static Map<String, Object> detectLiveFace(String unioncode, String operatorName, String operatorIdCard) {
        Map<String, Object> map = new HashMap<>();
        map.put("unioncode", unioncode);
        map.put("operatorName", operatorName);
        map.put("operatorIdCard", operatorIdCard);
        return map;
    }

    /**
     * 双录保存
     *
     * @param unioncode         双录唯一id
     * @param recordingType 双录类型：1==人人，2==人机
     * @param investorEntryTime 开始双录时间
     * @param flowId 流程id
     * @return
     */
    public static Map<String, Object> saveVideo(String unioncode, int recordingType, long investorEntryTime, String flowId) {
        Map<String, Object> map = new HashMap<>();
        map.put("unioncode", unioncode);
        map.put("recordingType", recordingType);
        map.put("investorEntryTime", investorEntryTime);
        map.put("id", flowId);
        map.put("codeValue", 2040);
        return map;
    }
}
