package com.my.stock.stockmanager.dto.social.kakao;

import lombok.Data;

@Data
public class KakaoProfile {
    private String nickname;
    private String thumbnail_image_url;

    private String profile_image_url;

    private boolean is_default_image;

}
