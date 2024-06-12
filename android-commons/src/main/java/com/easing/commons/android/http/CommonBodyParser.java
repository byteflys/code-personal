package com.easing.commons.android.http;

import com.easing.commons.android.data.JSON;
import com.easing.commons.android.data.Result;
import com.easing.commons.android.event.CommonEvents;
import com.easing.commons.android.webmessage.WebMessage;
import com.easing.commons.android.webmessage.WebMessageError;

@SuppressWarnings("all")
public class CommonBodyParser implements BodyParser<Object> {

    @Override
    public Result parse(String body) {
        WebMessage response = JSON.parse(body, WebMessage.class);
        if (response == null || response.code == null || response.code >= 300) {
            WebMessageError error = new WebMessageError();
            error.code = response.code;
            error.type = CommonEvents.RESULT_FAIL;
            Result result = Result.fail();
            result.code = response.code;
            result.data = body;
            result.error = error;
            return result;
        }
        return Result.success(Result.SUCCESS, "成功", body);
    }
}
