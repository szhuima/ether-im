package cn.ether.im.message.single.model.vo;

import cn.ether.im.common.enums.ImExceptionCode;
import cn.ether.im.common.exception.ImException;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * * @Author: Martin
 * * @Date    2024/9/15 15:10
 * * @Description
 **/
@Schema(description = "响应返回数据对象")
@Data
public class Resp {
    @Schema(
            title = "code",
            description = "响应码",
            format = "int32",
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    private Integer code;
    @Schema(
            title = "msg",
            description = "响应信息",
            accessMode = Schema.AccessMode.READ_ONLY,
            example = "success",
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    private String msg;
    @Schema(title = "data", description = "响应数据", accessMode = Schema.AccessMode.READ_ONLY)
    private Object data;


    // success 方法
    public static Resp success(Object data) {
        Resp resp = new Resp();
        resp.setCode(ImExceptionCode.SUCCESS.getCode());
        resp.setMsg("success");
        resp.setData(data);
        return resp;
    }

    // success
    public static Resp success() {
        return success(null);
    }

    // fail
    public static Resp fail(String msg) {
        Resp resp = new Resp();
        resp.setCode(ImExceptionCode.SUCCESS.getCode());
        resp.setMsg(msg);
        return resp;
    }

    public static Resp fail() {
        Resp resp = new Resp();
        resp.setCode(ImExceptionCode.SUCCESS.getCode());
        resp.setMsg("操作失败");
        return resp;
    }

    public static Resp fail(ImException exception) {
        ImExceptionCode exceptionCode = exception.getExceptionCode();
        Resp resp = new Resp();
        resp.setCode(exceptionCode.getCode());
        resp.setMsg(exceptionCode.getMsg());
        return resp;
    }

}
