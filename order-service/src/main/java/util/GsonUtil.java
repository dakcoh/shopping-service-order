package util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class GsonUtil {
    public static final Gson GSON = new GsonBuilder()
            .setPrettyPrinting() // 들여쓰기 적용
            .disableHtmlEscaping() // HTML escaping 비활성화
            .create();

    public static final Gson GSON_BIZ = new GsonBuilder()
            .disableHtmlEscaping() // HTML escaping 비활성화
            .create();

    // private 생성자: 인스턴스 생성 방지
    private GsonUtil() {
        throw new UnsupportedOperationException("인스턴스 생성에러");
    }
}