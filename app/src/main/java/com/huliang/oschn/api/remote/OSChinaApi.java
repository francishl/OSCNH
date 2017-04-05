package com.huliang.oschn.api.remote;

import android.text.TextUtils;

import com.huliang.oschn.api.ApiHttpClient;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;

/**
 * Created by huliang on 17/3/20.
 */
public class OSChinaApi {

    /**
     * 动弹列表
     *
     * @param aid     author id, 请求某人的动弹
     * @param tag     相关话题
     * @param type    1: 广场（所有动弹）， 2：朋友圈（好友动弹）
     * @param order   1: 最新， 2：最热
     * @param handler handler
     */
    public static void getTweetList(Long aid, String tag, Integer type, Integer order,
                                    String pageToken, TextHttpResponseHandler handler) {
        RequestParams params = new RequestParams();
        // aid, tag, type 只能三者选其一
        if (aid != null) {
            params.put("authorId", aid);
        } else if (!TextUtils.isEmpty(tag)) {
            params.put("tag", tag);
        } else {
            params.put("type", type);
        }
        params.put("order", order);
        params.put("pageToken", pageToken);
        ApiHttpClient.get("action/apiv2/tweet_list", params, handler);
    }

    /**
     * 请求动弹详情
     *
     * @param id      动弹id
     * @param handler
     */
    public static void getTweetDetail(long id, TextHttpResponseHandler handler) {
        RequestParams params = new RequestParams();
        params.put("id", id);
        ApiHttpClient.get("action/apiv2/tweet", params, handler);
    }

    /**
     * 请求动弹点赞列表
     *
     * @param sourceId 动弹id
     * @param handler  回调
     */
    public static void getTweetLikeList(long sourceId, String pageToken, TextHttpResponseHandler handler) {
        RequestParams params = new RequestParams();
        params.put("sourceId", sourceId);
        params.put("pageToken", pageToken);
        ApiHttpClient.get("action/apiv2/tweet_likes", params, handler);
    }
}
